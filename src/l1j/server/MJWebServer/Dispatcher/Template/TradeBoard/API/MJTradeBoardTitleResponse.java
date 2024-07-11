package l1j.server.MJWebServer.Dispatcher.Template.TradeBoard.API;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.utils.SQLUtil;

public class MJTradeBoardTitleResponse extends MJHttpResponse {
	private static int board_page_no;

	public MJTradeBoardTitleResponse(MJHttpRequest request) {
		super(request);

		int page_number = Integer.valueOf(request.getUri().replace("/api/board/list/title/", ""));
		board_page_no = page_number;
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		StringBuilder stringBuilder = new StringBuilder();

		getTradeBoardDetailInfo(stringBuilder, board_page_no);

		HttpResponse response = create_response(HttpResponseStatus.OK, stringBuilder.toString());
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}

	private void getTradeBoardDetailInfo(StringBuilder stringBuilder, int board_id) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection(); // 獲取資料庫連接
			pstm = con.prepareStatement("SELECT * FROM board_item_trade WHERE id=?"); // 準備 SQL 查詢語句
			pstm.setInt(1, board_id); // 設置查詢參數
			rs = pstm.executeQuery(); // 執行查詢
			if (rs.next()) { // 如果查詢結果有下一行
				stringBuilder.append("<span class="state">[銷售] </span>" + rs.getString("title")); // 添加查詢結果中的標題
			}
		} catch (Exception e) {
			e.printStackTrace(); // 打印異常堆棧跟蹤
		} finally {
			SQLUtil.close(rs, pstm, con); // 關閉結果集、預處理語句和連接
		}
	}
