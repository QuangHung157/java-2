<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Products - Laptop Shop</title>

                <link rel="preconnect" href="https://fonts.googleapis.com">
                <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
                <link
                    href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600&family=Raleway:wght@600;800&display=swap"
                    rel="stylesheet">

                <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css" />
                <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css"
                    rel="stylesheet">

                <link href="<c:url value='/client/lib/lightbox/css/lightbox.min.css'/>" rel="stylesheet">
                <link href="<c:url value='/client/lib/owlcarousel/assets/owl.carousel.min.css'/>" rel="stylesheet">

                <link href="<c:url value='/client/css/bootstrap.min.css'/>" rel="stylesheet">
                <link href="<c:url value='/client/css/style.css'/>" rel="stylesheet">

                <style>
                    .product-thumb {
                        height: 200px;
                        object-fit: cover;
                        width: 100%;
                    }

                    .product-card {
                        transition: transform 0.3s ease-in-out, box-shadow 0.3s ease-in-out;
                    }

                    .product-card:hover {
                        transform: translateY(-5px);
                        box-shadow: 0 .5rem 1rem rgba(0, 0, 0, .15) !important;
                    }

                    .product-name-link {
                        color: #333;
                        text-decoration: none;
                        font-weight: 600;
                        display: -webkit-box;
                        -webkit-line-clamp: 2;
                        -webkit-box-orient: vertical;
                        overflow: hidden;
                        text-align: center;
                    }

                    .product-name-link:hover {
                        color: var(--bs-primary);
                    }

                    .short-desc {
                        display: -webkit-box;
                        -webkit-line-clamp: 2;
                        -webkit-box-orient: vertical;
                        overflow: hidden;
                        text-align: center;
                    }

                    .pagination {
                        display: flex !important;
                        flex-direction: row !important;
                        justify-content: center !important;
                        gap: 5px;
                    }
                </style>
            </head>

            <body>

                <div id="spinner"
                    class="show w-100 vh-100 bg-white position-fixed translate-middle top-50 start-50 d-flex align-items-center justify-content-center">
                    <div class="spinner-grow text-primary" role="status"></div>
                </div>
                <jsp:include page="../layout/header.jsp" />

                <div class="container-fluid py-3 bg-light">
                    <div class="container">
                        <nav aria-label="breadcrumb">
                            <ol class="breadcrumb mb-0">
                                <li class="breadcrumb-item"><a href="/">Home</a></li>
                                <li class="breadcrumb-item active" aria-current="page">Products</li>
                            </ol>
                        </nav>
                    </div>
                </div>

                <div class="container py-5">
                    <div class="row g-4">

                        <div class="col-lg-3">
                            <div class="card shadow-sm border-0 p-4">
                                <h4 class="mb-4 fw-bold">Filter Products</h4>
                                <form method="get" action="<c:url value='/products'/>">

                                    <div class="mb-4">
                                        <h6 class="mb-3 fw-bold">Brand</h6>
                                        <c:set var="sf" value="${selectedFactories}" />
                                        <div class="d-flex flex-column gap-2">
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" name="factory"
                                                    value="APPLE" id="brandApple" <c:if
                                                    test="${sf != null && sf.contains('APPLE')}">checked</c:if>>
                                                <label class="form-check-label" for="brandApple">Apple</label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" name="factory"
                                                    value="ACER" id="brandAcer" <c:if
                                                    test="${sf != null && sf.contains('ACER')}">checked</c:if>>
                                                <label class="form-check-label" for="brandAcer">Acer</label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" name="factory"
                                                    value="ASUS" id="brandAsus" <c:if
                                                    test="${sf != null && sf.contains('ASUS')}">checked</c:if>>
                                                <label class="form-check-label" for="brandAsus">Asus</label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" name="factory"
                                                    value="LENOVO" id="brandLenovo" <c:if
                                                    test="${sf != null && sf.contains('LENOVO')}">checked</c:if>>
                                                <label class="form-check-label" for="brandLenovo">Lenovo</label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" name="factory"
                                                    value="DELL" id="brandDell" <c:if
                                                    test="${sf != null && sf.contains('DELL')}">checked</c:if>>
                                                <label class="form-check-label" for="brandDell">Dell</label>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="mb-4">
                                        <h6 class="mb-3 fw-bold">Purpose</h6>
                                        <c:set var="st" value="${selectedTargets}" />
                                        <div class="d-flex flex-column gap-2">
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" name="target"
                                                    value="GAMING" id="targetGaming" <c:if
                                                    test="${st != null && st.contains('GAMING')}">checked</c:if>>
                                                <label class="form-check-label" for="targetGaming">Gaming</label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" name="target"
                                                    value="OFFICE" id="targetOffice" <c:if
                                                    test="${st != null && st.contains('OFFICE')}">checked</c:if>>
                                                <label class="form-check-label" for="targetOffice">Student /
                                                    Office</label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" name="target"
                                                    value="GRAPHIC" id="targetGraphic" <c:if
                                                    test="${st != null && st.contains('GRAPHIC')}">checked</c:if>>
                                                <label class="form-check-label" for="targetGraphic">Design /
                                                    Graphics</label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" name="target"
                                                    value="LIGHT" id="targetLight" <c:if
                                                    test="${st != null && st.contains('LIGHT')}">checked</c:if>>
                                                <label class="form-check-label" for="targetLight">Lightweight</label>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="mb-4">
                                        <h6 class="mb-3 fw-bold">Price Range</h6>
                                        <c:set var="sp" value="${selectedPrices}" />
                                        <div class="d-flex flex-column gap-2">
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" name="price"
                                                    value="UNDER_10" id="priceUnder10" <c:if
                                                    test="${sp != null && sp.contains('UNDER_10')}">checked</c:if>>
                                                <label class="form-check-label" for="priceUnder10">Under 10M</label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" name="price"
                                                    value="FROM_10_15" id="price1015" <c:if
                                                    test="${sp != null && sp.contains('FROM_10_15')}">checked</c:if>>
                                                <label class="form-check-label" for="price1015">10M – 15M</label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" name="price"
                                                    value="FROM_15_20" id="price1520" <c:if
                                                    test="${sp != null && sp.contains('FROM_15_20')}">checked</c:if>>
                                                <label class="form-check-label" for="price1520">15M – 20M</label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" name="price"
                                                    value="OVER_20" id="priceOver20" <c:if
                                                    test="${sp != null && sp.contains('OVER_20')}">checked</c:if>>
                                                <label class="form-check-label" for="priceOver20">Over 20M</label>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="mb-4">
                                        <h6 class="mb-3 fw-bold">Sort By</h6>
                                        <div class="d-flex flex-column gap-2">
                                            <div class="form-check">
                                                <input class="form-check-input" type="radio" name="sort" value=""
                                                    id="sortNone" <c:if test="${empty selectedSort}">checked</c:if>>
                                                <label class="form-check-label" for="sortNone">Newest</label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="radio" name="sort"
                                                    value="price-asc" id="sortAsc" <c:if
                                                    test="${selectedSort == 'price-asc'}">checked</c:if>>
                                                <label class="form-check-label" for="sortAsc">Price: Low to High</label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="radio" name="sort"
                                                    value="price-desc" id="sortDesc" <c:if
                                                    test="${selectedSort == 'price-desc'}">checked</c:if>>
                                                <label class="form-check-label" for="sortDesc">Price: High to
                                                    Low</label>
                                            </div>
                                        </div>
                                    </div>

                                    <button type="submit" class="btn btn-primary w-100 py-2 fw-bold">Apply
                                        Filters</button>
                                </form>
                            </div>
                        </div>

                        <div class="col-lg-9">
                            <div class="row g-4">
                                <c:forEach var="product" items="${products}">
                                    <div class="col-md-6 col-lg-4">
                                        <div
                                            class="card h-100 shadow-sm border-0 product-card position-relative rounded overflow-hidden">

                                            <span
                                                class="badge bg-warning text-white position-absolute top-0 start-0 m-3 px-3 py-2"
                                                style="z-index: 1;">Laptop</span>

                                            <a href="<c:url value='/product/${product.id}'/>" class="d-block">
                                                <img src="<c:url value='/images/product/${product.image}'/>"
                                                    class="card-img-top product-thumb" alt="${product.name}">
                                            </a>

                                            <div class="card-body d-flex flex-column p-4">
                                                <h5 class="card-title mb-2">
                                                    <a href="<c:url value='/product/${product.id}'/>"
                                                        class="product-name-link">
                                                        ${product.name}
                                                    </a>
                                                </h5>

                                                <p class="card-text small text-muted mb-3 short-desc">
                                                    ${product.shortDesc}
                                                </p>

                                                <div class="mt-auto">
                                                    <h5 class="fw-bold mb-3 text-dark text-center">
                                                        <fmt:formatNumber type="number" value="${product.price}" /> đ
                                                    </h5>

                                                    <form method="post"
                                                        action="<c:url value='/add-product-to-cart/${product.id}'/>">
                                                        <input type="hidden" name="${_csrf.parameterName}"
                                                            value="${_csrf.token}" />
                                                        <button type="submit"
                                                            class="btn btn-outline-primary w-100 rounded-pill fw-bold">
                                                            <i class="fa fa-shopping-bag me-2"></i> Add to Cart
                                                        </button>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>

                            <c:if test="${totalPages > 1}">
                                <div class="mt-5 d-flex justify-content-center">
                                    <nav aria-label="Page navigation">
                                        <ul class="pagination">
                                            <c:set var="prev" value="${currentPage - 1}" />
                                            <c:set var="next" value="${currentPage + 1}" />

                                            <li class="page-item <c:if test='${currentPage == 1}'>disabled</c:if>">
                                                <a class="page-link shadow-none"
                                                    href="<c:url value='/products?page=${prev}'/>"
                                                    aria-label="Previous">
                                                    <span aria-hidden="true">&laquo;</span>
                                                </a>
                                            </li>

                                            <c:forEach begin="1" end="${totalPages}" var="i">
                                                <li class="page-item <c:if test='${i == currentPage}'>active</c:if>">
                                                    <a class="page-link shadow-none"
                                                        href="<c:url value='/products?page=${i}'/>">${i}</a>
                                                </li>
                                            </c:forEach>

                                            <li
                                                class="page-item <c:if test='${currentPage == totalPages}'>disabled</c:if>">
                                                <a class="page-link shadow-none"
                                                    href="<c:url value='/products?page=${next}'/>" aria-label="Next">
                                                    <span aria-hidden="true">&raquo;</span>
                                                </a>
                                            </li>
                                        </ul>
                                    </nav>
                                </div>
                            </c:if>

                        </div>
                    </div>
                </div>
                <jsp:include page="../layout/footer.jsp" />

                <a href="#" class="btn btn-primary border-3 border-primary rounded-circle back-to-top">
                    <i class="fa fa-arrow-up"></i>
                </a>

                <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
                <script src="<c:url value='/client/lib/easing/easing.min.js'/>"></script>
                <script src="<c:url value='/client/lib/waypoints/waypoints.min.js'/>"></script>
                <script src="<c:url value='/client/lib/lightbox/js/lightbox.min.js'/>"></script>
                <script src="<c:url value='/client/lib/owlcarousel/owl.carousel.min.js'/>"></script>
                <script src="<c:url value='/client/js/main.js'/>"></script>

            </body>

            </html>