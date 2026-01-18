<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <div class="container-fluid fixed-top">
      <div class="container px-0">
        <nav class="navbar navbar-light bg-white navbar-expand-xl">
          <a href="/" class="navbar-brand">
            <h1 class="text-primary display-6">LaptopShop</h1>
          </a>

          <button class="navbar-toggler py-2 px-3" type="button" data-bs-toggle="collapse"
            data-bs-target="#navbarCollapse">
            <span class="fa fa-bars text-primary"></span>
          </button>

          <div class="collapse navbar-collapse bg-white justify-content-between mx-5" id="navbarCollapse">
            <div class="navbar-nav">
              <a href="/" class="nav-item nav-link active">Home</a>
              <a href="/products" class="nav-item nav-link">Products</a>
            </div>

            <div class="d-flex m-3 me-0 align-items-center">
              <%-- CART --%>
                <a href="/cart" class="position-relative me-4 my-auto">
                  <i class="fa fa-shopping-bag fa-2x text-primary"></i>
                  <span
                    class="position-absolute bg-secondary rounded-circle d-flex align-items-center justify-content-center text-dark px-1"
                    style="top:-5px; left:15px; height:20px; min-width:20px;">
                    <c:out value="${empty sessionScope.sum ? 0 : sessionScope.sum}" />
                  </span>
                </a>

                <%-- USER --%>
                  <c:choose>
                    <c:when test="${pageContext.request.userPrincipal == null}">
                      <a href="/login" class="my-auto text-decoration-none">
                        <i class="fas fa-user fa-2x text-primary"></i>
                      </a>
                    </c:when>

                    <c:otherwise>
                      <div class="dropdown my-auto">
                        <a class="dropdown-toggle text-decoration-none" href="#" role="button" data-bs-toggle="dropdown"
                          aria-expanded="false">
                          <i class="fas fa-user fa-2x text-primary"></i>
                        </a>

                        <ul class="dropdown-menu dropdown-menu-end">
                          <li class="dropdown-item-text small text-muted">
                            <c:out value="${pageContext.request.userPrincipal.name}" />
                          </li>
                          <li>
                            <hr class="dropdown-divider" />
                          </li>

                          <li>
                            <form method="post" action="/logout" style="margin:0;">

                              <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                              <button type="submit" class="dropdown-item">Đăng xuất</button>
                            </form>
                          </li>
                        </ul>
                      </div>
                    </c:otherwise>
                  </c:choose>

            </div>
          </div>
        </nav>
      </div>
    </div>