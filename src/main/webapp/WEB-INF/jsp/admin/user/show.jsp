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

        <body>


            <jsp:include page="../layout/header.jsp" />

            <div id="layoutSidenav">

                <jsp:include page="../layout/sidebar.jsp" />


                <div id="layoutSidenav_content">
                    <main>
                        <div class="container-fluid px-4">

                            <h1 class="mt-3">Manage Users</h1>

                            <ol class="breadcrumb mb-4">
                                <li class="breadcrumb-item"><a href="/admin">Dashboard</a></li>
                                <li class="breadcrumb-item active"><a href="/admin/user">Users</a></li>
                            </ol>


                            <form method="get" action="/admin/user" class="row g-2 mb-3">
                                <div class="col-md-8 col-lg-6">
                                    <input type="text" name="keyword" class="form-control"
                                        placeholder="Search by full name..." value="${keyword}" />
                                </div>

                                <div class="col-md-4 col-lg-3 d-flex gap-2">
                                    <button type="submit" class="btn btn-primary w-100">Search</button>
                                    <a href="/admin/user" class="btn btn-secondary w-100">Clear</a>
                                </div>
                            </form>


                            <div class="d-flex justify-content-between">
                                <h3>Table Users</h3>
                                <a href="/admin/user/create">
                                    <button class="btn btn-primary">Create user</button>
                                </a>
                            </div>

                            <hr />

                            <!-- TABLE -->
                            <table class="table table-bordered table-hover align-middle">
                                <thead>
                                    <tr>
                                        <th style="width: 80px;">ID</th>
                                        <th>Email</th>
                                        <th>Full Name</th>
                                        <th style="width: 120px;">Role</th>
                                        <th style="width: 260px;">Action</th>
                                    </tr>
                                </thead>

                                <tbody>
                                    <c:forEach var="user" items="${users1}">
                                        <tr>
                                            <th>${user.id}</th>
                                            <td>${user.email}</td>
                                            <td>${user.fullName}</td>
                                            <td>${user.role.name}</td>



                                            <td>
                                                <div class="d-flex gap-2 flex-nowrap">
                                                    <a href="/admin/user/${user.id}" class="btn btn-success">View</a>
                                                    <a href="/admin/user/update/${user.id}"
                                                        class="btn btn-warning">Update</a>
                                                    <a href="/admin/user/delete/${user.id}"
                                                        class="btn btn-danger">Delete</a>

                                                </div>
                                            </td>

                                        </tr>

                                    </c:forEach>

                                </tbody>
                            </table>

                        </div>
                    </main>

                    <!-- FOOTER -->
                    <jsp:include page="../layout/footer.jsp" />
                </div>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
                crossorigin="anonymous"></script>
            <script src="/js/scripts.js"></script>

        </body>

        </html>