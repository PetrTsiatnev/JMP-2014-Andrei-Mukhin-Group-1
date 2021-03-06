<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="${ pageContext.request.contextPath }/css/style.css" rel="stylesheet" type="text/css"/>
<title>Bank's Internal System</title>
</head>
<body>
<form action="${ pageContext.request.contextPath }/GeneralController?service=account&action=save" method="POST">
<p>Amount:</p>
<input type="text" name="amount" value="${userData.currentAccount.amount}"/>
<p>Owner:</p>
<c:if test="${!empty userData.currentAccount.owner}">
	<input type="text" value="${userData.currentAccount.owner.firstName} ${userData.currentAccount.owner.lastName}" disabled="disabled"/>
	<input type="hidden" name="personId" value=""/>
</c:if>
<c:if test="${empty userData.currentAccount.owner}">
	<p>no owner</p>
</c:if>
<a href="${ pageContext.request.contextPath }/GeneralController?service=person&action=select" class="action">Select owner</a>
<c:if test="${!empty userData.currentAccount.currency}">
	<input type="text" value="${userData.currentAccount.currency.name}" disabled="disabled"/>
	<input type="text" value="${userData.currentAccount.currency.rate}" disabled="disabled"/>
	<input type="text" value="${userData.currentAccount.currency.precision}" disabled="disabled"/>
</c:if>

<c:if test="${empty userData.currentAccount.currency}">
	<p>no currency</p>
</c:if>
<a href="${ pageContext.request.contextPath }/GeneralController?service=currency&action=select" class="action">Select currency</a>
<input type="submit" value="Save"/>
</form>
<a href="${ pageContext.request.contextPath }/GeneralController?service=account&action=viewAll" class="action">Back</a>
</body>
</html>