<%@page contentType="text/html" pageEncoding="UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <!DOCTYPE html>
    <html lang="en">

    <head>
      <meta charset="utf-8" />
      <meta http-equiv="X-UA-Compatible" content="IE=edge" />
      <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
      <meta name="description" content="LaptopShop Admin Dashboard" />
      <meta name="author" content="Admin" />
      <title>Admin Dashboard - LaptopShop</title>

      <!-- Bootstrap + Icons -->
      <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
        crossorigin="anonymous" />
      <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet" />

      <!-- Your existing styles (optional) -->
      <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" />

      <style>
        /* Dashboard styling (clean + modern) */
        body {
          background: #f6f8fb;
        }

        .page-title {
          font-weight: 700;
          letter-spacing: .2px;
        }

        .kpi-card {
          border: 0;
          border-radius: 16px;
          box-shadow: 0 10px 25px rgba(16, 24, 40, .06);
          overflow: hidden;
        }

        .kpi-icon {
          width: 46px;
          height: 46px;
          border-radius: 12px;
          display: inline-flex;
          align-items: center;
          justify-content: center;
          font-size: 20px;
        }

        .kpi-label {
          color: #667085;
          font-weight: 600;
          font-size: .9rem;
        }

        .kpi-value {
          font-weight: 800;
          font-size: 1.6rem;
          margin: 0;
        }

        .kpi-sub {
          color: #667085;
          font-size: .85rem;
          margin: 0;
        }

        .card-soft {
          border: 0;
          border-radius: 16px;
          box-shadow: 0 10px 25px rgba(16, 24, 40, .06);
        }

        .chip {
          display: inline-flex;
          gap: 6px;
          align-items: center;
          padding: 6px 10px;
          border-radius: 999px;
          font-size: .85rem;
          font-weight: 600;
          border: 1px solid rgba(16, 24, 40, .08);
          background: #fff;
        }

        .table thead th {
          color: #475467;
          font-weight: 700;
          font-size: .85rem;
          border-bottom: 1px solid #eaecf0;
        }

        .table tbody td {
          vertical-align: middle;
          border-top: 1px solid #f1f2f4;
        }

        .badge-soft {
          border-radius: 999px;
          padding: .35rem .6rem;
          font-weight: 700;
          font-size: .75rem;
        }

        .badge-paid {
          background: rgba(18, 183, 106, .12);
          color: #027A48;
        }

        .badge-pending {
          background: rgba(245, 158, 11, .12);
          color: #B45309;
        }

        .badge-canceled {
          background: rgba(239, 68, 68, .12);
          color: #B42318;
        }

        .btn-pill {
          border-radius: 999px;
        }
      </style>
    </head>

    <body class="sb-nav-fixed">
      <jsp:include page="../layout/header.jsp" />
      <div id="layoutSidenav">
        <jsp:include page="../layout/sidebar.jsp" />

        <div id="layoutSidenav_content">
          <main class="py-4">
            <div class="container-fluid px-4">

              <!-- Header -->
              <div class="d-flex flex-wrap align-items-center justify-content-between gap-2 mb-3">
                <div>
                  <h1 class="page-title mb-1">Dashboard</h1>
                  <div class="text-muted">Overview of system activity and key metrics</div>
                </div>

                <div class="d-flex flex-wrap gap-2">
                  <span class="chip"><i class="bi bi-calendar3"></i> Last 30 days</span>
                  <a class="btn btn-light btn-pill" href="${pageContext.request.contextPath}/" title="Back to website">
                    <i class="bi bi-house-door"></i> Website
                  </a>
                  <button class="btn btn-dark btn-pill" type="button">
                    <i class="bi bi-arrow-clockwise"></i> Refresh
                  </button>
                </div>
              </div>

              <!-- KPI Cards -->
              <div class="row g-3 mb-3">
                <div class="col-12 col-sm-6 col-xl-3">
                  <div class="card kpi-card">
                    <div class="card-body d-flex align-items-start justify-content-between">
                      <div>
                        <div class="kpi-label">Total Users</div>
                        <p class="kpi-value">1,248</p>
                        <p class="kpi-sub"><i class="bi bi-arrow-up-right"></i> +6.2% vs last period</p>
                      </div>
                      <div class="kpi-icon" style="background: rgba(59,130,246,.12); color:#1d4ed8;">
                        <i class="bi bi-people"></i>
                      </div>
                    </div>
                    <div class="px-3 pb-3">
                      <a class="btn btn-outline-primary btn-sm btn-pill w-100"
                        href="${pageContext.request.contextPath}/admin/user">
                        Manage Users
                      </a>
                    </div>
                  </div>
                </div>

                <div class="col-12 col-sm-6 col-xl-3">
                  <div class="card kpi-card">
                    <div class="card-body d-flex align-items-start justify-content-between">
                      <div>
                        <div class="kpi-label">Products</div>
                        <p class="kpi-value">356</p>
                        <p class="kpi-sub"><i class="bi bi-box-seam"></i> 18 low-stock items</p>
                      </div>
                      <div class="kpi-icon" style="background: rgba(16,185,129,.12); color:#047857;">
                        <i class="bi bi-laptop"></i>
                      </div>
                    </div>
                    <div class="px-3 pb-3">
                      <a class="btn btn-outline-success btn-sm btn-pill w-100"
                        href="${pageContext.request.contextPath}/admin/product">
                        Manage Products
                      </a>
                    </div>
                  </div>
                </div>

                <div class="col-12 col-sm-6 col-xl-3">
                  <div class="card kpi-card">
                    <div class="card-body d-flex align-items-start justify-content-between">
                      <div>
                        <div class="kpi-label">Orders (30d)</div>
                        <p class="kpi-value">482</p>
                        <p class="kpi-sub"><i class="bi bi-receipt"></i> 23 awaiting payment</p>
                      </div>
                      <div class="kpi-icon" style="background: rgba(245,158,11,.12); color:#B45309;">
                        <i class="bi bi-bag-check"></i>
                      </div>
                    </div>
                    <div class="px-3 pb-3">
                      <a class="btn btn-outline-warning btn-sm btn-pill w-100"
                        href="${pageContext.request.contextPath}/admin/order">
                        Manage Orders
                      </a>
                    </div>
                  </div>
                </div>

                <div class="col-12 col-sm-6 col-xl-3">
                  <div class="card kpi-card">
                    <div class="card-body d-flex align-items-start justify-content-between">
                      <div>
                        <div class="kpi-label">Revenue (30d)</div>
                        <p class="kpi-value">₫ 184.2M</p>
                        <p class="kpi-sub"><i class="bi bi-arrow-up-right"></i> +12.8% growth</p>
                      </div>
                      <div class="kpi-icon" style="background: rgba(168,85,247,.12); color:#6d28d9;">
                        <i class="bi bi-cash-coin"></i>
                      </div>
                    </div>
                    <div class="px-3 pb-3">
                      <button class="btn btn-outline-secondary btn-sm btn-pill w-100" type="button" disabled>
                        Sales Report (demo)
                      </button>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Charts -->
              <div class="row g-3 mb-3">
                <div class="col-12 col-xl-8">
                  <div class="card card-soft">
                    <div class="card-body">
                      <div class="d-flex align-items-center justify-content-between mb-2">
                        <div>
                          <div class="fw-bold">Revenue & Orders Trend</div>
                          <div class="text-muted small">Monthly overview (demo data)</div>
                        </div>
                        <span class="chip"><i class="bi bi-graph-up"></i> Analytics</span>
                      </div>
                      <div style="height: 310px;">
                        <canvas id="chartRevenue"></canvas>
                      </div>
                    </div>
                  </div>
                </div>

                <div class="col-12 col-xl-4">
                  <div class="card card-soft h-100">
                    <div class="card-body">
                      <div class="d-flex align-items-center justify-content-between mb-2">
                        <div>
                          <div class="fw-bold">Order Status</div>
                          <div class="text-muted small">Distribution (demo data)</div>
                        </div>
                        <span class="chip"><i class="bi bi-pie-chart"></i> Status</span>
                      </div>
                      <div style="height: 310px;">
                        <canvas id="chartStatus"></canvas>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Tables -->
              <div class="row g-3">
                <div class="col-12 col-xl-8">
                  <div class="card card-soft">
                    <div class="card-body">
                      <div class="d-flex flex-wrap align-items-center justify-content-between gap-2 mb-2">
                        <div>
                          <div class="fw-bold">Recent Orders</div>
                          <div class="text-muted small">Latest transactions (demo data)</div>
                        </div>
                        <a class="btn btn-dark btn-sm btn-pill" href="${pageContext.request.contextPath}/admin/order">
                          View all
                        </a>
                      </div>

                      <div class="table-responsive">
                        <table class="table align-middle mb-0">
                          <thead>
                            <tr>
                              <th>Order ID</th>
                              <th>Customer</th>
                              <th>Payment</th>
                              <th>Total</th>
                              <th>Status</th>
                              <th class="text-end">Action</th>
                            </tr>
                          </thead>
                          <tbody>
                            <tr>
                              <td>#1042</td>
                              <td>Nguyen Van A</td>
                              <td>VNPAY</td>
                              <td>₫ 24,990,000</td>
                              <td><span class="badge-soft badge-paid">PAID</span></td>
                              <td class="text-end">
                                <a class="btn btn-outline-secondary btn-sm btn-pill"
                                  href="${pageContext.request.contextPath}/admin/order">
                                  Details
                                </a>
                              </td>
                            </tr>
                            <tr>
                              <td>#1041</td>
                              <td>Tran Thi B</td>
                              <td>COD</td>
                              <td>₫ 17,690,000</td>
                              <td><span class="badge-soft badge-pending">PENDING</span></td>
                              <td class="text-end">
                                <a class="btn btn-outline-secondary btn-sm btn-pill"
                                  href="${pageContext.request.contextPath}/admin/order">
                                  Details
                                </a>
                              </td>
                            </tr>
                            <tr>
                              <td>#1040</td>
                              <td>Le Van C</td>
                              <td>VNPAY</td>
                              <td>₫ 31,490,000</td>
                              <td><span class="badge-soft badge-canceled">CANCELED</span></td>
                              <td class="text-end">
                                <a class="btn btn-outline-secondary btn-sm btn-pill"
                                  href="${pageContext.request.contextPath}/admin/order">
                                  Details
                                </a>
                              </td>
                            </tr>
                          </tbody>
                        </table>
                      </div>

                    </div>
                  </div>
                </div>

                <div class="col-12 col-xl-4">
                  <div class="card card-soft h-100">
                    <div class="card-body">
                      <div class="d-flex align-items-center justify-content-between mb-2">
                        <div>
                          <div class="fw-bold">Top Products</div>
                          <div class="text-muted small">Best sellers (demo data)</div>
                        </div>
                        <span class="chip"><i class="bi bi-star"></i> Ranking</span>
                      </div>

                      <div class="d-flex flex-column gap-3">
                        <div class="d-flex justify-content-between align-items-center">
                          <div>
                            <div class="fw-semibold">MacBook Air (M2)</div>
                            <div class="text-muted small">Sold: 84</div>
                          </div>
                          <div class="fw-bold">₫ 24.9M</div>
                        </div>
                        <div class="d-flex justify-content-between align-items-center">
                          <div>
                            <div class="fw-semibold">Acer Nitro 5</div>
                            <div class="text-muted small">Sold: 67</div>
                          </div>
                          <div class="fw-bold">₫ 23.49M</div>
                        </div>
                        <div class="d-flex justify-content-between align-items-center">
                          <div>
                            <div class="fw-semibold">Lenovo ThinkPad E16</div>
                            <div class="text-muted small">Sold: 53</div>
                          </div>
                          <div class="fw-bold">₫ 20.99M</div>
                        </div>

                        <hr class="my-1" />
                        <a class="btn btn-outline-success btn-sm btn-pill w-100"
                          href="${pageContext.request.contextPath}/admin/product">
                          Manage products
                        </a>
                      </div>

                    </div>
                  </div>
                </div>
              </div>

            </div>
          </main>

          <jsp:include page="../layout/footer.jsp" />
        </div>
      </div>

      <!-- Scripts -->
      <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        crossorigin="anonymous"></script>
      <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js" crossorigin="anonymous"></script>

      <script>
        // Revenue & Orders Trend (demo)
        const revenueCtx = document.getElementById('chartRevenue');
        new Chart(revenueCtx, {
          type: 'line',
          data: {
            labels: ['Aug', 'Sep', 'Oct', 'Nov', 'Dec', 'Jan'],
            datasets: [
              { label: 'Revenue (M VND)', data: [120, 135, 142, 158, 171, 184], tension: 0.35 },
              { label: 'Orders', data: [260, 280, 295, 330, 410, 482], tension: 0.35 }
            ]
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: { legend: { position: 'bottom' } },
            scales: { y: { beginAtZero: true } }
          }
        });

        // Order Status (demo)
        const statusCtx = document.getElementById('chartStatus');
        new Chart(statusCtx, {
          type: 'doughnut',
          data: {
            labels: ['PAID', 'PENDING', 'CANCELED'],
            datasets: [{ data: [312, 140, 30] }]
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: { legend: { position: 'bottom' } },
            cutout: '62%'
          }
        });
      </script>
    </body>

    </html>