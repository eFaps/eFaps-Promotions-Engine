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

import org.efaps.abacus.api.ICalcPosition;
import org.efaps.abacus.pojo.CalcDocument;
import org.efaps.promotionengine.api.IDocument;
import org.efaps.promotionengine.api.IPosition;

public class Document
    extends CalcDocument
    implements IDocument
{

    private BigDecimal discount;

    public BigDecimal getDiscount()
    {
        return discount;
    }

    @Override
    public void setDiscount(BigDecimal discount)
    {
        this.discount = discount;
    }

    public Document addPosition(IPosition position)
    {
        return (Document) super.addPosition(position);
    }

    @Override
    public Document clone() {
        final var doc =  new Document();
        doc.setNetTotal(this.getNetTotal());
        doc.setCrossTotal(this.getCrossTotal());
        doc.setTaxTotal(this.getTaxTotal());
        doc.setDiscount(this.getDiscount());
        doc.setTaxes(this.getTaxes());
        doc.setPositions(this.getPositions().stream().map(ICalcPosition::clone).toList());
        return doc;
    }
}
