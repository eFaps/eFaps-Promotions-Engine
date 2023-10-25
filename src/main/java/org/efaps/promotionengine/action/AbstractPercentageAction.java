package org.efaps.promotionengine.action;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class AbstractPercentageAction
    extends AbstractAction
{

    private BigDecimal percentage;

    public BigDecimal getPercentage()
    {
        return percentage;
    }

    public AbstractPercentageAction setPercentage(final BigDecimal percentage)
    {
        this.percentage = percentage;
        return this;
    }

    public BigDecimal calculate(final BigDecimal original)
    {
        // discounted price = original price - (original price × discount / 100)
        return original.subtract(
                        original.multiply(
                                        getPercentage().setScale(8).divide(new BigDecimal(100), RoundingMode.HALF_UP)));
    }
}
