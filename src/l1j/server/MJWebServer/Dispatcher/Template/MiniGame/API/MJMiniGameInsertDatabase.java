package l1j.server.MJWebServer.Dispatcher.Template.MiniGame.API;

import com.google.gson.Gson;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJMiniGameInsertDatabase extends MJHttpResponse {
	
	public MJMiniGameInsertDatabase(MJHttpRequest request) {
		super(request);
	}
	
	class ErrorResponse{
		public String result_code;
		public String message;
	}
	
	class SuccessResponse{
		public String result_code;
		public int usage_point;
		public int remain_point;
		public String item_name;
		public String item_url;
	}

	@Override
	if (pc == null) {
		ErrorResponse dInfo = new ErrorResponse();
		dInfo.result_code = "fail";
		dInfo.message = "哈哈哈哈";
		Gson gson = new Gson();
		document = gson.toJson(dInfo);
	} else {
		SuccessResponse resp = new SuccessResponse();
		resp.result_code = "success";
		resp.usage_point = (int) pc.get_exp();
		resp.remain_point = 100;
		resp.item_name = "道具";
		resp.item_url = "/mingameimg/10.gif";
		Gson gson = new Gson();
		document = gson.toJson(resp);
	}
