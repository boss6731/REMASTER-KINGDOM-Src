package l1j.server.MJWebServer.Dispatcher.Template.MyTradeHistory.API;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Dispatcher.Template.TradeBoard.POJO.MJTradeBoardInfo;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.utils.SQLUtil;

public class MJMyTradeHistoryListResponse extends MJHttpResponse {
	
	private int _page_firstNum;
	
	public MJMyTradeHistoryListResponse(MJHttpRequest request) {
		super(request);

		/**
		 * 페이지 처음 값
		 */
		int url_number = Integer.valueOf(request.get_request_uri().replaceAll("/api/notice/board/mytradelist/", ""));
		_page_firstNum = url_number;
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		StringBuilder stringBuilder = new StringBuilder();
		
		getTradeBoardInfoList(stringBuilder, _page_firstNum);
		getTradeBoardbuyInfoList(stringBuilder, _page_firstNum);
		
		HttpResponse response = create_response(HttpResponseStatus.OK, stringBuilder.toString());
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}
	//TODO 구매
	private void getTradeBoardbuyInfoList(StringBuilder stringBuilder, int firstNum) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		MJTradeBoardInfo tbi = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_item_trade WHERE buy_account_name=? ORDER BY buy_result_date DESC, 8");
			//pstm = con.prepareStatement("SELECT * FROM board_item_trade WHERE buy_account_name=? limit "+ firstNum +", 8");
			pstm.setString(1, _player == null ? "" : _player.getAccount().getName());
			rs = pstm.executeQuery();
			
			int maxCnt = getTotalDatabaseCount();
			while (rs.next()) {
				tbi = new MJTradeBoardInfo();
				
				if (rs.getTimestamp("buy_result_date") == null) {
					return;
				}
				
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

				stringBuilder.append("<a href="#">
						");
						stringBuilder.append("<span class="line1" style="width:15% !important;">" + tbi.category + "</span>
				"); // 類別
				stringBuilder.append("<span class="line2" style="width:13% !important;">[" + "購買" + "]</span>
				"); // "구매" => "購買"
				stringBuilder.append("<span class="line3" style="width:28% !important;">" + tbi.title + "</span>
				"); // 標題
				stringBuilder.append("<span class="line4" style="width:15% !important;">&nbsp;" + (MJString.parse_money_string(tbi.price) + "元") + "</span>
				"); // "원" => "元"

				SimpleDateFormat sdfCurrent = new SimpleDateFormat("MM-dd hh:mm");
				Timestamp currentTime = new Timestamp(rs.getTimestamp("buy_result_date").getTime());
				String today = sdfCurrent.format(currentTime);

				stringBuilder.append("<span class="line4" style="width:20% !important;"><font size=1 color=#A4A4A4 face="微軟正黑體">" + today + "</font></span>
				"); // "맑은고딕" => "微軟正黑體"

				stringBuilder.append("</a>
						");
						stringBuilder.append("</li>
								");
			}
			stringBuilder.append("<input type="hidden" id="totalCnt" value="" + maxCnt + ""/>
					"); // 總數量
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con); // 關閉結果集、預處理語句和連接
		}
	}
	//TODO 판매
	private void getTradeBoardInfoList(StringBuilder stringBuilder, int firstNum) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		MJTradeBoardInfo tbi = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_item_trade WHERE char_account_name=? ORDER BY buy_result_date DESC, 8");
//			pstm = con.prepareStatement("SELECT * FROM board_item_trade WHERE char_account_name=? limit "+ firstNum +", 8");
			pstm.setString(1, _player == null ? "" : _player.getAccount().getName());
			rs = pstm.executeQuery();

			int maxCnt = getTotalDatabaseCount();
			while (rs.next()) {
				tbi = new MJTradeBoardInfo();
				
				if (rs.getTimestamp("buy_result_date") == null) {
					return;
				}
				
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

				stringBuilder.append("<a href="#">
						");
						stringBuilder.append("<span class="line1" style="width:15% !important;">" + tbi.category + "</span>
				"); // 類別
				stringBuilder.append("<span class="line2" style="width:13% !important;">[" + "銷售" + "]</span>
				"); // "판매" => "銷售"
				stringBuilder.append("<span class="line3" style="width:28% !important;">" + tbi.title + "</span>
				"); // 標題
				stringBuilder.append("<span class="line4" style="width:15% !important;">&nbsp;" + (MJString.parse_money_string(tbi.price) + "元") + "</span>
				"); // "원" => "元"

				SimpleDateFormat sdfCurrent = new SimpleDateFormat("MM-dd hh:mm");
				Timestamp currentTime = new Timestamp(rs.getTimestamp("buy_result_date").getTime());
				String today = sdfCurrent.format(currentTime);

				stringBuilder.append("<span class="line4" style="width:20% !important;"><font size=1 color=#A4A4A4 face="微軟正黑體">" + today + "</font></span>
				"); // "맑은고딕" => "微軟正黑體"
				stringBuilder.append("</a>
						");

				stringBuilder.append("</a>\n");
				stringBuilder.append("</li>\n");
			}
			stringBuilder.append("<input type=\"hidden\" id=\"totalCnt\" value=\"" + maxCnt + "\"/>\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	private int getTotalDatabaseCount() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int total_count = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_item_trade WHERE char_account_name=?");
			pstm.setString(1, _player == null ? "" : _player.getAccount().getName());
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
