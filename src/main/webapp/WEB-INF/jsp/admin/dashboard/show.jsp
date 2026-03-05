<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

      <!DOCTYPE html>
      <html lang="en">

      <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="laptopshop" />
        <meta name="author" content="Hung" />
        <title>Dashboard - Admin</title>

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
                <h1 class="mt-4">Dashboard</h1>
                <ol class="breadcrumb mb-4">
                  <li class="breadcrumb-item active">Thống kê</li>
                </ol>

                <!-- KPI CARDS -->
                <div class="row">

                  <div class="col-xl-3 col-md-6">
                    <div class="card bg-primary text-white mb-4 shadow-sm">
                      <div class="card-body d-flex align-items-center justify-content-between">
                        <div>
                          <div class="small text-white-50">Users</div>
                          <div class="fs-4 fw-bold">
                            <fmt:formatNumber type="number" value="${countUsers}" />
                          </div>
                        </div>
                        <i class="fas fa-users fa-2x text-white-50"></i>
                      </div>
                      <div class="card-footer d-flex align-items-center justify-content-between">
                        <a class="small text-white stretched-link" href="/admin/user">View Details</a>
                        <div class="small text-white"><i class="fas fa-angle-right"></i></div>
                      </div>
                    </div>
                  </div>

                  <div class="col-xl-3 col-md-6">
                    <div class="card bg-success text-white mb-4 shadow-sm">
                      <div class="card-body d-flex align-items-center justify-content-between">
                        <div>
                          <div class="small text-white-50">Products</div>
                          <div class="fs-4 fw-bold">
                            <fmt:formatNumber type="number" value="${countProducts}" />
                          </div>
                        </div>
                        <i class="fas fa-laptop fa-2x text-white-50"></i>
                      </div>
                      <div class="card-footer d-flex align-items-center justify-content-between">
                        <a class="small text-white stretched-link" href="/admin/product">View Details</a>
                        <div class="small text-white"><i class="fas fa-angle-right"></i></div>
                      </div>
                    </div>
                  </div>

                  <div class="col-xl-3 col-md-6">
                    <div class="card bg-danger text-white mb-4 shadow-sm">
                      <div class="card-body d-flex align-items-center justify-content-between">
                        <div>
                          <div class="small text-white-50">Orders</div>
                          <div class="fs-4 fw-bold">
                            <fmt:formatNumber type="number" value="${countOrders}" />
                          </div>
                        </div>
                        <i class="fas fa-receipt fa-2x text-white-50"></i>
                      </div>
                      <div class="card-footer d-flex align-items-center justify-content-between">
                        <a class="small text-white stretched-link" href="/admin/order">View Details</a>
                        <div class="small text-white"><i class="fas fa-angle-right"></i></div>
                      </div>
                    </div>
                  </div>

                  <div class="col-xl-3 col-md-6">
                    <div class="card bg-dark text-white mb-4 shadow-sm">
                      <div class="card-body d-flex align-items-center justify-content-between">
                        <div>
                          <div class="small text-white-50">Revenue (last 30 days)</div>
                          <div class="fs-4 fw-bold">
                            <fmt:formatNumber type="number" value="${revenue30d}" /> đ
                          </div>
                        </div>
                        <i class="fas fa-chart-line fa-2x text-white-50"></i>
                      </div>
                      <div class="card-footer d-flex align-items-center justify-content-between">
                        <a class="small text-white stretched-link" href="/admin/order">Orders</a>
                        <div class="small text-white"><i class="fas fa-angle-right"></i></div>
                      </div>
                    </div>
                  </div>

                </div>

                <!-- LATEST ORDERS -->
                <div class="card mb-4 shadow-sm">
                  <div class="card-header">
                    <i class="fas fa-clock me-1"></i>
                    Latest Orders
                  </div>

                  <div class="card-body">
                    <div class="table-responsive">
                      <table class="table table-hover align-middle">
                        <thead>
                          <tr>
                            <th style="width:90px;">Order ID</th>
                            <th>User</th>
                            <th style="width:160px;">Created</th>
                            <th style="width:140px;">Status</th>
                            <th class="text-end" style="width:160px;">Total</th>
                            <th style="width:120px;"></th>
                          </tr>
                        </thead>

                        <tbody>
                          <c:forEach var="o" items="${latestOrders}">
                            <tr>
                              <td>#${o.id}</td>
                              <td>
                                <c:out value="${o.user.email}" />
                              </td>
                              <td>
                                <c:out value="${o.createdAt}" />
                              </td>
                              <td>
                                <c:choose>
                                  <c:when test="${o.status == 'NEW'}">
                                    <span class="badge bg-secondary">NEW</span>
                                  </c:when>
                                  <c:when test="${o.status == 'CONFIRMED'}">
                                    <span class="badge bg-primary">CONFIRMED</span>
                                  </c:when>
                                  <c:when test="${o.status == 'SHIPPING'}">
                                    <span class="badge bg-info text-dark">SHIPPING</span>
                                  </c:when>
                                  <c:when test="${o.status == 'DONE'}">
                                    <span class="badge bg-success">DONE</span>
                                  </c:when>
                                  <c:when test="${o.status == 'CANCELED'}">
                                    <span class="badge bg-danger">CANCELED</span>
                                  </c:when>
                                  <c:otherwise>
                                    <span class="badge bg-dark">${o.status}</span>
                                  </c:otherwise>
                                </c:choose>
                              </td>
                              <td class="text-end">
                                <fmt:formatNumber type="number" value="${o.totalPrice}" /> đ
                              </td>
                              <td class="text-end">
                                <a class="btn btn-sm btn-outline-primary" href="/admin/order/${o.id}">
                                  View
                                </a>
                              </td>
                            </tr>
                          </c:forEach>

                          <c:if test="${empty latestOrders}">
                            <tr>
                              <td colspan="6">No orders found.</td>
                            </tr>
                          </c:if>
                        </tbody>
                      </table>
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
      </body>

      </html>