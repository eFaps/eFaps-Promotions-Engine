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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.efaps.abacus.api.ICalcPosition;
import org.efaps.promotionengine.api.IPosition;
import org.efaps.promotionengine.process.ProcessData;
import org.efaps.promotionengine.process.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractProductCondition<T>
    extends AbstractOperatorCondition
{

    private static final Logger LOG = LoggerFactory.getLogger(AbstractProductCondition.class);

    private EntryOperator entryOperator;

    private BigDecimal positionQuantity;

    public EntryOperator getEntryOperator()
    {
        return entryOperator;
    }

    @SuppressWarnings("unchecked")
    public T setEntryOperator(final EntryOperator entryOperator)
    {
        this.entryOperator = entryOperator;
        return (T) this;
    }

    public BigDecimal getPositionQuantity()
    {
        return positionQuantity;
    }

    @SuppressWarnings("unchecked")
    public T setPositionQuantity(final BigDecimal quantity)
    {
        this.positionQuantity = quantity;
        return (T) this;
    }

    public abstract List<String> getProducts();

    @Override
    public boolean isMet(final ProcessData process)
    {
        LOG.info("Checking if condition is met for Document: {}", process.getDocument());
        var ret = false;
        ret = switch (getEntryOperator()) {
            case INCLUDES_ANY -> includesAny(process).size() > 0;
            case EXCLUDES -> checkExclude(process);
            default -> throw new IllegalArgumentException("Unexpected value: " + getEntryOperator());
        };
        LOG.info("Condition met: {}", ret);
        return ret;
    }

    /**
     * Check that at least one position has a product that is not excluded
     */
    protected boolean checkExclude(final ProcessData process)
    {
        final Set<String> positionProducts = process.getDocument().getPositions().stream()
                        .map(ICalcPosition::getProductOid)
                        .distinct().collect(Collectors.toSet());

        boolean atLeastOneProduct = false;
        for (final var prodOid : positionProducts) {
            if (!getProducts().contains(prodOid)) {
                atLeastOneProduct = true;
                break;
            }
        }
        return atLeastOneProduct;
    }

    @Override
    public List<IPosition> evalPositions(final ProcessData process)
    {
        List<IPosition> ret = new ArrayList<>();
        ret = switch (getEntryOperator()) {
            case INCLUDES_ANY -> includesAny(process);
            case EXCLUDES -> exclude(process);
            default -> throw new IllegalArgumentException("Unexpected value: " + getEntryOperator());
        };
        return ret;
    }

    @Override
    public boolean positionMet(final IPosition position)
    {
        return switch (getEntryOperator()) {
            case INCLUDES_ANY -> getProducts().contains(position.getProductOid());
            case EXCLUDES -> !getProducts().contains(position.getProductOid());
            default -> throw new IllegalArgumentException("Unexpected value: " + getEntryOperator());
        };
    }

    private List<IPosition> exclude(final ProcessData process)
    {
        final var ret = new ArrayList<IPosition>();
        for (final var calcPosition : process.getDocument().getPositions()) {
            final var position = (IPosition) calcPosition;
            if (positionMet(position)) {
                ret.add(position);
            }
        }
        return ret;
    }


    private List<IPosition> includesAny(final ProcessData process)
    {
        final var ret = new ArrayList<IPosition>();
        for (final var product : getProducts()) {
            var quantity = getPositionQuantity();
            for (final var calcPosition : process.getDocument().getPositions()) {
                final var position = (IPosition) calcPosition;
                if (position.getProductOid().equals(product)
                                && !process.getPositionsUsedForSouce().contains(position)) {
                    quantity = quantity.subtract(position.getQuantity());
                    LOG.info("Found product with oid: {} and quantity: {}", position.getProductOid(), quantity);
                    process.registerConditionMet(position);
                    ret.add(position);
                    // for source interrupt as soon as it is found
                    if (quantity.compareTo(BigDecimal.ZERO) < 1 && process.getStep().equals(Step.SOURCECONDITION)) {
                        break;
                    }
                }
            }
            // for source interrupt as soon as it is found
            if (quantity.compareTo(BigDecimal.ZERO) < 1 && process.getStep().equals(Step.SOURCECONDITION)) {
                break;
            }
        }
        return ret;
    }

}
