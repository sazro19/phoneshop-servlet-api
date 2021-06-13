<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.product.cart.Cart" scope="request"/>
Cart: ${cart.totalQuantity} items; total price: <fmt:formatNumber value="${not empty cart.items ? cart.totalPrice : 0}" type="currency" currencySymbol="${not empty cart.items ? cart.items.get(0).product.currency.symbol : ''}"/>