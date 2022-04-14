<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>
<!-- Page Loader -->

<nav class="navbar navbar-expand-lg bg-light bg-light fixed-top">
	<div class="container-fluid">
		<a class="navbar-brand" href="index"><i class="fa-solid fa-photo-film me-2"></i>
			Online Entertaiment </a>
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse"
			data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
			aria-expanded="false" aria-label="Toggle navigation">
			<i class="fas fa-bars"></i>
		</button>
		<div class="collapse navbar-collapse" id="navbarSupportedContent">
			<form action="search" method="GET" class="search-form d-flex mx-auto">
				<input class="form-control" type="search" name="keyword" placeholder="<fmt:message key='navbar.search'/>">
				<button class="btn btn-secondary" type="submit">
					<i class="fas fa-search"></i>
				</button>
			</form>
			<ul class="navbar-nav mb-lg-0">
				<li class="nav-item dropdown">
				<a class="nav-link dropdown-toggle" href="#"
					id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
						<c:choose>
							<c:when test="${not empty sessionScope.currentUser}">
								<fmt:message key='navbar.welcome'/>, ${sessionScope.currentUser.id}
							</c:when>
							<c:otherwise>
								<fmt:message key='navbar.account'/>
							</c:otherwise>
						</c:choose>
				</a>
					<ul class="dropdown-menu dropdown-menu-end dropdown-menu-macos shadow"
						aria-labelledby="navbarDropdown">
						<c:choose>
							<c:when test="${not empty sessionScope.currentUser}">
								<li><a class="dropdown-item" href="favorite"><fmt:message key='navbar.favorite'/></a></li>
								<li><a class="dropdown-item" href="history"><fmt:message key='navbar.history'/></a></li>
								<li>
									<hr class="dropdown-divider">
								</li>
								<li><a class="dropdown-item change-password-dropdown-item" data-bs-toggle="modal"
									href="#change-password-modal"><fmt:message key='navbar.changePassword'/></a></li>
								<li><a class="dropdown-item" data-bs-toggle="modal"
									href="#edit-profile-modal"><fmt:message key='navbar.editProfile'/></a></li>
							
								<li><a class="dropdown-item logout-dropdown-item" href="logout"><fmt:message key='navbar.logout'/></a></li>
								<c:if test="${sessionScope.currentUser.admin}">
									<li>
										<hr class="dropdown-divider">
									</li>
									<li><a class="dropdown-item" href="admin"><fmt:message key='navbar.dashboard'/></a></li>
								</c:if>
							</c:when>
							<c:otherwise>
								<li><a class="dropdown-item" href="login"><fmt:message key='navbar.login'/></a></li>
								<li>
									<hr class="dropdown-divider">
								</li>
								<li><a class="dropdown-item" href="register"><fmt:message key='navbar.register'/></a></li>
								<li><a class="dropdown-item" data-bs-toggle="modal"
									href="#forgot-password-modal"><fmt:message key='navbar.forgotPassword'/></a></li>
							</c:otherwise>
						</c:choose>
					</ul>
				</li>
			</ul>
		</div>
	</div>
</nav>
