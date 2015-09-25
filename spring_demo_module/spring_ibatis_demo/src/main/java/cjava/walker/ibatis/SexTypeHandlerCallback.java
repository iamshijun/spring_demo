package cjava.walker.ibatis;

import java.sql.SQLException;

import cjava.walker.ibatis.entity.Sex;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

public class SexTypeHandlerCallback implements TypeHandlerCallback {

    public Object getResult(ResultGetter getter) throws SQLException {
        Sex result = null;
        if(!getter.wasNull() && getter.getObject()!= null) {
            for(Sex sex : Sex.values()) {
                if(sex.getCode() == getter.getInt()) {
                    result = sex;
                    break;
                }
            }
        }
        return result;
    }

    public void setParameter(ParameterSetter setter, Object obj)
            throws SQLException {
        if(obj == null) {
            setter.setInt(-1);
        }else {
            Sex status = (Sex)obj;
            setter.setInt(status.getCode());
        }
    }

    public Object valueOf(String s) {
//    	return Enum.valueOf(Sex.class, (String)s);
    	return s;
    }

}