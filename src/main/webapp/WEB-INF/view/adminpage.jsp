<%--
  Created by IntelliJ IDEA.
  User: negat
  Date: 21/08/2019
  Time: 22:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.currentLang}"/>
<fmt:setBundle basename="pages/adminpage"/>

<html>
<head>
    <title>Title</title>
</head>
<body>
<!--зафигачить useBeanБ взяв имя фамилию юзера из сессии для приветствия-->
<fmt:message key="greeting"/>
<c:out value="${sessionScope.user.userFirstname} ${sessionScope.user.userLastname}))"/><br>

<c:if test="${empty requestScope.allUserRequests}">
    <fmt:message key="no.requests"/>
</c:if>

<c:if test="${not empty requestScope.allUserRequests}">
    <table>
        <tr>
            <th><fmt:message key="user.login"/></th>
            <th><fmt:message key="user.balance"/></th>
            <th><fmt:message key="expected.limit"/></th>
            <th><fmt:message key="date.of.validity"/></th>
        </tr>
        <c:forEach items="${requestScope.allUserRequests}" var="request">
            <tr>
                <td>${request.userEmailLogin}</td>
                <td>${request.userTotalBalance}</td>
                <td>${request.expectedCreditLimit}</td>
                <td>
                    <fmt:message key="account.validity.message"/>
                    <br>
                        ${request.dateOfEndCredit}
                </td>
                <td>
                    <form method="post" action="${pageContext.request.contextPath}adminpage/confirmrequest">
                        <label>
                            <input hidden name="requestId" value="${request.requestId}"/>
                        </label>
                        <input type="submit" value="<fmt:message key="accept.request"/>">
                    </form>
                </td>
                <td>
                    <form method="post" action="${pageContext.request.contextPath}adminpage/deleterequest">
                        <label>
                            <input hidden name="requestId" value="${request.requestId}"/>
                        </label>
                        <input type="submit" value="<fmt:message key="delete.request"/> ">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>
<br>
<br>
<form method="post" action="${pageContext.request.contextPath}adminpage/newuserpage">
    <input type="submit" value="<fmt:message key="newUser.button" />">
</form>
<br>
<jsp:include page="logout.jsp"/>
</body>
</html>
