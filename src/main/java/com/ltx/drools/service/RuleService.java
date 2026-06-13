package com.ltx.drools.service;


import com.ltx.drools.entity.Order;
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
