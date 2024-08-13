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
import java.util.ArrayList;

import org.efaps.abacus.pojo.Configuration;
import org.efaps.abacus.pojo.Tax;
import org.efaps.promotionengine.pojo.Document;
import org.efaps.promotionengine.pojo.Position;
import org.efaps.promotionengine.promotion.Promotion;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CombinationsTest
{

    @Test
    public void promotionsHirachyGrowingTicket()
    {
        final var calculator = new Calculator(new Configuration());
        final var promotion1 = Promotions.productsFiftyPercentOff();
        final var promotion2 = Promotions.buyOneGetOneFree();

        final var promotions = new ArrayList<Promotion>();
        promotions.add(promotion1.build());
        promotions.add(promotion2.build());

        // one position
        final var document = new Document()
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_50OFF1)
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));

        calculator.calc(document, promotions);

        Assert.assertTrue(new BigDecimal(50).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(50).compareTo(document.getPromotionInfo().getNetTotalDiscount()) == 0);

        // second position
        final var document2 = new Document()
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_50OFF1)
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("123.888")
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));

        calculator.calc(document2, promotions);

        Assert.assertTrue(new BigDecimal(50).compareTo(document2.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(100).compareTo(document2.getPositions().get(1).getNetPrice()) == 0);

        Assert.assertTrue(new BigDecimal(50).compareTo(document2.getPromotionInfo().getNetTotalDiscount()) == 0);
        Assert.assertTrue(new BigDecimal(50)
                        .compareTo(document2.getPromotionInfo().getDetails().get(0).getNetUnitDiscount()) == 0);
        Assert.assertNull(document2.getPromotionInfo().getDetails().get(1).getNetUnitDiscount());

        // third position
        final var document3 = new Document()
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_50OFF1)
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("123.888")
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_50OFF_BOGOF)
                                        .setNetUnitPrice(new BigDecimal(150))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));

        calculator.calc(document3, promotions);
        Assert.assertTrue(new BigDecimal(50).compareTo(document3.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(100).compareTo(document3.getPositions().get(1).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(75).compareTo(document3.getPositions().get(2).getNetPrice()) == 0);

        Assert.assertTrue(new BigDecimal(125).compareTo(document3.getPromotionInfo().getNetTotalDiscount()) == 0);
        Assert.assertTrue(new BigDecimal(50)
                        .compareTo(document3.getPromotionInfo().getDetails().get(0).getNetUnitDiscount()) == 0);
        Assert.assertNull(document3.getPromotionInfo().getDetails().get(1).getNetUnitDiscount());
        Assert.assertTrue(new BigDecimal(75)
                        .compareTo(document3.getPromotionInfo().getDetails().get(2).getNetUnitDiscount()) == 0);

        // fourth position
        final var document4 = new Document()
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_50OFF1)
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("123.888")
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_50OFF_BOGOF)
                                        .setNetUnitPrice(new BigDecimal(150))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("OTHER")
                                        .setNetUnitPrice(new BigDecimal(200))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));

        calculator.calc(document4, promotions);
        Assert.assertTrue(new BigDecimal(50).compareTo(document4.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(100).compareTo(document4.getPositions().get(1).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(75).compareTo(document4.getPositions().get(2).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(200).compareTo(document4.getPositions().get(3).getNetPrice()) == 0);

        Assert.assertTrue(new BigDecimal(125).compareTo(document4.getPromotionInfo().getNetTotalDiscount()) == 0);
        Assert.assertTrue(new BigDecimal(50)
                        .compareTo(document4.getPromotionInfo().getDetails().get(0).getNetUnitDiscount()) == 0);
        Assert.assertNull(document4.getPromotionInfo().getDetails().get(1).getNetUnitDiscount());
        Assert.assertTrue(new BigDecimal(75)
                        .compareTo(document3.getPromotionInfo().getDetails().get(2).getNetUnitDiscount()) == 0);
        Assert.assertNull(document4.getPromotionInfo().getDetails().get(3).getNetUnitDiscount());

        // five position
        final var document5 = new Document()
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_50OFF1)
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("123.888")
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_50OFF_BOGOF)
                                        .setNetUnitPrice(new BigDecimal(150))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("OTHER")
                                        .setNetUnitPrice(new BigDecimal(200))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_50OFF_BOGOF)
                                        .setNetUnitPrice(new BigDecimal(150))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));

        calculator.calc(document5, promotions);
        Assert.assertTrue(new BigDecimal(50).compareTo(document5.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(100).compareTo(document5.getPositions().get(1).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(75).compareTo(document5.getPositions().get(2).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(200).compareTo(document5.getPositions().get(3).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(75).compareTo(document5.getPositions().get(4).getNetPrice()) == 0);

        Assert.assertTrue(new BigDecimal(200).compareTo(document5.getPromotionInfo().getNetTotalDiscount()) == 0);
        Assert.assertTrue(new BigDecimal(50)
                        .compareTo(document5.getPromotionInfo().getDetails().get(0).getNetUnitDiscount()) == 0);
        Assert.assertNull(document5.getPromotionInfo().getDetails().get(1).getNetUnitDiscount());
        Assert.assertTrue(new BigDecimal(75)
                        .compareTo(document5.getPromotionInfo().getDetails().get(2).getNetUnitDiscount()) == 0);
        Assert.assertNull(document5.getPromotionInfo().getDetails().get(3).getNetUnitDiscount());
        Assert.assertTrue(new BigDecimal(75)
                        .compareTo(document5.getPromotionInfo().getDetails().get(4).getNetUnitDiscount()) == 0);

    }

}
