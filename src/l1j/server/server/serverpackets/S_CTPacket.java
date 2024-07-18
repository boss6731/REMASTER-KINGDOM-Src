package l1j.server.server.serverpackets;

import java.io.IOException;
import java.util.ArrayDeque;

import l1j.server.MJCTSystem.MJCTCharInfo;
import l1j.server.MJCTSystem.MJCTItem;
import l1j.server.MJCTSystem.MJCTSpell;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;

/** MJCTSystem **/
public class S_CTPacket extends ServerBasePacket{
	public static S_CTPacket getCharacterInfo(MJCTCharInfo info){
		S_CTPacket pck = new S_CTPacket();
		pck.writeC(Opcodes.S_MESSAGE);
		pck.writeC(0x09);
		pck.writeS(info.toString());
		
		/*
		pck.writeC(Opcodes.S_BOARD_READ);
		pck.writeD(1);
		pck.writeS(info.name);
		pck.writeS("人物資訊");
		pck.writeS("");
		pck.writeS(info.toString());*/
		return pck;
	}
	
	public static S_CTPacket getSpellList(ArrayDeque<MJCTSpell> spQ){
		S_CTPacket pck = new S_CTPacket();
		pck.writeC(Opcodes.S_RETRIEVE_LIST);
		pck.writeD(1);
		if(spQ == null){
			pck.writeH(0x00);
			pck.writeC(0x03);
			pck.writeC(0x00);
			return pck;
		}
		pck.writeH(spQ.size());
		pck.writeC(0x03);
		L1ItemInstance item = ItemTable.getInstance().createItem(40005);
		while(!spQ.isEmpty()){
			MJCTSpell sp = spQ.poll();
			pck.writeD(item.getId());
			pck.writeC(item.getItem().getType2());
			pck.writeH(sp.xicon);
			pck.writeC(1);
			pck.writeD(1);
			pck.writeC(1);
			pck.writeS(sp.name);
			pck.writeC(0);
		}
		pck.writeD(100);
		pck.writeD(0x0);
		pck.writeH(0x00);
		spQ.clear();
		return pck;
	}
	
	public static S_CTPacket getInvList(ArrayDeque<MJCTItem> itemQ){
		S_CTPacket pck = new S_CTPacket();
		pck.writeC(Opcodes.S_RETRIEVE_LIST);
		pck.writeD(1);
		if(itemQ == null){
			pck.writeH(0x00);
			pck.writeC(0x03);
			pck.writeC(0x00);
			return pck;
		}
		pck.writeH(itemQ.size());
		pck.writeC(0x03);
		while(!itemQ.isEmpty()){
			MJCTItem item = itemQ.poll();
			pck.writeD(item.id);
			pck.writeC(item.item.getType2());
			pck.writeH(item.item.getGfxId());
			pck.writeC(item.bless);
			pck.writeD(item.count);
			pck.writeC(item.iden);
			pck.writeS(item.toString());
			pck.writeC(0);
		}
		pck.writeD(100);
		pck.writeD(0x0);
		pck.writeH(0x00);
		itemQ.clear();
		return pck;
	}
	
	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}
}


