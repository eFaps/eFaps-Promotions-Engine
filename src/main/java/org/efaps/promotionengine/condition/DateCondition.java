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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.efaps.promotionengine.api.IPosition;
import org.efaps.promotionengine.process.ProcessData;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DateCondition
    extends AbstractCondition
{

    private List<DateConditionRange> ranges;

    private ZoneId zoneId = ZoneId.systemDefault();

    @JsonIgnore
    public ZoneId getZoneId()
    {
        return zoneId;
    }

    public void setZoneId(ZoneId zoneId)
    {
        this.zoneId = zoneId;
    }

    public List<DateConditionRange> getRanges()
    {
        return ranges;
    }

    public void setRanges(List<DateConditionRange> ranges)
    {
        this.ranges = ranges;
    }

    @Override
    public boolean isMet(final ProcessData process)
    {
        final var date = LocalDate.now(zoneId);
        for (final var range : ranges) {
            if ((date.isAfter(range.getStartDate()) || date.isEqual(range.getStartDate()))
                            && (date.isBefore(range.getEndDate()) || date.isEqual(range.getEndDate()))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<IPosition> evalPositions(final ProcessData process)
    {
        return new ArrayList<>();
    }

    @Override
    public boolean positionMet(final IPosition position)
    {
        return false;
    }

    public DateCondition addRange(final LocalDate startDate,
                                  final LocalDate endDate)
    {
        if (ranges == null) {
            ranges = new ArrayList<>();
        }
        ranges.add(new DateConditionRange().setStartDate(startDate).setEndDate(endDate));
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
        if (!(obj instanceof final DateCondition condition)) {
            return false;
        }
        return Objects.equals(this.getRanges(), condition.getRanges());
    }

}
