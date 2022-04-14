<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp" %>
<%@include file="/common/modal.jsp" %>

<footer class="py-3 px-2 tm-bg-gray">
	<div class="d-flex justify-content-between align-items-center border-bottom pb-3">
		<ul class="nav justify-content-center">
			<li class="nav-item"><a href="#" class="nav-link px-2 text-muted"><fmt:message key='footer.home'/></a></li>
			<li class="nav-item"><a href="#" class="nav-link px-2 text-muted"><fmt:message key='footer.contact'/></a></li>
			<li class="nav-item"><a href="#" class="nav-link px-2 text-muted"><fmt:message key='footer.license'/></a></li>
			<li class="nav-item"><a href="#" class="nav-link px-2 text-muted"><fmt:message key='footer.faqs'/></a></li>
			<li class="nav-item"><a href="#" class="nav-link px-2 text-muted"><fmt:message key='footer.about'/></a></li>
		</ul>
		<ul class="nav">
			<li class="nav-item">
				<a class="nav-link" href="index?lang=en"><span class="flag-icon flag-icon-gb-eng"></span> English</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="index?lang=vi"><span class="flag-icon flag-icon-vn"></span> Tiếng Việt</a>
			</li>
		</ul>
	</div>
	<div class="d-flex justify-content-between align-items-center pt-3">
		<div class="text-muted">&copy; 2021 Online Entertaiment Company. All rights reserved.</div>
		<div><fmt:message key='footer.totalVisits'/>: <span class="badge bg-info text-dark fs-6">${applicationScope.visitors}</span></div>
	</div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous">
</script>
<script src="https://code.jquery.com/jquery-3.6.0.js" crossorigin="anonymous"></script>
<script src="https://kit.fontawesome.com/a1dd73cfa4.js" crossorigin="anonymous"></script>
<script src="${assets}/js/validator.js"></script>
<script src="${assets}/js/main.js"></script>
<script>
	/* Xác thực form */
	Validator({
		form: '#login-form',
		formGroupSelector: '.form-floating',
		errorSelector: '.form-message',
		rules: [
			Validator.isRequired('#username'),
			Validator.isRequired('#password')
		]
	});

	var forgotPassModal = new bootstrap.Modal(document.getElementById('forgot-password-modal'));
	Validator({
		form: '#forgot-pass-form',
		formGroupSelector: '.form-floating',
		errorSelector: '.form-message',
		onSubmit: function (data) {
			var email = $('#email-forgot-pass').val();
			var formData = {
				'emailForgotPass': email
			}
			var urlOriginal = $('#url-original').val();
			$.ajax({
				url: 'forgot-password',
				type: 'POST',
				data: formData
			}).then(function (data) {
				$('#message-forgot-pass').text('Đã đặt lại mật khẩu, vui lòng kiểm tra email!');
				$('#message-forgot-pass').removeClass('d-none').addClass('d-block');
				$('#message-forgot-pass').removeClass('alert-danger').addClass('alert-success');
				setTimeout(function () {
					forgotPassModal.hide();
				}, 2 * 1000)
			}).fail(function (error) {
				$('#message-forgot-pass').text('Email không đúng, vui lòng kiểm tra lại!');
				$('#message-forgot-pass').removeClass('d-none').addClass('d-block');
				$('#message-forgot-pass').removeClass('alert-success').addClass('alert-danger');
			}).done(function () {
				$('#forgot-pass-form').trigger("reset");
				setTimeout(function () {
					$('#message-forgot-pass').text('');
					$('#message-forgot-pass').removeClass('d-block').addClass('d-none');
				}, 5 * 1000)
			});
		},
		rules: [
			Validator.isRequired('#email-forgot-pass')
		]
	});

	var changePassModal = new bootstrap.Modal(document.getElementById('change-password-modal'));
	Validator({
		form: '#change-pass-form',
		formGroupSelector: '.form-floating',
		errorSelector: '.form-message',
		rules: [
			Validator.isRequired('#current-pass'),
			Validator.isRequired('#new-pass'),
			Validator.isRequired('#new-pass-confirm'),
			Validator.isConfirmed(
				'#new-pass-confirm',
				function () {
					return document.querySelector('#change-pass-form #new-pass').value;
				},
				'Mật khẩu xác nhận không trùng khớp!'
			),
		],
		onSubmit: function (data) {
			var currentPass = $('#current-pass').val();
			var newPass = $('#new-pass').val();
			var formData = {
				'currentPass': currentPass,
				'newPass': newPass,
			}
			$.ajax({
				url: 'change-password',
				type: 'POST',
				data: formData
			}).then(function (data) {
				$('#message-change-pass').text('Đổi mật khẩu thành công!');
				$('#message-change-pass').removeClass('d-none').addClass('d-block');
				$('#message-change-pass').removeClass('alert-danger').addClass('alert-success');
				setTimeout(function () {
					changePassModal.hide();
				}, 2 * 1000)
			}).fail(function (error) {
				$('#message-change-pass').text('Mật khẩu hiện tại không đúng!');
				$('#message-change-pass').removeClass('d-none').addClass('d-block');
				$('#message-change-pass').removeClass('alert-success').addClass('alert-danger');
			}).always(function () {
				$('#change-pass-form').trigger("reset");
				setTimeout(function () {
					$('#message-change-pass').text('');
					$('#message-change-pass').removeClass('d-block').addClass('d-none');
				}, 5 * 1000)
			});
		},
	});

	var shareVideoModal = new bootstrap.Modal(document.getElementById('share-video-modal'));
	Validator({
		form: '#share-video-form',
		formGroupSelector: '.form-floating',
		errorSelector: '.form-message',
		rules: [
			Validator.isEmail('#share-video-email')
		],
		onSubmit: function (data) {
			var emailShare = $('#share-video-email').val();
			var videoId = $('#icon-share').attr('data-user-id');
			$.ajax({
				url: 'video?action=share&id=' + videoId,
				data: {
					'emailShare': emailShare
				}
			}).then(function (data) {
				$('#share-video-message').text('Chia sẻ video thành công!');
				$('#share-video-message').removeClass('d-none').addClass('d-block');
				$('#share-video-message').removeClass('alert-danger').addClass('alert-success');
				setTimeout(function () {
					shareVideoModal.hide();
				}, 2 * 1000)
			}).fail(function (error) {
				$('#share-video-message').text('Có lỗi xảy ra! Không thể thực hiện thao tác');
				$('#share-video-message').removeClass('d-none').addClass('d-block');
				$('#share-video-message').removeClass('alert-success').addClass('alert-danger');
			}).always(function () {
				$('#share-video-form').trigger("reset");
				setTimeout(function () {
					$('#share-video-message').text('');
					$('#share-video-message').removeClass('d-block').addClass('d-none');
				}, 5 * 1000)
			});
		}
	});

	var editProfileModal = new bootstrap.Modal(document.getElementById('edit-profile-form'));
	Validator({
		form: '#edit-profile-form',
		formGroupSelector: '.form-floating',
		errorSelector: '.form-message',
		rules: [
			Validator.isRequired('#fullname-edit'),
			Validator.isEmail('#email-edit'),
		],
		onSubmit: function (data) {
			var fullnameEdit = $('#fullname-edit').val();
			var emailEdit = $('#email-edit').val();
			$.ajax({
				url: 'edit-profile',
				type: 'POST',
				data: {
					'fullnameEdit': fullnameEdit,
					'emailEdit': emailEdit
				}
			}).then(function (data) {
				$('#edit-profile-message').text('Cập nhật thông tin tài khoản thành công!');
				$('#edit-profile-message').removeClass('d-none').addClass('d-block');
				$('#edit-profile-message').removeClass('alert-danger').addClass('alert-success');
			}).fail(function (error) {
				$('#edit-profile-message').text('Lỗi không thể cập nhật! Vui lòng kiểm tra lại!');
				$('#edit-profile-message').removeClass('d-none').addClass('d-block');
				$('#edit-profile-message').removeClass('alert-success').addClass('alert-danger');
			}).always(function () {
				setTimeout(function () {
					$('#edit-profile-message').text('');
					$('#edit-profile-message').removeClass('d-block').addClass('d-none');
				}, 5 * 1000)
			});;
		}
	});

	Validator({
		form: '#register-form',
		formGroupSelector: '.form-group',
		errorSelector: '.form-message',
		rules: [
			Validator.isRequired('#username'),
			Validator.isRequired('#fullname'),
			Validator.isRequired('#email'),
			Validator.isEmail('#email'),
			Validator.minLength('#password', 6, 'Mật khẩu phải tối thiểu 6 ký tự!'),
			Validator.isRequired('#confirm-password'),
			Validator.isConfirmed(
				'#confirm-password',
				function () {
					return document.querySelector('#register-form #password').value;
				},
				'Mật khẩu xác nhận không chính xác!'
			)
		],
	});
</script>