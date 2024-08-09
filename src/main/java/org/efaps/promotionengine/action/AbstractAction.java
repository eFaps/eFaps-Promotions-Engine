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

import org.efaps.promotionengine.api.IPosition;
import org.efaps.promotionengine.process.ProcessData;

public abstract class AbstractAction
    implements IAction
{

    private Strategy strategy = Strategy.CHEAPEST;
    private String note;

    public Strategy getStrategy()
    {
        return strategy;
    }

    public AbstractAction setStrategy(final Strategy strategy)
    {
        this.strategy = strategy;
        return this;
    }

    public AbstractAction setNote(final String note)
    {
        this.note = note;
        return this;
    }

    @Override
    public void run(final ProcessData processData)
    {
        processData.setCurrentAction(this);
        final var conditions = processData.getCurrentPromotion().getTargetConditions();
        for (final var condition : conditions) {
            final var positions = condition.evalPositions(processData);
            System.out.println(positions);
            for (final var pos : positions) {
                apply(processData, pos);
            }
        }
    }

    public abstract void apply(final ProcessData process,
                               final IPosition position);

    @Override
    public String getNote()
    {
        return note;
    }
}
