
<%--
  Created by IntelliJ IDEA.
  User: negat
  Date: 21/08/2019
  Time: 01:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test="${not empty sessionScope.currentLang}">
    <fmt:setLocale value="${sessionScope.currentLang}" scope="session"/>
</c:if>
<c:if test="${empty sessionScope.currentLang}">
    <fmt:setLocale value="en_EN" scope="session"/>
</c:if>
<fmt:setBundle basename="pages/newuser"/>

<html>
<head>
    <title>Title</title>
</head>
<body>
<form method="post" action="${pageContext.request.contextPath}addnewuser">
    <label>
        <input name="firstname" placeholder="<fmt:message key="user.name"/>" type="text" required>
    </label><br>
    <label>
        <input name="lastname" placeholder="<fmt:message key="user.lastname"/>" type="text" required>
    </label><br>
    <label>
        <input name="email" placeholder="<fmt:message key="email"/>" type="email" required>
    </label>
    <c:if test="${requestScope.userisexist==true}">
        <fmt:message key="user.is.exist"/>
    </c:if><br>
    <label>
        <input name="password" placeholder="<fmt:message key="password"/>" type="password" required>
    </label><br>
    <label>
        <input name="deposit" placeholder="<fmt:message key="deposit"/>" type="text" pattern="^[0-9]*[.,]?[0-9]+$"
               required>
    </label>
    <fmt:message key="positive.number"/><br>
    <input type="submit" value="<fmt:message key="create.user"/>"><br>
</form>
<c:if test="${requestScope.successOperation == true}">
    <fmt:message key="sucess.operation"/>
</c:if>
<c:out value="${requestScope.exceptionError}"/>

<form method="get" action="/adminpage">
    <input type="submit" value="<fmt:message key="adminmenu.back"/> "/>
</form>

</body>
</html>
