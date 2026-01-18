<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="utf-8" />
            <title>User Detail</title>
            <link href="/css/style.css" rel="stylesheet" />
        </head>

        <body class="sb-nav-fixed">
            <jsp:include page="../layout/header.jsp" />
            <div id="layoutSidenav">
                <jsp:include page="../layout/sidebar.jsp" />

                <div id="layoutSidenav_content">
                    <main>
                        <div class="container-fluid px-4">

                            <h1 class="mt-4">User Detail</h1>
                            <hr />

                            <div class="card" style="width:60%">
                                <div class="card-header">User information</div>
                                <ul class="list-group list-group-flush">
                                    <li class="list-group-item">ID: ${user.id}</li>
                                    <li class="list-group-item">Email: ${user.email}</li>
                                    <li class="list-group-item">Full Name: ${user.fullName}</li>
                                    <li class="list-group-item">Role: ${user.role.name}</li>
                                    <li class="list-group-item">Address: ${user.address}</li>
                                    <li class="list-group-item">Phone: ${user.phone}</li>
                                </ul>
                            </div>

                            <a href="/admin/user" class="btn btn-success mt-3">Back</a>

                        </div>
                    </main>
                </div>
            </div>
        </body>

        </html>