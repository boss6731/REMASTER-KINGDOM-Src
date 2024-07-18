package l1j.server.server.serverpackets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1MarketPrice;
import l1j.server.server.utils.BinaryOutputStream;

public class S_MarketPriceList extends ServerBasePacket {
	public S_MarketPriceList(L1PcInstance pc, ArrayList<L1MarketPrice> list) {
		try {
			writeC(Opcodes.S_RETRIEVE_LIST);
			writeD(0);
			writeH(list.size());
			writeC(5); // 捐血倉庫
			L1Item item = null;
			L1Item template = null;
			
			Collections.sort(list, new NoAscCompare());
			int size = list.size();
			if(size > 200){
				size = 200;
			}
			for (int i = 0; i < size; i++) {
				L1MarketPrice auction = list.get(i);
				item = ItemTable.getInstance().getTemplate(auction.getItemId());
				writeD(auction.getOrder());
				writeC(item.getType2());
				writeH(item.getGfxId());
				writeC(auction.getIden());
				writeD(auction.getCount());
				writeC(1);


				StringBuilder name = new StringBuilder();
				switch (auction.getAttr()) {
					case 1:
						name.append("$6115");
						break; // 火靈1段
					case 2:
						name.append("$6116");
						break; // 火靈2段
					case 3:
						name.append("$6117");
						break; // 火靈3段（火的屬性）
					case 4:
						name.append("$14361");
						break; // 火靈4段
					case 5:
						name.append("$14365");
						break; // 火靈5段

					case 6:
						name.append("$6118");
						break; // 水靈1段
					case 7:
						name.append("$6119");
						break; // 水靈2段
					case 8:
						name.append("$6120");
						break; // 水靈3段（水的屬性）
					case 9:
						name.append("$14362");
						break; // 水靈4段
					case 10:
						name.append("$14366");
						break; // 水靈5段

					case 11:
						name.append("$6121");
						break; // 風靈1段
					case 12:
						name.append("$6122");
						break; // 風靈2段
					case 13:
						name.append("$6123");
						break; // 風靈3段（風的屬性）
					case 14:
						name.append("$14363");
						break; // 風靈4段
					case 15:
						name.append("$14367");
						break; // 風靈5段
					case 16:
						name.append("$6124");
						break; // 地靈1段
					case 17:
						name.append("$6125");
						break; // 地靈2段
					case 18:
						name.append("$6126");
						break; // 地靈3段（地的屬性）
					case 19:
						name.append("$14364");
						break; // 地靈4段
					case 20:
						name.append("$14368");
						break; // 地靈5段
					default:
						break;
					if (auction.getEnchant() >= 0) {
						name.append("+" + auction.getEnchant() + " ");
					} else if (auction.getEnchant() < 0) {
						name.append(String.valueOf(auction.getEnchant()) + " ");
					}

					if (auction.getIden() == 0) { // 祝福？
						writeS("\fH祝福的 " + item.getName()); // 物品名稱
					} else if (auction.getIden() == 2) { // 詛咒
						writeS("\f3詛咒的 " + item.getName()); // 物品名稱
					} else {
						writeS(name + item.getName()); // 物品名稱
					}

					int type = item.getUseType();
					if (type < 0) {
						type = 0;
					}
					template = ItemTable.getInstance().getTemplate(item.getItemId());
					if (template == null) {
						writeC(0);
					} else {
						@suppresswarnings("resource")
						BinaryOutputStream os = new BinaryOutputStream();
						os.writeC(39);
						if(auction.getCharName() != null){
							os.writeS("\fY賣家 : " + auction.getCharName());
						} else {
							os.writeS("\fU賣家 : 管理員商店");
						}
					
					
					if(auction.getPrice() > 0){
						StringBuffer max = null;
						if(auction.getPrice() >= 1000){
							max = reverse(String.valueOf(auction.getPrice()));
							max.insert(3, ",");
						}
						if(auction.getPrice() >= 1000000){
							max = reverse(String.valueOf(auction.getPrice()));
							max.insert(6, ",");
							max.insert(3, ",");
						} 
						if(auction.getPrice() > 1000000000){
							max = reverse(String.valueOf(auction.getPrice()));
							max.insert(9, ",");
							max.insert(6, ",");
							max.insert(3, ",");
						}
						max = reverse(max.toString());
						max.insert(0, "\\aC");

						os.writeC(39);
						os.writeS("\aG販售金額 : " + max.toString());
					}

						os.writeC(39);
						if(auction.getCount() > 0){
							os.writeS("\aH販售數量 : " + auction.getCount());
						}
						writeC(os.getBytes().length);
						for (byte b : os.getBytes()) {
							writeC(b);
						}
					}
				}
			writeD(0);
			writeD(0x00000000);
			writeH(0x00);
			
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

		private StringBuffer reverse(String s) {
			return new StringBuffer(s).reverse();
		}

		static class NoAscCompare implements Comparator<L1MarketPrice> {
			/**
			 * 升冪(ASC)
			 */
			@override
			public int compare(L1MarketPrice arg0, L1MarketPrice arg1) {
				// TODO 自動生成的方法存根
				return arg0.getPrice() < arg1.getPrice() ? -1 : arg0.getPrice() > arg1.getPrice() ? 1 : 0;
			}
		}


		public byte[] getContent() throws IOException {
			return getBytes();
		}



