package org.efaps.promotionengine.api;

import java.math.BigDecimal;

import org.efaps.abacus.api.ICalcDocument;

public interface IDocument extends ICalcDocument
{
    void setDiscount(BigDecimal discount);
}
