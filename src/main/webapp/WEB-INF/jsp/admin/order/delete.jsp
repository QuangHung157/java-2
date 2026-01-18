<%@page contentType="text/html" pageEncoding="UTF-8" %>

    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8">
        <title>Delete Order</title>
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
                            <li class="breadcrumb-item active">Delete</li>
                        </ol>

                        <h3>Delete order with ID = ${id}</h3>
                        <hr />

                        <div class="alert alert-danger">Are you sure you want to delete this order?</div>

                        <form method="post" action="/admin/order/delete">
                            <input type="hidden" name="id" value="${id}" />
                            <button class="btn btn-danger">Confirm</button>
                            <a href="/admin/order" class="btn btn-secondary ms-2">Back</a>
                        </form>

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