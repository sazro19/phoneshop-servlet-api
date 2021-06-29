<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Checkout">
  <c:if test="${not empty param.message and empty errors}">
    <span class="success">
        ${param.message}
    </span>
  </c:if>
  <c:if test="${not empty errors}">
    <span class="error">
      Errors occurred during placing the order
    </span>
  </c:if>
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
  <form method="post">
    <h3>Details</h3>
    <span style="color:red">*</span>-necessary field;
    <table>
      <tags:orderDetailsRow name="firstname" label="Firstname" order="${order}" errors="${errors}"/>
      <tags:orderDetailsRow name="lastname" label="Lasttname" order="${order}" errors="${errors}"/>
      <tags:orderDetailsRow name="phone" label="Phone" order="${order}" errors="${errors}"/>
      <tags:orderDetailsRow name="deliveryDate" label="Delivery date" order="${order}" errors="${errors}"/>
      <tags:orderDetailsRow name="deliveryAddress" label="Delivery address" order="${order}" errors="${errors}"/>
      <td>
          Payment Method<span style="color:red">*</span>
      </td>
      <td>
          <select name="paymentMethod" >
            <c:forEach var="method" items="${paymentMethods}">
                <c:choose>
                  <c:when test="${not empty errors and param.paymentMethod eq method}">
                    <option selected>${method}</option>
                  </c:when>
                  <c:otherwise>
                    <option>${method}</option>
                  </c:otherwise>
                </c:choose>
            </c:forEach>
          </select>
        <c:if test="${not empty errors.get('paymentMethod')}">
          <span class="error">
             ${errors.get('paymentMethod')}
          </span>
        </c:if>
      </td>
    </table>
    <button>
      Place order
    </button>
  </form>
</tags:master>