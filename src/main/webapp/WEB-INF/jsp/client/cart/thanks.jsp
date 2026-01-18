<%@page contentType="text/html" pageEncoding="UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

      <!DOCTYPE html>
      <html lang="en">

      <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Thank You - LaptopShop</title>

        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
        <link
          href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600&family=Raleway:wght@600;800&display=swap"
          rel="stylesheet" />

        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet" />

        <link href="/client/lib/lightbox/css/lightbox.min.css" rel="stylesheet" />
        <link href="/client/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet" />

        <link href="/client/css/bootstrap.min.css" rel="stylesheet" />
        <link href="/client/css/style.css" rel="stylesheet" />

        <style>
          .bill-header {
            background-color: #f8f9fa;
            border-bottom: 2px solid #81c408;
            padding: 15px 20px;
            border-radius: 10px 10px 0 0;
            font-weight: bold;
            color: #45595b;
          }

          .bill-body {
            background-color: #fff;
            padding: 20px;
            border: 1px solid #dee2e6;
            border-top: none;
            border-radius: 0 0 10px 10px;
          }

          .order-info-label {
            color: #747d88;
            font-size: 0.9rem;
          }
        </style>
      </head>

      <body>

        <div id="spinner"
          class="show w-100 vh-100 bg-white position-fixed translate-middle top-50 start-50 d-flex align-items-center justify-content-center">
          <div class="spinner-grow text-primary" role="status"></div>
        </div>

        <jsp:include page="../layout/header.jsp" />

        <div class="container" style="margin-top: 100px; margin-bottom: 50px;">

          <div class="text-center mb-5">
            <c:choose>
              <c:when test="${order.paymentMethod == 'VNPAY' && order.paymentStatus == 'PAID'}">
                <div class="alert alert-success d-inline-block px-5 py-3 rounded-pill shadow-sm" role="alert">
                  <i class="fas fa-check-circle me-2"></i>
                  Payment successful! Your order has been placed.
                </div>
              </c:when>

              <c:when test="${order.paymentMethod == 'VNPAY' && order.paymentStatus == 'FAILED'}">
                <div class="alert alert-danger d-inline-block px-5 py-3 rounded-pill shadow-sm" role="alert">
                  <i class="fas fa-times-circle me-2"></i>
                  Payment failed! Please try again.
                </div>
              </c:when>

              <%-- ✅ thêm trạng thái chờ xác nhận để giống production (Return có thể về trước IPN) --%>
                <c:when test="${order.paymentMethod == 'VNPAY' && order.paymentStatus == 'PENDING'}">
                  <div class="alert alert-warning d-inline-block px-5 py-3 rounded-pill shadow-sm" role="alert">
                    <i class="fas fa-spinner me-2"></i>
                    Waiting for payment confirmation...
                  </div>
                </c:when>

                <c:otherwise>
                  <div class="alert alert-success d-inline-block px-5 py-3 rounded-pill shadow-sm" role="alert">
                    <i class="fas fa-check-circle me-2"></i>
                    Thank you! Your order has been placed successfully.
                  </div>
                </c:otherwise>
            </c:choose>
          </div>

          <div class="row g-4">

            <div class="col-lg-4 col-md-12">
              <div class="bill-header">
                <i class="fas fa-user-check me-2"></i> Receiver Information
              </div>
              <div class="bill-body">
                <div class="mb-3 border-bottom pb-2">
                  <span class="order-info-label">Order ID:</span>
                  <span class="fw-bold text-primary float-end">#${order.id}</span>
                </div>
                <div class="mb-3">
                  <span class="order-info-label">Name:</span>
                  <span class="float-end fw-bold">${order.receiverName}</span>
                </div>
                <div class="mb-3">
                  <span class="order-info-label">Phone:</span>
                  <span class="float-end fw-bold">${order.receiverPhone}</span>
                </div>

                <!-- ✅ Payment Method + Status -->
                <div class="mb-3">
                  <span class="order-info-label">Payment:</span>

                  <c:choose>
                    <c:when test="${order.paymentMethod == 'VNPAY'}">
                      <span class="float-end badge bg-primary">VNPAY</span>
                    </c:when>
                    <c:otherwise>
                      <span class="float-end badge bg-warning text-dark">COD</span>
                    </c:otherwise>
                  </c:choose>
                </div>

                <div class="mb-3">
                  <span class="order-info-label">Payment status:</span>
                  <c:choose>
                    <c:when test="${order.paymentStatus == 'PAID'}">
                      <span class="float-end badge bg-success">PAID</span>
                    </c:when>
                    <c:when test="${order.paymentStatus == 'FAILED'}">
                      <span class="float-end badge bg-danger">FAILED</span>
                    </c:when>
                    <c:otherwise>
                      <span class="float-end badge bg-secondary">PENDING</span>
                    </c:otherwise>
                  </c:choose>
                </div>

                <c:if test="${not empty order.vnpTransactionNo}">
                  <div class="mb-3">
                    <span class="order-info-label">VNPAY Transaction:</span>
                    <span class="float-end fw-bold">${order.vnpTransactionNo}</span>
                  </div>
                </c:if>

                <div>
                  <span class="order-info-label d-block mb-1">Address:</span>
                  <span class="fw-bold" style="font-size: 0.95rem;">${order.receiverAddress}</span>
                </div>
              </div>

              <div class="mt-4 text-center">
                <a href="/" class="btn btn-primary border-secondary rounded-pill py-3 px-5 text-white">
                  <i class="fas fa-arrow-left me-2"></i> Continue Shopping
                </a>
              </div>
            </div>

            <div class="col-lg-8 col-md-12">
              <div class="bill-header">
                <i class="fas fa-shopping-bag me-2"></i> Order Summary
              </div>
              <div class="bill-body p-0">
                <div class="table-responsive">
                  <table class="table table-hover mb-0">
                    <thead class="table-light">
                      <tr>
                        <th scope="col" class="ps-4">Product</th>
                        <th scope="col" class="text-center">Price</th>
                        <th scope="col" class="text-center">Qty</th>
                        <th scope="col" class="text-end pe-4">Total</th>
                      </tr>
                    </thead>
                    <tbody>
                      <c:forEach var="detail" items="${orderDetails}">
                        <tr>
                          <td class="ps-4">
                            <div class="d-flex align-items-center">
                              <img src="/images/product/${detail.product.image}"
                                style="width: 50px; height: 50px; object-fit: cover; border-radius: 8px; border: 1px solid #eee;"
                                class="me-3" alt="${detail.product.name}">
                              <div>
                                <h6 class="mb-0 text-dark" style="font-size: 0.95rem;">${detail.product.name}</h6>
                              </div>
                            </div>
                          </td>

                          <td class="text-center align-middle">
                            <fmt:formatNumber value="${detail.price}" type="currency" currencySymbol="đ" />
                          </td>

                          <td class="text-center align-middle">x ${detail.quantity}</td>

                          <%-- ✅ BigDecimal: price.multiply(quantity) --%>
                            <c:set var="rowTotal" value="${detail.price.multiply(detail.quantity)}" />
                            <td class="text-end pe-4 align-middle fw-bold">
                              <fmt:formatNumber value="${rowTotal}" type="currency" currencySymbol="đ" />
                            </td>
                        </tr>
                      </c:forEach>
                    </tbody>

                    <tfoot class="bg-light">
                      <tr>
                        <td colspan="3" class="text-end fw-bold pt-3 pb-3">TOTAL AMOUNT</td>
                        <td class="text-end pe-4 fw-bold text-danger pt-3 pb-3" style="font-size: 1.2rem;">
                          <fmt:formatNumber value="${order.totalPrice}" type="currency" currencySymbol="đ" />
                        </td>
                      </tr>
                    </tfoot>
                  </table>
                </div>
              </div>
            </div>

          </div>
        </div>

        <jsp:include page="../layout/feature.jsp" />
        <jsp:include page="../layout/footer.jsp" />

        <a href="#" class="btn btn-primary border-3 border-primary rounded-circle back-to-top">
          <i class="fa fa-arrow-up"></i>
        </a>

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="/client/lib/easing/easing.min.js"></script>
        <script src="/client/lib/waypoints/waypoints.min.js"></script>
        <script src="/client/lib/lightbox/js/lightbox.min.js"></script>
        <script src="/client/lib/owlcarousel/owl.carousel.min.js"></script>
        <script src="/client/js/main.js"></script>
      </body>

      </html>