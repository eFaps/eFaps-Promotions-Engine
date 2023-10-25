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
}
