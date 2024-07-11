package l1j.server.MJWebServer.Dispatcher.Template.Market;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Character.MJUser;
import l1j.server.MJWebServer.Dispatcher.Template.Market.API.MJMarketPriceLoader;
import l1j.server.MJWebServer.Dispatcher.Template.Market.API.POJO.MJMarketItemObject;
import l1j.server.MJWebServer.Dispatcher.Template.Market.API.POJO.Util.MJHttpUtil;
import l1j.server.MJWebServer.Dispatcher.Template.Market.API.POJO.Util.MJMPSECore;
import l1j.server.MJWebServer.Dispatcher.Template.Market.API.POJO.Util.MJMPSEElement;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJMarketSearchResponse extends MJHttpResponse {
	private static String m_page_document;
	private String query;

	public MJMarketSearchResponse(MJHttpRequest request) {
		super(request);
		if (request.get_parameters().get("query") == null) {
			try {
				query = MJUser.cookiesToItemQuery(request.get_cookies());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			query = request.get_parameters().get("query").get(0);
		}
	}

	@Override
	public HttpResponse get_response() throws l1j.server.MJWebServer.Dispatcher.MJHttpClosedException {
		if(m_page_document == null) {
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
			sb.append("<title>plaync 리니지 :: 시세</title>");
			sb.append("</head>");
			sb.append("<body>");
			sb.append(MJHttpUtil.getItemSearchBoxString());
			sb.append("<div id=\"contents\">");
			m_page_document = sb.toString();
		}
		/**
		 * 검색 내용
		 */
		StringBuilder responseBuilder = new StringBuilder(256 + m_page_document.length());
		responseBuilder.append(m_page_document);
		responseBuilder.append(getSearchListString(query));
		responseBuilder.append("</div>");
		responseBuilder.append(MJHttpUtil.createJsTag("/mj_js/mjtotal.js"));
		responseBuilder.append("</body>");
		responseBuilder.append("</html>");
		String s = responseBuilder.toString();
		HttpResponse response = create_response(HttpResponseStatus.OK, s);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}

	private String getSearchListString(String query) {
		MJMPSEElement ele = MJMPSECore.getInstance().getElement(query);
		if (ele != null)
			return getSearchSingleString(ele, query);
		return getSearchMultipleString(query);
	}

	private String getSearchSingleString(MJMPSEElement element, String query) {
		StringBuilder sb = new StringBuilder(1024);
		sb.append("<div class=\"wrap_search_result\">");
		sb.append("<p class=\"pageinfo\">");
		sb.append("<strong>");
		sb.append(query);
		sb.append("</strong>");
		sb.append("검색 결과 ");
		sb.append("<span class=\"count\"><strong>1</strong> 건</span>");
		sb.append("</p>");
		sb.append("<ul class=\"ul_itemlist\">");
		String normalInfo = MJHttpUtil.createATagForGoItem(0, -1, element.real_name_id_view, element.icon_id, 0, -1, -1, -1, "", 0,
				0);
		sb.append("<li>");
		sb.append("<p class=\"thumb\">");
		sb.append(normalInfo);
		sb.append(MJHttpUtil.createImgTag(element.icon_id));
		sb.append("</a>");
		sb.append("</p>");
		sb.append("<dl>");
		sb.append("<dt>");
		sb.append(normalInfo);
		sb.append(element.real_name_id_view);
		sb.append("</a>");
		sb.append("<a class=\"ingameshop no\" href=\"javascript:L1.OpenIngameShop(\'");
		sb.append(element.real_name_id_view);
		sb.append("\', \'\')\">아이템상점</a>");
		sb.append("</dt>");
		sb.append(getSellingString(element, normalInfo));
		sb.append(getPurchasingString(element));
		sb.append("</dl>");
		sb.append("</li>");
		sb.append("</ul>");
		sb.append("</div>");
		return sb.toString();
	}

	public static String getSearchMultipleString(String query) {
		if(MJString.isNullOrEmpty(query)) {
			StringBuilder sb = new StringBuilder(256);
			sb.append("<div class=\"wrap_search_result\">");
			sb.append(getFailString("검색된 결과가 없습니다.", "검색어를 확인하신 후 다시 검색해 주세요"));
			sb.append("</div>");
			return sb.toString();
		}
		
		ArrayList<String> keys = MJMPSECore.getInstance().getKeyworlds(query);
		if (keys == null || keys.size() <= 0) {
			StringBuilder sb = new StringBuilder(256);
			sb.append("<div class=\"wrap_search_result\">");
			sb.append(getFailString("검색된 결과가 없습니다.", "검색어를 확인하신 후 다시 검색해 주세요"));
			sb.append("</div>");
			return sb.toString();
		}

		int keySize = keys.size();
		StringBuilder sb = new StringBuilder(1024 + (keySize * 256));
		sb.append("<div class=\"wrap_search_result\">");
		sb.append("<p class=\"pageinfo\">");
		sb.append("<strong>");
		sb.append(query);
		sb.append("</strong>");
		sb.append("검색 결과 ");
		sb.append("<span class=\"count\">");
		sb.append("<strong>");
		int cnt = 0;
		StringBuilder sbResult = new StringBuilder(1024);
		for (int i = 0; i < keySize; i++) {
			String key = keys.get(i);
			MJMPSEElement element = MJMPSECore.getInstance().getElement(key);

			/*if (element == null || !MJMarketPriceLoader.getInstance().contains(element))
				continue;*/

			cnt++;
			String normalInfo = MJHttpUtil.createATagForGoItem(0, -1, element.real_name_id_view, element.icon_id, 0, -1, -1, -1, "",
					0, 0);
			sbResult.append("<li>");
			sbResult.append("<p class=\"thumb\">");
			sbResult.append(normalInfo);
			sbResult.append(MJHttpUtil.createImgTag(element.icon_id));
			sbResult.append("</a>");
			sbResult.append("</p>");
			sbResult.append("<dl>");
			sbResult.append("<dt>");
			sbResult.append(normalInfo);
			sbResult.append(element.real_name_id_view);
			sbResult.append("</a>");
			sbResult.append("<a class=\"ingameshop no\" href=\"javascript:L1.OpenIngameShop(\'");
			sbResult.append(element.real_name_id_view);
			sbResult.append("\', \'\')\">아이템상점</a>");
			sbResult.append("</dt>");
			sbResult.append(getSellingString(element, normalInfo));
			sbResult.append(getPurchasingString(element));
			sbResult.append("</dl>");
			sbResult.append("</li>");
		}
		sb.append(cnt);
		sb.append("</strong>");
		sb.append(" 건");
		sb.append("</span>");
		sb.append("</p>");
		sb.append("<ul class=\"ul_itemlist\">");
		sb.append(sbResult.toString());
		sb.append("</ul>");
		sb.append("</div>");
		return sb.toString();
	}

	private static String getPurchasingString(MJMPSEElement element) {
		StringBuilder sb = new StringBuilder(512);

		sb.append("<dd class=\"price buy\">");
		ArrayList<MJMarketItemObject> normals = element.normalId > 0
				? MJMarketPriceLoader.getInstance().getPurchasings(element.normalId)
				: null;
		ArrayList<MJMarketItemObject> blesses = element.blessId > 0
				? MJMarketPriceLoader.getInstance().getPurchasings(element.blessId)
				: null;
		ArrayList<MJMarketItemObject> curses = element.curseId > 0
				? MJMarketPriceLoader.getInstance().getPurchasings(element.curseId)
				: null;
		if (normals == null && blesses == null && curses == null)
			sb.append(getPriceNoneString());
		else {
			sb.append("<p class=\"total\">");
			sb.append(
					MJHttpUtil.createATagForGoItem(0, -1, element.real_name_id_view, element.icon_id, 1, -1, -1, -1, "price", 1, 0));

			int min = 0, max = 0, count = 0;
			int normalMin, blessMin, curseMin;
			int normalMax, blessMax, curseMax;
			String sNormal = null;
			String sBless = null;
			String sCurse = null;
			StringBuilder nons = new StringBuilder(32);
			if (normals != null) {
				min = normalMin = normals.get(0).price;
				max = normalMax = normals.get(normals.size() - 1).price;
				count = normals.size();
				sNormal = getResultMoreString(normals.get(0), normalMin, normalMax, normals.size());
			} else
				nons.append(" [").append(MJHttpUtil.getIdenToDisplay(2)).append("]");

			if (blesses != null) {
				blessMin = blesses.get(0).price;
				blessMax = blesses.get(blesses.size() - 1).price;
				count += blesses.size();
				if (min == 0) {
					min = blessMin;
					max = blessMax;
				} else {
					min = Math.min(blessMin, min);
					max = Math.max(blessMax, max);
				}

				sBless = getResultMoreString(blesses.get(0), blessMin, blessMax, blesses.size());
			} else
				nons.append(" [").append(MJHttpUtil.getIdenToDisplay(3)).append("]");

			if (curses != null) {
				curseMin = curses.get(0).price;
				curseMax = curses.get(curses.size() - 1).price;
				count += curses.size();
				if (min == 0) {
					min = curseMin;
					max = curseMax;
				} else {
					min = Math.min(curseMin, min);
					max = Math.max(curseMax, max);
				}

				sCurse = getResultMoreString(curses.get(0), curseMin, curseMax, curses.size());
			} else
				nons.append(" [").append(MJHttpUtil.getIdenToDisplay(4)).append("]");

			sb.append(MJHttpUtil.getPriceFormat(min));
			sb.append("~");
			sb.append(MJHttpUtil.getPriceFormat(max));
			sb.append("</a>");
			sb.append("<em>[<strong class=\"store blind\">");
			sb.append(count);
			sb.append("</strong>]</em>");
			sb.append("</p>");
			if (sNormal != null)
				sb.append(sNormal);
			if (sBless != null)
				sb.append(sBless);
			if (sCurse != null)
				sb.append(sCurse);
			if (nons.length() > 0)
				sb.append(getPriceNoneString(nons.toString()));
		}
		sb.append("</dd>");
		return sb.toString();
	}
	
	private static String getSellingString(MJMPSEElement element, String normalInfo) {
		StringBuilder sb = new StringBuilder(512);

		sb.append("<dd class=\"price sell\">");
		ArrayList<MJMarketItemObject> normals = element.normalId > 0
				? MJMarketPriceLoader.getInstance().getSellings(element.normalId)
				: null;
		ArrayList<MJMarketItemObject> blesses = element.blessId > 0
				? MJMarketPriceLoader.getInstance().getSellings(element.blessId)
				: null;
		ArrayList<MJMarketItemObject> curses = element.curseId > 0
				? MJMarketPriceLoader.getInstance().getSellings(element.curseId)
				: null;
		if (normals == null && blesses == null && curses == null)
			sb.append(getPriceNoneString());
		else {
			sb.append("<p class=\"total\">");
			sb.append(normalInfo);

			int min = 0, max = 0, count = 0;
			int normalMin, blessMin, curseMin;
			int normalMax, blessMax, curseMax;
			String sNormal = null;
			String sBless = null;
			String sCurse = null;
			StringBuilder nons = new StringBuilder(32);
			if (normals != null) {
				min = normalMin = normals.get(0).price;
				max = normalMax = normals.get(normals.size() - 1).price;
				count = normals.size();
				sNormal = getResultMoreString(normals.get(0), normalMin, normalMax, normals.size());
			} else
				nons.append(" [").append(MJHttpUtil.getIdenToDisplay(2)).append("]");

			if (blesses != null) {
				blessMin = blesses.get(0).price;
				blessMax = blesses.get(blesses.size() - 1).price;
				count += blesses.size();
				if (min == 0) {
					min = blessMin;
					max = blessMax;
				} else {
					min = Math.min(blessMin, min);
					max = Math.max(blessMax, max);
				}

				sBless = getResultMoreString(blesses.get(0), blessMin, blessMax, blesses.size());
			} else
				nons.append(" [").append(MJHttpUtil.getIdenToDisplay(3)).append("]");

			if (curses != null) {
				curseMin = curses.get(0).price;
				curseMax = curses.get(curses.size() - 1).price;
				count += curses.size();
				if (min == 0) {
					min = curseMin;
					max = curseMax;
				} else {
					min = Math.min(curseMin, min);
					max = Math.max(curseMax, max);
				}

				sCurse = getResultMoreString(curses.get(0), curseMin, curseMax, curses.size());
			} else
				nons.append(" [").append(MJHttpUtil.getIdenToDisplay(4)).append("]");

			sb.append(MJHttpUtil.getPriceFormat(min));
			sb.append("~");
			sb.append(MJHttpUtil.getPriceFormat(max));
			sb.append("</a>");
			sb.append("<em>[<strong class=\"store blind\">");
			sb.append(count);
			sb.append("</strong>]</em>");
			sb.append("</p>");
			if (sNormal != null)
				sb.append(sNormal);
			if (sBless != null)
				sb.append(sBless);
			if (sCurse != null)
				sb.append(sCurse);
			if (nons.length() > 0)
				sb.append(getPriceNoneString(nons.toString()));
		}
		sb.append("</dd>");
		return sb.toString();
	}

	private static String getFailString(String msg, String msg2) {
		StringBuilder sb = new StringBuilder(64);
		sb.append("<div class=\"search_result_none\">");
		sb.append("<strong>");
		sb.append(msg);
		sb.append("</strong>");
		sb.append("<br>");
		sb.append(msg2);
		sb.append("</div>");

		return sb.toString();
	}

	private static String getResultMoreString(MJMarketItemObject obj, int min, int max, int count) {
		StringBuilder sb = new StringBuilder(128);
		sb.append("<p>");
		sb.append(MJHttpUtil.createATagForGoItem(0, obj.itemId, obj.real_name_id_view, obj.icon_id, obj.type, -1, obj.iden, -1,
				"price", 0, 0));
		sb.append(" [");
		sb.append(MJHttpUtil.getIdenToDisplay(obj.iden));
		sb.append("] ");
		sb.append(MJHttpUtil.getPriceFormat(min));
		sb.append("~");
		sb.append(MJHttpUtil.getPriceFormat(max));
		sb.append("</a>");
		sb.append("<em>[<strong class=\"store blind\">");
		sb.append(count);
		sb.append("</strong>]</em>");
		sb.append("</p>");
		return sb.toString();
	}

	private static String getPriceNoneString() {
		return "<p class=\"price_none\">  [미확인] [일반] [축복] [저주] 판매 정보 없음.</p>";
	}

	private static String getPriceNoneString(String msg) {
		return String.format("<p class=\"price_none\"> %s 판매 정보 없음.</p>", msg);
	}
}
