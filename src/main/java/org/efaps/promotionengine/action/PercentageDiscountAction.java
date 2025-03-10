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

import org.efaps.promotionengine.api.IPosition;
import org.efaps.promotionengine.dto.PromotionDetailDto;
import org.efaps.promotionengine.process.ProcessData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PercentageDiscountAction
    extends AbstractPercentageAction
{

    private static final Logger LOG = LoggerFactory.getLogger(PercentageDiscountAction.class);

    @Override
    public boolean apply(final ProcessData process,
                         final IPosition position)
    {
        boolean ret = false;
        if (!position.isBurned(process)) {
            ret = true;
            final var netUnitDiscount = discount(position.getNetUnitPrice());
            position.addPromotionDetail(PromotionDetailDto.builder()
                            .withNetUnitBase(position.getNetUnitPrice())
                            .withNetUnitDiscount(netUnitDiscount)
                            .withPromotionOid(process.getCurrentPromotion().getOid())
                            .build());

            position.setNetUnitPrice(position.getNetUnitPrice().subtract(netUnitDiscount));
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
        if (!(obj instanceof final PercentageDiscountAction other)) {
            return false;
        }

        if (this.getPercentage() == null && other.getPercentage() == null) {
            return true;
        }

        return this.getPercentage() != null && other.getPercentage() != null
                        && this.getPercentage().compareTo(other.getPercentage()) == 0;
    }
}
