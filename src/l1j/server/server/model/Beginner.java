package l1j.server.server.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;

public class Beginner {

	private static Logger _log = Logger.getLogger(Beginner.class.getName());

	private static Beginner _instance;

	private static int _weapont_count = 0;
	
	public static Beginner getInstance() {
		if (_instance == null) {
			_instance = new Beginner();
		}
		return _instance;
	}

	private Beginner() {
	}
	
	public static void reload() {
		Beginner oldInstance = _instance;
		_instance = new Beginner();
		if (oldInstance != null);
	}

	public int GiveItemToActivePc(L1PcInstance pc) {
		Selector.exec("select * from beginner where activate=全部 or activate=?", new SelectorHandler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, classid_to_db_name(pc.getClassId()));
			}
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()) {
					int itemid = rs.getInt("item_id");
					int count = rs.getInt("count");
					int enchant = rs.getInt("enchantlvl");
					L1Item temp = ItemTable.getInstance().getTemplate(itemid);
					if (temp != null) {
						if (!temp.isStackable()) {
							L1ItemInstance item = null;
							int createCount;
							for (createCount = 0; createCount < count; createCount++) {
								item = ItemTable.getInstance().createItem(itemid);
								item.setEnchantLevel(enchant);
								if (pc.getInventory().checkAddItem(item, 1) == L1Inventory.OK) {
									pc.getInventory().storeItem(item);
								} else {
									break;
								}
							}
							if (createCount > 0) {
								pc.sendPackets(new S_ServerMessage(403, // 你已獲得 %0.
										item.getLogName() + "(ID:" + itemid + ")"));
							}
						}
					}
				}
			}
		});
		return 0;
		
	}

	
	public void writeBookmark(L1PcInstance pc) {
		Connection c = null;
		PreparedStatement p = null;
		PreparedStatement p1 = null;
		ResultSet r = null;

		try {
			c = L1DatabaseFactory.getInstance().getConnection();
			p = c.prepareStatement("SELECT * FROM beginner_teleport");

			r = p.executeQuery();
			while (r.next()) {
				p1 = c.prepareStatement("INSERT INTO character_teleport SET id = ?, char_id = ?, name = ?, locx = ?, locy = ?, mapid = ?, speed =?, num=?");
				p1.setInt(1, IdFactory.getInstance().nextId());
				p1.setInt(2, pc.getId());
				p1.setString(3, r.getString("name"));
				p1.setInt(4, r.getInt("locx")); 
				p1.setInt(5, r.getInt("locy"));
				p1.setShort(6, r.getShort("mapid"));
				p1.setInt(7, -1);
				p1.setInt(8, 0);
//				p1.setInt(7, r.getInt("num"));
				p1.execute();
				SQLUtil.close(p1);
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, "添加書籤時發生錯誤。", e);
		} finally {
			SQLUtil.close(r);
			SQLUtil.close(p1);
			SQLUtil.close(p);
			SQLUtil.close(c);
		}
	}
	
	private static String classid_to_db_name(int class_id) {		
		switch(class_id) {
		case L1PcInstance.CLASSID_PRINCE:
			case L1PcInstance.CLASSID_PRINCESS:
				return "王族";
			case L1PcInstance.CLASSID_KNIGHT_MALE:
			case L1PcInstance.CLASSID_KNIGHT_FEMALE:
				return "騎士";
			case L1PcInstance.CLASSID_ELF_MALE:
			case L1PcInstance.CLASSID_ELF_FEMALE:
				return "妖精";
			case L1PcInstance.CLASSID_WIZARD_MALE:
			case L1PcInstance.CLASSID_WIZARD_FEMALE:
				return "法師";
			case L1PcInstance.CLASSID_DARK_ELF_MALE:
			case L1PcInstance.CLASSID_DARK_ELF_FEMALE:
				return "黑暗妖精";
			case L1PcInstance.CLASSID_DRAGONKNIGHT_MALE:
			case L1PcInstance.CLASSID_DRAGONKNIGHT_FEMALE:
				return "龍騎士";
			case L1PcInstance.CLASSID_BLACKWIZARD_MALE:
			case L1PcInstance.CLASSID_BLACKWIZARD_FEMALE:
				return "幻術師";
			case L1PcInstance.CLASSID_WARRIOR_MALE:
			case L1PcInstance.CLASSID_WARRIOR_FEMALE:
				return "戰士";
			case L1PcInstance.CLASSID_FENCER_MALE:
			case L1PcInstance.CLASSID_FENCER_FEMALE:
				return "劍士";
			case L1PcInstance.CLASSID_LANCER_MALE:
			case L1PcInstance.CLASSID_LANCER_FEMALE:
				return "黃金槍騎";
		}
		return "全部";
	}
	
	public int GiveItem(final L1PcInstance pc) {
		_weapont_count = 0;
		Selector.exec("select * from beginner where activate='all' or activate=?", new SelectorHandler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, classid_to_db_name(pc.getClassId()));
			}
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()) {
					final int count = rs.getInt("count");
					if(count <= 0)
						continue;
					
					final int item_id = rs.getInt("item_id");
					
					L1Item tem = ItemTable.getInstance().getTemplate(item_id);
					if(tem == null) {
						System.out.println("試圖發放資料庫中不存在的物品。[" + item_id + "]");
						continue;
					}
					
					final String item_name = rs.getString("item_name");
					final int enchant_level = rs.getInt("enchantlvl");
					final int charge_count = rs.getInt("charge_count");
					final int bless = rs.getInt("bless");
					Updator.exec("INSERT INTO character_items SET id=?, item_id=?, char_id=?, item_name=?, count=?, is_equipped=?, enchantlvl=?, is_id=?, durability=?, charge_count=?, remaining_time=?, last_used=?, bless=?, attr_enchantlvl=?, special_enchant = 0", 
							new Handler() {
								@Override
								public void handle(PreparedStatement pstm) throws Exception {
									int idx = 0;
									pstm.setInt(++idx, IdFactory.getInstance().nextId());
									pstm.setInt(++idx, item_id);
									pstm.setInt(++idx, pc.getId());
									pstm.setString(++idx, item_name);
									pstm.setInt(++idx, count);
									if (item_id >= 22300 && item_id <= 22312 || item_id == 22337 || item_id == 203012
											|| item_id == 22073 || item_id == 22339 || item_id == 22338 || item_id == 48
											|| item_id == 120 || item_id == 147 || item_id == 156 || item_id == 174
											|| item_id == 175 || item_id == 35 || item_id == 505013
											|| item_id == 7000222) {
										if(item_id == 203012 || item_id == 48 || item_id == 120 
												|| item_id == 147 || item_id == 156 || item_id == 174
												 || item_id == 175 || item_id == 35 || item_id == 505013
													|| item_id == 7000222) {
											if(_weapont_count == 1) {
												pstm.setInt(++idx, 0);
											} else { 
												pstm.setInt(++idx, 1);
												_weapont_count++;
											}
										} else {
											pstm.setInt(++idx, 1);
										}
									} else {
										pstm.setInt(++idx, 0);
									}
									pstm.setInt(++idx, enchant_level);
									pstm.setInt(++idx, 1);
									pstm.setInt(++idx, 0);
									pstm.setInt(++idx, charge_count);
									pstm.setInt(++idx, 0);
									pstm.setTimestamp(++idx, null);
									pstm.setInt(++idx, bless);
									pstm.setInt(++idx, 0);
								}
					});
				}
			}
		});
		return 0;
	}
}