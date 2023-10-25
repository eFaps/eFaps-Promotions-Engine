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
package org.efaps.promotionengine.pojo;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Position
{

    private Integer index;

    private String productOid;

    private BigDecimal quantity;

    private BigDecimal crossTotal;

    private String appliedPromotionOid;

    public String getAppliedPromotionOid()
    {
        return appliedPromotionOid;
    }

    public Position setAppliedPromotionOid(final String appliedPromotionOid)
    {
        this.appliedPromotionOid = appliedPromotionOid;
        return this;
    }

    public Integer getIndex()
    {
        return index;
    }

    public Position setIndex(Integer index)
    {
        this.index = index;
        return this;
    }

    public String getProductOid()
    {
        return productOid;
    }

    public Position setProductOid(String productOid)
    {
        this.productOid = productOid;
        return this;
    }

    public BigDecimal getQuantity()
    {
        return quantity;
    }

    public Position setQuantity(BigDecimal quantity)
    {
        this.quantity = quantity;
        return this;
    }

    public BigDecimal getCrossTotal()
    {
        return crossTotal;
    }

    public Position setCrossTotal(BigDecimal crossTotal)
    {
        this.crossTotal = crossTotal;
        return this;
    }

    public boolean isBurned()
    {
        return this.appliedPromotionOid != null;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
