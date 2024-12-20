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
package org.efaps.promotionengine;

import java.time.OffsetDateTime;

import org.efaps.promotionengine.api.IPromotionsConfig;
import org.efaps.promotionengine.process.EngineRule;

public class PromotionsConfiguration
    implements IPromotionsConfig
{

    private EngineRule engineRule;

    private OffsetDateTime evaluationDateTime;

    @Override
    public EngineRule getEngineRule()
    {
        return engineRule;
    }

    public PromotionsConfiguration setEngineRule(EngineRule engineRule)
    {
        this.engineRule = engineRule;
        return this;
    }

    @Override
    public OffsetDateTime getEvaluationDateTime()
    {
        if (evaluationDateTime == null) {
            return IPromotionsConfig.super.getEvaluationDateTime();
        }
        return evaluationDateTime;
    }

    public void setEvaluationDateTime(final OffsetDateTime evaluationDateTime)
    {
        this.evaluationDateTime = evaluationDateTime;
    }

}
