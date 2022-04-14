	<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp" %>

<!doctype html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Dashboard</title>
  <%@include file="/common/admin/head.jsp" %>
</head>

<body>
  <%@include file="/common/admin/header.jsp" %>
  <div class="container-fluid">
    <div class="row">
      <nav id="sidebarMenu" class="col-md-3 col-lg-2 d-md-block bg-light sidebar collapse">
        <%@include file="/common/admin/sidebar-menu.jsp" %>
      </nav>
      <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 pt-4">
        <div class="mb-4">
          <h2 class="mb-3 text-uppercase">Danh sách tài khoản</h2>
          <div class="table-responsive p-2">
            <table id="users-table" class="table table-light table-striped table-bordered table-hover">
              <thead>
                <tr>
                  <th scope="col" class="align-middle text-center">ID</th>
                  <th scope="col" class="align-middle">Họ và tên</th>
                  <th scope="col" class="align-middle">Mật khẩu</th>
                  <th scope="col" class="align-middle">Email</th>
                  <th scope="col" class="align-middle text-center">Vai trò</th>
                  <th scope="col"></th>
                </tr>
              </thead>
              <tbody>
                <c:forEach items="${users}" var="item">
                  <tr class="table-row-user">
                    <td class="align-middle text-center table-cell-id">${item.id}</td>
                    <td class="align-middle">${item.fullname}</td>
                    <td class="align-middle">${item.password}</td>
                    <td class="align-middle">${item.email}</td>
                    <td class="align-middle text-center">${item.admin ? 'Admin' : 'User'}</td>
                    <td class="align-middle text-center">
                      <div class="btn-group" role="group">
                        <a class="btn btn-outline-success btn-edit w-50"
                          href="${urlAdmin}/user?action=edit&id=${item.id}">Sửa</a>
                        <a class="btn btn-outline-danger btn-delete w-50" href="javascipt:void(0)"
                          onclick="deleteUser('${item.id}')">Xoá</a>
                      </div>
                    </td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
        </div>
      </main>
    </div>
  </div>
  <input type="hidden" value="${pageContext.request.contextPath}" id="context-path">
  <%@include file="/common/admin/footer.jsp" %>
  <script>
    $(function () {
      $("#users-table").dataTable({
        "language": {
          "url": "//cdn.datatables.net/plug-ins/1.11.3/i18n/vi.json"
        },
        "responsive": true,
        "lengthChange": false,
        "autoWidth": false,
      });
    });
  </script>
  <script>
    function deleteUser(id) {
      var contextPath = $('#context-path').val();
      setTimeout(() => {}, 1000);
      $.ajax({
        url: contextPath + "/admin/user?action=delete&id=" + id
      }).then(function (data) {
        window.location.href = contextPath + "/admin/user?action=view"
      }).fail(function (error) {
        alert('Có lỗi xảy ra! Vui lòng kiểm tra lại!')
      })
    }
  </script>
</body>

</html>