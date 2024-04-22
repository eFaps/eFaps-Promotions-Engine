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

import org.efaps.abacus.pojo.Configuration;
import org.efaps.abacus.pojo.Tax;
import org.efaps.promotionengine.action.PercentageDiscountAction;
import org.efaps.promotionengine.condition.EntryOperator;
import org.efaps.promotionengine.condition.ProductsCondition;
import org.efaps.promotionengine.pojo.Document;
import org.efaps.promotionengine.pojo.Position;
import org.efaps.promotionengine.process.EngineRule;
import org.efaps.promotionengine.promotion.Promotion;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PriorityTest
{

    final Promotion promo1 = Promotion.builder()
                    .withOid("222.1")
                    .withName("Both products are 10% off")
                    .withPriority(1)
                    .withStartDateTime(OffsetDateTime.now().minusDays(5))
                    .withEndDateTime(OffsetDateTime.now().plusDays(5))
                    .addTargetCondition(new ProductsCondition()
                                    .setPositionQuantity(BigDecimal.ONE)
                                    .setEntryOperator(EntryOperator.INCLUDES_ANY)
                                    .addProduct("PROD1")
                                    .addProduct("PROD2"))
                    .addAction(new PercentageDiscountAction()
                                    .setPercentage(new BigDecimal(10)))
                    .build();

    final Promotion promo2 = Promotion.builder()
                    .withOid("222.2")
                    .withName("PROD1 is 20% off")
                    .withPriority(2)
                    .withStartDateTime(OffsetDateTime.now().minusDays(5))
                    .withEndDateTime(OffsetDateTime.now().plusDays(5))
                    .addTargetCondition(new ProductsCondition()
                                    .setPositionQuantity(BigDecimal.ONE)
                                    .setEntryOperator(EntryOperator.INCLUDES_ANY)
                                    .addProduct("PROD1"))
                    .addAction(new PercentageDiscountAction().setPercentage(new BigDecimal(20)))
                    .build();

    final Promotion promo3 = Promotion.builder()
                    .withOid("222.3")
                    .withName("Does not apply to any produt")
                    .withPriority(4)
                    .withStartDateTime(OffsetDateTime.now().minusDays(5))
                    .withEndDateTime(OffsetDateTime.now().plusDays(5))
                    .addTargetCondition(new ProductsCondition()
                                    .setPositionQuantity(BigDecimal.ONE)
                                    .setEntryOperator(EntryOperator.INCLUDES_ANY)
                                    .addProduct("SOMEOTHERPRODUCT"))
                    .addAction(new PercentageDiscountAction().setPercentage(new BigDecimal(100)))
                    .build();

    final Promotion promo4 = Promotion.builder()
                    .withOid("222.4")
                    .withName("PROD2 is 30% off")
                    .withPriority(3)
                    .withStartDateTime(OffsetDateTime.now().minusDays(5))
                    .withEndDateTime(OffsetDateTime.now().plusDays(5))
                    .addTargetCondition(new ProductsCondition()
                                    .setPositionQuantity(BigDecimal.ONE)
                                    .setEntryOperator(EntryOperator.INCLUDES_ANY)
                                    .addProduct("PROD2"))
                    .addAction(new PercentageDiscountAction().setPercentage(new BigDecimal(20)))
                    .build();

    final Promotion promo5 = Promotion.builder()
                    .withOid("222.4")
                    .withName("Both products are 50% off")
                    .withPriority(1)
                    .withStartDateTime(OffsetDateTime.now().minusDays(5))
                    .withEndDateTime(OffsetDateTime.now().plusDays(5))
                    .addTargetCondition(new ProductsCondition()
                                    .setPositionQuantity(BigDecimal.ONE)
                                    .setEntryOperator(EntryOperator.INCLUDES_ANY)
                                    .addProduct("PROD1")
                                    .addProduct("PROD2"))
                    .addAction(new PercentageDiscountAction().setPercentage(new BigDecimal(50)))
                    .build();


    private Document getDocument()
    {
        return new Document()
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("PROD1")
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("PROD2")
                                        .setNetUnitPrice(new BigDecimal(200))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));
    }

    @Test
    public void testBothProductsAre10Off()
    {
        final var document = getDocument();
        final var promotions = new ArrayList<Promotion>();
        promotions.add(promo1);
        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, promotions);
        Assert.assertTrue(new BigDecimal(90).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(180).compareTo(document.getPositions().get(1).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(270).compareTo(document.getNetTotal()) == 0);
    }

    @Test
    public void testProduct1Is20ff()
    {
        final var document = getDocument();
        final var promotions = new ArrayList<Promotion>();
        promotions.add(promo2);
        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, promotions);
        Assert.assertTrue(new BigDecimal(80).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(200).compareTo(document.getPositions().get(1).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(280).compareTo(document.getNetTotal()) == 0);
    }

    @Test
    public void testNoneApply()
    {
        final var document = getDocument();
        final var promotions = new ArrayList<Promotion>();
        promotions.add(promo3);
        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, promotions);
        Assert.assertTrue(new BigDecimal(100).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(200).compareTo(document.getPositions().get(1).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(300).compareTo(document.getNetTotal()) == 0);
    }

    @Test
    public void testProduct2Is30ff()
    {
        final var document = getDocument();
        final var promotions = new ArrayList<Promotion>();
        promotions.add(promo4);
        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, promotions);
        Assert.assertTrue(new BigDecimal(100).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(160).compareTo(document.getPositions().get(1).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(260).compareTo(document.getNetTotal()) == 0);
    }

    @Test
    public void testBothProductsAre50Off()
    {
        final var document = getDocument();
        final var promotions = new ArrayList<Promotion>();
        promotions.add(promo5);
        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, promotions);
        Assert.assertTrue(new BigDecimal(50).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(100).compareTo(document.getPositions().get(1).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(150).compareTo(document.getNetTotal()) == 0);
    }


    @Test
    public void testApplyByPriority()
    {
        final var document = getDocument();
        final var promotions = new ArrayList<Promotion>();
        promotions.add(promo1);
        promotions.add(promo2);
        promotions.add(promo3);
        promotions.add(promo4);
        promotions.add(promo5);
        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, promotions);

        Assert.assertTrue(new BigDecimal(80).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(160).compareTo(document.getPositions().get(1).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(240).compareTo(document.getNetTotal()) == 0);
    }

    @Test
    public void bestForClient()
    {
        final var document = getDocument();
        final var promotions = new ArrayList<Promotion>();
        promotions.add(promo1);
        promotions.add(promo2);
        promotions.add(promo3);
        promotions.add(promo4);
        promotions.add(promo5);
        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, promotions, null,
                        new PromotionsConfiguration().setEngineRule(EngineRule.MOSTDISCOUNT));

        Assert.assertTrue(new BigDecimal(50).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(100).compareTo(document.getPositions().get(1).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(150).compareTo(document.getNetTotal()) == 0);
    }

}
