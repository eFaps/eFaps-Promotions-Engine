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

import org.testng.Assert;
import org.testng.annotations.Test;

public class ProgramActionTest
{
    @Test
    public void equalsTest() {
        final var action = new ProgramAction();
        Assert.assertTrue(action.equals(action));
        Assert.assertFalse(action.equals("different"));

        Assert.assertTrue(action.equals(new ProgramAction()));

        action.setClassName("java-class-name");
        final var action2 = new ProgramAction().setClassName("java-class-name");
        Assert.assertTrue(action.equals(action2));
    }
}
