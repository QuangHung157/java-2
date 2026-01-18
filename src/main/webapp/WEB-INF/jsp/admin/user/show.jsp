<%@ page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!DOCTYPE html>
        <html lang="en">

        <head>

            <meta charset="utf-8" />
            <meta http-equiv="X-UA-Compatible" content="IE=edge" />
            <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
            <meta name="description" content="laptopshop" />
            <meta name="author" content="hung" />

            <title>Users</title>


            <link href="/css/style.css" rel="stylesheet" />
            <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        </head>

        <body class="sb-nav-fixed">

            <!-- ================= HEADER ================= -->
            <jsp:include page="../layout/header.jsp" />

            <div id="layoutSidenav">

                <!-- ================= SIDEBAR ================= -->
                <jsp:include page="../layout/sidebar.jsp" />

                <!-- ================= CONTENT ================= -->
                <div id="layoutSidenav_content">
                    <main>
                        <div class="container-fluid px-4">

                            <!-- ===== Title ===== -->
                            <h1 class="mt-4">Manage User</h1>

                            <!-- ===== Breadcrumb ===== -->
                            <ol class="breadcrumb mb-4">
                                <li class="breadcrumb-item">
                                    <a href="/admin">Dashboard</a>
                                </li>
                                <li class="breadcrumb-item active">
                                    <a href="/admin/user">Users</a>
                                </li>
                            </ol>

                            <!-- ================= USER TABLE ================= -->
                            <div class="mt-5">
                                <div class="row">
                                    <div class="col-12 mx-auto">

                                        <!-- ===== Table header + button ===== -->
                                        <div class="d-flex justify-content-between">
                                            <h3>Table Users</h3>
                                            <a href="/admin/user/create">
                                                <button class="btn btn-primary">
                                                    Create user
                                                </button>
                                            </a>
                                        </div>

                                        <hr />

                                        <!-- ===== TABLE ===== -->
                                        <table class="table table-bordered table-hover">

                                            <!-- ===== TABLE HEAD ===== -->
                                            <thead>
                                                <tr>
                                                    <th>ID</th>
                                                    <th>Email</th>
                                                    <th>Full Name</th>
                                                    <th>Role</th>
                                                    <th>Action</th>
                                                </tr>
                                            </thead>

                                            <!-- ===== TABLE BODY ===== -->
                                            <tbody>
                                                <!-- users1 được truyền từ Controller -->
                                                <c:forEach var="user" items="${users1}">
                                                    <tr>
                                                        <th>${user.id}</th>
                                                        <td>${user.email}</td>
                                                        <td>${user.fullName}</td>
                                                        <td>${user.role.name}</td>
                                                        <td>
                                                            <a href="/admin/user/${user.id}" class="btn btn-success">
                                                                View
                                                            </a>

                                                            <a href="/admin/user/update/${user.id}"
                                                                class="btn btn-warning">
                                                                Update
                                                            </a>

                                                            <a href="/admin/user/delete/${user.id}"
                                                                class="btn btn-danger">
                                                                Delete
                                                            </a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>

                                    </div>
                                </div>
                            </div>

                        </div>
                    </main>

                    <!-- ================= FOOTER ================= -->
                    <jsp:include page="../layout/footer.jsp" />
                </div>
            </div>

            <!-- ================= JS ================= -->
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
                crossorigin="anonymous"></script>
            <script src="/js/scripts.js"></script>

        </body>

        </html>