<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product">
  <p>
    Price history
  </p>
  <p>
    ${product.description}
  </p>
  <table>
    <thead>
    <tr>
      <td>Start date</td>
      <td class="price">
        Price
      </td>
    </tr>
    </thead>
    <c:forEach var="history" items="${product.priceHistoryList}">
      <tr>
        <td>
          ${history.calendar.get(5)}.${history.calendar.get(2) + 1}.${history.calendar.get(1)}
        </td>
        <td class="price">
          <fmt:formatNumber value="${history.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
        </td>
      </tr>
    </c:forEach>
  </table>
</tags:master>