<%@page contentType="text/html" pageEncoding="UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

      <!DOCTYPE html>
      <html lang="en">

      <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Cart - LaptopShop</title>

        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
        <link
          href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600&family=Raleway:wght@600;800&display=swap"
          rel="stylesheet" />

        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css" />
        <link href="/client/css/bootstrap.min.css" rel="stylesheet" />
        <link href="/client/css/style.css" rel="stylesheet" />

        <style>
          .cart-topline {
            margin-top: 110px;
          }

          .cart-item-row img {
            width: 60px;
            height: 60px;
            object-fit: cover;
            border-radius: 14px;
            border: 1px solid #eee;
          }

          .cart-card {
            border-radius: 18px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, .06);
            border: 0;
          }

          .muted {
            color: #7b8794;
          }

          .qty-input {
            width: 80px;
            border-radius: 14px;
            text-align: center;
          }

          .pay-box {
            border-radius: 18px;
            background: #f6f7f9;
          }

          .pay-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 0;
            border-bottom: 1px solid rgba(0, 0, 0, .06);
          }

          .pay-row:last-child {
            border-bottom: 0;
          }
        </style>
      </head>

      <body>
        <jsp:include page="../layout/header.jsp" />

        <div class="container cart-topline">
          <c:choose>
            <c:when test="${empty cartDetails}">
              <div class="text-center py-5">
                <h3 class="mb-2">Your cart is empty</h3>
                <p class="muted mb-4">Add some laptops to see them here.</p>
                <a href="/" class="btn btn-primary rounded-pill px-4">Back to Home</a>
              </div>
            </c:when>

            <c:otherwise>
              <div class="card cart-card mb-4">
                <div class="card-body">
                  <div class="table-responsive">
                    <table class="table align-middle mb-0">
                      <thead>
                        <tr class="muted">
                          <th style="width:90px;">Item</th>
                          <th>Product</th>
                          <th style="width:160px;">Price</th>
                          <th style="width:140px;" class="text-center">Quantity</th>
                          <th style="width:180px;">Subtotal</th>
                          <th style="width:90px;"></th>
                        </tr>
                      </thead>
                      <tbody>
                        <c:forEach var="cd" items="${cartDetails}">
                          <tr class="cart-item-row">
                            <td>
                              <img src="/images/product/${cd.product.image}" alt="Product" />
                            </td>
                            <td>
                              <a href="/product/${cd.product.id}" class="fw-bold text-primary"
                                style="text-decoration:none;">
                                ${cd.product.name}
                              </a>
                              <div class="muted" style="font-size:13px;">${cd.product.shortDesc}</div>
                            </td>

                            <td class="fw-bold">
                              <fmt:formatNumber type="number" value="${cd.price}" /> đ
                            </td>

                            <td class="align-middle">
                              <form action="/cart/update" method="post" style="margin: 0;">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                <input type="hidden" name="productId" value="${cd.product.id}" />

                                <%-- ✅ min=0 để user nhập 0 sẽ remove (controller đã hỗ trợ) --%>
                                  <input class="form-control qty-input mx-auto" type="number" min="0"
                                    max="${cd.product.quantity}" name="quantity" value="${cd.quantity}"
                                    onchange="this.form.submit()" />
                              </form>

                              <c:choose>
                                <%-- Nếu số lượng mua đã chạm trần tồn kho --%>
                                  <c:when test="${cd.quantity >= cd.product.quantity}">
                                    <div class="small text-danger text-center mt-1" style="font-size: 11px;">
                                      Only ${cd.product.quantity} items left
                                    </div>
                                  </c:when>

                                  <%-- Còn hàng thoải mái --%>
                                    <c:otherwise>
                                      <div class="small text-success text-center mt-1" style="font-size: 11px;">
                                        In Stock
                                      </div>
                                    </c:otherwise>
                              </c:choose>
                            </td>

                            <%-- ✅ BigDecimal subtotal: price.multiply(quantity) --%>
                              <c:set var="lineTotal" value="${cd.price.multiply(cd.quantity)}" />
                              <td class="fw-bold">
                                <fmt:formatNumber type="number" value="${lineTotal}" /> đ
                              </td>

                              <td>
                                <form method="post" action="/cart/remove">
                                  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                  <input type="hidden" name="productId" value="${cd.product.id}" />
                                  <button type="submit" class="btn btn-outline-danger btn-sm rounded-pill px-3">
                                    Remove
                                  </button>
                                </form>
                              </td>
                          </tr>
                        </c:forEach>
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>

              <div class="row g-4 pb-5">
                <div class="col-lg-6">
                  <div class="card cart-card pay-box">
                    <div class="card-body p-4">
                      <h3 class="mb-4">Payment Information</h3>
                      <div class="pay-row">
                        <div class="fw-bold">Shipping fee</div>
                        <div class="muted">0 đ</div>
                      </div>
                      <div class="pay-row">
                        <div class="fw-bold">Payment method</div>
                        <div class="muted">Cash on Delivery (COD)</div>
                      </div>
                      <div class="pay-row mt-2">
                        <div class="fw-bold">Total</div>
                        <div class="fw-bold text-dark">
                          <fmt:formatNumber type="number" value="${totalPrice}" /> đ
                        </div>
                      </div>
                      <a href="/checkout" class="btn btn-primary rounded-pill px-5 py-2 mt-4">
                        CONFIRM PAYMENT
                      </a>
                    </div>
                  </div>
                </div>

                <div class="col-lg-6">
                  <div class="card cart-card">
                    <div class="card-body p-4">
                      <h4 class="mb-3">Tips</h4>
                      <p class="muted mb-0">
                        Change the quantity (Qty) to auto-update.
                        <br>If you set it to 0, the item will be removed.
                        <br>System will limit quantity based on available stock.
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </c:otherwise>
          </c:choose>
        </div>

        <jsp:include page="../layout/footer.jsp" />
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
      </body>

      </html>