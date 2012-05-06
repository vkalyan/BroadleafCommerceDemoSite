package org.broadleafcommerce.demo.web.filter;

import java.io.IOException;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.broadleafcommerce.common.locale.domain.Locale;
import org.broadleafcommerce.common.locale.service.LocaleService;
import org.broadleafcommerce.common.money.CurrencyConsiderationContext;
import org.broadleafcommerce.demo.currency.service.DefaultLocaleCurrencyDeterminationService;

public class CurrencyDeterminationFilter implements Filter {

	@Resource(name="blLocaleService")
	private LocaleService localeService;
	
	@Resource
	private DefaultLocaleCurrencyDeterminationService currencyDeterminationService;
	
	//@Resource
	//private
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// do nothing
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HashMap map = new HashMap();
		Locale defaultLocale = localeService.findDefaultLocale();
		map.put(DefaultLocaleCurrencyDeterminationService.DEFAULT_LOCALE_KEY, defaultLocale);
		
		//put the default locale on the request so that the header can give the correct info in the select box
		request.setAttribute("defaultLocale", defaultLocale);
		
		CurrencyConsiderationContext.setCurrencyConsiderationContext(map);
		CurrencyConsiderationContext.setCurrencyDeterminationService(currencyDeterminationService);
		
		try {
			chain.doFilter(request, response);
		} finally {
			CurrencyConsiderationContext.setCurrencyConsiderationContext(null);
			CurrencyConsiderationContext.setCurrencyDeterminationService(null);
		}
	}

	@Override
	public void destroy() {
		// do nothing
	}

}
