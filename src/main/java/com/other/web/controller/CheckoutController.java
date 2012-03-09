/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.other.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.time.SystemTime;
import org.broadleafcommerce.core.checkout.service.CheckoutService;
import org.broadleafcommerce.core.checkout.service.exception.CheckoutException;
import org.broadleafcommerce.core.order.domain.DiscreteOrderItem;
import org.broadleafcommerce.core.order.domain.FulfillmentGroup;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.domain.OrderItem;
import org.broadleafcommerce.core.order.service.CartService;
import org.broadleafcommerce.core.order.service.type.OrderStatus;
import org.broadleafcommerce.core.payment.domain.AmountItem;
import org.broadleafcommerce.core.payment.domain.AmountItemImpl;
import org.broadleafcommerce.core.payment.domain.CreditCardPaymentInfo;
import org.broadleafcommerce.core.payment.domain.PaymentInfo;
import org.broadleafcommerce.core.payment.domain.PaymentResponseItem;
import org.broadleafcommerce.core.payment.domain.Referenced;
import org.broadleafcommerce.core.payment.domain.TotalledPaymentInfoImpl;
import org.broadleafcommerce.core.payment.service.CompositePaymentService;
import org.broadleafcommerce.core.payment.service.PaymentInfoService;
import org.broadleafcommerce.core.payment.service.SecurePaymentInfoService;
import org.broadleafcommerce.core.payment.service.type.PaymentInfoType;
import org.broadleafcommerce.core.payment.service.workflow.CompositePaymentResponse;
import org.broadleafcommerce.core.web.checkout.model.CheckoutForm;
import org.broadleafcommerce.core.web.checkout.validator.CheckoutFormValidator;
import org.broadleafcommerce.payment.service.module.PayPalPaymentModule;
import org.broadleafcommerce.profile.core.domain.Country;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.domain.CustomerPhone;
import org.broadleafcommerce.profile.core.domain.CustomerPhoneImpl;
import org.broadleafcommerce.profile.core.service.CountryService;
import org.broadleafcommerce.profile.core.service.CustomerAddressService;
import org.broadleafcommerce.profile.core.service.CustomerPhoneService;
import org.broadleafcommerce.profile.core.service.CustomerService;
import org.broadleafcommerce.profile.core.service.StateService;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.broadleafcommerce.vendor.paypal.service.payment.MessageConstants;
import org.broadleafcommerce.vendor.paypal.service.payment.message.details.PayPalDetailsRequest;
import org.broadleafcommerce.vendor.paypal.service.payment.message.details.PayPalDetailsResponse;
import org.broadleafcommerce.vendor.paypal.service.payment.type.PayPalMethodType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("myCheckoutController")
@RequestMapping("/checkout")
public class CheckoutController {

    private static final Log LOG = LogFactory.getLog(CheckoutController.class);

    @Resource(name="blCartService")
    protected CartService cartService;
    @Resource(name="blCustomerState")
    protected CustomerState customerState;
    @Resource(name="blCustomerAddressService")
    protected CustomerAddressService customerAddressService;
    @Resource(name="blCustomerPhoneService")
    protected CustomerPhoneService customerPhoneService;
    @Resource(name="blCheckoutService")
    protected CheckoutService checkoutService;
    @Resource(name="blStateService")
    protected StateService stateService;
    @Resource(name="blCountryService")
    protected CountryService countryService;
    @Resource(name="blPaymentInfoService")
    protected PaymentInfoService paymentInfoService;
    @Resource(name="blSecurePaymentInfoService")
    protected SecurePaymentInfoService securePaymentInfoService;
    @Resource(name="blCheckoutFormValidator")
    protected CheckoutFormValidator checkoutFormValidator;
    @Resource(name="blCustomerService")
    protected CustomerService customerService;

    //this service is backed by the entire payment workflow configured in application context
    //it is the entry point for engaging the payment workflow
    @Resource(name="blCompositePaymentService")
    protected CompositePaymentService compositePaymentService;

    @Resource(name="blPayPalModule")
    protected PayPalPaymentModule payPalPaymentModule;

    protected String checkoutView;
    protected String receiptView;

    public void setReceiptView(String receiptView) {
        this.receiptView = receiptView;
    }

    public void setCheckoutView(String checkoutView) {
        this.checkoutView = checkoutView;
    }

    private CheckoutForm copyAddress (CheckoutForm checkoutForm) {
        checkoutForm.getShippingAddress().setFirstName(checkoutForm.getBillingAddress().getFirstName());
        checkoutForm.getShippingAddress().setLastName(checkoutForm.getBillingAddress().getLastName());
        checkoutForm.getShippingAddress().setAddressLine1(checkoutForm.getBillingAddress().getAddressLine1());
        checkoutForm.getShippingAddress().setAddressLine2(checkoutForm.getBillingAddress().getAddressLine2());
        checkoutForm.getShippingAddress().setCity(checkoutForm.getBillingAddress().getCity());
        checkoutForm.getShippingAddress().setState(checkoutForm.getBillingAddress().getState());
        checkoutForm.getShippingAddress().setPostalCode(checkoutForm.getBillingAddress().getPostalCode());
        checkoutForm.getShippingAddress().setCountry(checkoutForm.getBillingAddress().getCountry());
        checkoutForm.getShippingAddress().setPrimaryPhone(checkoutForm.getBillingAddress().getPrimaryPhone());

        return checkoutForm;
    }

    @RequestMapping(value = "/checkout.htm", method = {RequestMethod.POST})
    public String processCheckout(@ModelAttribute CheckoutForm checkoutForm,
            BindingResult errors,
            ModelMap model,
            HttpServletRequest request) {

        if (checkoutForm.getIsSameAddress()) {
            copyAddress(checkoutForm);
        }

        checkoutFormValidator.validate(checkoutForm, errors);

        if (errors.hasErrors()) {
            return checkout(checkoutForm, errors, model, request);
        }

        checkoutForm.getBillingAddress().setCountry(countryService.findCountryByAbbreviation(checkoutForm.getBillingAddress().getCountry().getAbbreviation()));
        checkoutForm.getBillingAddress().setState(stateService.findStateByAbbreviation(checkoutForm.getBillingAddress().getState().getAbbreviation()));
        checkoutForm.getShippingAddress().setCountry(countryService.findCountryByAbbreviation(checkoutForm.getShippingAddress().getCountry().getAbbreviation()));
        checkoutForm.getShippingAddress().setState(stateService.findStateByAbbreviation(checkoutForm.getShippingAddress().getState().getAbbreviation()));
        
        Order order = retrieveCartOrder(request, model);
        order.setOrderNumber(new SimpleDateFormat("yyyyMMddHHmmssS").format(SystemTime.asDate()));

        List<FulfillmentGroup> groups = order.getFulfillmentGroups();
        if(groups.size() < 1){
        	return "redirect:/basket/currentCart.htm";
        }
        FulfillmentGroup group = groups.get(0);
        group.setOrder(order);
        group.setAddress(checkoutForm.getShippingAddress());
        group.setShippingPrice(order.getTotalShipping());

        //TODO this controller needs to handle the other payment types as well, not just credit card.
        Map<PaymentInfo, Referenced> payments = new HashMap<PaymentInfo, Referenced>();
        CreditCardPaymentInfo creditCardPaymentInfo = ((CreditCardPaymentInfo) securePaymentInfoService.create(PaymentInfoType.CREDIT_CARD));

        creditCardPaymentInfo.setCvvCode(checkoutForm.getCreditCardCvvCode());
        creditCardPaymentInfo.setExpirationMonth(Integer.parseInt(checkoutForm.getCreditCardExpMonth()));
        creditCardPaymentInfo.setExpirationYear(Integer.parseInt(checkoutForm.getCreditCardExpYear()));
        creditCardPaymentInfo.setPan(checkoutForm.getCreditCardNumber());
        creditCardPaymentInfo.setReferenceNumber(checkoutForm.getCreditCardNumber());

        PaymentInfo paymentInfo = paymentInfoService.create();
        paymentInfo.setAddress(checkoutForm.getBillingAddress());
        paymentInfo.setOrder(order);
        paymentInfo.setType(PaymentInfoType.CREDIT_CARD);
        paymentInfo.setReferenceNumber(checkoutForm.getCreditCardNumber());
        paymentInfo.setAmount(order.getTotal());
        payments.put(paymentInfo, creditCardPaymentInfo);
        List<PaymentInfo> paymentInfos = new ArrayList<PaymentInfo>();
        paymentInfos.add(paymentInfo);
        order.setPaymentInfos(paymentInfos);
        

        order.setStatus(OrderStatus.SUBMITTED);
        order.setSubmitDate(Calendar.getInstance().getTime());

        try {
            checkoutService.performCheckout(order, payments);
        } catch (CheckoutException e) {
            LOG.error("Cannot perform checkout", e);
        }

        //Fill out a few customer values for anonymous customers
        Customer customer = order.getCustomer();
        if (StringUtils.isEmpty(customer.getFirstName())) {
            customer.setFirstName(checkoutForm.getBillingAddress().getFirstName());
        }
        if (StringUtils.isEmpty(customer.getLastName())) {
            customer.setLastName(checkoutForm.getBillingAddress().getLastName());
        }
        if (StringUtils.isEmpty(customer.getEmailAddress())) {
            customer.setEmailAddress(order.getEmailAddress());
        }
        customerService.saveCustomer(customer, false);

        return receiptView != null ? "redirect:" + receiptView : "redirect:/orders/viewOrderConfirmation.htm?orderNumber=" + order.getOrderNumber();
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/checkout.htm", method = {RequestMethod.GET})
    public String checkout(@ModelAttribute CheckoutForm checkoutForm,
            BindingResult errors,
            ModelMap model,
            HttpServletRequest request) {

        model.addAttribute("stateList", stateService.findStates());
        List<Country> countries = countryService.findCountries();
        Collections.sort(countries, new ReverseComparator(new BeanComparator("abbreviation")));
        model.addAttribute("countryList", countries);

        Customer currentCustomer = customerState.getCustomer(request);
        model.addAttribute("customer", currentCustomer);

        List<CustomerPhone> customerPhones = customerPhoneService.readAllCustomerPhonesByCustomerId(currentCustomer.getId());
        while(customerPhones.size() < 2) {
            customerPhones.add(new CustomerPhoneImpl());
        }

        customerAddressService.readActiveCustomerAddressesByCustomerId(currentCustomer.getId());
        model.addAttribute("order", retrieveCartOrder(request, model));
        return checkoutView;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/paypalCheckout.htm", method = {RequestMethod.GET})
    public String paypalCheckout(@ModelAttribute CheckoutForm checkoutForm,
                           BindingResult errors,
                           ModelMap model,
                           HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        try {
            final Order order = retrieveCartOrder(request, model);
            Map<PaymentInfo, Referenced> payments = new HashMap<PaymentInfo, Referenced>();

            TotalledPaymentInfoImpl paymentInfo = new TotalledPaymentInfoImpl();
            paymentInfo.setOrder(order);
            paymentInfo.setType(PaymentInfoType.PAYPAL);
            paymentInfo.getAdditionalFields().put(MessageConstants.PAYPALMETHODTYPE, PayPalMethodType.CHECKOUT.getType());
            paymentInfo.setReferenceNumber(String.valueOf(order.getId()));
            paymentInfo.setAmount(order.getTotal());
            paymentInfo.setSubTotal(order.getSubTotal());
            paymentInfo.setTotalShipping(order.getTotalShipping());
            paymentInfo.setTotalTax(order.getTotalTax());
            paymentInfo.setShippingDiscount(order.getFulfillmentGroupAdjustmentsValue());
            for (OrderItem orderItem : order.getOrderItems()) {
                AmountItem amountItem = new AmountItemImpl();
                if (DiscreteOrderItem.class.isAssignableFrom(orderItem.getClass())) {
                    amountItem.setDescription(((DiscreteOrderItem)orderItem).getSku().getDescription());
                    amountItem.setSystemId(String.valueOf(((DiscreteOrderItem) orderItem).getSku().getId()));
                }
                amountItem.setShortDescription(orderItem.getName());
                amountItem.setPaymentInfo(paymentInfo);
                amountItem.setQuantity((long) orderItem.getQuantity());
                amountItem.setUnitPrice(orderItem.getPrice().getAmount());
                paymentInfo.getAmountItems().add(amountItem);
            }
            payments.put(paymentInfo, paymentInfo.createEmptyReferenced());
            List<PaymentInfo> paymentInfos = new ArrayList<PaymentInfo>();
            paymentInfos.add(paymentInfo);
            order.setPaymentInfos(paymentInfos);
            
            CompositePaymentResponse compositePaymentResponse = compositePaymentService.executePayment(order, payments);
            PaymentResponseItem responseItem = compositePaymentResponse.getPaymentResponse().getResponseItems().get(paymentInfo);

            if(responseItem.getTransactionSuccess()) {
                order.setOrderNumber(new SimpleDateFormat("yyyyMMddHHmmssS").format(SystemTime.asDate()));
                model.addAttribute("order", retrieveCartOrder(request, model));
                return "redirect:" + responseItem.getAdditionalFields().get(MessageConstants.REDIRECTURL);
            } else {
                //TODO this needs some work
                //return resp.substring(resp.indexOf("MODULEERRORCODE"));
                return "";
            }
        } catch (Exception e) {
            System.out.println(e);
            return "";
        }
    }

    /*@RequestMapping(value="/paypalDetails.htm", method = {RequestMethod.GET})
    public String paypalDetails(ModelMap model,
                                @RequestParam String token,
                                @RequestParam("PayerID") String payerID,
                                CheckoutForm checkoutForm,
                                HttpServletRequest request) {

        try {
            PayPalDetailsRequest detailsRequest = new PayPalDetailsRequest();
            detailsRequest.setToken(token);
            detailsRequest.setMethodType(PayPalMethodType.DETAILS);
            PayPalDetailsResponse detailsResponse = payPalPaymentModule.getExpressCheckoutDetails(detailsRequest);

                model.addAttribute("order", retrieveCartOrder(request, model));
                model.addAttribute("token", token);
                model.addAttribute("payerID", payerID);
                return "checkout/checkoutReview";//paypalProcess(model, token, payerID, checkoutForm, request);

        } catch (Exception e) {
            System.out.println(e);
            return "";
        }
    }*/

    @RequestMapping(value="/paypalProcess.htm", method = {RequestMethod.GET})
    public String paypalProcess(ModelMap model,
                                @RequestParam String token,
                                @RequestParam("PayerID") String payerID,
                                CheckoutForm checkoutForm,
                                HttpServletRequest request) {
        Order order = retrieveCartOrder(request, model);
        Map<PaymentInfo, Referenced> payments = new HashMap<PaymentInfo, Referenced>();
        for (PaymentInfo paymentInfo : order.getPaymentInfos()) {
            if (paymentInfo.getType() == PaymentInfoType.PAYPAL) {
                //There should only be one payment info of type paypal in the order
                paymentInfo.getAdditionalFields().put(MessageConstants.PAYERID, payerID);
                paymentInfo.getAdditionalFields().put(MessageConstants.TOKEN, token);
                paymentInfo.getAdditionalFields().put(MessageConstants.PAYPALMETHODTYPE, PayPalMethodType.PROCESS.getType());
                payments.put(paymentInfo, paymentInfo.createEmptyReferenced());
                break;
            }
        }
        
        order.setStatus(OrderStatus.SUBMITTED);
        order.setSubmitDate(Calendar.getInstance().getTime());

        try {
            checkoutService.performCheckout(order, payments);
        } catch (CheckoutException e) {
            LOG.error("Cannot perform checkout", e);
        }

        //Fill out a few customer values for anonymous customers
        Customer customer = order.getCustomer();
        if (StringUtils.isEmpty(customer.getFirstName())) {
            customer.setFirstName(checkoutForm.getBillingAddress().getFirstName());
        }
        if (StringUtils.isEmpty(customer.getLastName())) {
            customer.setLastName(checkoutForm.getBillingAddress().getLastName());
        }
        if (StringUtils.isEmpty(customer.getEmailAddress())) {
            customer.setEmailAddress(order.getEmailAddress());
        }
        customerService.saveCustomer(customer, false);

        return receiptView != null ? "redirect:" + receiptView : "redirect:/orders/viewOrderConfirmation.htm?orderNumber=" + order.getOrderNumber();
    }

    protected Order retrieveCartOrder(HttpServletRequest request, ModelMap model) {
        Customer currentCustomer = customerState.getCustomer(request);
        Order currentCartOrder = null;
        if (currentCustomer != null) {
            currentCartOrder = cartService.findCartForCustomer(currentCustomer);
            if (currentCartOrder == null) {
                currentCartOrder = cartService.createNewCartForCustomer(currentCustomer);
            }
        }

        return currentCartOrder;
    }

}