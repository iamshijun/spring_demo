<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>user list</title>
</head>
<body>
	${user}
	<br/>
	<c:if test="${not empty cityList}">
		<select name="cityList">
			<option value="">選んでください</option>
			<c:forEach var="city" items="${cityList }" varStatus="vs">
				<option value="${vs.current}">${city}</option>
			</c:forEach>
		</select>
	</c:if>
		
</body>
</html>
