/*
 * Copyright 2003 - 2023 The eFaps Team
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
 *
 */
package org.efaps.promotionengine.rule;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.efaps.promotionengine.pojo.Document;
import org.efaps.promotionengine.pojo.Position;
import org.efaps.promotionengine.process.Engine;
import org.efaps.promotionengine.promotion.Promotion;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CombinationsTest
{
    @Test
    public void promotionsHirachyGrowingTicket()
    {
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
                                        .setCrossTotal(new BigDecimal(100)));

        final var engine = new Engine();
        engine.apply(document, promotions);
        Assert.assertTrue(new BigDecimal(50).compareTo(document.getPositions().get(0).getCrossTotal()) == 0);

        // second position
        final var document2 = new Document()
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_50OFF1)
                                        .setCrossTotal(new BigDecimal(100)))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("123.888")
                                        .setCrossTotal(new BigDecimal(100)));

        engine.apply(document2, promotions);
        Assert.assertTrue(new BigDecimal(50).compareTo(document2.getPositions().get(0).getCrossTotal()) == 0);
        Assert.assertTrue(new BigDecimal(100).compareTo(document2.getPositions().get(1).getCrossTotal()) == 0);


        // third position
        final var document3 = new Document()
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_50OFF1)
                                        .setCrossTotal(new BigDecimal(100)))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("123.888")
                                        .setCrossTotal(new BigDecimal(100)))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_50OFF_BOGOF)
                                        .setCrossTotal(new BigDecimal(150)));

        engine.apply(document3, promotions);
        Assert.assertTrue(new BigDecimal(50).compareTo(document3.getPositions().get(0).getCrossTotal()) == 0);
        Assert.assertTrue(new BigDecimal(100).compareTo(document3.getPositions().get(1).getCrossTotal()) == 0);
        Assert.assertTrue(new BigDecimal(75).compareTo(document3.getPositions().get(2).getCrossTotal()) == 0);

        // fourth position
        final var document4 = new Document()
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_50OFF1)
                                        .setCrossTotal(new BigDecimal(100)))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("123.888")
                                        .setCrossTotal(new BigDecimal(100)))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_50OFF_BOGOF)
                                        .setCrossTotal(new BigDecimal(150)))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("OTHER")
                                        .setCrossTotal(new BigDecimal(200)));

        engine.apply(document4, promotions);
        Assert.assertTrue(new BigDecimal(50).compareTo(document4.getPositions().get(0).getCrossTotal()) == 0);
        Assert.assertTrue(new BigDecimal(100).compareTo(document4.getPositions().get(1).getCrossTotal()) == 0);
        Assert.assertTrue(new BigDecimal(75).compareTo(document4.getPositions().get(2).getCrossTotal()) == 0);
        Assert.assertTrue(new BigDecimal(200).compareTo(document4.getPositions().get(3).getCrossTotal()) == 0);


        // five position
        final var document5 = new Document()
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_50OFF1)
                                        .setCrossTotal(new BigDecimal(100)))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("123.888")
                                        .setCrossTotal(new BigDecimal(100)))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_50OFF_BOGOF)
                                        .setCrossTotal(new BigDecimal(150)))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("OTHER")
                                        .setCrossTotal(new BigDecimal(200)))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid(Promotions.PROD_50OFF_BOGOF)
                                        .setCrossTotal(new BigDecimal(150)));

        engine.apply(document5, promotions);
        Assert.assertTrue(new BigDecimal(50).compareTo(document5.getPositions().get(0).getCrossTotal()) == 0);
        Assert.assertTrue(new BigDecimal(100).compareTo(document5.getPositions().get(1).getCrossTotal()) == 0);
        Assert.assertTrue(new BigDecimal(150).compareTo(document5.getPositions().get(2).getCrossTotal()) == 0);
        Assert.assertTrue(new BigDecimal(200).compareTo(document5.getPositions().get(3).getCrossTotal()) == 0);
        Assert.assertTrue(new BigDecimal(0).compareTo(document5.getPositions().get(4).getCrossTotal()) == 0);
    }
}
