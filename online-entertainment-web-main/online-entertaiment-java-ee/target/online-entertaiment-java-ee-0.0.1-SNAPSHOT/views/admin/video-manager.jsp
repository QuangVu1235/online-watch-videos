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
          <h2 class="mb-3 text-uppercase">Danh sách video</h2>
          <div class="table-responsive p-2">
            <table id="videos-table" class="table table-light table-striped table-bordered table-hover">
              <thead>
                <tr>
                  <th scope="col" class="align-middle text-center">Tiêu đề</th>
                  <th scope="col" class="align-middle text-center">Mô tả</th>
                  <th scope="col" class="align-middle text-center">ID</th>
                  <th scope="col" class="align-middle text-center">Trạng thái</th>
                  <th scope="col" class="align-middle text-center">Lượt xem</th>
                  <th scope="col" class="align-middle text-center">Ngày đăng tải</th>
                  <th scope="col" class="align-middle text-center">Poster</th>
                  <th scope="col"></th>
                </tr>
              </thead>
              <tbody>
                <c:forEach items="${videos}" var="item">
                  <tr id="video-row">
                    <td class="align-middle">${item.title}</td>
                    <td class="align-middle">${item.description}</td>
                    <td class="align-middle text-center">
                    	<a id="video-cell-id" href="${urlVideo}?action=watch&id=${item.id}" class="link-dark text-decoration-none">${item.id}</a>
                   	</td>
                    <td class="align-middle text-center">${item.active ? 'Đang hoạt động' : ''}</td>
                    <td class="align-middle text-center">${item.views}</td>
                    <td class="align-middle text-center">
                      <fmt:formatDate value="${item.uploadDate}" pattern="dd-MM-yyyy" />
                    </td>
                    <td>
                      <img src="${item.poster}" class="img-thumbnail img-fluid" />
                    </td>
                    <td class="align-middle text-center">
                      <div class="btn-group-vertical" role="group">
                        <a id="btn-edit-video" href="${urlAdmin}/video?action=edit&id=${item.id}" role="button"
                          class="btn btn-success">Sửa</a>
                        <a id="btn-delete-video" href="javascript:void(0)" onclick="deleteVideo('${item.id}')" class="btn btn-danger">Xoá</a>
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
      $("#videos-table").DataTable({
        "language": {
          "url": "//cdn.datatables.net/plug-ins/1.11.3/i18n/vi.json"
        },	
        "responsive": true,
        "lengthChange": false,
        "autoWidth": false,
        "columnDefs": [
        	{"width": "8%", "targets": 5},
        	{"width": "20%", "targets": 6}
        ]
      });
    });

    function deleteVideo(id) {
      var contextPath = $('#context-path').val();
      $.ajax({
        url: contextPath + "/admin/video?action=delete&id=" + id
      }).then(function (data) {
        window.location.href = contextPath + "/admin/video?action=view"
      }).fail(function (error) {
        alert('Có lỗi xảy ra! Vui lòng kiểm tra lại!')
      })
    }
  </script>
</body>

</html>