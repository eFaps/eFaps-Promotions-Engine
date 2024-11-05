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

public class StackCondition
    extends AbstractCondition
{

    @Override
    public boolean isMet(final ProcessData process)
    {
        return true;
    }

    @Override
    public List<IPosition> evalPositions(ProcessData process)
    {
        return new ArrayList<>();
    }

    @Override
    public boolean positionMet(IPosition position)
    {
        return false;
    }

}
