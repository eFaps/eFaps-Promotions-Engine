package org.efaps.promotionengine;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;

import org.efaps.abacus.pojo.Configuration;
import org.efaps.abacus.pojo.Tax;
import org.efaps.promotionengine.action.PercentageDiscountAction;
import org.efaps.promotionengine.action.Strategy;
import org.efaps.promotionengine.condition.EntryOperator;
import org.efaps.promotionengine.condition.ProductsCondition;
import org.efaps.promotionengine.pojo.Document;
import org.efaps.promotionengine.pojo.Position;
import org.efaps.promotionengine.process.EngineRule;
import org.efaps.promotionengine.promotion.Promotion;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MostDiscountExcludeTest
{

    final Promotion promoWithExclusion = Promotion.builder()
                    .withOid("222.3")
                    .withName("Includes all but some not")
                    .withPriority(2)
                    .withStartDateTime(OffsetDateTime.now().minusDays(5))
                    .withEndDateTime(OffsetDateTime.now().plusDays(5))
                    .addTargetCondition(new ProductsCondition()
                                    .setPositionQuantity(BigDecimal.ONE)
                                    .setEntryOperator(EntryOperator.INCLUDES_ANY)
                                    .addProduct("7004.222004")
                                    .addProduct("7004.222001")
                                    .addProduct("7004.222009")
                                    .addProduct("7004.199102")
                                    .addProduct("7004.199101")
                                    .addProduct("7004.199101"))
                    .addTargetCondition(new ProductsCondition()
                                    .setPositionQuantity(BigDecimal.ONE)
                                    .setEntryOperator(EntryOperator.EXCLUDES)
                                    .addProduct("7004.199102")
                                    .addProduct("7004.199101")
                                    .addProduct("7004.199101"))
                    .addAction(new PercentageDiscountAction().setPercentage(new BigDecimal(20))
                                    .setStrategy(Strategy.INDEX))
                    .build();

    private Document getDocument()
    {
        return new Document()
                        .addPosition(new Position()
                                        .setIndex(1)
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("7004.222004")
                                        .setNetUnitPrice(new BigDecimal(200))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))))
                        .addPosition(new Position()
                                        .setIndex(1)
                                        .setQuantity(BigDecimal.ONE)
                                        .setProductOid("7004.199102")
                                        .setNetUnitPrice(new BigDecimal(100))
                                        .addTax(Tax.getAdvalorem("IGV", new BigDecimal("18"))));
    }

    @Test
    public void feb2055()
    {
        final var document = getDocument();
        final var promotions = new ArrayList<Promotion>();
        promotions.add(promoWithExclusion);

        final var calculator = new Calculator(new Configuration());
        calculator.calc(document, promotions, null,
                        new PromotionsConfiguration().setEngineRule(EngineRule.MOSTDISCOUNT));

        Assert.assertTrue(new BigDecimal(160).compareTo(document.getPositions().get(0).getNetPrice()) == 0);
        Assert.assertTrue(new BigDecimal(100).compareTo(document.getPositions().get(1).getNetPrice()) == 0);
    }
}
