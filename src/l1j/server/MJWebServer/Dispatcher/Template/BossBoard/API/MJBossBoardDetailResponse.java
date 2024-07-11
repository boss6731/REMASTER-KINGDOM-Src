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

public class MJBossBoardDetailResponse extends MJHttpResponse {
	private static int board_page_no;

	public MJBossBoardDetailResponse(MJHttpRequest request) {
		super(request);

		int page_number = Integer.valueOf(request.getUri().replace("/api/board/boss/detail/", ""));
		board_page_no = page_number;
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		/**
		 * 페이지 정보 전달
		 */
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
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_boss_date WHERE npcid=?");
			pstm.setInt(1, board_id);
			rs = pstm.executeQuery();
			if (rs.next()) {
				stringBuilder.append("<center>");
				stringBuilder.append("<section class=\"section-info-body\" style=\"border-top:1px solid #3a3836; padding:10px; font-size:14px;\">\n");
				stringBuilder.append("<p class=\"mon_mapname\"><font color=#BDBDBD>출현지역 : <span class=\"line1\">" + rs.getString("mapname") + "</span></p>\n");
				stringBuilder.append("<p class=\"spawn_count\">[스폰시간 리스트]\n");
				stringBuilder.append("<p class=\"spawn_count\"><font color=#FF0000>[랜덤간격 : " + rs.getInt("rnd_time_min") + "분]\n");
				String[] spwans = rs.getString("spawn_time").split(",");
				for (String times : spwans) {
					String[] time = times.split(":");
					int hour = Integer.valueOf(time[0]);
					int min = Integer.valueOf(time[1]);
					String state = hour < 12 ? "오전" : "오후";
					if (state.equalsIgnoreCase("오후") && hour != 12) {
						hour -= 12;
					}
					stringBuilder.append("<p class=\"spawn_time\">" + state + " <span>" + hour + "시 " + min + "분 </span></p>\n");
				}

				stringBuilder.append("</section>\n");
				stringBuilder.append("</center>");

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
}
