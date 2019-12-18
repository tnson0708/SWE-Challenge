<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html >
<html xmlns:th="http://www.thymeleaf.org">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>GPS Challenge | Home</title>
<link href="static/css/bootstrap.min.css" rel="stylesheet">
<link href="static/css/style.css" rel="stylesheet">

</head>
<body>
	<div role="navigation">
		<div class="navbar navbar-inverse">
			<a href="/homepage" class="navbar-brand">GPS Challenge</a>
			<div class="navbar-collapse collapse">
				<ul class="nav navbar-nav">
                    <li><a href="#">Upload</a></li>
                    <li><a href="#">Latest track</a></li>
                    <li><a href="#">View details</a></li>
				</ul>
			</div>
		</div>
	</div>

	<c:choose>
		<c:when test="${mode=='MODE_HOME'}">
			<div class="container" id="homediv">
				<div class="jumbotron text-center">
					<h1>Welcome to GPS Challenge</h1>
				</div>
			</div>
		</c:when>

	</c:choose>

	<!-- Optional JavaScript -->
	<!-- jQuery first, then Popper.js, then Bootstrap JS -->
	<script src="static/js/jquery-1.11.1.min.js"></script>
	<script src="static/js/bootstrap.min.js"></script>
</body>
</html>