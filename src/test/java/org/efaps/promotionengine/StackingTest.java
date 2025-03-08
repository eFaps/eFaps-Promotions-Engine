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
package org.efaps.promotionengine;

import java.math.BigDecimal;
import java.util.List;

import org.efaps.abacus.pojo.Configuration;
import org.efaps.abacus.pojo.Tax;
import org.efaps.promotionengine.condition.StackCondition;
import org.efaps.promotionengine.pojo.Document;
import org.efaps.promotionengine.pojo.Position;
import org.testng.Assert;
import org.testng.annotations.Test;

public class StackingTest
{

    @Test
    public void test10PercentAdditional()
    {
        final var prod1 = "111.333";

        // add a lot of them to test sorting
        final var promo1 = Promotions.products10PercentOff(prod1).withPriority(10).build();
        final var promo2 = Promotions.products10PercentOff("not").withPriority(20).build();
        final var promo3 = Promotions.products10PercentOff("not").withPriority(30).build();
        final var promo4 = Promotions.products10PercentOff("not").withPriority(40).build();
        final var additionalPromo1 = Promotions.products10PercentOff(prod1)
                        .withOid("APPLY")
                        .withPriority(50)
                        .addSourceCondition(new StackCondition())
                        .build();
        final var additionalPromo2 = Promotions.products10PercentOff("not")
                        .withPriority(20)
                        .addSourceCondition(new StackCondition())
                        .build();

        final var document = new Document()
                        .addPosition(new Position()
                                        .setIndex(1)
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(prod1)
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));

        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, List.of(promo1, promo2, promo3, promo4, additionalPromo1, additionalPromo2));

        Assert.assertTrue(new BigDecimal(81).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertNotNull(document.getPromotionInfo());
        Assert.assertEquals(2, document.getPromotionInfo().getPromotionOids().size());
        Assert.assertEquals(1, document.getPromotionInfo().getDetails().get(0).getPositionIndex());
        Assert.assertEquals(1, document.getPromotionInfo().getDetails().get(1).getPositionIndex());
        Assert.assertEquals(promo1.getOid(), document.getPromotionInfo().getDetails().get(0).getPromotionOid());
        Assert.assertEquals(additionalPromo1.getOid(), document.getPromotionInfo().getDetails().get(1).getPromotionOid());
    }
}
