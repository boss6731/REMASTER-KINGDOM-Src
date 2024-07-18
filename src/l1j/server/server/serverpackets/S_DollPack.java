package l1j.server.server.serverpackets;

import MJShiftObject.Battle.MJShiftBattleCharacterInfo;
import l1j.server.MJTemplate.MJString;
import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_DollPack extends ServerBasePacket {






	private static final String S_DOLLPACK = "[S] S_DollPack";

	public S_DollPack(L1DollInstance pet, L1PcInstance player) {
		/*
		 * int addbyte = 0; int addbyte1 = 1; int addbyte2 = 13; int setting =
		 * 4;
		 */

		writeC(Opcodes.S_PUT_OBJECT);
		writeH(pet.getX());
		writeH(pet.getY());
		writeD(pet.getId());
		writeH(pet.getCurrentSpriteId()); // SpriteID in List.spr
		writeC(pet.getStatus()); // Modes in List.spr
		writeC(pet.getHeading());
		writeC(0); // (Bright) - 0~15
		writeC(pet.getMoveSpeed()); // ⅩΥ【Ι - 0:normal,1:fast,2:slow
		writeD(0);
		writeH(0);
		writeS(pet.getNameId());
		writeS(pet.getTitle());
		writeC(pet.getMaster() != null ? (pet.getMaster().isInvisble() ? 2 : 0) : 0); // sisinite - 0:mob, item(atk pointer) , 1:poisoned() ,
		// 2:invisable() , 4:pc, 8:cursed() , 16:brave() ,
		// 32:??, 64:??(??) , 128:invisable but name
		writeD(0); // ??
		writeS(null); // ??
		
		String master_name = MJString.EmptyString;
		L1Character c = pet.getMaster();
		if(c != null) {
			master_name = c.getName();
			if(c instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance)c;
				if(pc.is_shift_battle()) {
					String server_description = pc.get_server_description();
					MJShiftBattleCharacterInfo cInfo = pc.get_battle_info();
					if(l1j.server.MJTemplate.MJString.isNullOrEmpty(server_description) || cInfo == null){
						master_name = "身份不明的人";
					}else{
						master_name = cInfo.to_name_pair();
					}
				}
			}
		}
		
		writeS(master_name);
		writeC(0); // ??
		writeC(0xFF);
		writeC(0);
		writeC(pet.getLevel()); // PC = 0, Mon = Lv
		writeC(0);
		writeC(0xFF);
		writeC(0xFF);
		writeC(0);
		writeC(0);
		writeC(0xFF);
		writeH(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_DOLLPACK;
	}

}
