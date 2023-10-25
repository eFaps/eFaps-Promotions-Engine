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
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class SerializationTest
{

    private static final Logger LOG = LoggerFactory.getLogger(SerializationTest.class);

    @Test
    public void toJsonAndBack()
        throws JsonProcessingException
    {
        final var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        final var promotion = Promotions.productsFiftyPercentOff().build();
        final var jsonStr = objectMapper.writeValueAsString(promotion);
        LOG.info("toJson: \n{}", jsonStr);

        final var deserializedPromotion = objectMapper.readValue(jsonStr, Promotion.class);
        LOG.info("{}", deserializedPromotion);
        Assert.assertEquals(promotion, deserializedPromotion);
    }
}
