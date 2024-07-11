package l1j.server.MJWebServer.Dispatcher.PhoneApp;

import java.util.Map;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.Config;
import l1j.server.MJWebServer.Dispatcher.MJHttpClosedException;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Warehouse.SupplementaryService;
import l1j.server.server.model.Warehouse.WarehouseManager;

public class AutoCashResultResponse extends MJHttpResponse {
	private static String m_page_document;
	private String _cash;
	private String _user;
	private String _balance;

	public AutoCashResultResponse(MJHttpRequest request) {
		super(request);

		Map<String, String> post_datas = m_request.get_post_datas();
		// System.out.println("\r\npost data");
		for (String k : post_datas.keySet()) {
			switch (k) {
			case "Cash":
				_cash = post_datas.get(k);
				break;
			case "CharaterName":
				_user = post_datas.get(k);
				break;
			case "PhoneNumber":
				_balance = post_datas.get(k);
				break;
			}
		}
	}

	@Override
	public HttpResponse get_response() {

		StringBuilder sb = new StringBuilder();
		
		if (!_balance.equalsIgnoreCase(Config.Login.ServerGmPhoneNumber)) {
			sb.append("이상한 번호로 전송이 되었습니다. [" + _balance +"]");
			System.out.println("운영자핸드폰번호가 아닌 다른 번호에서 정보가 전달되었습니다.");
		} else {
			AutoCashUserInfo user_aci = AutoCashResultDatabase.getIntstance().getAutoCashUserInfo(_user);

			if (user_aci == null) {
				user_aci = new AutoCashUserInfo();
				user_aci.setCharName("");
				user_aci.setAccountName("");
				user_aci.setCash(Integer.valueOf(_cash));

				AutoCashResultDatabase.getIntstance().updateCashNotResult(_user, user_aci);

				sb.append("입금자와 관련된 캐릭터를 찾을 수 없어 디비에 기록합니다.");
				System.out.println("입금자와 관련된 캐릭터를 찾을 수 없어 디비에 기록합니다.");
			} else {
				user_aci.setCash(Integer.valueOf(_cash));
				AutoCashInfo aci = AutoCashResultDatabase.getIntstance().getCashInfo(Integer.valueOf(_cash));
				if (aci == null) {
					AutoCashResultDatabase.getIntstance().updateCashNotResult(_user, user_aci);

					sb.append("금액에 관련된 지급아이템을 찾을 수 없어 디비에 기록합니다.");
					System.out.println("금액에 관련된 지급아이템을 찾을 수 없어 디비에 기록합니다.");
				} else {
					L1PcInstance target = L1World.getInstance().getPlayer(user_aci.getCharName());
					if (target == null) {
						SupplementaryService pwh = WarehouseManager.getInstance().getSupplementaryService(user_aci.getAccountName());
						L1ItemInstance item = ItemTable.getInstance().createItem(aci.getItemId());
						if (item != null) {
							item.setIdentified(true);
							item.setCount(aci.getCount());

							pwh.storeTradeItem(item);

							sb.append("캐릭터가 접속되어있지 않아 부가아이템창고로 지급합니다.");
							System.out.println("캐릭터가 접속되어있지 않아 부가아이템창고로 지급합니다.");
						}
					} else {
						target.getInventory().storeItem(aci.getItemId(), aci.getCount());
						target.sendPackets("후원결과 : [" + _cash + "] 의 아이템 [" + aci.getItemName() + "] 가(이) <" + user_aci.getCharName() + ">에게 지급되었습니다.");
						sb.append("[" + _cash + "] 의 아이템 [" + aci.getItemName() + "] 가(이) <" + user_aci.getCharName() + ">에게 지급되었습니다.");
					}

					AutoCashResultDatabase.getIntstance().updateLogCashResult(_user, user_aci);
				}
			}
		}

		m_page_document = sb.toString();
		HttpResponse response = null;
		try {
			response = create_response(HttpResponseStatus.OK, m_page_document);
		} catch (MJHttpClosedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}
}
