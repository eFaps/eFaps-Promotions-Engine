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
package org.efaps.promotionengine.api;

import java.math.BigDecimal;
import java.util.List;

import org.efaps.abacus.api.ICalcDocument;

public interface IDocument
    extends ICalcDocument
{

    /**
     * A discount applied on the amount that the costumer should pay (including
     * tax etc). e.g. payable amount of 100 minus discount of 20 results in new
     * payable amount of 20
     *
     * @return
     */
    BigDecimal getDocDiscount();

    void addDocDiscount(BigDecimal discount);

    void setPromotionInfo(IPromotionInfo info);

    default IPromotionInfo getPromotionInfo()
    {
        return null;
    }

    void addPromotionOid(String oid);

    List<String> getPromotionOids();

    @Override
    IDocument clone();

}
