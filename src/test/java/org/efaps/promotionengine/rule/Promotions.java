package org.efaps.promotionengine.rule;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;

import org.efaps.promotionengine.action.IAction;
import org.efaps.promotionengine.action.PercentageDiscountAction;
import org.efaps.promotionengine.action.PercentageDocDiscountAction;
import org.efaps.promotionengine.condition.DocTotalCondition;
import org.efaps.promotionengine.condition.EntryOperator;
import org.efaps.promotionengine.condition.ICondition;
import org.efaps.promotionengine.condition.Operator;
import org.efaps.promotionengine.condition.ProductsCondition;
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

    public static Promotion.Builder buyOneGetOneFree()
    {
        final var sourceConditions = new ArrayList<ICondition>();
        sourceConditions.add(new ProductsCondition()
                        .setPositionQuantity(BigDecimal.ONE)
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .addProduct(PROD_BOGOF1)
                        .addProduct(PROD_50OFF_BOGOF));

        final var targetConditions = new ArrayList<ICondition>();
        targetConditions.add(new ProductsCondition()
                        .setPositionQuantity(BigDecimal.ONE)
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .addProduct(PROD_BOGOF1)
                        .addProduct(PROD_50OFF_BOGOF));

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

    public static Promotion.Builder second25PercentOff()
    {
        final var sourceConditions = new ArrayList<ICondition>();
        sourceConditions.add(new ProductsCondition()
                        .setPositionQuantity(BigDecimal.ONE)
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .addProduct(PROD_BOGOF2)
                        .addProduct(PROD_BOGOF3));

        final var targetConditions = new ArrayList<ICondition>();
        targetConditions.add(new ProductsCondition()
                        .setPositionQuantity(BigDecimal.ONE)
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .addProduct(PROD_BOGOF2)
                        .addProduct(PROD_BOGOF3));

        final var actions = new ArrayList<IAction>();
        final var action = new PercentageDiscountAction().setPercentage(new BigDecimal(25));
        actions.add(action);

        return Promotion.builder()
                        .withOid("222.3")
                        .withName("get second (cheaper one) 25% off")
                        .withDescription("This can be a long text")
                        .withPriority(100)
                        .withStartDateTime(OffsetDateTime.now().minusDays(5))
                        .withEndDateTime(OffsetDateTime.now().plusDays(5))
                        .withSourceConditions(sourceConditions)
                        .withTargetConditions(targetConditions)
                        .withActions(actions);
    }

    public static Promotion.Builder buyMoreThan100AndGet10PercentOff() {
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

}
