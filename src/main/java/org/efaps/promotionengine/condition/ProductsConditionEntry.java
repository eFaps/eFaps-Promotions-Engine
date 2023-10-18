package org.efaps.promotionengine.condition;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ProductsConditionEntry
{

    private String productOid;

    private BigDecimal quantity;

    public String getProductOid()
    {
        return productOid;
    }

    public ProductsConditionEntry setProductOid(String productOid)
    {
        this.productOid = productOid;
        return this;
    }

    public BigDecimal getQuantity()
    {
        return quantity;
    }

    public ProductsConditionEntry setQuantity(BigDecimal quantity)
    {
        this.quantity = quantity;
        return this;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
