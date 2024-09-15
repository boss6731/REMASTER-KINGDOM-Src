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
		cInfo.categoryName = "판매";
		cInfo.serverId = 16;
		cInfo.serverName = "어레인";
		cInfo.title = "★★각종템사고팜니당★★";
		cInfo.summary = "l!!! 구매대행 이전대행 !어떤템도구해드림니다 물품에따라서 흥정 가능합니다&nbsp;※최고가통매입환영※&nbsp;24시간언제든&nbsp;연락주세요&nbsp;댓글&nbsp;댓글주세용&nbsp;바로바로&nbsp;답드림니다댓글안달리시에는 접하셔서 귓말 주심댐니다&nbsp;간편검색방법: 컨트롤+F 검색하시면됩니다 각종이전사고 0프로 ^^// 안적힌것도&nbsp;";
		cInfo.link = "/board/market/view?articleId=3928283&categoryId=155";
		community.list.add(cInfo);

		cInfo = new ItemCommunityInfo();
		cInfo.articleId = 3928252;
		cInfo.emoticonUrl = "";
		cInfo.categoryId = 156;
		cInfo.categoryName = "구매";
		cInfo.serverId = 16;
		cInfo.serverName = "어레인";
		cInfo.title = "축10수단 삽니다";
		cInfo.summary = "댓글이나 편지 주세요";
		cInfo.link = "/board/market/view?articleId=3928252&categoryId=156";
		community.list.add(cInfo);

		cInfo = new ItemCommunityInfo();
		cInfo.articleId = 3928066;
		cInfo.emoticonUrl = "";
		cInfo.categoryId = 156;
		cInfo.categoryName = "구매";
		cInfo.serverId = 16;
		cInfo.serverName = "어레인";
		cInfo.title = "7제로스 올아덴삼";
		cInfo.summary = "7제지 올아덴 2.4삼3단7제지 &nbsp;2.5삼이가격보다비싸면 장사꾼들이니댓글하지마세요파실분만댓주세요";
		cInfo.link = "/board/market/view?articleId=3928066&categoryId=156";
		community.list.add(cInfo);

		cInfo = new ItemCommunityInfo();
		cInfo.articleId = 3928056;
		cInfo.emoticonUrl = "";
		cInfo.categoryId = 155;
		cInfo.categoryName = "판매";
		cInfo.serverId = 16;
		cInfo.serverName = "어레인";
		cInfo.title = "4단10진싸.. 쌍7신성기백 싸게팜 ..귓주세요";
		cInfo.summary = "귓말";
		cInfo.link = "/board/market/view?articleId=3928056&categoryId=155";
		community.list.add(cInfo);

		cInfo = new ItemCommunityInfo();
		cInfo.articleId = 3928037;
		cInfo.emoticonUrl = "";
		cInfo.categoryId = 156;
		cInfo.categoryName = "구매";
		cInfo.serverId = 16;
		cInfo.serverName = "어레인";
		cInfo.title = "8 - 10 축요정족창 삽니다";
		cInfo.summary = "8 - 10 축요정족창 삽니다";
		cInfo.link = "/board/market/view?articleId=3928037&categoryId=156";
		community.list.add(cInfo);

		cInfo = new ItemCommunityInfo();
		cInfo.articleId = 3928010;
		cInfo.emoticonUrl = "/img/img-nodata.png";
		cInfo.categoryId = 156;
		cInfo.categoryName = "구매";
		cInfo.serverId = 1;
		cInfo.serverName = "Server";
		cInfo.title = "쌍 8마왕반지 삽니다";
		cInfo.summary = "댓글 또는 편지 주세요";
		cInfo.link = "/board/market/view?articleId=3928010&categoryId=156";
		community.list.add(cInfo);

		Gson gson = new Gson();
		String json = gson.toJson(community);
		HttpResponse response = create_response(HttpResponseStatus.OK, json);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");
		return response;
	}
}
