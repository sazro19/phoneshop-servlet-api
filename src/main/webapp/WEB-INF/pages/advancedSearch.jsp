<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Advanced search">
  <h1>Advanced search</h1>
  <form>
    <table>
      <tr>
        <td>
          Description
        </td>
        <td>
          <input name="description">
        </td>
        <td>
          <select name="descriptionOptions">
            <c:forEach var="option" items="${options}">
              <option>
                ${option}
              </option>
            </c:forEach>
          </select>
        </td>
      </tr>
      <tr>
        <td>
          Min price
        </td>
        <td>
          <input name="minPrice" value="${not empty errors ? errors.get("minPrice") : ''}">
          <c:if test="${not empty errors}">
            <span class="error">
              ${errors.get("minPrice")}: not a number
            </span>
          </c:if>
        </td>
      </tr>
      <tr>
        <td>
          Max price
        </td>
        <td>
          <input name="maxPrice" value="${not empty errors ? errors.get("maxPrice") : ''}">
          <c:if test="${not empty errors}">
            <span class="error">
              ${errors.get("maxPrice")}: not a number
            </span>
          </c:if>
        </td>
      </tr>
    </table>
    <button>Search</button>
  </form>
  <c:if test="${not empty param}">
    <table>
      <thead>
      <tr>
        <td>Image</td>
        <td>
          Description
        </td>
        <td class="price">
          Price
        </td>
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
          <td class="price">
            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
          </td>
        </tr>
      </c:forEach>
    </table>
  </c:if>
</tags:master>