package l1j.server.MJWebServer.Dispatcher.Template.TradeBoard.API;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import com.google.gson.JsonObject;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Character.MJUser;
import l1j.server.MJWebServer.Dispatcher.Template.TradeBoard.POJO.MJTradeBoardInfo;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class MJTradeBoardInsertDatabase extends MJHttpResponse {
	private static String _action;
	private static String _message;

	public MJTradeBoardInsertDatabase(MJHttpRequest request) {
		super(request);

		try {
			// TODO 登錄時檢查詳細信息
//			 System.out.println(request.get_parameters());

			MJTradeBoardInfo tbi = MJTradeBoardInfo.tradeBoardInfoPaser(request.get_parameters(), _user);

			if (tbi != null) {
				if (checkCashInfo(tbi, _user)) {
					insertTradeBoardInfo(tbi, _user);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		JsonObject json = new JsonObject();
		json.addProperty(_action, _message);

		HttpResponse response = create_response(HttpResponseStatus.OK, json.toString());
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");

		return response;
	}

	private void insertTradeBoardInfo(MJTradeBoardInfo info, MJUser user) {
		Connection con = null;
		try {

			L1PcInstance pc = L1World.getInstance().getPlayer(_user.getCharName());
			if (pc == null)
				return;

			con = L1DatabaseFactory.getInstance().getConnection();
			String sql = "INSERT INTO board_item_trade (category, state, char_account_name, character_name, title, price, item_name, item_count, item_enchant, item_bless, item_attr, item_attr_level, insert_date) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
					+ "ON DUPLICATE KEY UPDATE category=?, state=?,char_account_name=?,character_name=?,title=?,price=?,item_name=?,item_count=?,item_enchant=?,item_bless=?,item_attr=?,item_attr_level=?,insert_date=?";

			SQLUtil.execute(con, sql, new Object[] { getReName(info.category), info.state, pc.getAccount().getName(),
					pc.getName(), info.title, info.price, info.item_name, info.item_count, info.item_enchant,
					getReName(info.item_bless), getReName(info.item_attr), info.item_attr_level,
					new Timestamp(System.currentTimeMillis()),

					getReName(info.category), info.state, pc.getAccount().getName(), pc.getName(), info.title,
					info.price, info.item_name, info.item_count, info.item_enchant, getReName(info.item_bless),
					getReName(info.item_attr), info.item_attr_level, new Timestamp(System.currentTimeMillis()) });
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(con);
		}
	}

	private String getReName(String name) {
		switch (name) {
			case "select_item":
				return "道具";
			case "select_adena":
				return "金幣";
			case "bless_normal":
				return "1";
			case "bless_bless":
				return "0";
			case "bless_curse":
				return "2";
			case "attr_fire":
				return "火屬性";
			case "attr_wind":
				return "風屬性";
			case "attr_water":
				return "水屬性";
			case "attr_earth":
				return "地屬性";
			case "attr_none":
				return "無屬性";
			case "火屬性":
				return "1";
			case "水屬性":
				return "2";
			case "風屬性":
				return "3";
			case "地屬性":
				return "4";
			case "無屬性":
				return "0";
			case "":
				return "0";
			default:
				return "";
		}
	}

	private boolean checkCashInfo(MJTradeBoardInfo info, MJUser user) {
		boolean pass_ok = true;
		try {
			if (user.getCharName() == null || user.getCharName().length() < 1) {
				pass_ok = false;
				_action = "error";
				_message = "非正常的訪問。";
			} else {
				L1PcInstance pc = L1World.getInstance().getPlayer(user.getCharName());
				if (pc == null) {
					pass_ok = false;
					_action = "error";
					_message = "非正常的訪問。";
				} else {
					if (_player.isInvisible()) {
						pass_ok = false;
						_action = "error";
						_message = "在隱身狀態下無法執行此操作。";
					} else if (pc.getAccount().validatePassword(pc.getAccountName(), info.account_pass)) {
						/**
						 * 需要檢查道具刪除。
						 */

						if (pc.getAccount().getCPW() == null) {
							int insertItem_id = ItemTable.getInstance().findItemIdByNameWithoutSpace(info.item_name, Integer.valueOf(getReName(info.item_bless)));
							L1ItemInstance item = _player.getInventory().findItemId(insertItem_id);

							int result_code = 0;
							if (insertItem_id == 40308) {
								if (!_player.getInventory().consumeItem(insertItem_id, info.item_count)) {
									result_code = 1;
								}
							} else {
								int bless = Integer.valueOf(getReName(info.item_bless));
								int attr_type = Integer.valueOf(getReName(info.item_attr) == "無" ? "0" : getReName(getReName(info.item_attr)));
								result_code = getAdvanceItemDeleteByInventory(_player, insertItem_id, info.item_count, info.item_enchant, attr_type, info.item_attr_level, bless, item);
							}

							if (result_code == 0) {
								_action = "login_ok";
								_message = "物品註冊已完成。";
							} else if (result_code == 1) {
								pass_ok = false;
								_action = "error";
								_message = "物品或數量不足。";
							} else if (result_code == 2) {
								pass_ok = false;
								_action = "error";
								_message = "此物品無法交易。";
							} else if (result_code == 3) {
								pass_ok = false;
								_action = "error";
								_message = "此物品正穿戴中。";
								return false;
							} else if (result_code == 4) {
								pass_ok = false;
								_action = "error";
								_message = "背包中數量不足或不存在。";
							} else if (result_code == 5) {
								pass_ok = false;
								_action = "error";
								_message = "存有刻印或相同物品時，必須交由倉庫管理，只能保留一個。";
							} else if (result_code == 6) {
								pass_ok = false;
								_action = "error";
								_message = "屬性檢查錯誤。";
							}
						} else {
							if (_player.isInvisble()) {
								pass_ok = false;
								_action = "error";
								_message = "在透明狀態下是不可能的.";
							} else if (pc.getAccount().getCPW().equals(info.account_secend_pass)) {
								int insertItem_id = ItemTable.getInstance().findItemIdByNameWithoutSpace(info.item_name, Integer.valueOf(getReName(info.item_bless)));

								L1ItemInstance item = _player.getInventory().findItemId(insertItem_id);

								int result_code = 0;
								if (insertItem_id == 40308) {
									if (!_player.getInventory().consumeItem(insertItem_id, info.item_count)) {
										result_code = 1;
									}
								} else {
									int bless = Integer.valueOf(getReName(info.item_bless));
									int attr_type = Integer.valueOf(getReName(info.item_attr) == "無" ? "0" : getReName(getReName(info.item_attr)));
									result_code = getAdvanceItemDeleteByInventory(_player, insertItem_id, info.item_count, info.item_enchant, attr_type, info.item_attr_level, bless, item);
								}

								if (result_code == 0) {
									_action = "login_ok";
									_message = "物品註冊已完成。";
								} else if (result_code == 1) {
									pass_ok = false;
									_action = "error";
									_message = "物品/數量/屬性與填寫不符。";
								} else if (result_code == 2) {
									pass_ok = false;
									_action = "error";
									_message = "無法交易的物品。";
								} else if (result_code == 3) {
									pass_ok = false;
									_action = "error";
									_message = "正穿戴的物品。";
									return false;
								} else if (result_code == 4) {
									pass_ok = false;
									_action = "error";
									_message = "庫存數量不足或不存在。";
								} else if (result_code == 5) {
									pass_ok = false;
									_action = "error";
									_message = "存在刻印或相同物品時，必須寄存於倉庫，只能保留一件。";
								} else if (result_code == 6) {
									pass_ok = false;
									_action = "error";
									_message = "屬性檢查有誤。";
								} else {
									pass_ok = false;
									_action = "error";
									_message = "二次密碼資訊錯誤。";
								}
							}
						} else{
							pass_ok = false;
							_action = "error";
							_message = "帳戶信息錯誤。";
						}
					}
				}
			} catch(Exception e){
				System.out.println("MJTradeBoardInsertDatabase:" + m_request.get_remote_address().toString() + " " + pass_ok);
				e.printStackTrace();
				pass_ok = false;
				_action = "error";
				_message = "請聯繫管理員。";
			}

			return pass_ok;

	} catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

        private int getAdvanceItemDeleteByInventory(L1PcInstance pc, int itemid, int count, int en, int attr, int attr_en,
			int bless, L1ItemInstance tem) {
		if (count <= 0 || itemid <= 0) {
			return 4;
		}
		
		if (tem == null)
			return 4;
		
		if (tem.isEquipped())
			return 3;

		if (!tem.getItem().isTradable()) 
			return 2;
		
		if (tem.get_Carving() != 0) 
			return 5;
		
		if (attr == 0 && attr_en > 0)
			return 6;

		if (attr == 2) {
			attr_en += 5;
		} else if (attr == 3) {
			attr_en += 10;
		} else if (attr == 4) {
			attr_en += 15;
		}

		if (ItemTable.getInstance().getTemplate(itemid).isStackable()) {
			ArrayList<L1ItemInstance> items = pc.getInventory().findItemIds(itemid);
			if (items.size() > 0) {
				for (L1ItemInstance item : items) {
					if (item != null) {
						boolean isdescid = item.getItem().getItemId() == itemid ? true : false;
						boolean isenchant = item.getEnchantLevel() == en ? true : false;
						boolean isattrtype = item.getAttrEnchantLevel() == attr_en ? true : false;
						boolean isbless = item.getBless() == bless ? true : false;
						if (isdescid && isenchant && isattrtype && isbless) {
							pc.getInventory().removeItem(item, count);
							return 0;
						}
					}
				}
			}
		} else {
			L1ItemInstance[] itemList = pc.getInventory().findItemsId(itemid);
			if (itemList.length == count) {
				int j = 0;
				for (int i = 0; i < count; ++i) {
					boolean isdescid = itemList[i].getItem().getItemId() == itemid ? true : false;
					boolean isenchant = itemList[i].getEnchantLevel() == en ? true : false;
					boolean isattrtype = itemList[i].getAttrEnchantLevel() == attr_en ? true : false;
					boolean isbless = itemList[i].getBless() == bless ? true : false;
					if (isdescid && isenchant && isattrtype && isbless) {
						pc.getInventory().removeItem(itemList[i], 1);
						if (++j == count)
							return 0;
					}
				}
			} else if (itemList.length > count) {
				DataComparator dc = new DataComparator();
				extracted(itemList, dc);
				int j = 0;
				for (int i = 0; i < itemList.length; ++i) {
					boolean isdescid = itemList[i].getItem().getItemId() == itemid ? true : false;
					boolean isenchant = itemList[i].getEnchantLevel() == en ? true : false;
					boolean isattrtype = itemList[i].getAttrEnchantLevel() == attr_en ? true : false;
					boolean isbless = itemList[i].getBless() == bless ? true : false;
					if (isdescid && isenchant && isattrtype && isbless) {
						pc.getInventory().removeItem(itemList[i], 1);
						if (++j == count)
							return 0;
					}
				}
			}
		}
		return 1;
	}

	private void extracted(L1ItemInstance[] itemList, DataComparator dc) {
		Arrays.sort(itemList, dc);
	}

	public class DataComparator implements Comparator<Object> {
		public int compare(Object item1, Object item2) {
			return ((L1ItemInstance) item1).getEnchantLevel() - ((L1ItemInstance) item2).getEnchantLevel();
		}
	}
}
