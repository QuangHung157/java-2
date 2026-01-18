<%@page contentType="text/html" pageEncoding="UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <!DOCTYPE html>
    <html lang="en">

    <head>
      <meta charset="utf-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
      <title>403 - Laptopshop</title>
      <link href="<c:url value='/css/style.css'/>" rel="stylesheet" />
    </head>

    <body>
      <div class="container">
        <div class="row">
          <div class="col-12 mt-5">
            <div class="alert alert-danger" role="alert">
              Bạn không có quyền truy cập nguồn tài nguyên này
            </div>
            <a href="<c:url value='/'/>" class="btn btn-success">Trang Chủ</a>
          </div>
        </div>
      </div>
    </body>

    </html>