package l1j.server.MJWebServer.Dispatcher.Template.TradeBoard.API;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Comparator;

import com.google.gson.JsonObject;

import MJNCoinSystem.MJNCoinSettings;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_GOODS_INVEN_NOTI;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Character.MJUser;
import l1j.server.MJWebServer.Dispatcher.Template.TradeBoard.POJO.MJTradeBoardInfo;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Warehouse.SupplementaryService;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.serverpackets.S_LetterList;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SurvivalCry;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;

public class MJTradeBoardBuyResponse extends MJHttpResponse {
	private static String _action;
	private static String _message;
	private static int board_page_no;

	public MJTradeBoardBuyResponse(MJHttpRequest request) {
		super(request);

		int page_number = Integer.valueOf(request.get_request_uri().replace("/api/board/list/buy_", ""));
		board_page_no = page_number;
		MJTradeBoardInfo buy_info = MJTradeBoardInfo.tradeBoardInfoDatabasePaser(request.get_parameters(), board_page_no);
		try {
			if (checkCashInfo(buy_info, _user)) {
				updateTradeBoardInfo(buy_info, board_page_no);
			}
		}catch(Exception e) {
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

	private void updateTradeBoardInfo(MJTradeBoardInfo buy_info, int number) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE board_item_trade SET state=?, buy_account_name=?, buy_character_name=?, buy_result_date=?, commission=?, commission_ncoin=?, result_ncoin=? WHERE id=?");
			pstm.setString(1, "完成");
			pstm.setString(2, _player.getAccountName());
			pstm.setString(3, _player.getName());
			pstm.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			pstm.setString(5, String.valueOf(buy_info.commission * 100) + "%");
			pstm.setInt(6, buy_info.commission_coin);
			pstm.setInt(7, buy_info.result_ncoin);
			pstm.setInt(8, number);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	private boolean getTradeBoardResult(int number) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String result = "";
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_item_trade WHERE id=?");
			pstm.setInt(1, number);
			rs = pstm.executeQuery();
			if (rs.next()) {
				result = rs.getString("state");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return (result.equalsIgnoreCase("") || result.equalsIgnoreCase("完成")) ? false : true;
	}

	private String getReName(String name) {
		switch (name) {
			case "select_item":
				return "物品";
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
		}
		return "";
	}

	private boolean checkCashInfo(MJTradeBoardInfo info, MJUser user) {
		boolean pass_ok = true;

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
				if (pc.getAccount().validatePassword(pc.getAccountName(), info.account_pass)) {
					/**
					 必須檢查刪除N幣。
					 */
					if (pc.getAccount().getCPW() == null) {
						if (getTradeBoardResult(board_page_no)) {
							boolean deleteOk = _player.getAccount().Ncoin_point >= info.price;

							if (_player.isInvisble()) {
								pass_ok = false;
								_action = "error";
								_message = "透明狀態下無法進行。";
							} else if (deleteOk) {
								int insertItem_id = ItemTable.getInstance().findItemIdByNameWithoutSpace(info.item_name);
								if (insertItem_id != 0) {
									commit(pc, insertItem_id, info.item_count, info.item_enchant,
											Integer.valueOf(getReName(info.item_attr)), info.item_attr_level,
											Integer.valueOf(info.item_bless));
									pc.sendPackets(new S_SurvivalCry(S_SurvivalCry.OTP_CHECK_MSG, pc));
									pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\f3商品已正常結算。您購買的商品可在背包中的'附加物品倉庫'查看。"));
									pc.sendPackets("\f2商品已正常結算。您購買的商品可在背包中的'附加物品倉庫'查看。");
									pc.send_effect(2048, false);
									pc.addNcoin(-info.price);

									boolean load_account = true;
									double commission = pc.isGm() ? MJNCoinSettings.GM_COMMISSION : MJNCoinSettings.USER_COMMISSION;
									info.commission = commission;
									int _commission_coint = (int) (info.price * commission);
									info.commission_coin = _commission_coint;
									int add_ncoin = (int) (info.price - _commission_coint);
									info.result_ncoin = add_ncoin;
									for (L1PcInstance target : L1World.getInstance().getAllPlayers()) {
										if (target.noPlayerCK || target.getAccount() == null) {
											continue;
										}
										if (target.getAccount().getName().equalsIgnoreCase(info.char_account_name)) {
											target.addNcoin(add_ncoin);
											target.send_effect(2048, false);
											target.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\f3商品已正常售出。N幣收入可在應用中心查看。"));
											target.sendPackets("\f3商品已正常售出。N幣收入可在應用中心查看。");
											load_account = false;
											break;
										}
									}
									String current_date = MJString.get_current_datetime();
									do_write_letter(info.character_name, current_date, "[銷售通知] 銷售已完成。",
											to_provider(info, add_ncoin, commission));

									if (load_account) {
										Account.Account target_account = Account.Account.load(info.char_account_name);
										if (target_account != null) {
											target_account.Ncoin_point += add_ncoin;
											target_account.updateNcoin();
										} else {
											System.out.println("應用中心道具購買失敗，N幣充電失敗");
										}
									}
									_action = "login_ok";
									_message = "購買完成。";
								} else {
									pass_ok = false;
									_action = "error";
									_message = "道具生成失敗，請聯繫管理員。";
								}

							} else {
								pass_ok = false;
								_action = "error";
								_message = "N幣不足，無法購買。";
							}
						} else {
							pass_ok = false;
							_action = "error";
							_message = "這個道具已經售出了。";
						}
					} else {
						if (_player.isInvisble()) {
							pass_ok = false;
							_action = "error";
							_message = "透明狀態下無法進行。";
						} else if (pc.getAccount().getCPW().equals(info.account_secend_pass)) {
							if (getTradeBoardResult(board_page_no)) {
								boolean deleteOk = _player.getAccount().Ncoin_point >= info.price;

								if (deleteOk) {
									int insertItem_id = ItemTable.getInstance().findItemIdByNameWithoutSpace(info.item_name);
									if (insertItem_id != 0) {

										commit(pc, insertItem_id, info.item_count, info.item_enchant,
												Integer.valueOf(getReName(info.item_attr)), info.item_attr_level,
												Integer.valueOf(info.item_bless));
										pc.sendPackets(new S_SurvivalCry(S_SurvivalCry.OTP_CHECK_MSG, pc));
										pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\f3商品已成功支付。購買的商品可在背包中的“附加道具倉庫”中查看。"));
										pc.sendPackets("\f2商品已成功支付。購買的商品可在背包中的“附加道具倉庫”中查看。");
										pc.send_effect(2048, false);
										pc.addNcoin(-info.price);

										boolean load_account = true;
										double commission = pc.isGm() ? MJNCoinSettings.GM_COMMISSION : MJNCoinSettings.USER_COMMISSION;
										info.commission = commission;
										int _commission_coint = (int) (info.price * commission);
										info.commission_coin = _commission_coint;
										int add_ncoin = (int) (info.price - _commission_coint);
										info.result_ncoin = add_ncoin;
										for (L1PcInstance target : L1World.getInstance().getAllPlayers()) {
											if (target.noPlayerCK || target.getAccount() == null) {
												continue;
											}
											if (target.getAccount().getName().equalsIgnoreCase(info.char_account_name)) {
												target.addNcoin(add_ncoin);
												target.send_effect(2048, false);
												target.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\f3商品已成功售出。您可以在應用中心查看售出商品的N幣。"));
												target.sendPackets("\f3商品已成功售出。您可以在應用中心查看售出商品的N幣。");
												load_account = false;
												break;
											}
										}
										String current_date = MJString.get_current_datetime();
										do_write_letter(info.character_name, current_date, "[銷售通知] 銷售已完成。",
												to_provider(info, add_ncoin, commission));

										if (load_account) {
											Account.Account target_account = Account.Account.load(info.char_account_name);
											if (target_account != null) {
												target_account.Ncoin_point += add_ncoin;
												target_account.updateNcoin();
											} else {
												System.out.println("應用中心道具購買幣值充值失敗");
											}
										}

										_action = "login_ok";
										_message = "購買完成。";
									} else {
										pass_ok = false;
										_action = "error";
										_message = "生成道具失敗，請聯繫管理員。";
									}

								} else {
									pass_ok = false;
									_action = "error";
									_message = "N幣不足，無法購買。"
								}
							} else {
								pass_ok = false;
								_action = "login_ok";
								_message = "這件道具已經售出。"
							}
						} else {
							pass_ok = false;
							_action = "error";
							_message = "二次密碼資訊錯誤。"
						}
					}
				} else {
					pass_ok = false;
					_action = "error";
					_message = "帳戶資訊錯誤。"
				}
			}
		}

		return pass_ok;
	}

	protected void do_write_letter(String receiver, String generate_date, String subject, String content) {
		int id = LetterTable.getInstance().writeLetter(949, generate_date, "梅提斯", receiver, 0, subject, content);
		L1PcInstance pc = L1World.getInstance().getPlayer(receiver);
		if (pc != null) {
			pc.sendPackets(new S_LetterList(S_LetterList.WRITE_TYPE_PRIVATE_MAIL, id, S_LetterList.TYPE_RECEIVE, "梅提斯", subject));
			pc.send_effect(1091, false);
			pc.sendPackets(428);
		}
	}

	public String to_provider(MJTradeBoardInfo info, int real_price, double commission) {
		String content = "";
		return new StringBuilder(content.length() + 48)
				.append(content).append("\n").append("[銷售詳細內容]")
				.append("\n").append("N幣存入金額 : ").append(String.format("%,d", real_price))
				.append("\n").append("適用手續費 : ")
				.append(String.format("%,d(", info.price - real_price)).append((int) (commission * 100))
				.append("%)").append("感謝您的光臨，請再度使用。").toString();
	}

	public boolean commit(L1PcInstance pc, int itemid, int count, int enchant, int attr, int attr_en, int bless) {
		SupplementaryService pwh = WarehouseManager.getInstance().getSupplementaryService(pc.getAccountName());

		if (pwh == null)
			return false;

		if (attr == 2) {
			attr_en += 5;
		} else if (attr == 3) {
			attr_en += 10;
		} else if (attr == 4) {
			attr_en += 15;
		}

		if (attr == 0) {
			attr_en = 0;
		}

		L1Item tempItem = ItemTable.getInstance().getTemplate(itemid);
		if (tempItem.isStackable()) {
			L1ItemInstance item = ItemTable.getInstance().createItem(tempItem.getItemId());
			item.setIdentified(true);
			item.setEnchantLevel(enchant);
			item.setBless(bless);
			item.setCount(count);
			item.setAttrEnchantLevel(attr_en);
			pwh.storeTradeItem(item);
		} else {
			L1ItemInstance item = null;
			int createCount;
			for (createCount = 0; createCount < count; createCount++) {
				item = ItemTable.getInstance().createItem(tempItem.getItemId());
				item.setIdentified(true);
				item.setBless(bless);
				item.setEnchantLevel(enchant);
				item.setAttrEnchantLevel(attr_en);
				pwh.storeTradeItem(item);
			}
		}
		SC_GOODS_INVEN_NOTI.do_send(pc);
		return true;
	}

	public class DataComparator implements Comparator<Object> {
		public int compare(Object item1, Object item2) {
			return ((L1ItemInstance) item1).getEnchantLevel() - ((L1ItemInstance) item2).getEnchantLevel();
		}
	}
}
