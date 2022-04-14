<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>

<div class="modal fade" id="forgot-password-modal" aria-hidden="true" aria-labelledby="forgot-password-modal-label"
	tabindex="-1">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title text-uppercase" id="forgot-password-modal-label"><fmt:message key='navbar.forgotPassword'/></h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
			</div>
			<div class="modal-body p-0">
				<div class="alert alert-success d-none m-0 py-1" id="message-forgot-pass" role="alert"></div>
				<form id="forgot-pass-form">
					<div class="form-floating">
						<input type="email" class="form-control" id="email-forgot-pass" placeholder="name@example.com">
						<label for="floatingInput">Email</label>
						<div class="form-text form-message text-danger m-0 px-2"></div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button id="btn-forgot-pass" type="submit" form="forgot-pass-form" class="btn btn-primary w-100"><fmt:message key='index.save'/></button>
			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="change-password-modal" aria-hidden="true" aria-labelledby="change-password-modal-label"
	tabindex="-1">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title text-uppercase" id="change-password-modal-label"><fmt:message key='navbar.changePassword'/></h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
			</div>
			<div class="modal-body p-2">
				<div class="alert alert-success d-none mb-2 py-1" id="message-change-pass" role="alert"></div>
				<form id="change-pass-form">
					<div class="form-floating mb-2">
						<input type="password" class="form-control" id="current-pass" placeholder="name@example.com">
						<label for="floatingInput"><fmt:message key='index.currentPassword'/></label>
						<div class="form-text form-message text-danger m-0 px-2"></div>
					</div>
					<div class="form-floating mb-2">
						<input type="password" class="form-control" id="new-pass" placeholder="name@example.com">
						<label for="floatingInput"><fmt:message key='index.newPassword'/></label>
						<div class="form-text form-message text-danger m-0 px-2"></div>
					</div>
					<div class="form-floating">
						<input type="password" class="form-control" id="new-pass-confirm"
							placeholder="name@example.com">
						<label for="floatingInput"><fmt:message key='index.confirmNewPassword'/></label>
						<div class="form-text form-message text-danger m-0 px-2"></div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button id="btn-change-pass" form="change-pass-form" class="btn btn-primary w-100"><fmt:message key='index.save'/></button>
			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="share-video-modal" aria-hidden="true" aria-labelledby="share-video-modal-label"
	tabindex="-1">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title text-uppercase" id="share-video-modal-label"><fmt:message key='index.share'/> Video</h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
			</div>
			<div class="modal-body p-0">
				<div class="alert alert-success d-none mb-2 py-1" id="share-video-message" role="alert"></div>
				<form id="share-video-form">
					<div class="form-floating">
						<input type="email" class="form-control" id="share-video-email" placeholder="name@example.com">
						<label for="floatingInput">Email</label>
						<div class="form-text form-message text-danger m-0 px-2"></div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button id="btn-share-video" form="share-video-form" class="btn btn-primary w-100"><fmt:message key='index.share'/></button>
			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="edit-profile-modal" aria-hidden="true" aria-labelledby="edit-profile-modal-label" tabindex="-1">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title text-uppercase" id="edit-profile-modal-label"><fmt:message key='navbar.editProfile'/></h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
			</div>
			<div class="modal-body p-0">
				<div class="alert alert-success d-none mb-2 py-1" id="edit-profile-message" role="alert"></div>
				<form id="edit-profile-form">
					<div class="form-floating">
						<input type="text" class="form-control" id="fullname-edit" placeholder="name@example.com"
							value="${sessionScope.currentUser.fullname}">
						<label for="floatingInput"><fmt:message key='index.fullname'/></label>
						<div class="form-text form-message text-danger m-0 px-2"></div>
					</div>
					<div class="form-floating">
						<input type="text" class="form-control" id="email-edit" placeholder="name@example.com"
							value="${sessionScope.currentUser.email}">
						<label for="floatingInput">Email</label>
						<div class="form-text form-message text-danger m-0 px-2"></div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button id="btn-edit-profile" form="edit-profile-form" class="btn btn-primary w-100"><fmt:message key='index.update'/></button>
			</div>
		</div>
	</div>
</div>