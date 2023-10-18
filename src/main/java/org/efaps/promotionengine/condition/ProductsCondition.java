package org.efaps.promotionengine.condition;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.efaps.promotionengine.ProcessData;
import org.efaps.promotionengine.pojo.Position;
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

    private List<Position> includesAny(final ProcessData process)
    {
        final var ret = new ArrayList<Position>();
        for (final var product : products) {
            var quantity = positionQuantity;
            for (final var position: process.getDocument().getPositions()) {
                if (position.getProductOid().equals(product) && !process.getPositionsUsedForSouce().contains(position)) {
                    quantity = quantity.subtract(position.getQuantity());
                    LOG.info("Found product with oid: {} and quantity: {}", position.getProductOid(), quantity);
                    process.registerConditionMet(position);
                    ret.add(position);
                    if (quantity.compareTo(BigDecimal.ZERO) < 1) {
                        break;
                    }
                }
            }
            if (quantity.compareTo(BigDecimal.ZERO) < 1) {
                break;
            }
        }
        return ret;
    }

    @Override
    public List<Position> evalPositions(final ProcessData process)
    {
        List<Position> ret = new ArrayList<>();
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
    public boolean positionMet(final Position position)
    {
        return products.contains(position.getProductOid());
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
