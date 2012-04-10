package com.other.api;

import com.other.domain.OtherProduct;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.web.api.wrapper.ProductWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

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

    @Override
    public void wrap(Product model, HttpServletRequest request) {
        super.wrap(model, request);

        //Extending the ProductWrapper in the REST-ful API
        OtherProduct otherProduct = (OtherProduct) model;
        companyNumber = otherProduct.getCompanyNumber();
        releaseDate= otherProduct.getReleaseDate();

        //Suppressing a default property on product
        super.manufacturer = null;

    }

}
