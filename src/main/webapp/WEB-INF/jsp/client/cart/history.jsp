<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1" />
                <title>Order History - LaptopShop</title>

                <link rel="preconnect" href="https://fonts.googleapis.com" />
                <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
                <link
                    href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600&family=Raleway:wght@600;800&display=swap"
                    rel="stylesheet" />

                <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css" />
                <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css"
                    rel="stylesheet" />

                <link href="/client/css/bootstrap.min.css" rel="stylesheet" />
                <link href="/client/css/style.css" rel="stylesheet" />
            </head>

            <body>
                <jsp:include page="../layout/header.jsp" />

                <main style="padding-top:110px;">
                    <div class="container py-4">

                        <div class="mb-4">
                            <a href="/" class="text-decoration-none">Home</a>
                            <span class="text-muted"> / Order History</span>
                        </div>

                        <div class="d-flex align-items-center justify-content-between mb-3 flex-wrap">
                            <h2 class="mb-0">Order History</h2>
                            <div class="text-muted small mt-2 mt-md-0">
                                Logged in as:
                                <b>
                                    <c:out value="${pageContext.request.userPrincipal.name}" />
                                </b>
                            </div>
                        </div>

                        <c:if test="${empty orders}">
                            <div class="alert alert-info">
                                You have no orders yet. <a href="/products">Start shopping</a>.
                            </div>
                        </c:if>

                        <c:forEach var="o" items="${orders}">
                            <div class="card mb-4 shadow-sm">

                                <div class="card-header bg-white">
                                    <div class="d-flex flex-wrap justify-content-between align-items-center">
                                        <div>
                                            <div class="fw-bold">Order #
                                                <c:out value="${o.id}" />
                                            </div>
                                            <div class="text-muted small">
                                                Created at:
                                                <c:out value="${o.createdAt}" />
                                            </div>
                                        </div>

                                        <div class="text-end mt-2 mt-md-0">
                                            <div class="mb-1">

                                                <!-- Status badge -->
                                                <c:choose>
                                                    <c:when test="${o.status == 'NEW'}">
                                                        <span class="badge bg-secondary">NEW</span>
                                                    </c:when>
                                                    <c:when test="${o.status == 'CONFIRMED'}">
                                                        <span class="badge bg-primary">CONFIRMED</span>
                                                    </c:when>
                                                    <c:when test="${o.status == 'SHIPPING'}">
                                                        <span class="badge bg-info text-dark">SHIPPING</span>
                                                    </c:when>
                                                    <c:when test="${o.status == 'DONE'}">
                                                        <span class="badge bg-success">DONE</span>
                                                    </c:when>
                                                    <c:when test="${o.status == 'CANCELED'}">
                                                        <span class="badge bg-danger">CANCELED</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-dark">
                                                            <c:out value="${o.status}" />
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>

                                                <!-- Payment status (COD only) -->
                                                <c:choose>
                                                    <c:when test="${o.paymentStatus == 'PAID'}">
                                                        <span class="badge bg-success ms-2">PAID</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-warning text-dark ms-2">UNPAID</span>
                                                    </c:otherwise>
                                                </c:choose>

                                                <!-- Payment method -->
                                                <span class="badge bg-warning text-dark ms-2">COD</span>
                                            </div>

                                            <div class="fw-bold">
                                                Total:
                                                <fmt:formatNumber type="number" value="${o.totalPrice}" /> đ
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="card-body p-0">
                                    <table class="table mb-0 table-hover align-middle">
                                        <thead class="table-light">
                                            <tr>
                                                <th style="width: 80px;">Image</th>
                                                <th>Product</th>
                                                <th style="width: 160px;">Price</th>
                                                <th style="width: 120px;">Qty</th>
                                            </tr>
                                        </thead>

                                        <tbody>
                                            <c:forEach var="d" items="${o.orderDetails}">
                                                <tr>
                                                    <td>
                                                        <img src="/images/product/${d.product.image}" alt="product"
                                                            style="width:60px;height:60px;object-fit:cover;border-radius:8px;" />
                                                    </td>

                                                    <td>
                                                        <a href="/product/${d.product.id}" class="text-decoration-none">
                                                            <c:out value="${d.product.name}" />
                                                        </a>
                                                        <div class="text-muted small">
                                                            Factory:
                                                            <c:out value="${d.product.factory}" /> |
                                                            Target:
                                                            <c:out value="${d.product.target}" />
                                                        </div>
                                                    </td>

                                                    <td>
                                                        <fmt:formatNumber type="number" value="${d.price}" /> đ
                                                    </td>

                                                    <td>
                                                        <c:out value="${d.quantity}" />
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>

                                <div
                                    class="card-footer bg-white d-flex justify-content-between align-items-center flex-wrap">
                                    <div class="text-muted small">
                                        Receiver:
                                        <c:out value="${o.receiverName}" /> |
                                        Phone:
                                        <c:out value="${o.receiverPhone}" />
                                    </div>

                                    <div class="text-muted small mt-2 mt-md-0">
                                        Shipping:
                                        <c:out value="${o.shippingMethod}" />
                                    </div>
                                </div>

                            </div>
                        </c:forEach>

                    </div>
                </main>

                <jsp:include page="../layout/footer.jsp" />
            </body>

            </html>