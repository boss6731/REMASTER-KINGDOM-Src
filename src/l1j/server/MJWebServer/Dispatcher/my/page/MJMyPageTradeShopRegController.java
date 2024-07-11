package l1j.server.MJWebServer.Dispatcher.my.page;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.MJMyBinaryModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyHtmlModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.page.MJMyPageMapped.MJMyPageInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyRepresentativeService;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

class MJMyPageTradeShopRegController extends MJMyPageController{
	
	MJMyPageTradeShopRegController(MJHttpRequest request, MJMyPageInfo pInfo) {
		super(request, pInfo);
	}

	@Override
	protected MJMyModel viewModelInternal() {
		String representative = MJMyRepresentativeService.service().selectRepresentativeCharacter(userInfo().account());
		if(MJString.isNullOrEmpty(representative)){
			return back("��ǥ�� ������ ĳ���Ͱ� ���� ��ǰ ����� �Ұ����մϴ�.");
		}
		L1PcInstance pc = L1World.getInstance().getPlayer(representative);
		if(pc == null){
			return back(String.format("���忡 �������� ���� ��ǥ ĳ���ͷδ�\r\n��ǰ ����� �Ұ����մϴ�.\r\n(ĳ���͸�: %s)", representative));
		}
		return new MJMyBinaryModel(request(), pInfo.pageBinary, MJMyHtmlModel.htmlContentType(), null);
	}
}