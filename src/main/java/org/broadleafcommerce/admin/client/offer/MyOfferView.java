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

import java.util.LinkedHashMap;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import org.broadleafcommerce.admin.client.view.promotion.OfferView;
import org.broadleafcommerce.openadmin.client.BLCMain;
import org.broadleafcommerce.openadmin.client.view.TabSet;

/**
 * @author Jeff Fischer
 */
public class MyOfferView extends OfferView implements MyOfferDisplay {

    protected Tab codesTab;
    protected MultiCodesView multiCodesView;

    @Override
    public void build(DataSource entityDataSource, DataSource... additionalDataSources) {
        super.build(entityDataSource, additionalDataSources);

        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put("AUTOMATIC", BLCMain.getMessageManager().getString("deliveryTypeEnumAutomatic"));
        valueMap.put("CODE", BLCMain.getMessageManager().getString("deliveryTypeEnumCode"));
        valueMap.put("MULTICODE", BLCMain.getMessageManager().getString("deliveryTypeEnumMultiCode"));
        valueMap.put("MANUAL", BLCMain.getMessageManager().getString("deliveryTypeEnumManual"));
        deliveryTypeRadio.setValueMap(valueMap);

        //re-arrange the default offer form into a details tab - making possible a multi-codes tab
        TabSet topTabSet = new TabSet();
        topTabSet.setID("offerTopTabSet");
        topTabSet.setTabBarPosition(Side.TOP);
        topTabSet.setPaneContainerOverflow(Overflow.HIDDEN);
        topTabSet.setWidth("70%");
        topTabSet.setHeight100();
        topTabSet.setPaneMargin(0);
        addMember(topTabSet);

        Tab detailsTab = new Tab("Details");
        detailsTab.setID("offerDetailsTab");
        Canvas detailsPane = getMember("offerRightVerticalLayout");
        removeMember(detailsPane);
        detailsPane.setWidth100();
        detailsTab.setPane(detailsPane);
        topTabSet.addTab(detailsTab);

        multiCodesView = new MultiCodesView();
        codesTab = new Tab(BLCMain.getMessageManager().getString("codes"));
        codesTab.setID("codesTab");
        codesTab.setPane(multiCodesView);
        codesTab.setDisabled(true);
        topTabSet.addTab(codesTab);
    }

    @Override
    public Tab getCodesTab() {
        return codesTab;
    }

    @Override
    public ToolStripButton getGenerateButton() {
        return multiCodesView.getGenerateButton();
    }
}
