package l1j.server.MJWebServer.Dispatcher.Template.GameLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.utils.SQLUtil;

public class MJGameLogResponse extends MJHttpResponse {
	private static String m_page_document;
	private static BugAscPrice _bugasc;
	private static DogAscPrice _dogasc;
	
	public MJGameLogResponse(MJHttpRequest request) {
		super(request);

		_bugasc = new BugAscPrice();
		_dogasc = new DogAscPrice();
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {

		StringBuilder sb = new StringBuilder(1024);
		sb.append(
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		sb.append("<head>");
		sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />");
		sb.append("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=Edge\" />");
		sb.append("<script type=\"text/javascript\">");
		sb.append("var ingame_staticurl = 'http://static.plaync.co.kr/ingame/lineage';");
		sb.append("var ingame_url = 'http://g.lineage.plaync.co.kr';");
		sb.append("</script>");
		sb.append("<script type=\"text/javascript\">");
		sb.append("var $j = jQuery.noConflict();");
		sb.append("</script>");
		sb.append("<title>plaync 天堂 :: </title>");
		sb.append("</head>");
		sb.append("<body><input type=\"hidden\" name=\"searchFocusFlag\" id=\"searchFocusFlag\" value=\"N\" />");
		sb.append("<div id=\"header\">");
		sb.append("<h1><a href=\"index\"><img src=\"/img/gamelog.png\" alt=\"\" /></a>");
		sb.append("<h1>");
		sb.append("</div>");
		sb.append("<script type=\"text/javascript\">");
		sb.append("var $j = jQuery.noConflict();");
		sb.append("</script>");
		sb.append("<SCRIPT type=\"text/JavaScript\" src=\"/mj_js/jquery.js\"></SCRIPT>");
		sb.append("<SCRIPT type=\"text/JavaScript\" src=\"/mj_js/lightslider.js\"></SCRIPT>");
		sb.append("<link rel=\"stylesheet\" href=\"/css/lightslider.css\" />");
		sb.append(
				"<style> body { margin:5px; pading:0; background:url('/img/body_bg.gif'); color: #9d9692;  } #slider > li { height:250px; } "
				+ ".leftBox > div > ul, .rightBox > div > ul { margin:0; padding:0; } "
				+ ".leftBox > div { float: left; margin:0  15px; } "
				+ ".leftBox > div > ul > li { list-style:none; margin-bottom:10px; } "
				+ ".rightBox > div { float: left; margin:0  15px; } "
				+ ".rightBox > div > ul > li { list-style:none; margin-bottom:10px; } "
				+ "</style>");
		sb.append("<script> $(function () { $('#slider').lightSlider({ item : 1 }); }); </script>");
		sb.append("<title>plaync 天堂 :: </title>");
		sb.append("</head>");
		sb.append("<body><input type=\"hidden\" name=\"searchFocusFlag\" id=\"searchFocusFlag\" value=\"N\" />");
		sb.append("<div id=\"header\"></div>");
		sb.append("<div class=\"wrap_sanctions\">");
		sb.append("<div class=\"box_mjmsg\" style=\"display:-none;\">");
		sb.append("<div class=\"inbox\">");
		sb.append("<ul id=\"slider\">");

		/**
		 * 내역 출력 순서
		 */
		sb.append(getBugGameLogSend());
		sb.append(getDogGameLogSend());

		sb.append("</div>");
		sb.append("</div>");
		sb.append("</div>");
		sb.append("<SCRIPT type=\"text/JavaScript\" src=\"/mj_js/mjtotal.js\"></SCRIPT>");
		sb.append("</body>");
		sb.append("</html>");

		m_page_document = sb.toString();

		HttpResponse response = create_response(HttpResponseStatus.OK, m_page_document);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}

	private String getDogGameLogSend() {
		StringBuilder sb = new StringBuilder(256);
		StringBuilder sbQry = new StringBuilder(64);
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		GameDogLogInfo gli = null;
		ArrayList<GameDogLogInfo> gli_list = null;
		
		sbQry.append("SELECT * FROM dogfight_history ");
		sbQry.append("ORDER BY game_number DESC limit 0,30");

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(sbQry.toString());
			rs = pstm.executeQuery();

			gli_list = new ArrayList<GameDogLogInfo>();
			while (rs.next()) {
				gli = new GameDogLogInfo();
				gli._id = rs.getInt("game_number");
				gli._winner_id = rs.getInt("winner_id");

				gli_list.add(gli);
			}

			gli_list.sort(_dogasc);

			sb.append("<li>");
			sb.append("<div class=\"leftBox\">");

			int size = gli_list.size();
			int last_i = 0;
			for (int i = 0; i < size; i++) {
				GameDogLogInfo sort_gli = gli_list.get(i);
				
				if (i % 6 == 0) {
					sb.append("<div>");
					sb.append("<ul>");
					last_i = i + 5;
				}

				String img_url = sort_gli._winner_id == 0 ? "/img/dog_0.png" : "/img/dog_1.png";
				sb.append("<li><img src='" + img_url + "' style='width:28px;height:28px;' /></li>");

				if (last_i == i) {
					sb.append("</ul>");
					sb.append("</div>");
				}
			}
			sb.append("</count>");
			sb.append("</div>");
			sb.append("</li>");
			
		} catch (Exception e) {
			System.out.println("MJGameLogResponse:" +e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}

		return sb.toString();
	}
	
	private String getBugGameLogSend() {
		StringBuilder sb = new StringBuilder(256);
		StringBuilder sbQry = new StringBuilder(64);
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		GameBugLogInfo gli = null;
		ArrayList<GameBugLogInfo> gli_list = null;
		
		//TODO 您可以載入和增加備份日誌
		sbQry.append("SELECT * FROM bug_history ");
		sbQry.append("ORDER BY round DESC limit 0,18");

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(sbQry.toString());
			rs = pstm.executeQuery();

			gli_list = new ArrayList<GameBugLogInfo>();
			while (rs.next()) {
				gli = new GameBugLogInfo();
				gli._id = rs.getInt("round");
				gli._winner_name = rs.getString("winner_name");

				gli_list.add(gli);
			}

			gli_list.sort(_bugasc);

			int size = gli_list.size();
			int last_i = 0;

			sb.append("<li>");
			sb.append("<div class=\"rightBox\">");
			for (int i = 0; i < size; i++) {
				GameBugLogInfo sort_gli = gli_list.get(i);
				
				if (i % 6 == 0) {
					sb.append("<div>");
					sb.append("<ul>");
					last_i = i + 5;
				}

				String img_url = sort_gli._winner_name;
				int id = sort_gli._id;

//				sb.append("<li><img src='" + img_url + "' style='width:15px;height:15px;' /></li>");
				sb.append("<li style="width:15px;height:15px; !important;
				">"
						+ " <font color="
				"><font size="2px" color="#A4A4A4" face="맑은고딕 s#FF0040" face="맑은고딕">[" + id + ":<font color="#FAFAFA" face="맑은고딕">" + img_url + "<font color="#FF0040" face="맑은고딕">]</li>");

				if (last_i == i) {
					sb.append("</ul>");
					sb.append("</div>");
				}
			}

			sb.append("</div>");
			sb.append("</li>");
			
		} catch (Exception e) {
			System.out.println("MJGameLogResponse:" +e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}

		return sb.toString();
	}

	public class GameDogLogInfo {
		public int _id;
		public int _winner_id;
	}
	
	public class GameBugLogInfo {
		public int _id;
		public String _winner_name;
	}

	class DogAscPrice implements Comparator<GameDogLogInfo> {
		@Override
		public int compare(GameDogLogInfo arg0, GameDogLogInfo arg1) {
			return arg0._id - arg1._id;
		}
	}
	
	class BugAscPrice implements Comparator<GameBugLogInfo> {
		@Override
		public int compare(GameBugLogInfo arg0, GameBugLogInfo arg1) {
			return arg0._id - arg1._id;
		}
	}
}
