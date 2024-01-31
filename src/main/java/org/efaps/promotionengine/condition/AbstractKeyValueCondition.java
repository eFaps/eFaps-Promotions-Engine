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
import java.util.Objects;

import org.efaps.promotionengine.process.ProcessData;

public abstract class AbstractKeyValueCondition<T, S>
    extends AbstractCondition
{

    private EntryOperator entryOperator = EntryOperator.INCLUDES_ANY;

    private List<KeyValueEntry<S>> entries = new ArrayList<>();

    public void setEntries(final List<KeyValueEntry<S>> entries)
    {
        this.entries = entries;
    }

    public List<KeyValueEntry<S>> getEntries()
    {
        return entries;
    }

    public EntryOperator getEntryOperator()
    {
        return entryOperator;
    }

    @SuppressWarnings("unchecked")
    public T setEntryOperator(final EntryOperator entryOperator)
    {
        this.entryOperator = entryOperator;
        return (T) this;
    }

    @Override
    public boolean isMet(final ProcessData process)
    {
        switch (entryOperator) {
            case INCLUDES_ANY: {
                for (final var entry : entries) {
                    if (process.containstData(entry.getKey())
                                    && Objects.equals(process.getData(entry.getKey()), entry.getValue())) {
                        return true;
                    }
                }
            }
            case INCLUDES_ALL: {
                boolean ret = true;
                for (final var entry : entries) {
                    ret = process.containstData(entry.getKey())
                                    && Objects.equals(process.getData(entry.getKey()), entry.getValue());
                }
                return ret;
            }
            case EXCLUDES: {
                for (final var entry : entries) {
                    if (process.containstData(entry.getKey())
                                    && Objects.equals(process.getData(entry.getKey()), entry.getValue())) {
                        return false;
                    }
                }
                return true;
            }
            default:
        }
        return false;
    }

    protected void addKeyValue(final String key,
                               final S value)
    {
        this.entries.add(new KeyValueEntry<S>().setKey(key).setValue(value));
    }
}
