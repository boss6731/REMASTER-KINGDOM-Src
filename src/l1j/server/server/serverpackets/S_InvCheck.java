package l1j.server.server.serverpackets;

import java.io.IOException;
import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.BinaryOutputStream;

public class S_InvCheck extends ServerBasePacket {
	public static S_InvCheck get(L1PcInstance pc){
		S_InvCheck pck = new S_InvCheck();
		List<L1ItemInstance> items = pc.getInventory().getItems();
		int size = items.size();
		
		if(items != null && items.size() <= 0){
			for(L1ItemInstance itm : items){
				if(itm == null)
					continue;
				L1World.getInstance().removeObject(itm);
			}
			items.clear();
		}
		
		pck.writeC(8);
		pck.writeBit(pc.getId());
		
		if(items == null || items.size() <= 0){
			pck.writeC(16);
			pck.writeBit(0);
			return pck;
		}
		
		pck.writeC(16);
		pck.writeBit(size);
		
		pck.writeC(24);
		pck.writeBit(18);
		
		pck.writeC(32);
		pck.writeBit(0);
		
		pck.writeC(40);
		pck.writeBit(size);
		
		L1ItemInstance 	item	= null;
		
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				pck.writeC(50);
				item = items.get(i);
				byte[] stats = CheckInventoryInfo(pc, i, item);
				pck.writeBit(stats.length);
				pck.writeByte(stats);
			}
		}
		
		pck.writeC(56);
		pck.writeBit(1L);

		pck.writeH(0);
		return pck;
	}
	
	private static byte[] CheckInventoryInfo(L1PcInstance pc, int index, L1ItemInstance item) {
		item.setIdentified(true);
		BinaryOutputStream os = new BinaryOutputStream();
		os.writeC(8);
		os.writeBit(index);
		os.writeC(18);
		byte[] stats = pc.sendItemPacket(pc, item);
		os.writeBit(stats.length);
		os.writeByte(stats);
		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return os.getBytes();
	}
	
	private S_InvCheck(){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(1032);
	}

	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}
}


