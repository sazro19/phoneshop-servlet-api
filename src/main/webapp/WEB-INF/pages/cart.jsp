<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.product.cart.Cart" scope="request"/>
<tags:master pageTitle="Cart">
  <p>
    Cart: ${cart}
  </p>
  <c:if test="${not empty param.message and empty error}">
    <span class="success">
        ${param.message}
    </span>
  </c:if>
  <c:if test="${not empty error}">
    <span class="error">
        Error occurred during cart updating
    </span>
  </c:if>
  <c:if test="${not empty errors}">
    <span class="error">
      Errors occurred during updating cart
    </span>
  </c:if>
  <form method="post" action="${pageContext.servletContext.contextPath}/cart">
    <table>
      <thead>
        <tr>
          <td>Image</td>
          <td>
            Description
          </td>
          <td>
            Quantity
          </td>
          <td class="price">
            Price
          </td>
        </tr>
      </thead>
      <c:forEach var="item" items="${cart.items}" varStatus="status">
        <tr>
          <td>
            <img class="product-tile" src="${item.product.imageUrl}">
          </td>
          <td>
            <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
              ${item.product.description}
            </a>
          </td>
          <td>
            <fmt:formatNumber value="${item.quantity}" var="quantity"/>
              <c:set var="error" value="${errors[item.product.id]}"/>
              <input name="quantity" value="${not empty error ? paramValues['quantity'][status.index] : item.quantity}" class="quantity">
            <c:if test="${not empty error}">
                <span class="error">
                  ${errors[item.product.id]}
                </span>
            </c:if>
            <input type="hidden" name="productId" value="${item.product.id}">
          </td>
          <td class="price">
            <a href="${pageContext.servletContext.contextPath}/products/priceHistory/${item.product.id}">
              <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/>
            </a>
          </td>
        </tr>
      </c:forEach>
    </table>
    <p>
      <button>Update</button>
    </p>
  </form>
</tags:master>