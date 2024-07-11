package l1j.server.MJWebServer.Dispatcher.my.service.character;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;

public class MJMyRepresentativeService {
	private static final MJMyRepresentativeService service = new MJMyRepresentativeService();
	public static MJMyRepresentativeService service(){
		return service;
	}
	
	private ConcurrentHashMap<String, String> representativeCache;
	private MJMyRepresentativeService(){
		representativeCache = new ConcurrentHashMap<>();
	}
	
	public int selectNcoinAmount(final String accountName) {
		final MJObjectWrapper<Integer> wrapper = new MJObjectWrapper<>();
		wrapper.value = 0;
		Selector.exec("select Ncoin_Point from accounts where login=? limit 1", new SelectorHandler() {

			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, accountName);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				if(rs.next()) {
					wrapper.value = rs.getInt("Ncoin_Point");
				}
			}
		});
		return wrapper.value;
	}
	
	public String selectRepresentativeCharacter(final String accountName){
		String representative = representativeCache.get(accountName);
		if(!MJString.isNullOrEmpty(representative)){
			return representative;
		}
		representative = selectRepresentativeCharacterDatabase(accountName);
		if(!MJString.isNullOrEmpty(representative)){
			representativeCache.put(accountName, representative);
		}
		return representative;
	}
	
	private String selectRepresentativeCharacterDatabase(final String accountName){
		MJObjectWrapper<String> wrapper = new MJObjectWrapper<>();
		wrapper.value = MJString.EmptyString;
		Selector.exec("select characterName from character_representative where accountName=? limit 1", new SelectorHandler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, accountName);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				if(rs.next()){
					wrapper.value = rs.getString("characterName");
				}
			}
		});
		return wrapper.value;
	}
	
	
	
	public void updateRepresentativeCharacter(final String accountName, final String characterName){
		representativeCache.put(accountName, characterName);
		updateRepresentativeCharacterDatabase(accountName, characterName);
	}
	
	private void updateRepresentativeCharacterDatabase(final String accountName, final String characterName){
		Updator.exec("insert into character_representative set accountName=?, characterName=? on duplicate key update characterName=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setString(++idx, accountName);
				pstm.setString(++idx, characterName);
				pstm.setString(++idx, characterName);
			}
		});
	}
}
