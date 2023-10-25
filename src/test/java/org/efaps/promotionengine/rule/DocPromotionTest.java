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
import java.util.Collections;

import org.efaps.promotionengine.pojo.Document;
import org.efaps.promotionengine.pojo.Position;
import org.efaps.promotionengine.process.Engine;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DocPromotionTest
{
    @Test
    public void buyMoreThan100AndGet10PercentOff() {
        final var promotion = Promotions.buyMoreThan100AndGet10PercentOff();

        final var document = new Document()
                        .setCrossTotal(new BigDecimal(110))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("123.4")
                                        .setCrossTotal(new BigDecimal(20)))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("123.5")
                                        .setCrossTotal(new BigDecimal(30)))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("123.5")
                                        .setCrossTotal(new BigDecimal(60)));

        final var engine = new Engine();
        engine.apply(document, Collections.singletonList(promotion.build()));
        Assert.assertTrue(new BigDecimal(82.5).compareTo(document.getCrossTotal()) == 0);
    }

    @Test
    public void buyMoreThan100AndGet20Off() {
        final var promotion = Promotions.buyMoreThan100AndGet20Off();

        final var document = new Document()
                        .setCrossTotal(new BigDecimal(110))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("123.4")
                                        .setCrossTotal(new BigDecimal(20)))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("123.5")
                                        .setCrossTotal(new BigDecimal(30)))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("123.5")
                                        .setCrossTotal(new BigDecimal(60)));

        final var engine = new Engine();
        engine.apply(document, Collections.singletonList(promotion.build()));
        Assert.assertTrue(new BigDecimal(90).compareTo(document.getCrossTotal()) == 0);
    }
}
