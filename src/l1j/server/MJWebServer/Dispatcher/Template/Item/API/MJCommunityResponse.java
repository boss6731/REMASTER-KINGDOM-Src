package l1j.server.MJWebServer.Dispatcher.Template.Item.API;

import com.google.gson.Gson;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Item.POJO.MJItemCommunityInfo;
import l1j.server.MJWebServer.Dispatcher.Template.Item.POJO.MJItemCommunityInfo.ItemCommunityInfo;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJCommunityResponse  extends MJHttpResponse{
	public MJCommunityResponse(MJHttpRequest request) {
		super(request);
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		MJItemCommunityInfo community = new MJItemCommunityInfo();
		ItemCommunityInfo cInfo = new ItemCommunityInfo();
		cInfo.articleId = 3928283;
		cInfo.emoticonUrl = "";
		cInfo.categoryId = 155;
		cInfo.categoryName = "出售";
		cInfo.serverId = 16;
		cInfo.serverName = "어레인";
		cInfo.title = "★★各種物品買賣★★";
		cInfo.summary = "!!! 代購和代辦 任何物品都可尋找，根據物品可以議價&nbsp;※歡迎最高價整件收購※&nbsp;24小時隨時&nbsp;請聯繫&nbsp;留言&nbsp;留言給我&nbsp;馬上回覆&nbsp;如果沒留言請私聊&nbsp;簡易搜索方法: 按下 Ctrl+F 搜索&nbsp;各種交易零風險 ^^// 即使未列出的物品&nbsp;";
		cInfo.link = "/board/market/view?articleId=3928283&categoryId=155";
		community.list.add(cInfo);

		// 返回 HTTP 响应，假设 createResponse 是您定义的方法
		return createResponse(community);
	}

	cInfo = new ItemCommunityInfo();
	cInfo.articleId = 3928252;
	cInfo.emoticonUrl = "";
	cInfo.categoryId = 156;
	cInfo.categoryName = "購買";
	cInfo.serverId = 16;
	cInfo.serverName = "艾瑞恩";
	cInfo.title = "買10祝福水晶";
	cInfo.summary = "請留言或發信";
	cInfo.link = "/board/market/view?articleId=3928252&categoryId=156";
community.list.add(cInfo);

	cInfo = new ItemCommunityInfo();
	cInfo.articleId = 3928066;
	cInfo.emoticonUrl = "";
	cInfo.categoryId = 156;
	cInfo.categoryName = "購買";
	cInfo.serverId = 16;
	cInfo.serverName = "艾瑞恩";
	cInfo.title = "全金幣7零售";
	cInfo.summary = "以2.4買7制品全阿登，商人請勿留言，價格高於2.5的請勿打擾，僅限賣家留言";
	cInfo.link = "/board/market/view?articleId=3928066&categoryId=156";
	community.list.add(cInfo);

	cInfo = new ItemCommunityInfo();
	cInfo.articleId = 3928056;
	cInfo.emoticonUrl = "";
	cInfo.categoryId = 155;
	cInfo.categoryName = "出售";
	cInfo.serverId = 16;
	cInfo.serverName = "艾瑞恩";
	cInfo.title = "4段10强化.. 出售双7神圣气焰 ..请私聊我";
	cInfo.summary = "私聊";
	cInfo.link = "/board/market/view?articleId=3928056&categoryId=155";
	community.list.add(cInfo);

	cInfo = new ItemCommunityInfo();
	cInfo.articleId = 3928037;
	cInfo.emoticonUrl = "";
	cInfo.categoryId = 156;
	cInfo.categoryName = "購買";
	cInfo.serverId = 16;
	cInfo.serverName = "艾瑞恩";
	cInfo.title = "收購 8 - 10 強化精靈族長矛";
	cInfo.summary = "收購 8 - 10 強化精靈族長矛";
	cInfo.link = "/board/market/view?articleId=3928037&categoryId=156";
	community.list.add(cInfo);

	cInfo = new ItemCommunityInfo();
	cInfo.articleId = 3928010;
	cInfo.emoticonUrl = "/img/img-nodata.png";
	cInfo.categoryId = 156;
	cInfo.categoryName = "購買";
	cInfo.serverId = 1;
	cInfo.serverName = "伺服器";
	cInfo.title = "收購雙 8 魔王戒指";
	cInfo.summary = "請留言或發信給我";
	cInfo.link = "/board/market/view?articleId=3928010&categoryId=156";
	community.list.add(cInfo);

	Gson gson = new Gson();
	String json = gson.toJson(community);
	HttpResponse response = create_response(HttpResponseStatus.OK, json);
response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");
return response;
}
