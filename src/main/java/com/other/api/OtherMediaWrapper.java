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

import org.broadleafcommerce.cms.file.service.StaticAssetService;
import org.broadleafcommerce.core.media.domain.Media;
import org.broadleafcommerce.core.web.api.wrapper.MediaWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is a JAXB wrapper around Media.
 *
 * User: Elbert Bautista
 * Date: 4/11/12
 */
@XmlRootElement(name = "media")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class OtherMediaWrapper extends MediaWrapper {

    @Override
    public void wrap(Media media, HttpServletRequest request) {
        super.wrap(media, request);

        //Extending the MediaWrapper in the REST-ful API
        StaticAssetService staticAssetService = (StaticAssetService) context.getBean("blStaticAssetService");
        super.url = staticAssetService.convertAssetPath(media.getUrl(), request.getContextPath(), request.isSecure());
    }
}
