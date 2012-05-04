package org.broadleafcommerce.demo.catalog.service;

import java.util.HashMap;

import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.core.catalog.service.dynamic.DynamicSkuPrices;
import org.broadleafcommerce.core.catalog.service.dynamic.DynamicSkuPricingService;
import org.springframework.stereotype.Service;

@Service
public class CustomerLocaleDynamicSkuPricingService implements DynamicSkuPricingService {
	
	public static final String LOCALE_KEY = "locale";
	
	@Override
	public DynamicSkuPrices getSkuPrices(Sku sku, HashMap skuPricingConsiderations) {
		DynamicSkuPrices prices = new DynamicSkuPrices();
		
		return null;
	}

}
