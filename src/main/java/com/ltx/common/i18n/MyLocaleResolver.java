package com.ltx.common.i18n;

import cn.hutool.core.util.StrUtil;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 自定义LocaleResolver
 * 默认为AcceptHeaderLocaleResolver(根据请求头Accept-Language自动设置Locale)
 *
 * @author tianxing
 */
public class MyLocaleResolver implements LocaleResolver {

    /**
     * 从HTTP请求中解析Locale(语言环境)
     *
     * @param request HTTP请求对象
     * @return Locale对象
     */
    @Override
    @NonNull
    public Locale resolveLocale(HttpServletRequest request) {
        // 从请求参数中获取语言设置(格式: lang=zh-CN)
        String lang = request.getParameter("lang");
        // 解析Locale
        if (StrUtil.isNotBlank(lang)) {
            String[] arr = lang.split("-");
            if (arr.length >= 2) {
                return new Locale(arr[0], arr[1]);
            } else if (arr.length == 1) {
                return new Locale(arr[0]);
            }
        }
        return Locale.getDefault();
    }

    @Override
    public void setLocale(@NonNull HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
}
