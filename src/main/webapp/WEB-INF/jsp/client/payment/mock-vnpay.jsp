<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8" />
            <title>Mock VNPAY - LaptopShop</title>
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />

            <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css" />
            <link href="/client/css/bootstrap.min.css" rel="stylesheet">
            <link href="/client/css/style.css" rel="stylesheet">
        </head>

        <body>

            <jsp:include page="../layout/header.jsp" />

            <div class="container" style="margin-top: 110px; margin-bottom: 50px;">
                <div class="row justify-content-center">
                    <div class="col-md-7">
                        <div class="p-4 border rounded bg-light">

                            <h4 class="mb-2">
                                <i class="fas fa-qrcode me-2"></i>
                                VNPAY Payment (MOCK)
                            </h4>
                            <p class="text-muted mb-4">
                                Demo localhost - flow giống production (IPN → Return)
                            </p>

                            <div class="p-3 bg-white border rounded mb-4">
                                <div class="d-flex justify-content-between">
                                    <span><b>Order ID</b></span>
                                    <span>#${param.vnp_TxnRef}</span>
                                </div>
                                <div class="d-flex justify-content-between mt-2">
                                    <span><b>Amount</b></span>
                                    <span>${param.vnp_Amount} (VND × 100)</span>
                                </div>
                                <div class="mt-2">
                                    <small class="text-muted">${param.vnp_OrderInfo}</small>
                                </div>
                            </div>


                            <form method="post" action="/mock-vnpay/confirm" class="mb-2">


                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                                <input type="hidden" name="result" value="success" />

                                <c:forEach var="p" items="${paramValues}">
                                    <c:if test="${p.key ne 'result'}">
                                        <input type="hidden" name="${p.key}" value="${param[p.key]}" />
                                    </c:if>
                                </c:forEach>

                                <button class="btn btn-success rounded-pill w-100 py-3">
                                    <i class="fas fa-check-circle me-2"></i>
                                    Pay success
                                </button>
                            </form>


                            <form method="post" action="/mock-vnpay/confirm">


                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                                <input type="hidden" name="result" value="fail" />

                                <c:forEach var="p" items="${paramValues}">
                                    <c:if test="${p.key ne 'result'}">
                                        <input type="hidden" name="${p.key}" value="${param[p.key]}" />
                                    </c:if>
                                </c:forEach>

                                <button class="btn btn-outline-danger rounded-pill w-100 py-3">
                                    <i class="fas fa-times-circle me-2"></i>
                                    Pay failed / cancel
                                </button>
                            </form>

                            <div class="mt-4 small text-muted">
                                * Khi bấm nút: hệ thống gọi <b>IPN</b> cập nhật DB trước,
                                sau đó mới redirect về trang <b>Thanks</b>.
                            </div>

                        </div>
                    </div>
                </div>
            </div>

            <jsp:include page="../layout/footer.jsp" />

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>