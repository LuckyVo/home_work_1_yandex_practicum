package ru.myblog.utils;

import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.SQLException;

@UtilityClass
public class SqlUtils {

    public static Long getLong(ResultSet rs, String fieldName) throws SQLException {
        Object object = rs.getObject(fieldName);
        if (object == null) return null;
        if (object instanceof Number) {
            return ((Number) object).longValue();
        }
        throw new IllegalArgumentException(" Can't extract long value from " + fieldName);
    }

    public static Integer getInteger(ResultSet rs, String fieldName) throws SQLException {
        Object object = rs.getObject(fieldName);
        if (object == null) return null;
        if (object instanceof Number) {
            return ((Number) object).intValue();
        }
        throw new IllegalArgumentException(" Can't extract Integer value from " + fieldName);
    }

    public static String getStringOrElseEmpty(ResultSet rs, String fieldName) throws SQLException {
        Object object = rs.getObject(fieldName);
        if (object == null) return "";
        if (object instanceof String) {
            return (String) object;
        }
        throw new IllegalArgumentException(" Can't extract long value from " + fieldName);
    }
}
