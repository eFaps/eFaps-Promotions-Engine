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
package org.efaps.promotionengine;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.efaps.abacus.api.IConfig;
import org.efaps.promotionengine.api.IDocument;
import org.efaps.promotionengine.api.IPromotionsConfig;
import org.efaps.promotionengine.process.Engine;
import org.efaps.promotionengine.process.ProcessData;
import org.efaps.promotionengine.promotion.Promotion;
import org.efaps.promotionengine.promotion.PromotionInfo;

public class Calculator
    extends org.efaps.abacus.Calculator
{

    public Calculator(final IConfig config)
    {
        super(config);
    }

    public void calc(final IDocument document)
    {
        calc(document, null);
    }

    public void calc(final IDocument document,
                     final List<Promotion> promotions)
    {
        calc(document, promotions, null, new PromotionsConfiguration());
    }

    public void calc(final IDocument document,
                     final List<Promotion> promotions,
                     final Map<String, Object> data,
                     final IPromotionsConfig promotionsConfig)
    {
        super.calc(document);

        if (CollectionUtils.isNotEmpty(promotions)) {
            final var promoDoc = document.clone();
            new Engine(promotionsConfig).withProcessData(new ProcessData(getConfig(), promoDoc, data)).apply(promoDoc,
                            promotions);
            super.calc(promoDoc);
            if (promoDoc.getDocDiscount() != null) {
                promoDoc.setCrossTotal(promoDoc.getCrossTotal().subtract(promoDoc.getDocDiscount()));
            }
            final var promoInfo = PromotionInfo.evalPromotionInfo(getConfig(), document, promoDoc);
            document.setPromotionInfo(promoInfo);

            // sync the information into the original document
            document.updateWith(promoDoc);
        }
    }
}
