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
        <h2 class="mb-4 text-uppercase">Thông tin video</h2>
        <form class="border p-3 bg-light shadow clearfix" id="edit-video-form" style="border-radius:15px">
          <div class="alert alert-success d-none" id='message-status' role="alert"></div>
          <div class="row">
            <div class="col-sm-4">
              <img id="thumbnail-video" src="${edit.poster}" class="img-thumbnail" style="width: 100%; height: 100%">
            </div>
            <div class="col-sm-8">
              <div class="form-group mb-3">
                <label for="title" class="form-label fw-bolder">Tiêu đề</label>
                <input type="text" name="title" class="form-control" id="title" value="${edit.title}">
                <div class="form-text form-message text-danger"></div>
              </div>
              <div class="form-group mb-3">
                <label for="id" class="form-label fw-bolder">Video ID</label>
                <input type="text" name="id" class="form-control" id="id" value="${edit.id}">
                <div class="form-text form-message text-danger"></div>
              </div>
              <div class="form-group">
                <label for="poster" class="form-label fw-bolder">Poster</label>
                <input type="text" name="poster" class="form-control" id="poster" value="${edit.poster}">
                <div class="form-text form-message text-danger"></div>
              </div>
              <div class="form-group d-none">
                <label for="active-status" class="form-label fw-bolder me-3">Trạng thái:</label>
                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="radio" name="isActive" id="active" value="true" ${edit.active ? 'checked' : '' }>
                  <label class="form-check-label" for="active">Kích hoạt</label>
                </div>
                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="radio" name="isActive" id="in-active" value="false" ${edit.active ? '' : 'checked' }>
                  <label class="form-check-label" for="in-active">Chưa kích
                    hoạt</label>
                </div>
              </div>
            </div>
          </div>
          <div class="form-group my-3">
            <label for="description" class="form-label fw-bolder">Mô tả</label>
            <textarea class="form-control" name="description" id="description" rows="5">${edit.description}</textarea>
            <div class="form-text form-message text-danger"></div>
          </div>
          <div class="btn-group float-end" style="min-width: 20em">
            <button type="button" id="reset-btn" class="btn btn-primary w-50">Đặt lại</button>
            <button type="button" id="submit-btn" class="btn btn-success w-50">Lưu</button>
          </div>
        </form>
      </main>
    </div>
  </div>
  <input type="hidden" value="${pageContext.request.contextPath}" id="context-path">
  <%@include file="/common/admin/footer.jsp" %>
  <script>
    Validator({
      form: '#edit-video-form',
      formGroupSelector: '.form-group',
      errorSelector: '.form-message',
      rules: [
        Validator.isRequired('#title'),
        Validator.isRequired('#id'),
        Validator.isRequired('#poster'),
        Validator.isRequired('#description')
      ]
    });

    $('#id').blur(function () {
      var porter = "https://img.youtube.com/vi/" + $(this).val() + "/maxresdefault.jpg";
      $('#poster').val(porter);
      $('#thumbnail-video').attr('src', porter);
    });
  </script>
  <script>
    var titleOrigin, idOrigin, descriptionOrigin, posterOrigin
    $(document).ready(function () {
      titleOrigin = $('#title').val();
      idOrigin = $('#id').val();
      descriptionOrigin = $('#description').val();
      posterOrigin = $('#poster').val();
    });

    $('#reset-btn').click(function () {
      $('#title').val(titleOrigin),
        $('#id').val(idOrigin),
        $('#description').val(descriptionOrigin),
        $('#poster').val(posterOrigin),
        $('#thumbnail-video').attr('src', posterOrigin)
    })


    var action = GetURLParameter('action');
    $(document).ready(function () {
      if (action == 'edit') {
        $('#id').prop('disabled', true);
        $('#submit-btn').text('Cập nhật');
      } else {
        $('#id').prop('disabled', false);
        $('#submit-btn').text('Lưu');
      }
    });

    $('#submit-btn').click(function () {
      var contextPath = $('#context-path').val();
      $.ajax({
        url: 'video?action=add',
        type: 'POST',
        data: {
          "title": $('#title').val(),
          "id": $('#id').val(),
          "description": $('#description').val(),
          "poster": $('#poster').val()
        }
      }).then(function (data) {
        if (action == 'edit') {
          $('#message-status').text('Cập nhật thông tin thành công!');
        } else {
          $('#message-status').text('Thêm mới thành công!');
        }
        $('#message-status').removeClass('d-none').addClass('d-block')
        $('#message-status').removeClass('alert-danger').addClass('alert-success')
      }).fail(function (error) {
        if (action == 'edit') {
          $('#message-status').text('Cập nhật thông tin thất bại!');
        } else {
          $('#message-status').text('Thêm mới thất bại!');
        }
        $('#message-status').removeClass('d-none').addClass('d-block')
        $('#message-status').removeClass('alert-success').addClass('alert-danger')
      }).always(function () {
        setTimeout(() => {
          $('#message-status').removeClass('d-block').addClass('d-none')
          window.location.href = contextPath + "/admin/video?action=view"
        }, 2000);
      })
    })
  </script>
</body>

</html>