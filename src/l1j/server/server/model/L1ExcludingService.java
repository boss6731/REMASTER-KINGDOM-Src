package l1j.server.server.model;

import java.util.List;

import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJTemplate.MJProto.MainServer_Client.CS_EXCLUDE_REQ_PACKET.eExcludeMode;
import l1j.server.MJTemplate.MJProto.MainServer_Client.CS_EXCLUDE_REQ_PACKET.eExcludeType;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.SpamTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.templates.L1CharName;

public class L1ExcludingService {
	private static final L1ExcludingService service = new L1ExcludingService();
	public static L1ExcludingService service() {
		return service;
	}
	
	private L1ExcludingService() {
	}
	
	public void onRequest(L1PcInstance pc, List<byte[]> characterNames, eExcludeMode eMode, eExcludeType eType) {
		switch(eMode) {
		case eEXCLUDE_MODE_LIST:
			onViewExclude(pc, eType);
			break;
		case eEXCLUDE_MODE_ADD:
		case eEXCLUDE_MODE_DEL:
			if(characterNames == null) {
				return;
			}
			for(byte[] buffer : characterNames) {
				String characterName = new String(buffer, MJEncoding.MS949);
				if(eMode == eExcludeMode.eEXCLUDE_MODE_ADD) {
					onAppendExclude(pc, characterName, eType);
				}else {
					onRemoveExclude(pc, characterName, eType);					
				}
			}			
			break;
		}
	}
	
	public void onViewExclude(L1PcInstance pc, eExcludeType eType) {
		L1ExcludingList exList = SpamTable.getInstance().getExcludeTable(pc.getId());
		if(eType == eExcludeType.eEXCLUDE_TYPE_MAX) {
			onViewExclude(pc, eExcludeType.eEXCLUDE_TYPE_CHAT);
			onViewExclude(pc, eExcludeType.eEXCLUDE_TYPE_MAIL);
			return;
		}
		int type = eType.toInt();
		pc.sendPackets(new S_PacketBox(S_PacketBox.SHOW_LIST_EXCLUDE, exList.getExcludeList(type), type));
	}
	
	public void onAppendExclude(L1PcInstance pc, String characterName, eExcludeType eType) {
		if(eType == eExcludeType.eEXCLUDE_TYPE_MAX) {
			onAppendExclude(pc, characterName, eExcludeType.eEXCLUDE_TYPE_CHAT);
			onAppendExclude(pc, characterName, eExcludeType.eEXCLUDE_TYPE_MAIL);
			return;
		}
		
		L1ExcludingList exList = SpamTable.getInstance().getExcludeTable(pc.getId());
		if(exList.contains(eType.toInt(), characterName)) {
			return;
		}
		
		L1CharName cn = CharacterTable.getInstance().getCharName(characterName);
		if(cn == null) {
			//pc.sendPackets(String.format("%s님을 찾을 수 없습니다.", characterName));
			return;
		}
		exList.add(eType.toInt(), characterName);
		SpamTable.getInstance().insertExclude(pc, eType.toInt(), cn.getId(), characterName);
		pc.sendPackets(new S_PacketBox(S_PacketBox.ADD_EXCLUDE, eType.toInt(), characterName));
	}
	
	public void onRemoveExclude(L1PcInstance pc, String characterName, eExcludeType eType) {
		if(eType == eExcludeType.eEXCLUDE_TYPE_MAX) {
			onRemoveExclude(pc, characterName, eExcludeType.eEXCLUDE_TYPE_CHAT);
			onRemoveExclude(pc, characterName, eExcludeType.eEXCLUDE_TYPE_MAIL);
			return;
		}
		
		L1ExcludingList exList = SpamTable.getInstance().getExcludeTable(pc.getId());
		if(exList.contains(eType.toInt(), characterName)) {
			exList.remove(eType.toInt(), characterName);
			SpamTable.getInstance().delExclude(pc, eType.toInt(), characterName);
			pc.sendPackets(new S_PacketBox(S_PacketBox.REM_EXCLUDE, eType.toInt(), characterName));
		}
	}
}
