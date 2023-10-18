package org.efaps.promotionengine.promotion;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.efaps.promotionengine.action.IAction;
import org.efaps.promotionengine.condition.ICondition;

public class Promotion
{

    private final String oid;

    private final String name;

    private final String description;

    private final int priority;

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
        this.priority = builder.priority;
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

    public int getPriority()
    {
        return priority;
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
        return targetConditions;
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

    public static Builder builder()
    {
        return new Builder();
    }

    public static final class Builder
    {

        private String oid;
        private String name;
        private String description;
        private int priority;
        private OffsetDateTime startDateTime;
        private OffsetDateTime endDateTime;
        private List<ICondition> sourceConditions = Collections.emptyList();
        private List<ICondition> targetConditions = Collections.emptyList();
        private List<IAction> actions = Collections.emptyList();

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

        public Builder withPriority(int priority)
        {
            this.priority = priority;
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

        public Builder withTargetConditions(List<ICondition> targetConditions)
        {
            this.targetConditions = targetConditions;
            return this;
        }

        public Builder withActions(List<IAction> actions)
        {
            this.actions = actions;
            return this;
        }

        public Promotion build()
        {
            return new Promotion(this);
        }
    }
}
