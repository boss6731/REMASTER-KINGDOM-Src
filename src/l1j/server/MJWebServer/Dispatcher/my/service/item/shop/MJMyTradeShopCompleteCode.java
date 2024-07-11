package l1j.server.MJWebServer.Dispatcher.my.service.item.shop;

import java.sql.ResultSet;
import java.sql.SQLException;

public enum MJMyTradeShopCompleteCode {
	ALIVE(0),
	COMPLETE(1),
	CANCELED(2),
	;
	private int val;
	MJMyTradeShopCompleteCode(int val){
		this.val = val;
	}
	
	public int val(){
		return val;
	}
	
	public static MJMyTradeShopCompleteCode fromInt(int val){
		for(MJMyTradeShopCompleteCode code : values()){
			if(code.val() == val){
				return code;
			}
		}
		return ALIVE;
	}
	
	public static MJMyTradeShopCompleteCode fromResultSet(ResultSet rs) throws SQLException{
		return fromInt(rs.getInt("completed"));
	}
}
