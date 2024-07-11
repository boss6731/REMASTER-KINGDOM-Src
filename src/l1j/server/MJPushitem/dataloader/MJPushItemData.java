package l1j.server.MJPushitem.dataloader;

import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.MJPushitem.model.MJItemPushModel;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;

public class MJPushItemData {
	private static HashMap<Integer, MJItemPushModel> Push_Item_List;
	public static void do_load(){
		HashMap<Integer, MJItemPushModel> Push_Item_list = new HashMap<Integer, MJItemPushModel>();
		Selector.exec("select * from push_item_list", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					MJItemPushModel pushlist = MJItemPushModel.readToDatabase(rs);
					Push_Item_list.put(pushlist.getpushid(), pushlist);
				}
			}
		});
		Push_Item_List = Push_Item_list;
	}
	
	public static void close() {
		Push_Item_List.clear();
	}
	
	public static HashMap<Integer, MJItemPushModel> getlist(){
		return Push_Item_List;
	}
}
