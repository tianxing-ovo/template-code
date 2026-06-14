package com.ltx.config;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 分页插件配置
 *
 * @author tianxing
 */
@Configuration
@EnableTransactionManagement
public class MybatisPlusConfig {

    /**
     * 添加分页插件
     *
     * @return {@link MybatisPlusInterceptor }
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        // 如果有多数据源可以不配具体类型
        PaginationInnerInterceptor interceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        // 溢出总页数后是否进行处理(默认为false)
        interceptor.setOverflow(true);
        // 单页分页条数限制
        interceptor.setMaxLimit(1000L);
        // 添加分页插件
        mybatisPlusInterceptor.addInnerInterceptor(interceptor);
        return mybatisPlusInterceptor;
    }
}
