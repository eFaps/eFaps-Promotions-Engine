/*
 * Copyright 2003 - 2023 The eFaps Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
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
