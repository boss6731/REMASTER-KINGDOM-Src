package l1j.server.MJWebServer.Dispatcher.Template.Market;

import java.util.ArrayList;
import java.util.Comparator;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.MJWebServer.Dispatcher.Template.MJHttpResponse;
import l1j.server.MJWebServer.Dispatcher.Template.Market.API.MJMarketPriceLoader;
import l1j.server.MJWebServer.Dispatcher.Template.Market.API.POJO.MJMarketItemObject;
import l1j.server.MJWebServer.Dispatcher.Template.Market.API.POJO.Util.MJHttpUtil;
import l1j.server.MJWebServer.Dispatcher.Template.Market.API.POJO.Util.MJMPSECore;
import l1j.server.MJWebServer.Dispatcher.Template.Market.API.POJO.Util.MJMPSEElement;
import l1j.server.MJWebServer.Service.MJHttpRequest;

public class MJMarketItemInfoResponse extends MJHttpResponse{
	private static String m_page_document;
	private static int _itemId;
	private static String _name;
	private static int _itemGfx;
	private static int _tradeType;
	private static int _enchant;
	private static int _status;
	private static int _attr;
	private static String _sortColumn;
	private static int _sortType;
	private static int _page;
	private static String	_btnEchListJS;
	private static String	_btnStsListJS;
	private static String	_priceOrderListHead;
	private static String	_storeListHead;
	private static String	_statusListHead;
	private static String	_statusListTail;
	private static String	_enchantListHead;
	private static String	_enchantListTail;
	
	private static AscPrice 	_asc;
	private static DescPrice 	_desc;
	
	public MJMarketItemInfoResponse(MJHttpRequest request) {
		super(request);
		
		_itemId = Integer.valueOf(request.get_parameters().get("itemId").get(0));
		_name = request.get_parameters().get("itemName").get(0);
		_itemGfx = Integer.valueOf(request.get_parameters().get("itemGfx").get(0));
		_tradeType = Integer.valueOf(request.get_parameters().get("tradeType").get(0));
		_enchant = Integer.valueOf(request.get_parameters().get("enchant").get(0));
		_status = Integer.valueOf(request.get_parameters().get("status").get(0));
		_attr = Integer.valueOf(request.get_parameters().get("attribute").get(0));
		_sortColumn = request.get_parameters().get("sortColumn").get(0);
		_sortType = Integer.valueOf(request.get_parameters().get("sortType").get(0));
		if(_sortType == -1)
			_sortType = 0;
		_page = Integer.valueOf(request.get_parameters().get("page").get(0));
		
		StringBuilder sb = new StringBuilder();
		sb.append("<li>");
		sb.append("<a href=\"javascript:goMJItemInfo(\'0\', \'");
		_priceOrderListHead = sb.toString();
		
		sb = new StringBuilder(128);
		sb.append("<script type=\"text/javascript\">");
		sb.append("\r\n//<![CDATA[\r\n");
		sb.append("$j(\'#enchant_select > li\').click(function() { $j(\'#enchant\').html($j(this).html()); });");
		sb.append("\r\n//]]>\r\n");
		sb.append("</script>");
		_btnEchListJS = sb.toString(); 	
		
		sb = new StringBuilder(256);
		sb.append("<script type=\"text/javascript\">");
		sb.append("\r\n//<![CDATA[\r\n");
		sb.append("$j(\'.select_button\').click(function() {");
		sb.append("var layer = $j(this).siblings(\'.select_layer\');");
		sb.append("if(layer.is(\':visible\')) layer.hide();");
		sb.append("else	layer.show();");
		sb.append("});");
		sb.append("$j(\'.select_layer li\').click(function() {");
		sb.append("$j(\'.select_layer li\').removeClass(\'on\');");
		sb.append("var layer = $j(this).parents(\'.select_layer\');");
		sb.append("var button = layer.siblings(\'.select_button\');");
		sb.append("button.html ( $j(this).html() );");
		sb.append("$j(this).addClass(\'on\');");
		sb.append("if(layer.is(\':visible\'))	layer.hide();");
		sb.append("else	layer.show();");
		sb.append("});");
		sb.append("\r\n//]]>\r\n");
		sb.append("</script>");
		_btnStsListJS = sb.toString();
		
		_storeListHead 		= "<a href=\"javascript:goMJItemInfo(\'0\', \'";
		_statusListHead 	= "<li onclick=\"javascript:goMJItemInfo(\'0\', \'";
		_statusListTail		= "\'-1\');\">";
		_enchantListHead	= "<li onclick=\"javascript:goMJItemInfo(\'0\',\'";
		_enchantListTail	= "\'-1\', \'-1\');\">";
		_asc				= new AscPrice();
		_desc				= new DescPrice();
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
		sb.append("<title>plaync 리니지 :: 시세</title>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append(MJHttpUtil.getItemSearchBoxString());
		sb.append("<div id=\"contents\">");
		sb.append(getItemInfoString(_name, -1, _itemGfx, _tradeType, _enchant, _attr, _sortColumn, _sortType, _status, _page));
		sb.append("</div>");
		sb.append(MJHttpUtil.createJsTag("/mj_js/mjtotal.js"));
		sb.append("</body>");
		sb.append("</html>");
		
		m_page_document = sb.toString();
		
		HttpResponse response = create_response(HttpResponseStatus.OK, m_page_document);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
		return response;
	}
	
	private String getItemInfoString(String itemName, int itemId, int itemGfx, int tradeType, 
			int enchant, int attr, String sortColumn, int sortType, int status, int page){
		StringBuilder sb	= new StringBuilder(4096);
		sb.append("<div class=\"wrap_item\">");
		sb.append("<p class=\"wrap_thumb\">");
		sb.append(MJHttpUtil.createImgTag(itemGfx));
		sb.append("<a href=\"#\">");
		sb.append("</a>");
		sb.append("</p>");
		sb.append("<div class=\"itme_name\">");
		sb.append(getEnchantList(itemId, itemName, itemGfx, tradeType, enchant, status, attr, sortColumn, sortType, page));
		sb.append(_btnEchListJS);
		sb.append(getStatusList(itemId, itemName, itemGfx, tradeType, enchant, status, attr, sortColumn, sortType, page));
		sb.append(_btnStsListJS);
		sb.append("</div>");
		sb.append("</div>");
		sb.append(getStoreList(itemId, itemName, itemGfx, tradeType, enchant, status, attr, sortColumn, sortType));
		sb.append(getPriceOrderList(itemId, itemName, itemGfx, tradeType, enchant, status, attr, sortColumn, sortType));
		sb.append(getShopList(itemId, itemName, tradeType, enchant, status, attr, sortColumn, sortType));	
		return sb.toString();
	}
	
	private String getEnchantList(int itemId, String itemName, int itemGfx, int tradeType, 
			int enchant, int iden, int attr, String sortColumn, int sortType, int page){
		StringBuilder sb = new StringBuilder(512);
		sb.append("<span class=\"enchant\" id=\"enchant\" onclick=\"$j(\'#enchant_select\').show();\">");
		if(enchant == -1)
			sb.append("전체");
		else
			sb.append("+").append(enchant);
		sb.append("</span>");
		sb.append("<ul class=\"ul_enchant_select\" id=\"enchant_select\" onclick=\"$j(\'#enchant_select\').hide();\">");
		for(int i = -1; i<15; i++){
			sb.append(_enchantListHead);
			sb.append(itemId);
			sb.append("\', \'");
			sb.append(itemName);
			sb.append("\', \'");
			sb.append(itemGfx);
			sb.append("\', \'");
			sb.append(tradeType);
			sb.append("\', \'");
			sb.append(i);
			sb.append("\', \'");
			sb.append(iden);
			sb.append("\', \'");
			sb.append(attr);
			sb.append("\', \'");
			sb.append(sortColumn);
			sb.append("\', \'");
			sb.append(sortType);
			sb.append("\', \'");
			sb.append(page);
			sb.append("\');\">");
			if(i == -1)
				sb.append("全部");
			else
				sb.append("+").append(i);
			sb.append("</li>");
		}
		sb.append("</ul>");
		return sb.toString();
	}
	
	private String getStatusList(int itemId, String itemName, int itemGfx, int tradeType, 
			int enchant, int iden, int attr, String sortColumn, int sortType, int page){
		StringBuilder sb = new StringBuilder();

		sb.append("<strong>");
		sb.append(itemName);
		sb.append("</strong>");
		sb.append("<div class=\"selectOptionWrap\">");
		sb.append("<div class=\"statusWrap\">");
		sb.append("<span class=\"select_button\" id=\"status_selected\">");
		sb.append(MJHttpUtil.getIdenToDisplay(iden));
		sb.append("</span>");
		sb.append("<ul class=\"select_layer\" id=\"status_select\">");
		
		for(int i = 0; i<5; i++){
			sb.append(_statusListHead);
			sb.append(itemId);
			sb.append("\', \'");
			sb.append(itemName);
			sb.append("\', \'");
			sb.append(itemGfx);
			sb.append("\', \'");
			sb.append(tradeType);	// trade type
			sb.append("\', \'");
			sb.append(enchant);		// enchant
			sb.append("\', \'");
			if(i == 0)	sb.append(-1);
			else		sb.append(i);
			sb.append("\', \'");
			sb.append(attr);
			sb.append("\', \'");
			sb.append(sortColumn);
			sb.append("\', \'");
			sb.append(sortType);
			sb.append("\', \'");
			sb.append(page);
			sb.append("\');\">");
			if(i == 0)	sb.append(MJHttpUtil.getIdenToDisplay(-1));
			else		sb.append(MJHttpUtil.getIdenToDisplay(i));
			sb.append("</li>");
		}
		
		sb.append("</ul>");
		sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}
	
	private String getStoreList(int itemId, String itemName, int itemGfx, int tradeType, 
			int enchant, int status, int attr, String sortColumn, int sortType){
		StringBuilder sb = new StringBuilder(128);
		sb.append("<ul class=\"tab_storelist\">");
		for(int i=0; i<2; i++){
			sb.append("<li class=\"m");
			sb.append((i+1));
			sb.append("\">");
			sb.append(_storeListHead);
			sb.append(itemId);
			sb.append("\', \'");			
			sb.append(itemName);			
			sb.append("\', \'");	
			sb.append(itemGfx);
			sb.append("\', \'");
			sb.append(i);
			sb.append("\', \'");
			sb.append(enchant);
			sb.append("\', \'");
			sb.append(status);
			sb.append("\', \'");
			sb.append(attr);
			sb.append("\', \'");			
			sb.append(sortColumn);
			sb.append("\', \'");			
			sb.append(sortType);
			sb.append("\', \'");
			sb.append("1\');\" ");
			if(tradeType == i)
				sb.append("class=\"on\"");
			sb.append(">");
			sb.append("<span>");			
			sb.append(MJHttpUtil.getTradeTypeToDisplay(tradeType));			
			sb.append("</span>");			
			sb.append("");			
			sb.append("</a>");			
			sb.append("</li>");			
		}
		sb.append("</ul>");
		return sb.toString();
	}
	
	private String getPriceOrderList(int itemId, String itemName, int itemGfx, int tradeType, 
			int enchant, int status, int attr, String sortColumn, int sortType){
		StringBuilder sb = new StringBuilder(256);
		sb.append("<ul class=\"ul_orderby\">");
		for(int i=0; i<2; i++){
			sb.append(_priceOrderListHead);
			sb.append(itemId);
			sb.append("\', \'");
			sb.append(itemName);
			sb.append("\', \'");
			sb.append(itemGfx);
			sb.append("\', \'");
			sb.append(tradeType);
			sb.append("\', \'");
			sb.append(enchant);
			sb.append("\', \'");
			sb.append(status);
			sb.append("\', \'");
			sb.append(attr);
			sb.append("\', \'price\', \'");
			sb.append(i);
			sb.append("\');\" ");
			if(sortType == i)
				sb.append("class=\"on\">");
			else
				sb.append(">");
			sb.append(MJHttpUtil.getSortTypeToDisplay(i));
			sb.append("</a>");
			sb.append("</li>");
			if(i == 0)
				sb.append("|");
		}
		sb.append("</ul>");
		return sb.toString();
	}
	
	private String getShopList(int itemId, String itemName, int tradeType, 
			int enchant, int status, int attr, String sortColumn, int sortType){
		StringBuilder sb = new StringBuilder(1024);
		
		ArrayList<MJMarketItemObject> list = getResultList(itemId, itemName, tradeType, enchant, status, attr, sortColumn, sortType);
		sb.append("<div class=\"wrap_storelist\" id=\"wrap_storelist\">");
		sb.append("<ul class=\"ul_storelist\" id=\"type_");
		if(tradeType == 1) 	sb.append("sell\">");
		else				sb.append("buy\">");
		
		if(list == null || list.size() <= 0)
			sb.append("<div class=\"storelist_none\">선택하신 아이템을<br/>거래중인 상점이 없습니다.<br/><br/>아이템명 아래의 상태 또는 속성 값을 변경해<br/>다른 상점들을 확인하세요.<br/></div>");
		else{
			MJMarketItemObject obj = null;
			int size = list.size();
			for(int i=0; i<size; i++){
				obj = list.get(i);
				
				sb.append("<li>");
				sb.append("<dl>");
				sb.append("<dt>");
				if(obj.attr != -1)
					sb.append(MJHttpUtil.getAttrToDisplay(obj.attr));
				if(obj.enchant > 0)
					sb.append("+").append(obj.enchant);
				sb.append(" "+itemName);
				sb.append("&nbsp;");
				sb.append("<span class=\"count\">");
				sb.append("[<strong>");
				sb.append(obj.count);
				sb.append("</strong>]");
				sb.append("</span>");
				sb.append("</dt>");
				sb.append("<dd class=\"price\">");
				sb.append(MJHttpUtil.getPriceFormat(obj.price));
				sb.append("</dd>");
				sb.append("<dd class=\"state\">");
				sb.append(MJHttpUtil.getIdenToDisplay(obj.iden));
				sb.append("</dd>");
				sb.append("<dd class=\"location\">");
				sb.append("<a href=\"javascript:L1.FindMerchant(\'");
				sb.append(obj.cname);
				sb.append("\')\">");
				sb.append("<span class=\"tradeuser\">");
				sb.append(obj.cname);
				sb.append("</span>");
				sb.append("(중앙구역)");
				sb.append("</a>");
				sb.append("</dd>");
				sb.append("</dl>");
				sb.append("</li>");
			}
		}
		sb.append("</ul>");
		sb.append("<p class=\"noti_tradeuser\">* 상인 이름을 클릭하시면 상인 앞으로 바로 이동합니다.</p>");
		sb.append("</div>");
		return sb.toString();
	}
	
	private ArrayList<MJMarketItemObject> getMarketList(String itemName, int tradeType){
		ArrayList<MJMarketItemObject> tmp	= null;
		ArrayList<MJMarketItemObject> sList = null;
		
		MJMPSEElement element = MJMPSECore.getInstance().getElement(itemName);
		if(element == null)
			return null;
		
		sList = getMarketList(element.normalId, itemName, tradeType);
		if(sList == null)
			sList 	= getMarketList(element.blessId, itemName, tradeType);
		else{
			tmp		= getMarketList(element.blessId, itemName, tradeType);
			if(tmp != null) sList.addAll(tmp);
		}
		if(sList == null)
			sList 	= getMarketList(element.curseId, itemName, tradeType);
		else{
			tmp		= getMarketList(element.curseId, itemName, tradeType);
			if(tmp != null) sList.addAll(tmp);
		}
		return sList;
	}
	
	private ArrayList<MJMarketItemObject> getMarketList(int itemId, String itemName, int tradeType){
		if(itemId == -1)
			return getMarketList(itemName, tradeType);
		
		ArrayList<MJMarketItemObject> sells = MJMarketPriceLoader.getInstance().getSellings(itemId);
		ArrayList<MJMarketItemObject> purs 	= MJMarketPriceLoader.getInstance().getPurchasings(itemId);
		ArrayList<MJMarketItemObject> sList = null;
	
		if(tradeType == -1){
			if(sells != null && purs != null){
				sList = new ArrayList<MJMarketItemObject>(sells.size() + purs.size());
				sList.addAll(sells);
				sList.addAll(purs);
			}else if(sells != null){
				sList = new ArrayList<MJMarketItemObject>(sells.size());
				sList.addAll(sells);
			}else if(purs != null){
				sList = new ArrayList<MJMarketItemObject>(purs.size());
				sList.addAll(purs);
			}
		}else if(tradeType == 0){
			if(sells != null){
				sList = new ArrayList<MJMarketItemObject>(sells.size());
				sList.addAll(sells);
			}
		}else if(tradeType == 1){
			if(purs != null){
				sList = new ArrayList<MJMarketItemObject>(purs.size());
				sList.addAll(purs);
			}
		}
		
		return sList;
	}
	
	private ArrayList<MJMarketItemObject> getResultList(int itemId, String itemName, int tradeType, 
			int enchant, int status, int attr, String sortColumn, int sortType){
		ArrayList<MJMarketItemObject> list = getMarketList(itemId, itemName, tradeType);
		if(list == null || list.size() <= 0)
			return null;
		
		ArrayList<MJMarketItemObject> 	results = new ArrayList<MJMarketItemObject>(list.size());
		int 							size 	= list.size();
		MJMarketItemObject 				obj		= null;
		for(int i=0; i<size; i++){
			obj = list.get(i);
			if(attr != -1 && obj.attr != attr)
				continue;
			
			if(status != -1 && obj.iden != status)
				continue;
			
			if(enchant != -1 && obj.enchant != enchant)
				continue;
			
			results.add(obj);
		}
		
		if(results == null || results.size() <= 0) 	return null;
		else if(sortType == 1)						results.sort(_desc);
		else										results.sort(_asc);
		return results;
	}
	
	@SuppressWarnings("unused")
	private String getFailString(){
		StringBuilder sb = new StringBuilder(128);
		sb.append("<div class=\"wrap_search_result\" style=\"width:100%;text-align:center;margin-top:130px;\">");
		sb.append("<p style=\"color:#ab6f11;\"><strong>상점 정보가 없습니다.</strong> </p>");
		sb.append("</div>");
		return sb.toString();
	}
	
	class AscPrice implements Comparator<MJMarketItemObject>{
		@Override
		public int compare(MJMarketItemObject arg0, MJMarketItemObject arg1) {
			return arg0.price - arg1.price;
		}
		
	}
	
	class DescPrice implements Comparator<MJMarketItemObject>{
		@Override
		public int compare(MJMarketItemObject arg0, MJMarketItemObject arg1) {
			return arg1.price - arg0.price;
		}
	}
}
