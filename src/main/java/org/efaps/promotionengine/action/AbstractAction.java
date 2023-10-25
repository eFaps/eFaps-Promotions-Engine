package org.efaps.promotionengine.action;

public abstract class AbstractAction
    implements IAction
{

    private Strategy strategy = Strategy.CHEAPEST;

    public Strategy getStrategy()
    {
        return strategy;
    }

    public void setStrategy(Strategy strategy)
    {
        this.strategy = strategy;
    }
}
