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
import java.util.function.Predicate;

import org.efaps.abacus.api.ICalcPosition;
import org.efaps.promotionengine.api.IPosition;
import org.efaps.promotionengine.process.ProcessData;

public abstract class AbstractTotalCondition
    extends AbstractOperatorCondition
{

    private BigDecimal total;

    public BigDecimal getTotal()
    {
        return total;
    }

    public AbstractTotalCondition setTotal(final BigDecimal total)
    {
        this.total = total;
        return this;
    }

    @Override
    public boolean positionMet(IPosition position)
    {
        return false;
    }

    @Override
    public List<IPosition> evalPositions(final ProcessData process)
    {
        return Collections.emptyList();
    }

    @Override
    public boolean isMet(final ProcessData process)
    {
        final var netTotal = process.getDocument().getPositions().stream()
                        .map(position -> ((IPosition) position))
                        .filter(getFilterPredicate())
                        .map(ICalcPosition::getNetPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        boolean ret = false;
        ret = switch (getOperator()) {
            case EQUAL -> netTotal.compareTo(getTotal()) == 0;
            case GREATER -> netTotal.compareTo(getTotal()) > 0;
            case GREATEREQUAL -> netTotal.compareTo(getTotal()) > -1;
            case SMALLER -> netTotal.compareTo(getTotal()) < 0;
            case SMALLEREQUAL -> netTotal.compareTo(getTotal()) < 1;
        };
        return ret;
    }

    protected abstract Predicate<IPosition> getFilterPredicate();

}
