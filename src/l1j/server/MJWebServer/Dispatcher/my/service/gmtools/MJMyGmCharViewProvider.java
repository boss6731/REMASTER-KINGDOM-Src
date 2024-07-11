package l1j.server.MJWebServer.Dispatcher.my.service.gmtools;

import java.util.ArrayList;
import java.util.List;

import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
import l1j.server.MJNetServer.Codec.MJNSHandler;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJClassesType.MJEClassesType;
import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;

public class MJMyGmCharViewProvider {
	private static final MJMyGmCharViewProvider provider = new MJMyGmCharViewProvider();
	static MJMyGmCharViewProvider provider(){
		return provider;
	}
	
	private MJMyGmCharViewProvider(){
	}
	
	public MJMyGmCharacterOutModel newCharacterOut(String account){
		MJMyGmCharacterOutModel model = new MJMyGmCharacterOutModel();
		model.acount = account;
		return model;
	}
	
	public MJMyGmCharViewModel newModel(GameClient clnt){
		if(clnt.getStatus().toInt() != MJClientStatus.CLNT_STS_ENTERWORLD.toInt()){
			return null;
		}
		
		L1PcInstance pc = clnt.getActiveChar();
		if(pc == null){
			return null;
		}
		
		MJMyGmCharViewModel model = new MJMyGmCharViewModel();
		model.address = clnt.getIp();
		model.account = clnt.getAccountName();
		model.objectId = pc.getId();
		model.character = pc.getName();
		model.characterTitle = MJString.isNullOrEmpty(pc.getTitle()) ? MJString.EmptyString : pc.getTitle();
		model.characterPledge = MJString.isNullOrEmpty(pc.getClanname()) ? MJString.EmptyString : pc.getClanname();
		model.characterClass = MJEClassesType.fromGfx(pc.getClassId()).toInt();
		model.level = pc.getLevel();
		model.ncoin = pc.getNcoin();
		L1ItemInstance item = pc.getInventory().findItemId(L1ItemId.ADENA);
		model.adena = item == null ? 0 : item.getCount();
		return model;
	}
	
	public List<MJMyGmCharViewModel> onlineUsers(){
		List<MJMyGmCharViewModel> characters = new ArrayList<>(MJNSHandler.getClientSize());
		for(GameClient clnt : MJNSHandler.getClients()){
			MJMyGmCharViewModel model = newModel(clnt);
			if(model == null){
				continue;
			}
			
			characters.add(model);
		}
		return characters;
	}
}
