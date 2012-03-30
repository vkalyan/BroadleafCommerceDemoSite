function autoFillCreditCardInfoBraintree() {
    var x=document.forms[1];                   //Select the form

    //Fill out Payment Information
    x.elements[1].value = "4111111111111111";  //Credit Card Number
    x.elements[2].value = "12";                //Credit Card Exp Month
    x.elements[3].value = "2020";               //Credit Card Exp Year
    x.elements[4].value = "321";               //Credit Card CV Code
}
