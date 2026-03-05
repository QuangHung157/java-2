<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

      <!DOCTYPE html>
      <html lang="en">

      <head>
        <meta charset="utf-8">
        <title>Checkout - LaptopShop</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport">

        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css" />
        <link href="/client/css/bootstrap.min.css" rel="stylesheet">
        <link href="/client/css/style.css" rel="stylesheet">
      </head>

      <body>
        <jsp:include page="../layout/header.jsp" />

        <div class="container-fluid py-5" style="padding-top:110px;">
          <div class="container py-4">

            <div class="mb-3">
              <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                  <li class="breadcrumb-item"><a href="/">Home</a></li>
                  <li class="breadcrumb-item active" aria-current="page">Checkout</li>
                </ol>
              </nav>
            </div>

            <!-- CART TABLE -->
            <div class="table-responsive">
              <table class="table align-middle">
                <thead>
                  <tr>
                    <th>Product</th>
                    <th>Name</th>
                    <th class="text-end">Price</th>
                    <th class="text-center">Qty</th>
                    <th class="text-end">Subtotal</th>
                  </tr>
                </thead>

                <tbody>
                  <c:forEach var="cd" items="${cartDetails}">
                    <tr>
                      <td style="width:90px;">
                        <img src="/images/product/${cd.product.image}" class="img-fluid rounded-circle"
                          style="width: 70px; height: 70px; object-fit: cover;" alt="Product">
                      </td>

                      <td>
                        <a href="/product/${cd.product.id}" target="_blank" class="text-decoration-none">
                          <c:out value="${cd.product.name}" />
                        </a>
                      </td>

                      <td class="text-end">
                        <fmt:formatNumber type="number" value="${cd.price}" /> đ
                      </td>

                      <td class="text-center">
                        <c:out value="${cd.quantity}" />
                      </td>

                      <%-- BigDecimal subtotal --%>
                        <c:set var="lineTotal" value="${cd.price.multiply(cd.quantity)}" />
                        <td class="text-end">
                          <fmt:formatNumber type="number" value="${lineTotal}" /> đ
                        </td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </div>

            <c:if test="${not empty errorMessage}">
              <div class="alert alert-danger text-center my-3">
                <c:out value="${errorMessage}" />
              </div>
            </c:if>

            <!-- CHECKOUT FORM -->
            <form action="/place-order" method="post" id="checkoutForm" novalidate>
              <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
              <input type="hidden" name="paymentMethod" value="COD" />

              <div class="row g-4 mt-2">

                <!-- LEFT: CUSTOMER -->
                <div class="col-12 col-lg-6">
                  <div class="p-4 border rounded">
                    <h5 class="mb-3">Customer Information</h5>

                    <div class="mb-3">
                      <label class="form-label">Full name <span class="text-danger">*</span></label>
                      <input class="form-control" name="receiverName" id="receiverName" required minlength="2"
                        maxlength="60" placeholder="" />
                      <div class="invalid-feedback">Please enter your full name (2–60 characters).</div>
                    </div>

                    <div class="mb-3">
                      <label class="form-label">Phone <span class="text-danger">*</span></label>
                      <input class="form-control" name="receiverPhone" id="receiverPhone" required inputmode="numeric"
                        autocomplete="tel" pattern="^[0-9]{10}$" maxlength="10" placeholder="" />
                      <div class="form-text"></div>
                      <div class="invalid-feedback">Phone must be exactly 10 digits (numbers only).</div>
                    </div>

                    <hr />

                    <h6 class="mb-2">Shipping method <span class="text-danger">*</span></h6>
                    <div class="mb-2">
                      <label class="me-4">
                        <input type="radio" name="shippingMethod" value="DELIVERY" checked>
                        Delivery to my address
                      </label>
                      <label>
                        <input type="radio" name="shippingMethod" value="PICKUP">
                        Pickup at store
                      </label>
                    </div>
                    <div class="small text-muted" id="shippingHint"></div>

                    <div class="mt-3">
                      <label class="form-label">Address (required for delivery) <span
                          class="text-danger">*</span></label>
                      <input class="form-control" name="receiverAddress" id="receiverAddress" required minlength="5"
                        maxlength="120" placeholder="Your delivery address" />
                      <div class="invalid-feedback">Please enter your address (required for delivery).</div>
                    </div>

                    <hr />

                    <h6 class="mb-2">Payment</h6>
                    <div class="mb-3">
                      <span class="badge bg-warning text-dark">COD</span>
                      <span class="text-muted ms-2">Pay on delivery / pickup</span>
                    </div>

                    <div class="mt-3">
                      <i class="fas fa-arrow-left"></i>
                      <a href="/cart">Back to cart</a>
                    </div>
                  </div>
                </div>

                <div class="col-12 col-lg-6">
                  <div class="p-4 bg-light rounded">
                    <h4 class="mb-3">Order Summary</h4>

                    <div class="d-flex justify-content-between">
                      <span>Shipping fee</span>
                      <span>0 đ</span>
                    </div>

                    <div class="d-flex justify-content-between mt-2">
                      <span>Shipping</span>
                      <span id="summaryShipping">Delivery</span>
                    </div>

                    <div class="d-flex justify-content-between mt-2">
                      <span>Payment</span>
                      <span>COD</span>
                    </div>

                    <hr />

                    <div class="d-flex justify-content-between">
                      <strong>Total</strong>
                      <strong>
                        <fmt:formatNumber type="number" value="${totalPrice}" /> đ
                      </strong>
                    </div>

                    <button class="btn btn-primary rounded-pill px-4 py-3 mt-4 w-100" type="submit">
                      PLACE ORDER
                    </button>

                    <div class="text-muted small mt-3">
                      By placing the order, you confirm your information is correct.
                    </div>
                  </div>
                </div>

              </div>
            </form>

          </div>
        </div>

        <jsp:include page="../layout/footer.jsp" />
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>

        <script>
          const form = document.getElementById('checkoutForm');
          const receiverPhone = document.getElementById('receiverPhone');
          const receiverAddress = document.getElementById('receiverAddress');
          const shippingHint = document.getElementById('shippingHint');
          const summaryShipping = document.getElementById('summaryShipping');

          function digitsOnly(str) {
            return (str || '').replace(/\D/g, '');
          }

          // Phone: keep only digits, limit 10
          receiverPhone.addEventListener('input', () => {
            receiverPhone.value = digitsOnly(receiverPhone.value).slice(0, 10);
          });

          function updateShippingUI() {
            const method = document.querySelector('input[name="shippingMethod"]:checked').value;

            if (method === 'PICKUP') {
              receiverAddress.required = false;
              receiverAddress.disabled = true;
              receiverAddress.value = '';
              receiverAddress.placeholder = "Not required for pickup";

              shippingHint.textContent = "Pickup at store. Address is not required.";
              summaryShipping.textContent = "Pickup";
            } else {
              receiverAddress.disabled = false;
              receiverAddress.required = true;
              receiverAddress.placeholder = "Your delivery address";

              shippingHint.textContent = "We will deliver to your address.";
              summaryShipping.textContent = "Delivery";
            }
          }

          document.querySelectorAll('input[name="shippingMethod"]').forEach(r => {
            r.addEventListener('change', updateShippingUI);
          });
          updateShippingUI();

          // Bootstrap validation
          form.addEventListener('submit', (e) => {
            updateShippingUI();

            if (!form.checkValidity()) {
              e.preventDefault();
              e.stopPropagation();
            }
            form.classList.add('was-validated');
          });
        </script>
      </body>

      </html>