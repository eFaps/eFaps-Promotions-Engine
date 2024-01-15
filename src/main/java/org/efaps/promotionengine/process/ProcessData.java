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
package org.efaps.promotionengine.process;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.efaps.promotionengine.api.IDocument;
import org.efaps.promotionengine.api.IPosition;
import org.efaps.promotionengine.promotion.Promotion;

public class ProcessData
{

    private final IDocument document;

    private final Map<String, Object> data;

    private Step step;

    private final Set<IPosition> positionsUsedForSouce = new HashSet<>();

    private Promotion currentPromotion;

    public ProcessData(final IDocument document,
                       final Map<String, Object> data)
    {
        this.document = document;
        this.data = data == null ? new HashMap<>() : data;
        this.step = Step.SOURCECONDITION;
    }

    public Promotion getCurrentPromotion()
    {
        return currentPromotion;
    }

    public ProcessData setCurrentPromotion(Promotion currentPromotion)
    {
        this.currentPromotion = currentPromotion;
        return this;
    }

    public IDocument getDocument()
    {
        return document;
    }

    public Step getStep()
    {
        return step;
    }

    public ProcessData setStep(Step step)
    {
        this.step = step;
        return this;
    }

    public Set<IPosition> getPositionsUsedForSouce()
    {
        return positionsUsedForSouce;
    }

    public void registerConditionMet(final IPosition position)
    {
        switch (step) {
            case SOURCECONDITION:
                positionsUsedForSouce.add(position);
                break;
            case TARGETCONDITION:
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + step);
        }

    }

    public Map<String, Object> getData()
    {
        return data;
    }

    public Object getData(final String key)
    {
        return getData().get(key);
    }

    public boolean containstData(final String key)
    {
        return getData().containsKey(key);
    }


    public ProcessData addData(final String key,
                               final Object object)
    {
        getData().put(key, object);
        return this;
    }

}
