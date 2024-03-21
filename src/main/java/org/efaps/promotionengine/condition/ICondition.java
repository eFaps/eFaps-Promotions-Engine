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

import java.util.List;

import org.efaps.promotionengine.api.IAnnotated;
import org.efaps.promotionengine.api.IPosition;
import org.efaps.promotionengine.process.ProcessData;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ProductsCondition.class, name = "ProductsCondition"),
    @JsonSubTypes.Type(value = ProductFamilyCondition.class, name = "ProductFamilyCondition"),
    @JsonSubTypes.Type(value = DocTotalCondition.class, name = "DocTotalCondition"),
    @JsonSubTypes.Type(value = StoreCondition.class, name = "StoreCondition"),
    @JsonSubTypes.Type(value = TimeCondition.class, name = "TimeCondition"),
    @JsonSubTypes.Type(value = WeekdayCondition.class, name = "WeekdayCondition"),
    @JsonSubTypes.Type(value = DateCondition.class, name = "DateCondition")
})
public interface ICondition
    extends IAnnotated
{

    boolean isMet(final ProcessData process);

    List<IPosition> evalPositions(final ProcessData process);

    boolean positionMet(final IPosition position);

}
