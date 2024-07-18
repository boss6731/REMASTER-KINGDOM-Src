package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.GameTimeClock;

public class S_OwnCharStatus extends ServerBasePacket {
	private static final String S_OWB_CHAR_STATUS = "[S] S_OwnCharStatus";

	public S_OwnCharStatus(L1PcInstance pc) {
		int time = GameTimeClock.getInstance().getGameTime().getSeconds();
		time = time - (time % 300);
		// _log.warning((new
		// StringBuilder()).append("송신 시간:").append(i).toString());

		/*a0
		 * 1f 18 b1 00
		 * 47
		 * 39 bd 8f 31
		 * 0a 00 力量
		 * 0c 00 智力
		 * 0c 00 精神
		 * 2b 00 敏捷
		 * 0c 00 體質
		 * 09 00 魅力
		 * 70 03 HP
		 * 70 03 HP
		 * aa 01 MP
		 * a4 01 MP

		 * 04 06 82 22 時間
		 * e1 重量
		 * 00 重量百分比
		 * ff 7f // 善良
		 * 00 00 // 屬性
		 * 00 00 // 屬性
		 * 00 00 // 屬性
		 * 00 00 // 屬性
		 * 00 00 00 00 // 怪物擊殺
		 * a6 09*/
		
		
		writeC(Opcodes.S_STATUS);
		writeD(pc.getId());
		writeC(pc.getLevel());
		writeD(pc.get_exp());
		
		writeH(pc.getAbility().getTotalStr());
		writeH(pc.getAbility().getTotalInt());
		writeH(pc.getAbility().getTotalWis());
		writeH(pc.getAbility().getTotalDex());
		writeH(pc.getAbility().getTotalCon());
		writeH(pc.getAbility().getTotalCha());
		
		writeH(pc.getCurrentHp());
		
		writeH(pc.getMaxHp());
		writeH(pc.getCurrentMp());
		writeH(pc.getMaxMp());
		//writeD(pc.getAC().getAc()); // 01-22 它消失了 S_OwnCharAttrDef
		writeD(time);
		writeC(pc.get_food());
		writeC(pc.getInventory().getWeight100());
		writeH(pc.getLawful());
		if(pc.getResistance() != null){
			writeH(pc.getResistance().getFire());
			writeH(pc.getResistance().getWater());
			writeH(pc.getResistance().getWind());
			writeH(pc.getResistance().getEarth());
		}else{
			writeD(0x00);
			writeD(0x00);
		}
		writeD(pc.getMonsterkill());
		writeH(0);

		
		/*String 메세지 = "a0 aa dc 0d 02 3a 28 9f fb 15 0a 00 0c 00 0c 00 1c 00 0c 00"
				+ "09 00 98 02 69 02 42 01 2d 01 f9 ff ff ff d6 ac 35 20 28 00"
				+ "ff 7f 00 00 00 00 00 00 00 00 00 00 00 00 8a a9";
		StringTokenizer st = new StringTokenizer(메세지.toString());
		while (st.hasMoreTokens()) {
			writeC(Integer.parseInt(st.nextToken(), 16));
		}*/
	}	

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_OWB_CHAR_STATUS;
	}
}


