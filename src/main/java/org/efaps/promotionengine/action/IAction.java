package org.efaps.promotionengine.action;

import java.util.List;

import org.efaps.promotionengine.ProcessData;
import org.efaps.promotionengine.pojo.Position;

public interface IAction
{

    void run(final ProcessData process, final List<Position> position);

}
