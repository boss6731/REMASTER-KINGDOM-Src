package l1j.server.MJWebServer.Dispatcher.my.service.item.shop;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.templates.L1Item;

public class MJMyShopStatisticsGmModel {
	StatisticsGmModel adena;
	StatisticsGmModel weapon;
	StatisticsGmModel armor;
	StatisticsGmModel etc;
	MJMyShopStatisticsGmModel(){
		adena = new StatisticsGmModel(true);
		weapon = new StatisticsGmModel(false);
		armor = new StatisticsGmModel(false);
		etc = new StatisticsGmModel(false);
	}
	
	void onAppendData(ResultSet rs) throws SQLException{
		int itemId = rs.getInt("item_id");
		StatisticsGmModel model = selectModel(itemId);
		model.onData(rs);
	}
	
	private StatisticsGmModel selectModel(int itemId){
		if(itemId == L1ItemId.ADENA){
			return adena;
		}
		L1Item item = ItemTable.getInstance().getTemplate(itemId);
		if(item == null){
			return etc;
		}
		switch(item.getType2()){
		case 1:
			return weapon;
		case 2:
			return armor;
		}
		return etc;
	}
	
	public static class StatisticsGmModel{
		int numOfTrade;		// �Ǽ�
		long fee;			// ������ �հ�
		int highPriceUnit;	// ������ �ְ� �ŷ���
		int lowPriceUnit;	// ������ ���� �ŷ���
		long ncoin;			// �� ncoin ����(������ �� ������)
		long unit;			// ���� ���� �հ�
		boolean adena;
		StatisticsGmModel(boolean adena){
			this.adena = adena;
		}
		
		private void onData(ResultSet rs) throws SQLException{
			++numOfTrade;
			long price = rs.getLong("price");
			long pricePerUnit =  rs.getLong("pricePerUnit");
			double commission = rs.getDouble("commission");
			unit += (price / pricePerUnit);
			fee += (price * commission);
			ncoin += (price - fee);			
			if(highPriceUnit == 0){
				highPriceUnit = (int)pricePerUnit;
			}else{
				highPriceUnit = Math.max(highPriceUnit, (int)pricePerUnit);
			}
			if(lowPriceUnit == 0){
				lowPriceUnit = (int)pricePerUnit;
			}else{
				lowPriceUnit = Math.min(lowPriceUnit, (int)pricePerUnit);
			}
		}
	}
	
}
