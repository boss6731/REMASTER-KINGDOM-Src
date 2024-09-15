package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.Map;

import MJNCoinSystem.MJNCoinSettings;
import MJNCoinSystem.Commands.MJNCoinExecutor;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJTimesFormatter;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.service.character.MJMyRepresentativeService;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyShopService;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyTradeShopModel;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.GMCommands;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ManagerInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_PacketBox;

class MJMyApiTradeConfirmController extends MJMyApiController {
	
	private int tradeNo;
	MJMyApiTradeConfirmController(MJHttpRequest request) {
		super(request);
		parseParameters();
	}
	
	private void parseParameters(){
		Map<String, String> postDatas = request().get_post_datas();
		if(postDatas == null){
			return;
		}
		
		tradeNo = MJString.tryParseInt(postDatas.get("tradeNo"), -1);
	}
	
	@Override
	protected boolean isNeedLogin() {
		return true;
	}

	@Override
	protected MJMyModel responseModel() {
		if(tradeNo < 0){
			return failModel("�� �� ���� �ŷ��Դϴ�.");
		}

		String representative = MJMyRepresentativeService.service().selectRepresentativeCharacter(userInfo().account());
		if(MJString.isNullOrEmpty(representative)){
			return confirm("��ǥ�� ������ ĳ���Ͱ� ���� ���Ű� �Ұ����մϴ�.");
		}
		L1PcInstance pc = L1World.getInstance().getPlayer(representative);
		if(pc == null){
			return confirm(String.format("���忡 �������� ���� ��ǥ ĳ���ͷδ�\r\n�ŷ��� �Ұ����մϴ�.\r\n(ĳ���͸�: %s)", representative));
		}
		
		MJMyTradeShopModel shop = MJMyShopService.service().selectTradeShopModel(tradeNo);
		if(shop == null){			
			return confirm("�ش� �ŷ��� ���� �̿��� �� �����ϴ�. ��� �� �õ����ּ���.");
		}
		
		try{
			return confirmInternal(pc, shop);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			MJMyShopService.service().releaseTradeShopModel(tradeNo);
		}
		return confirm("��ְ� �߻��߽��ϴ�. ��� �� �̿����ּ���.");
	}
	
	private MJMyJsonModel confirmInternal(L1PcInstance pc, MJMyTradeShopModel shop){
		switch(shop.completed()){
		case CANCELED:
			return confirm("�̹� ��ҵ� �ŷ��Դϴ�.");
		case COMPLETE:
			return confirm("�̹� �Ϸ�� �ŷ��Դϴ�.");
		case ALIVE:
			Account account = pc.getAccount();
			if(account == null){
				return confirm("���� ���� �ε��� �����߽��ϴ�.");				
			}

			int confirmPrice = shop.itemModel().price();
			if(account.Ncoin_point < confirmPrice){
				return confirm("N������ �����մϴ�.");
			}
			account.Ncoin_point -= confirmPrice;
			account.updateNcoin();
			double commission = 0D;
			int fee = 0;
			if(shop.itemModel().itemId() == L1ItemId.ADENA){
				commission = pc.isGm() ? MJNCoinSettings.GM_COMMISSION : MJNCoinSettings.USER_COMMISSION;
			}
			if(commission > 0){
				fee = (int) Math.ceil(confirmPrice * commission);
				confirmPrice -= fee;
			}
			if(!shop.onBuy(pc, request().get_remote_address_string(), commission)){
				account.Ncoin_point += shop.itemModel().price();
				account.updateNcoin();
				System.out.println(String.format("%s(%s) �ŷ� ���� .���� Ȯ��(%,d����)", pc.getName(), account.getName(), shop.itemModel().price()));
				return confirm("�ŷ��� �����߽��ϴ�.");
			}

			shop.toItem(pc);
			String completeDate = MJTimesFormatter.BASIC.toString(shop.consumerModel().actionMillis());
			GMCommands.increaseNcoinshop(L1ManagerInstance.getInstance(), shop.registeredModel().characterName(), confirmPrice, shop);
			MJNCoinExecutor.do_write_letter(
					shop.registeredModel().characterName(),
					completeDate,
//					String.format("[�ǸſϷ�] %s", shop.itemModel().itemName()),
					String.format("[�ǸſϷ�] �ŷ��� ��ǰ"),
					String.format("N���� �Աݾ�:%,d\n���������:%,d(%d%%)\n\n�����մϴ�. �� �̿����ּ���.", confirmPrice, fee, (int)(commission * 100)));
			MJNCoinExecutor.do_write_letter(
					pc.getName(), 
					completeDate, 
//					String.format("[���ſϷ�] %s", shop.itemModel().itemName()),
					String.format("[���ſϷ�] �ŷ��� ��ǰ"),
					String.format("%s\n�հ�:%,d\n\n������ �������� �ΰ� ������ â���� Ȯ�����ּ���.\n�����մϴ�.", shop.displayPart(), shop.itemModel().price()));
					pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\aG�ۼ���(CTRL+Z) �ŷ��ҿ��� ��ǰ�� �����Ͽ� N������ ���� �Ǿ����ϴ�."));
					pc.sendPackets(String.format("\\aG�ŷ��ҿ��� ��ǰ�� �����Ͽ� N���� (%,d)���� ���� �Ǿ����ϴ�.", shop.itemModel().price()));
			
			return confirm("�ŷ��� �Ϸ� �Ǿ����ϴ�.", shop.itemModel().price(), account.Ncoin_point);
		}
		return confirm("�� �� ���� �ŷ��Դϴ�.");
	}
	
	private MJMyJsonModel confirm(String message){
		MJMyApiTradeConfirmModel model = new MJMyApiTradeConfirmModel();
		model.code = MJMyApiModel.SUCCESS;
		model.tradeNo = tradeNo;
		model.confirmMessage = message;
		return new MJMyJsonModel(request(), model, null);
	}
	
	private MJMyJsonModel confirm(String message, int useCoin, int remainCoin){
		MJMyApiTradeConfirmModel model = new MJMyApiTradeConfirmModel();
		model.code = MJMyApiModel.SUCCESS;
		model.confirmMessage = message;
		model.tradeNo = tradeNo;
		model.useCoin = useCoin;
		model.remainCoin = remainCoin;
		return new MJMyJsonModel(request(), model, null);
	}
}