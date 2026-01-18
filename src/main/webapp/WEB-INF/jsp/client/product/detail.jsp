<%@page contentType="text/html" pageEncoding="UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

      <!DOCTYPE html>
      <html lang="en">

      <head>
        <meta charset="utf-8">
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <title>${product.name} - Laptopshop</title>

        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link
          href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600&family=Raleway:wght@600;800&display=swap"
          rel="stylesheet">

        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">

        <link href="/client/lib/lightbox/css/lightbox.min.css" rel="stylesheet">
        <link href="/client/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">

        <link href="/client/css/bootstrap.min.css" rel="stylesheet">

        <link href="/client/css/style.css" rel="stylesheet">

        <style>
          /* CSS cho phần chọn màu */
          .color-option {
            width: 30px;
            height: 30px;
            border-radius: 50%;
            display: inline-block;
            cursor: pointer;
            border: 2px solid #ddd;
            margin-right: 10px;
            position: relative;
          }

          .color-option.active {
            border-color: #81c408;
            /* Màu xanh chủ đạo của shop */
            box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);
          }

          /* Ẩn radio button thật đi */
          .color-input {
            display: none;
          }
        </style>
      </head>

      <body>

        <div id="spinner"
          class="show w-100 vh-100 bg-white position-fixed translate-middle top-50 start-50 d-flex align-items-center justify-content-center">
          <div class="spinner-grow text-primary" role="status"></div>
        </div>
        <jsp:include page="../layout/header.jsp" />

        <div class="container-fluid page-header py-5" style="background: #81c408 !important;">
          <h1 class="text-center text-white display-6">Product Details</h1>
          <ol class="breadcrumb justify-content-center mb-0">
            <li class="breadcrumb-item"><a href="/" class="text-white">Home</a></li>
            <li class="breadcrumb-item"><a href="/products" class="text-white">Products</a></li>
            <li class="breadcrumb-item active text-white">Details</li>
          </ol>
        </div>
        <div class="container-fluid py-5 mt-5">
          <div class="container py-5">
            <div class="row g-4 mb-5">
              <div class="col-12">
                <nav aria-label="breadcrumb">
                  <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="/">Home</a></li>
                    <li class="breadcrumb-item active" aria-current="page">Product Details</li>
                  </ol>
                </nav>
              </div>

              <div class="col-lg-8 col-xl-9">
                <div class="row g-4">
                  <div class="col-lg-6">
                    <div class="border rounded">
                      <a href="#">
                        <img src="/images/product/${product.image}" class="img-fluid rounded" alt="${product.name}">
                      </a>
                    </div>
                  </div>

                  <div class="col-lg-6">
                    <h4 class="fw-bold mb-3">${product.name}</h4>
                    <p class="mb-3 text-muted">Brand: ${product.factory}</p>

                    <h5 class="fw-bold mb-3 text-primary">
                      <fmt:formatNumber type="number" value="${product.price}" /> đ
                    </h5>

                    <div class="d-flex mb-4">
                      <i class="fa fa-star text-warning"></i>
                      <i class="fa fa-star text-warning"></i>
                      <i class="fa fa-star text-warning"></i>
                      <i class="fa fa-star text-warning"></i>
                      <i class="fa fa-star text-warning"></i>
                    </div>

                    <ul class="list-group list-group-flush mb-4">
                      <c:forTokens items="${product.shortDesc}" delims="•" var="line">
                        <c:if test="${not empty line}">
                          <li class="list-group-item px-0 py-2 border-0"><i
                              class="bi bi-check2 text-success me-2"></i>${line}</li>
                        </c:if>
                      </c:forTokens>
                    </ul>

                    <form action="/add-product-to-cart/${product.id}" method="post">
                      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                      <div class="mb-4">
                        <h6 class="fw-bold mb-2">Color:</h6>
                        <div class="d-flex align-items-center">
                          <label class="color-option active" style="background-color: #000;" title="Black"
                            onclick="selectColor(this)">
                            <input type="radio" name="color" value="Black" class="color-input" checked>
                          </label>

                          <label class="color-option" style="background-color: #C0C0C0;" title="Silver"
                            onclick="selectColor(this)">
                            <input type="radio" name="color" value="Silver" class="color-input">
                          </label>

                          <label class="color-option" style="background-color: #808080;" title="Space Grey"
                            onclick="selectColor(this)">
                            <input type="radio" name="color" value="Grey" class="color-input">
                          </label>
                        </div>
                      </div>

                      <div class="input-group quantity mb-4" style="width: 130px;">
                        <div class="input-group-btn">
                          <button class="btn btn-sm btn-minus rounded-circle bg-light border" type="button">
                            <i class="fa fa-minus"></i>
                          </button>
                        </div>
                        <input type="text" class="form-control form-control-sm text-center border-0" name="quantity"
                          value="1">
                        <div class="input-group-btn">
                          <button class="btn btn-sm btn-plus rounded-circle bg-light border" type="button">
                            <i class="fa fa-plus"></i>
                          </button>
                        </div>
                      </div>

                      <button type="submit"
                        class="btn border border-secondary rounded-pill px-4 py-2 mb-4 text-primary fw-bold">
                        <i class="fa fa-shopping-bag me-2 text-primary"></i> Add to Cart
                      </button>
                    </form>

                  </div>

                  <div class="col-12">
                    <nav>
                      <div class="nav nav-tabs mb-3">
                        <button class="nav-link active border-white border-bottom-0" type="button" role="tab"
                          data-bs-toggle="tab" data-bs-target="#nav-about">
                          Description
                        </button>
                      </div>
                    </nav>
                    <div class="tab-content mb-5">
                      <div class="tab-pane active" id="nav-about" role="tabpanel">
                        <p style="text-align: justify;">${product.detailDesc}</p>
                        <div class="mt-4">
                          <img src="/images/product/${product.image}" class="img-fluid w-100 rounded"
                            alt="${product.name}">
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div class="col-lg-4 col-xl-3">
                <div class="row g-4 fruite">
                  <div class="col-12">
                    <div class="mb-4">
                      <h4>Categories</h4>
                      <ul class="list-unstyled fruite-categorie">
                        <li>
                          <div class="d-flex justify-content-between fruite-name"><a href="#">Apple</a><span>(3)</span>
                          </div>
                        </li>
                        <li>
                          <div class="d-flex justify-content-between fruite-name"><a href="#">Dell</a><span>(5)</span>
                          </div>
                        </li>
                        <li>
                          <div class="d-flex justify-content-between fruite-name"><a href="#">ASUS</a><span>(2)</span>
                          </div>
                        </li>
                        <li>
                          <div class="d-flex justify-content-between fruite-name"><a href="#">Lenovo</a><span>(8)</span>
                          </div>
                        </li>
                        <li>
                          <div class="d-flex justify-content-between fruite-name"><a href="#">Acer</a><span>(5)</span>
                          </div>
                        </li>
                      </ul>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <jsp:include page="../layout/footer.jsp" />

        <a href="#" class="btn btn-primary border-3 border-primary rounded-circle back-to-top">
          <i class="fa fa-arrow-up"></i>
        </a>

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="/client/lib/easing/easing.min.js"></script>
        <script src="/client/lib/waypoints/waypoints.min.js"></script>
        <script src="/client/lib/lightbox/js/lightbox.min.js"></script>
        <script src="/client/lib/owlcarousel/owl.carousel.min.js"></script>
        <script src="/client/js/main.js"></script>

        <script>
          function selectColor(element) {

            document.querySelectorAll('.color-option').forEach(el => el.classList.remove('active'));

            element.classList.add('active');


            const radio = element.querySelector('input[type="radio"]');
            if (radio) radio.checked = true;
          }
        </script>
      </body>

      </html>