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
package org.efaps.promotionengine.process;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.iterators.PermutationIterator;
import org.efaps.promotionengine.PromotionsConfiguration;
import org.efaps.promotionengine.api.IDocument;
import org.efaps.promotionengine.api.IPromotionsConfig;
import org.efaps.promotionengine.condition.ICondition;
import org.efaps.promotionengine.pojo.Document;
import org.efaps.promotionengine.promotion.Promotion;
import org.efaps.promotionengine.promotion.PromotionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Engine
{

    private final IPromotionsConfig config;

    public Engine()
    {
        this(new PromotionsConfiguration());
    }

    public Engine(IPromotionsConfig config)
    {
        this.config = config;
    }

    private static final Logger LOG = LoggerFactory.getLogger(Engine.class);

    private ProcessData processData;

    public void apply(final IDocument document,
                      final List<Promotion> promotions)
    {
        LOG.info("Applying Promotions: {} to Document: {}", promotions, document);
        final var currentPromotions = promotions.stream()
                        .filter(promo -> (promo.getStartDateTime().isBefore(OffsetDateTime.now())
                                        && promo.getEndDateTime().isAfter(OffsetDateTime.now())))
                        .collect(Collectors.toList());

        if (EngineRule.MOSTDISCOUNT.equals(config.getEngineRule())) {
            IDocument mostDiscountDoc = null;
            BigDecimal mostDiscount = BigDecimal.ZERO;
            final PermutationIterator<Promotion> permutationIterator = new PermutationIterator<>(currentPromotions);
            while (permutationIterator.hasNext()) {
                final var currentDoc = document.clone();
                getProcessData().setDocument(currentDoc);
                final var currentPermutation = permutationIterator.next();
                for (final var promotion : currentPermutation) {
                    getProcessData().setCurrentPromotion(promotion);
                    getProcessData().setStep(Step.SOURCECONDITION);
                    if (!promotion.hasSource() || meetsConditions(promotion.getSourceConditions())) {
                        runActions(promotion);
                    }
                    getProcessData().getPositionsUsedForSouce().clear();
                }
                BigDecimal currentDiscount = BigDecimal.ZERO;
                new org.efaps.abacus.Calculator(getProcessData().getCalculatorConfig()).calc(currentDoc);
                final var promoInfo = PromotionInfo.evalPromotionInfo(document, currentDoc);
                if (promoInfo != null) {
                    currentDiscount = promoInfo.getCrossTotalDiscount();
                }
                if (mostDiscountDoc == null || mostDiscount.compareTo(currentDiscount) < 0) {
                    mostDiscountDoc = currentDoc;
                    mostDiscount = currentDiscount;
                }
            }
            if (mostDiscountDoc != null && mostDiscount.compareTo(BigDecimal.ZERO) != 0) {
                ((Document) document).setPositions(mostDiscountDoc.getPositions().stream().toList());
            }
        } else {
            // sort that highest number first
            Collections.sort(currentPromotions, (promotion0,
                                                 promotion1) -> promotion1.getPriority() - promotion0.getPriority());
            final var promoIter = currentPromotions.iterator();
            Promotion promotion = null;
            if (promoIter.hasNext()) {
                promotion = promoIter.next();
            }
            while (promotion != null) {
                getProcessData().setCurrentPromotion(promotion);
                getProcessData().setStep(Step.SOURCECONDITION);
                if (!promotion.hasSource() || meetsConditions(promotion.getSourceConditions())) {
                    if (!runActions(promotion)) {
                        promotion = null;
                    }
                } else if (promoIter.hasNext()) {
                    promotion = promoIter.next();
                    getProcessData().getPositionsUsedForSouce().clear();
                } else {
                    promotion = null;
                }
            }
        }
    }

    public boolean runActions(final Promotion promotion)
    {
        final boolean actionRun = false;
        getProcessData().setStep(Step.TARGETCONDITION);
        if (promotion.hasSource()) {
            for (final var action : promotion.getActions()) {
                action.run(processData);
            }
        }

        /**
        if (promotion.hasSource()) {
            getProcessData().getDocument().addPromotionOid(promotion.getOid());
            List<IPosition> commonPositions = null;
            for (final var condition : promotion.getTargetConditions()) {
                final var positions = condition.evalPositions(getProcessData());
                if (commonPositions == null) {
                    commonPositions = positions;
                } else {
                    commonPositions.retainAll(positions);
                }
            }
            actionRun = actionRun || CollectionUtils.isNotEmpty(commonPositions);
            for (final var action : promotion.getActions()) {
                action.run(processData, commonPositions);
            }
            if (actionRun) {
                getProcessData().getPositionsUsedForSouce().forEach(pos -> {
                    pos.setPromotionOid(promotion.getOid());
                });
            }
        } else {
            for (final var calcPosition : processData.getDocument().getPositions()) {
                final var position = (IPosition) calcPosition;
                boolean meetsConditions = !position.isBurned();
                if (meetsConditions) {
                    for (final var condition : promotion.getTargetConditions()) {
                        meetsConditions = meetsConditions && condition.positionMet(position);
                    }
                }
                if (meetsConditions) {
                    getProcessData().getDocument().addPromotionOid(promotion.getOid());
                    for (final var action : promotion.getActions()) {
                        action.run(processData, Collections.singletonList(position));
                    }
                }
            }
        }
        **/
        return actionRun;
    }

    public boolean meetsConditions(final List<ICondition> conditions)
    {
        boolean ret = false;
        for (final var condition : conditions) {
            ret = meetsCondition(condition);
        }
        return ret;
    }

    public boolean meetsCondition(final ICondition condition)
    {
        return condition.isMet(getProcessData());
    }

    public ProcessData getProcessData()
    {
        return processData;
    }

    public Engine withProcessData(final ProcessData processData)
    {
        this.processData = processData;
        return this;
    }
}
