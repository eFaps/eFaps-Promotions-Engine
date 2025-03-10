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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.iterators.PermutationIterator;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.efaps.promotionengine.PromotionsConfiguration;
import org.efaps.promotionengine.api.IDocument;
import org.efaps.promotionengine.api.IPosition;
import org.efaps.promotionengine.api.IPromotionsConfig;
import org.efaps.promotionengine.condition.ICondition;
import org.efaps.promotionengine.pojo.Document;
import org.efaps.promotionengine.pojo.Position;
import org.efaps.promotionengine.promotion.Promotion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Engine
{

    private static final Logger LOG = LoggerFactory.getLogger(Engine.class);

    private final IPromotionsConfig config;

    private ProcessData processData;

    public Engine()
    {
        this(new PromotionsConfiguration());
    }

    public Engine(final IPromotionsConfig config)
    {
        this.config = config;
    }

    public IPromotionsConfig getConfig()
    {
        return config;
    }

    public void apply(final IDocument document,
                      final List<Promotion> promotions)
    {
        LOG.info("Applying {} Promotions: {} against Document: {}", promotions.size(), promotions, document);
        final var currentPromotions = promotions.stream()
                        .filter(promo -> (promo.getStartDateTime().isBefore(getConfig().getEvaluationDateTime())
                                        && promo.getEndDateTime().isAfter(getConfig().getEvaluationDateTime())))
                        .collect(Collectors.toList());

        if (EngineRule.MOSTDISCOUNT.equals(config.getEngineRule())) {
            LOG.info("MOSTDISCOUNT active, checking {} discounts against the document", currentPromotions.size());
            // check to reduce the possible Promotions by testing them
            // individually
            final MultiValuedMap<Integer, Promotion> possiblePromotions = new ArrayListValuedHashMap<>();
            final List<Promotion> stackables = new ArrayList<>();

            currentPromotions.forEach(promotion -> {
                getProcessData().setDocument(document.clone());
                getProcessData().setCurrentPromotion(promotion);
                if (meetsConditions(promotion.getSourceConditions())
                                || meetsConditions(promotion.getTargetConditions())) {
                    LOG.debug("Promotion {} meets source or target conditions", promotion.getOid());
                    if (promotion.isStackable()) {
                        stackables.add(promotion);
                    } else {
                        getProcessData().setDocument(document.clone());
                        // evaluate priorities
                        applyInternal(Collections.singletonList(promotion));
                        LOG.debug("ProcessDoc: {}", getProcessData().getDocument());
                        // check if docDiscount
                        if (getProcessData().getDocument().getPromotionOids().isEmpty()) {
                            final Set<Integer> indexes = new HashSet<>();
                            BigDecimal discountedAmount = BigDecimal.ZERO;
                            for (final var pos : getProcessData().getDocument().getPositions()) {
                                if (!((IPosition) pos).getPromotionDetails().isEmpty()) {
                                    indexes.add(pos.getIndex());
                                    discountedAmount = discountedAmount
                                                    .add(pos.getNetUnitPrice().multiply(pos.getQuantity()));
                                }
                            }
                            if (BigDecimal.ZERO.compareTo(discountedAmount) < 0) {
                                BigDecimal orginalAmount = BigDecimal.ZERO;
                                for (final var pos : document.getPositions()) {
                                    if (indexes.contains(pos.getIndex())) {
                                        orginalAmount = orginalAmount
                                                        .add(pos.getNetUnitPrice().multiply(pos.getQuantity()));
                                    }
                                }
                                final var discount = orginalAmount.subtract(discountedAmount);
                                LOG.info("Promotion {} results in orginal amount: {} - discounted amount: {} -> discount amount: {}",
                                                promotion.getOid(), orginalAmount, discountedAmount, discount);
                                possiblePromotions.put(discount.multiply(new BigDecimal(100)).intValue(), promotion);
                            } else {
                                LOG.info("Promotion {} does not discount", promotion);
                            }
                        } else {
                            LOG.warn("Not implemened: {}", promotion);
                            getProcessData().getDocument().getDocDiscount();
                        }
                    }
                }
            });
            LOG.info("Found {} possible promotions of {}", possiblePromotions.size(), currentPromotions.size());

            getProcessData().setDocument(document);

            final var sorted = new ArrayList<Promotion>();
            possiblePromotions.asMap().entrySet()
                            .stream()
                            .sorted(Map.Entry.comparingByKey())
                            .forEach(entry -> {
                                sorted.addAll(entry.getValue());
                            });
            Collections.reverse(sorted);
            LOG.info("Promotion hierarchy: {}", sorted.stream().map(Promotion::getOid).toList());

            permutation(document, sorted, stackables);
        } else {
            // sort that highest number first, and the stackable equally sorted
            // afterwards
            final var sorted = currentPromotions.stream()
                            .sorted(Comparator.comparing(Promotion::isStackable)
                                            .thenComparing(Comparator.comparing(Promotion::getPriority).reversed()))
                            .toList();
            applyInternal(sorted);
        }
    }

    private void permutation(final IDocument document,
                             final List<Promotion> sorted,
                             final List<Promotion> stackables)
    {
        IDocument mostDiscountDoc = null;
        BigDecimal mostDiscount = BigDecimal.ZERO;

        final List<Promotion> permutables = new ArrayList<>();
        final List<Promotion> overhang = new ArrayList<>();
        int idx = 0;
        for (final var promo : sorted) {
            if (idx < 5) {
                permutables.add(promo);
            } else {
                overhang.add(promo);
            }
            idx++;
        }

        int y = 0;

        final PermutationIterator<Promotion> permutationIterator = new PermutationIterator<>(permutables);
        while (permutationIterator.hasNext()) {
            LOG.info("Permutation Nr: {}", y++);
            final var currentDoc = document.clone();
            getProcessData().setDocument(currentDoc);
            final var currentPermutation = permutationIterator.next();

            final var applieable = Stream.of(currentPermutation, overhang, stackables).flatMap(Collection::stream)
                            .toList();
            applyInternal(applieable);
            getProcessData().setDocument(document);

            BigDecimal currentDiscount = currentDoc.getDocDiscount() != null ? currentDoc.getDocDiscount()
                            : BigDecimal.ZERO;

            for (final var pos : currentDoc.getPositions()) {
                for (final var detail : ((Position) pos).getPromotionDetails()) {
                    currentDiscount = currentDiscount.add(pos.getQuantity().multiply(detail.getNetUnitDiscount()));
                }
            }

            if (mostDiscountDoc == null || mostDiscount.compareTo(currentDiscount) < 0) {
                mostDiscountDoc = currentDoc;
                mostDiscount = currentDiscount;
            }
        }
        if (mostDiscountDoc != null && mostDiscount.compareTo(BigDecimal.ZERO) != 0) {
            ((Document) document).setPositions(mostDiscountDoc.getPositions().stream().toList());
        }
    }

    private void applyInternal(final List<Promotion> promotions)
    {
        final var promoIter = promotions.iterator();
        Promotion promotion = null;
        if (promoIter.hasNext()) {
            promotion = promoIter.next();
        }
        int counter = 0;
        while (promotion != null) {
            getProcessData().setCurrentPromotion(promotion);
            getProcessData().setStep(Step.SOURCECONDITION);
            if (!promotion.hasSource() || meetsConditions(promotion.getSourceConditions())) {
                if (!runActions(promotion)) {
                    if (promoIter.hasNext()) {
                        counter = 0;
                        promotion = promoIter.next();
                        getProcessData().getPositionsUsedForSouce().clear();
                    } else {
                        promotion = null;
                    }
                } else {
                    counter++;
                    if (promotion.getMax() > 0 && counter >= promotion.getMax()) {
                        counter = 0;
                        if (promoIter.hasNext()) {
                            promotion = promoIter.next();
                            getProcessData().getPositionsUsedForSouce().clear();
                        } else {
                            promotion = null;
                        }
                    }
                }
            } else if (promoIter.hasNext()) {
                counter = 0;
                promotion = promoIter.next();
                getProcessData().getPositionsUsedForSouce().clear();
            } else {
                promotion = null;
            }
        }
    }

    public boolean runActions(final Promotion promotion)
    {
        boolean actionRun = false;
        getProcessData().setStep(Step.TARGETCONDITION);
        for (final var action : promotion.getActions()) {
            actionRun = action.run(processData);
        }
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
        processData.setPromotionsConfig(getConfig());
        return processData;
    }

    public Engine withProcessData(final ProcessData processData)
    {
        this.processData = processData;
        return this;
    }
}
