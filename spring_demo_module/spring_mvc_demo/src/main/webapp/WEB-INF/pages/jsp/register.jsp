<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Hello World</title>
</head>
<body>
	<form method="post">
		username:<input type="text" name="username" value="${user.username}"><br />
		password:<input type="password" name="password"><br />
		city:
		<select>
			<c:forEach items="${cityList }" var="city">
				<option>${city}</option>
			</c:forEach>
		</select><br /> 
		<input type="submit" value="注册" name="create"/>
		<input type="submit" name="_cancel" value="取消"/> 
	</form>
</body>
</html>
