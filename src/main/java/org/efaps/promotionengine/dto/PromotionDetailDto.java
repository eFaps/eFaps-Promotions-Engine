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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.efaps.promotionengine.api.IPromotionDetail;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = PromotionDetailDto.Builder.class)
public class PromotionDetailDto
    implements IPromotionDetail
{

    private final int index;

    private final BigDecimal discount;

    private final String promotionOid;

    private PromotionDetailDto(Builder builder)
    {
        this.index = builder.index;
        this.discount = builder.discount;
        this.promotionOid = builder.promotionOid;
    }

    @Override
    public int getIndex()
    {
        return index;
    }

    @Override
    public BigDecimal getDiscount()
    {
        return discount;
    }

    @Override
    public String getPromotionOid()
    {
        return promotionOid;
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

        private int index;
        private BigDecimal discount;
        private String promotionOid;

        private Builder()
        {
        }

        public Builder withIndex(int index)
        {
            this.index = index;
            return this;
        }

        public Builder withDiscount(BigDecimal discount)
        {
            this.discount = discount;
            return this;
        }

        public Builder withPromotionOid(String promotionOid)
        {
            this.promotionOid = promotionOid;
            return this;
        }

        public IPromotionDetail build()
        {
            return new PromotionDetailDto(this);
        }
    }
}
