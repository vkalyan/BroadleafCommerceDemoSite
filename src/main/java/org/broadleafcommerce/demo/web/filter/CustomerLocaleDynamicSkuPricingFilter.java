package org.broadleafcommerce.demo.web.filter;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.broadleafcommerce.core.catalog.service.dynamic.DynamicSkuPricingService;
import org.broadleafcommerce.core.web.catalog.AbstractDynamicSkuPricingFilter;
import org.broadleafcommerce.demo.catalog.service.CustomerLocaleDynamicSkuPricingService;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.web.core.CustomerState;

public class CustomerLocaleDynamicSkuPricingFilter extends AbstractDynamicSkuPricingFilter {
	
	@Resource(name="customerLocaleDynamicSkuPricingService")
	DynamicSkuPricingService pricingService;
	
	@Resource(name="blCustomerState")
    protected CustomerState customerState;
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap getPricingConsiderations(ServletRequest request) {
		Customer customer = customerState.getCustomer((HttpServletRequest)request);
		HashMap pricingConsiderations = new HashMap();
		pricingConsiderations.put(CustomerLocaleDynamicSkuPricingService.LOCALE_KEY, customer.getCustomerLocale());
		
		return pricingConsiderations;
	}

	@Override
	public DynamicSkuPricingService getDynamicSkuPricingService(ServletRequest request) {
		return pricingService;
	}
	
	
}
