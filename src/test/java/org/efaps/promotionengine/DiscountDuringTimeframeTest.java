/*
 * Copyright 2003 - 2024 The eFaps Team
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
package org.efaps.promotionengine;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.Collections;

import org.efaps.abacus.pojo.Configuration;
import org.efaps.abacus.pojo.Tax;
import org.efaps.promotionengine.action.IAction;
import org.efaps.promotionengine.action.PercentageDiscountAction;
import org.efaps.promotionengine.condition.EntryOperator;
import org.efaps.promotionengine.condition.ICondition;
import org.efaps.promotionengine.condition.ProductsCondition;
import org.efaps.promotionengine.condition.TimeCondition;
import org.efaps.promotionengine.pojo.Document;
import org.efaps.promotionengine.pojo.Position;
import org.efaps.promotionengine.promotion.Promotion;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DiscountDuringTimeframeTest
{
    @Test
    public void from4pmTo6pmPercentageOff() {
        final var sourceConditions = new ArrayList<ICondition>();
        sourceConditions.add(new TimeCondition().addRange(
                        OffsetTime.now().minusHours(3).withSecond(0).withNano(0),
                        OffsetTime.now().plusHours(1).withSecond(0).withNano(0)));

        final var targetConditions = new ArrayList<ICondition>();
        targetConditions.add(new ProductsCondition()
                        .setPositionQuantity(BigDecimal.ONE)
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .addProduct("123.456"));

        // apply twenty percent off
        final var actions = new ArrayList<IAction>();
        final var action = new PercentageDiscountAction().setPercentage(new BigDecimal(20));
        actions.add(action);

        final var promotion = Promotion.builder()
                        .withOid("123.456")
                        .withName("Apply twenty percent off")
                        .withDescription("This can be a long text")
                        .withPriority(1)
                        .withStartDateTime(OffsetDateTime.now().minusDays(5))
                        .withEndDateTime(OffsetDateTime.now().plusDays(5))
                        .withSourceConditions(sourceConditions)
                        .withTargetConditions(targetConditions)
                        .withActions(actions)
                        .build();


        final var document = new Document()
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("123.456")
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));

        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, Collections.singletonList(promotion));
        Assert.assertTrue(new BigDecimal(80).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal("94.4").compareTo(document.getPositions().get(0).getCrossPrice()) == 0);
    }
}
