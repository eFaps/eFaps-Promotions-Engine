package org.efaps.promotionengine.condition;

import java.util.List;

import org.efaps.promotionengine.ProcessData;
import org.efaps.promotionengine.pojo.Position;

public interface ICondition
{

    boolean isMet(final ProcessData process);

    List<Position> evalPositions(final ProcessData process);

    boolean positionMet(final Position position);

}
