package l1j.server.MJWebServer.Dispatcher.my.service.character;

import java.util.Collections;
import java.util.List;

import l1j.server.MJTemplate.MJSqlHelper.Clause.MJWhereClause;
import l1j.server.MJTemplate.matcher.Matcher;
import l1j.server.MJTemplate.matcher.Matchers;
import l1j.server.server.GameClient;
import l1j.server.server.Controller.LoginController;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJMyCharInvService {
	
	private static final MJMyCharInvService service = new MJMyCharInvService();
	public static MJMyCharInvService service(){
		return service;
	}
	
	private static final MJWhereClause equippedWhereClause = MJWhereClause.integerClause("is_equipped", 1); 
	private static final Matcher<L1ItemInstance> equippedMatcher = new Matcher<L1ItemInstance>(){
		@Override
		public boolean matches(L1ItemInstance t) {
			return t.isEquipped();
		}
	};
	
	private MJMyCharInvService(){
	}
	
	private L1PcInstance findMemoryFromAccount(String accountName){
		GameClient clnt = LoginController.getInstance().getClientByAccount(accountName);
		return clnt == null ? null : clnt.getActiveChar();
	}
	
	private L1PcInstance findMemory(int characterId){
		L1Object obj = L1World.getInstance().findObject(characterId);
		return obj != null && obj instanceof L1PcInstance ? (L1PcInstance)obj : null;
	}
	
	private L1PcInstance findMemory(String characterName){
		return L1World.getInstance().getPlayer(characterName);
	}
	
	public List<MJMyCharInvItemInfo> equippedItems(int characterId){
		L1PcInstance pc = findMemory(characterId);
		if(pc != null){
			return MJMyCharInvDatabaseProvider.characterInventory().fromMemory(pc, equippedMatcher);			
		}
		return MJMyCharInvDatabaseProvider.characterInventory().fromDatabase(characterId, equippedWhereClause, true);
	}
	
	public List<MJMyCharInvItemInfo> matchesInventoryItems(String characterName, Matcher<L1ItemInstance> matcher){
		return matchesInventoryItems(findMemory(characterName), matcher);
	}
	
	public List<MJMyCharInvItemInfo> matchesInventoryItems(int characterId, Matcher<L1ItemInstance> matcher){
		return matchesInventoryItems(findMemory(characterId), matcher);
	}
	
	public List<MJMyCharInvItemInfo> matchesInventoryItems(L1PcInstance pc, Matcher<L1ItemInstance> matcher){
		if(pc != null){
			return MJMyCharInvDatabaseProvider.characterInventory().fromMemory(pc, matcher);			
		}
		return Collections.emptyList();
	}
	
	public List<MJMyCharInvItemInfo> allInventoryItems(int characterId){
		L1PcInstance pc = findMemory(characterId);
		if(pc != null){
			return MJMyCharInvDatabaseProvider.characterInventory().fromMemory(pc, Matchers.all());			
		}
		return MJMyCharInvDatabaseProvider.characterInventory().fromDatabase(characterId, MJWhereClause.emptyClause(), true);	
	}
	
	public int numOfInventoryItems(int characterId){
		L1PcInstance pc = findMemory(characterId);
		if(pc != null){
			return MJMyCharInvDatabaseProvider.characterInventory().numOfItemsFromMemory(pc);
		}
		return MJMyCharInvDatabaseProvider.characterInventory().numOfItemsFromDatabase(characterId, MJWhereClause.emptyClause(), true);
	}
	
	public List<MJMyCharInvItemInfo> accountWarehouseItems(String account){
		L1PcInstance pc = findMemoryFromAccount(account);
		if(pc != null){
			return MJMyCharInvDatabaseProvider.accountWarehouse().fromMemory(pc, Matchers.all());
		}
		return MJMyCharInvDatabaseProvider.accountWarehouse().fromDatabase(account, MJWhereClause.emptyClause(), true);
	}
	
	public int numOfAccountWarehouseItems(String account){
		L1PcInstance pc = findMemoryFromAccount(account);
		if(pc != null){
			return MJMyCharInvDatabaseProvider.accountWarehouse().numOfItemsFromMemory(pc);
		}
		return MJMyCharInvDatabaseProvider.accountWarehouse().numOfItemsFromDatabase(account, MJWhereClause.emptyClause(), true);
	}
	
	public List<MJMyCharInvItemInfo> characterWarehouseItems(String account){
		L1PcInstance pc = findMemoryFromAccount(account);
		if(pc != null){
			return MJMyCharInvDatabaseProvider.characterWarehouse().fromMemory(pc, Matchers.all());
		}
		return MJMyCharInvDatabaseProvider.characterWarehouse().fromDatabase(account, MJWhereClause.emptyClause(), true);
	}
	
	public int numOfCharacterWarehouseItems(String account){
		L1PcInstance pc = findMemoryFromAccount(account);
		if(pc != null){
			return MJMyCharInvDatabaseProvider.characterWarehouse().numOfItemsFromMemory(pc);
		}
		return MJMyCharInvDatabaseProvider.characterWarehouse().numOfItemsFromDatabase(account, MJWhereClause.emptyClause(), true);
	}
	
	public List<MJMyCharInvItemInfo> elfWarehouseItems(String account){
		L1PcInstance pc = findMemoryFromAccount(account);
		if(pc != null){
			return MJMyCharInvDatabaseProvider.elfWarehouseProvider().fromMemory(pc, Matchers.all());
		}
		return MJMyCharInvDatabaseProvider.elfWarehouseProvider().fromDatabase(account, MJWhereClause.emptyClause(), true);
	}
	
	public int numOfElfWarehouseItems(String account){
		L1PcInstance pc = findMemoryFromAccount(account);
		if(pc != null){
			return MJMyCharInvDatabaseProvider.elfWarehouseProvider().numOfItemsFromMemory(pc);
		}
		return MJMyCharInvDatabaseProvider.elfWarehouseProvider().numOfItemsFromDatabase(account, MJWhereClause.emptyClause(), true);
	} 

}
