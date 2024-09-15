package l1j.server.server.server.command.executor;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_WAREHOUSE_ITEM_LIST_NOTI;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

import java.util.StringTokenizer;

public class L1InvGfxId implements L1CommandExecutor {

	private L1InvGfxId() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1InvGfxId();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			int gfxid = Integer.parseInt(st.nextToken(), 10);
			int count = Integer.parseInt(st.nextToken(), 10);
			if(count <= 200){
				SC_WAREHOUSE_ITEM_LIST_NOTI noti = SC_WAREHOUSE_ITEM_LIST_NOTI.newDisplayInstance(pc.getId(), 0, gfxid, count, true);
				pc.sendPackets(noti, MJEProtoMessages.SC_WAREHOUSE_ITEM_LIST_NOTI, true);
			}else{
				int page = (count / 200) + ((count % 200) > 0 ? 1 : 0);
				for(int i=0; i<page; ++i){
					int real_index = i * 200;
					SC_WAREHOUSE_ITEM_LIST_NOTI noti = SC_WAREHOUSE_ITEM_LIST_NOTI.newDisplayInstance(pc.getId(), real_index, gfxid + real_index, 200, (i + 1 == page));
					pc.sendPackets(noti, MJEProtoMessages.SC_WAREHOUSE_ITEM_LIST_NOTI, true);
				}
			}
			
			
			//pc.sendPackets(new S_RetrieveList(pc.getId(), pc, gfxid, count));

			pc.sendPackets(String.valueOf(new S_SystemMessage("\\aHInvGfxId " + gfxid + "~" + String.valueOf(count) + " 已創建。")));
		} catch (Exception exception) {
			int count = 0;
			for (L1ItemInstance item : pc.getInventory().getItems()) {
				if (item.getItemId() == 40005) {
					pc.getInventory().deleteItem(item);
					count++;
				}
			}
			if (count > 0) {
				pc.sendPackets(String.valueOf(new S_SystemMessage("\\aAInvGfxId 確認用道具(" + count + ")已刪除。")));
			} else {
				pc.sendPackets(String.valueOf(new S_SystemMessage("\\aH" + cmdName + " 請輸入 [InvGfxId] [數量]。")));
			}
		}
	}
}
