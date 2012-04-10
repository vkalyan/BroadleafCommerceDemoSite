package com.other.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.broadleafcommerce.core.catalog.domain.Category;
import org.broadleafcommerce.core.catalog.domain.CategoryImpl;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "OTHER_CATEGORY")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blStandardElements")
public class OtherCategoryImpl extends CategoryImpl implements Category {

	private static final long serialVersionUID = 1L;
	
	@Column(name="TEST_PROPERTY_1")
	private String testProperty1;
	
	@Column(name="TEST_PROPERTY_2")
	private String testProperty2;

	public String getTestProperty1() {
		return testProperty1;
	}

	public void setTestProperty1(String testProperty1) {
		this.testProperty1 = testProperty1;
	}

	public String getTestProperty2() {
		return testProperty2;
	}

	public void setTestProperty2(String testProperty2) {
		this.testProperty2 = testProperty2;
	}
	
}
