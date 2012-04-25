/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.other.api;

import com.other.domain.OtherProduct;
import org.apache.commons.lang.StringUtils;
import org.broadleafcommerce.cms.file.service.StaticAssetService;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.media.domain.Media;
import org.broadleafcommerce.core.web.api.wrapper.MediaWrapper;
import org.broadleafcommerce.core.web.api.wrapper.ProductWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * This is a JAXB wrapper around Product.
 *
 * User: Elbert Bautista
 * Date: 4/11/12
 */
@XmlRootElement(name = "otherProduct")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class OtherProductWrapper extends ProductWrapper {

    @XmlElement
    private Long companyNumber;

    @XmlElement
	private Date releaseDate;

    @XmlElement
    private String truncatedName;

    @XmlElement
    @XmlElementWrapper(name = "mediaList")
    private List<MediaWrapper> media;

    @Override
    public void wrap(Product model, HttpServletRequest request) {
        super.wrap(model, request);

        //Extending the ProductWrapper in the REST-ful API
        if (model instanceof OtherProduct) {
            OtherProduct otherProduct = (OtherProduct) model;
            companyNumber = otherProduct.getCompanyNumber();
            releaseDate= otherProduct.getReleaseDate();
        }

        //adding a new property just for display
        truncatedName = StringUtils.abbreviate(model.getName(), 20);

        //demonstrating something more complicated by adding a list of MediaWrappers
        if (model.getProductMedia() != null && !model.getProductMedia().isEmpty()) {
            media = new ArrayList<MediaWrapper>();

            for (Media m : model.getProductMedia().values()) {
                MediaWrapper wrapper = (MediaWrapper) context.getBean(MediaWrapper.class.getName());
                wrapper.wrap(m, request);
                media.add(wrapper);
            }

        }

        //Suppressing a default property on product
        super.manufacturer = null;
    }

}
