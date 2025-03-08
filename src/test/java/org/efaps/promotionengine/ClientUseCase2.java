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
import org.efaps.promotionengine.action.Strategy;
import org.efaps.promotionengine.condition.EntryOperator;
import org.efaps.promotionengine.condition.ProductsCondition;
import org.efaps.promotionengine.pojo.Document;
import org.efaps.promotionengine.pojo.Position;
import org.efaps.promotionengine.process.EngineRule;
import org.efaps.promotionengine.promotion.Promotion;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ClientUseCase2
{

    final Promotion promo1 = Promotion.builder()
                    .withOid("222.2")
                    .withName("Products of a Category that are also of a label (eql queries)")
                    .withPriority(2)
                    .withStartDateTime(OffsetDateTime.now().minusDays(5))
                    .withEndDateTime(OffsetDateTime.now().plusDays(5))
                    .addTargetCondition(new ProductsCondition()
                                    .setPositionQuantity(BigDecimal.ZERO)
                                    .setEntryOperator(EntryOperator.INCLUDES_ANY)
                                    .addProduct("7004.199101")
                                    .addProduct("7004.199102")
                                    .addProduct("7004.199103")
                                    .addProduct("7004.199104")
                                    .addProduct("7004.199105"))
                    .addTargetCondition(new ProductsCondition()
                                    .setPositionQuantity(BigDecimal.ZERO)
                                    .setEntryOperator(EntryOperator.INCLUDES_ANY)
                                    .addProduct("7004.199101")
                                    .addProduct("7004.199102"))
                    .addAction(new PercentageDiscountAction().setPercentage(new BigDecimal(25))
                                    .setStrategy(Strategy.INDEX))
                    .build();

    final Promotion promo2 = Promotion.builder()
                    .withOid("222.3")
                    .withName("Products of the same Category but of different Label (eql queries)")
                    .withPriority(2)
                    .withStartDateTime(OffsetDateTime.now().minusDays(5))
                    .withEndDateTime(OffsetDateTime.now().plusDays(5))
                    .addTargetCondition(new ProductsCondition()
                                    .setPositionQuantity(BigDecimal.ZERO)
                                    .setEntryOperator(EntryOperator.INCLUDES_ANY)
                                    .addProduct("7004.199101")
                                    .addProduct("7004.199102")
                                    .addProduct("7004.199103")
                                    .addProduct("7004.199104")
                                    .addProduct("7004.199105"))
                    .addTargetCondition(new ProductsCondition()
                                    .setPositionQuantity(BigDecimal.ZERO)
                                    .setEntryOperator(EntryOperator.INCLUDES_ANY)
                                    .addProduct("7004.199103")
                                    .addProduct("7004.199104"))
                    .addAction(new PercentageDiscountAction().setPercentage(new BigDecimal(15))
                                    .setStrategy(Strategy.INDEX))
                    .build();

    private Document getDocument1()
    {
        return new Document()
                        .addPosition(new Position()
                                        .setIndex(1)
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("7004.199102")
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setIndex(1)
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("7004.199104")
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));
    }

    private Document getDocument2()
    {
        return new Document()
                        .addPosition(new Position()
                                        .setIndex(1)
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("7004.199104")
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setIndex(1)
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("7004.199102")
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));
    }

    @Test
    public void doc1()
    {
        final var document = getDocument1();
        final var promotions = new ArrayList<Promotion>();
        promotions.add(promo1);
        promotions.add(promo2);

        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, promotions, null,
                        new PromotionsConfiguration().setEngineRule(EngineRule.MOSTDISCOUNT));

        Assert.assertTrue(new BigDecimal(75).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(85).compareTo(document.getPositions().get(1).getNetPrice()) == 0);
    }


    @Test
    public void doc2()
    {
        final var document = getDocument2();
        final var promotions = new ArrayList<Promotion>();
        promotions.add(promo1);
        promotions.add(promo2);

        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, promotions, null,
                        new PromotionsConfiguration().setEngineRule(EngineRule.MOSTDISCOUNT));

        Assert.assertTrue(new BigDecimal(85).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(75).compareTo(document.getPositions().get(1).getNetPrice()) == 0);
    }
}
