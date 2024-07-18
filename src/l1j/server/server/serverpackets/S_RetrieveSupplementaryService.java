package l1j.server.server.serverpackets;

import java.io.IOException;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Warehouse.SupplementaryService;
import l1j.server.server.model.Warehouse.WarehouseManager;

public class S_RetrieveSupplementaryService extends ServerBasePacket {



	private static final String _S_RetrieveSupplementaryService = "[S] S_RetrieveSupplementaryService";

	public S_RetrieveSupplementaryService(int objid, L1PcInstance pc) {

		SupplementaryService warehouse = WarehouseManager.getInstance().getSupplementaryService(pc.getAccountName());
		writeC(Opcodes.S_RETRIEVE_LIST);
		writeD(objid);
		writeH(warehouse.getSize());
		writeC(21);
		writeC(0x01); // 將此處設置為1以外的數字會增加計數，但會導致斷線
		writeC(0x01); // 將此處設置為1以外的數字，列表將不顯示
		for (L1ItemInstance item : warehouse.getItems()) {
			writeD(item.getId());
			writeD(item.getCount());
			writeC(0x01);    // false: 不可交易 / true: 可交易
			writeC(0x01);    // 頁面（不要修改）
			writeC(0x00);
			writeC(0x01);
			writeD(-1);
			String viewName = item.getName();
			int length = viewName.length();
			writeH((length + 1) * 2);
			for(int i = 0; i < length; ++i) {
				writeH(viewName.charAt(i));
			}
			writeH(0x00);
			
			/*byte[] buffer = viewName.getBytes(Charset.forName("UTF-16"));			
			int length = buffer.length - 2;
			writeH(length + 2);
			for(int i=0; i<length; i += 2) {
				int current = i + 2;
				int next = i + 3;
				writeC(buffer[next]);
				writeC(buffer[current]);
			}
			writeH(0x00);
*/
			writeD(0);
			writeH(0);
			writeC(0);
			writeH(item.get_gfxid());
			writeC(item.getItem().getBless());
			writeD(item.getCount());
			writeC(item.isIdentified() ? 1 : 0);	
			writeC(1);
			writeS(item.getViewName());
		}
		writeD(0);
		writeD(0x0);
		writeH(0x00);
	}
	
	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}

	@Override
	public String getType() {
		return _S_RetrieveSupplementaryService;
	}
}


