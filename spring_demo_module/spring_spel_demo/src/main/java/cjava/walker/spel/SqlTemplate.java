package cjava.walker.spel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.jdbc.core.SqlProvider;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

public class SqlTemplate implements SqlProvider{
	
	private ExpressionParser expressionParser;
	
	private ParserContext parserContext;
	private StandardEvaluationContext evaluationContext; 
	private Expression parseExpression;
	
	private String table;
	private Map<String,Object> params;
	
	private String statement;
	
	public SqlTemplate(String statement){
		this(statement, null, null);
	}
	
	public static SqlTemplate create(String statement){
		return new SqlTemplate(statement);
	}
	
	public SqlTemplate addParameter(String param, Object value){
		params.put(param, value);
		return this;
	}
	
	public SqlTemplate addVaraiable(String variable,Object value){
		evaluationContext.setVariable(variable, value);
		return this;
	}
	public SqlTemplate addVaraiables(Map<String,Object> variables){
		evaluationContext.setVariables(variables);
		return this;
	}
	
	public SqlTemplate addIf(String param,Object value,Predicate<Object> predicate){
		if(predicate.test(value))
			addParameter(param, value);
		return this;
	}
	
	public SqlTemplate withTable(String table){
		this.table = table;
		return this;
	}
	
	public SqlTemplate(String statement, String table, Map<String,Object> params){
		this.statement = statement;
		this.table = table;
		if(params == null)
			params = new HashMap<String, Object>();
		this.params = params;
		init();
	}

	@Override
	public String getSql() {
		return parseExpression.getValue(evaluationContext, this, String.class);
	}
	
	private void init() {
		expressionParser = new SpelExpressionParser();
		parserContext = new TemplateParserContext();// prefix="${",suffix="}"

		parseExpression = expressionParser.parseExpression(statement, parserContext);

		evaluationContext = new StandardEvaluationContext();

		evaluationContext.addPropertyAccessor(SqlTemplateAccessor.INSTANCE);
		//evaluationContext.addPropertyAccessor(new ReflectivePropertyAccessor());
		//evaluationContext.addPropertyAccessor(new MapAccessor());

		evaluationContext.registerFunction("isEmpty", ReflectionUtils.findMethod(StringUtils.class, "isEmpty",new Class[]{Object.class}));
	}
	
	public String getTable() {
		return table;
	}
	
	public Map<String, Object> getParams() {
		return params;
	}
	

	private static class SqlTemplateAccessor implements PropertyAccessor{

		public final static SqlTemplateAccessor INSTANCE = new SqlTemplateAccessor(); 
		
		@Override
		public Class<?>[] getSpecificTargetClasses() {
			return new Class<?>[]{SqlTemplate.class};
		}
	
		@Override
		public boolean canRead(EvaluationContext context, Object target, String name)
				throws AccessException {
			return true;
		}
	
		@Override
		public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
			SqlTemplate SqlTemplate = (SqlTemplate)target;
			Object value = null;
			if("table".equalsIgnoreCase(name)){
				value = SqlTemplate.getTable();
			}else{
				value = SqlTemplate.getParams().get(name);
				if(value == null){
					return TypedValue.NULL;
				}
			}
			return new TypedValue(value);
		}
	
		@Override
		public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException {
			return false;
		}
	
		@Override
		public void write(EvaluationContext context, Object target, String name, Object newValue) throws AccessException {
			//throw new UnsupportedOperationException("write is not support in SqlTemplateAccessor");
			throw new AccessException("properties in SqlExpression are read-only");
		}
		
	}


}
