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

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.efaps.promotionengine.process.ProcessData;

public abstract class AbstractKeyValueCondition
    extends AbstractCondition
{

    private final EntryOperator entryOperator = EntryOperator.INCLUDES_ANY;

    private final List<Pair<String, Object>> pairs = new ArrayList<>();

    @Override
    public boolean isMet(final ProcessData process)
    {
        switch (entryOperator) {
            case INCLUDES_ANY: {
                for (final var pair : pairs) {
                    if (process.containstData(pair.getKey())
                                    && Objects.equals(process.getData(pair.getKey()), pair.getValue())) {
                        return true;
                    }
                }
            }
            case INCLUDES_ALL: {
                boolean ret = true;
                for (final var pair : pairs) {
                    ret = process.containstData(pair.getKey())
                                    && Objects.equals(process.getData(pair.getKey()), pair.getValue());
                }
                return ret;
            }
            case EXCLUDES: {
                for (final var pair : pairs) {
                    if (process.containstData(pair.getKey())
                                    && Objects.equals(process.getData(pair.getKey()), pair.getValue())) {
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
                               final Object object)
    {
        this.pairs.add(new ImmutablePair<>(key, object));
    }
}
