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

import org.efaps.abacus.api.ICalcPosition;
import org.efaps.abacus.pojo.CalcDocument;
import org.efaps.promotionengine.api.IDocument;
import org.efaps.promotionengine.api.IPosition;
import org.efaps.promotionengine.api.IPromotionInfo;

public class Document
    extends CalcDocument
    implements IDocument
{

    private IPromotionInfo promotionInfo;
    private BigDecimal docDiscount;
    private final List<String> promotionOids = new ArrayList<>();

    public IPromotionInfo getPromotionInfo()
    {
        return promotionInfo;
    }

    @Override
    public void setPromotionInfo(IPromotionInfo info)
    {
        this.promotionInfo = info;
    }

    public Document addPosition(IPosition position)
    {
        return (Document) super.addPosition(position);
    }

    @Override
    public Document clone()
    {
        final var doc = new Document();
        doc.setNetTotal(this.getNetTotal());
        doc.setCrossTotal(this.getCrossTotal());
        doc.setTaxTotal(this.getTaxTotal());
        doc.setPromotionInfo(this.getPromotionInfo());
        doc.addDocDiscount(this.getDocDiscount());
        doc.setTaxes(this.getTaxes());
        doc.setPositions(this.getPositions().stream().map(ICalcPosition::clone).toList());
        return doc;
    }

    @Override
    public BigDecimal getDocDiscount()
    {
        return docDiscount;
    }

    @Override
    public void addDocDiscount(final BigDecimal discount)
    {
        if (discount != null) {
            if (this.docDiscount == null) {
                this.docDiscount = BigDecimal.ZERO;
            }
            this.docDiscount = discount;
        }
    }

    @Override
    public void addPromotionOid(String oid)
    {
        promotionOids.add(oid);
    }

    @Override
    public List<String> getPromotionOids()
    {
        return promotionOids;
    }
}
