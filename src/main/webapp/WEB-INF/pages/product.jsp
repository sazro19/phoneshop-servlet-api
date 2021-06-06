<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product">
  <p>
    Cart: ${cart}
  <br>
    ${product.description}
    <br><img src="${product.imageUrl}">
  </p>
  <c:if test="${not empty param.message and empty error}">
    <span class="success">
      ${param.message}
    </span>
  </c:if>
  <c:if test="${not empty error}">
    <span class="error">
        Error occurred during adding to cart
    </span>
  </c:if>
  <form method="post">
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
          <th>
            Quantity
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
          <th>
              <input name="quantity" value="${not empty error ? param.quantity : 1}">
              <c:if test="${not empty error}">
                <span class="error">
                  '${param.quantity}': ${error}
                </span>
              </c:if>
          </th>
        </tr>
    </table>
    <p>
      <button>Add to cart</button>
    </p>
  </form>
  <p>
    <b>Recently viewed</b>
  </p>
  <table>
    <c:forEach var="product" items="${viewed.threeLastProducts}">
      <th>
        <br>
          <img class="product-tile" src="${product.imageUrl}">
        </br>
        <br>
          <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
              ${product.description}
          </a>
        </br>
        <br>
          <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
        </br>
      </th>
    </c:forEach>
  </table>
</tags:master>