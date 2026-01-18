package com.example.laptop.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.laptop.domain.Cart;
import com.example.laptop.domain.CartDetail;
import com.example.laptop.domain.Product;
import com.example.laptop.domain.User;
import com.example.laptop.repository.CartDetailRepository;
import com.example.laptop.repository.CartRepository;
import com.example.laptop.repository.ProductRepository;
import com.example.laptop.repository.UserRepository;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository,
            CartDetailRepository cartDetailRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Cart getOrCreateCartByEmail(String email) {
        String normalizedEmail = email == null ? "" : email.trim().toLowerCase();

        User user = userRepository.findFirstByEmailOrderByIdAsc(normalizedEmail).orElse(null);
        if (user == null)
            return null;

        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    cart.setSum(0);
                    return cartRepository.save(cart);
                });
    }

    @Transactional
    public void addToCartByEmail(String email, long productId, long quantity) {
        Cart cart = getOrCreateCartByEmail(email);
        if (cart == null)
            return;

        if (quantity <= 0)
            quantity = 1;

        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty())
            return;

        Product product = productOpt.get();
        long stock = product.getQuantity();

        // ✅ hết hàng => không add
        if (stock <= 0)
            return;

        CartDetail cd = cartDetailRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElse(null);

        long currentQty = (cd == null) ? 0 : cd.getQuantity();
        long requestQty = currentQty + quantity;

        if (requestQty > stock)
            requestQty = stock;
        if (requestQty < 1)
            return;

        if (cd == null) {
            cd = new CartDetail();
            cd.setCart(cart);
            cd.setProduct(product);
            cd.setPrice(product.getPrice()); // BigDecimal
            cd.setQuantity(requestQty);
        } else {
            cd.setQuantity(requestQty);
        }

        cartDetailRepository.save(cd);
        recalcSum(cart);
    }

    @Transactional
    public void updateQuantityByEmail(String email, long productId, long quantity) {
        Cart cart = getOrCreateCartByEmail(email);
        if (cart == null)
            return;

        CartDetail cd = cartDetailRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElse(null);

        if (cd == null)
            return;

        long stock = cd.getProduct().getQuantity();

        if (quantity <= 0) {
            cartDetailRepository.delete(cd);
        } else if (stock <= 0) {
            cartDetailRepository.delete(cd);
        } else if (quantity > stock) {
            cd.setQuantity(stock);
            cartDetailRepository.save(cd);
        } else {
            cd.setQuantity(quantity);
            cartDetailRepository.save(cd);
        }

        recalcSum(cart);
    }

    @Transactional
    public void removeByProductByEmail(String email, long productId) {
        Cart cart = getOrCreateCartByEmail(email);
        if (cart == null)
            return;

        CartDetail cd = cartDetailRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElse(null);

        if (cd != null) {
            cartDetailRepository.delete(cd);
            recalcSum(cart);
        }
    }

    @Transactional
    public void clearCartByEmail(String email) {
        Cart cart = getOrCreateCartByEmail(email);
        if (cart == null)
            return;

        List<CartDetail> details = cartDetailRepository.findByCartId(cart.getId());
        if (!details.isEmpty())
            cartDetailRepository.deleteAll(details);

        cart.setSum(0);
        cartRepository.save(cart);
    }

    public List<CartDetail> getCartDetailsByEmail(String email) {
        Cart cart = getOrCreateCartByEmail(email);
        if (cart == null)
            return List.of();
        return cartDetailRepository.findByCartId(cart.getId());
    }

    // ✅ BigDecimal total
    public BigDecimal calcTotalPrice(List<CartDetail> cartDetails) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartDetail d : cartDetails) {
            total = total.add(d.getPrice().multiply(BigDecimal.valueOf(d.getQuantity())));
        }
        return total;
    }

    public int getSumByEmail(String email) {
        Cart cart = getOrCreateCartByEmail(email);
        return cart == null ? 0 : cart.getSum();
    }

    @Transactional
    private void recalcSum(Cart cart) {
        List<CartDetail> details = cartDetailRepository.findByCartId(cart.getId());

        int sum = 0;
        for (CartDetail d : details)
            sum += (int) d.getQuantity();

        cart.setSum(sum);
        cartRepository.save(cart);
    }
}
