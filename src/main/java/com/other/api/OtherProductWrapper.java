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
 * Created by IntelliJ IDEA.
 * User: developer
 * Date: 4/10/12
 * Time: 7:07 AM
 * To change this template use File | Settings | File Templates.
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

        //Suppressing a default property
        super.manufacturer = null;

    }

}
