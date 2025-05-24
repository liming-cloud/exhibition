package cn.exhibition.admin.config;

import cn.exhibition.admin.domain.enums.ExhibitionType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 展会类型枚举处理器
 */
public class ExhibitionTypeHandler extends BaseTypeHandler<ExhibitionType> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ExhibitionType parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, parameter.getCode());
    }

    @Override
    public ExhibitionType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String code = rs.getString(columnName);
        return code == null ? null : ExhibitionType.fromCode(code);
    }

    @Override
    public ExhibitionType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String code = rs.getString(columnIndex);
        return code == null ? null : ExhibitionType.fromCode(code);
    }

    @Override
    public ExhibitionType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String code = cs.getString(columnIndex);
        return code == null ? null : ExhibitionType.fromCode(code);
    }
}