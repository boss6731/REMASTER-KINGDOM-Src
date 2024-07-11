package l1j.server.MJWebServer.Dispatcher.Template.Board.API;

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

public class MJNoticeBoardTitleResponse extends MJHttpResponse {
	private static int board_page_no;

	public MJNoticeBoardTitleResponse(MJHttpRequest request) {
		super(request);

		int page_number = Integer.valueOf(request.getUri().replace("/api/board/notice/title/", ""));
		board_page_no = page_number;
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		StringBuilder stringBuilder = new StringBuilder();

		getNoticeBoardDetailInfo(stringBuilder, board_page_no);

		HttpResponse response = create_response(HttpResponseStatus.OK, stringBuilder.toString());
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}

	private void getNoticeBoardDetailInfo(StringBuilder stringBuilder, int board_id) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_mjnotice WHERE id=?");
			pstm.setInt(1, board_id);
			rs = pstm.executeQuery();
			if (rs.next()) {
				stringBuilder.append(rs.getString("title"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
}
