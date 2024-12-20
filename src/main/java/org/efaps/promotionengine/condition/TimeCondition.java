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

import java.time.OffsetTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.efaps.promotionengine.api.IPosition;
import org.efaps.promotionengine.process.ProcessData;

public class TimeCondition
    extends AbstractCondition
{

    private List<TimeConditionRange> ranges;

    public List<TimeConditionRange> getRanges()
    {
        return ranges;
    }

    public void setRanges(final List<TimeConditionRange> ranges)
    {
        this.ranges = ranges;
    }

    @Override
    public boolean isMet(final ProcessData process)
    {
        final var time = process.getPromotionsConfig().getEvaluationDateTime().toOffsetTime();
        for (final var range : ranges) {
            if (time.isAfter(range.getStartTime()) && time.isBefore(range.getEndTime())) {
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

    public TimeCondition addRange(final OffsetTime startTime,
                                  final OffsetTime endTime)
    {
        if (ranges == null) {
            ranges = new ArrayList<>();
        }
        ranges.add(new TimeConditionRange().setStartTime(startTime).setEndTime(endTime));
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
        if (!(obj instanceof final TimeCondition condition)) {
            return false;
        }
        return Objects.equals(this.getRanges(), condition.getRanges());
    }
}
