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
package org.efaps.promotionengine.promotion;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.efaps.promotionengine.action.IAction;
import org.efaps.promotionengine.condition.ICondition;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = Promotion.Builder.class)
public class Promotion
{

    private final String oid;

    private final String name;

    private final String description;

    private final String label;

    private final int priority;

    private final int max;

    private final OffsetDateTime startDateTime;

    private final OffsetDateTime endDateTime;

    private final List<ICondition> sourceConditions;

    private final List<ICondition> targetConditions;

    private final List<IAction> actions;

    private Promotion(Builder builder)
    {
        this.oid = builder.oid;
        this.name = builder.name;
        this.description = builder.description;
        this.label = builder.label;
        this.priority = builder.priority;
        this.max = builder.max;
        this.startDateTime = builder.startDateTime;
        this.endDateTime = builder.endDateTime;
        this.sourceConditions = builder.sourceConditions;
        this.targetConditions = builder.targetConditions;
        this.actions = builder.actions;
    }

    public String getOid()
    {
        return oid;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public String getLabel()
    {
        return label;
    }

    public int getPriority()
    {
        return priority;
    }

    public int getMax()
    {
        return max;
    }

    public OffsetDateTime getStartDateTime()
    {
        return startDateTime;
    }

    public OffsetDateTime getEndDateTime()
    {
        return endDateTime;
    }

    public List<ICondition> getSourceConditions()
    {
        return sourceConditions;
    }

    public List<ICondition> getTargetConditions()
    {
        return targetConditions.isEmpty() ? sourceConditions : targetConditions;
    }

    public boolean hasSource()
    {
        return !this.sourceConditions.isEmpty();
    }

    public List<IAction> getActions()
    {
        return actions;
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
        if (!(obj instanceof final Promotion promo)) {
            return false;
        }
        return Objects.equals(this.oid, promo.oid)
                        && Objects.equals(this.getActions(), promo.getActions())
                        && Objects.equals(this.getSourceConditions(), promo.getSourceConditions())
                        && Objects.equals(this.getTargetConditions(), promo.getTargetConditions());
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public static final class Builder
    {

        private String oid;
        private String name;
        private String description;
        private String label;
        private int priority;
        private int max;
        private OffsetDateTime startDateTime;
        private OffsetDateTime endDateTime;
        private List<ICondition> sourceConditions = new ArrayList<>();
        private List<ICondition> targetConditions = new ArrayList<>();
        private List<IAction> actions = new ArrayList<>();

        private Builder()
        {
        }

        public Builder withOid(String oid)
        {
            this.oid = oid;
            return this;
        }

        public Builder withName(String name)
        {
            this.name = name;
            return this;
        }

        public Builder withDescription(String description)
        {
            this.description = description;
            return this;
        }

        public Builder withLabel(String label)
        {
            this.label = label;
            return this;
        }

        public Builder withPriority(int priority)
        {
            this.priority = priority;
            return this;
        }

        public Builder withMax(int max)
        {
            this.max = max;
            return this;
        }

        public Builder withStartDateTime(OffsetDateTime startDateTime)
        {
            this.startDateTime = startDateTime;
            return this;
        }

        public Builder withEndDateTime(OffsetDateTime endDateTime)
        {
            this.endDateTime = endDateTime;
            return this;
        }

        public Builder withSourceConditions(List<ICondition> sourceConditions)
        {
            this.sourceConditions = sourceConditions;
            return this;
        }

        public Builder addSourceCondition(final ICondition sourceCondition)
        {
            this.sourceConditions.add(sourceCondition);
            return this;
        }

        public Builder withTargetConditions(List<ICondition> targetConditions)
        {
            this.targetConditions = targetConditions;
            return this;
        }

        public Builder addTargetCondition(final ICondition targetCondition)
        {
            this.targetConditions.add(targetCondition);
            return this;
        }

        public Builder withActions(List<IAction> actions)
        {
            this.actions = actions;
            return this;
        }

        public Builder addAction(final IAction action)
        {
            this.actions.add(action);
            return this;
        }

        public Promotion build()
        {
            return new Promotion(this);
        }
    }
}
