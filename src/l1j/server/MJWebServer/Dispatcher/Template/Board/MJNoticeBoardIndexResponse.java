package l1j.server.MJWebServer.Dispatcher.Template.Board;

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

public class MJNoticeBoardIndexResponse extends MJHttpResponse {
	private static String m_page_document;
	private static int type;
	private static String sitem;
	public MJNoticeBoardIndexResponse(MJHttpRequest request) {
		super(request);
		
		if(request.get_parameters().get("type") != null) {
			type = Integer.valueOf(request.get_parameters().get("type").get(0));
		}
		
		if(request.get_parameters().get("sitem") != null) {
			sitem = request.get_parameters().get("sitem").get(0);
		}
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
			StringBuilder sb = new StringBuilder();
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
			sb.append("<title>plaync 리니지 :: 소식</title>");
			sb.append("</head>");
			sb.append("<body>");
			sb.append("<div id=\"header\">");
			sb.append("<input type=\"hidden\" name=\"searchCondition\" id=\"searchCondition\" value=\"6\" />");
			sb.append("<input type=\"hidden\" name=\"searchFocusFlag\" id=\"searchFocusFlag\" value=\"N\" />");
			sb.append("<h1>");
			sb.append("<a href=\"/ingame/notice/list\">");
			sb.append("<img src=\"/img/notice.gif\" alt=\"소식\" />");
			sb.append("</a>");
			sb.append("</h1>");
			/*sb.append("<div class=\"wrap_search\">");
			sb.append("<input type=\"text\" value=\"검색어를 입력해 주세요\" ");
			sb.append("onfocus=\"if(this.value == \'검색어를 입력해 주세요\'){this.value=\'\'; document.getElementById(\'searchFocusFlag\').value=\'Y\'}\"");
			sb.append("onblur=\"if(this.value == \'\'){this.value=\'검색어를 입력해 주세요\'; document.getElementById(\'searchFocusFlag\').value=\'N\'}\"");
			sb.append("id=\"searchKeyword\" name=\"searchKeyword\" class=\"suggest\" onkeypress=\"if(window.event.keyCode==13){javascript:Board.goSearchNotice(); return false;}\" maxlength=\"12\" />");
			sb.append("<a href=\"javascript:Board.goSearchNotice(); return false;\"><input type=\"image\" value=\"검색\" src=\"/img/ic_search.gif\" id=\"sitem_submit\" onclick=\"javascript:Board.goSearchNotice(); return false;\" /></a>");
			sb.append("</div>");*/
			sb.append("</div>");
			sb.append("<script type=\"text/javascript\">");
			sb.append("//<![CDATA[\r\n");
			sb.append("$j(\'#sitem\').focus(function() {");
			sb.append("if($j(this).val() == \"검색어를 입력해 주세요\") {$j(this).val(\'\').css(\'borderColor\', \'#ab6f11\');}");
			sb.append("$j(this).css(\'borderColor\', \'#ab6f11\');");
			sb.append("}).blur(function() {");
			sb.append("if(!$j(this).val()) {$j(this).val(\"검색어를 입력해 주세요\");}");
			sb.append("$j(this).css(\'borderColor\', \'#71604e\');");
			sb.append("});\r\n");
			sb.append("//]]>\r\n");
			sb.append("</script>");
			sb.append("<div id=\"contents\">");
			sb.append("<div class=\"wrap_header\">");
			sb.append("<ul class=\"tab bbsNotice\">");
			
			sb.append("<input id=\"searchType\" type=\"hidden\" value=\"");
			sb.append(type);
			sb.append("\">");
			sb.append("<li class=\"m1\"><a href=\"javascript:Board.noticeList(\'notice\', 1);\" class=\"");
			if(type == 0)
				sb.append("on");
			sb.append("\">공지</a></li>");
			sb.append("<li class=\"m2\"><a href=\"javascript:Board.updateList(\'update\', 1);\" class=\"");
			if(type == 1)
				sb.append("on");
			sb.append("\">업데이트</a></li>");
			sb.append("</ul>");
			sb.append("<div id=\"selectBox\" class=\"selectBox\">");
			sb.append("<span id=\"selectServer\">");
			sb.append(Config.Message.GameServerName);
			sb.append("</span>");
			sb.append("<ul class=\"selectList\">");
			sb.append("<li server=\"\">");
			sb.append(Config.Message.GameServerName);
			sb.append("</li>");
			sb.append("</ul>");
			sb.append("</div>");
			sb.append("</div>");
			sb.append("<ul class=\"list_ty1\">");
			if(type == 0)
				sb.append(getNoticeSubjectString(sitem));
			else
				sb.append(getUpdateSubjectString(sitem));
			
			sb.append("</ul>");
			sb.append("<div class=\"pager\">");
			sb.append("<span class=\"current\">1</span>");
			sb.append("</div>");
			sb.append("</div>");
			sb.append("<script type=\"text/javascript\">");
			sb.append("//<![CDATA[\r\n");
			sb.append("var selectBtn = $j(\"#selectServer\");");
			sb.append("var selectLayer = $j(\"#selectBox ul.selectList\");");
			sb.append("selectBtn.bind(\"click\", function(e){");
			sb.append("if ( selectLayer.css(\'display\') == \'block\' ) {");
			sb.append("selectLayer.hide();");
			sb.append("} else {");
			sb.append("selectLayer.show();");
			sb.append("}");
			sb.append("});");
			sb.append("selectLayer.find(\'li\').bind(\"click\", function(e){");
			sb.append("var serverType = $j(this).attr(\"server\");");
			sb.append("var board = serverType+\'notice\';");
			sb.append("Board.noticeList(board, 1);");
			sb.append("selectBtn.html($j(this).text());");
			sb.append("selectLayer.hide();");
			sb.append("});\r\n");
			sb.append("//]]>\r\n");
			sb.append("</script>");
			sb.append(MJHttpUtil.createJsTag("/mj_js/mjtotal.js"));
			sb.append("</body>");
			sb.append("</html>");
			
			m_page_document = sb.toString();
			
		HttpResponse response = create_response(HttpResponseStatus.OK, m_page_document);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}
	
	private String getUpdateSubjectString(String sitem){
		StringBuilder sb		= new StringBuilder(256);
		StringBuilder sbQry		= new StringBuilder(64);
		Connection con 			= null;
		PreparedStatement pstm 	= null;
		ResultSet rs 			= null;
		
		sbQry.append("SELECT id, date, title FROM board_mjnotice WHERE type=1 ");
		if(sitem != null)
			sbQry.append("AND title LIKE ? ");
		sbQry.append("ORDER BY date DESC");
		
		try{			
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm		= con.prepareStatement(sbQry.toString());
			if(sitem != null)
				pstm.setString(1, "%" + sitem + "%");
			
			rs 			= pstm.executeQuery(); rs.last();
			int rownum	= rs.getRow(); rs.beforeFirst();
			if(rownum < 1){
				sb.append("<p class=\"nodata\">글이 없습니다.</p>");
				return sb.toString();
			}
			
			
			int id 		= 0;
			String date;
			String title;
			
			while(rs.next()){
				id 		= rs.getInt("id");
				date 	= rs.getString("date");
				title 	= rs.getString("title");
				
				sb.append("<li class=\"\">");
				sb.append("<a href=\"javascript:Board.view(");
				sb.append(id);
				sb.append(");\" class=\"subject\">");
				sb.append(title);
				sb.append("</a>");
				sb.append("<p class=\"info\">");
				sb.append("<span class=\"date\"> <img src=\"/img/notice_lineage.png\"> ");
				sb.append(date.replace("/", "."));
				sb.append("</span>");
				sb.append("</p>");
				sb.append("</li>");
			}			
		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (SecurityException e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return sb.toString();
	}
	
	private String getNoticeSubjectString(String sitem){
		StringBuilder sb		= new StringBuilder(256);
		StringBuilder sbQry		= new StringBuilder(64);
		Connection con 			= null;
		PreparedStatement pstm 	= null;
		ResultSet rs 			= null;
		
		sbQry.append("SELECT id, date, title FROM board_mjnotice WHERE type=0 ");
		if(sitem != null)
			sbQry.append("AND title LIKE ? ");
		sbQry.append("ORDER BY date DESC");
		try{			
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm		= con.prepareStatement(sbQry.toString());
			if(sitem != null)
				pstm.setString(1, "%" + sitem + "%");
			rs 			= pstm.executeQuery(); rs.last();
			int rownum	= rs.getRow(); rs.beforeFirst();
			if(rownum < 1){
				sb.append("<p class=\"nodata\">글이 없습니다.</p>");
				return sb.toString();
			}
			
			int id 		= 0;
			String date;
			String title;
			
			while(rs.next()){
				id 		= rs.getInt("id");
				date 	= rs.getString("date");
				title 	= rs.getString("title");
				
				sb.append("<li class=\"\">");
				sb.append("<a href=\"javascript:Board.view(");
				sb.append(id);
				sb.append(");\" class=\"subject\">");
				sb.append(title);
				sb.append("</a>");
				sb.append("<p class=\"info\">");
				sb.append("<span class=\"date\"> <img src=\"/img/notice_lineage.png\"> ");
				sb.append(date.replace("/", "."));
				sb.append("</span>");
				sb.append("</p>");
				sb.append("</li>");
			}			
		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (SecurityException e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return sb.toString();
	}
}
