<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
            <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

                <!DOCTYPE html>
                <html lang="en">

                <head>
                    <meta charset="utf-8" />
                    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
                    <meta name="description" content="laptopshop" />
                    <meta name="author" content="Hung" />
                    <title>Update Product</title>

                    <link href="/css/style.css" rel="stylesheet" />
                    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
                    <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js"
                        crossorigin="anonymous"></script>

                    <script>
                        $(document).ready(function () {
                            $("#avatarFile").change(function (e) {
                                if (e.target.files && e.target.files[0]) {
                                    const imgURL = URL.createObjectURL(e.target.files[0]);
                                    $("#imagePreview").attr("src", imgURL).css("display", "block");
                                }
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
                                    <h1 class="mt-4">Manage Products</h1>
                                    <ol class="breadcrumb mb-4">
                                        <li class="breadcrumb-item"><a href="/admin">Dashboard</a></li>
                                        <li class="breadcrumb-item"><a href="/admin/product">Products</a></li>
                                        <li class="breadcrumb-item active">Update</li>
                                    </ol>

                                    <div class="container mt-4">
                                        <div class="row">
                                            <div class="col-md-8 col-12 mx-auto">
                                                <h2>Update Product</h2>
                                                <hr />

                                                <!-- IMPORTANT: modelAttribute PHẢI là newProduct -->
                                                <form:form method="post" action="/admin/product/update"
                                                    modelAttribute="newProduct" enctype="multipart/form-data"
                                                    class="row needs-validation" novalidate="novalidate">

                                                    <!-- id hidden -->
                                                    <div style="display:none">
                                                        <form:input path="id" />
                                                    </div>

                                                    <div class="mb-3 col-12 col-md-6">
                                                        <label class="form-label">Name</label>
                                                        <form:input path="name" type="text" class="form-control"
                                                            required="required" />
                                                        <div class="invalid-feedback">Please enter product name.</div>
                                                    </div>

                                                    <div class="mb-3 col-12 col-md-6">
                                                        <label class="form-label">Price</label>
                                                        <form:input path="price" type="number" class="form-control"
                                                            min="0" step="0.01" required="required" />
                                                        <div class="invalid-feedback">Please enter a valid price.</div>
                                                    </div>

                                                    <div class="mb-3 col-12">
                                                        <label class="form-label">Detail description</label>
                                                        <form:textarea path="detailDesc" class="form-control" rows="4"
                                                            required="required" />
                                                        <div class="invalid-feedback">Please enter detail description.
                                                        </div>
                                                    </div>

                                                    <div class="mb-3 col-12 col-md-6">
                                                        <label class="form-label">Short description</label>
                                                        <form:input path="shortDesc" type="text" class="form-control"
                                                            required="required" />
                                                        <div class="invalid-feedback">Please enter short description.
                                                        </div>
                                                    </div>

                                                    <div class="mb-3 col-12 col-md-6">
                                                        <label class="form-label">Quantity</label>
                                                        <form:input path="quantity" type="number" class="form-control"
                                                            min="0" step="1" required="required" />
                                                        <div class="invalid-feedback">Please enter a valid quantity.
                                                        </div>
                                                    </div>

                                                    <div class="mb-3 col-12 col-md-6">
                                                        <label class="form-label">Factory</label>
                                                        <form:select class="form-select" path="factory"
                                                            required="required">
                                                            <form:option value="" label="-- Select factory --" />
                                                            <form:option value="APPLE">Apple (MacBook)</form:option>
                                                            <form:option value="ASUS">Asus</form:option>
                                                            <form:option value="DELL">Dell</form:option>
                                                            <form:option value="ACER">Acer</form:option>
                                                            <form:option value="LENOVO">Lenovo</form:option>
                                                            <form:option value="LG">LG</form:option>
                                                        </form:select>
                                                        <div class="invalid-feedback">Please choose a factory.</div>
                                                    </div>

                                                    <div class="mb-3 col-12 col-md-6">
                                                        <label class="form-label">Target</label>
                                                        <form:select class="form-select" path="target"
                                                            required="required">
                                                            <form:option value="" label="-- Select target --" />
                                                            <form:option value="GAMING">Gaming</form:option>
                                                            <form:option value="SINHVIEN-VANPHONG">Office & Study
                                                            </form:option>
                                                            <form:option value="THIET-KE-DO-HOA">Graphic Design
                                                            </form:option>
                                                            <form:option value="MONG-NHE">Thin & Light</form:option>
                                                            <form:option value="DOANH-NHAN">Business</form:option>
                                                        </form:select>
                                                        <div class="invalid-feedback">Please choose a target.</div>
                                                    </div>

                                                    <!-- Upload image: KHÔNG required (vì update có thể giữ ảnh cũ) -->
                                                    <div class="mb-3 col-12 col-md-6">
                                                        <label for="avatarFile" class="form-label">Image
                                                            (optional)</label>
                                                        <input class="form-control" type="file" id="avatarFile"
                                                            name="File" accept=".png,.jpg,.jpeg" />
                                                        <div class="form-text">Leave blank to keep the current image.
                                                        </div>
                                                    </div>

                                                    <!-- Preview: ưu tiên ảnh hiện có -->
                                                    <div class="mb-3 col-12">
                                                        <c:if test="${not empty newProduct.image}">
                                                            <img id="imagePreview"
                                                                src="/images/product/${newProduct.image}"
                                                                style="max-height:250px; display:block;"
                                                                alt="Product image" />
                                                        </c:if>
                                                        <c:if test="${empty newProduct.image}">
                                                            <img id="imagePreview"
                                                                style="max-height:250px; display:none;"
                                                                alt="Product image" />
                                                        </c:if>
                                                    </div>

                                                    <div class="mb-4 col-12">
                                                        <button type="submit" class="btn btn-warning">Update</button>
                                                        <a href="/admin/product" class="btn btn-secondary ms-2">Back</a>
                                                    </div>

                                                </form:form>

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

                    <!-- Bootstrap validate -->
                    <script>
                        (() => {
                            'use strict';
                            const forms = document.querySelectorAll('.needs-validation');
                            Array.from(forms).forEach(form => {
                                form.addEventListener('submit', event => {
                                    if (!form.checkValidity()) {
                                        event.preventDefault();
                                        event.stopPropagation();
                                    }
                                    form.classList.add('was-validated');
                                }, false);
                            });
                        })();
                    </script>
                </body>

                </html>