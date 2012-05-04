package org.broadleafcommerce.demo.catalog.service;

import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.core.catalog.service.dynamic.DynamicSkuPrices;
import org.broadleafcommerce.core.catalog.service.dynamic.DynamicSkuPricingService;
import org.springframework.stereotype.Service;

@Service("blDynamicSkuPricingService")
public class CustomerLocaleDynamicSkuPricingService implements DynamicSkuPricingService {
	
	public static final String LOCALE_KEY = "locale";
	
	@Override
	public DynamicSkuPrices getSkuPrices(Sku sku, HashMap skuPricingConsiderations) {
		DynamicSkuPrices prices = new DynamicSkuPrices();
		org.broadleafcommerce.common.locale.domain.Locale customerLocale = (org.broadleafcommerce.common.locale.domain.Locale)skuPricingConsiderations.get(LOCALE_KEY);
		//Locale codes should conform to <language>_<country>
		Locale locale = new Locale(customerLocale.getLocaleCode().split("_")[0], customerLocale.getLocaleCode().split("_")[1]);
		
		prices.setRetailPrice(new Money(sku.getRetailPrice().getAmount(), Currency.getInstance(locale)));
		prices.setSalePrice(new Money(sku.getSalePrice().getAmount(), Currency.getInstance(locale)));
		return prices;
	}

}
