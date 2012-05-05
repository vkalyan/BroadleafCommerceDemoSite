/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.broadleafcommerce.admin.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.smartgwt.client.util.SC;
import org.broadleafcommerce.openadmin.client.reflection.ModuleFactory;

public class MyMerchandisingModule extends MerchandisingModule {
		
	public void onModuleLoad() {
		super.onModuleLoad();
		addConstants(GWT.<ConstantsWithLookup>create(MyAdminMessages.class));
		
		ModuleFactory moduleFactory = ModuleFactory.getInstance();
        moduleFactory.put("offer", "org.broadleafcommerce.admin.client.offer.MyOfferView");
		moduleFactory.put("offerPresenter", "org.broadleafcommerce.admin.client.offer.MyOfferPresenter");

        GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
            public void onUncaughtException(Throwable e) {
                if (e.getMessage() != null) {
                	SC.say(e.getMessage());
                } else {
                	SC.say("Uncaught Exception: " + e.getClass().getName());
                	e.printStackTrace();
                }
                
            }
        });
	}

}