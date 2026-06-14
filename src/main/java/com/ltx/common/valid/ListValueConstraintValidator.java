package com.ltx.common.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

/**
 * ListValue约束校验器
 *
 * @author tianxing
 */
public class ListValueConstraintValidator implements ConstraintValidator<ListValue, Integer> {

    private final List<Integer> list = new ArrayList<>();

    /**
     * 初始化
     *
     * @param listValue listValue
     */
    @Override
    public void initialize(ListValue listValue) {
        for (int value : listValue.values()) {
            list.add(value);
        }
    }

    /**
     * 判断是否校验成功
     *
     * @param value   需要校验的值
     * @param context 上下文
     * @return boolean
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return list.contains(value);
    }
}
