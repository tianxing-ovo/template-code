package com.ltx.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Jwt工具类测试
 *
 * @author tianxing
 */
@Slf4j
class JwtUtilTest {

    @Test
    void genSecret() {
        String secret = JwtUtil.genSecret();
        assertNotNull(secret);
        log.info("secret: {}", secret);
    }
}