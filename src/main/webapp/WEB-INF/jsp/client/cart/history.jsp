<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <title>Order History</title>
                <link href="/client/css/bootstrap.min.css" rel="stylesheet">
            </head>

            <body>
                <jsp:include page="../layout/header.jsp" />

                <div class="container py-5" style="margin-top: 50px;">
                    <h3 class="mb-4">My Order History</h3>

                    <c:if test="${empty orders}">
                        <div class="alert alert-info">You haven't placed any orders yet.</div>
                    </c:if>

                    <c:if test="${not empty orders}">
                        <table class="table table-bordered table-hover">
                            <thead class="table-light">
                                <tr>
                                    <th>Order ID</th>
                                    <th>Date</th>
                                    <th>Total Price</th>
                                    <th>Status</th>
                                    <th>Receiver</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="order" items="${orders}">
                                    <tr>
                                        <td>#${order.id}</td>
                                        <td>${order.createdAt}</td>
                                        <td>
                                            <fmt:formatNumber value="${order.totalPrice}" type="currency"
                                                currencySymbol="đ" />
                                        </td>
                                        <td>
                                            <span
                                                class="badge ${order.status == 'PENDING' ? 'bg-warning' : 'bg-success'}">
                                                ${order.status}
                                            </span>
                                        </td>
                                        <td>${order.receiverName} - ${order.receiverPhone}</td>
                                        <td>
                                            <a href="#" class="btn btn-sm btn-primary">View Detail</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:if>
                </div>

                <jsp:include page="../layout/footer.jsp" />
            </body>

            </html>