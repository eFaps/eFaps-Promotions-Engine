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

import java.math.BigDecimal;

import org.testng.Assert;
import org.testng.annotations.Test;

public class FixedAmountActionTest
{
    @Test
    public void equalsTest() {
        final var obj1 = new FixedAmountAction();
        Assert.assertTrue(obj1.equals(obj1));
        Assert.assertFalse(obj1.equals("different"));

        Assert.assertTrue(obj1.equals(new FixedAmountAction()));

        obj1.setAmount(new BigDecimal(10));
        final var obj2 = new FixedAmountAction().setAmount(new BigDecimal(10));
        Assert.assertTrue(obj1.equals(obj2));
    }
}
