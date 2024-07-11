package l1j.server.MJWebServer.Dispatcher.Template.Coupon.API;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;

import com.google.gson.JsonObject;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJNetServer.Codec.MJNSHandler;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_GOODS_INVEN_NOTI;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Coupon.POJO.AppCouponChargeInfo;
import l1j.server.MJWebServer.Dispatcher.Template.Coupon.POJO.AppCouponInfo;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.Payment.MJPaymentInfo;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Warehouse.SupplementaryService;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;

public class MJCouponChargeInsertDatabase extends MJHttpResponse {
	private static String _action;
	private static String _message;
	private MJPaymentInfo m_pInfo;
	
	public MJCouponChargeInsertDatabase(MJHttpRequest request) {
		super(request);

		try {
			AppCouponChargeInfo cashInfo = AppCouponChargeInfo.CouponInfoPaser(request.get_parameters(), _user);
			AppCouponInfo info = AppCouponInfo.loadDatabaseCouponInfo();
			if (cashInfo != null) {
				checkCashInfo(cashInfo, info);
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

	private void insertCouponInfo(String code) {
		try {
			SupplementaryService pwh = WarehouseManager.getInstance().getSupplementaryService(_player.getAccountName());
			if (_player == null)
				return;
			if (pwh == null)
				return;
			m_pInfo = MJPaymentInfo.newInstance(code.toUpperCase());
			
			m_pInfo
			.set_account_name(_player.getAccountName())
			.set_character_name(_player.getName())
			.set_expire_date(MJNSHandler.getLocalTime())
			.set_is_use(true)
			.do_update();

			L1Item tempItem = ItemTable.getInstance().getTemplate(m_pInfo.get_itemid());
			if (tempItem.isStackable()) {
				L1ItemInstance item = ItemTable.getInstance().createItem(tempItem.getItemId());
				item.setIdentified(true);
				item.setCount(m_pInfo.get_count());
				pwh.storeTradeItem(item);
			} else {
				L1ItemInstance item = null;
				int createCount;
				for (createCount = 0; createCount < m_pInfo.get_count(); createCount++) {
					item = ItemTable.getInstance().createItem(tempItem.getItemId());
					item.setIdentified(true);
					pwh.storeTradeItem(item);
				}
			}

			// _player.getInventory().storeItem(m_pInfo.get_itemid(), m_pInfo.get_count()); // òÁïÈðíìýÛÎøÐ
			SC_GOODS_INVEN_NOTI.do_send(_player);
			_player.send_effect(2048);
			_player.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, String.format("\f3éÐû³ÏçÚªù¡ (%s) ËÁì«Û¡Û¯¡£ôëîñèÙÝ¾Ê¥Úªù¡óÚÍ·ÖÅö¢¡£", new DecimalFormat("#,##0").format(m_pInfo.get_count()))));
			_player.sendPackets(String.format("éÐû³ÏçÚªù¡ (%s) ËÁì«Û¡Û¯¡£", new DecimalFormat("#,##0").format(m_pInfo.get_count())));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean checkCashInfo(AppCouponChargeInfo info, AppCouponInfo cashinfo) {
		boolean pass_ok = true;
		if (info._coupon == null || info._coupon.length() < 1) {
			pass_ok = false;
			_action = "error";
			_message = "ÞªÛöîÜÛ¾Ùý¡£";
		} else {
			if (_player == null) {
				pass_ok = false;
				_action = "error";
				_message = "ÞªÛöîÜÛ¾Ùý¡£";
			} else {
				if (info._coupon == null) {
					pass_ok = false;
					_action = "error";
					_message = "ôëâÃìýéÐû³ÏçûÜØ§¡£";
				} else {
					if (Check_Coupon(info._coupon)) {
						if (Check_Coupon_use(info._coupon)) {
							_action = "login_ok";
							_message = "éÐû³Ïçà÷ÍíÞÅéÄ¡£";
							insertCouponInfo(info._coupon);
						} else {
							_action = "error";
							_message = "éÐû³Ïçì«ù¬ÞÅéÄ¡£";
						}
					} else {
						_action = "error";
						_message = "Ú±ñÉóüîÜéÐû³Ïç¡£";
					}
				}
			}
		}
		return pass_ok;
	}

		return pass_ok;
	}
	
	private boolean Check_Coupon(String coupon) {
		PreparedStatement pstm = null;
		ResultSet rs = null;
		java.sql.Connection con = null;
		try {
			String code = null;
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select code from payment_info where code Like '"+coupon+"'");
			rs = pstm.executeQuery();	
			if (rs.next()) {
				code = rs.getString(1);
			}
			if (code != null) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return false;
	}
	
	private boolean Check_Coupon_use(String coupon) {
		PreparedStatement pstm = null;
		ResultSet rs = null;
		java.sql.Connection con = null;
		try {
			int code = 0;
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select is_use from payment_info where code Like '"+coupon+"'");
			rs = pstm.executeQuery();		
			if (rs.next()) {
				code = rs.getInt(1);
			}
			if (code > 0) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return true;
	}
}
