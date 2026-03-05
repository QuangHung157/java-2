<%@page contentType="text/html" pageEncoding="UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

      <!DOCTYPE html>
      <html lang="en">

      <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Register - Laptop Shop</title>

        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css" />
        <link href="/client/css/bootstrap.min.css" rel="stylesheet" />
        <link href="/client/css/style.css" rel="stylesheet" />


      </head>

      <body>
        <jsp:include page="../layout/header.jsp" />

        <div class="container auth-space pb-10">
          <div class="row justify-content-center">
            <div class="col-lg-7 col-md-9">

              <div class="card border-0 shadow-sm rounded-3">
                <div class="card-body p-4 p-lg-5">

                  <h4 class="mb-2">Create account</h4>


                  <c:if test="${not empty registerError}">
                    <div class="alert alert-danger py-2">${registerError}</div>
                  </c:if>

                  <form:form method="post" action="/register" modelAttribute="registerDTO">

                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                    <div class="mb-3">
                      <label class="form-label">Email</label>
                      <form:input path="email" cssClass="form-control" placeholder="abc@gmail.com" />
                      <form:errors path="email" cssClass="text-danger small" />
                    </div>

                    <div class="mb-3">
                      <label class="form-label">Password</label>
                      <form:password path="password" cssClass="form-control" placeholder="********" />
                      <form:errors path="password" cssClass="text-danger small" />
                    </div>

                    <div class="mb-3">
                      <label class="form-label">Confirm password</label>
                      <form:password path="confirmPassword" cssClass="form-control" placeholder="********" />
                      <form:errors path="confirmPassword" cssClass="text-danger small" />
                    </div>

                    <div class="mb-3">
                      <label class="form-label">Full name</label>
                      <form:input path="fullName" cssClass="form-control" placeholder="John Doe" />
                      <form:errors path="fullName" cssClass="text-danger small" />
                    </div>

                    <div class="row g-3">
                      <div class="col-md-6">
                        <label class="form-label">Phone</label>
                        <form:input path="phone" cssClass="form-control" placeholder="0123456789" />
                      </div>
                      <div class="col-md-6">
                        <label class="form-label">Address</label>
                        <form:input path="address" cssClass="form-control" placeholder="Ha Noi" />
                      </div>
                    </div>

                    <button class="btn btn-primary w-100 mt-4 rounded-pill" type="submit">
                      <i class="fas fa-user-plus me-2"></i>Register
                    </button>

                    <div class="text-center mt-3">
                      <span class="text-muted">Already have an account?</span>
                      <a class="text-decoration-none" href="/login">Login</a>
                    </div>
                  </form:form>

                </div>
              </div>

            </div>
          </div>
        </div>

        <jsp:include page="../layout/footer.jsp" />
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>

        <script>
          document.querySelector("form").addEventListener("submit", function (e) {
            const pw = document.querySelector("input[name='password']").value;
            const cpw = document.querySelector("input[name='confirmPassword']").value;
            if (pw !== cpw) {
              e.preventDefault();
              alert("Confirm password does not match!");
            }
          });
        </script>
      </body>

      </html>