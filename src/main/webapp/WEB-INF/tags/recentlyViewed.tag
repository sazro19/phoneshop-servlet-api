<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="recentlyViewedList" rtexprvalue="true" type="java.util.Deque" required="true" %>

<table>
    <c:forEach var="product" items="${recentlyViewedList}">
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