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
package org.efaps.promotionengine.action;

import java.util.List;

import org.efaps.promotionengine.pojo.Position;
import org.efaps.promotionengine.process.ProcessData;

public class PercentageDocDiscountAction extends AbstractPercentageAction
{

    @Override
    public void run(final ProcessData process,
                    final List<Position> position)
    {
        process.getDocument().setCrossTotal(calculate(process.getDocument().getCrossTotal()));
    }

}
