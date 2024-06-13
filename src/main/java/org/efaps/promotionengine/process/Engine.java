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
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.iterators.PermutationIterator;
import org.efaps.promotionengine.PromotionsConfiguration;
import org.efaps.promotionengine.api.IDocument;
import org.efaps.promotionengine.api.IPosition;
import org.efaps.promotionengine.api.IPromotionDetail;
import org.efaps.promotionengine.api.IPromotionDoc;
import org.efaps.promotionengine.api.IPromotionsConfig;
import org.efaps.promotionengine.condition.ICondition;
import org.efaps.promotionengine.dto.PromotionDetailDto;
import org.efaps.promotionengine.dto.PromotionInfoDto;
import org.efaps.promotionengine.pojo.Document;
import org.efaps.promotionengine.promotion.Promotion;
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
                final var currentDiscount = currentDoc.getPositions().stream()
                                .map(pos -> (IPosition) pos)
                                .map(IPosition::getDiscount)
                                .filter(Objects::nonNull)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                if (mostDiscountDoc == null || mostDiscount.compareTo(currentDiscount) < 0) {
                    mostDiscountDoc = currentDoc;
                    mostDiscount = currentDiscount;
                }
            }
            if (mostDiscountDoc != null && mostDiscount.compareTo(BigDecimal.ZERO) != 0) {
                ((Document) document).setPositions(mostDiscountDoc.getPositions().stream().toList());
                evalPromoInfo(document);
            }
        } else {
            // sort that highest number first
            Collections.sort(currentPromotions, (promotion0,
                                                 promotion1) -> promotion1.getPriority() - promotion0.getPriority());
            for (final var promotion : currentPromotions) {
                getProcessData().setCurrentPromotion(promotion);
                getProcessData().setStep(Step.SOURCECONDITION);
                if (!promotion.hasSource() || meetsConditions(promotion.getSourceConditions())) {
                    runActions(promotion);
                }
                getProcessData().getPositionsUsedForSouce().clear();
            }
            evalPromoInfo(document);
        }
    }

    private void evalPromoInfo(final IDocument document)
    {
        if (document instanceof IPromotionDoc) {
            final var details = document.getPositions().stream()
                            .map(pos -> (IPosition) pos)
                            .map(pos -> PromotionDetailDto.builder()
                                            .withIndex(pos.getIndex())
                                            .withDiscount(pos.getDiscount())
                                            .withPromotionOid(pos.getPromotionOid())
                                            .build())
                            .toList();
            var totalDiscount = details.stream()
                            .map(IPromotionDetail::getDiscount)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (document.getDocDiscount() != null) {
                totalDiscount = totalDiscount.add(document.getDocDiscount());
            }

            final var info = PromotionInfoDto.builder()
                            .withTotalDiscount(totalDiscount)
                            .withDetails(details)
                            .withPromotionOids(((IPromotionDoc) document).getPromotionOids())
                            .build();
            ((IPromotionDoc) document).setPromotionInfo(info);
        }
    }

    public void runActions(final Promotion promotion)
    {
        getProcessData().setStep(Step.TARGETCONDITION);
        if (promotion.hasSource()) {
            if (getProcessData().getDocument() instanceof IPromotionDoc) {
                ((IPromotionDoc) getProcessData().getDocument()).addPromotionOid(promotion.getOid());
            }
            boolean actionRun = false;
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
                    if (getProcessData().getDocument() instanceof IPromotionDoc) {
                        ((IPromotionDoc) getProcessData().getDocument()).addPromotionOid(promotion.getOid());
                    }
                    for (final var action : promotion.getActions()) {
                        action.run(processData, Collections.singletonList(position));
                    }
                }
            }
        }
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
