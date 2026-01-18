<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <title>Delete Product</title>
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
                                <h1 class="mt-4">Manage Products</h1>
                                <ol class="breadcrumb mb-4">
                                    <li class="breadcrumb-item"><a href="/admin">Dashboard</a></li>
                                    <li class="breadcrumb-item"><a href="/admin/product">Products</a></li>
                                    <li class="breadcrumb-item active">Delete</li>
                                </ol>

                                <h3>Delete product with ID = ${id}</h3>
                                <hr />

                                <div class="alert alert-danger">Are you sure you want to delete this product?</div>

                                <form:form method="post" action="/admin/product/delete" modelAttribute="newProduct">
                                    <div style="display:none">
                                        <form:input path="id" value="${id}" />
                                    </div>

                                    <button class="btn btn-danger">Confirm</button>
                                    <a href="/admin/product" class="btn btn-secondary ms-2">Back</a>
                                </form:form>

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