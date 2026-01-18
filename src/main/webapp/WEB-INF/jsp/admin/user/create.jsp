<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="utf-8" />
                <title>Create User</title>
                <link href="/css/style.css" rel="stylesheet" />

                <!-- ===== jQuery (PHẦN HỌ MỚI THÊM) ===== -->
                <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

                <!-- ===== Image Preview Script (PHẦN HỌ MỚI THÊM) ===== -->
                <script>
                    $(document).ready(function () {
                        $("#avatarFile").change(function (e) {
                            const imgURL = URL.createObjectURL(e.target.files[0]);
                            $("#avatarPreview").attr("src", imgURL);
                            $("#avatarPreview").css("display", "block");
                        });
                    });
                </script>
            </head>

            <body class="sb-nav-fixed">
                <jsp:include page="../layout/header.jsp" />

                <div id="layoutSidenav">
                    <jsp:include page="../layout/sidebar.jsp" />

                    <div id="layoutSidenav_content">
                        <main>
                            <div class="container-fluid px-4">

                                <h1 class="mt-4">Create User</h1>
                                <hr />

                                <!-- ===== FORM ===== -->
                                <form:form method="post" action="/admin/user/create" modelAttribute="newUser"
                                    enctype="multipart/form-data">

                                    <div class="row">

                                        <!-- Email -->
                                        <div class="mb-3 col-12 col-md-6">
                                            <label class="form-label">Email</label>
                                            <form:input path="email" type="email" class="form-control" />
                                        </div>

                                        <!-- Password -->
                                        <div class="mb-3 col-12 col-md-6">
                                            <label class="form-label">Password</label>
                                            <form:input path="password" type="password" class="form-control" />
                                        </div>

                                        <!-- Phone -->
                                        <div class="mb-3 col-12 col-md-6">
                                            <label class="form-label">Phone</label>
                                            <form:input path="phone" class="form-control" />
                                        </div>

                                        <!-- Full Name -->
                                        <div class="mb-3 col-12 col-md-6">
                                            <label class="form-label">Full Name</label>
                                            <form:input path="fullName" class="form-control" />
                                        </div>

                                        <!-- Address -->
                                        <div class="mb-3 col-12">
                                            <label class="form-label">Address</label>
                                            <form:input path="address" class="form-control" />
                                        </div>

                                        <!-- Role (UI only – chưa bind backend) -->
                                        <div class="mb-3 col-12 col-md-6">
                                            <label class="form-label">Role</label>
                                            <form:select class="form-select" path="role.name">
                                                <form:option value="ADMIN">ADMIN</form:option>
                                                <form:option value="USER">USER</form:option>
                                            </form:select>
                                        </div>

                                        <!-- Avatar -->
                                        <div class="mb-3 col-12 col-md-6">
                                            <label class="form-label">Avatar</label>
                                            <input class="form-control" type="file" id="avatarFile"
                                                accept=".png,.jpg,.jpeg" name="File" />
                                        </div>

                                        <!-- Avatar Preview -->
                                        <div class=" mb-3 col-12">
                                            <img id="avatarPreview" style="max-height:250px; display:none;"
                                                alt="avatar preview" />
                                        </div>

                                        <!-- Submit -->
                                        <div class="col-12">
                                            <button type="submit" class="btn btn-primary">
                                                Create
                                            </button>
                                        </div>

                                    </div>
                                </form:form>

                            </div>
                        </main>
                    </div>
                </div>

            </body>

            </html>