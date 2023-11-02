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
package org.efaps.promotionengine.condition;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.efaps.promotionengine.api.IPosition;
import org.efaps.promotionengine.process.ProcessData;
import org.efaps.promotionengine.process.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductsCondition
    extends AbstractCondition
{
    private static final Logger LOG = LoggerFactory.getLogger(ProductsCondition.class);

    private EntryOperator entryOperator;

    private BigDecimal positionQuantity;


    private List<String> products;

    public List<String> getProducts()
    {
        return products;
    }

    public ProductsCondition setEntries(final List<String> products)
    {
        this.products = products;
        return this;
    }

    public ProductsCondition addProduct(final String product)
    {
        if (this.products == null) {
            this.products = new ArrayList<>();
        }
        this.products.add(product);
        return this;
    }

    public EntryOperator getEntryOperator()
    {
        return entryOperator;
    }

    public ProductsCondition setEntryOperator(final EntryOperator entryOperator)
    {
        this.entryOperator = entryOperator;
        return this;
    }

    public BigDecimal getPositionQuantity()
    {
        return positionQuantity;
    }


    public ProductsCondition setPositionQuantity(final BigDecimal quantity)
    {
        this.positionQuantity = quantity;
        return this;
    }


    @Override
    public boolean isMet(final ProcessData process)
    {
        LOG.info("Checking if condition is met for Document: {}", process.getDocument());
        var ret = false;
        switch (getEntryOperator()) {
            case INCLUDES_ANY:
                ret = includesAny(process).size() > 0;
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + getEntryOperator());
        }
        LOG.info("Condition met: {}", ret);
        return ret;
    }

    private List<IPosition> includesAny(final ProcessData process)
    {
        final var ret = new ArrayList<IPosition>();
        for (final var product : products) {
            var quantity = positionQuantity;
            for (final var calcPosition: process.getDocument().getPositions()) {
                final var position = (IPosition) calcPosition;
                if (position.getProductOid().equals(product) && !process.getPositionsUsedForSouce().contains(position)) {
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

    @Override
    public List<IPosition> evalPositions(final ProcessData process)
    {
        List<IPosition> ret = new ArrayList<>();
        switch (getEntryOperator()) {
            case INCLUDES_ANY:
                ret = includesAny(process);
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + getEntryOperator());
        }
        return ret;
    }

    @Override
    public boolean positionMet(final IPosition position)
    {
        return products.contains(position.getProductOid());
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof final ProductsCondition condition)) {
            return false;
        }
        return Objects.equals(this.entryOperator, condition.entryOperator)
                        && Objects.equals(this.positionQuantity, condition.positionQuantity)
                        && Objects.equals(this.products, condition.products);
    }

}
