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
import org.efaps.promotionengine.pojo.Document;
import org.efaps.promotionengine.pojo.Position;
import org.efaps.promotionengine.promotion.Promotion;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MaxPromotionsTest
{

    @Test
    public void onePromotion()
    {
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
        calculator.calc(document, Collections.singletonList(getPromotion()));

        Assert.assertTrue(new BigDecimal(100).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(0).compareTo(document.getPositions().get(1).getNetPrice()) == 0);
    }

    @Test
    public void twoPromotions()
    {
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
                                        .setProductOid(Promotions.PROD_SO1)
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_SO2)
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));

        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, Collections.singletonList(getPromotion()));

        Assert.assertTrue(new BigDecimal(100).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(0).compareTo(document.getPositions().get(1).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(100).compareTo(document.getPositions().get(2).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(0).compareTo(document.getPositions().get(3).getNetPrice()) == 0);
    }

    @Test
    public void threePromotions()
    {
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
                                        .setProductOid(Promotions.PROD_SO1)
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_SO2)
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));

        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, Collections.singletonList(getPromotion()));

        Assert.assertTrue(new BigDecimal(100).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(0).compareTo(document.getPositions().get(1).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(100).compareTo(document.getPositions().get(2).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(0).compareTo(document.getPositions().get(3).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(100).compareTo(document.getPositions().get(4).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(0).compareTo(document.getPositions().get(5).getNetPrice()) == 0);
    }

    @Test
    public void maxPromotions()
    {
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
                                        .setProductOid(Promotions.PROD_SO1)
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_SO2)
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));

        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, Collections.singletonList(getPromotion()));

        Assert.assertTrue(new BigDecimal(100).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(0).compareTo(document.getPositions().get(1).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(100).compareTo(document.getPositions().get(2).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(0).compareTo(document.getPositions().get(3).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(100).compareTo(document.getPositions().get(4).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(0).compareTo(document.getPositions().get(5).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(100).compareTo(document.getPositions().get(6).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(100).compareTo(document.getPositions().get(7).getNetPrice()) == 0);
    }

    private Promotion getPromotion()
    {
        return Promotions.buyOneGetOneFree(Promotions.PROD_SO1, Promotions.PROD_SO2).withMax(3) .build();
    }
}
