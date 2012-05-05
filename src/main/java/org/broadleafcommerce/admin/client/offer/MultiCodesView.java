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

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import org.broadleafcommerce.openadmin.client.BLCMain;

/**
 * @author Jeff Fischer
 */
public class MultiCodesView extends VLayout {

    protected ToolStrip toolBar;
    protected ToolStripButton generateButton;
    protected ListGrid grid;

    public MultiCodesView() {
        setHeight100();
        setWidth100();
        setBackgroundColor("#eaeaea");
        setOverflow(Overflow.AUTO);

        HStack hStack = new HStack(10);

        hStack.setHeight("45%");
        hStack.setWidth100();
        hStack.setBackgroundColor("#eaeaea");
        hStack.setAlign(Alignment.CENTER);

        VLayout stack = new VLayout();
        stack.setHeight100();
        stack.setWidth100();
        //stack.setLayoutMargin(12);

        toolBar = new ToolStrip();
        toolBar.setHeight(30);
        toolBar.setWidth100();
        toolBar.setMinWidth(300);
        toolBar.addSpacer(6);
        generateButton = new ToolStripButton();
        generateButton.setTitle(BLCMain.getMessageManager().getString("generateTitle"));
        generateButton.setIcon(GWT.getModuleBaseURL() + "sc/skins/Enterprise/images/actions/add.png");
        //generateButton.setDisabled(true);
        toolBar.addButton(generateButton);
        toolBar.addFill();
        stack.addMember(toolBar);

        DataSource ds = new DataSource();
        DataSourceField code = new DataSourceTextField("code", BLCMain.getMessageManager().getString("code"));
        DataSourceField status = new DataSourceTextField("status", BLCMain.getMessageManager().getString("status"));
        DataSourceField maxUse = new DataSourceTextField("maxUse", BLCMain.getMessageManager().getString("maxUse"));
        ds.addField(code);
        ds.addField(status);
        ds.addField(maxUse);

        grid = new ListGrid();
        grid.setAutoFetchData(false);
        grid.setShowHeader(true);
        grid.setShowHeaderContextMenu(false);
        grid.setDisabled(true);
        grid.setCanSort(false);
        grid.setCellPadding(5);
        grid.setAlternateRecordStyles(true);
        grid.setDataSource(ds);
        stack.addMember(grid);

        hStack.addMember(stack);
        hStack.setOverflow(Overflow.AUTO);
        hStack.setShowResizeBar(true);

        addMember(hStack);
    }

    public ToolStripButton getGenerateButton() {
        return generateButton;
    }

    public ListGrid getGrid() {
        return grid;
    }

    public ToolStrip getToolBar() {
        return toolBar;
    }
}
