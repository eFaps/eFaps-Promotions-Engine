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
package org.efaps.promotionengine.condition;

import java.time.LocalDate;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class DateConditionRange
{

    private LocalDate startDate;

    private LocalDate endDate;

    public LocalDate getStartDate()
    {
        return startDate;
    }

    public DateConditionRange setStartDate(final LocalDate startDate)
    {
        this.startDate = startDate;
        return this;
    }

    public LocalDate getEndDate()
    {
        return endDate;
    }

    public DateConditionRange setEndDate(final LocalDate endDate)
    {
        this.endDate = endDate;
        return this;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof final DateConditionRange range)) {
            return false;
        }
        return Objects.equals(this.getStartDate(), range.getStartDate())
                        && Objects.equals(this.getEndDate(), range.getEndDate());
    }
}
