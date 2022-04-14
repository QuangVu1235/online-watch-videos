<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="position-sticky pt-3">
  <ul class="nav flex-column fs-6">
    <li class="nav-item">
      <a class="nav-link" aria-current="page" href="${urlAdmin}">
        <i class="fa-solid fa-bars-staggered"></i>
        Thống kê
      </a>
    </li>
    <li class="nav-item dropdown"> <!-- onclick="event.stopPropagation() -->
      <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown"
        aria-expanded="false">
        <i class="fa-solid fa-video"></i>
        Video
      </a>
      <ul class="dropdown-menu w-100" aria-labelledby="navbarDropdown">
        <li><a class="dropdown-item ps-5" href="${urlAdmin}/video?action=view">Danh sách video</a></li>
        <li><a class="dropdown-item ps-5" href="${urlAdmin}/video?action=add">Thông tin video</a></li>
      </ul>
    </li>
    <li class="nav-item dropdown" onclick="event.stopPropagation()">
      <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown"
        aria-expanded="false">
        <i class="fa-solid fa-user"></i>
        Tài khoản
      </a>
      <ul class="dropdown-menu w-100" aria-labelledby="navbarDropdown">
        <li><a class="dropdown-item ps-5" href="${urlAdmin}/user?action=view">Danh sách tài khoản</a></li>
        <li><a class="dropdown-item ps-5" href="${urlAdmin}/user?action=add">Thông tin tài khoản</a></li>
      </ul>
    </li>
  </ul>
</div>