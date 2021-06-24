<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="order" required="true" type="com.es.phoneshop.model.product.order.Order" %>
<%@ attribute name="errors" required="true" type="java.util.Map" %>

<tr>
  <td>
    ${label}<span style="color:red">*</span>
    <c:if test="${name eq 'phone'}">(example: +375297766551)</c:if>
  </td>
  <c:set var="error" value="${errors.get(name)}"/>
  <td>
    <c:if test="${name eq 'deliveryDate'}">
      <input type="date" name="${name}" value="${not empty errors ? param[name] : order[name]}">
    </c:if>
    <c:if test="${name ne 'deliveryDate'}">
      <input name="${name}" value="${not empty errors ? param[name] : order[name]}">
    </c:if>
    <c:if test="${not empty error}">
      <span class="error">
        ${error}
      </span>
    </c:if>
  </td>
</tr>