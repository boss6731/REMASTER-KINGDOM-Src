package l1j.server.MJTemplate.MJSqlHelper.Clause;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

import l1j.server.MJTemplate.MJString;

public interface MJWhereClause {
	static MJWhereClause emptyClause = new EmptyClause();
	public static MJWhereClause emptyClause(){
		return emptyClause;
	}
	
	public static MJWhereClause isNullClause(String column){
		return new IsNullClause(column);
	}
	
	public static MJWhereClause isNotNullClause(String column){
		return new IsNotNullClause(column);
	}
	
	public static MJWhereClause integerRangeClause(String column, int low, int high){
		return new IntRangeClause(column, low, high);
	}
	
	public static MJWhereClause integerClause(String column, int value){
		return new IntegerClause(column, value);
	}
	
	public static MJWhereClause booleanClause(String column, boolean value){
		return new BooleanClause(column, value);
	}
	
	public static MJWhereClause booleanBitClause(String column, boolean value){
		return new BooleanBitClause(column, value);
	}
	
	public static MJWhereClause stringClause(String column, String value){
		return new StringClause(column, value);
	}
	
	public static MJWhereClause stringLikeClause(String column, String value){
		return new StringLikeClause(column, value);
	}
	
	public static MJWhereClause compositeClause(boolean and, MJWhereClause... clauses){
		return new CompositeClause(and, clauses);
	}
	
	public static MJWhereClause compositeClause(boolean and, Collection<MJWhereClause> clauses){
		return new CollectionBaseCompositeClause(and, clauses);
	}
	
	public String whereClause();
	public int numOfClause();
	public void preparedClause(int idx, PreparedStatement pstm) throws SQLException;
	
	static class EmptyClause implements MJWhereClause{
		@Override
		public String whereClause() {
			return MJString.EmptyString;
		}
		@Override
		public int numOfClause() {
			return 0;
		}
		@Override
		public void preparedClause(int idx, PreparedStatement pstm) throws SQLException {
		}
	}
	
	static abstract class SimpleValueClause<T> implements MJWhereClause{
		String column;
		T value;
		SimpleValueClause(String column, T value){
			this.column = column;
			this.value = value;
		}
		@Override
		public String whereClause() {
			return new StringBuilder(column.length() + 4).append(column).append("=?").toString();
		}

		@Override
		public int numOfClause() {
			return 1;
		}

		@Override
		public abstract void preparedClause(int idx, PreparedStatement pstm) throws SQLException;
	}
	
	static class IsNullClause implements MJWhereClause{
		String column;
		IsNullClause(String column){
			this.column = column;
		}
		
		@Override
		public String whereClause() {
			return new StringBuilder(column.length() + 10).append(column).append(" is null").toString();
		}

		@Override
		public int numOfClause() {
			return 0;
		}

		@Override
		public void preparedClause(int idx, PreparedStatement pstm) throws SQLException {
		}
	}
	
	static class IsNotNullClause implements MJWhereClause{
		String column;
		IsNotNullClause(String column){
			this.column = column;
		}
		
		@Override
		public String whereClause() {
			return new StringBuilder(column.length() + 10).append(column).append(" is not null").toString();
		}

		@Override
		public int numOfClause() {
			return 0;
		}

		@Override
		public void preparedClause(int idx, PreparedStatement pstm) throws SQLException {
		}
	}
	
	static abstract class LikeValueClause<T> extends SimpleValueClause<T>{
		LikeValueClause(String column, T value) {
			super(column, value);
		}
		
		@Override
		public String whereClause() {
			return new StringBuilder(column.length() + 8).append(column).append(" like ?").toString();
		}
		
	}
	
	static class IntRangeClause implements MJWhereClause{
		String column;
		int lowValue;
		int highValue;
		IntRangeClause(String column, int lowValue, int highValue){
			this.column = column;
			this.lowValue = lowValue;
			this.highValue = highValue;
		}
	
		@Override
		public String whereClause() {
			return new StringBuilder(column.length() + 8).append(column).append(">=? and ").append(column).append("<=?").toString();
		}

		@Override
		public int numOfClause() {
			return 2;
		}

		@Override
		public void preparedClause(int idx, PreparedStatement pstm) throws SQLException {
			pstm.setInt(idx, lowValue);
			pstm.setInt(idx + 1, highValue);
		}
		
	}
	
	static class IntegerClause extends SimpleValueClause<Integer>{
		IntegerClause(String column, Integer value) {
			super(column, value);
		}

		@Override
		public void preparedClause(int idx, PreparedStatement pstm) throws SQLException {
			pstm.setInt(idx, value);
		}	
	}
	
	static class BooleanClause extends SimpleValueClause<Boolean>{
		BooleanClause(String column, Boolean value) {
			super(column, value);
		}

		@Override
		public void preparedClause(int idx, PreparedStatement pstm) throws SQLException {
			pstm.setBoolean(idx, value);
		}	
	}
	
	static class BooleanBitClause extends SimpleValueClause<Boolean>{
		BooleanBitClause(String column, Boolean value) {
			super(column, value);
		}

		@Override
		public void preparedClause(int idx, PreparedStatement pstm) throws SQLException {
			pstm.setInt(idx, value ? 1 : 0);
		}	
	}
	
	static class StringClause extends SimpleValueClause<String>{
		StringClause(String column, String value) {
			super(column, value);
		}

		@Override
		public void preparedClause(int idx, PreparedStatement pstm) throws SQLException {
			pstm.setString(idx, value);
		}	
	}
	
	static class StringLikeClause extends LikeValueClause<String>{
		StringLikeClause(String column, String value) {
			super(column, value);
		}

		@Override
		public void preparedClause(int idx, PreparedStatement pstm) throws SQLException {
			pstm.setString(idx, String.format("%%%s%%", value));
		}
	}
	
	static class CompositeClause implements MJWhereClause{
		private MJWhereClause[] clauses;
		private String whereClause;
		private int numOfClause;
		CompositeClause(boolean and, MJWhereClause... clauses){
			this.clauses = clauses;
			parseClauses(and ? " and " : " or ");
		}
		
		private void parseClauses(String and){
			StringBuilder sb = new StringBuilder(clauses.length * 8);
			sb.append("(");
			String delimiter = MJString.EmptyString;
			for(MJWhereClause clause : clauses){
				if(clause.numOfClause() <= 0)
					continue;
				
				sb.append(delimiter);
				sb.append(clause.whereClause());
				delimiter = and;
				numOfClause += clause.numOfClause();
			}
			this.whereClause = sb.append(")").toString();
		}
		
		@Override
		public String whereClause() {
			return whereClause;
		}

		@Override
		public int numOfClause() {
			return numOfClause;
		}

		@Override
		public void preparedClause(int idx, PreparedStatement pstm) throws SQLException {
			for(MJWhereClause clause : clauses){
				clause.preparedClause(idx, pstm);
				idx += clause.numOfClause();
			}
		}
	}
	
	static class CollectionBaseCompositeClause implements MJWhereClause{
		private Collection<MJWhereClause> clauses;
		private String whereClause;
		private int numOfClause;
		CollectionBaseCompositeClause(boolean and, Collection<MJWhereClause> clauses){
			this.clauses = clauses;
			parseClauses(and ? " and " : " or ");
		}
		
		private void parseClauses(String and){
			StringBuilder sb = new StringBuilder(clauses.size() * 8);
			sb.append("(");
			String delimiter = MJString.EmptyString;
			for(MJWhereClause clause : clauses){
				if(clause.numOfClause() <= 0)
					continue;
				
				sb.append(delimiter);
				sb.append(clause.whereClause());
				delimiter = and;
				numOfClause += clause.numOfClause();
			}
			this.whereClause = sb.append(")").toString();
		}
		
		@Override
		public String whereClause() {
			return whereClause;
		}

		@Override
		public int numOfClause() {
			return numOfClause;
		}

		@Override
		public void preparedClause(int idx, PreparedStatement pstm) throws SQLException {
			for(MJWhereClause clause : clauses){
				clause.preparedClause(idx, pstm);
				idx += clause.numOfClause();
			}
		}
	}
}
