<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.product.order.Order" scope="request"/>
<tags:master pageTitle="Order overview">
  <h1>Order overview</h1>
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
      <c:forEach var="item" items="${order.items}" varStatus="status">
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
              ${item.quantity}
          </td>
          <td class="price">
            <a href="${pageContext.servletContext.contextPath}/products/priceHistory/${item.product.id}">
              <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/>
            </a>
          </td>
        </tr>
      </c:forEach>
      <tr>
        <td></td>
        <td></td>
        <td></td>
        <td class="price">Subtotal:
          <fmt:formatNumber value="${order.subtotal}" type="currency" currencySymbol="${not empty order.items ? order.items.get(0).product.currency.symbol : ''}"/>
          <br>Delivery cost:
          <fmt:formatNumber value="${order.deliveryCost}" type="currency" currencySymbol="${not empty order.items ? order.items.get(0).product.currency.symbol : ''}"/>
          <br>Total Price:
          <fmt:formatNumber value="${order.totalPrice}" type="currency" currencySymbol="${not empty order.items ? order.items.get(0).product.currency.symbol : ''}"/>
        </td>
      </tr>
    </table>
    <h3>Details</h3>
    <span style="color:red">*</span>-necessary field;
    <table>
      <tags:orderOverviewRow name="firstname" label="Firstname" order="${order}"/>
      <tags:orderOverviewRow name="lastname" label="Lasttname" order="${order}"/>
      <tags:orderOverviewRow name="phone" label="Phone" order="${order}"/>
      <tags:orderOverviewRow name="deliveryDate" label="Delivery date" order="${order}"/>
      <tags:orderOverviewRow name="deliveryAddress" label="Delivery address" order="${order}"/>
      <tr>
        <td>
            Payment method
        </td>
        <td>
            ${order.paymentMethod.getValue()}
        </td>
      </tr>
    </table>
</tags:master>