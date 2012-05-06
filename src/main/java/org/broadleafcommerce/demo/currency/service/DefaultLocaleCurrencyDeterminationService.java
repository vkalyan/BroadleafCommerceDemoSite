package org.broadleafcommerce.demo.currency.service;

import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

import org.broadleafcommerce.common.money.CurrencyDeterminationService;
import org.springframework.stereotype.Service;

@Service
public class DefaultLocaleCurrencyDeterminationService implements CurrencyDeterminationService {
	
	public static final String DEFAULT_LOCALE_KEY = "defaultLocale";
	
	@Override
	public String getCurrencyCode(HashMap currencyConsiderations) {
		org.broadleafcommerce.common.locale.domain.Locale defaultLocale = (org.broadleafcommerce.common.locale.domain.Locale)currencyConsiderations.get(DEFAULT_LOCALE_KEY);
		if (defaultLocale == null) {
			Locale locale = Locale.getDefault();
	        if (locale.getCountry() != null && locale.getCountry().length() == 2) {
	            return Currency.getInstance(locale).getCurrencyCode();
	        }
	        return Currency.getInstance("USD").getCurrencyCode();
		}
		//Locale codes should conform to <language>_<country>
		Locale locale = new Locale(defaultLocale.getLocaleCode().split("_")[0], defaultLocale.getLocaleCode().split("_")[1]);

		return Currency.getInstance(locale).getCurrencyCode();
	}

}
