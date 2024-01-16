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

import org.efaps.promotionengine.promotion.Promotion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class SerializationTest
{

    private static final Logger LOG = LoggerFactory.getLogger(SerializationTest.class);

    protected ObjectMapper getObjectMapper()
    {
        final var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        return objectMapper;
    }

    @Test(dataProvider = "promotions")
    public void toJsonAndBack(Promotion promotion)
        throws JsonProcessingException
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
                        Promotions.buyOneGetOneFree().build(),
                        Promotions.second25PercentOff().build(),
                        Promotions.buyMoreThan100AndGet10PercentOff().build(),
                        Promotions.buyMoreThan100AndGet20Off().build(),
                        Promotions.storeHas20PercentageOff().build(),
                        Promotions.from4pmTo7pm20PercentageOff().build(),
                        Promotions.everyMondayAndThursday20PercentageOff().build()
        };
    }
}
