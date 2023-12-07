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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ProductFamilyConditionEntry
{

    private String productFamilyOid;

    private List<String> products;

    public List<String> getProducts()
    {
        return products;
    }

    public ProductFamilyConditionEntry setProducts(List<String> products)
    {
        this.products = products;
        return this;
    }

    public ProductFamilyConditionEntry addProduct(final String product)
    {
        if (this.products == null) {
            this.products = new ArrayList<>();
        }
        this.products.add(product);
        return this;
    }

    public String getProductFamilyOid()
    {
        return productFamilyOid;
    }

    public ProductFamilyConditionEntry setProductFamilyOid(String productFamilyOid)
    {
        this.productFamilyOid = productFamilyOid;
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
        if (!(obj instanceof final ProductFamilyConditionEntry entry)) {
            return false;
        }
        return Objects.equals(this.productFamilyOid, entry.productFamilyOid)
                        && Objects.equals(this.products, entry.products);
    }
}
