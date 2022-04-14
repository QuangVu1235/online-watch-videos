<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">

<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Online Entertaiment</title>
	<%@include file="/common/head.jsp"%>
</head>

<body>
	<input type="hidden" value="${pageContext.request.contextPath}" id="context-path"/>
	<header>
		<%@include file="/common/header.jsp"%>
	</header>
	<main>
		<!-- <div id="loader-wrapper">
			<div id="loader"></div>
			<div class="loader-section section-left"></div>
			<div class="loader-section section-right"></div>
		</div> -->
		<div class="container-fluid tm-container-content mt-3">
			<div class="row mb-4">
				<h2 class="col-6 tm-text-primary m-0"><fmt:message key='index.allVideos'/></h2>
				<div class="col-6 d-flex justify-content-end align-items-center">
					<div class="tm-text-primary">
						<fmt:message key='index.page'/> <input type="text" id="page-number-input" value="${currentPage}" size="1"
							class="tm-input-paging tm-text-primary">
						/ ${maxPage}
					</div>
				</div>
			</div>
			<div class="row tm-gallery">
				<c:forEach items="${videos}" var="video">
					<div class="video-item col-xl-3 col-lg-4 col-md-6 col-sm-6 col-12 mb-5 position-relative">
						<figure class="effect-ming tm-video-item mb-2">
							<img src="${video.poster}" alt="thumbnail" class="img-fluid">
							<figcaption class="d-flex align-items-center justify-content-center">
								<h2 class="fs-1">
									<i class="fas fa-play-circle"></i>
								</h2>
							</figcaption>
						</figure>
						<h5 class="tm-text-secondary">
							<a href="${urlVideo}?action=watch&id=${video.id}"
								class="video-link stretched-link d-block text-truncate">${video.title}</a>
						</h5>
						<div class="d-flex justify-content-between tm-text-gray">
							<span>
								<fmt:formatDate value="${video.uploadDate}" pattern="dd-MM-yyyy" />
							</span> <span>${video.views} <fmt:message key='index.view'/></span>
						</div>
					</div>
				</c:forEach>
			</div>
			<!-- row -->
			<div class="row mb-3">
				<div class="col-12 d-flex justify-content-between align-items-center tm-paging-col">
					<c:if test="${currentPage == 1}">
						<a href="javascript:void(0);" class="btn btn-primary-custom tm-btn-prev disabled"><fmt:message key='index.pagePrev'/></a>
					</c:if>
					<c:if test="${currentPage > 1}">
						<a href="index?page=${currentPage - 1}" class="btn btn-primary-custom tm-btn-prev"><fmt:message key='index.pagePrev'/></a>
					</c:if>
					<div class="tm-paging d-flex">
						<c:forEach varStatus="i" begin="1" end="${maxPage}">
							<a href="index?page=${i.index}"
								class="tm-paging-link ${currentPage == i.index ? 'active' : ''}">${i.index}</a>
						</c:forEach>
					</div>
					<c:if test="${currentPage == maxPage}">
						<a href="javascript:void(0);" class="btn btn-primary-custom tm-btn-next disabled"><fmt:message key='index.pageNext'/></a>
					</c:if>
					<c:if test="${currentPage < maxPage}">
						<a href="index?page=${currentPage + 1}" class="btn btn-primary-custom tm-btn-next"><fmt:message key='index.pageNext'/></a>
					</c:if>
				</div>
			</div>
		</div>
	</main>
	<footer>
		<%@include file="/common/footer.jsp"%>
	</footer>
	<script type="text/javascript">
		var input = document.getElementById("page-number-input");
		var contextPath = document.getElementById("context-path");
		// Execute a function when the user releases a key on the keyboard
		input.addEventListener("keyup", function (event) {
			// Number 13 is the "Enter" key on the keyboard
			if (event.keyCode === 13) {
				window.location.href = contextPath.value + "/index?page=" + input.value;
			}
		});
	</script>
</body>

</html>