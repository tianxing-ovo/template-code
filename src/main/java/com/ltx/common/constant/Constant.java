package com.ltx.common.constant;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 常量
 *
 * @author tianxing
 */
public interface Constant {
    String USER_HOME = System.getProperty("user.home");
    Path DESKTOP_PATH = Paths.get(USER_HOME, "Desktop");
    String COMMA = ",";
}
