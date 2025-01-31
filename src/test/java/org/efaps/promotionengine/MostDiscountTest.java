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
import org.efaps.promotionengine.action.PercentageDiscountAction;
import org.efaps.promotionengine.condition.EntryOperator;
import org.efaps.promotionengine.condition.ProductsCondition;
import org.efaps.promotionengine.pojo.Document;
import org.efaps.promotionengine.pojo.Position;
import org.efaps.promotionengine.process.EngineRule;
import org.efaps.promotionengine.promotion.Promotion;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MostDiscountTest
{

    final Promotion.Builder promoBuilder = Promotion.builder()
                    .withOid("222.1")
                    .withName("PROD1 to 4 are 10% off")
                    .withPriority(1)
                    .withStartDateTime(OffsetDateTime.now().minusDays(5))
                    .withEndDateTime(OffsetDateTime.now().plusDays(5))
                    .addTargetCondition(new ProductsCondition()
                                    .setPositionQuantity(BigDecimal.ONE)
                                    .setEntryOperator(EntryOperator.INCLUDES_ANY)
                                    .addProduct("PROD1")
                                    .addProduct("PROD2")
                                    .addProduct("PROD3")
                                    .addProduct("PROD4"))
                    .addAction(new PercentageDiscountAction()
                                    .setPercentage(new BigDecimal(10)));

    final Promotion promo2 = Promotion.builder()
                    .withOid("222.2")
                    .withName("PROD2 is 20% off")
                    .withPriority(2)
                    .withStartDateTime(OffsetDateTime.now().minusDays(5))
                    .withEndDateTime(OffsetDateTime.now().plusDays(5))
                    .addTargetCondition(new ProductsCondition()
                                    .setPositionQuantity(BigDecimal.ONE)
                                    .setEntryOperator(EntryOperator.INCLUDES_ANY)
                                    .addProduct("PROD2"))
                    .addAction(new PercentageDiscountAction().setPercentage(new BigDecimal(20)))
                    .build();

    final Promotion promo3 = Promotion.builder()
                    .withOid("222.3")
                    .withName("Some other product has a discount")
                    .withPriority(2)
                    .withStartDateTime(OffsetDateTime.now().minusDays(5))
                    .withEndDateTime(OffsetDateTime.now().plusDays(5))
                    .addSourceCondition(new ProductsCondition()
                                    .setPositionQuantity(BigDecimal.ONE)
                                    .setEntryOperator(EntryOperator.INCLUDES_ANY)
                                    .addProduct("PROD1"))
                    .addTargetCondition(new ProductsCondition()
                                    .setPositionQuantity(BigDecimal.ONE)
                                    .setEntryOperator(EntryOperator.INCLUDES_ANY)
                                    .addProduct("NOT in the reciept"))
                    .addAction(new PercentageDiscountAction().setPercentage(new BigDecimal(20)))
                    .build();

    private Document getDocument()
    {
        return new Document()
                        .addPosition(new Position()
                                        .setIndex(1)
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("PROD1")
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setIndex(2)
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("PROD2")
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setIndex(3)
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("PROD3")
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setIndex(4)
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("PROD4")
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setIndex(5)
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("PROD5")
                                        .setNetUnitPrice(new BigDecimal(200))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));
    }

    @Test
    public void bestSortMechanism()
    {
        final var document = getDocument();
        final var promotions = new ArrayList<Promotion>();
        promotions.add(promoBuilder.build());
        promotions.add(promoBuilder.withOid("222.1-a")
                        .withActions(Collections.singletonList(new PercentageDiscountAction()
                                        .setPercentage(new BigDecimal(9))))
                        .build());
        promotions.add(promo2);
        promotions.add(promo3);
        promotions.add(promoBuilder.withOid("222.1-b")
                        .withActions(Collections.singletonList(new PercentageDiscountAction()
                                        .setPercentage(new BigDecimal(8))))
                        .build());

        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, promotions, null,
                        new PromotionsConfiguration().setEngineRule(EngineRule.MOSTDISCOUNT));

        Assert.assertTrue(new BigDecimal(90).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(80).compareTo(document.getPositions().get(1).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(90).compareTo(document.getPositions().get(2).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(90).compareTo(document.getPositions().get(3).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(200).compareTo(document.getPositions().get(4).getNetPrice()) == 0);

        Assert.assertTrue(new BigDecimal(550).compareTo(document.getNetTotal()) == 0);
    }
}
