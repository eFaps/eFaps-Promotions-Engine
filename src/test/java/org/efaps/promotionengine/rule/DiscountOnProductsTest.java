package org.efaps.promotionengine.rule;

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

public class DiscountOnProductsTest
{

    @Test
    public void listOfProductsAreFityPercentOff()
    {
        final var targetConditions = new ArrayList<ICondition>();
        targetConditions.add(new ProductsCondition()
                        .setPositionQuantity(BigDecimal.ONE)
                        .setEntryOperator(EntryOperator.INCLUDES_ANY)
                        .addProduct("123.4")
                        .addProduct("123.5")
                        .addProduct("123.6"));

        final var actions = new ArrayList<IAction>();
        final var action = new PercentageDiscountAction().setPercentage(new BigDecimal(50));
        actions.add(action);

        final var promotion = Promotion.builder()
                        .withOid("123.456")
                        .withName("All products from Provider are 50% off")
                        .withDescription("This can be a long text")
                        .withPriority(1)
                        .withStartDateTime(OffsetDateTime.now().minusDays(5))
                        .withEndDateTime(OffsetDateTime.now().plusDays(5))
                        .withTargetConditions(targetConditions)
                        .withActions(actions)
                        .build();

        final var document = new Document()
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("123.4")
                                        .setCrossTotal(new BigDecimal(100)))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("223.456")
                                        .setCrossTotal(new BigDecimal(150)))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("223.456")
                                        .setCrossTotal(new BigDecimal(180)))
                        .addPosition(new Position()
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("123.5")
                                        .setCrossTotal(new BigDecimal(250)));

        final var engine = new Engine();
        engine.apply(document, Collections.singletonList(promotion));
        Assert.assertTrue(new BigDecimal(50).compareTo(document.getPositions().get(0).getCrossTotal()) == 0);
        Assert.assertTrue(new BigDecimal(150).compareTo(document.getPositions().get(1).getCrossTotal()) == 0);
        Assert.assertTrue(new BigDecimal(180).compareTo(document.getPositions().get(2).getCrossTotal()) == 0);
        Assert.assertTrue(new BigDecimal(125).compareTo(document.getPositions().get(3).getCrossTotal()) == 0);
    }
}
