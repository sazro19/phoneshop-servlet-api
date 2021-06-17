<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <form>
    <input name="query" value="${param.query}">
    <button>Search</button>
  </form>
  <form method="post">
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
  <table>
    <thead>
      <tr>
        <td>Image</td>
        <td>
          Description
          <tags:sortLink sort="DESCRIPTION" order="ASC"/>
          <tags:sortLink sort="DESCRIPTION" order="DESC"/>
        </td>
        <td>
          Quantity
        </td>
        <td class="price">
          Price
          <tags:sortLink sort="PRICE" order="ASC"/>
          <tags:sortLink sort="PRICE" order="DESC"/>
        </td>
        <td></td>
      </tr>
    </thead>
    <c:forEach var="product" items="${products}">
      <tr>
        <td>
          <img class="product-tile" src="${product.imageUrl}">
        </td>
        <td>
          <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
            ${product.description}
          </a>
        </td>
        <td>
          <input type="number" min="1" name="quantity" value="${not empty error and product.id eq index ? input : 1}">
          <c:if test="${not empty error and product.id eq index}">
                <span class="error">
                  ${error}
                </span>
          </c:if>
          <input type="hidden" name="productId" value="${product.id}">
        </td>
        <td class="price">
          <a href="${pageContext.servletContext.contextPath}/products/priceHistory/${product.id}">
            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
          </a>
        </td>
        <td>
          <button formaction="${pageContext.servletContext.contextPath}/products/addToCart/${product.id}?sort=${param.sort}&order=${param.order}&query=${param.query}">
            Add to cart
          </button>
        </td>
      </tr>
    </c:forEach>
  </table>
  </form>
  <p>
    <b>Recently viewed</b>
  </p>
  <tags:recentlyViewed recentlyViewedList="${viewed}"/>
</tags:master>