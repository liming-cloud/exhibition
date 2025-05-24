package cn.exhibition.admin.config;

import cn.exhibition.admin.domain.enums.ContactProgress;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 联系进度枚举处理器
 */
public class ContactProgressHandler extends BaseTypeHandler<ContactProgress> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ContactProgress parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, parameter.getCode());
    }

    @Override
    public ContactProgress getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String code = rs.getString(columnName);
        return code == null ? null : ContactProgress.fromCode(code);
    }

    @Override
    public ContactProgress getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String code = rs.getString(columnIndex);
        return code == null ? null : ContactProgress.fromCode(code);
    }

    @Override
    public ContactProgress getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String code = cs.getString(columnIndex);
        return code == null ? null : ContactProgress.fromCode(code);
    }
}