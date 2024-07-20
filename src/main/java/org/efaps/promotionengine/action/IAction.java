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

import java.util.List;

import org.efaps.promotionengine.api.IAnnotated;
import org.efaps.promotionengine.api.IPosition;
import org.efaps.promotionengine.process.ProcessData;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
                @JsonSubTypes.Type(value = PercentageDiscountAction.class, name = "PercentageDiscountAction"),
                @JsonSubTypes.Type(value = PercentageDocDiscountAction.class, name = "PercentageDocDiscountAction"),
                @JsonSubTypes.Type(value = FixedDocDiscountAction.class, name = "FixedDocDiscountAction"),
                @JsonSubTypes.Type(value = ProgramAction.class, name = "ProgramAction"),
                @JsonSubTypes.Type(value = FixedAmountAction.class, name = "FixedAmountAction")
})
public interface IAction
    extends IAnnotated
{

    void run(final ProcessData process,
             final List<IPosition> positions);

}
