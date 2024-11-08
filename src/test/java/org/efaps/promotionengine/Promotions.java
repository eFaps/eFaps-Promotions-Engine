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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.efaps.promotionengine.action.FixedAmountAction;
import org.efaps.promotionengine.action.FixedDocDiscountAction;
import org.efaps.promotionengine.action.IAction;
import org.efaps.promotionengine.action.PercentageDiscountAction;
import org.efaps.promotionengine.action.PercentageDocDiscountAction;
import org.efaps.promotionengine.action.Strategy;
import org.efaps.promotionengine.condition.DateCondition;
import org.efaps.promotionengine.condition.DocTotalCondition;
import org.efaps.promotionengine.condition.EntryOperator;
import org.efaps.promotionengine.condition.ICondition;
import org.efaps.promotionengine.condition.Operator;
import org.efaps.promotionengine.condition.OrCondition;
import org.efaps.promotionengine.condition.ProductFamilyConditionEntry;
import org.efaps.promotionengine.condition.ProductFamilyTotalCondition;
import org.efaps.promotionengine.condition.ProductTotalCondition;
import org.efaps.promotionengine.condition.ProductsCondition;
import org.efaps.promotionengine.condition.StoreCondition;
import org.efaps.promotionengine.condition.TimeCondition;
import org.efaps.promotionengine.condition.WeekdayCondition;
import org.efaps.promotionengine.promotion.Promotion;

public class Promotions
{

    public static String PROD_50OFF1 = "PROD_50OFF1";
    public static String PROD_50OFF2 = "PROD_50OFF2";
    public static String PROD_50OFF3 = "PROD_50OFF3";
    public static String PROD_BOGOF1 = "PROD_BOGOF1";

    public static String PROD_50OFF_BOGOF = "PROD_50OFF_BOGOF";

    public static String PROD_BOGOF2 = "PROD_BOGOF2";
    public static String PROD_BOGOF3 = "PROD_BOGOF3";

    public static String PROD_SO1 = "PROD_SO1";
    public static String PROD_SO2 = "PROD_SO2";
    public static String PROD_SO3 = "PROD_SO3";
    public static String PROD_SO4 = "PROD_SO4";

    public static Promotion.Builder productsFiftyPercentOff()
    {
        // products are 50% off
        final var targetConditions = new ArrayList<ICondition>();
        targetConditions.add(new ProductsCondition()
                        .setPositionQuantity(BigDecimal.ONE)
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .addProduct(PROD_50OFF1)
                        .addProduct(PROD_50OFF2)
                        .addProduct(PROD_50OFF3)
                        .addProduct(PROD_50OFF_BOGOF));

        final var actions = new ArrayList<IAction>();
        final var action = new PercentageDiscountAction().setPercentage(new BigDecimal(50));
        actions.add(action);

        return Promotion.builder()
                        .withOid("222.1")
                        .withName("All products from Provider are 50% off")
                        .withDescription("This can be a long text")
                        .withPriority(50)
                        .withStartDateTime(OffsetDateTime.now().minusDays(5))
                        .withEndDateTime(OffsetDateTime.now().plusDays(5))
                        .withTargetConditions(targetConditions)
                        .withActions(actions);
    }

    public static Promotion.Builder buyOneGetOneFree(final String... productOid)
    {
        final var productOids = new HashSet<>(Arrays.asList(productOid));

        final var sourceConditions = new ArrayList<ICondition>();
        sourceConditions.add(new ProductsCondition()
                        .setPositionQuantity(BigDecimal.ONE)
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .setProducts(productOids));

        final var targetConditions = new ArrayList<ICondition>();
        targetConditions.add(new ProductsCondition()
                        .setPositionQuantity(BigDecimal.ONE)
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .setProducts(productOids));

        final var actions = new ArrayList<IAction>();
        final var action = new PercentageDiscountAction().setPercentage(new BigDecimal(100));
        actions.add(action);

        return Promotion.builder()
                        .withOid("222.2")
                        .withName("Buy one get one free")
                        .withDescription("This can be a long text")
                        .withPriority(100)
                        .withStartDateTime(OffsetDateTime.now().minusDays(5))
                        .withEndDateTime(OffsetDateTime.now().plusDays(5))
                        .withSourceConditions(sourceConditions)
                        .withTargetConditions(targetConditions)
                        .withActions(actions);
    }

    public static Promotion.Builder second25PercentOff(final Strategy strategy,
                                                       final String... productOid)
    {
        final var productOids = new HashSet<>(Arrays.asList(productOid));

        final var sourceConditions = new ArrayList<ICondition>();
        sourceConditions.add(new ProductsCondition()
                        .setPositionQuantity(BigDecimal.ONE)
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .setProducts(productOids));

        final var targetConditions = new ArrayList<ICondition>();
        targetConditions.add(new ProductsCondition()
                        .setPositionQuantity(BigDecimal.ONE)
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .setProducts(productOids));

        final var actions = new ArrayList<IAction>();
        final var action = new PercentageDiscountAction().setPercentage(new BigDecimal(25)).setStrategy(strategy);
        actions.add(action);

        return Promotion.builder()
                        .withOid("222.3")
                        .withName("get second (" + strategy + ") 25% off")
                        .withDescription("This can be a long text")
                        .withPriority(100)
                        .withStartDateTime(OffsetDateTime.now().minusDays(5))
                        .withEndDateTime(OffsetDateTime.now().plusDays(5))
                        .withSourceConditions(sourceConditions)
                        .withTargetConditions(targetConditions)
                        .withActions(actions);
    }

    public static Promotion.Builder buyMoreThan100AndGet10PercentOff()
    {
        final var sourceConditions = new ArrayList<ICondition>();
        sourceConditions.add(new DocTotalCondition()
                        .setTotal(new BigDecimal(100)).setOperator(Operator.GREATEREQUAL));

        final var actions = new ArrayList<IAction>();
        final var action = new PercentageDocDiscountAction().setPercentage(new BigDecimal(25));
        actions.add(action);

        return Promotion.builder()
                        .withOid("222.3")
                        .withName("get second (cheaper one) 25% off")
                        .withDescription("This can be a long text")
                        .withPriority(100)
                        .withStartDateTime(OffsetDateTime.now().minusDays(5))
                        .withEndDateTime(OffsetDateTime.now().plusDays(5))
                        .withSourceConditions(sourceConditions)
                        .withActions(actions);
    }

    public static Promotion.Builder buyMoreThan100AndGet20Off()
    {
        final var sourceConditions = new ArrayList<ICondition>();
        sourceConditions.add(new DocTotalCondition()
                        .setTotal(new BigDecimal(100)).setOperator(Operator.GREATEREQUAL));

        final var actions = new ArrayList<IAction>();
        final var action = new FixedDocDiscountAction().setAmount(new BigDecimal(20));
        actions.add(action);

        return Promotion.builder()
                        .withOid("222.3")
                        .withName("get 20 off if you buy more than 100")
                        .withDescription("This can be a long text")
                        .withPriority(100)
                        .withStartDateTime(OffsetDateTime.now().minusDays(5))
                        .withEndDateTime(OffsetDateTime.now().plusDays(5))
                        .withSourceConditions(sourceConditions)
                        .withActions(actions);
    }

    public static Promotion.Builder storeHas20PercentageOff()
    {
        final var sourceConditions = new ArrayList<ICondition>();
        sourceConditions.add(new StoreCondition().addIdentifier("XYZ"));

        final var targetConditions = new ArrayList<ICondition>();
        targetConditions.add(new ProductsCondition()
                        .setPositionQuantity(BigDecimal.ONE)
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .addProduct("123.456"));

        final var actions = new ArrayList<IAction>();
        final var action = new PercentageDiscountAction().setPercentage(new BigDecimal(20));
        actions.add(action);

        return Promotion.builder()
                        .withOid("123.456")
                        .withName("Apply twenty percent off")
                        .withDescription("This can be a long text")
                        .withPriority(1)
                        .withStartDateTime(OffsetDateTime.now().minusDays(5))
                        .withEndDateTime(OffsetDateTime.now().plusDays(5))
                        .withSourceConditions(sourceConditions)
                        .withTargetConditions(targetConditions)
                        .withActions(actions);
    }

    public static Promotion.Builder from4pmTo7pm20PercentageOff()
    {
        final var sourceConditions = new ArrayList<ICondition>();
        sourceConditions.add(
                        new TimeCondition().addRange(OffsetTime.now().minusHours(3).withSecond(0).withNano(0),
                                        OffsetTime.now().plusHours(1).withSecond(0).withNano(0)));

        final var targetConditions = new ArrayList<ICondition>();
        targetConditions.add(new ProductsCondition()
                        .setPositionQuantity(BigDecimal.ONE)
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .addProduct("123.456"));

        final var actions = new ArrayList<IAction>();
        final var action = new PercentageDiscountAction().setPercentage(new BigDecimal(20));
        actions.add(action);

        return Promotion.builder()
                        .withOid("123.456")
                        .withName("Apply twenty percent off")
                        .withDescription("This can be a long text")
                        .withPriority(1)
                        .withStartDateTime(OffsetDateTime.now().minusDays(5))
                        .withEndDateTime(OffsetDateTime.now().plusDays(5))
                        .withSourceConditions(sourceConditions)
                        .withTargetConditions(targetConditions)
                        .withActions(actions);
    }

    public static Promotion.Builder everyMondayAndThursday20PercentageOff()
    {
        final var sourceConditions = new ArrayList<ICondition>();
        sourceConditions.add(new WeekdayCondition().addDay(DayOfWeek.MONDAY).addDay(DayOfWeek.THURSDAY));

        final var targetConditions = new ArrayList<ICondition>();
        targetConditions.add(new ProductsCondition()
                        .setPositionQuantity(BigDecimal.ONE)
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .addProduct("123.456"));

        final var actions = new ArrayList<IAction>();
        final var action = new PercentageDiscountAction().setPercentage(new BigDecimal(20));
        actions.add(action);

        return Promotion.builder()
                        .withOid("123.456")
                        .withName("Apply twenty percent off")
                        .withDescription("This can be a long text")
                        .withPriority(1)
                        .withStartDateTime(OffsetDateTime.now().minusDays(5))
                        .withEndDateTime(OffsetDateTime.now().plusDays(5))
                        .withSourceConditions(sourceConditions)
                        .withTargetConditions(targetConditions)
                        .withActions(actions);
    }

    public static Promotion.Builder cyberWow20PercentageOff()
    {
        final var sourceConditions = new ArrayList<ICondition>();
        sourceConditions.add(new DateCondition().addRange(LocalDate.now().minusDays(1), LocalDate.now().plusDays(3)));

        final var targetConditions = new ArrayList<ICondition>();
        targetConditions.add(new ProductsCondition()
                        .setPositionQuantity(BigDecimal.ONE)
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .addProduct("123.456"));

        final var actions = new ArrayList<IAction>();
        final var action = new PercentageDiscountAction().setPercentage(new BigDecimal(20));
        actions.add(action);

        return Promotion.builder()
                        .withOid("123.456")
                        .withName("Apply twenty percent off")
                        .withDescription("This can be a long text")
                        .withPriority(1)
                        .withStartDateTime(OffsetDateTime.now().minusDays(5))
                        .withEndDateTime(OffsetDateTime.now().plusDays(5))
                        .withSourceConditions(sourceConditions)
                        .withTargetConditions(targetConditions)
                        .withActions(actions);
    }

    public static Promotion.Builder secondForOne(final Strategy strategy)
    {
        final var sourceConditions = new ArrayList<ICondition>();
        sourceConditions.add(new ProductsCondition()
                        .setPositionQuantity(BigDecimal.ONE)
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .addProduct(PROD_SO1)
                        .addProduct(PROD_SO2));

        final var targetConditions = new ArrayList<ICondition>();
        targetConditions.add(new ProductsCondition()
                        .setPositionQuantity(BigDecimal.ONE)
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .addProduct(PROD_SO1)
                        .addProduct(PROD_SO2));

        final var actions = new ArrayList<IAction>();
        final var action = new FixedAmountAction().setAmount(new BigDecimal(1)).setStrategy(strategy);
        actions.add(action);

        return Promotion.builder()
                        .withOid("222.2")
                        .withName("Get second one for One Sol")
                        .withDescription("This can be a long text")
                        .withPriority(100)
                        .withStartDateTime(OffsetDateTime.now().minusDays(5))
                        .withEndDateTime(OffsetDateTime.now().plusDays(5))
                        .withSourceConditions(sourceConditions)
                        .withTargetConditions(targetConditions)
                        .withActions(actions);
    }

    public static Promotion.Builder get10OffIfYouBuyMoreThan100()
    {
        final var sourceConditions = new ArrayList<ICondition>();
        sourceConditions.add(new ProductTotalCondition()
                        .addProduct(PROD_SO1)
                        .addProduct(PROD_SO2)
                        .setTotal(new BigDecimal(100))
                        .setOperator(Operator.GREATER));

        final var actions = new ArrayList<IAction>();
        final var action = new FixedDocDiscountAction().setAmount(new BigDecimal(10));
        actions.add(action);

        return Promotion.builder()
                        .withOid("222.23")
                        .withName("Get 10 off if you bur more than 100")
                        .withDescription("This can be a long text")
                        .withPriority(100)
                        .withStartDateTime(OffsetDateTime.now().minusDays(5))
                        .withEndDateTime(OffsetDateTime.now().plusDays(5))
                        .withSourceConditions(sourceConditions)
                        .withActions(actions);
    }

    public static Promotion.Builder get10OffIfYouBuyMoreThan100ByFamily()
    {
        final var sourceConditions = new ArrayList<ICondition>();
        sourceConditions.add(new ProductFamilyTotalCondition()
                        .addEntry(new ProductFamilyConditionEntry()
                                        .setProductFamilyOid("FAMOID1")
                                        .addProduct(PROD_SO1))
                        .addEntry(new ProductFamilyConditionEntry()
                                        .setProductFamilyOid("FAMOID2")
                                        .addProduct(PROD_SO2))
                        .setTotal(new BigDecimal(100))
                        .setOperator(Operator.GREATER));

        final var actions = new ArrayList<IAction>();
        final var action = new FixedDocDiscountAction().setAmount(new BigDecimal(10));
        actions.add(action);

        return Promotion.builder()
                        .withOid("222.23")
                        .withName("Get 10 off if you bur more than 100")
                        .withDescription("This can be a long text")
                        .withPriority(100)
                        .withStartDateTime(OffsetDateTime.now().minusDays(5))
                        .withEndDateTime(OffsetDateTime.now().plusDays(5))
                        .withSourceConditions(sourceConditions)
                        .withActions(actions);
    }

    public static Promotion.Builder getBuyTwoAndGetBothForFixAmount(final String... productOid)
    {
        final var productOids = new HashSet<>(Arrays.asList(productOid));

        final var conditions = new ArrayList<ICondition>();
        conditions.add(new ProductsCondition()
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .setPositionQuantity(new BigDecimal(2))
                        .setAllowTargetSameAsSource(true)
                        .setProducts(productOids));

        final var actions = new ArrayList<IAction>();
        actions.add(new FixedAmountAction().setAmount(new BigDecimal(10)));

        return Promotion.builder()
                        .withOid("222.24")
                        .withName("But Two products and get both for a fixes price of 1")
                        .withDescription("This can be a long text")
                        .withPriority(100)
                        .withStartDateTime(OffsetDateTime.now().minusDays(5))
                        .withEndDateTime(OffsetDateTime.now().plusDays(5))
                        .withSourceConditions(conditions)
                        .withTargetConditions(conditions)
                        .withActions(actions);
    }

    public static Promotion.Builder getAggregationPromotion()
    {
        // simulate multiple product list e.g. all from one label OR from another label
        final var condition = new OrCondition()
                        .addCondition(new ProductsCondition()
                                        .setPositionQuantity(BigDecimal.ONE)
                                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                                        .addProduct(PROD_SO1))
                        .addCondition(new ProductsCondition()
                                        .setPositionQuantity(BigDecimal.ONE)
                                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                                        .addProduct(PROD_SO2))
                        .addCondition(new ProductsCondition()
                                        .setPositionQuantity(BigDecimal.ONE)
                                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                                        .addProduct(PROD_SO3));

        final var targetConditions = new ArrayList<ICondition>();
        targetConditions.add(condition);

        final var actions = new ArrayList<IAction>();
        final var action = new PercentageDiscountAction().setPercentage(new BigDecimal(50));
        actions.add(action);

        return Promotion.builder()
                        .withOid("777.2")
                        .withName("All products from one of the lists are 50% off")
                        .withDescription("This can be a long text")
                        .withPriority(50)
                        .withStartDateTime(OffsetDateTime.now().minusDays(5))
                        .withEndDateTime(OffsetDateTime.now().plusDays(5))
                        .withTargetConditions(targetConditions)
                        .withActions(actions);
    }

    public static Promotion.Builder products10PercentOff(final String... productOid)
    {
        final var productOids = new HashSet<>(Arrays.asList(productOid));
        final var targetConditions = new ArrayList<ICondition>();
        targetConditions.add(new ProductsCondition()
                        .setPositionQuantity(BigDecimal.ONE)
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .setProducts(productOids));

        final var actions = new ArrayList<IAction>();
        final var action = new PercentageDiscountAction().setPercentage(new BigDecimal(10));
        actions.add(action);

        return Promotion.builder()
                        .withOid("222.1")
                        .withName("Products are 10% off")
                        .withPriority(50)
                        .withStartDateTime(OffsetDateTime.now().minusDays(5))
                        .withEndDateTime(OffsetDateTime.now().plusDays(5))
                        .withTargetConditions(targetConditions)
                        .withActions(actions);
    }
}
