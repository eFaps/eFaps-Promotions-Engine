/*
 * Copyright 2003 - 2023 The eFaps Team
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
 *
 */
package org.efaps.promotionengine.condition;

import java.util.List;

import org.efaps.promotionengine.api.IPosition;
import org.efaps.promotionengine.process.ProcessData;

public class StoreCondition
    extends AbstractKeyValueCondition<StoreCondition>
{
    public static String KEY = "StoreCondition-Identifier";

    @Override
    public List<IPosition> evalPositions(final ProcessData process)
    {
        return null;
    }

    @Override
    public boolean positionMet(final IPosition position)
    {
        return false;
    }

    public StoreCondition addIdentifier(final String identifier)
    {
        this.addKeyValue(KEY, identifier);
        return this;
    }

}
