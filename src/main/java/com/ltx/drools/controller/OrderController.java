package com.ltx.drools.controller;

import com.ltx.drools.entity.Order;
import com.ltx.drools.service.RuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tianxing
 */
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final RuleService ruleService;

    @PostMapping("/orders")
    public Order saveOrder(@RequestBody Order order) {
        return ruleService.executeRule(order);
    }
}


