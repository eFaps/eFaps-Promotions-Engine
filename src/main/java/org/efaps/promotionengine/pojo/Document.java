package org.efaps.promotionengine.pojo;

import java.math.BigDecimal;

import org.efaps.abacus.pojo.CalcDocument;
import org.efaps.promotionengine.api.IDocument;
import org.efaps.promotionengine.api.IPosition;

public class Document
    extends CalcDocument
    implements IDocument
{

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

    public Document addPosition(IPosition position)
    {
        return (Document) super.addPosition(position);
    }

}
