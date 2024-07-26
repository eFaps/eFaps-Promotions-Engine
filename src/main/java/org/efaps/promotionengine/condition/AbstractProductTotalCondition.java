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
import java.util.function.Predicate;

import org.efaps.promotionengine.api.IPosition;

public abstract class AbstractProductTotalCondition
    extends AbstractTotalCondition
{

    private List<String> products = new ArrayList<>();

    public void setProducts(final List<String> products)
    {
        this.products = products;
    }

    public List<String> getProducts()
    {
        return products;
    }

    public AbstractProductTotalCondition addProduct(final String product)
    {
        if (this.products == null) {
            this.products = new ArrayList<>();
        }
        this.products.add(product);
        return this;
    }

    @Override
    protected
    Predicate<IPosition> getFilterPredicate()
    {
        return position -> getProducts().contains(position.getProductOid());
    }
}
