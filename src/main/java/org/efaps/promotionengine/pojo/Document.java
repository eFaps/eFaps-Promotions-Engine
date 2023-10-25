package org.efaps.promotionengine.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Document
{

    private BigDecimal crossTotal;

    private List<Position> positions;

    public Document setPositions(final List<Position> positions)
    {
        this.positions = positions;
        return this;
    }

    public List<Position> getPositions()
    {
        return positions;
    }

    public Document addPosition(final Position position)
    {
        if (positions == null) {
            positions = new ArrayList<>();
        }
        if (position.getIndex() == null) {
            position.setIndex(positions.size() + 1);
        }
        positions.add(position);
        return this;
    }

    public BigDecimal getCrossTotal()
    {
        return crossTotal;
    }

    public Document setCrossTotal(final BigDecimal crossTotal)
    {
        this.crossTotal = crossTotal;
        return this;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
