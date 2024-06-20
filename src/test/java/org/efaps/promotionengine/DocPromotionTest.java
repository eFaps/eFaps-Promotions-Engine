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

public class DocPromotionTest
{

    @Test
    public void buyMoreThan100AndGet10PercentOff()
    {
        final var promotion = Promotions.buyMoreThan100AndGet10PercentOff().build();

        final var document = new Document()
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("123.4")
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("223.456")
                                        .setNetUnitPrice(new BigDecimal(150))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));

        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, Collections.singletonList(promotion));

        Assert.assertTrue(new BigDecimal(232.5).compareTo(document.getCrossTotal()) == 0);

        Assert.assertTrue(new BigDecimal(62.5).compareTo(document.getPromotionInfo().getCrossTotalDiscount()) == 0);
        Assert.assertNull(document.getPromotionInfo().getDetails().get(0).getPromotionOid());
        Assert.assertTrue(document.getPromotionInfo().getPromotionOids().contains(promotion.getOid()));
    }

    @Test
    public void buyMoreThan100AndGet20Off()
    {

        final var promotion = Promotions.buyMoreThan100AndGet20Off().build();

        final var document = new Document()
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("123.4")
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("223.456")
                                        .setNetUnitPrice(new BigDecimal(150))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));

        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, Collections.singletonList(promotion));

        Assert.assertTrue(new BigDecimal(275).compareTo(document.getCrossTotal()) == 0);

        Assert.assertTrue(new BigDecimal(20).compareTo(document.getPromotionInfo().getCrossTotalDiscount()) == 0);
        Assert.assertNull(document.getPromotionInfo().getDetails().get(0).getPromotionOid());
        Assert.assertTrue(document.getPromotionInfo().getPromotionOids().contains(promotion.getOid()));
    }

}
