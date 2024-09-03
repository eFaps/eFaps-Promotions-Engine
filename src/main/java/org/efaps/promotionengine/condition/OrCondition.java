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

import org.efaps.promotionengine.api.IPosition;
import org.efaps.promotionengine.process.ProcessData;

public class OrCondition
    extends AbstractAggregationCondition
{

    @Override
    public boolean isMet(final ProcessData process)
    {
        boolean ret = false;
        for (final var condition : getConditions()) {
            if (condition.isMet(process)) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    @Override
    public List<IPosition> evalPositions(final ProcessData process)
    {
        final List<IPosition> positions = new ArrayList<>();
        for (final var condition : getConditions()) {
            positions.addAll(condition.evalPositions(process));
        }
        return positions;
    }

    @Override
    public boolean positionMet(final IPosition position)
    {
        boolean ret = false;
        for (final var condition : getConditions()) {
            if (condition.positionMet(position)) {
                ret = true;
                break;
            }
        }
        return ret;
    }
}
