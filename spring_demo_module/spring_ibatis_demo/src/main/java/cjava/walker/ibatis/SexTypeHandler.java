package cjava.walker.ibatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cjava.walker.ibatis.entity.Sex;

import com.ibatis.sqlmap.engine.type.BaseTypeHandler;

public class SexTypeHandler extends BaseTypeHandler {
	
//	private Class<Sex> sexType = Sex.class;
//	private Sex[] enums =  sexType.getEnumConstants();
	
	@Override
	public void setParameter(PreparedStatement ps, int i, Object parameter,
			String jdbcType) throws SQLException {
		ps.setInt(i,((Sex)parameter).getCode());
	}

	@Override
	public Object getResult(ResultSet rs, String columnName)
			throws SQLException {
		return Sex.getByCode(rs.getInt(columnName));
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		if(rs.wasNull())
			return null;
		return Sex.getByCode(rs.getInt(columnIndex));
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		if(cs.wasNull())
			return null;
		return Sex.getByCode(cs.getInt(columnIndex));
	}

	@Override
	public Object valueOf(String s) {

		return null;
	}

}
