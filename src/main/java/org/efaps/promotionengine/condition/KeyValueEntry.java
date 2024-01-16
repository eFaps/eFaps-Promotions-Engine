/*
 * Copyright 2003 - 2024 The eFaps Team
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

import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class KeyValueEntry<T>
{

    private String key;

    private T value;

    public String getKey()
    {
        return key;
    }

    public KeyValueEntry<T> setKey(String key)
    {
        this.key = key;
        return this;
    }

    public Object getValue()
    {
        return value;
    }

    public KeyValueEntry<T> setValue(T value)
    {
        this.value = value;
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
        if (!(obj instanceof final KeyValueEntry keyValue)) {
            return false;
        }
        return Objects.equals(this.getKey(), keyValue.getKey())
                        && Objects.equals(this.getValue(), keyValue.getValue());
    }
}
