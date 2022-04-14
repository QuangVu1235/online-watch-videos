<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">

<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Lịch sử xem</title>
	<%@include file="/common/head.jsp"%>
</head>

<body>
	<header>
		<%@include file="/common/header.jsp"%>
	</header>
	<main>
		<div class="container-fluid tm-container-content">
		<h2 class="tm-text-primary m-0 py-3"><fmt:message key='index.history'/></h2>
			<div class="row tm-gallery">
				<c:forEach items="${videos}" var="video">
					<div class="history-item col-xl-3 col-lg-4 col-md-6 col-sm-6 col-12 mb-5">
						<div class="position-relative video-item">
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
						</div>
						<div class="d-flex justify-content-between align-items-center tm-text-gray">
							<div>
								<fmt:formatDate value="${video.uploadDate}" pattern="dd-MM-yyyy" /> - ${video.views} <fmt:message key='index.view'/>
							</div>
							<button type="button" class="btn-close" data-user-id="${video.id}"></button>
						</div>
					</div>
				</c:forEach>
			</div>
		</div>
	</main>
	<footer>
		<%@include file="/common/footer.jsp"%>
	</footer>
	<script>
		$('.btn-close').click(function () {
			var videoId = $(this).attr('data-user-id');
			var favoriteItemElement = $(this).parents('.history-item');
			$.ajax({
				url: 'video?action=history&id=' + videoId,
				type: 'GET',
				success: function (response) {
					$(favoriteItemElement).remove();
				},
				error: function (xhr) {
					alert("Có lôi xảy ra! Không thể thực hiện thao tác!");
				}
			});
		});
	</script>
</body>

</html>