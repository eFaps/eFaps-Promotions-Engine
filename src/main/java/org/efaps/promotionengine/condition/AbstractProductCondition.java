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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
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

    private boolean allowTargetSameAsSource;

    public boolean isAllowTargetSameAsSource()
    {
        return allowTargetSameAsSource;
    }

    @SuppressWarnings("unchecked")
    public T setAllowTargetSameAsSource(boolean allowTargetSameAsSource)
    {
        this.allowTargetSameAsSource = allowTargetSameAsSource;
        return (T) this;
    }

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

    public abstract Set<String> getProducts();

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
        final boolean checkQuantity = getPositionQuantity().compareTo(BigDecimal.ZERO) > 0;
        var quantity = getPositionQuantity();
        if (process.getStep().equals(Step.SOURCECONDITION)) {
            for (final var calcPosition : process.getDocument().getPositions()) {
                final var position = (IPosition) calcPosition;
                if (!position.isBurned(process) && getProducts().contains(position.getProductOid())) {
                    quantity = quantity.subtract(position.getQuantity());
                    LOG.info("Found product with oid: {} and quantity: {}", position.getProductOid(), quantity);

                    ret.add(position);
                    if (checkQuantity && quantity.compareTo(BigDecimal.ZERO) < 1) {
                        break;
                    }
                }
            }
            if (quantity.compareTo(BigDecimal.ZERO) < 1) {
                ret.forEach(pos -> process.registerConditionMet(pos));
            } else {
                ret.clear();
            }
        } else {
            // target
            final var positions = process.getDocument().getPositions().stream().map(pos -> (IPosition) pos)
                            .collect(Collectors.toList());
            switch (process.getCurrentAction().getStrategy()) {
                case PRICIEST:
                    Collections.sort(positions, Comparator.comparing(IPosition::getNetUnitPrice));
                    Collections.reverse(positions);
                    break;
                case CHEAPEST:
                    Collections.sort(positions, Comparator.comparing(IPosition::getNetUnitPrice));
                    break;
                case INDEX:
                default:
            }

            for (final var position : positions) {
                if (!position.isBurned(process)) {
                    if (isAllowTargetSameAsSource() && getProducts().contains(position.getProductOid())) {
                        quantity = quantity.subtract(position.getQuantity());
                        LOG.info("Found product with oid: {} and quantity: {}", position.getProductOid(), quantity);
                        process.registerConditionMet(position);
                        ret.add(position);
                        if (checkQuantity && quantity.compareTo(BigDecimal.ZERO) < 1) {
                            break;
                        }
                    } else if (getProducts().contains(position.getProductOid())
                                    && canbeUsed(process, positions, position)) {
                        quantity = quantity.subtract(position.getQuantity());
                        LOG.info("Found product with oid: {} and quantity: {}", position.getProductOid(), quantity);
                        process.registerConditionMet(position);
                        ret.add(position);
                        if (checkQuantity && quantity.compareTo(BigDecimal.ZERO) < 1) {
                            break;
                        }
                    }
                }
            }
            if (quantity.compareTo(BigDecimal.ZERO) < 1) {
                ret.forEach(pos -> process.registerConditionMet(pos));
            } else {
                ret.clear();
            }
        }
        return ret;
    }

    private boolean canbeUsed(final ProcessData processData,
                              final List<IPosition> positions,
                              final IPosition currentPosition)
    {
        boolean ret = false;
        if (!processData.getPositionsUsedForSouce().contains(currentPosition)) {
            ret = true;
        } else {
            final var index = positions.indexOf(currentPosition);
            if (index == 0 && positions.size() > 1
                            && positions.get(1).getNetUnitPrice().compareTo(currentPosition.getNetUnitPrice()) > 0) {
                final var replacementOpt = positions.stream().filter(pos -> !pos.equals(currentPosition)
                                && getProducts().contains(pos.getProductOid())
                                && !pos.isBurned(processData)).findFirst();
                if (replacementOpt.isPresent()) {
                    processData.getPositionsUsedForSouce().remove(currentPosition);
                    processData.getPositionsUsedForSouce().add(replacementOpt.get());
                    ret = true;
                }
            }
        }
        return ret;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                        .appendSuper(super.toString())
                        .append("entryOperator", entryOperator)
                        .append("positionQuantity", positionQuantity)
                        .append("allowTargetSameAsSource", allowTargetSameAsSource)
                        .build();
    }
}
