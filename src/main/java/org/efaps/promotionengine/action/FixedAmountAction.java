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
package org.efaps.promotionengine.action;

import java.math.BigDecimal;

import org.efaps.promotionengine.api.IPosition;
import org.efaps.promotionengine.process.ProcessData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixedAmountAction
    extends AbstractAction
{

    private static final Logger LOG = LoggerFactory.getLogger(FixedAmountAction.class);

    private BigDecimal amount;

    public BigDecimal getAmount()
    {
        return amount;
    }

    public FixedAmountAction setAmount(final BigDecimal amount)
    {
        this.amount = amount;
        return this;
    }

    @Override
    public boolean apply(final ProcessData process,
                         final IPosition position)
    {
        boolean ret = false;
        if (position.getPromotionOid() == null) {
            ret = true;
            position.setPromotionOid(process.getCurrentPromotion().getOid());
            position.setNetUnitPrice(amount);
            LOG.info("Applied action on positon: {}", position);
        }
        return ret;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof final FixedAmountAction other)) {
            return false;
        }

        if (this.getAmount() == null && other.getAmount() == null) {
            return true;
        }

        return this.getAmount() != null && other.getAmount() != null
                        && this.getAmount().compareTo(other.getAmount()) == 0;
    }

}
