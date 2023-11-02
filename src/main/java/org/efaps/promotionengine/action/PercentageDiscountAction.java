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
package org.efaps.promotionengine.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.efaps.promotionengine.api.IPosition;
import org.efaps.promotionengine.process.Engine;
import org.efaps.promotionengine.process.ProcessData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PercentageDiscountAction
    extends AbstractPercentageAction
{

    private static final Logger LOG = LoggerFactory.getLogger(Engine.class);

    @Override
    public void run(final ProcessData process,
                    final List<IPosition> positions)
    {
        if (!positions.isEmpty()) {
            LOG.info("Applying action on positions: {}", positions);
            final List<IPosition> strategySorted = new ArrayList<>(positions);
            if (!process.getPositionsUsedForSouce().isEmpty()) {
                strategySorted.addAll(process.getPositionsUsedForSouce());
            }
            switch (getStrategy()) {
                case CHEAPEST:
                default:
                    Collections.sort(strategySorted, Comparator.comparing(IPosition::getNetUnitPrice));
                    apply(process, strategySorted.get(0));
            }
        }
    }

    public void apply(final ProcessData process,
                      final IPosition position)
    {
        position.setPromotionOid(process.getCurrentPromotion().getOid());
        position.setDiscount(discount(position.getNetUnitPrice()));
        LOG.info("Applied action on positon: {}", position);
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
