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
package org.efaps.promotionengine;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;

import org.efaps.promotionengine.action.IAction;
import org.efaps.promotionengine.action.PercentageDiscountAction;
import org.efaps.promotionengine.condition.EntryOperator;
import org.efaps.promotionengine.condition.ICondition;
import org.efaps.promotionengine.condition.ProductsCondition;
import org.efaps.promotionengine.pojo.Document;
import org.efaps.promotionengine.pojo.Position;
import org.efaps.promotionengine.process.Engine;
import org.efaps.promotionengine.promotion.Promotion;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BuyOneGetOneFreeTest
{

    @Test
    public void buyOneGetOneFree()
    {
        final var sourceConditions = new ArrayList<ICondition>();
        sourceConditions.add(new ProductsCondition()
                        .setPositionQuantity(BigDecimal.ONE)
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .addProduct("123.456"));

        final var targetConditions = new ArrayList<ICondition>();
        targetConditions.add(new ProductsCondition()
                        .setPositionQuantity(BigDecimal.ONE)
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .addProduct("123.456"));

        final var actions = new ArrayList<IAction>();
        final var action = new PercentageDiscountAction().setPercentage(new BigDecimal(100));
        actions.add(action);

        final var promotion = Promotion.builder()
                        .withOid("123.456")
                        .withName("Buy one get one free")
                        .withDescription("This can be a long text")
                        .withPriority(1)
                        .withStartDateTime(OffsetDateTime.now().minusDays(5))
                        .withEndDateTime(OffsetDateTime.now().plusDays(5))
                        .withSourceConditions(sourceConditions)
                        .withTargetConditions(targetConditions)
                        .withActions(actions)
                        .build();

        final var positions = new ArrayList<Position>();
        positions.add(new Position()
                        .setQuantity(BigDecimal.ONE)
                        .setProductOid("123.456")
                        .setCrossTotal(new BigDecimal(122)));
        positions.add(new Position()
                        .setQuantity(BigDecimal.ONE)
                        .setProductOid("123.456")
                        .setCrossTotal(new BigDecimal(122)));
        final var document = new Document()
                        .setPositions(positions);
        final var engine = new Engine();
        engine.apply(document, Collections.singletonList(promotion));
        Assert.assertTrue(new BigDecimal(122).compareTo(document.getPositions().get(0).getCrossTotal()) == 0);
        Assert.assertTrue(new BigDecimal(0).compareTo(document.getPositions().get(1).getCrossTotal()) == 0);
    }


    @Test
    public void buyOneGetOneFreeNotMet()
    {
        final var sourceConditions = new ArrayList<ICondition>();
        sourceConditions.add(new ProductsCondition()
                        .setPositionQuantity(BigDecimal.ONE)
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .addProduct("123.456"));

        final var targetConditions = new ArrayList<ICondition>();
        targetConditions.add(new ProductsCondition()
                        .setPositionQuantity(BigDecimal.ONE)
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .addProduct("223.456"));

        final var actions = new ArrayList<IAction>();
        final var action = new PercentageDiscountAction().setPercentage(new BigDecimal(100));
        actions.add(action);

        final var promotion = Promotion.builder()
                        .withOid("123.456")
                        .withName("Buy one get one free")
                        .withDescription("This can be a long text")
                        .withPriority(1)
                        .withStartDateTime(OffsetDateTime.now().minusDays(5))
                        .withEndDateTime(OffsetDateTime.now().plusDays(5))
                        .withSourceConditions(sourceConditions)
                        .withTargetConditions(targetConditions)
                        .withActions(actions)
                        .build();

        final var positions = new ArrayList<Position>();
        positions.add(new Position()
                        .setQuantity(BigDecimal.ONE)
                        .setProductOid("123.456")
                        .setCrossTotal(new BigDecimal(122)));
        positions.add(new Position()
                        .setQuantity(BigDecimal.ONE)
                        .setProductOid("123.456")
                        .setCrossTotal(new BigDecimal(122)));
        final var document = new Document()
                        .setPositions(positions);
        final var engine = new Engine();
        engine.apply(document, Collections.singletonList(promotion));
        Assert.assertTrue(new BigDecimal(122).compareTo(document.getPositions().get(0).getCrossTotal()) == 0);
        Assert.assertTrue(new BigDecimal(122).compareTo(document.getPositions().get(1).getCrossTotal()) == 0);
    }

    @Test
    public void threeForTwo()
    {
        final var sourceConditions = new ArrayList<ICondition>();
        sourceConditions.add(new ProductsCondition()
                        .setPositionQuantity(new BigDecimal(2))
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .addProduct("123.456"));

        final var targetConditions = new ArrayList<ICondition>();
        targetConditions.add(new ProductsCondition()
                        .setPositionQuantity(BigDecimal.ONE)
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .addProduct("123.456"));

        final var actions = new ArrayList<IAction>();
        final var action = new PercentageDiscountAction().setPercentage(new BigDecimal(100));
        actions.add(action);

        final var promotion = Promotion.builder()
                        .withOid("123.456")
                        .withName("Buy Three pay two")
                        .withDescription("This can be a long text")
                        .withPriority(1)
                        .withStartDateTime(OffsetDateTime.now().minusDays(5))
                        .withEndDateTime(OffsetDateTime.now().plusDays(5))
                        .withSourceConditions(sourceConditions)
                        .withTargetConditions(targetConditions)
                        .withActions(actions)
                        .build();

        final var positions = new ArrayList<Position>();
        positions.add(new Position()
                        .setQuantity(new BigDecimal(2))
                        .setProductOid("123.456")
                        .setCrossTotal(new BigDecimal(244)));
        positions.add(new Position()
                        .setQuantity(BigDecimal.ONE)
                        .setProductOid("123.456")
                        .setCrossTotal(new BigDecimal(122)));
        final var document = new Document()
                        .setPositions(positions);
        final var engine = new Engine();
        engine.apply(document, Collections.singletonList(promotion));
        Assert.assertTrue(new BigDecimal(244).compareTo(document.getPositions().get(0).getCrossTotal()) == 0);
        Assert.assertTrue(new BigDecimal(0).compareTo(document.getPositions().get(1).getCrossTotal()) == 0);
    }

    @Test
    public void second25PercentOff() {
        final var document = new Document()
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_BOGOF2)
                                        .setCrossTotal(new BigDecimal(150)))
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_BOGOF2)
                                        .setCrossTotal(new BigDecimal(250)))
                        .addPosition(new Position()
                                        .setQuantity(new BigDecimal(1))
                                        .setProductOid(Promotions.PROD_BOGOF3)
                                        .setCrossTotal(new BigDecimal(200)))

                        ;
        final var promotion = Promotions.second25PercentOff();
        final var engine = new Engine();
        engine.apply(document, Collections.singletonList(promotion.build()));
        Assert.assertTrue(new BigDecimal(112.5).compareTo(document.getPositions().get(0).getCrossTotal()) == 0);
        Assert.assertTrue(new BigDecimal(250).compareTo(document.getPositions().get(1).getCrossTotal()) == 0);
        Assert.assertTrue(new BigDecimal(200).compareTo(document.getPositions().get(2).getCrossTotal()) == 0);
    }


}