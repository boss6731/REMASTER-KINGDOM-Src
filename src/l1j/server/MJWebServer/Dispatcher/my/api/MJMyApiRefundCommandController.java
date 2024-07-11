package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.Map;

import MJNCoinSystem.MJNCoinRefundInfo;
import MJNCoinSystem.Commands.MJNCoinExecutor;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.service.refund.MJMyRefundCategory;
import l1j.server.MJWebServer.Service.MJHttpRequest;

class MJMyApiRefundCommandController extends MJMyApiController{
	private int commandId;
	private int articleId;
	protected MJMyApiRefundCommandController(MJHttpRequest request) {
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
			return failModel(String.format("invalid refund command parameters. commandId:%d, articleId:%d", commandId, articleId));
		}
		
		MJNCoinRefundInfo nInfo = MJNCoinRefundInfo.from_refund_id(articleId);
		if(nInfo == null){
			return failModel(String.format("출금 정보가 없습니다. %d", articleId));
		}
		if(nInfo.get_is_refund()){
			return failModel(String.format("이미 출금 완료된 정보입니다. %d", articleId));
		}
		
		fireCommand(nInfo, "출금이 완료 되었습니다.\n확인하시길 바랍니다", "[출금완료]");
		MJMyApiRefundCommandModel model = new MJMyApiRefundCommandModel();
		model.code = MJMyApiModel.SUCCESS;
		model.refundId = articleId;
		model.category = commandId;
		model.categoryTitle = MJMyRefundCategory.findCategory(commandId).categoryTitle();
		return new MJMyJsonModel(request(), model, null);
	}
	
	private void fireCommand(MJNCoinRefundInfo nInfo, String content, String subject){
		String current_date = MJString.get_current_datetime();
		MJNCoinRefundInfo.update_is_refund(articleId, true, current_date);
		MJNCoinExecutor.do_write_letter(nInfo.get_character_name(), current_date, subject, content);
	}
	
}
