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

import org.efaps.promotionengine.api.IDocument;
import org.efaps.promotionengine.api.IPosition;
import org.efaps.promotionengine.api.IPromotionDetail;
import org.efaps.promotionengine.api.IPromotionInfo;
import org.efaps.promotionengine.dto.PromotionDetailDto;
import org.efaps.promotionengine.dto.PromotionInfoDto;

public class PromotionInfo
{

    public static IPromotionInfo evalPromotionInfo(final IDocument originalDoc,
                                                   final IDocument promoDoc)
    {
        PromotionInfoDto info = null;
        if (originalDoc.getCrossTotal().compareTo(promoDoc.getCrossTotal()) != 0) {
            final var originalDocPosIter = originalDoc.getPositions().iterator();
            final var promoDocPosIter = promoDoc.getPositions().iterator();

            final List<IPromotionDetail> details = new ArrayList<>();
            while (originalDocPosIter.hasNext()) {
                final IPosition originalDocPos = (IPosition) originalDocPosIter.next();
                final IPosition promoDocPos = (IPosition) promoDocPosIter.next();

                if (promoDocPos.getPromotionOid() != null) {
                    final var netUnitDiscount = originalDocPos.getNetUnitPrice()
                                    .subtract(promoDocPos.getNetUnitPrice());
                    final var netDiscount = originalDocPos.getNetPrice().subtract(promoDocPos.getNetPrice());
                    final var crossDiscount = originalDocPos.getCrossPrice().subtract(promoDocPos.getCrossPrice());
                    details.add(PromotionDetailDto.builder()
                                    .withIndex(promoDocPos.getIndex())
                                    .withNetUnitDiscount(netUnitDiscount)
                                    .withNetDiscount(netDiscount)
                                    .withCrossDiscount(crossDiscount)
                                    .withPromotionOid(promoDocPos.getPromotionOid())
                                    .build());
                } else {
                    details.add(PromotionDetailDto.builder().build());
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
                            .filter(Objects::nonNull).toList());

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
