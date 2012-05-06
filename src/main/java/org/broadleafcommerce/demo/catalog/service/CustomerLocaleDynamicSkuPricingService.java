package org.broadleafcommerce.demo.catalog.service;

import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

import org.broadleafcommerce.common.money.CurrencyConsiderationContext;
import org.broadleafcommerce.common.money.CurrencyConversionContext;
import org.broadleafcommerce.common.money.CurrencyConversionService;
import org.broadleafcommerce.common.money.Money;
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
		org.broadleafcommerce.common.locale.domain.Locale customerLocale = (org.broadleafcommerce.common.locale.domain.Locale)skuPricingConsiderations.get(LOCALE_KEY);
		//Locale codes should conform to <language>_<COUNTRY> or <language>
		String[] tokens = customerLocale.getLocaleCode().split("_");
		Locale locale = null;
		if (tokens.length == 2) {
			locale = new Locale(tokens[0], tokens[1]);
		} else if (tokens.length == 1) {
			locale = new Locale(tokens[0]);
		} else {
			throw new RuntimeException("Incorrectly formatted locale code: " + customerLocale.getLocaleCode());
		}
		
		Currency userCurrency = Currency.getInstance(locale);
		
		if (CurrencyConsiderationContext.getCurrencyDeterminationService() != null) {
			String systemCurrency = CurrencyConsiderationContext.getCurrencyDeterminationService().getCurrencyCode(CurrencyConsiderationContext.getCurrencyConsiderationContext());
			if (systemCurrency == userCurrency.getCurrencyCode()) {
				//currencies aren't different; don't do anything
				prices.setRetailPrice(sku.getRetailPrice());
				prices.setSalePrice(sku.getSalePrice());
			} else {
				CurrencyConversionService conversionService = CurrencyConversionContext.getCurrencyConversionService();
				if (conversionService == null) {
					throw new RuntimeException("User currency of " + userCurrency.getCurrencyCode() + " differs from the system currency of " + systemCurrency + ". Please configure a currency conversion service");
				} else {
					prices.setRetailPrice(conversionService.convertCurrency(sku.getRetailPrice(), userCurrency, 0));
					prices.setSalePrice(conversionService.convertCurrency(sku.getSalePrice(), userCurrency, 0));
				}
			}
		}
		
		return prices;
	}

}
