package org.broadleafcommerce.demo.web.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.broadleafcommerce.common.locale.domain.Locale;
import org.broadleafcommerce.common.locale.service.LocaleService;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.service.CustomerService;
import org.broadleafcommerce.profile.web.core.CustomerState;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CustomerLocaleController {
	
	@Resource(name="blLocaleService")
	private LocaleService localeService;
	
	@Resource(name="blCustomerState")
	private CustomerState customerState;
	
	@Resource(name="blCustomerService")
	private CustomerService customerService;
	
	@RequestMapping(value="/updateLocale", method=RequestMethod.POST)
	public String updateCustomerLocale(HttpServletRequest request, @RequestParam(value="localeCode") String localeCode) {
		String url = "redirect:" + request.getHeader("referer");
		
		Locale changeLocale = localeService.findLocaleByCode(localeCode);
		if (changeLocale == null) {
			return url;
		} else {
			Customer customer = customerState.getCustomer(request);
			customer.setCustomerLocale(changeLocale);
			customerService.saveCustomer(customer);
			return url;
		}
	}
}
