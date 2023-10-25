package org.efaps.promotionengine.action;

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
    extends AbstractPercentageAction
{

    private static final Logger LOG = LoggerFactory.getLogger(Engine.class);

    @Override
    public void run(final ProcessData process,
                    final List<Position> positions)
    {
        if (!positions.isEmpty()) {
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
    }

    public void apply(final ProcessData process,
                      final Position position)
    {
        position.setAppliedPromotionOid(process.getCurrentPromotion().getOid());
        position.setCrossTotal(calculate(position.getCrossTotal()));
        LOG.info("Applied action on positon: {}", position);
    }
}
