package l1j.server.MJWebServer.Dispatcher.Template.Cash.API;

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
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.utils.SQLUtil;

public class MJCashMyWithdrawListResponse extends MJHttpResponse {
	private int _page_firstNum;

	public MJCashMyWithdrawListResponse(MJHttpRequest request) {
		super(request);

		/**
		 * 페이지 처음 값
		 */
		int url_number = Integer.valueOf(request.get_request_uri().replaceAll("/api/trade/board/mywithdrawlist/", ""));
		_page_firstNum = url_number;

	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		StringBuilder stringBuilder = new StringBuilder();

		getMyChargeList(stringBuilder, _page_firstNum);

		HttpResponse response = create_response(HttpResponseStatus.OK, stringBuilder.toString());
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}

	private void getMyChargeList(StringBuilder stringBuilder, int firstNum) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM ncoin_trade_refund WHERE account_name=? ORDER BY generate_date DESC, 8");
			pstm.setString(1, _player == null ? "" : _player.getAccount().getName());
			rs = pstm.executeQuery();

			int maxCnt = getTotalDatabaseCount(_player.getAccount().getName());
			stringBuilder.append("<center><span class=\"title\"><font color=A4A4A4>[" + _player.getName() + "님의 출금신청내역] </center></font></span>");
			
			while (rs.next()) {
				
				if (rs.getTimestamp("generate_date") == null) {
					return;
				}
				
				stringBuilder.append("<br/>");
				stringBuilder.append("<li class=\"board-items\" style=\"padding:0 !important; padding-left:10px !important;\">\n");
				stringBuilder.append("<span class=\"line1\" style=\"width:5% !important;\"><font color=FF0000>[" + (rs.getInt("is_refund") == 1 ? "완료" : "대기") + "]</font></span>\n");
				stringBuilder.append("<span class=\"line2\" style=\"width:12% !important;\"><font color=BDBDBD>" + (MJString.parse_money_string(rs.getInt("ncoin_value")) + "원") + "</font></span>\n");
				stringBuilder.append("<span class=\"line3\" style=\"width:20% !important;\"> | </span>\n");

				SimpleDateFormat sdfCurrent = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Timestamp currentTime = new Timestamp(rs.getTimestamp("generate_date").getTime());
				String today = sdfCurrent.format(currentTime);

				stringBuilder.append("<span class=\"line4\" style=\"width:40% !important;\"><font size=2 color=#A4A4A4 face=\"맑은고딕\">" + today + "</font></span>\n");
				stringBuilder.append("</li>\n");
			}
			stringBuilder.append("<input type=\"hidden\" id=\"totalCnt\" value=\"" + maxCnt + "\"/>\n"); 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	private int getTotalDatabaseCount(String account_name) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int total_count = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM ncoin_trade_refund WHERE account_name=?");
			pstm.setString(1, account_name);
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
