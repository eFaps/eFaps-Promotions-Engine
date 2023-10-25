package org.efaps.promotionengine.action;

import java.util.List;

import org.efaps.promotionengine.pojo.Position;
import org.efaps.promotionengine.process.ProcessData;

public class PercentageDocDiscountAction extends AbstractPercentageAction
{

    @Override
    public void run(final ProcessData process,
                    final List<Position> position)
    {
        process.getDocument().setCrossTotal(calculate(process.getDocument().getCrossTotal()));
    }

}
