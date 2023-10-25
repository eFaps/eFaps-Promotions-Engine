package org.efaps.promotionengine.condition;

import java.math.BigDecimal;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ProductsConditionTest
{

    @Test
    public void equalsTest()
    {
        final var obj1 = new ProductsCondition();
        Assert.assertTrue(obj1.equals(obj1));
        Assert.assertFalse(obj1.equals("different"));

        Assert.assertTrue(obj1.equals(new ProductsCondition()));

        obj1.setEntryOperator(EntryOperator.INCLUDES_ALL)
                        .setPositionQuantity(BigDecimal.ONE);
        final var obj2 = new ProductsCondition().setEntryOperator(EntryOperator.INCLUDES_ALL)
                        .setPositionQuantity(BigDecimal.ONE);
        Assert.assertTrue(obj1.equals(obj2));
    }
}
