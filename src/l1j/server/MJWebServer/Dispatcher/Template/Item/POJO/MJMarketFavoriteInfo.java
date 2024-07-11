package l1j.server.MJWebServer.Dispatcher.Template.Item.POJO;

import java.util.ArrayList;

public class MJMarketFavoriteInfo {
	public MJNameCodeInfo[] itemStatusList;
	public ArrayList<MJNameCodeInfo> result;
	public MJMarketFavoriteInfo(){
		itemStatusList = MJNameCodeInfo.STATUS.ITEM_STATUS_LIST;
		result = new ArrayList<MJNameCodeInfo>();
	}
}
