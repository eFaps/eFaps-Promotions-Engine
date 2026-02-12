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

import org.efaps.promotionengine.action.Strategy;
import org.efaps.promotionengine.promotion.Promotion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;


public class SerializationTest
{

    private static final Logger LOG = LoggerFactory.getLogger(SerializationTest.class);

    protected ObjectMapper getObjectMapper()
    {
        final var objectMapper = JsonMapper.builder()
                        .enable(SerializationFeature.INDENT_OUTPUT)
                        .build();
        return objectMapper;
    }

    @Test(dataProvider = "promotions")
    public void toJsonAndBack(Promotion promotion)
    {
        final var objectMapper = getObjectMapper();
        final var jsonStr = objectMapper.writeValueAsString(promotion);
        LOG.info("toJson: \n{}", jsonStr);

        final var deserializedPromotion = objectMapper.readValue(jsonStr, Promotion.class);
        LOG.info("{}", deserializedPromotion);
        Assert.assertEquals(promotion, deserializedPromotion);
    }

    @DataProvider(name = "promotions")
    public static Object[] createData()
    {
        return new Object[] {
                        Promotions.productsFiftyPercentOff().build(),
                        Promotions.buyOneGetOneFree("123.456", "789.12").build(),
                        Promotions.second25PercentOff(Strategy.CHEAPEST, "123.456", "789.12").build(),
                        Promotions.second25PercentOff(Strategy.PRICIEST, "123.456", "789.12").build(),
                        Promotions.buyMoreThan100AndGet10PercentOff().build(),
                        Promotions.buyMoreThan100AndGet20Off().build(),
                        Promotions.storeHas20PercentageOff().build(),
                        Promotions.from4pmTo7pm20PercentageOff().build(),
                        Promotions.everyMondayAndThursday20PercentageOff().build(),
                        Promotions.cyberWow20PercentageOff().build(),
                        Promotions.secondForOne(Strategy.CHEAPEST).build(),
                        Promotions.secondForOne(Strategy.PRICIEST).build(),
                        Promotions.get10OffIfYouBuyMoreThan100().build(),
                        Promotions.get10OffIfYouBuyMoreThan100ByFamily().build(),
                        Promotions.getBuyTwoAndGetBothForFixAmount("123.456", "789.12").build(),
                        Promotions.getAggregationPromotion().build()
        };
    }
}
