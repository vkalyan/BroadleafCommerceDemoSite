package com.other.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.core.catalog.domain.SkuImpl;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "OTHER_SKU")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blStandardElements")
public class OtherSkuImpl extends SkuImpl {

	private static final long serialVersionUID = 1L;

	@Column(name="DETERMINED_PRICE")
	private BigDecimal determinedPrice;

	public Money getDeterminedPrice() {
		return determinedPrice == null ? null : new Money(determinedPrice);
	}

	public void setDeterminedPrice(Money determinedPrice) {
		this.determinedPrice = Money.toAmount(determinedPrice);
	}
	
}
