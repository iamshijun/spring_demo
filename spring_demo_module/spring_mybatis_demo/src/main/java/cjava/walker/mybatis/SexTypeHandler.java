package cjava.walker.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import cjava.walker.mybatis.entity.Sex;


public class SexTypeHandler<T> extends BaseTypeHandler<Sex> {

	/*private Class<T> enumType;
	
	
	public ValueTypeHandler(Class<T> enumType){
		 this.enumType = enumType; 
	}*/
	
	private Sex[] enums; 
	private EnumSet<Sex> enumSet;
	
	public SexTypeHandler(){
		enums =  Sex.class.getEnumConstants();
		enumSet = EnumSet.allOf(Sex.class);
	}
	
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i,
			Sex parameter, JdbcType jdbcType) throws SQLException {
		if(parameter == null){
			ps.setInt(i, JdbcType.INTEGER.TYPE_CODE);
		}else if(!enumSet.contains(parameter)){
			throw new IllegalArgumentException();
		}
		ps.setInt(i,parameter.getCode());
	}
	
	

	@Override
	public Sex getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		if(rs.wasNull())
			return null;
		return Sex.getByCode(rs.getInt(columnName));
	}

	@Override
	public Sex getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		if(rs.wasNull())
			return null;
		return Sex.getByCode(rs.getInt(columnIndex));
	}

	@Override
	public Sex getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		if(cs.wasNull())
			return null;
		return Sex.getByCode(cs.getInt(columnIndex));
	}
	

}
