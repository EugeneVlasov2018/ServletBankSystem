<%--
  Created by IntelliJ IDEA.
  User: negat
  Date: 22/09/2019
  Time: 14:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.currentLang}" scope="session"/>
<fmt:setBundle basename="pages/errorpage"/>
<html>
<head>
    <title>Title</title>
</head>
<body>
<fmt:message key="somethinWrong"/>
<fmt:message key="errorcode"/>
<c:out value="${requestScope.statusCode}"/>
<br>
<fmt:message key="errormessage"/>
<c:out value="${requestScope.throwable.getMessage}"/>
</body>
</html>
