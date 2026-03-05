<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="utf-8" />
            <meta http-equiv="X-UA-Compatible" content="IE=edge" />
            <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
            <meta name="description" content="laptopshop" />
            <meta name="author" content="Hung" />
            <title>Update Order</title>

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
                                <li class="breadcrumb-item active">Update</li>
                            </ol>

                            <div class="container mt-4">
                                <div class="row">
                                    <div class="col-md-8 col-12 mx-auto">
                                        <h2>Update Order Status</h2>
                                        <hr />

                                        <form method="post" action="/admin/order/update" class="row">

                                            <%-- ✅ nếu bật Spring Security CSRF thì cần token --%>
                                                <input type="hidden" name="${_csrf.parameterName}"
                                                    value="${_csrf.token}" />

                                                <input type="hidden" name="id" value="${order.id}" />

                                                <div class="mb-3 col-12 col-md-6">
                                                    <label class="form-label">Order ID</label>
                                                    <input class="form-control" value="${order.id}" disabled />
                                                </div>

                                                <div class="mb-3 col-12 col-md-6">
                                                    <label class="form-label">Status</label>
                                                    <select class="form-select" name="status" required>
                                                        <option value="NEW" ${order.status=='NEW' ? 'selected' : '' }>
                                                            NEW</option>

                                                        <option value="CONFIRMED" ${order.status=='CONFIRMED'
                                                            ? 'selected' : '' }>
                                                            CONFIRMED
                                                        </option>

                                                        <option value="SHIPPING" ${order.status=='SHIPPING' ? 'selected'
                                                            : '' }>
                                                            SHIPPING
                                                        </option>

                                                        <option value="DONE" ${order.status=='DONE' ? 'selected' : '' }>
                                                            DONE</option>

                                                        <option value="CANCELED" ${order.status=='CANCELED' ? 'selected'
                                                            : '' }>
                                                            CANCELED
                                                        </option>
                                                    </select>
                                                </div>

                                                <div class="mb-4 col-12">
                                                    <button type="submit" class="btn btn-warning">Update</button>
                                                    <a href="/admin/order/${order.id}"
                                                        class="btn btn-secondary ms-2">Back</a>
                                                </div>
                                        </form>

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