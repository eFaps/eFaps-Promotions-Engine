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
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.efaps.promotionengine.api.IPromotionDetail;
import org.efaps.promotionengine.api.IPromotionInfo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = PromotionInfoDto.Builder.class)
public class PromotionInfoDto
    implements IPromotionInfo
{

    private final BigDecimal netTotalDiscount;

    private final BigDecimal crossTotalDiscount;

    private final List<PromotionDetailDto> details;

    private final Set<String> promotionOids;

    private PromotionInfoDto(Builder builder)
    {
        this.netTotalDiscount = builder.netTotalDiscount;
        this.crossTotalDiscount = builder.crossTotalDiscount;
        this.details = builder.details;
        this.promotionOids = builder.promotionOids;
    }

    @Override
    public BigDecimal getNetTotalDiscount()
    {
        return netTotalDiscount;
    }

    @Override
    public BigDecimal getCrossTotalDiscount()
    {
        return crossTotalDiscount;
    }

    @Override
    public List<IPromotionDetail> getDetails()
    {
        return details.stream().map(detail -> ((IPromotionDetail) detail)).toList();
    }

    @Override
    public List<IPromotionDetail> findDetailsForPosition(int index)
    {
        return getDetails().stream().filter(detail -> detail.getPositionIndex() == index).collect(Collectors.toList());
    }

    @Override
    public Set<String> getPromotionOids()
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

        private BigDecimal netTotalDiscount;
        private BigDecimal crossTotalDiscount;
        private List<PromotionDetailDto> details = Collections.emptyList();
        private Set<String> promotionOids = Collections.emptySet();

        private Builder()
        {
        }

        public Builder withNetTotalDiscount(BigDecimal netTotalDiscount)
        {
            this.netTotalDiscount = netTotalDiscount;
            return this;
        }

        public Builder withCrossTotalDiscount(BigDecimal crossTotalDiscount)
        {
            this.crossTotalDiscount = crossTotalDiscount;
            return this;
        }

        public Builder withDetails(List<PromotionDetailDto> details)
        {
            this.details = details;
            return this;
        }

        public Builder withPromotionOids(Set<String> promotionOids)
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
