package org.efaps.promotionengine.pojo;

import java.math.BigDecimal;

import org.efaps.abacus.pojo.CalcPosition;
import org.efaps.promotionengine.api.IPosition;

public class Position
    extends CalcPosition
    implements IPosition
{

    private String promotionOid;
    private BigDecimal discount;


    public BigDecimal getDiscount()
    {
        return discount;
    }

    @Override
    public void setDiscount(BigDecimal discount)
    {
        this.discount = discount;
    }

    @Override
    public void setPromotionOid(String oid)
    {
       this.promotionOid = oid;
    }

    @Override
    public boolean isBurned()
    {
        return promotionOid != null;
    }

    @Override
    public Position setNetUnitPrice(BigDecimal netUnitPrice)
    {
        return (Position) super.setNetUnitPrice(netUnitPrice);
    }

    @Override
    public Position setQuantity(BigDecimal quantity)
    {
        return (Position) super.setQuantity(quantity);
    }

    @Override
    public Position setProductOid(String productOid)
    {
        return (Position) super.setProductOid(productOid);
    }

    @Override
    public BigDecimal getNetUnitPrice()
    {
        return getDiscount() != null ? super.getNetUnitPrice().subtract(discount) : super.getNetUnitPrice();
    }
}
