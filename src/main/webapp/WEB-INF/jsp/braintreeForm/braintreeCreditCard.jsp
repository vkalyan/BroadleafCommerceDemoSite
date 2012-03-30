<%@ include file="/WEB-INF/jsp/include.jsp" %>
<tiles:insertDefinition name="baseNoSide">
    <tiles:putAttribute name="mainContent" type="string">
        <div class="mainContent">
            <h3 style="margin:8px 0;font-weight:bold;">Braintree Credit Card</h3>
            <form action="${trUrl}" autocomplete="off" method="post">
                <div class="columns">
                    <script type="text/javascript" src="/src/main/webapp/js/braintreeCreditCard-autofill.js"></script>

                    <div class="fillButton">
                        <button type="button" onclick="autoFillCreditCardInfoBraintree()">Autofill the Credit Card Info</button>
                        <p>(This button is provided for demo purposes only.)</p>
                    </div>
                    <div class="span-11 column">

                        <div class="span-10 creditCardPayment orderBorder">
                            <div class="orderTitle" ><b> Credit Card Ininputation </b></div>
                            <span class="small"><b>* Required Fields</b></span> <br/>
                            <div>
                                <label><b>* Credit Card Number</b></label>
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
