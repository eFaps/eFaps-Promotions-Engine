package org.efaps.promotionengine.api;

import java.math.BigDecimal;

import org.efaps.abacus.api.ICalcPosition;

public interface IPosition extends ICalcPosition
{
    void setDiscount(BigDecimal netPrice);

    void setPromotionOid(String oid);

    boolean isBurned();
}
