package org.efaps.promotionengine.action;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.efaps.promotionengine.pojo.Position;
import org.efaps.promotionengine.process.Engine;
import org.efaps.promotionengine.process.ProcessData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PercentageDiscountAction
    extends AbstractAction
{

    private static final Logger LOG = LoggerFactory.getLogger(Engine.class);

    private BigDecimal percentage;

    public BigDecimal getPercentage()
    {
        return percentage;
    }

    public PercentageDiscountAction setPercentage(BigDecimal percentage)
    {
        this.percentage = percentage;
        return this;
    }

    @Override
    public void run(final ProcessData process,
                    final List<Position> positions)
    {
        LOG.info("Applying action on positions: {}", positions);
        final List<Position> strategySorted = new ArrayList<>(positions);
        if (!process.getPositionsUsedForSouce().isEmpty()) {
            strategySorted.addAll(process.getPositionsUsedForSouce());
        }
        switch (getStrategy()) {
            case CHEAPEST:
            default:
                Collections.sort(strategySorted, Comparator.comparing(Position::getCrossTotal));
                apply(process, strategySorted.get(0));
        }
    }

    public void apply(final ProcessData process,
                      final Position position)
    {
        position.setAppliedPromotionOid(process.getCurrentPromotion().getOid());
        position.setCrossTotal(calculate(position.getCrossTotal()));
        LOG.info("Applied action on positon: {}", position);
    }

    public BigDecimal calculate(BigDecimal original)
    {
        // discounted price = original price - (original price × discount / 100)
        return original.subtract(
                        original.multiply(percentage.setScale(8).divide(new BigDecimal(100), RoundingMode.HALF_UP)));
    }
}
