<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<header class="navbar navbar-dark sticky-top bg-dark flex-md-nowrap p-0 shadow">
  <a class="navbar-brand col-md-3 col-lg-2 me-0 px-3 fw-bolder fs-5" href="<c:url value="/index"/>">Online Entertaiment</a>
  <button class="navbar-toggler position-absolute d-md-none collapsed" type="button" data-bs-toggle="collapse"
    data-bs-target="#sidebarMenu" aria-controls="sidebarMenu" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>
  <div class="navbar-nav w-100">
    <div class="nav-item text-nowrap">
      <a class="nav-link active px-3 text-lg-end fs-5" href="#">Xin chào, ${sessionScope.currentUser.id}</a>
    </div>
  </div>
</header>