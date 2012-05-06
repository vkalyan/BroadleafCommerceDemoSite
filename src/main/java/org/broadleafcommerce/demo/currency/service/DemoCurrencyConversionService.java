package org.broadleafcommerce.demo.currency.service;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

import org.broadleafcommerce.common.money.BankersRounding;
import org.broadleafcommerce.common.money.CurrencyConversionService;
import org.broadleafcommerce.common.money.Money;
import org.springframework.stereotype.Service;


@Service
public class DemoCurrencyConversionService implements CurrencyConversionService {
	
	/**
	 * Maps currency code -> (currency code, exchange rate)
	 */
	private HashMap<String, HashMap<String, BigDecimal>> conversionTable;
	
	@Override
	public Money convertCurrency(Money source, Currency destinationCurrency, int destinationScale) {
		if (conversionTable == null) {
			initializeConversionTable();
		}
		
		if (source.getCurrency().equals(destinationCurrency)) {
			return source;
		} else {
			Currency sourceCurrency = source.getCurrency();
			HashMap<String, BigDecimal> rates = conversionTable.get(sourceCurrency.getCurrencyCode());
			if (rates == null) {
				throw new UnsupportedOperationException("Conversion not supported for source currency " + sourceCurrency.getCurrencyCode());
			}
			
			BigDecimal multiplier = rates.get(destinationCurrency.getCurrencyCode());
			if (multiplier == null) {
				throw new UnsupportedOperationException("Conversion not supported for destination currency " + destinationCurrency.getCurrencyCode());
			}
			
			BigDecimal convertedAmount = source.getAmount().multiply(multiplier);
			if (destinationScale == 0) {
				if (source.getAmount().scale() == 0) {
					destinationScale = BankersRounding.DEFAULT_SCALE;
				} else {
					destinationScale = source.getAmount().scale();
				}
			}
			
			return new Money(convertedAmount, destinationCurrency, destinationScale);
		}
	}

	public void initializeConversionTable() {
		conversionTable = new HashMap<String, HashMap<String,BigDecimal>>();
		//create default currencies for the ones supported in load_content_structure.sql
		//USD
		Currency usCurrency = Currency.getInstance(new Locale("en", "US"));
		//GBP
		Currency ukCurrency = Currency.getInstance(new Locale("en", "GB"));
		//SEK
		Currency swedenCurrency = Currency.getInstance(new Locale("sv", "SE"));
		
		HashMap<String, BigDecimal> usRates = new HashMap<String, BigDecimal>();
		usRates.put(ukCurrency.getCurrencyCode(), new BigDecimal("0.6193"));
		usRates.put(swedenCurrency.getCurrencyCode(), new BigDecimal("6.8188"));
		conversionTable.put(usCurrency.getCurrencyCode(), usRates);
		
		HashMap<String, BigDecimal> ukRates = new HashMap<String, BigDecimal>();
		ukRates.put(usCurrency.getCurrencyCode(), new BigDecimal("1.6146"));
		ukRates.put(swedenCurrency.getCurrencyCode(), new BigDecimal("11.0096"));
		conversionTable.put(ukCurrency.getCurrencyCode(), ukRates);
		
		HashMap<String, BigDecimal> swedenRates = new HashMap<String, BigDecimal>();
		swedenRates.put(ukCurrency.getCurrencyCode(), new BigDecimal("0.0908"));
		swedenRates.put(usCurrency.getCurrencyCode(), new BigDecimal("0.1467"));
		conversionTable.put(swedenCurrency.getCurrencyCode(), swedenRates);
	}

}
