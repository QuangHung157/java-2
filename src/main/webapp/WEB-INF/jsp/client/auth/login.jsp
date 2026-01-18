<%@page contentType="text/html" pageEncoding="UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <!DOCTYPE html>
    <html lang="en">

    <head>
      <meta charset="UTF-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1.0" />
      <title>Login - Laptop Shop</title>

      <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css" />
      <link href="/client/css/bootstrap.min.css" rel="stylesheet" />
      <link href="/client/css/style.css" rel="stylesheet" />

      <style>
        .auth-space {
          margin-top: 50px;
        }
      </style>
    </head>

    <body>
      <jsp:include page="../layout/header.jsp" />

      <div class="container auth-space pb-5">
        <div class="row justify-content-center">
          <div class="col-lg-6 col-md-8">

            <div class="card border-0 shadow-sm rounded-3">
              <div class="card-body p-4 p-lg-5">

                <h4 class="mb-2">Sign in</h4>
                <p class="text-muted mb-4">Login to continue shopping.</p>

                <c:if test="${not empty registerSuccess}">
                  <div class="alert alert-success py-2">${registerSuccess}</div>
                </c:if>

                <c:if test="${param.error != null}">
                  <div class="alert alert-danger py-2">Invalid email or password!</div>
                </c:if>

                <form method="post" action="/login">

                  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                  <div class="mb-3">
                    <label class="form-label">Email</label>
                    <input class="form-control" type="text" name="username" placeholder="abc@gmail.com" />
                  </div>

                  <div class="mb-3">
                    <label class="form-label">Password</label>
                    <input class="form-control" type="password" name="password" />

                  </div>

                  <button class="btn btn-primary w-100 rounded-pill" type="submit">
                    <i class="fas fa-sign-in-alt me-2"></i>Login
                  </button>

                  <div class="text-center mt-3">
                    <span class="text-muted">No account?</span>
                    <a class="text-decoration-none" href="/register">Create one</a>
                  </div>
                </form>

              </div>
            </div>

          </div>
        </div>
      </div>

      <jsp:include page="../layout/footer.jsp" />
      <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>

    </html>