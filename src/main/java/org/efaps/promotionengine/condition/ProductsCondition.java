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

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ProductsCondition
    extends AbstractProductCondition<ProductsCondition>
{

    private Set<String> products;

    @Override
    public Set<String> getProducts()
    {
        return products;
    }

    public ProductsCondition setProducts(final Set<String> products)
    {
        this.products = products;
        return this;
    }

    public ProductsCondition addProduct(final String product)
    {
        if (this.products == null) {
            this.products = new HashSet<>();
        }
        this.products.add(product);
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
        if (!(obj instanceof final ProductsCondition condition)) {
            return false;
        }
        return Objects.equals(this.getEntryOperator(), condition.getEntryOperator())
                        && Objects.equals(this.getPositionQuantity(), condition.getPositionQuantity())
                        && Objects.equals(this.products, condition.products);
    }

}
