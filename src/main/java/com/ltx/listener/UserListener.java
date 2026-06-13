package com.ltx.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.ltx.entity.po.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户监听器
 *
 * @author tianxing
 */
public class UserListener extends AnalysisEventListener<User> {

    private final List<User> list = new ArrayList<>();

    /**
     * 处理读取到的用户数据
     *
     * @param user            用户
     * @param analysisContext 分析上下文
     */
    @Override
    public void invoke(User user, AnalysisContext analysisContext) {
        list.add(user);
    }

    /**
     * 数据读取完成后的处理
     * 
     * @param analysisContext 分析上下文
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    /**
     * 获取用户列表
     *
     * @return 用户列表
     */
    public List<User> getUserList() {
        return list;
    }
}
