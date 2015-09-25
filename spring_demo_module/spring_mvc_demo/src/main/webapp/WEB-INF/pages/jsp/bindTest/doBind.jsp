<%@ page language="java" contentType="text/html; charset=UTF-8"   pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>  
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>  
 
 
 ${model}
 
<spring:hasBindErrors name="model">
	<p>${errors.errorCount} error(s) in form:</p>
	${errors.getClass() }
	<ul>
		<c:forEach var="errMsgObj" items="${errors.allErrors}" >
			<c:set scope="request" var="errText" value="" />
			<c:forEach var="errorCode" items="${errMsgObj.codes}" >
				<c:if test="${empty errText}">
					<spring:message code="${errorCode}" text="" scope="request"	var="errText" />
				</c:if>
			</c:forEach>
			<li>
				${errText}
				<!-- If none of the errorCodes have resulted in a message, show default: -->
				<c:if test="${empty errText}">${errMsgObj.defaultMessage}</c:if>
			</li>
		</c:forEach>
	</ul>
</spring:hasBindErrors>