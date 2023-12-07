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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ProductFamilyCondition
    extends AbstractProductCondition<ProductFamilyCondition>
{

    private List<ProductFamilyConditionEntry> entries;

    public List<ProductFamilyConditionEntry> getEntries()
    {
        return entries;
    }

    public ProductFamilyCondition setEntries(List<ProductFamilyConditionEntry> entries)
    {
        this.entries = entries;
        return this;
    }

    public ProductFamilyCondition addEntry(final ProductFamilyConditionEntry entry)
    {
        if (this.entries == null) {
            this.entries = new ArrayList<>();
        }
        this.entries.add(entry);
        return this;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof final ProductFamilyCondition condition)) {
            return false;
        }
        return Objects.equals(this.getEntryOperator(), condition.getEntryOperator())
                        && Objects.equals(this.getPositionQuantity(), condition.getPositionQuantity())
                        && Objects.equals(this.entries, condition.entries);
    }

    @Override
    public List<String> getProducts()
    {
        return entries.stream().flatMap(entry -> entry.getProducts().stream()).collect(Collectors.toList());
    }
}
