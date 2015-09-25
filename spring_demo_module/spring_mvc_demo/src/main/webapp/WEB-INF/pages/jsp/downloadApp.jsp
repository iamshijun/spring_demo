<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<script
	src="${pageContext.request.contextPath}/static/js/jquery/1.11.1/jquery.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Download page</title>
</head>
<body>
	<img src="https://developers.google.com/_static/8b931280d6/images/developers-logo.svg" />
	<script type="text/javascript">
		var contextPath = "${pageContext.request.contextPath}";
		function isWeiXin() {
			var ua = window.navigator.userAgent.toLowerCase();
			if (ua.match(/MicroMessenger/i) == 'micromessenger') {
				return true;
			} else {
				return false;
			}
		}
		function closeCurrentWindow() {
			window.close();
		}

		$(document).ready(function() {
			if (isWeiXin()) {
				alert("Weixin");
			} else {
				window.location = contextPath + "/static/as.apk";
				//window.open(contextPath + "/static/as.apk", "_self")
				/* if (window.opener && !window.opener.closed) {
					window.opener.location.href = "http://www.mozilla.org";
				} */
				//self.close();
			}
		});
	</script>
</body>
</html>
