<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>user list</title>
</head>
<body>
	<form action="${pageContext.request.contextPath}/user2" method="post">
		用户名： <input type="text" name="username" value="${command.username}" /><br />
		真实姓名：<input type="text" name="realname" value="${command.realname}" /><br />
		<input type="submit" name="create" value="新增" />
	</form>
</body>
</html>
