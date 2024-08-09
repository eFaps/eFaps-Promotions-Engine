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
import java.util.Collections;

import org.efaps.abacus.pojo.Configuration;
import org.efaps.abacus.pojo.Tax;
import org.efaps.promotionengine.action.Strategy;
import org.efaps.promotionengine.pojo.Document;
import org.efaps.promotionengine.pojo.Position;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Second25PercentOffTest
{

    @Test
    public void cheapestIsFirstElement()
    {
        final var document = new Document()
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_SO1)
                                        .setNetUnitPrice(new BigDecimal(150))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_SO1)
                                        .setNetUnitPrice(new BigDecimal(250))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_SO1)
                                        .setNetUnitPrice(new BigDecimal(200))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));
        final var promotion = Promotions.second25PercentOff(Strategy.CHEAPEST, Promotions.PROD_SO1).build();

        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, Collections.singletonList(promotion));

        Assert.assertTrue(new BigDecimal(112.5).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(250).compareTo(document.getPositions().get(1).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(200).compareTo(document.getPositions().get(2).getNetPrice()) == 0);

        Assert.assertTrue(new BigDecimal("37.5")
                        .compareTo(document.getPromotionInfo().getDetails().get(0).getNetDiscount()) == 0);
        Assert.assertNull(document.getPromotionInfo().getDetails().get(1).getPromotionOid());
        Assert.assertTrue(new BigDecimal(0)
                        .compareTo(document.getPromotionInfo().getDetails().get(2).getNetDiscount()) == 0);

    }

    @Test
    public void cheapest()
    {
        final var document = new Document()
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_SO1)
                                        .setNetUnitPrice(new BigDecimal(300))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_SO1)
                                        .setNetUnitPrice(new BigDecimal(250))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_SO1)
                                        .setNetUnitPrice(new BigDecimal(200))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));
        final var promotion = Promotions.second25PercentOff(Strategy.CHEAPEST, Promotions.PROD_SO1).build();

        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, Collections.singletonList(promotion));

        Assert.assertTrue(new BigDecimal(300).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(250).compareTo(document.getPositions().get(1).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(150).compareTo(document.getPositions().get(2).getNetPrice()) == 0);

        Assert.assertTrue(new BigDecimal(0)
                        .compareTo(document.getPromotionInfo().getDetails().get(0).getNetDiscount()) == 0);
        Assert.assertNull(document.getPromotionInfo().getDetails().get(1).getPromotionOid());
        Assert.assertTrue(new BigDecimal(50)
                        .compareTo(document.getPromotionInfo().getDetails().get(2).getNetDiscount()) == 0);
    }

    @Test
    public void priciest()
    {
        final var document = new Document()
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_SO1)
                                        .setNetUnitPrice(new BigDecimal(150))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_SO1)
                                        .setNetUnitPrice(new BigDecimal(250))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_SO1)
                                        .setNetUnitPrice(new BigDecimal(200))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));
        final var promotion = Promotions.second25PercentOff(Strategy.PRICIEST, Promotions.PROD_SO1).build();

        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, Collections.singletonList(promotion));

        Assert.assertTrue(new BigDecimal(150).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(187.5).compareTo(document.getPositions().get(1).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(200).compareTo(document.getPositions().get(2).getNetPrice()) == 0);

        Assert.assertNotNull(document.getPromotionInfo().getDetails().get(0).getPromotionOid());
        Assert.assertTrue(new BigDecimal(62.5)
                        .compareTo(document.getPromotionInfo().getDetails().get(1).getNetDiscount()) == 0);
        Assert.assertNull(document.getPromotionInfo().getDetails().get(2).getPromotionOid());
    }

    @Test
    public void secondForOneCheapest()
    {
        final var document = new Document()
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_SO1)
                                        .setNetUnitPrice(new BigDecimal(150))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_SO2)
                                        .setNetUnitPrice(new BigDecimal(250))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_BOGOF3)
                                        .setNetUnitPrice(new BigDecimal(200))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));
        final var promotion = Promotions.secondForOne(Strategy.CHEAPEST).build();

        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, Collections.singletonList(promotion));

        Assert.assertTrue(new BigDecimal(1).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(250).compareTo(document.getPositions().get(1).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(200).compareTo(document.getPositions().get(2).getNetPrice()) == 0);

        Assert.assertNotNull(document.getPromotionInfo().getDetails().get(0).getPromotionOid());
        Assert.assertTrue(new BigDecimal(149)
                        .compareTo(document.getPromotionInfo().getDetails().get(0).getNetDiscount()) == 0);
        Assert.assertNull(document.getPromotionInfo().getDetails().get(2).getPromotionOid());
    }

    @Test
    public void secondForOnePriciest()
    {
        final var document = new Document()
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_SO1)
                                        .setNetUnitPrice(new BigDecimal(150))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_SO2)
                                        .setNetUnitPrice(new BigDecimal(250))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_BOGOF3)
                                        .setNetUnitPrice(new BigDecimal(200))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));
        final var promotion = Promotions.secondForOne(Strategy.PRICIEST).build();

        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, Collections.singletonList(promotion));

        Assert.assertTrue(new BigDecimal(150).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(1).compareTo(document.getPositions().get(1).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(200).compareTo(document.getPositions().get(2).getNetPrice()) == 0);

        Assert.assertNotNull(document.getPromotionInfo().getDetails().get(0).getPromotionOid());
        Assert.assertTrue(new BigDecimal(249)
                        .compareTo(document.getPromotionInfo().getDetails().get(1).getNetDiscount()) == 0);
        Assert.assertNotNull(document.getPromotionInfo().getDetails().get(1).getPromotionOid());
        Assert.assertNull(document.getPromotionInfo().getDetails().get(2).getPromotionOid());
    }

}
