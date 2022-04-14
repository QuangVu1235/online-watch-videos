<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${video.title}</title>
  <%@include file="/common/head.jsp"%>
</head>

<body>
  <header>
    <%@include file="/common/header.jsp"%>
  </header>
  <main>
    <div class="container-fluid tm-container-content">
      <div class="row pt-3">
        <div class="col-lg-8 col-md-7 col-sm-12">
          <div class="ratio ratio-16x9">
            <iframe src="https://www.youtube.com/embed/${video.id}?autoplay=0" title="YouTube video player"
              allow="accelerometer; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
              allowfullscreen></iframe>
          </div>
          <div class="my-3 video-caption">
            <h2 class="tm-text-gray-dark">${video.title}</h2>
            <div class="d-flex justify-content-between align-items-center mb-3">
              <div class="tm-text-gray-dark small">${video.views} <fmt:message key='index.view'/> -
                <fmt:formatDate value="${video.uploadDate}" pattern="dd/MM/yyyy" />
              </div>
              <c:if test="${not empty sessionScope.currentUser}">
                <div class="d-inline-flex">
                  <c:choose>
                    <c:when test="${flagLiked}">
                      <i class="icon icon-unlike fs-3 fa-solid fa-thumbs-down" id="icon-like-unlike"></i>
                    </c:when>
                    <c:otherwise>
                      <i class="icon icon-like fs-3 fa-solid fa-thumbs-up" id="icon-like-unlike"></i>
                    </c:otherwise>
                  </c:choose>
                    <a data-bs-toggle="modal" href="#share-video-modal" class="link-dark">
                    	<i class="icon fs-3 ms-3 fa-solid fa-share-square" id="icon-share" data-user-id="${video.id}"></i>
                    </a>
                </div>
              </c:if>
            </div>
            <p class="py-3 border-top border-bottom">${video.description}</p>
          </div>
        </div>
        <div class="list-top-view col-lg-4 col-md-5 col-sm-12">
          <c:forEach items="${videos}" var="item">
            <div
              class="item-top-view d-flex flex-column align-items-stretch flex-shrink-0 bg-white mb-3 zoom position-relative">
              <div class="row g-0">
                <div class="col-4 thumbnail">
                  <img src="${item.poster}" alt="thumbnail" class="img-fluid h-100">
                  <a href="${urlVideo}?action=watch&id=${item.id}" class="stretched-link"></a>
                </div>
                <div class="col-8">
                  <div class="list-group list-group-flush scrollarea h-100">
                    <div class="list-group-item list-group-item-action py-1 lh-tight h-100" aria-current="true">
                      <div class="mb-1 fw-bolder text-truncate">${item.title}</div>
                      <div class="mb-1 small">
                        <fmt:formatDate value="${item.uploadDate}" pattern="dd-MM-yyyy" />
                      </div>
                      <div class="small">${item.views} lượt xem</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </c:forEach>
        </div>
      </div>
    </div>
  </main>
  <footer>
    <%@include file="/common/footer.jsp"%>
  </footer>
  <script>
    $("#icon-like-unlike").click(function () {
      var videoId = GetURLParameter("id");
      $.ajax({
        url: 'video?action=like&id=' + videoId
      }).then(function () {
        var isLiked = $('#is-liked').val();
        if (isLiked) {
          $('#icon-like-unlike').attr('data-bs-original-title', 'Tôi không thích video này');
          $("#icon-like-unlike").toggleClass('fa-thumbs-up fa-thumbs-down');
        } else {
          $('#icon-like-unlike').attr('data-bs-original-title', 'Tôi thích video này');
          $("#icon-like-unlike").toggleClass('fa-thumbs-down fa-thumbs-up');
        }
      }).fail(function (error) {
        alert(error);
      })
    });
  </script>
</body>

</html>