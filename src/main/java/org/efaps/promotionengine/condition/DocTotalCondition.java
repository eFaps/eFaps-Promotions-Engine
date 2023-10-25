package org.efaps.promotionengine.condition;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.efaps.promotionengine.pojo.Position;
import org.efaps.promotionengine.process.ProcessData;

public class DocTotalCondition
    extends AbstractCondition
{

    private BigDecimal total;

    public BigDecimal getTotal()
    {
        return total;
    }

    public DocTotalCondition setTotal(final BigDecimal total)
    {
        this.total = total;
        return this;
    }

    @Override
    public boolean isMet(final ProcessData process)
    {
        boolean ret = false;
        switch (getOperator()) {
            case EQUAL:
                ret = process.getDocument().getCrossTotal().compareTo(getTotal()) == 0;
                break;
            case GREATER:
                ret = process.getDocument().getCrossTotal().compareTo(getTotal()) > 0;
                break;
            case GREATEREQUAL:
                ret = process.getDocument().getCrossTotal().compareTo(getTotal()) > -1;
                break;
            case SMALLER:
                ret = process.getDocument().getCrossTotal().compareTo(getTotal()) < 0;
                break;
            case SMALLEREQUAL:
                ret = process.getDocument().getCrossTotal().compareTo(getTotal()) < 1;
                break;
        }
        return ret;
    }

    @Override
    public List<Position> evalPositions(final ProcessData process)
    {
        return Collections.emptyList();
    }

    @Override
    public boolean positionMet(final Position position)
    {
        return false;
    }
}
