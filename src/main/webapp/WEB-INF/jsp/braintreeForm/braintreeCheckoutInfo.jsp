<%@ include file="/WEB-INF/jsp/include.jsp" %>
<script type="text/javascript" src="/src/main/webapp/js/checkout-autofill.js"></script>

<div class="fillButton">
    <button type="button" onclick="autoFillContactInfo()">Autofill the Checkout Form</button>
    <p>(This button is provided for demo purposes only.)</p>
</div>
<div class="span-11 column">

    <div class="orderBorder">
        <div class="orderTitle"><b>Payment Information</b></div>
        <span class="small"><b>* Required Fields</b></span> <br/>
        <div style="margin-top:10px; font-size:13px;">
            <form:checkbox path="isSameAddress" id="sameShippingInfo"  />
            <span style="padding-left:4px"> Yes, my <b>Billing</b> and <b>Shipping</b> addresses are the same </span>
        </div>
        <div class="errorInputText" style="margin-top:10px;">
            <form:errors path="billingAddress.firstName" >
                <c:forEach items="${messages}" var="message"> ${message} <br/> </c:forEach>
            </form:errors>
            <form:errors path="billingAddress.lastName" >
                <c:forEach items="${messages}" var="message"> ${message} <br/> </c:forEach>
            </form:errors>
            <form:errors path="billingAddress.primaryPhone" >
                <c:forEach items="${messages}" var="message"> ${message} <br/> </c:forEach>
            </form:errors>
            <form:errors path="billingAddress.addressLine1" >
                <c:forEach items="${messages}" var="message"> ${message} <br/> </c:forEach>
            </form:errors>
            <form:errors path="billingAddress.addressLine2" >
                <c:forEach items="${messages}" var="message"> ${message} <br/> </c:forEach>
            </form:errors>
            <form:errors path="billingAddress.city" >
                <c:forEach items="${messages}" var="message"> ${message} <br/> </c:forEach>
            </form:errors>
            <form:errors path="billingAddress.postalCode" >
                <c:forEach items="${messages}" var="message"> ${message} <br/> </c:forEach>
            </form:errors>
            <form:errors path="emailAddress" >
                <c:forEach items="${messages}" var="message"> ${message} <br/> </c:forEach>
            </form:errors>
        </div>
        <div>
            <br/>
            <label for="contactInfo.firstName"><b>* First Name</b></label>
            <label style="margin-left:85px;" for="contactInfo.lastName"><b>* Last Name</b></label>
        </div>
        <div>
            <form:input path="billingAddress.firstName"/>
            <span style="margin-left: 20px;"><form:input path="billingAddress.lastName"/> </span>
        </div>
        <div>
            <label for="addressLine1"><b>* Address Line 1</b></label> <br/>
            <form:input path="billingAddress.addressLine1" size="50" />
        </div>
        <div>
            <label for="addressLine2"><b>Address Line 2</b></label> <br/>
            <form:input path="billingAddress.addressLine2"  size="50"/>
        </div>
        <div>
            <label for="city"><b>* City</b></label> <br/>
            <form:input path="billingAddress.city" />
        </div>
        <div>
            <label for="state"><b>* State</b></label><br/>
            <form:select path="billingAddress.state.abbreviation">
                <form:options items="${stateList}" itemValue="abbreviation" itemLabel="name" />
            </form:select>
        </div>
        <div>
            <label for="postalCode"><b>* Postal Code</b></label>
            <label style="margin-left:85px;" for="country"><b>Country</b></label>
        </div>
        <div>
            <form:input path="billingAddress.postalCode" />
			<span style="margin-left: 20px;" >
				<form:select path="billingAddress.country.abbreviation">
                    <form:options items="${countryList}" itemValue="abbreviation" itemLabel="name" />
                </form:select>
			</span>
        </div>
        <div>
            <label for="contactInfo.primaryPhone"><b>* Phone Number</b></label>
            <label style="margin-left: 70px" for="contactInfo.email"><b>* Email Address</b></label>
        </div>
        <div>
            <form:input path="billingAddress.primaryPhone"/>
            <span style="margin-left: 20px" ><form:input path="emailAddress"/></span>
        </div>
    </div>
</div>
