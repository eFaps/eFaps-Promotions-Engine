package org.efaps.promotionengine.action;

import java.util.List;

import org.efaps.promotionengine.pojo.Position;
import org.efaps.promotionengine.process.ProcessData;

public interface IAction
{

    void run(final ProcessData process, final List<Position> position);

}
