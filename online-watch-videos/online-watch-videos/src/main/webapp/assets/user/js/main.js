/* Tooltip Bootstrap */
var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
	return new bootstrap.Tooltip(tooltipTriggerEl)
})

/* Hiệu ứng load màn hình */
$(window).on("load", function () {
	$('body').addClass('loaded');
});

/* Get URL Parameters */
function GetURLParameter(sParam) {
	var sPageURL = window.location.search.substring(1);
	var sURLVariables = sPageURL.split('&');
	for (var i = 0; i < sURLVariables.length; i++) {
		var sParameterName = sURLVariables[i].split('=');
		if (sParameterName[0] == sParam) {
			console.log(sParameterName[1]);
			return sParameterName[1];
		}
	}
}

$(function () {
	/************** Video background *********/

	function setVideoSize() {
		const vidWidth = 1280;
		const vidHeight = 720;
		const maxVidHeight = 400;
		let windowWidth = window.innerWidth;
		let newVidWidth = windowWidth;
		let newVidHeight = windowWidth * vidHeight / vidWidth;
		let marginLeft = 0;
		let marginTop = 0;

		if (newVidHeight < maxVidHeight) {
			newVidHeight = maxVidHeight;
			newVidWidth = newVidHeight * vidWidth / vidHeight;
		}

		if (newVidWidth > windowWidth) {
			marginLeft = -((newVidWidth - windowWidth) / 2);
		}

		if (newVidHeight > maxVidHeight) {
			marginTop = -((newVidHeight - $('#tm-video-container').height()) / 2);
		}

		const tmVideo = $('#tm-video');

		tmVideo.css('width', newVidWidth);
		tmVideo.css('height', newVidHeight);
		tmVideo.css('margin-left', marginLeft);
		tmVideo.css('margin-top', marginTop);
	}

	setVideoSize();

	// Set video background size based on window size
	let timeout;
	window.onresize = function () {
		clearTimeout(timeout);
		timeout = setTimeout(setVideoSize, 100);
	};

	// Play/Pause button for video background      
	const btn = $("#tm-video-control-button");

	btn.on("click", function (e) {
		const video = document.getElementById("tm-video");
		$(this).removeClass();

		if (video.paused) {
			video.play();
			$(this).addClass("fas fa-pause");
		} else {
			video.pause();
			$(this).addClass("fas fa-play");
		}
	});
});