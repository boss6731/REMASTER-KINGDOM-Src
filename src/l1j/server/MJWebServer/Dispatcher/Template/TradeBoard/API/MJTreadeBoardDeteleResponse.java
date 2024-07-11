package l1j.server.MJWebServer.Dispatcher.Template.TradeBoard.API;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.google.gson.JsonObject;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_GOODS_INVEN_NOTI;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Warehouse.SupplementaryService;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;

public class MJTreadeBoardDeteleResponse extends MJHttpResponse {
	private static int board_page_no;
	private static String _action;
	private static String _message;

	public MJTreadeBoardDeteleResponse(MJHttpRequest request) {
		super(request);

		int page_number = Integer.valueOf(request.get_request_uri().replace("/api/board/list/delete/", ""));
		board_page_no = page_number;

		if (_player.isGm() && checkGmDeteleAccountSame(board_page_no)) {
			getGmTradeBoardDetailInfoDelete(board_page_no);
		} else if (!_player.isGm() && checkDeteleAccountSame(board_page_no)) {
			getTradeBoardDetailInfoDelete(board_page_no);
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
	
	private void getGmTradeBoardDetailInfoDelete(int board_id) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM board_item_trade WHERE id=?");
			pstm.setInt(1, board_id);
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	private void getTradeBoardDetailInfoDelete(int board_id) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM board_item_trade WHERE id=? AND char_account_name=?");
			pstm.setInt(1, board_id);
			pstm.setString(2, _player == null ? "" : _player.getAccount().getName());
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	private boolean checkGmDeteleAccountSame(int board_id) {
		boolean pass_ok = true;

		if (_player == null) {
			pass_ok = false;
			_action = "error";
			_message = "非法訪問。"; // "정상적인 접근이 아닙니다。" => "非法訪問。"
		} else {

			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("SELECT * FROM board_item_trade WHERE id=?");
				pstm.setInt(1, board_id);
				rs = pstm.executeQuery();
				if (rs.next()) {
					int insertItem_id = ItemTable.getInstance().findItemIdByNameWithoutSpace(rs.getString("item_name"));
					commit(_player, insertItem_id, rs.getInt("item_count"), rs.getInt("item_enchant"),
							Integer.valueOf(getReName(rs.getString("item_attr"))), rs.getInt("item_attr_level"),
							rs.getInt("item_bless"));
					_player.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\f3商品已成功取消銷?處理。")); // "상품이 정상적으로 판매취소 처리 되었습니다。" => "商品已成功取消銷?處理。"
					_action = "delete_ok";
					_message = "該帖子已被取消。"; // "해당 글이 취소 되었습니다。" => "該帖子已被取消。"
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				SQLUtil.close(rs, pstm, con); // 關閉結果集、預處理語句和連接
			}
		}

		return pass_ok;
	}

	private boolean checkDeteleAccountSame(int board_id) {
		boolean pass_ok = true;

		if (_player == null) {
			pass_ok = false;
			_action = "error";
			_message = "非法訪問。"; // "정상적인 접근이 아닙니다。" => "非法訪問。"
		} else {

			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			try {
				con = L1DatabaseFactory.getInstance().getConnection(); // 獲取資料庫連接
				pstm = con.prepareStatement("SELECT * FROM board_item_trade WHERE id=? AND char_account_name=?"); // 準備 SQL 查詢語句
				pstm.setInt(1, board_id); // 設置查詢參數 board_id
				pstm.setString(2, _player == null ? "" : _player.getAccount().getName()); // 設置查詢參數賬戶名
				rs = pstm.executeQuery(); // 執行查詢
				if (rs.next()) {
					if (rs.getString("state").equalsIgnoreCase("판매")) { // 如果狀態是 "銷售"
						int insertItem_id = ItemTable.getInstance().findItemIdByNameWithoutSpace(rs.getString("item_name")); // 查找物品 ID
						commit(_player, insertItem_id, rs.getInt("item_count"), rs.getInt("item_enchant"),
								Integer.valueOf(getReName(rs.getString("item_attr"))), rs.getInt("item_attr_level"),
								rs.getInt("item_bless")); // 提交物品信息
						_player.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\f3商品已成功取消銷售處理。")); // "상품이 정상적으로 판매취소 처리 되었습니다。" => "商品已成功取消銷售處理。"
						_action = "delete_ok";
						_message = "該帖子已被取消。"; // "해당 글이 취소 되었습니다。" => "該帖子已被取消。"
					} else {
						pass_ok = false;
						_action = "error";
						_message = "銷售已完成。"; // "이미 판매가 완료된 상태 입니다。" => "銷售已完成。"
					}
				} else {
					pass_ok = false;
					_action = "error";
					_message = "這不是您註冊的物品。"; // "본인이 등록한 아이템이 아닙니다。" => "這不是您註冊的物品。"
				}
			} catch (Exception e) {
				e.printStackTrace(); // 打印異常堆棧跟蹤
			} finally {
				SQLUtil.close(rs, pstm, con); // 關閉結果集、預處理語句和連接
			}
		}

		return pass_ok;
	}

	private String getReName(String name) {
		switch (name) {
			case "select_item":
				return "物品"; // "아이템" => "物品"
			case "select_adena":
				return "金幣"; // "아데나" => "金幣"
			case "bless_normal":
				return "1";
			case "bless_bless":
				return "0";
			case "bless_curse":
				return "2";
			case "attr_fire":
				return "火靈"; // "화령" => "火靈"
			case "attr_wind":
				return "風靈"; // "풍령" => "風靈"
			case "attr_water":
				return "水靈"; // "수령" => "水靈"
			case "attr_earth":
				return "地靈"; // "지령" => "地靈"
			case "attr_none":
				return "無"; // "없음" => "無"
			case "火靈":
				return "1";
			case "水靈":
				return "2";
			case "風靈":
				return "3";
			case "地靈":
				return "4";
			case "無":
				return "0";
			case "":
				return "0";
		}
		return "";
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
}
