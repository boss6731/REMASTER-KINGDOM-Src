package l1j.server.MJWebServer.Dispatcher.my.service.ncoin;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public enum MJMyNcoinSearchColumn {
	all("*", "(deposit_name=? or deposit_name like ? or character_name=? or character_name like ? or account_name=? or account_name like ?)", 3),
	deposit_name("deposit_name", "(deposit_name=? or deposit_name like ?)", 1),
	character_name("character_name", "(character_name=? or character_name like ?)", 1),
	account_name("account_name", "(account_name=? or account_name like ?)", 1),
	;
	
	private String searchColumn;
	private String whereClause;
	private int loop;
	MJMyNcoinSearchColumn(String searchColumn, String whereClause, int loop){
		this.searchColumn = searchColumn;
		this.whereClause = whereClause;
		this.loop = loop;
	}
	
	String makeWhereClause(){
		return whereClause;
	}
	
	void makePreparedClause(PreparedStatement pstm, String searchName) throws SQLException{
		String searchPattern1 = searchName;
		String searchPattern2 = String.format("%%%s%%", searchName);
		int idx = 0;
		for(int i=0; i<loop; ++i){
			pstm.setString(++idx, searchPattern1);
			pstm.setString(++idx, searchPattern2);
		}
	}
	
	public static MJMyNcoinSearchColumn fromColumn(String searchColumn){
		for(MJMyNcoinSearchColumn c : values()){
			if(c.searchColumn.equalsIgnoreCase(searchColumn)){
				return c;
			}
		}
		return null;
	}
}
