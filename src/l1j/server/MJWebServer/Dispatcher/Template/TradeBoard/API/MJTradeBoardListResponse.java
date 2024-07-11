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

public class MJTradeBoardListResponse extends MJHttpResponse {
	
	private int _page_firstNum;
	
	public MJTradeBoardListResponse(MJHttpRequest request) {
		super(request);

		/**
		 * 페이지 처음 값
		 */
		int url_number = Integer.valueOf(request.get_request_uri().replaceAll("/api/trade/board/list/", ""));
		_page_firstNum = url_number;
		
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		StringBuilder stringBuilder = new StringBuilder();
		
		getTradeBoardInfoList(stringBuilder, _page_firstNum);
		
		HttpResponse response = create_response(HttpResponseStatus.OK, stringBuilder.toString());
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}
	
	private void getTradeBoardInfoList(StringBuilder stringBuilder, int firstNum) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		MJTradeBoardInfo tbi = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_item_trade order by `id` desc limit "+ firstNum +", 8");
			rs = pstm.executeQuery();
			
			int maxCnt = getTotalDatabaseCount();
			while (rs.next()) {
				tbi = new MJTradeBoardInfo();
				
				stringBuilder.append("<li class=\"board-items\" style=\"padding:0 !important; padding-left:10px !important;\">\n");
				
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
				
				/* paging */
				int pageNum = 1;
				int pageItemCnt = 8;
				if(firstNum > 0) pageNum = (int)(Math.ceil(firstNum / pageItemCnt));
				/* paging */

				if (!tbi.state.equalsIgnoreCase("完成")) {// 如果狀態不是"完成"
					stringBuilder.append("<a href="/ingame/itemtrade/detail?id=" + tbi.number + "&page=" + pageNum + "">
							");
				} else {
					stringBuilder.append("<a href="">
							");
				}
// TODO: 在物品交易菜單中設置帖子樣式
				stringBuilder.append("<span class="line1" style="width:4% !important;">" + tbi.number + "</span>"); // 編號
				stringBuilder.append("<span class="line2" style="width:16% !important;">[" + tbi.category + "]</span>"); // 物品、金幣 類型
				stringBuilder.append("<span class="line3" style="width:15% !important;">[" + tbi.state + "]&nbsp;</span>"); // 銷售、完成類型
				stringBuilder.append("<span class="line4" style="width:40% !important;">" + tbi.title + "</span>");

				if (tbi.item_name.equalsIgnoreCase("金幣")) { // 如果物品名稱是"阿德納"
					stringBuilder.append("<span class="line5" style="width:18% !important;">" + (MJString.parse_money_string(tbi.price) + "元") + "</span>");
				} else {
					stringBuilder.append("<span class="line5" style="width:18% !important;">" + (MJString.parse_money_string(tbi.price) + "元") + "</span>");
				}

				stringBuilder.append("</a>
						");
						stringBuilder.append("</li>
								");
			}
			stringBuilder.append("<input type="hidden" id="totalCnt" value="" + maxCnt + ""/>
					");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con); // 關閉資源
		}
	}
	
	private int getTotalDatabaseCount() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int total_count = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_item_trade");
			rs = pstm.executeQuery();
			while (rs.next()) {
				total_count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return total_count;
	}
}
