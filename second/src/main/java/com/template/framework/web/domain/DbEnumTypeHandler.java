package com.template.framework.web.domain;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class DbEnumTypeHandler extends BaseTypeHandler<DbEnum> {

    private Class<DbEnum> type;

    public DbEnumTypeHandler(Class<DbEnum> type) {
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, DbEnum parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setInt(i, parameter.getConstant());
    }

    @Override
    public DbEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int constant = rs.getInt(columnName);
        if (rs.wasNull()) {
            return null;
        } else {
            return convert(constant);
        }
    }

    @Override
    public DbEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int constant = rs.getInt(columnIndex);
        if (rs.wasNull()) {
            return null;
        } else {
            return convert(constant);
        }
    }

    @Override
    public DbEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int constant = cs.getInt(columnIndex);
        if (cs.wasNull()) {
            return null;
        } else {
            return convert(constant);
        }
    }

    private DbEnum convert(int constant) {
        DbEnum[] dbEnums = type.getEnumConstants();
        for (DbEnum dbEnum : dbEnums) {
            if (dbEnum.getConstant().equals(constant)) {
                return dbEnum;
            }
        }
        return null;
    }

}
