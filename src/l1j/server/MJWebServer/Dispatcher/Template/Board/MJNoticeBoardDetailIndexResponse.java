package l1j.server.MJWebServer.Dispatcher.Template.Board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Market.API.POJO.Util.MJHttpUtil;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.utils.SQLUtil;

public class MJNoticeBoardDetailIndexResponse extends MJHttpResponse {
	private static String m_page_document;
	private static int article;
	
	public MJNoticeBoardDetailIndexResponse(MJHttpRequest request) {
		super(request);

		article = MJHttpUtil.paramParseInt(request.get_parameters().get("article").get(0));
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
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
			sb.append("<title>plaync 리니지 :: 소식</title>");
			sb.append("</head>");
			sb.append("<body>");
			sb.append(MJHttpUtil.createLinkTag("text/css", "/mj_css/board_gamedict.css"));
			sb.append("<div id=\"header\">");
			sb.append("<input type=\"hidden\" name=\"searchCondition\" id=\"searchCondition\" value=\"6\" />");
			sb.append("<input type=\"hidden\" name=\"searchFocusFlag\" id=\"searchFocusFlag\" value=\"N\" />");
			sb.append("<h1>");
			sb.append("<a href=\"javascript:Board.noticeList(\'notice\',1);\">");
			sb.append("<img src=\"/img/notice.gif\" alt=\"소식\" /></a>");
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
			sb.append("<div class=\"view_ty1\">");
			sb.append("<div class=\"subject\">");			
			
			if(article != -1)	
				sb.append(getNoticeContentString(article));
			else
				sb.append("<p class=\"nodata\">글이 없습니다.</p>");
			
			sb.append("</div></div>");
			sb.append("<div class=\"wrap_btn\">");
			sb.append("<a href=\"javascript:Board.noticeList(\'notice\', 1);\"><img src=\"/img/btn_list.gif\" alt=\"목록\"/></a>");
			sb.append("</div>");
			sb.append("</div>");
			sb.append("</div>");
			sb.append(MJHttpUtil.createJsTag("/mj_js/mjtotal.js"));
			sb.append("</body>");
			sb.append("</html>");
			
			m_page_document = sb.toString();
				
		HttpResponse response = create_response(HttpResponseStatus.OK, m_page_document);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}

	private String getNoticeContentString(int article){
		StringBuilder sb		= new StringBuilder(512);
		StringBuilder sbQry		= new StringBuilder(64);
		Connection con 			= null;
		PreparedStatement pstm 	= null;
		ResultSet rs 			= null;
		
		sbQry.append("SELECT * FROM board_mjnotice WHERE id=?");
		try{			
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm		= con.prepareStatement(sbQry.toString());
			pstm.setInt(1, article);
			rs 			= pstm.executeQuery();

			if(!rs.next()){
				sb.append("<p class=\"nodata\">글이 없습니다.</p>");
				return sb.toString();
			}
			
			String title 		= rs.getString("title");
			String content		= rs.getString("content") + "\n";
			String date			= rs.getString("date");
			String[] contents	= content.split("\n");
			
			sb.append("<strong>");
			sb.append(title);
			sb.append("</strong>");
			sb.append("</div>");
			sb.append("<div class=\"info\">");
			sb.append(date);
			sb.append("</div>");
			sb.append("<div class=\"article\" id=\"view_contents\">");
			sb.append("<div class=\"xed\">");
			for(int i=0; i<contents.length; i++){
				sb.append("<p>");
				sb.append("<font size=\"");
				sb.append("2");
				sb.append("\" face=\"");
				sb.append("Malgun Gothic");
				sb.append("\">");
				if(i == 0){
					sb.append("<strong>");
					sb.append(contents[i]);
					sb.append("</strong>");
				}else{
					sb.append(contents[i]);
				}
				sb.append("</font></p>");
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
