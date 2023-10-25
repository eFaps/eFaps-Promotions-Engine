package org.efaps.promotionengine.action;

import java.math.BigDecimal;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PercentageDocDiscountActionTest
{
    @Test
    public void equalsTest() {
        final var obj1 = new PercentageDocDiscountAction();
        Assert.assertTrue(obj1.equals(obj1));
        Assert.assertFalse(obj1.equals("different"));

        Assert.assertTrue(obj1.equals(new PercentageDocDiscountAction()));

        obj1.setPercentage(new BigDecimal(10));
        final var obj2 = new PercentageDocDiscountAction().setPercentage(new BigDecimal(10));
        Assert.assertTrue(obj1.equals(obj2));
    }
}
