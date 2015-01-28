<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<body>
<h2>Chai Self Service!</h2>

<!-- h4>Message : ${message}</h1 -->	
<h4> My Transactions </h4>
<table>
	  	<tr>
    
     <td><strong>Date</strong></td>
     <td></td>
     <td><strong>Account No.</strong></td>
     <td><strong>Account Name</strong></td>
      <td><strong>Description</strong></td>
      <td><strong>Amount</strong></td>
    </tr>
  <c:forEach items="${listTransaction}" var="transaction">

    <tr>
    
     <td><c:out value="${transaction.createdStamp}" /></td>
     <td></td>
     <td><c:out value="${transaction.accountNo}" /></td>
     <td><c:out value="${transaction.accountName}" /></td>
      <td><c:out value="${transaction.transactionType}" /></td>
      <td><c:out value="${transaction.transactionAmount}" /></td>
    </tr>
  </c:forEach>
</table>
</body>
</html>
