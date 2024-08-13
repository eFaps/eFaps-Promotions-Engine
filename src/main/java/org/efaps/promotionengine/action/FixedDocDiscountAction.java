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

import java.util.Objects;

import org.efaps.promotionengine.api.IPosition;
import org.efaps.promotionengine.process.ProcessData;

public class FixedDocDiscountAction
    extends AbstractFixedAction
{

    boolean executed = false;

    @Override
    public boolean run(final ProcessData process)
    {
        final boolean ret;
        if (!executed) {
            executed = true;
            process.getDocument().addDocDiscount(getAmount());
            process.getDocument().addPromotionOid(process.getCurrentPromotion().getOid());
            ret = true;
        } else {
            ret = false;
        }
        return ret;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FixedDocDiscountAction)) {
            return false;
        }
        return Objects.equals(this.getAmount(), ((FixedDocDiscountAction) obj).getAmount());
    }

    @Override
    public boolean apply(final ProcessData process,
                         final IPosition position)
    {
        return false;
    }
}
