<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product">
  <br>
    ${product.description}
    <br><img src="${product.imageUrl}">
  </p>
  <table border="1" width="50%" cellpadding="5">
      <tr>
        <th>
          Code
        </th>
        <th>
          Price
        </th>
        <th>
          Stock
        </th>
      </tr>
      <tr>
        <th>
          ${product.code}
        </th>
        <th>
          <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
        </th>
        <th>
          ${product.stock}
        </th>
      </tr>
  </table>
</tags:master>