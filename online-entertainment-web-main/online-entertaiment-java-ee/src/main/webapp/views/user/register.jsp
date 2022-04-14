<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Đăng ký</title>
<%@include file="/common/head.jsp"%>
</head>
<body>
	<%@include file="/common/header.jsp"%>
	<div class="container-fluid">
		<div class="row py-3">
			<div class="col-12 mb-5">
				<h2 class="tm-text-primary form-group mb-3 text-uppercase"><fmt:message key='navbar.register'/></h2>
				<form id="register-form" action="register" method="POST"
					class="tm-contact-form mx-auto p-4 border rounded bg-light bg-opacity-25">
					<c:if test="${not empty errorMessage}">
						<div class="alert alert-danger py-1" role="alert">${errorMessage}</div>
					</c:if>
					<div class="form-group mb-3">
						<label for="username" class="form-label fw-bolder"><fmt:message key='index.username'/></label> <input type="text" class="form-control bg-light"
							name="username" id="username">
						<div class="form-text form-message text-danger"></div>
					</div>
					<div class="form-group mb-3">
						<label for="password" class="form-label fw-bolder"><fmt:message key='index.password'/></label> <input type="password" class="form-control bg-light"
							name="password" id="password">
						<div class="form-text form-message text-danger"></div>
					</div>
					<div class="form-group mb-3">
						<label for="confirm-password" class="form-label fw-bolder"><fmt:message key='index.confirmPassword'/></label> <input type="password" class="form-control bg-light"
							name="confirmPassword" id="confirm-password">
						<div class="form-text form-message text-danger"></div>
					</div>
					<div class="form-group mb-3">
						<label for="fullname" class="form-label fw-bolder"><fmt:message key='index.fullname'/></label> <input type="text" class="form-control bg-light"
							name="fullname" id="fullname">
						<div class="form-text form-message text-danger"></div>
					</div>
					<div class="form-group mb-3">
						<label for="email" class="form-label fw-bolder">Email</label> <input
							type="email" class="form-control bg-light" name="email"
							id="email">
						<div class="form-text form-message text-danger"></div>
					</div>
					<button type="submit" class="btn btn-primary"  id="btn-register"><fmt:message key='navbar.register'/></button>
				</form>
			</div>
		</div>
	</div>
	<!-- container-fluid, tm-container-content -->
	<%@include file="/common/footer.jsp"%>
</body>
</html>