package com.ltx.common.typehandler;

import com.ltx.common.constant.Constant;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 列表类型处理器
 * List <--> varchar
 * ["1", "2", "3"] <--> "1,2,3"
 *
 * @author tianxing
 */
public class ListTypeHandler extends BaseTypeHandler<List<String>> {

    /**
     * 设置非null参数
     *
     * @param preparedStatement 预编译SQL语句的对象
     * @param index             参数索引
     * @param list              字符串列表
     * @param jdbcType          数据库字段类型
     * @throws SQLException SQL异常
     */
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int index, List<String> list, JdbcType jdbcType) throws SQLException {
        if (jdbcType == JdbcType.VARCHAR) {
            preparedStatement.setString(index, list.stream().distinct().sorted().collect(Collectors.joining(Constant.COMMA)));
        }
    }

    /**
     * 获取可为null的结果
     *
     * @param resultSet 结果集
     * @param column    字段名称
     * @return 字符串列表
     * @throws SQLException SQL异常
     */
    @Override
    public List<String> getNullableResult(ResultSet resultSet, String column) throws SQLException {
        String value = resultSet.getString(column);
        if (value != null) {
            return Arrays.asList(value.split(Constant.COMMA));
        }
        return null;
    }

    /**
     * 获取可为null的结果
     *
     * @param resultSet 结果集
     * @param index     参数索引
     * @return 字符串列表
     * @throws SQLException SQL异常
     */
    @Override
    public List<String> getNullableResult(ResultSet resultSet, int index) throws SQLException {
        String value = resultSet.getString(index);
        if (value != null) {
            return Arrays.asList(value.split(Constant.COMMA));
        }
        return null;
    }

    /**
     * 获取可为null的结果
     *
     * @param callableStatement 调用存储过程或函数的对象
     * @param index             参数索引
     * @return 字符串列表
     * @throws SQLException SQL异常
     */
    @Override
    public List<String> getNullableResult(CallableStatement callableStatement, int index) throws SQLException {
        String value = callableStatement.getString(index);
        if (value != null) {
            return Arrays.asList(value.split(Constant.COMMA));
        }
        return null;
    }
}
