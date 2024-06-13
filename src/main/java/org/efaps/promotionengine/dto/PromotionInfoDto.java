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
package org.efaps.promotionengine.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.efaps.promotionengine.api.IPromotionDetail;
import org.efaps.promotionengine.api.IPromotionInfo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = PromotionInfoDto.Builder.class)
public class PromotionInfoDto
    implements IPromotionInfo
{

    private final BigDecimal totalDiscount;

    private final List<IPromotionDetail> details;

    private final List<String> promotionOids;

    private PromotionInfoDto(Builder builder)
    {
        this.totalDiscount = builder.totalDiscount;
        this.details = builder.details;
        this.promotionOids = builder.promotionOids;
    }

    @Override
    public BigDecimal getTotalDiscount()
    {
        return totalDiscount;
    }

    @Override
    public List<IPromotionDetail> getDetails()
    {
        return details;
    }

    @Override
    public List<String> getPromotionOids()
    {
        return promotionOids;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public static final class Builder
    {

        private BigDecimal totalDiscount;
        private List<IPromotionDetail> details = Collections.emptyList();
        private List<String> promotionOids = new ArrayList<>();

        private Builder()
        {
        }

        public Builder withTotalDiscount(BigDecimal totalDiscount)
        {
            this.totalDiscount = totalDiscount;
            return this;
        }

        public Builder withDetails(List<IPromotionDetail> details)
        {
            this.details = details;
            return this;
        }

        public Builder withPromotionOids(List<String> promotionOids)
        {
            this.promotionOids = promotionOids;
            return this;
        }

        public PromotionInfoDto build()
        {
            return new PromotionInfoDto(this);
        }
    }
}
