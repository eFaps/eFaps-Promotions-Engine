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

import org.efaps.promotionengine.process.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProgramAction
{
    private static final Logger LOG = LoggerFactory.getLogger(Engine.class);

    private String className;


    public String getClassName()
    {
        return className;
    }


    public ProgramAction setClassName(final String className)
    {
        this.className = className;
        return this;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof final ProgramAction other)) {
            return false;
        }

        if (this.getClassName() == null && other.getClassName() == null) {
            return true;
        }

        return this.getClassName() != null && other.getClassName() != null
                        && this.getClassName().compareTo(other.getClassName()) == 0;
    }

}
