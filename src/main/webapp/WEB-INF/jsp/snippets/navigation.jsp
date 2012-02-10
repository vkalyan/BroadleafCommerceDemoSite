<%@ include file="/WEB-INF/jsp/include.jsp" %>

<blc:categoryLookup categoryName="Store" var="navCategory"/>

<div id="header">
	<a href="<c:out value="${pageContext.request.contextPath}"/>"><img class="logo" src="/broadleafdemo/images/havalettaLogo.png" /></a>
	<a href="http://www.broadleafcommerce.org/" class="promoCTA">This is a demo store. No payments accepted or orders fulfilled.</a>
	<ul id="userNav" class="clearfix">
		<li>
			<c:choose>
				<c:when test="${customer.anonymous}">
					<a href="<c:out value="${pageContext.request.contextPath}"/>/registerCustomer/registerCustomer.htm">Login</a>
				</c:when>
				<c:otherwise>
					<a href="<c:out value="${pageContext.request.contextPath}"/>/account/myAccount.htm">Hello <c:out value="${customer.firstName}"/></a></li>
					<li><a href="<c:out value="${pageContext.request.contextPath}"/>/logout.htm">Logout</a>
				</c:otherwise>
			</c:choose>
		</li>
		<li><a class="noTextUnderline" href="<c:out value="${pageContext.request.contextPath}"/>/orders/findOrder.htm" > Find Order </a></li>
		<li><a href="<c:out value="${pageContext.request.contextPath}"/>/storeLocator/findStores.htm">Store Locator</a></li>
		<li class="last"><a class="cartLink" href="<c:out value="${pageContext.request.contextPath}"/>/basket/viewCart.htm">View Cart</a></li>
	</ul>

<ul id="primaryNav" class="clearfix">
    <li><a class="${currentCategory.generatedUrl==null?'active':''}" href="<c:out value="${pageContext.request.contextPath}"/>/store">Home</a></li>
    <c:forEach var="childCategory" items="${navCategory.childCategories}" varStatus="status">
        <li><a class="${currentCategory.generatedUrl==childCategory.generatedUrl ? 'active':''}" href="<c:out value="${pageContext.request.contextPath}"/>/${childCategory.generatedUrl}">${childCategory.name}</a></li>
    </c:forEach>
</ul>
	
	<div id="searchBar">
		<form id="search" method="post" action="<c:out value="${pageContext.request.contextPath}"/>/search/results.htm">
			<input class="searchField" type="text" name="queryString" id="queryString" size="30" helpText="Search the store..." /><input type="image"
			class="imageBtn" src="<c:out value="${pageContext.request.contextPath}"/>/images/searchBtn.png" alt="Search" />
		</form>
	</div>
	<img class="slogan" src="<c:out value="${pageContext.request.contextPath}"/>/images/slogan2.gif" />
</div>

