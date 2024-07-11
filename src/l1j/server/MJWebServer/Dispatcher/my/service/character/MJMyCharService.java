package l1j.server.MJWebServer.Dispatcher.my.service.character;

import java.util.List;

import l1j.server.MJTemplate.MJSqlHelper.Clause.MJClauseBuilder;
import l1j.server.MJTemplate.MJSqlHelper.Clause.MJLimitClause;
import l1j.server.MJTemplate.MJSqlHelper.Clause.MJOrderClause;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJMyCharService {
	private static final MJMyCharService service = new MJMyCharService();
	
	public static MJMyCharService service(){
		return service;
	}
	
	private static final MJOrderClause expOrderDesc = MJOrderClause.simpleClause("exp", false);
	private MJMyCharService(){
	}
	
	public List<MJMyCharSimpleInfo> fromAccount(final String accountName){
		return fromAccountInternal(MJMyCharDatabaseProvider.simple(), accountName);
	}
	
	public List<MJMyCharDetailInfo> fromAccountDetail(final String accountName){
		return fromAccountInternal(MJMyCharDatabaseProvider.detail(), accountName);
	}
	
	public MJMyCharSimpleInfo fromCharacterName(final String characterName){
		return fromCharacterNameInternal(MJMyCharDatabaseProvider.simple(), characterName);
	}
	
	public MJMyCharDetailInfo fromCharacterNameDetail(final String characterName){
		return fromCharacterNameInternal(MJMyCharDatabaseProvider.detail(), characterName);
	}
	
	private <T> List<T> fromAccountInternal(final MJMyCharDatabaseProvider<T> provider, final String accountName){
		MJClauseBuilder<List<T>> builder = new MJClauseBuilder<List<T>>()
				.whereClause("account_name", accountName)
				.orderClause(expOrderDesc);
		
		return provider.fromDatabase(builder, true);
	}
	
	private <T> T fromCharacterNameInternal(final MJMyCharDatabaseProvider<T> provider, final String characterName){
		L1PcInstance pc = L1World.getInstance().getPlayer(characterName);
		if(pc != null){
			return provider.fromMemory(pc);
		}
		
		MJClauseBuilder<List<T>> builder = new MJClauseBuilder<List<T>>()
				.whereClause("char_name", characterName)
				.limitClause(MJLimitClause.oneRowLimitClause);
		List<T> list = provider.fromDatabase(builder, true);
		return list == null || list.size() <= 0 ? null : list.get(0);
	}
}
