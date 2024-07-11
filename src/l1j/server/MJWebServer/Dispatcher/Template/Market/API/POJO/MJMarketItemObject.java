package l1j.server.MJWebServer.Dispatcher.Template.Market.API.POJO;

public class MJMarketItemObject {
	public int		itemId;
	public String	cname;
	public String 	real_name_id_view;
	public int 		iden;
	public int		price;
	public int		icon_id;
	public int		attr;
	public int		type;
	public int		enchant;
	public int		count;
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder(256);
		sb.append("itemId : ").append(itemId).append("\r\n");
		sb.append("cname : ").append(cname).append("\r\n");
		sb.append("name : ").append(real_name_id_view).append("\r\n");
		return sb.toString();
	}
}
