const CART_KEY = "laptopshop_cart_v1";

function readCart() { try { return JSON.parse(localStorage.getItem(CART_KEY)) || [] } catch (e) { return [] } }
function saveCart(items) { localStorage.setItem(CART_KEY, JSON.stringify(items)); }
function countCart(items) { return items.reduce((s, it) => s + (Number(it.qty) || 0), 0); }

function updateBadge() {
    const badge = document.getElementById("cartBadge");
    if (badge) badge.textContent = countCart(readCart());
}

function addToCart(item) {
    const items = readCart();
    const idx = items.findIndex(x => String(x.id) === String(item.id));
    if (idx >= 0) items[idx].qty = (Number(items[idx].qty) || 0) + 1;   // bấm nhiều lần -> qty tăng
    else items.push({ id: String(item.id), name: item.name, price: Number(item.price) || 0, image: item.image, qty: 1 });
    saveCart(items);
    updateBadge();
}

document.addEventListener("DOMContentLoaded", () => {
    updateBadge();

    document.querySelectorAll("form.js-add-to-cart").forEach(form => {
        form.addEventListener("submit", (e) => {
            e.preventDefault();
            addToCart({
                id: form.dataset.id,
                name: form.dataset.name,
                price: form.dataset.price,
                image: form.dataset.image
            });
            window.location.href = "/client/cart";
        });
    });
});
