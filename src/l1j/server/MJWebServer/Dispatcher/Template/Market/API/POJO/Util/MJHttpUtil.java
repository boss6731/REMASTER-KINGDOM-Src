package l1j.server.MJWebServer.Dispatcher.Template.Market.API.POJO.Util;

import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public class MJHttpUtil {
	private static StringBuilder _sbItemSearchBox;
	
	public static String getItemSearchBoxString(){
		if(_sbItemSearchBox == null){
			_sbItemSearchBox = new StringBuilder(512);
			_sbItemSearchBox.append("<input type=\"hidden\" name=\"searchFocusFlag\" id=\"searchFocusFlag\" value=\"Y\" />");
			_sbItemSearchBox.append("<div id=\"header\">");
			_sbItemSearchBox.append("<h1>");
			_sbItemSearchBox.append("<a href=\"/ingame/item/intro\">");
			_sbItemSearchBox.append("<img src=\"/img/item.gif\" alt=\"시세\" />");
			_sbItemSearchBox.append("</a>");
			_sbItemSearchBox.append("</h1>");
			_sbItemSearchBox.append("<div class=\"wrap_search\">");
			_sbItemSearchBox.append("<input type=\"text\" value=\"검색어를 입력해 주세요\" ");
			_sbItemSearchBox.append("onfocus=\"if(this.value == \'검색어를 입력해 주세요\'){this.value=\'\'; document.getElementById(\'searchFocusFlag\').value=\'Y\'}\"");
			_sbItemSearchBox.append("onblur=\"if(this.value == \'\'){this.value=\'검색어를 입력해 주세요\'; document.getElementById(\'searchFocusFlag\').value=\'N\'}\"");
			_sbItemSearchBox.append("id=\"searchKeyword\" name=\"searchKeyword\" class=\"suggest\" onkeypress=\"if(window.event.keyCode==13){javascript:Board.goSearchItem();}\" maxlength=\"12\" />");
			_sbItemSearchBox.append("<input id=\"searchType\" type=\"hidden\" value=\"0\">");
			_sbItemSearchBox.append("<a href=\"javascript:Board.goSearchItem()\"><input type=\"image\" value=\"검색\" src=\"/img/ic_search.gif\" id=\"sitem_submit\" onclick=\"javascript:Board.goSearchItem()\" /></a>");
			_sbItemSearchBox.append("</div>");
			_sbItemSearchBox.append("</div>");
			_sbItemSearchBox.append("<script type=\"text/javascript\">");
			_sbItemSearchBox.append("//<![CDATA[\r\n");
			_sbItemSearchBox.append("$j(\'#sitem\').focus(function() {");
			_sbItemSearchBox.append("if($j(this).val() == \"검색어를 입력해 주세요\") {$j(this).val(\'\').css(\'borderColor\', \'#ab6f12\');}");
			_sbItemSearchBox.append("$j(this).css(\'borderColor\', \'#ab6f12\');");
			_sbItemSearchBox.append("}).blur(function() {");
			_sbItemSearchBox.append("if(!$j(this).val()) {$j(this).val(\"검색어를 입력해 주세요\");}");
			_sbItemSearchBox.append("$j(this).css(\'borderColor\', \'#71604e\');");
			_sbItemSearchBox.append("});\r\n");
			_sbItemSearchBox.append("//]]>\r\n");
			_sbItemSearchBox.append("</script>");
		}
		return _sbItemSearchBox.toString();
	}
	
	public static void sendData(HttpServletResponse response, String content){
		response.setContentType("text/html;charset=UTF-8");
		/*
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		*/
		try{
			PrintWriter out = response.getWriter();
			out.print(content);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void sendGZipData(HttpServletResponse response, String gzipContent){
		ServletOutputStream 	sos = null;
		BufferedOutputStream 	bos = null;
		GZIPOutputStream 		gos = null;
		
		try{
			response.setContentType("text/html;charset=UTF-8");
			response.setHeader("Content-Encoding", "gzip");
			sos = response.getOutputStream();
			bos = new BufferedOutputStream(sos);
			gos = new GZIPOutputStream(bos);
			byte[] content = gzipContent.getBytes();
			gos.write(content);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(gos != null){
					gos.flush();
					gos.close();
					gos = null;
				}
				
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
	}
	
	public static String createATagForGoItem(int serverId, int itemId, 
			String itemName, int itemGfx, int tradeType, int enchant, 
			int status, int attr, String sortColumn, int sortType, int page){
		StringBuilder sb = new StringBuilder(256);
		sb.append("<a href=\"JavaScript:goMJItemInfo(\'");
		sb.append(serverId);	// serverId
		sb.append("\', \'");
		sb.append(itemId);		// itemId
		sb.append("\', \'");
		sb.append(itemName);	// itemName
		sb.append("\', \'");
		sb.append(itemGfx);		// gfx
		sb.append("\', \'");
		sb.append(tradeType);	// trade type
		sb.append("\', \'");
		sb.append(enchant);		// enchant
		sb.append("\', \'");
		sb.append(status);		// status
		sb.append("\', \'");
		sb.append(attr);		// attr
		sb.append("\', \'");
		sb.append(sortColumn);	// sort column
		sb.append("\', \'");
		sb.append(sortType);	// sort type
		sb.append("\', \'");
		sb.append(page);		// page
		sb.append("\');\">");
		return sb.toString();
	}
	
	public static String createImgTag(int itemGfx){
		StringBuilder sb = new StringBuilder(64);
		sb.append("<img src=\"/img/powerbook/");
		sb.append(itemGfx);
		sb.append(".png\" ");
		sb.append("alt=\"\" class=\"thumb\" onerror=\"");
		sb.append("this.src=\'/img/noimg_item.gif\'");
		sb.append("\"/>");
		
		return sb.toString();
	}
	
	public static String createJsTag(String src){
		StringBuilder sb = new StringBuilder(32);
		sb.append("<SCRIPT type=\"text/JavaScript\"");
		if(src != null)
			sb.append(" ").append("src=\"").append(src).append("\"></SCRIPT>");
		else
			sb.append(">");
		return sb.toString();
	}
	
	public static String createLinkTag(String type, String src){
		StringBuilder sb = new StringBuilder(32);
		sb.append("<link rel=\"stylesheet\" type=\"");
		sb.append(type).append("\" ");
		sb.append("href=\"");
		sb.append(src).append("\" ");
		sb.append("media=\"screen\">");
		return sb.toString();
	}
	
	public static String getSortTypeToString(int type){
		if(type == 1)
			return "desc";
		return "asc";
	}
	
	public static String getSortTypeToDisplay(int type){
		if(type == 1)
			return "높은 가격 순";
		return "낮은 가격 순";
	}
	
	public static int paramParseInt(String s){
		if(s == null || s.equalsIgnoreCase(""))
			return -1;
		
		return Integer.parseInt(s);
	}
	
	public static String getJoinTypeToDisplay(int jointype){
		switch(jointype){
		case 2:
			return "암호 가입";
		case 1:
			return "가입 신청";
		case 0:
			return "즉시 가입";
		}
		
		return "";
	}
	
	public static String getBloodOrderToFieldName(int orderId){
		switch(orderId){
		case 0:
			return "clan_name";
			
		case 1:
			return "War_point";
			
		case 3:
			return "create_date";
			
		case 5:
			return "hashouse";
		}
		return "clan_name";
	}
	
	public static String getGenderToDisplay(int genderId){
		if(genderId == 1)
			return "true";
		return "false";
	}
	
	public static String getLfcGroupToDisplay(int type){
		if(type == 1)
			return "PvP";
		else if(type == 2)
			return "RvR";
		return "전체";
	}
	
	public static String getToDayKST(){
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("KST"));
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int date = cal.get(Calendar.DATE);
		return String.format("%04d-%02d-%02d", year, month, date);
	}
	
	public static String getClassToDisplay(int classId){
		switch(classId){
		case 1:
			return "기사";
		case 2:
			return "요정";
		case 3:
			return "마법사";
		case 4:
			return "다크엘프";
		case 5:
			return "용기사";
		case 6:
			return "환술사";
		case 7:
			return "전사";
		}
		return "군주";
	}
	
	public static String getEnchantToDisplay(int enchant){
		if(enchant < 0)
			return "전체";
		
		StringBuilder sb = new StringBuilder(16);
		return sb.append("+").append(enchant).toString();
	}
	
	public static String getTradeTypeToDisplay(int tradeType){
		if(tradeType == 1)
			return "구매";
		return "판매";
	}
	
	public static String getIdenToDisplay(int iden){
		switch(iden){
		case -1:
			return "전체";
		case 1:
			return "미확인";
		case 2:
			return "일반";
		case 3:
			return "축복";
		case 4:
			return "저주";
		}
		return "";
	}
	
	private static final String[] _attrToDisplay = new String[]{
		"",
		"화령:1단 ", "화령:2단 ", "화령:3단 ", "화령:4단 ", "화령:5단 ",
		"수령:1단 ", "수령:2단 ", "수령:3단 ", "수령:4단 ", "수령:5단 ",
		"풍령:1단 ", "풍령:2단 ", "풍령:3단 ", "풍령:4단 ", "풍령:5단 ",
		"지령:1단 ", "지령:2단 ", "지령:3단 ", "지령:4단 ", "지령:5단 ",
	};
	
	public static String getAttrToDisplay(int attr){
		if(attr < 0 || attr >= _attrToDisplay.length)
			return "";
		return _attrToDisplay[attr];
	}
	
	private static final DecimalFormat _decF = new DecimalFormat("#,###");
	
	public static String getPriceFormat(long price) {
		if (price <= 0L) {
			return "0";
		}
		long mi = price / 10000L;
		long lo = price % 10000L;
		
		long hi = price / 100000000L;
		long mip = (price % 100000000L - lo) / 10000L;
		String result = null;
		if (hi > 0L) {
			if (mip > 0L && lo == 0) {
				result = String.format("%s억%s만", new Object[] { _decF.format(hi), _decF.format(mip) });
			} else if (mip > 0L && lo != 0) {
				result = String.format("%s억%s만%s", new Object[] { _decF.format(hi), _decF.format(mip), _decF.format(lo) });
			} else {
				result = String.format("%s억", new Object[] { _decF.format(hi) });
			}
		} else if (mi > 0L) {
			if (lo > 0L) {
				result = String.format("%s만%s", new Object[] { _decF.format(mi), _decF.format(lo) });
			} else {
				result = String.format("%s만", new Object[] { _decF.format(mi) });
			} 
		} else {
			result = String.format("%s", new Object[] { Long.valueOf(lo) });
		}
		return result;
	}
	
	/*private static final DecimalFormat _decF = new DecimalFormat("#,###");
	public static String getPriceFormat(long price){
		if(price <= 0)
			return "0";
		
		long hi 		= price / 10000;
		long lo 		= price % 10000;
		String result 	= null;
		if(hi > 0){
			if(lo > 0)
				result = String.format("%s만%s", _decF.format(hi), _decF.format(lo));
			else
				result = String.format("%s만", _decF.format(hi));
		}else
			result = String.format("%s", lo);
		
		return result;
	}*/
}