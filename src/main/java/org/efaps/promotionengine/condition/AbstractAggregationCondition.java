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

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAggregationCondition
    extends AbstractCondition
{

    private List<ICondition> conditions;

    public List<ICondition> getConditions()
    {
        return conditions;
    }

    public AbstractAggregationCondition setConditions(final List<ICondition> conditions)
    {
        this.conditions = conditions;
        return this;
    }

    public AbstractAggregationCondition addCondition(final ICondition condition)
    {
        if (this.conditions == null) {
            this.conditions = new ArrayList<>();
        }
        this.conditions.add(condition);
        return this;
    }
}
