package l1j.server.MJWebServer.Dispatcher.Template.BossBoard.API;

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

public class MJBossBoardListResponse extends MJHttpResponse {

	private int _page_firstNum;

	public MJBossBoardListResponse(MJHttpRequest request) {
		super(request);

		/**
		 * 페이지 처음 값
		 */
		int url_number = Integer.valueOf(request.get_request_uri().replaceAll("/api/notice/board/boss_time/", ""));
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
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_boss_date limit " + firstNum + ", 9");
			rs = pstm.executeQuery();

			int maxCnt = getTotalDatabaseCount();
			while (rs.next()) {

				stringBuilder.append("<li class=\"board-items\" style=\"padding:0 !important; padding-left:10px !important;\">\n");

				/* paging */
				int pageNum = 1;
				int pageItemCnt = 9;
				if (firstNum > 0)
					pageNum = (int) (Math.ceil(firstNum / pageItemCnt));
				/* paging */

				stringBuilder.append("<a href=\"/ingame/bosstime/detail?id=" + rs.getInt("npcid") + "&page=" + pageNum + "\">\n");
				stringBuilder.append("<span class=\"line1\" style=\"width:40%;\"><font color=#F7FE2E>[" + rs.getString("monname") + "]</span>");
				stringBuilder.append("<span class=\"line1\" style=\"width:60% !important;\"><font color=#F7FE2E>" /*+ rs.getString("mapname")	+ "</span>\n"*/);

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
			pstm = con.prepareStatement("SELECT * FROM spawnlist_boss_date");
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
