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
package org.efaps.promotionengine.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.efaps.abacus.api.ICalcPosition;
import org.efaps.abacus.pojo.CalcPosition;
import org.efaps.abacus.pojo.Tax;
import org.efaps.promotionengine.api.IPosition;

public class Position
    extends CalcPosition
    implements IPosition
{

    private List<String> promotionOids;

    @Override
    public void addPromotionOid(String promotionOid)
    {
        getPromotionOids().add(promotionOid);
    }

    public void setPromotionOids(final List<String> promotionOids)
    {
        this.promotionOids = promotionOids;
    }

    @Override
    public List<String> getPromotionOids()
    {
        if (this.promotionOids == null) {
            this.promotionOids = new ArrayList<>();
        }
        return promotionOids;
    }

    @Override
    public Position setNetUnitPrice(BigDecimal netUnitPrice)
    {
        return (Position) super.setNetUnitPrice(netUnitPrice);
    }

    @Override
    public Position setQuantity(BigDecimal quantity)
    {
        return (Position) super.setQuantity(quantity);
    }

    @Override
    public Position setProductOid(String productOid)
    {
        return (Position) super.setProductOid(productOid);
    }

    public Position addTax(final Tax tax)
    {
        if (CollectionUtils.isEmpty(getTaxes())) {
            setTaxes(new ArrayList<>());
        }
        getTaxes().add(tax);
        return this;
    }

    @Override
    public Position clone()
    {
        final var position = new Position();
        position.setCrossPrice(getCrossPrice());
        position.setCrossUnitPrice(getCrossUnitPrice());
        position.setIndex(getIndex());
        position.setNetPrice(getNetPrice());
        position.setNetUnitPrice(getNetUnitPrice());
        position.setProductOid(getProductOid());
        position.setQuantity(getQuantity());
        position.setTaxAmount(getTaxAmount());
        position.setTaxes(getTaxes());
        return position;
    }

    @Override
    public Position updateWith(final ICalcPosition position)
    {
        super.updateWith(position);
        setPromotionOids(((IPosition) position).getPromotionOids());
        return this;
    }
}
