package org.broadleafcommerce.demo.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.broadleafcommerce.common.locale.domain.Locale;
import org.broadleafcommerce.common.locale.service.LocaleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DefaultLocaleController {
	
	@Resource(name="blLocaleService")
	private LocaleService localeService;
	
	@RequestMapping(value="/updateDefaultLocale", method=RequestMethod.POST)
	public String updateCustomerLocale(HttpServletRequest request, @RequestParam(value="localeCode") String localeCode) {
		String url = "redirect:" + request.getHeader("referer");
		
		Locale changeLocale = localeService.findLocaleByCode(localeCode);
		if (changeLocale == null) {
			return url;
		} else {
			//reset initial locale, save off the other
			Locale defaultLocale = localeService.findDefaultLocale();
			if (defaultLocale != null) {
				defaultLocale.setDefaultFlag(false);
				localeService.save(defaultLocale);
			}
			changeLocale.setDefaultFlag(true);
			localeService.save(changeLocale);
			
			return url;
		}
	}
}
