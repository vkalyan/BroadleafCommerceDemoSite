<%@ include file="/WEB-INF/jsp/include.jsp" %>
<tiles:insertDefinition name="baseNoSide">
    <tiles:putAttribute name="mainContent" type="string">
        <div class="mainContent">
            <h3 style="margin:8px 0;font-weight:bold;">Braintree Checkout Form</h3>
            <form action="${trUrl}" autocomplete="off" method="post">
                <div class="columns">
                    <script type="text/javascript" src="/src/main/webapp/js/braintreeCheckout-autofill.js"></script>

                    <div class="fillButton">
                        <button type="button" onclick="autoFillContactInfoBraintree()">Autofill the Checkout form</button>
                        <p>(This button is provided for demo purposes only.)</p>
                    </div>
                    <div class="span-11 column">

                        <div class="orderBorder">
                            <div class="orderTitle"><b>Payment Information</b></div>
                            <span class="small"><b>* Required Fields</b></span> <br/>
                            <div class="errorFormText" style="margin-top:10px;">
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
                                <label><b>* First Name</b></label>
                                <label style="margin-left:85px;"><b>* Last Name</b></label>
                            </div>
                            <div>
                                <input  name="transaction[billing][first_name]"/>
                                <span style="margin-left: 20px;"><input  name="transaction[billing][last_name]" /> </span>
                            </div>
                            <div>
                                <label><b>* Address Line 1</b></label> <br/>
                                <input size="50"  name="transaction[billing][street_address]" />
                            </div>
                            <div>
                                <label><b>Address Line 2</b></label> <br/>
                                <input size="50"  name="transaction[billing][extended_address]" />
                            </div>
                            <div>
                                <label><b>* City</b></label> <br/>
                                <input  name="transaction[billing][locality]"/>
                            </div>
                            <div>
                                <label><b>* State</b></label><br/>
                                <input items="${stateList}" itemValue="abbreviation" itemLabel="name"  name="transaction[billing][region]"/>
                            </div>
                            <div>
                                <label><b>* Postal Code</b></label>
                                <label style="margin-left:85px;"><b>Country</b></label>
                            </div>
                            <div>
                                <input  name="transaction[billing][postal_code]"/>
                                <span style="margin-left: 20px;" >
                                    <input items="${countryList}" itemValue="abbreviation" itemLabel="name"  name="transaction[billing][country_code_alpha3]"/>
                                </span>
                            </div>
                            <div>
                                <label><b>* Phone Number</b></label>
                                <label style="margin-left: 70px"><b>* Email Address</b></label>
                            </div>
                            <div>
                                <input  name="transaction[customer][phone]"/>
                                <span style="margin-left: 20px" ><input  name="transaction[customer][email]"/></span>
                            </div>
                        </div>

                        <div class="span-10 creditCardPayment orderBorder">
                            <div class="orderTitle" ><b> Credit Card Ininputation </b></div>
                            <span class="small"><b>* Required Fields</b></span> <br/>
                            <div class="errorInputText" style="margin-top:10px;">
                                <input:errors  name="creditCardNumber" >
                                    <c:forEach items="${messages}" var="message"> ${message} <br/> </c:forEach>
                                </input:errors>
                                <input:errors  name="creditCardExpMonth" >
                                    <c:forEach items="${messages}" var="message"> ${message} <br/> </c:forEach>
                                </input:errors>
                                <input:errors  name="creditCardExpYear" >
                                    <c:forEach items="${messages}" var="message"> ${message} <br/> </c:forEach>
                                </input:errors>
                                <input:errors  name="creditCardCvvCode" >
                                    <c:forEach items="${messages}" var="message"> ${message} <br/> </c:forEach>
                                </input:errors>
                            </div>
                            <div>
                                <br/>
                                <label ><b>* Card Type</b></label><br/>
                                <input items="${checkoutinput.approvedCreditCardTypes}" itemValue="type" itemLabel="type" />
                            </div>
                            <div>
                                <label for="creditCardNumber"><b>* Credit Card Number</b></label>
                                <label style="margin-left:50px;"><b>* Expiration Date</b></label>
                            </div>
                            <div>
                                <input maxlength="16" size="16"  name="transaction[credit_card][number]"/>
                                <span style="margin-left:50px;">
                                    <input maxlength="2" size="2"  name="transaction[credit_card][expiration_month]"/> / <input maxlength="4" size="4"  name="transaction[credit_card][expiration_year]"/>
                                </span>
                            </div>
                            <div>
                                <label><b>* CVV Code</b></label>
                            </div>
                            <div>
                                <input maxlength="4" size="4"   name="transaction[credit_card][cvv]"/>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="inputButtonFooter personinputButtons">
                    <input id="tr_data"  name="tr_data" type="hidden" value="${trData}" />
                    <button type="submit" id="transaction_submit"  name="commit">Submit Order</button>
                </div>
            </form>
        </div>
    </tiles:putAttribute>
</tiles:insertDefinition>
