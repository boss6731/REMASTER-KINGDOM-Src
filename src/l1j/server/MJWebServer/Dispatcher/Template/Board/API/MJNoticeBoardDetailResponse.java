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

public class MJNoticeBoardDetailResponse extends MJHttpResponse {
	private static int board_page_no;

	public MJNoticeBoardDetailResponse(MJHttpRequest request) {
		super(request);

		int page_number = Integer.valueOf(request.getUri().replace("/api/board/notice/detail/", ""));
		board_page_no = page_number;
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		/**
		 * 페이지 정보 전달
		 */
		StringBuilder stringBuilder = new StringBuilder();

		getTradeBoardDetailInfo(stringBuilder, board_page_no);

		/**
		 * 판매물풀 : 판매갯수 : 내용 :
		 */
		HttpResponse response = create_response(HttpResponseStatus.OK, stringBuilder.toString());
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}

	private void getTradeBoardDetailInfo(StringBuilder stringBuilder, int board_id) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_mjnotice WHERE id=?");
			pstm.setInt(1, board_id);
			rs = pstm.executeQuery();
			if (rs.next()) {

				stringBuilder.append("<section class=\"section-info-body\" style=\"border-top:1px solid #3a3836; padding:10px; font-size:14px;\">\n");
				stringBuilder.append(rs.getString("content"));
				stringBuilder.append("</section>\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
}
