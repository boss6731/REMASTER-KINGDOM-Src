package l1j.server.MJWebServer.Dispatcher.Template.TradeBoard.API;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Dispatcher.Template.TradeBoard.POJO.MJTradeBoardInfo;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.utils.SQLUtil;

public class MJTradeBoardDetailResponse extends MJHttpResponse {
	private static int board_page_no;

	public MJTradeBoardDetailResponse(MJHttpRequest request) {
		super(request);

		int page_number = Integer.valueOf(request.getUri().replace("/api/board/list/detail/", ""));
		board_page_no = page_number;
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		/**
		 * 傳遞頁面信息
		 */
		StringBuilder stringBuilder = new StringBuilder();

		getTradeBoardDetailInfo(stringBuilder, board_page_no);

		/**
		 * 銷售物品 : 銷售數量 : 內容 :
		 */
		HttpResponse response = create_response(HttpResponseStatus.OK, stringBuilder.toString());
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}

	private void getTradeBoardDetailInfo(StringBuilder stringBuilder, int board_id) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		MJTradeBoardInfo tbi = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_item_trade WHERE id=?");
			pstm.setInt(1, board_id);
			rs = pstm.executeQuery();
			if (rs.next()) {
				tbi = new MJTradeBoardInfo();

				tbi.number = rs.getInt("id");
				tbi.state = rs.getString("state");
				tbi.category = rs.getString("category");
				tbi.title = rs.getString("title");
				tbi.price = rs.getInt("price");
				tbi.item_name = rs.getString("item_name");
				tbi.item_count = rs.getInt("item_count");
				tbi.item_bless = rs.getString("item_bless");
				tbi.item_enchant = rs.getInt("item_enchant");
				tbi.item_attr = rs.getString("item_attr");
				tbi.item_attr_level = rs.getInt("item_attr_level");

				stringBuilder.append("<section class=\"section-info-body\" style=\"border-top:1px solid #3a3836; padding:1px; font-size:12px;\">\n");
				stringBuilder.append("<p class=\"item_name\">物品名稱： <span class=\"line1\">" + tbi.item_name + "</span></p>\n");
//				stringBuilder.append("<p class=\"item_count\">數量： <span>" + MJString.parse_money_string(tbi.item_count) + "</span></p>\n");
				stringBuilder.append("<p class=\"item_count\">數量： <span>" + String.format("%,d", tbi.item_count) + "</span></p>\n");
				stringBuilder.append("<p class=\"price\">銷售金額： <span>" + MJString.parse_money_string(tbi.price) + "元" + "</span></p>\n");
				if (tbi.category.equalsIgnoreCase("物品")) {
					stringBuilder.append("<p class=\"item_enchant\">附魔： <span>+" + tbi.item_enchant + "</span></p>\n");
					stringBuilder.append("<p class=\"item_bless\">狀態： <span>" + (Integer.valueOf(tbi.item_bless) == 0 ? "祝福" : Integer.valueOf(tbi.item_bless) == 2 ? "詛咒" : "一般") + "</span></p>\n");
					if (!tbi.item_attr.equalsIgnoreCase("NONE")) {
						stringBuilder.append("<p class=\"item_attr\">屬性： <span>" + tbi.item_attr + "</span></p>\n");
						stringBuilder.append("<p class=\"item_attr_level\">屬性附魔： <span>" + tbi.item_attr_level + "</span></p>\n");
					}
				}
				stringBuilder.append("</section>\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
}
