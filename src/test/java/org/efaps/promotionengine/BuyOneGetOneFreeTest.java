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
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;

import org.efaps.abacus.pojo.Configuration;
import org.efaps.abacus.pojo.Tax;
import org.efaps.promotionengine.action.IAction;
import org.efaps.promotionengine.action.PercentageDiscountAction;
import org.efaps.promotionengine.condition.EntryOperator;
import org.efaps.promotionengine.condition.ICondition;
import org.efaps.promotionengine.condition.ProductsCondition;
import org.efaps.promotionengine.pojo.Document;
import org.efaps.promotionengine.pojo.Position;
import org.efaps.promotionengine.promotion.Promotion;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BuyOneGetOneFreeTest
{

    @Test
    public void onlyOneProduct()
    {
        final var promotion = Promotions.buyOneGetOneFree(Promotions.PROD_SO1, Promotions.PROD_SO2).build();

        final var document = new Document()
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_SO1)
                                        .setNetUnitPrice(new BigDecimal(122))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));

        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, Collections.singletonList(promotion));

        Assert.assertTrue(new BigDecimal(122).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertNull(document.getPromotionInfo());
    }


    @Test
    public void meetExactlyButDifferentProducts()
    {
        final var promotion = Promotions.buyOneGetOneFree(Promotions.PROD_SO1, Promotions.PROD_SO2).build();

        final var document = new Document()
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_SO1)
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_SO2)
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));

        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, Collections.singletonList(promotion));

        Assert.assertTrue(new BigDecimal(100).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(0).compareTo(document.getPositions().get(1).getNetPrice()) == 0);

        Assert.assertTrue(new BigDecimal(100)
                        .compareTo(document.getPromotionInfo().findDetailsForPosition(2).get(0).getNetDiscount()) == 0);
    }

    @Test
    public void meetExactlySameProducts()
    {
        final var promotion = Promotions.buyOneGetOneFree(Promotions.PROD_SO1, Promotions.PROD_SO2).build();

        final var document = new Document()
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_SO1)
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_SO1)
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));

        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, Collections.singletonList(promotion));

        Assert.assertTrue(new BigDecimal(100).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(0).compareTo(document.getPositions().get(1).getNetPrice()) == 0);

        Assert.assertTrue(new BigDecimal(100)
                        .compareTo(document.getPromotionInfo().findDetailsForPosition(2).get(0).getNetDiscount()) == 0);
    }


    @Test
    public void additionalUnrelatedProduct()
    {
        final var promotion = Promotions.buyOneGetOneFree(Promotions.PROD_SO1, Promotions.PROD_SO2).build();

        final var document = new Document()
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_SO1)
                                        .setNetUnitPrice(new BigDecimal(122))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_SO2)
                                        .setNetUnitPrice(new BigDecimal(122))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("123.456")
                                        .setNetUnitPrice(new BigDecimal(150))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));

        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, Collections.singletonList(promotion));

        Assert.assertTrue(new BigDecimal(122).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(0).compareTo(document.getPositions().get(1).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(150).compareTo(document.getPositions().get(2).getNetPrice()) == 0);

        Assert.assertTrue(new BigDecimal(122)
                        .compareTo(document.getPromotionInfo().findDetailsForPosition(2).get(0).getNetDiscount()) == 0);
    }

    @Test
    public void buyOneGetOneFreeMultipleTimes()
    {
        final var promotion = Promotions.buyOneGetOneFree(Promotions.PROD_SO1, Promotions.PROD_SO2).build();

        final var document = new Document()
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_SO1)
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_SO2)
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("123.456")
                                        .setNetUnitPrice(new BigDecimal(150))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_SO1)
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_SO2)
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));

        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, Collections.singletonList(promotion));

        Assert.assertTrue(new BigDecimal(100).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(0).compareTo(document.getPositions().get(1).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(150).compareTo(document.getPositions().get(2).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(100).compareTo(document.getPositions().get(3).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(0).compareTo(document.getPositions().get(4).getNetPrice()) == 0);

        Assert.assertTrue(new BigDecimal(100)
                        .compareTo(document.getPromotionInfo().findDetailsForPosition(2).get(0).getNetDiscount()) == 0);
        Assert.assertTrue(new BigDecimal(100)
                        .compareTo(document.getPromotionInfo().findDetailsForPosition(5).get(0).getNetDiscount()) == 0);
    }

    @Test
    public void buyOneGetOneFreeNotMet()
    {
        final var sourceConditions = new ArrayList<ICondition>();
        sourceConditions.add(new ProductsCondition()
                        .setPositionQuantity(BigDecimal.ONE)
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .addProduct("123.456"));

        final var targetConditions = new ArrayList<ICondition>();
        targetConditions.add(new ProductsCondition()
                        .setPositionQuantity(BigDecimal.ONE)
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .addProduct("223.456"));

        final var actions = new ArrayList<IAction>();
        final var action = new PercentageDiscountAction().setPercentage(new BigDecimal(100));
        actions.add(action);

        final var promotion = Promotion.builder()
                        .withOid("123.456")
                        .withName("Buy one get one free")
                        .withDescription("This can be a long text")
                        .withPriority(1)
                        .withStartDateTime(OffsetDateTime.now().minusDays(5))
                        .withEndDateTime(OffsetDateTime.now().plusDays(5))
                        .withSourceConditions(sourceConditions)
                        .withTargetConditions(targetConditions)
                        .withActions(actions)
                        .build();

        final var document = new Document()
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("123.456")
                                        .setNetUnitPrice(new BigDecimal(122))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("123.456")
                                        .setNetUnitPrice(new BigDecimal(122))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));

        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, Collections.singletonList(promotion));

        Assert.assertTrue(new BigDecimal(122).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(122).compareTo(document.getPositions().get(1).getNetPrice()) == 0);
        Assert.assertNull(document.getDocDiscount());
    }

}
