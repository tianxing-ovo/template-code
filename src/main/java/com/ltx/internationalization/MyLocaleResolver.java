package com.ltx.internationalization;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        if (StringUtils.isNotBlank(lang)) {
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
