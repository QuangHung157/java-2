<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

      <!DOCTYPE html>
      <html lang="en">

      <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Thank you - LaptopShop</title>

        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css" />
        <link href="/client/css/bootstrap.min.css" rel="stylesheet" />
        <link href="/client/css/style.css" rel="stylesheet" />
      </head>

      <body>
        <jsp:include page="../layout/header.jsp" />

        <main style="padding-top:110px;">
          <div class="container py-4">

            <c:if test="${not empty errorMessage}">
              <div class="alert alert-danger text-center my-3">
                <c:out value="${errorMessage}" />
              </div>
            </c:if>

            <div class="text-center mb-4">
              <h2 class="mb-2">Thank you! Your order has been placed.</h2>
              <div class="text-muted">
                Order ID: <b>#
                  <c:out value="${order.id}" />
                </b>
              </div>
            </div>

            <div class="row g-4">
              <div class="col-12 col-lg-6">
                <div class="p-4 border rounded">
                  <h5 class="mb-3">Order Information</h5>

                  <div class="mb-2"><b>Status:</b>
                    <c:out value="${order.status}" />
                  </div>
                  <div class="mb-2"><b>Shipping:</b>
                    <c:out value="${order.shippingMethod}" />
                  </div>
                  <div class="mb-2"><b>Payment:</b> COD</div>
                  <div class="mb-2"><b>Payment status:</b>
                    <c:out value="${order.paymentStatus}" />
                  </div>

                  <hr />

                  <h6 class="mb-2">Receiver</h6>
                  <div class="mb-1"><b>Name:</b>
                    <c:out value="${order.receiverName}" />
                  </div>
                  <div class="mb-1"><b>Phone:</b>
                    <c:out value="${order.receiverPhone}" />
                  </div>
                  <c:if test="${order.shippingMethod != 'PICKUP'}">
                    <div class="mb-1"><b>Address:</b>
                      <c:out value="${order.receiverAddress}" />
                    </div>
                  </c:if>

                  <hr />
                  <div class="d-flex justify-content-between">
                    <span class="fw-bold">Total</span>
                    <span class="fw-bold">
                      <fmt:formatNumber type="number" value="${order.totalPrice}" /> đ
                    </span>
                  </div>

                  <div class="mt-3">
                    <a href="/history" class="btn btn-outline-primary">
                      View order history
                    </a>
                    <a href="/products" class="btn btn-primary ms-2">
                      Continue shopping
                    </a>
                  </div>

                </div>
              </div>

              <div class="col-12 col-lg-6">
                <div class="p-4 bg-light rounded">
                  <h5 class="mb-3">Items</h5>

                  <div class="table-responsive">
                    <table class="table align-middle">
                      <thead>
                        <tr>
                          <th>Product</th>
                          <th class="text-end">Price</th>
                          <th class="text-center">Qty</th>
                          <th class="text-end">Subtotal</th>
                        </tr>
                      </thead>
                      <tbody>
                        <c:forEach var="d" items="${orderDetails}">
                          <c:set var="sub" value="${d.price.multiply(d.quantity)}" />
                          <tr>
                            <td>
                              <a href="/product/${d.product.id}" class="text-decoration-none">
                                <c:out value="${d.product.name}" />
                              </a>
                            </td>
                            <td class="text-end">
                              <fmt:formatNumber type="number" value="${d.price}" /> đ
                            </td>
                            <td class="text-center">
                              <c:out value="${d.quantity}" />
                            </td>
                            <td class="text-end">
                              <fmt:formatNumber type="number" value="${sub}" /> đ
                            </td>
                          </tr>
                        </c:forEach>
                      </tbody>
                    </table>
                  </div>

                  <div class="text-muted small">
                    If you chose Pickup, please come to our store and show Order ID to receive your items.
                  </div>
                </div>
              </div>
            </div>

          </div>
        </main>

        <jsp:include page="../layout/footer.jsp" />
      </body>

      </html>