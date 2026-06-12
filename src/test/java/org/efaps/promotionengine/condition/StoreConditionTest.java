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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;

import org.efaps.promotionengine.process.ProcessData;
import org.testng.annotations.Test;

public class StoreConditionTest
{
    @Test
    public void conditionAnyIsNotMet()
    {
        final var storeCondition = new StoreCondition()
                        .addIdentifier("Store1")
                        .addIdentifier("Store2");

        final var map = new HashMap<String, Object>();
        map.put(StoreCondition.KEY, "AnotherStore");
        final var processData = new ProcessData(null, null, map);
        assertFalse(storeCondition.isMet(processData));
    }

    @Test
    public void conditionAnyIsMet()
    {
        final var storeCondition = new StoreCondition()
                        .addIdentifier("Store1")
                        .addIdentifier("Store2");

        final var map = new HashMap<String, Object>();
        map.put(StoreCondition.KEY, "Store1");
        final var processData = new ProcessData(null, null, map);
        assertTrue(storeCondition.isMet(processData));
    }

    @Test
    public void conditionExcludeIsNotMet()
    {
        final var storeCondition = new StoreCondition()
                        .setEntryOperator(EntryOperator.EXCLUDES)
                        .addIdentifier("Store1")
                        .addIdentifier("Store2");

        final var map = new HashMap<String, Object>();
        map.put(StoreCondition.KEY, "AnotherStore");
        final var processData = new ProcessData(null, null, map);
        assertTrue(storeCondition.isMet(processData));
    }

    @Test
    public void conditionExcludeIsMet()
    {
        final var storeCondition = new StoreCondition()
                        .setEntryOperator(EntryOperator.EXCLUDES)
                        .addIdentifier("Store1")
                        .addIdentifier("Store2");

        final var map = new HashMap<String, Object>();
        map.put(StoreCondition.KEY, "Store1");
        final var processData = new ProcessData(null, null, map);
        assertFalse(storeCondition.isMet(processData));
    }

}
