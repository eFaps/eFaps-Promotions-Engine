package org.efaps.promotionengine.condition;

public abstract class AbstractCondition
    implements ICondition
{

    private Operator operator;

    public Operator getOperator()
    {
        return operator;
    }

    public AbstractCondition setOperator(Operator operator)
    {
        this.operator = operator;
        return this;
    }
}
