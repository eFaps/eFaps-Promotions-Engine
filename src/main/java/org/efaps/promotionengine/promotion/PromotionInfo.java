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
package org.efaps.promotionengine.promotion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import org.efaps.abacus.api.ICalcPosition;
import org.efaps.abacus.api.IConfig;
import org.efaps.abacus.pojo.CalcDocument;
import org.efaps.abacus.pojo.CalcPosition;
import org.efaps.promotionengine.api.IDocument;
import org.efaps.promotionengine.api.IPromotionDetail;
import org.efaps.promotionengine.api.IPromotionInfo;
import org.efaps.promotionengine.dto.PromotionDetailDto;
import org.efaps.promotionengine.dto.PromotionInfoDto;
import org.efaps.promotionengine.pojo.Position;

public class PromotionInfo
{

    public static IPromotionInfo evalPromotionInfo(final IConfig config,
                                                   final IDocument originalDoc,
                                                   final IDocument promoDoc)
    {
        PromotionInfoDto info = null;
        if (originalDoc.getCrossTotal().compareTo(promoDoc.getCrossTotal()) != 0) {
            final List<IPromotionDetail> details = new ArrayList<>();
            for (final ICalcPosition position : promoDoc.getPositions()) {
                final var promoDocPos = (Position) position;
                for (final var detail : promoDocPos.getPromotionDetails()) {
                    if (detail.getNetUnitBase() != null) {
                        final var calculator = new org.efaps.abacus.Calculator(config);

                        final var detailCalcDoc = new CalcDocument();
                        final var calcPos = new CalcPosition()
                                        .setQuantity(promoDocPos.getQuantity())
                                        .setNetUnitPrice(detail.getNetUnitBase())
                                        .setProductOid(promoDocPos.getProductOid())
                                        .setTaxes(promoDocPos.getTaxes());
                        detailCalcDoc.addPosition(calcPos);
                        calculator.calc(detailCalcDoc);

                        final var netBase = detailCalcDoc.getPositions().get(0).getNetPrice();
                        final var crossUnitPriceBase = detailCalcDoc.getPositions().get(0).getCrossUnitPrice();
                        final var crossPriceBase = detailCalcDoc.getPositions().get(0).getCrossPrice();
                        calcPos.setNetUnitPrice(detail.getNetUnitBase().subtract(detail.getNetUnitDiscount()));
                        calculator.calc(detailCalcDoc);
                        final var netPrice = detailCalcDoc.getPositions().get(0).getNetPrice();
                        final var crossUnitPrice = detailCalcDoc.getPositions().get(0).getCrossUnitPrice();
                        final var crossPrice = detailCalcDoc.getPositions().get(0).getCrossPrice();

                        details.add(PromotionDetailDto.builder()
                                        .withPositionIndex(position.getIndex())
                                        .withNetUnitDiscount(detail.getNetUnitDiscount())
                                        .withNetUnitBase(detail.getNetUnitBase())
                                        .withNetDiscount(netBase.subtract(netPrice))
                                        .withNetBase(netBase)
                                        .withCrossDiscount(crossPriceBase.subtract(crossPrice))
                                        .withCrossUnitDiscount(crossUnitPriceBase.subtract(crossUnitPrice))
                                        .withPromotionOid(detail.getPromotionOid())
                                        .build());
                    }
                }
            }

            var docCrossDiscount = promoDoc.getDocDiscount() != null ? promoDoc.getDocDiscount() : BigDecimal.ZERO;
            docCrossDiscount = docCrossDiscount.add(details.stream()
                            .map(IPromotionDetail::getCrossDiscount)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add));

            final var docNetDiscount = details.stream()
                            .map(IPromotionDetail::getNetDiscount)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

            final var promotionOids = new HashSet<>(promoDoc.getPromotionOids());
            promotionOids.addAll(details.stream()
                            .map(IPromotionDetail::getPromotionOid)
                            .filter(Objects::nonNull)
                            .toList());

            info = PromotionInfoDto.builder()
                            .withNetTotalDiscount(docNetDiscount)
                            .withCrossTotalDiscount(docCrossDiscount)
                            .withDetails(details)
                            .withPromotionOids(promotionOids)
                            .build();
        }
        return info;
    }
}
