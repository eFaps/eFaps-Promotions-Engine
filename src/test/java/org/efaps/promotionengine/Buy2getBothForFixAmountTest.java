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
import org.testng.Assert;
import org.testng.annotations.Test;

public class Buy2getBothForFixAmountTest
{

    @Test
    public void onlyOneProduct()
    {
        final var document = new Document()
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_SO1)
                                        .setNetUnitPrice(new BigDecimal(15))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));

        final var promotion = Promotions.getBuyTwoAndGetBothForFixAmount(Promotions.PROD_SO1).build();
        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, Collections.singletonList(promotion));

        Assert.assertTrue(new BigDecimal(15).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertNull(document.getPromotionInfo());
    }

    @Test
    public void meetExactly()
    {
        final var document = new Document()
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_SO1)
                                        .setNetUnitPrice(new BigDecimal(15))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_SO2)
                                        .setNetUnitPrice(new BigDecimal(20))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));

        final var promotion = Promotions.getBuyTwoAndGetBothForFixAmount(Promotions.PROD_SO1, Promotions.PROD_SO2)
                        .build();
        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, Collections.singletonList(promotion));

        Assert.assertTrue(new BigDecimal(10).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(10).compareTo(document.getPositions().get(1).getNetPrice()) == 0);

        Assert.assertTrue(new BigDecimal(5)
                        .compareTo(document.getPromotionInfo().getDetails().get(0).getNetDiscount()) == 0);
        Assert.assertTrue(new BigDecimal(10)
                        .compareTo(document.getPromotionInfo().getDetails().get(1).getNetDiscount()) == 0);
    }

    @Test
    public void meetButAdditionalProduct()
    {
        final var document = new Document()
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_SO1)
                                        .setNetUnitPrice(new BigDecimal(15))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_SO3)
                                        .setNetUnitPrice(new BigDecimal(20))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_SO2)
                                        .setNetUnitPrice(new BigDecimal(30))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));

        final var promotion = Promotions.getBuyTwoAndGetBothForFixAmount(Promotions.PROD_SO1, Promotions.PROD_SO2)
                        .build();
        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, Collections.singletonList(promotion));

        Assert.assertTrue(new BigDecimal(10).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(20).compareTo(document.getPositions().get(1).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(10).compareTo(document.getPositions().get(2).getNetPrice()) == 0);

        Assert.assertTrue(new BigDecimal(5)
                        .compareTo(document.getPromotionInfo().getDetails().get(0).getNetDiscount()) == 0);

        Assert.assertTrue(new BigDecimal(20)
                        .compareTo(document.getPromotionInfo().findDetailsForPosition(3).get(0).getNetDiscount()) == 0);
    }

    @Test
    public void meetThreeAndOneMore()
    {
        final var document = new Document()
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_SO1)
                                        .setNetUnitPrice(new BigDecimal(15))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_SO3)
                                        .setNetUnitPrice(new BigDecimal(30))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_SO2)
                                        .setNetUnitPrice(new BigDecimal(20))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_SO4)
                                        .setNetUnitPrice(new BigDecimal(40))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));

        final var promotion = Promotions
                        .getBuyTwoAndGetBothForFixAmount(Promotions.PROD_SO1, Promotions.PROD_SO2, Promotions.PROD_SO3)
                        .build();
        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, Collections.singletonList(promotion));

        Assert.assertTrue(new BigDecimal(10).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(30).compareTo(document.getPositions().get(1).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(10).compareTo(document.getPositions().get(2).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(40).compareTo(document.getPositions().get(3).getNetPrice()) == 0);

        Assert.assertTrue(new BigDecimal(5)
                        .compareTo(document.getPromotionInfo().getDetails().get(0).getNetDiscount()) == 0);
        Assert.assertTrue(new BigDecimal(10)
                        .compareTo(document.getPromotionInfo().findDetailsForPosition(3).get(0).getNetDiscount()) == 0);
    }

}
