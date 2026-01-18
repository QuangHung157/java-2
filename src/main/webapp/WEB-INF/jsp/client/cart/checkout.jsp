<%@page contentType="text/html" pageEncoding="UTF-8" %>
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

        <div class="container-fluid py-5">
          <div class="container py-5">

            <div class="mb-3">
              <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                  <li class="breadcrumb-item"><a href="/">Home</a></li>
                  <li class="breadcrumb-item active" aria-current="page">Checkout</li>
                </ol>
              </nav>
            </div>

            <div class="table-responsive">
              <table class="table">
                <thead>
                  <tr>
                    <th>Product</th>
                    <th>Name</th>
                    <th>Price</th>
                    <th>Qty</th>
                    <th>Subtotal</th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach var="cd" items="${cartDetails}">
                    <tr>
                      <td>
                        <img src="/images/product/${cd.product.image}" class="img-fluid rounded-circle"
                          style="width: 70px; height: 70px;" alt="Product">
                      </td>
                      <td>
                        <a href="/product/${cd.product.id}" target="_blank">${cd.product.name}</a>
                      </td>
                      <td>
                        <fmt:formatNumber type="number" value="${cd.price}" /> đ
                      </td>
                      <td>${cd.quantity}</td>

                      <%-- ✅ BigDecimal subtotal --%>
                        <c:set var="lineTotal" value="${cd.price.multiply(cd.quantity)}" />
                        <td>
                          <fmt:formatNumber type="number" value="${lineTotal}" /> đ
                        </td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </div>

            <c:if test="${not empty errorMessage}">
              <div class="alert alert-danger text-center my-3">${errorMessage}</div>
            </c:if>

            <form action="/place-order" method="post">
              <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

              <div class="row g-4 mt-3">

                <div class="col-12 col-md-6">
                  <div class="p-4 border rounded">
                    <h5>Customer Information</h5>

                    <div class="mb-3">
                      <label class="form-label">Full name</label>
                      <input class="form-control" name="receiverName" required />
                    </div>

                    <div class="mb-3">
                      <label class="form-label">Phone</label>
                      <input class="form-control" name="receiverPhone" required />
                    </div>

                    <div class="mb-3">
                      <label class="form-label">Address (for delivery)</label>
                      <input class="form-control" name="receiverAddress" id="receiverAddress" required />
                    </div>

                    <hr />

                    <h6>Shipping method</h6>
                    <div class="mb-3">
                      <label class="me-4">
                        <input type="radio" name="shippingMethod" value="DELIVERY" checked>
                        Delivery to my address
                      </label>
                      <label>
                        <input type="radio" name="shippingMethod" value="PICKUP">
                        Pickup at store
                      </label>
                      <div class="small text-muted mt-1" id="shippingHint"></div>
                    </div>

                    <hr />

                    <h6>Payment method</h6>
                    <div class="mb-3">
                      <label class="me-4">
                        <input type="radio" name="paymentMethod" value="COD" checked>
                        Pay on delivery / pickup (COD)
                      </label>
                      <label>
                        <input type="radio" name="paymentMethod" value="VNPAY">
                        Online payment (VNPAY QR)
                      </label>
                    </div>

                    <div class="mt-3">
                      <i class="fas fa-arrow-left"></i>
                      <a href="/cart">Back to cart</a>
                    </div>
                  </div>
                </div>

                <div class="col-12 col-md-6">
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
                      <span id="summaryPayment">COD</span>
                    </div>

                    <hr />

                    <div class="d-flex justify-content-between">
                      <strong>Total</strong>
                      <strong>
                        <fmt:formatNumber type="number" value="${totalPrice}" /> đ
                      </strong>
                    </div>

                    <button class="btn btn-primary rounded-pill px-4 py-3 mt-4" type="submit">
                      PLACE ORDER
                    </button>
                  </div>
                </div>

              </div>
            </form>

          </div>
        </div>

        <jsp:include page="../layout/footer.jsp" />
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>

        <script>
          const receiverAddress = document.getElementById('receiverAddress');
          const shippingHint = document.getElementById('shippingHint');
          const summaryShipping = document.getElementById('summaryShipping');
          const summaryPayment = document.getElementById('summaryPayment');

          function updateShipping() {
            const method = document.querySelector('input[name="shippingMethod"]:checked').value;
            if (method === 'PICKUP') {
              receiverAddress.required = false;
              receiverAddress.placeholder = "Not required for pickup";
              shippingHint.textContent = "You will pickup at store. Address is not required.";
              summaryShipping.textContent = "Pickup";
            } else {
              receiverAddress.required = true;
              receiverAddress.placeholder = "";
              shippingHint.textContent = "We will deliver to your address.";
              summaryShipping.textContent = "Delivery";
            }
          }

          function updatePayment() {
            const method = document.querySelector('input[name="paymentMethod"]:checked').value;
            summaryPayment.textContent = (method === 'VNPAY') ? "VNPAY" : "COD";
          }

          document.querySelectorAll('input[name="shippingMethod"]').forEach(r => r.addEventListener('change', updateShipping));
          document.querySelectorAll('input[name="paymentMethod"]').forEach(r => r.addEventListener('change', updatePayment));

          updateShipping();
          updatePayment();
        </script>
      </body>

      </html>