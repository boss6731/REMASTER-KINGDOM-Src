package l1j.server.MJWebServer.Dispatcher.Template.Market;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Market.API.POJO.MJRecommendItemFormat;
import l1j.server.MJWebServer.Dispatcher.Template.Market.API.POJO.Util.MJHttpUtil;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.utils.SQLUtil;

public class MJMarketIndexResponse extends MJHttpResponse {
	private static String m_page_document;

	public MJMarketIndexResponse(MJHttpRequest request) {
		super(request);
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		StringBuilder sb = new StringBuilder(2048);
		sb.append(
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		sb.append("<head>");
		sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />");
		sb.append("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=Edge\" />");
		sb.append(MJHttpUtil.createLinkTag("text/css", "/mj_css/mjtotal.css"));
		sb.append(MJHttpUtil.createJsTag("/mj_js/jslib_p170_j171.js"));
		sb.append(MJHttpUtil.createJsTag("/mj_js/NCScriptUtil.js"));
		sb.append("<script type=\"text/javascript\">");
		sb.append(
				"var ingame_staticurl = \'http://static.plaync.co.kr/ingame/lineage\'; var ingame_url = \'http://g.lineage.plaync.co.kr\';");
		sb.append("</script>");
		sb.append(MJHttpUtil.createJsTag("/mj_js/item.js"));
		sb.append(MJHttpUtil.createJsTag("/mj_js/rank.js"));
		sb.append(MJHttpUtil.createJsTag("/mj_js/board.js"));
		sb.append(MJHttpUtil.createJsTag("/mj_js/ingame.js"));
		sb.append("<script type=\"text/javascript\">var $j = jQuery.noConflict();</script>");
		sb.append("<title>plaync °∂Ù∏”—°∑ :: „ºÌﬁ §Ã´</title>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append(MJHttpUtil.getItemSearchBoxString());
		sb.append("<div id=\"contents\">");
		sb.append("<div class=\"hotItems\">");
		sb.append("<ul id=\"hotItems\">");

		ArrayList<MJRecommendItemFormat> fList = getRecommends();
		if (fList != null) {
			int size = fList.size();
			MJRecommendItemFormat format = null;
			for (int i = 0; i < size; i++) {
				format = fList.get(i);
				sb.append("<li>");
				sb.append("<div class=\"wrap_hotitem\">");
				sb.append("<p class=\"wrap_thumb\">");
				sb.append("<a href=\"#\">");
				sb.append(MJHttpUtil.createImgTag(format.getGfx()));
				sb.append("</a>");
				sb.append("</p>");
				sb.append("<div class=\"item_name\">");
				sb.append("<a href=\"javascript:Board.goSearchItem(\'");
				sb.append(format.getName());
				sb.append("\')\">");
				sb.append("<strong>");
				sb.append(format.getName());
				sb.append("</strong>");
				sb.append("</a>");
				sb.append("<div class=\"item_price\">");
				sb.append("<div class=\"sell\"><em>ıÃÓ∏? §</em><span>");
				sb.append("<strong class=\"low\">");
				sb.append("-");
				sb.append("</strong>");
				sb.append("</span>");
				sb.append("</div>");
				sb.append("<div class=\"buy\"><em>ıÃÕ‘œ≈ÿ‚ §</em> <span>");
				sb.append("<strong class=\"heigh\">");
				sb.append("-");
				sb.append("</strong>");
				sb.append("</span>");
				sb.append("</div>");
				sb.append("</div>");
				sb.append("</div>");
				sb.append("<a class=\"ingameshop no\" href=\"javascript:L1.OpenIngameShop(\'");
				sb.append(format.getName());
				sb.append("\', \'\')\">");
				sb.append(format.getName());
				sb.append("</a>");
				sb.append("</div>");
				sb.append("</li>");
			}
		}
		sb.append("</ul>");

		sb.append("<div id=\"hotItemsPager\" class=\"rollingPager\">");
		sb.append("</div>");
		sb.append("</div>");
		sb.append("<script language=\"javascript\" type=\"text/javascript\">\r\n");
		sb.append("function callRolling() {");
		sb.append("var hotItem = new rollingData({");
		sb.append("banner: \'#hotItems\',");
		sb.append("bannerFind: \'li\',");
		sb.append("page: \'#hotItemsPager\',");
		sb.append("startAt:0,");
		sb.append("fn: [\'basic\'],");
		sb.append("time: ");
		sb.append(3000);
		sb.append("});");
		sb.append("};");
		sb.append("$j(document).ready(function(){");
		sb.append("callRolling();");
		sb.append("});");
		sb.append("</script>");
		sb.append("<div class=\"wrap_search_result\">");
		sb.append("<p class=\"pageinfo\"><em>");
//		sb.append(Config.Login.GameServerName);
		sb.append(" ˙ºÔ◊Ú¶—ı");
		sb.append("</em></p>");
		sb.append("</div>");
		sb.append("<div class=\"msg_favorite\">");
		sb.append("<p><center>?‚Û˙ºÔ◊Ó‹€∞€ˆ</center><p><center>ÙÎÓ§ıÃﬂæ€∞‚√Ï˝?È©?‚ÛÓ‹⁄™˘°Ÿ£ˆ‡.</center></p></p>");
//		sb.append("<p><center>Ω√ºº ∞Àªˆ¿ª ≈Î«œø© ∫∏¥Ÿ ∆Ì∏Æ«œ∞‘</center><p><center>∆«∏≈«œ¥¬ æ∆¿Ã≈€¿« Ω√ºº∏¶ »Æ¿Œ«œººø‰.</center></p></p>");
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

	private ArrayList<MJRecommendItemFormat> getRecommends() {
		//TODO √ﬂ√µ æ∆¿Ã≈€
		//System.out.println(Config.ADVICE_ITEM.toString());
		int[] recommends = Config.Web.adviceItems;
		ArrayList<MJRecommendItemFormat> fList = new ArrayList<MJRecommendItemFormat>(recommends.length);
		MJRecommendItemFormat format = null;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int id = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			for (int i = 0; i < recommends.length; i++) {
				format = new MJRecommendItemFormat();
				pstm = con.prepareStatement("SELECT item_id, real_name_id_view, icon_id from etcitem WHERE item_id=? UNION SELECT item_id, real_name_id_view, icon_id from weapon WHERE item_id=? UNION SELECT item_id, real_name_id_view, icon_id from armor WHERE item_id=?");
				id = recommends[i];
				pstm.setInt(1, id);
				pstm.setInt(2, id);
				pstm.setInt(3, id);
				rs = pstm.executeQuery();
				if (rs.next()) {
					format.setItemId(id);
					format.setName(rs.getString("real_name_id_view"));
					format.setGfx(rs.getInt("icon_id"));
					fList.add(format);
				}
				rs.close();
				pstm.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		return fList;
	}
}
