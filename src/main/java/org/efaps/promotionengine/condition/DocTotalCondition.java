/*
 * Copyright © 2003 - 2024 The eFaps Team (-)
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
 */
package org.efaps.promotionengine.condition;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.efaps.promotionengine.api.IPosition;
import org.efaps.promotionengine.process.ProcessData;

public class DocTotalCondition
    extends AbstractOperatorCondition
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
    public List<IPosition> evalPositions(final ProcessData process)
    {
        return Collections.emptyList();
    }

    @Override
    public boolean positionMet(final IPosition position)
    {
        return false;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof final DocTotalCondition condition)) {
            return false;
        }
        return Objects.equals(this.getOperator(), condition.getOperator())
                        && (this.total == null && condition.total == null
                                        || this.total != null && condition.total != null
                                                        && this.total.compareTo(condition.total) == 0);
    }
}
