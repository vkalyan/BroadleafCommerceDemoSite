package com.other.domain;

import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.core.catalog.domain.Sku;

public interface OtherSku extends Sku {

	public Money getDeterminedPrice();

	public void setDeterminedPrice(Money determinedPrice);
	
}
