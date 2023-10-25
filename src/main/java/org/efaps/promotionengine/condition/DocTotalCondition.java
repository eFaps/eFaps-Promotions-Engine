package org.efaps.promotionengine.condition;

import java.math.BigDecimal;
import java.util.List;

import org.efaps.promotionengine.pojo.Position;
import org.efaps.promotionengine.process.ProcessData;

public class DocTotalCondition extends AbstractCondition
{
    BigDecimal total;

    @Override
    public boolean isMet(ProcessData process)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<Position> evalPositions(ProcessData process)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean positionMet(Position position)
    {
        // TODO Auto-generated method stub
        return false;
    }


}
