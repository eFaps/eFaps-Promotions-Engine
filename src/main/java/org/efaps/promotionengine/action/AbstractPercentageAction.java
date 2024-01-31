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
import java.math.RoundingMode;

public abstract class AbstractPercentageAction
    extends AbstractAction
{

    private BigDecimal percentage;

    public BigDecimal getPercentage()
    {
        return percentage;
    }

    public AbstractPercentageAction setPercentage(final BigDecimal percentage)
    {
        this.percentage = percentage;
        return this;
    }

    public BigDecimal discount(final BigDecimal original)
    {
        // discount price = (original price × discount / 100)
        return original.multiply(
                        getPercentage().setScale(8).divide(new BigDecimal(100), RoundingMode.HALF_UP));
    }
}
