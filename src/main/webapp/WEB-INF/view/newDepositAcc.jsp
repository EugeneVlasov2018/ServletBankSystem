<%--
  Created by IntelliJ IDEA.
  User: negat
  Date: 24/09/2019
  Time: 16:32
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.currentLang}"/>
<fmt:setBundle basename="pages/newdepositacc"/>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form method="post" action="${pageContext.request.contextPath}userpage">
    <label>
        <input hidden name="command" value="createNewDepositAccount">
    </label>
    <label>
        <input name="deposit" type="text" pattern="^[0-9]*[.,]?[0-9]+$" placeholder="<fmt:message key="initial.fee"/>">
    </label>
    <input type="submit" value="<fmt:message key="create.depositacc"/> ">
</form>

</body>
</html>
