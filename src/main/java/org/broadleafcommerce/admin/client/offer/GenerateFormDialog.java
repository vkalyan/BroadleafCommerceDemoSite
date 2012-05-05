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

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import org.broadleafcommerce.openadmin.client.BLCMain;

/**
 * 
 * @author jfischer
 *
 */
public class GenerateFormDialog extends Window {

	protected IButton saveButton;

	public GenerateFormDialog() {
		super();
		this.setIsModal(true);
		this.setShowModalMask(true);
		this.setShowMinimizeButton(false);
		this.setWidth(350);
		this.setHeight(300);
		this.setCanDragResize(true);
		this.setOverflow(Overflow.AUTO);
		this.setVisible(false);

        HLayout formLayout = new HLayout();
        formLayout.setLayoutMargin(20);
        formLayout.setAlign(Alignment.CENTER);
        formLayout.setWidth100();
        DynamicForm form = new DynamicForm();

        TextItem numberOfCodes = new TextItem("numberOfCodes", BLCMain.getMessageManager().getString("numberOfCodes"));
        numberOfCodes.setValue("100");
        numberOfCodes.setWrapTitle(false);
        TextItem codeLength = new TextItem("codeLength", BLCMain.getMessageManager().getString("codeLength"));
        codeLength.setValue("6");
        codeLength.setWrapTitle(false);
        TextItem codePrefix = new TextItem("codePrefix", BLCMain.getMessageManager().getString("codePrefix"));
        codePrefix.setWrapTitle(false);
        TextItem codeSuffix = new TextItem("codeSuffix", BLCMain.getMessageManager().getString("codeSuffix"));
        codeSuffix.setWrapTitle(false);
        TextItem codeMaxUses = new TextItem("maxUse", BLCMain.getMessageManager().getString("maxUse"));
        codeMaxUses.setWrapTitle(false);
        codeMaxUses.setValue("1");

        form.setFields(numberOfCodes, codeLength, codePrefix, codeSuffix, codeMaxUses);
        formLayout.addMember(form);
        
        addItem(formLayout);
        
        saveButton = new IButton(BLCMain.getMessageManager().getString("ok"));
        saveButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                SC.say(BLCMain.getMessageManager().getString("generateComplete"));
                hide();
            }
        });

        IButton cancelButton = new IButton(BLCMain.getMessageManager().getString("cancel"));
        cancelButton.addClickHandler(new ClickHandler() {  
            public void onClick(ClickEvent event) {  
            	hide();
            }  
        });
        
        HLayout hLayout = new HLayout(10);
        hLayout.setAlign(Alignment.CENTER);
        hLayout.addMember(saveButton);
        hLayout.addMember(cancelButton);
        hLayout.setLayoutTopMargin(10);
        hLayout.setLayoutBottomMargin(10);
        
        addItem(hLayout);
	}
	
	public void launch() {
		this.setTitle(BLCMain.getMessageManager().getString("generateTitle"));
		centerInPage();
		show();
	}

    public IButton getSaveButton() {
        return saveButton;
    }

    public void setSaveButton(IButton saveButton) {
        this.saveButton = saveButton;
    }
}
