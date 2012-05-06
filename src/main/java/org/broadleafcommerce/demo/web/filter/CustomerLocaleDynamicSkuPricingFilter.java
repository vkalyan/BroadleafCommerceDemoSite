package org.broadleafcommerce.demo.web.filter;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.broadleafcommerce.common.money.CurrencyConversionContext;
import org.broadleafcommerce.core.catalog.service.dynamic.DynamicSkuPricingService;
import org.broadleafcommerce.core.web.catalog.AbstractDynamicSkuPricingFilter;
import org.broadleafcommerce.demo.catalog.service.CustomerLocaleDynamicSkuPricingService;
import org.broadleafcommerce.demo.currency.service.DemoCurrencyConversionService;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.web.core.CustomerState;

public class CustomerLocaleDynamicSkuPricingFilter extends AbstractDynamicSkuPricingFilter {
	
	@Resource(name="blDynamicSkuPricingService")
	DynamicSkuPricingService pricingService;
	
	@Resource(name="blCustomerState")
    protected CustomerState customerState;
	
	@Resource
	private DemoCurrencyConversionService demoCurrencyConversionService;
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap getPricingConsiderations(ServletRequest request) {
		Customer customer = customerState.getCustomer((HttpServletRequest)request);
		HashMap pricingConsiderations = new HashMap();
		pricingConsiderations.put(CustomerLocaleDynamicSkuPricingService.LOCALE_KEY, customer.getCustomerLocale());
		
		CurrencyConversionContext.setCurrencyConversionService(demoCurrencyConversionService);
		CurrencyConversionContext.setCurrencyConversionContext(new HashMap());
		
		return pricingConsiderations;
	}

	@Override
	public DynamicSkuPricingService getDynamicSkuPricingService(ServletRequest request) {
		return pricingService;
	}
	
	
}
