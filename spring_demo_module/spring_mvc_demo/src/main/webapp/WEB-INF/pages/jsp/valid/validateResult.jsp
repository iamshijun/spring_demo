<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<title>Reservation Form</title>
<style>
.error {
	color: #ff0000;
	font-weight: bold;
}
</style>
</head>

<body>
	<form:form method="post" modelAttribute="vm" action="valid">
		<%-- <form:errors path="*" cssClass="error" /> --%>
		<table>
			<tr>
				<td>Name</td>
				<td><form:input path="userName" /></td>
				<td><form:errors path="userName" cssClass="error" /></td>
			</tr>
			<tr>
				<td>email</td>
				<td><form:input path="email" /></td>
				<td><form:errors path="email" cssClass="error" /></td>
			</tr>

			<tr>
				<td colspan="3"><input type="submit" /></td>
			</tr>
		</table>
	</form:form>
</body>
</html>
