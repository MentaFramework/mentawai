package org.mentawai.bean.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BooleanEnumType extends EnumType {
	
	public static enum Value { T , F }
	
	public BooleanEnumType() {
		
		super(Value.class);
	}
	
	@Override
	public String toString() {
		
		return this.getClass().getSimpleName();
	}

	@Override
	public Object getFromResultSet(ResultSet rset, int index) throws SQLException {
		
		Value v = (Value) super.getFromResultSet(rset, index);
		
		return v == Value.T;
	}

	@Override
	public Object getFromResultSet(ResultSet rset, String field) throws SQLException {
		
		Value v = (Value) super.getFromResultSet(rset, field);
		
		return v == Value.T;
	}
	
	@Override
	public Class<? extends Object> getTypeClass() {
		
		return Boolean.class;
	}
	
	@Override
	public void bindToStmt(PreparedStatement stmt, int index, Object value) throws SQLException {
		
		if (value == null) {
		
			stmt.setString(index, null);
			
		} else if (value instanceof Boolean) {
			
			Boolean b = (Boolean) value;
			
			if (b) stmt.setString(index, Value.T.toString());
			else stmt.setString(index, Value.F.toString());
		
		} else {
			
			throw new IllegalArgumentException("value is not a boolean!");
		}
		
	}
}