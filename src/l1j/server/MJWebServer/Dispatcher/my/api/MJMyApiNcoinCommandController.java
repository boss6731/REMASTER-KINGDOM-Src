package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.Map;

import MJNCoinSystem.MJNCoinDepositInfo;
import MJNCoinSystem.Commands.MJCoinCompleteDepositExecutor;
import MJNCoinSystem.Commands.MJNCoinExecutor;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.service.ncoin.MJMyNcoinCategory;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.GMCommands;
import l1j.server.server.model.Instance.L1ManagerInstance;

class MJMyApiNcoinCommandController extends MJMyApiController{

	private int commandId;
	private int articleId;
	MJMyApiNcoinCommandController(MJHttpRequest request) {
		super(request);
		parseParameters();
	}

	private void parseParameters(){
		Map<String, String> postDatas = request().get_post_datas();
		if(postDatas == null){
			return;
		}
		
		commandId = MJString.tryParseInt(postDatas.get("commandId"), -1);
		articleId = MJString.tryParseInt(postDatas.get("articleId"), -1);
	}
	
	@Override
	protected boolean isNeedLogin() {
		return true;
	}

	@Override
	protected MJMyModel responseModel() {
		if(!gm()){
			return notFound();
		}
		
		if(articleId < 0 || commandId < 0){
			return failModel(String.format("invalid ncoin command parameters. commandId:%d, articleId:%d", commandId, articleId));
		}
		
		MJNCoinDepositInfo dInfo = MJNCoinDepositInfo.from_deposit_id(articleId);
		if(dInfo == null){
			return failModel(String.format("���� ������ �����ϴ�. %d", articleId));
		}
		
		if(dInfo.is_deposit() == 1){
			return failModel("�̹� ��� �Ϸ�� �����Դϴ�.");
		}
		
		switch(commandId){
		case MJCoinCompleteDepositExecutor.ALREADY:
			fireCommand(dInfo, "[�������]", "���������·� ��ȯ�Ǿ����ϴ�.");
			break;
		case MJCoinCompleteDepositExecutor.COMPLETE:
			int deposit = dInfo.is_deposit();
			if(deposit == 2){
				return failModel("�̹� ��� ��ҵ� �����Դϴ�.");
			}
			fireCommand(dInfo, "[�����Ϸ�]", "������ �Ϸ� �Ǿ����ϴ�.\nȮ���Ͻñ� �ٶ��ϴ�.");
			break;
		case MJCoinCompleteDepositExecutor.CANCEL:
			fireCommand(dInfo, "[�������]", "������û�� ��ҵǾ����ϴ�.");
			break;
		default:
			return failModel(String.format("�� �� ���� ����Դϴ�. {\"articleId\": %d, \"commandId\": %d}", articleId, commandId));
		}
		
		MJMyApiNcoinCommandModel model = new MJMyApiNcoinCommandModel();
		model.code = MJMyApiModel.SUCCESS;
		model.depositId = articleId;
		model.category = commandId;
		model.categoryTitle = MJMyNcoinCategory.findCategory(commandId).categoryTitle();
		return new MJMyJsonModel(request(), model, null);
	}
	
	private void fireCommand(MJNCoinDepositInfo dInfo, String subject, String content){
		String current_date = MJString.get_current_datetime();
		MJNCoinDepositInfo.update_is_deposit(articleId, commandId, current_date);
		if(MJCoinCompleteDepositExecutor.COMPLETE == commandId){
			GMCommands.increaseNcoin(L1ManagerInstance.getInstance(), dInfo.get_character_name(), dInfo.get_ncoin_value());
		}
		MJNCoinExecutor.do_write_letter(dInfo.get_character_name(), current_date, subject, content);
	}

}
