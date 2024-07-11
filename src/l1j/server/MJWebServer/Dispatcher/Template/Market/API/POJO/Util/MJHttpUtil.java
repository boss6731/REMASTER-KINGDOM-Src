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
			_sbItemSearchBox.append("<img src=\"/img/item.gif\" alt=\"�ü�\" />");
			_sbItemSearchBox.append("</a>");
			_sbItemSearchBox.append("</h1>");
			_sbItemSearchBox.append("<div class=\"wrap_search\">");
			_sbItemSearchBox.append("<input type=\"text\" value=\"�˻�� �Է��� �ּ���\" ");
			_sbItemSearchBox.append("onfocus=\"if(this.value == \'�˻�� �Է��� �ּ���\'){this.value=\'\'; document.getElementById(\'searchFocusFlag\').value=\'Y\'}\"");
			_sbItemSearchBox.append("onblur=\"if(this.value == \'\'){this.value=\'�˻�� �Է��� �ּ���\'; document.getElementById(\'searchFocusFlag\').value=\'N\'}\"");
			_sbItemSearchBox.append("id=\"searchKeyword\" name=\"searchKeyword\" class=\"suggest\" onkeypress=\"if(window.event.keyCode==13){javascript:Board.goSearchItem();}\" maxlength=\"12\" />");
			_sbItemSearchBox.append("<input id=\"searchType\" type=\"hidden\" value=\"0\">");
			_sbItemSearchBox.append("<a href=\"javascript:Board.goSearchItem()\"><input type=\"image\" value=\"�˻�\" src=\"/img/ic_search.gif\" id=\"sitem_submit\" onclick=\"javascript:Board.goSearchItem()\" /></a>");
			_sbItemSearchBox.append("</div>");
			_sbItemSearchBox.append("</div>");
			_sbItemSearchBox.append("<script type=\"text/javascript\">");
			_sbItemSearchBox.append("//<![CDATA[\r\n");
			_sbItemSearchBox.append("$j(\'#sitem\').focus(function() {");
			_sbItemSearchBox.append("if($j(this).val() == \"�˻�� �Է��� �ּ���\") {$j(this).val(\'\').css(\'borderColor\', \'#ab6f12\');}");
			_sbItemSearchBox.append("$j(this).css(\'borderColor\', \'#ab6f12\');");
			_sbItemSearchBox.append("}).blur(function() {");
			_sbItemSearchBox.append("if(!$j(this).val()) {$j(this).val(\"�˻�� �Է��� �ּ���\");}");
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
			return "���� ���� ��";
		return "���� ���� ��";
	}
	
	public static int paramParseInt(String s){
		if(s == null || s.equalsIgnoreCase(""))
			return -1;
		
		return Integer.parseInt(s);
	}
	
	public static String getJoinTypeToDisplay(int jointype){
		switch(jointype){
		case 2:
			return "��ȣ ����";
		case 1:
			return "���� ��û";
		case 0:
			return "��� ����";
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
		return "��ü";
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
			return "���";
		case 2:
			return "����";
		case 3:
			return "������";
		case 4:
			return "��ũ����";
		case 5:
			return "����";
		case 6:
			return "ȯ����";
		case 7:
			return "����";
		}
		return "����";
	}
	
	public static String getEnchantToDisplay(int enchant){
		if(enchant < 0)
			return "��ü";
		
		StringBuilder sb = new StringBuilder(16);
		return sb.append("+").append(enchant).toString();
	}
	
	public static String getTradeTypeToDisplay(int tradeType){
		if(tradeType == 1)
			return "����";
		return "�Ǹ�";
	}
	
	public static String getIdenToDisplay(int iden){
		switch(iden){
		case -1:
			return "��ü";
		case 1:
			return "��Ȯ��";
		case 2:
			return "�Ϲ�";
		case 3:
			return "�ູ";
		case 4:
			return "����";
		}
		return "";
	}
	
	private static final String[] _attrToDisplay = new String[]{
		"",
		"ȭ��:1�� ", "ȭ��:2�� ", "ȭ��:3�� ", "ȭ��:4�� ", "ȭ��:5�� ",
		"����:1�� ", "����:2�� ", "����:3�� ", "����:4�� ", "����:5�� ",
		"ǳ��:1�� ", "ǳ��:2�� ", "ǳ��:3�� ", "ǳ��:4�� ", "ǳ��:5�� ",
		"����:1�� ", "����:2�� ", "����:3�� ", "����:4�� ", "����:5�� ",
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
				result = String.format("%s��%s��", new Object[] { _decF.format(hi), _decF.format(mip) });
			} else if (mip > 0L && lo != 0) {
				result = String.format("%s��%s��%s", new Object[] { _decF.format(hi), _decF.format(mip), _decF.format(lo) });
			} else {
				result = String.format("%s��", new Object[] { _decF.format(hi) });
			}
		} else if (mi > 0L) {
			if (lo > 0L) {
				result = String.format("%s��%s", new Object[] { _decF.format(mi), _decF.format(lo) });
			} else {
				result = String.format("%s��", new Object[] { _decF.format(mi) });
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
				result = String.format("%s��%s", _decF.format(hi), _decF.format(lo));
			else
				result = String.format("%s��", _decF.format(hi));
		}else
			result = String.format("%s", lo);
		
		return result;
	}*/
}