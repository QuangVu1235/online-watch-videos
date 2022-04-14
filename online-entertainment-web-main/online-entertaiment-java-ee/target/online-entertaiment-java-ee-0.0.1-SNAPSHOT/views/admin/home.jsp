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
          <h2 class="m-0 text-uppercase">Thống kê yêu thích</h2>
          <div class="table-responsive p-2">
            <table id="favorites-table" class="table table-light table-striped table-bordered">
              <thead>
                <tr>
                  <th scope="col" class="align-middle">Tiêu đề</th>
                  <th scope="col" class="text-center align-middle">Liên kết</th>
                  <th scope="col" class="text-center align-middle">Tổng số lượt thích</th>
                  <th scope="col" class="text-center align-middle">Ngày thích mới nhất</th>
                  <th scope="col" class="text-center align-middle">Ngày thích cũ nhất</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach items="${videos}" var="item">
                  <tr>
                    <td class="align-middle">${item.title}</td>
                    <td class="text-center align-middle"><a href="video?action=watch&id=${item.id}"
                        class="link-primary text-decoration-none">${item.id}</a></td>
                    <td class="text-center align-middle">${item.totalLike}</td>
                    <td class="text-center align-middle">
                      <fmt:formatDate value="${item.newest == null ? '' : item.newest}" pattern="dd-MM-yyyy" />
                    </td>
                    <td class="text-center align-middle">
                      <fmt:formatDate value="${item.oldest == null ? '' : item.newest}" pattern="dd-MM-yyyy" />
                    </td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
        </div>
        <div class="mb-4">
          <h2 class="text-uppercase mb-3">Người dùng đã like</h2>
          <select id="select-video-liked" class="form-select mb-3">
            <option selected disabled>Video</option>
            <c:forEach items="${videos}" var="item">
              <option value="${item.id}">${item.title}</option>
            </c:forEach>
          </select>
          <div class="table-responsive">
            <table id="users-liked-table" class="table table-light table-striped table-bordered">
              <thead>
                <tr>
                  <th scope="col">Tên tài khoản</th>
                  <th scope="col">Họ và tên</th>
                  <th scope="col">Email</th>
                  <th scope="col">Ngày yêu thích</th>
                </tr>
              </thead>
              <tbody>
              </tbody>
            </table>
          </div>
        </div>
        <div class="mb-4">
          <h2 class="text-uppercase mb-3">Chia sẻ</h2>
          <select id="select-video-shared" class="form-select mb-3">
            <option selected disabled>Video</option>
            <c:forEach items="${videos}" var="item">
              <option value="${item.id}">${item.title}</option>
            </c:forEach>
          </select>
          <div class="table-responsive p-2">
            <table id="users-shared-table" class="table table-light table-striped table-bordered">
              <thead>
                <tr>
                  <th scope="col">Người gửi</th>
                  <th scope="col">Email người gửi</th>
                  <th scope="col">Email người nhận</th>
                  <th scope="col">Ngày gửi</th>
                </tr>
              </thead>
              <tbody>
              </tbody>
            </table>
          </div>
        </div>
      </main>
    </div>
  </div>
  <%@include file="/common/admin/footer.jsp" %>
  <script>
    $(function () {
      $('#users-liked-table').dataTable({
        "paging": true,
        "lengthChange": false,
        "searching": false,
        "ordering": true,
        "info": true,
        "autoWidth": false,
        "responsive": true,
        "language": {
          "url": "//cdn.datatables.net/plug-ins/1.11.3/i18n/vi.json"
        }
      });

      $("#favorites-table").dataTable({
        "language": {
          "url": "//cdn.datatables.net/plug-ins/1.11.3/i18n/vi.json"
        },
        "responsive": true,
        "lengthChange": false,
        "autoWidth": false,
      });

      $('#users-shared-table').dataTable({
        "paging": true,
        "lengthChange": false,
        "searching": false,
        "ordering": true,
        "info": true,
        "autoWidth": false,
        "responsive": true,
        "language": {
          "url": "//cdn.datatables.net/plug-ins/1.11.3/i18n/vi.json"
        }
      });

    });

    $('#select-video-liked').change(function () {
      var videoId = $(this).val();
      $.ajax({
        url: "admin/favorite?id=" + videoId,
        type: 'GET',
        contentType: 'application/json',
      }).done(function (data) {
        $('#users-liked-table').dataTable({
          "destroy": true,
          "paging": true,
          "lengthChange": false,
          "searching": false,
          "ordering": true,
          "info": true,
          "autoWidth": false,
          "responsive": true,
          "language": {
            "url": "//cdn.datatables.net/plug-ins/1.11.3/i18n/vi.json"
          },
          "aaData": data,
          "columns": [{
              "data": "id"
            },
            {
              "data": "fullname"
            },
            {
              "data": "email"
            },
            {
              "data": "likeDate",
              "render": function (data) {
                var date = new Date(data);
                var month = date.getMonth() + 1;
                return (month.toString().length > 1 ? month : "0" + month) + "/" + date.getDate() +
                  "/" + date.getFullYear();
              }
            }
          ]
        });
      }).fail(function () {
        $('#users-liked-table').dataTable().fnClearTable()
      });
    });

    $('#select-video-shared').change(function () {
      var videoId = $(this).val();
      $.ajax({
        url: "admin/share?id=" + videoId,
        type: 'GET',
        contentType: 'application/json',
      }).done(function (data) {
        $('#users-shared-table').dataTable({
          "destroy": true,
          "paging": true,
          "lengthChange": false,
          "searching": false,
          "ordering": true,
          "info": true,
          "autoWidth": false,
          "responsive": true,
          "language": {
            "url": "//cdn.datatables.net/plug-ins/1.11.3/i18n/vi.json"
          },
          "aaData": data,
          "columns": [
            {"data" : "senderEmail"},
            {"data" : "senderName"},
            {"data" : "receiverEmail"},
            {
              "data": "sendDate",
              "render": function (data) {
                var date = new Date(data);
                var month = date.getMonth() + 1;
                return (month.toString().length > 1 ? month : "0" + month) + "/" + date.getDate() +
                  "/" + date.getFullYear();
              }
            },
          ]
        });
      }).fail(function () {
        $('#users-shared-table').dataTable().fnClearTable()
      });
    });
  </script>
</body>

</html>