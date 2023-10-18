package org.efaps.promotionengine;

import java.util.Collections;
import java.util.List;

import org.efaps.promotionengine.condition.ICondition;
import org.efaps.promotionengine.pojo.Document;
import org.efaps.promotionengine.promotion.Promotion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Engine
{

    private static final Logger LOG = LoggerFactory.getLogger(Engine.class);

    private ProcessData process;

    public void apply(final Document document,
                      final List<Promotion> promotions)
    {
        process = new ProcessData(document);
        LOG.info("Applying Promotions: {} to Document: {}", promotions, document);
        Collections.sort(promotions, (promotion0,
                                      promotion1) -> promotion0.getPriority() - promotion1.getPriority());
        for (final var promotion : promotions) {
            if (!promotion.hasSource() || meetsConditions(promotion.getSourceConditions())) {
                runActions(promotion);
            }
        }
    }

    public void runActions(final Promotion promotion)
    {
        process.setStep(Step.TARGETCONDITION);
        if (promotion.hasSource()) {
            for (final var condition : promotion.getTargetConditions()) {
                final var positions = condition.evalPositions(process);
                for (final var action : promotion.getActions()) {
                    action.run(process, positions);
                }
            }
        } else {
            for (final var position : process.getDocument().getPositions()) {
                for (final var condition : promotion.getTargetConditions()) {
                    if (condition.positionMet(position)) {
                        for (final var action : promotion.getActions()) {
                            action.run(process, Collections.singletonList(position));
                        }
                        break;
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
        return condition.isMet(process);
    }

}
