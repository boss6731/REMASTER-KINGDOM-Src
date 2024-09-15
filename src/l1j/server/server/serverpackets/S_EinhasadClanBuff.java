package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;


public class S_EinhasadClanBuff extends ServerBasePacket {
    private static final String _S_EinhasadClanBuff = "[S] S_EinhasadClanBuff";

	public S_EinhasadClanBuff(L1PcInstance pc) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(0xfb);
		writeC(0x03);
		
		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if(clan == null){
			writeC(0xfb);
			writeC(0x03);			
			writeC(0x0a); 
			writeC(0x00);
			writeH(0x00);
			return;
		}
		
		if(clan.getBuffFirst() != 0){
			writeC(0x0a); // 編號
			writeC(0x05); // 大小
			writeC(0x08); // 分隔符
			writeBit(clan.getBuffFirst()); // 訊息編號
			writeC(0x10); // 分隔符
			if (clan.getEinhasadBlessBuff() == 0) {
				writeC(0x01); // 開關
			} else if (clan.getEinhasadBlessBuff() == clan.getBuffFirst()) {
				writeC(0x02); // 開關
			} else {
				writeC(0x03); // 開關
			}

			// 以下同樣適用
		if(clan.getBuffSecond() != 0){
			writeC(0x0a);
			writeC(0x05);
			writeC(0x08);
			writeBit(clan.getBuffSecond());
			writeC(0x10);
			if(clan.getEinhasadBlessBuff() == 0){
				writeC(0x01); // 開關
			}else if(clan.getEinhasadBlessBuff() == clan.getBuffSecond()){
				writeC(0x02); // 開關
			}else writeC(0x03); // 開關
		}

			// 以下同樣適用
		if(clan.getBuffThird() != 0){
			writeC(0x0a);
			writeC(0x05);
			writeC(0x08);
			writeBit(clan.getBuffThird());
			writeC(0x10);
			if(clan.getEinhasadBlessBuff() == 0){
				writeC(0x01); // 開關
			}else if(clan.getEinhasadBlessBuff() == clan.getBuffThird()){
				writeC(0x02); // 開關
			}else writeC(0x03); // 開關
		}

			writeC(0x10); // 分隔符
			writeC(0x01); // 全體變更啟用 若為0則為禁用，1則為啟用
		
		writeH(0x00);
	}
    
    @Override
    public byte[] getContent() {
    	return getBytes();
    }

    @Override
    public String getType() {
        return _S_EinhasadClanBuff;
    }
}
