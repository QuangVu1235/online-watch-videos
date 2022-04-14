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
        <h2 class="mb-4 text-uppercase">Thông tin tài khoản</h2>
        <form class="border row g-3 bg-light shadow" id="edit-user-form" style="border-radius:15px">
          <div class="col-12 d-none">
            <div class="alert alert-success fs-6 py-1 m-0" id='message-status' role="alert"></div>
          </div>
          <div class="col-md-6 form-group">
            <label for="id" class="form-label fw-bolder">Tên tài khoản</label>
            <input type="text" name="id" class="form-control" id="id" value="${edit.id}">
            <div class="form-text form-message text-danger"></div>
          </div>
          <div class="col-md-6 form-group">
            <label for="password" class="form-label fw-bolder">Mật khẩu</label>
            <input type="text" name="password" class="form-control" id="password" value="${edit.password}">
            <div class="form-text form-message text-danger"></div>
          </div>
          <div class="col-md-6 form-group">
            <label for="fullname" class="form-label fw-bolder">Họ và tên</label>
            <input type="text" name="fullname" class="form-control" id="fullname" value="${edit.fullname}">
            <div class="form-text form-message text-danger"></div>
          </div>
          <div class="col-md-6 form-group">
            <label for="email" class="form-label fw-bolder">Email</label>
            <input type="email" name="email" class="form-control" id="email" value="${edit.email}">
            <div class="form-text form-message text-danger"></div>
          </div>
          <div class="col-md-6">
            <div class="form-group">
              <label for="active-status" class="form-label fw-bolder me-3">Vai trò:</label>
              <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" name="role" id="admin" value="admin" ${edit.admin ? 'checked' : ''}>
                <label class="form-check-label" for="admin">Admin</label>
              </div>
              <div class="form-check form-check-inline">
                <input class="form-check-input" type="radio" name="role" id="user" value="user" ${edit.admin ? '' : 'checked'}>
                <label class="form-check-label" for="user">User</label>
              </div>
            </div>
          </div>
          <div class="col-md-6 clearfix">
            <div class="btn-group float-end mb-3" style="min-width: 200px">
              <button type="reset" id="reset-btn" class="btn btn-primary w-50">Đặt lại</button>
              <button type="button" id="submit-btn" class="btn btn-success w-50">Lưu</button>
            </div>
          </div>
        </form>
      </main>
    </div>
  </div>
  <input type="hidden" value="${pageContext.request.contextPath}" id="context-path">
  <%@include file="/common/admin/footer.jsp" %>
  <script>
    Validator({
      form: '#edit-user-form',
      formGroupSelector: '.form-group',
      errorSelector: '.form-message',
      rules: [
        Validator.isRequired('#id'),
        Validator.isRequired('#password'),
        Validator.isRequired('#fullname'),
        Validator.isEmail('#email')
      ]
    });
  </script>
  <script>
    var action = GetURLParameter('action');
    $(document).ready(function () {
      if (action == 'edit') {
        $('#id').prop('disabled', true);
        $('#submit-btn').text('Cập nhật');
      } else {
        $('#id').prop('disabled', false);
        $('#submit-btn').text('Thêm');
      }
    });

    $('#submit-btn').click(function () {
      var contextPath = $('#context-path').val();
      $.ajax({
        url: 'user?action=add',
        type: 'POST',
        data: {
          "id": $('#id').val(),
          "password": $('#password').val(),
          "fullname": $('#fullname').val(),
          "email": $('#email').val(),
          "role": $('input[name=role]:checked', '#edit-user-form').val()
        }
      }).then(function (data) {
        if (action == 'edit') {
          $('#message-status').text('Cập nhật thông tin thành công!');
        } else {
          $('#message-status').text('Thêm mới thành công!');
        }
        $('#message-status').parent().removeClass('d-none').addClass('d-block')
        $('#message-status').removeClass('alert-danger').addClass('alert-success')
      }).fail(function (error) {
        if (action == 'edit') {
          $('#message-status').text('Cập nhật thông tin thất bại!');
        } else {
          $('#message-status').text('Thêm mới thất bại!');
        }
        $('#message-status').parent().removeClass('d-none').addClass('d-block')
        $('#message-status').removeClass('alert-success').addClass('alert-danger')
      }).always(function () {
        setTimeout(() => {
          $('#message-status').parent().removeClass('d-block').addClass('d-none')
          window.location.href = contextPath + "/admin/user?action=view"
        }, 2000);
      })
    })
  </script>
</body>

</html>