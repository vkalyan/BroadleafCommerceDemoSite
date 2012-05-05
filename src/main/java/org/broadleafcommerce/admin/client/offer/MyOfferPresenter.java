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

package org.broadleafcommerce.admin.client.offer;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import org.broadleafcommerce.admin.client.presenter.promotion.OfferPresenter;

public class MyOfferPresenter extends OfferPresenter {

    protected GenerateFormDialog generateFormDialog = new GenerateFormDialog();

    @Override
    public void bind() {
        super.bind();
        getDisplay().getDeliveryTypeRadio().addChangedHandler(new ChangedHandler() {
            public void onChanged(ChangedEvent event) {
                String deliveryType = event.getValue().toString();
                if (deliveryType.equals("MULTICODE")) {
                    ((MyOfferDisplay) getDisplay()).getCodesTab().setDisabled(false);
                } else {
                    ((MyOfferDisplay) getDisplay()).getCodesTab().setDisabled(true);
                }
            }
        });
        ((MyOfferDisplay) getDisplay()).getGenerateButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (event.isLeftButtonDown()) {
                    generateFormDialog.launch();
                }
            }
        });
    }
}
