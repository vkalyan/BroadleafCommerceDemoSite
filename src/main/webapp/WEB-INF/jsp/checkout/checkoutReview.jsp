<%@ include file="/WEB-INF/jsp/include.jsp" %>
<tiles:insertDefinition name="baseNoSide">
    <tiles:putAttribute name="mainContent" type="string">

        <div class="mainContent">
            <h3 class="pageTitle" ><b>Checkout Confirmation</b></h3>
            <jsp:include page="/WEB-INF/jsp/orderSnippet.jsp" />
        </div>
        <form:form method="post" modelAttribute="checkoutForm">
            <div class="formButtonFooter personFormButtons">
                <button type="submit" id="paypalProcess" name="paypalProcess">Submit Order</button>
            </div>
        </form:form>
    </tiles:putAttribute>
</tiles:insertDefinition>