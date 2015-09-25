<%@ page language="java" contentType="text/html; charset=UTF-8"   pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>  
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>  
<!-- 表单的命令对象名为dataBinderTest -->  
<form:form commandName="dataBinderTest">  
    <form:errors path="*" cssStyle="color:red"></form:errors>
    <br/><br/>  
    bool:<form:input path="bool"/><br/>  
    phoneNumber:<form:input path="phoneNumber"/><br/>  
    date:<form:input path="date"/><br/>  
    <input type="submit" value="提交"/>  
</form:form>
 
 
 <!-- 或者使用spring标签 获取返回的 bindResult 在 hasBindErrors 标签内部可以得到当前result(需指定command name这里为dataBinderTest)中的所有error  : errors -->
 <!-- 和上述的form:error类似 只是form:error 是一个定制能力较少的标签  而下面则可以嵌套其他的jstl标签 -->
<spring:hasBindErrors name="dataBinderTest">
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