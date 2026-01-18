<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="utf-8" />
                <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
                <meta name="description" content="laptopshop" />
                <meta name="author" content="Hung" />
                <title>Order detail</title>

                <link href="/css/style.css" rel="stylesheet" />
                <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
            </head>

            <body class="sb-nav-fixed">
                <jsp:include page="../layout/header.jsp" />
                <div id="layoutSidenav">
                    <jsp:include page="../layout/sidebar.jsp" />
                    <div id="layoutSidenav_content">
                        <main>
                            <div class="container-fluid px-4">
                                <h1 class="mt-4">Manage Orders</h1>
                                <ol class="breadcrumb mb-4">
                                    <li class="breadcrumb-item"><a href="/admin">Dashboard</a></li>
                                    <li class="breadcrumb-item"><a href="/admin/order">Orders</a></li>
                                    <li class="breadcrumb-item active">Detail ID = ${order.id}</li>
                                </ol>

                                <div class="container mt-4">
                                    <div class="row">
                                        <div class="col-12 mx-auto">
                                            <div class="d-flex justify-content-between">
                                                <h3>Order detail ID = ${order.id}</h3>
                                            </div>
                                            <hr />

                                            <div class="card" style="width: 70%;">
                                                <div class="card-header">
                                                    Receiver information
                                                </div>
                                                <ul class="list-group list-group-flush">
                                                    <li class="list-group-item">Receiver name: ${order.receiverName}
                                                    </li>
                                                    <li class="list-group-item">Receiver phone: ${order.receiverPhone}
                                                    </li>
                                                    <li class="list-group-item">Receiver address:
                                                        ${order.receiverAddress}</li>
                                                    <li class="list-group-item">Status: <b>${order.status}</b></li>
                                                    <li class="list-group-item">Total:
                                                        <b>
                                                            <fmt:formatNumber type="number"
                                                                value="${order.totalPrice}" /> đ
                                                        </b>
                                                    </li>
                                                </ul>
                                            </div>

                                            <div class="mt-4">
                                                <h4>Order items</h4>
                                                <table class="table table-bordered table-hover">
                                                    <thead>
                                                        <tr>
                                                            <th>Product</th>
                                                            <th>Price</th>
                                                            <th>Qty</th>
                                                            <th>Subtotal</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach var="d" items="${details}">
                                                            <tr>
                                                                <td>${d.product.name}</td>
                                                                <td>
                                                                    <fmt:formatNumber type="number"
                                                                        value="${d.price}" /> đ
                                                                </td>
                                                                <td>${d.quantity}</td>
                                                                <td>
                                                                    <fmt:formatNumber type="number"
                                                                        value="${d.price * d.quantity}" /> đ
                                                                </td>
                                                            </tr>
                                                        </c:forEach>

                                                        <c:if test="${empty details}">
                                                            <tr>
                                                                <td colspan="4">No items found.</td>
                                                            </tr>
                                                        </c:if>
                                                    </tbody>
                                                </table>
                                            </div>

                                            <a href="/admin/order" class="btn btn-success mt-2">Back</a>
                                            <a href="/admin/order/update/${order.id}"
                                                class="btn btn-warning mt-2 ms-2">Update</a>
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </main>

                        <jsp:include page="../layout/footer.jsp" />
                    </div>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
                    crossorigin="anonymous"></script>
                <script src="/js/scripts.js"></script>
            </body>

            </html>