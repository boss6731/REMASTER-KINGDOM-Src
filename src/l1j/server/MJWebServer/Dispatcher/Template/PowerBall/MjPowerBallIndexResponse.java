/**package l1j.server.MJWebServer.Dispatcher.Template.PowerBall;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Market.API.POJO.Util.MJHttpUtil;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.utils.SQLUtil;

public class MjPowerBallIndexResponse extends MJHttpResponse {
	private static String m_page_document;
	
	private static StringBuilder _sbHead;
	private static StringBuilder _sbBody;

	public MjPowerBallIndexResponse(MJHttpRequest request) {
		super(request, false);
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		if(_sbHead == null){
			StringBuilder sb = new StringBuilder(1024);
			sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
			sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
			sb.append("<head>");
			sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />");
			sb.append("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=Edge\" />");
			sb.append(MJHttpUtil.createLinkTag("text/css", "/mj_css/mjtotal.css"));
			sb.append(MJHttpUtil.createJsTag("/mj_js/jslib_p170_j171.js"));
			sb.append(MJHttpUtil.createJsTag("/mj_js/NCScriptUtil.js"));
			sb.append("<script type=\"text/javascript\">");
			sb.append("var ingame_staticurl = \'http://static.plaync.co.kr/ingame/lineage\'; var ingame_url = \'http://g.lineage.plaync.co.kr\';");
			sb.append("</script>");
			sb.append(MJHttpUtil.createJsTag("/mj_js/item.js"));
			sb.append(MJHttpUtil.createJsTag("/mj_js/rank.js"));
			sb.append(MJHttpUtil.createJsTag("/mj_js/board.js"));
			sb.append(MJHttpUtil.createJsTag("/mj_js/ingame.js"));
			sb.append("<script type=\"text/javascript\">var $j = jQuery.noConflict();</script>");
			sb.append("<title>plaync 天堂 :: PowerBall</title>");
			sb.append("</head>");
			sb.append("<body>");
			sb.append("<div id=\"header\">");
			sb.append("<h1>");
			sb.append("<a href=\"/ingame/poweball/index\">");
			sb.append("PowerBall");
			sb.append("</a>");
			sb.append("</h1>");
			sb.append("</div>");
			sb.append("<div id=\"contents\">");
			sb.append("<div class=\"info_servermaster\">");
			sb.append("<p>");
			sb.append("</p>");
			sb.append("<center><a href=\"/ingame/poweball_view/index\"><img src=\"/img/viewer.png\" alt=\"(抽籤檢視器)\" /></a></center>");
			sb.append("</div>");
			sb.append("<div class=\"wrap_header\">");
			sb.append("<ul class=\"tab\">");		
			_sbHead = sb;
		}
		
		if(_sbBody == null){
			StringBuilder sb = new StringBuilder(32);	
			sb.append("</ul>");
			sb.append("</div>");
			sb.append(MJHttpUtil.createJsTag("/mj_js/mjtotal.js"));
			sb.append("</body>");
			sb.append("</html>");
			_sbBody = sb;
		}
	// TODO 如果將 appcenterCacheReset 設為 false，並且 m_page_document 為 null，則進行例外處理，確保能正常打開。
		if (Config.Web.appcenterCacheReset || m_page_document == null) {
			StringBuilder sb		= new StringBuilder(512);
			sb.append(_sbHead.toString());
			sb.append(getBallListString());
			sb.append(_sbBody.toString());

			m_page_document = sb.toString();
		}
		HttpResponse response = create_response(HttpResponseStatus.OK, m_page_document);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}
	
	private String getBallListString(){		
		StringBuilder sb		= new StringBuilder(512);
		StringBuilder sbQry		= new StringBuilder(64);
		Connection con 			= null;
		PreparedStatement pstm 	= null;
		ResultSet rs 			= null;
//		sb.append("</br>");
		sb.append("<center><font color=#E6E6E6>[不是PowerBall，而是[普通球(數字)].]</center></font>");
		sb.append("</ul>");
		sb.append("</div>");
		sb.append("<ul class=\"list_ty1\">");
		
		sbQry.append("SELECT * FROM powerball_info "); 
		sbQry.append("ORDER BY num DESC");
		
		try{			
			con 		= L1DatabaseFactory.getInstance().getConnection();
			pstm		= con.prepareStatement(sbQry.toString());

			rs 			= pstm.executeQuery(); rs.last();
			int rownum	= rs.getRow(); rs.beforeFirst();
			if(rownum < 1){
				sb.append("<p class=\"nodata\">沒有文章.</p>");
				return sb.toString();
			}
			
			int 	num = 0;
			int 	todaycount = 0;
			String 	total;
			int 	plusnum;
			String 	oddEven;
			String 	unover;
			while(rs.next()){
				num 		= rs.getInt("num");
				todaycount 	= rs.getInt("TodayCount");
				total		= rs.getString("totalnum");
				plusnum 	= rs.getInt("plusnum");
				oddEven 	= rs.getString("oddEven");
				unover		= rs.getString("unover");
				if (total != null) {
					sb.append("<li>");
					sb.append("第 " + num + "("+todaycount+") 回合 [普通球] 抽籤結果.");
					sb.append("<p>");
					sb.append("抽籤號碼： " + total);
					sb.append("</p>");
					sb.append("<p>");
//					sb.append("數字總和： " + plusnum);
					sb.append("<font color=#01DF01>[數字總和： " + plusnum +"]</font>");
					sb.append("</p>");
					sb.append("<p>");
//					sb.append("結果：" + oddEven + "/" + unover + "");
					sb.append("<font color=#FF8000>[普通球結果：" + oddEven + "/" + unover + "]</font>");
					sb.append("</p>");
					sb.append("</li>");
				}
			}			
		} catch (SQLException e) {
			System.out.println("MjPowerBallIndexResponse1:" +e.getLocalizedMessage());
			e.printStackTrace();
		} catch (SecurityException e) {
			System.out.println("MjPowerBallIndexResponse2:" +e.getLocalizedMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("MjPowerBallIndexResponse3:" +e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return sb.toString();
	}
}**/
