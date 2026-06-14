package com.ltx.service.drools;


import com.ltx.entity.drools.Order;
import lombok.RequiredArgsConstructor;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

/**
 * @author tianxing
 */
@Service
@RequiredArgsConstructor
public class RuleService {
    private final KieBase kieBase;

    public Order executeRule(Order order) {
        KieSession kieSession = kieBase.newKieSession();
        kieSession.insert(order);
        kieSession.fireAllRules();
        kieSession.dispose();
        return order;
    }
}
