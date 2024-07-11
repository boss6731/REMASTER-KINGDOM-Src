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
			sb.append("�̻��� ��ȣ�� ������ �Ǿ����ϴ�. [" + _balance +"]");
			System.out.println("����ڵ�����ȣ�� �ƴ� �ٸ� ��ȣ���� ������ ���޵Ǿ����ϴ�.");
		} else {
			AutoCashUserInfo user_aci = AutoCashResultDatabase.getIntstance().getAutoCashUserInfo(_user);

			if (user_aci == null) {
				user_aci = new AutoCashUserInfo();
				user_aci.setCharName("");
				user_aci.setAccountName("");
				user_aci.setCash(Integer.valueOf(_cash));

				AutoCashResultDatabase.getIntstance().updateCashNotResult(_user, user_aci);

				sb.append("�Ա��ڿ� ���õ� ĳ���͸� ã�� �� ���� ��� ����մϴ�.");
				System.out.println("�Ա��ڿ� ���õ� ĳ���͸� ã�� �� ���� ��� ����մϴ�.");
			} else {
				user_aci.setCash(Integer.valueOf(_cash));
				AutoCashInfo aci = AutoCashResultDatabase.getIntstance().getCashInfo(Integer.valueOf(_cash));
				if (aci == null) {
					AutoCashResultDatabase.getIntstance().updateCashNotResult(_user, user_aci);

					sb.append("�ݾ׿� ���õ� ���޾������� ã�� �� ���� ��� ����մϴ�.");
					System.out.println("�ݾ׿� ���õ� ���޾������� ã�� �� ���� ��� ����մϴ�.");
				} else {
					L1PcInstance target = L1World.getInstance().getPlayer(user_aci.getCharName());
					if (target == null) {
						SupplementaryService pwh = WarehouseManager.getInstance().getSupplementaryService(user_aci.getAccountName());
						L1ItemInstance item = ItemTable.getInstance().createItem(aci.getItemId());
						if (item != null) {
							item.setIdentified(true);
							item.setCount(aci.getCount());

							pwh.storeTradeItem(item);

							sb.append("ĳ���Ͱ� ���ӵǾ����� �ʾ� �ΰ�������â��� �����մϴ�.");
							System.out.println("ĳ���Ͱ� ���ӵǾ����� �ʾ� �ΰ�������â��� �����մϴ�.");
						}
					} else {
						target.getInventory().storeItem(aci.getItemId(), aci.getCount());
						target.sendPackets("�Ŀ���� : [" + _cash + "] �� ������ [" + aci.getItemName() + "] ��(��) <" + user_aci.getCharName() + ">���� ���޵Ǿ����ϴ�.");
						sb.append("[" + _cash + "] �� ������ [" + aci.getItemName() + "] ��(��) <" + user_aci.getCharName() + ">���� ���޵Ǿ����ϴ�.");
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
