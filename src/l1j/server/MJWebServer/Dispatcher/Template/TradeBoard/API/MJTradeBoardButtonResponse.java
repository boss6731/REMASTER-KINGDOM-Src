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

public class MJTradeBoardButtonResponse extends MJHttpResponse {
	private static int board_page_no;

	public MJTradeBoardButtonResponse(MJHttpRequest request) {
		super(request);

		int page_number = Integer.valueOf(request.getUri().replace("/api/board/list/button/", ""));
		board_page_no = page_number;
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		StringBuilder stringBuilder = new StringBuilder();

/**
 * TODO: 由於通過網頁訪問無法檢查 GM，因此會輸出錯誤（刪除按鈕）
 *       此外，如果以隱身狀態啟動服務器或重新連接，將無法識別玩家
 *       需要完全關閉客戶端後重新連接或重新啟動服務器
 * **/
		if (_player.isGm()) {
			getGmTradeBoardDetailInfo(stringBuilder, board_page_no); // 這裡之前有錯誤
		} else {
			getTradeBoardDetailInfo(stringBuilder, board_page_no);
		}

		HttpResponse response = create_response(HttpResponseStatus.OK, stringBuilder.toString());
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}

	private void getGmTradeBoardDetailInfo(StringBuilder stringBuilder, int board_id) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			String gm = "GmDel";
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_item_trade WHERE id=? AND GM_DEL=?");
			pstm.setInt(1, board_id);
			pstm.setString(2, gm);
			rs = pstm.executeQuery();
			if (rs.next()) {
				stringBuilder.append("<a id="listLink" onclick="item_delete()"><input type="button" value="刪除"/></a> ");
			}
			stringBuilder.append("<a id="listLink" href="/ingame/itemtrade/index?page="><input type="button" value="列表"/></a>");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	private void getTradeBoardDetailInfo(StringBuilder stringBuilder, int board_id) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_item_trade WHERE id=? AND char_account_name=?");
			pstm.setInt(1, board_id);
			pstm.setString(2, _player == null ? "" : _player.getAccount().getName());
			rs = pstm.executeQuery();
			if (rs.next()) {
				stringBuilder.append("<a id="listLink" onclick="item_delete()"><input type="button" value="刪除"/></a> ");
			}
			stringBuilder.append("<a id="listLink" href="/ingame/itemtrade/index?page="><input type="button" value="列表"/></a>");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
}
