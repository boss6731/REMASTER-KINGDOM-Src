package l1j.server.MJDeathPenalty.Item;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.LinkedList;

import l1j.server.CPMWBQSystem.info.CPMWBQinfo;
import l1j.server.MJDeathPenalty.MJDeathPenaltyProvider;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJDeathPenaltyItemDatabaseLoader {
	
	private static MJDeathPenaltyItemDatabaseLoader _instance;
	
	public static MJDeathPenaltyItemDatabaseLoader getInstance(){
		if(_instance == null)
			_instance = new MJDeathPenaltyItemDatabaseLoader();
		return _instance;
	}
	
	public void do_Select(L1PcInstance pc){
		pc.attribute().getNotExistsNew(L1PcInstance.deathpenaltyitemModelKey).set(new LinkedList<MJDeathPenaltyItemModel>());
		Selector.exec("select * from characters_deathpenalty_item where owner_id=? order by Delete_time desc limit 20", new SelectorHandler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, pc.getId());
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					MJDeathPenaltyItemModel info = MJDeathPenaltyItemModel.readToDatabase(rs);
					pc.attribute().getNotExistsNew(L1PcInstance.deathpenaltyitemModelKey).get().add(info);
				}
				MJDeathPenaltyProvider.provider().senditeminfo(pc);
			}
		});
	}
	
	public void update(final int ownerId, L1ItemInstance item){
		try{
			Updator.exec(String.format("insert into %s set itemobjid=?, item_id=?, owner_id=?, item_name=?, count=?, is_equipped=?, enchantlvl=?"
					+ ", is_id=?, durability=?, charge_count=?, remaining_time=?, last_used=?, bless=?, attr_enchantlvl=?, special_enchant=?"
					+ ", end_time=?, package=?, item_level=?, Hotel_Town=?, dollpoten=?, delete_time=? on duplicate key update"
					+ " item_id=?, owner_id=?, item_name=?, count=?, is_equipped=?, enchantlvl=?"
					+ ", is_id=?, durability=?, charge_count=?, remaining_time=?, last_used=?, bless=?, attr_enchantlvl=?, special_enchant=?"
					+ ", end_time=?, package=?, item_level=?, Hotel_Town=?, dollpoten=?, delete_time=?", "characters_deathpenalty_item"), new Handler() {
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					int idx = 0;
					pstm.setInt(++idx, item.getId());
					pstm.setInt(++idx, item.getItemId());
					pstm.setInt(++idx, ownerId);
					pstm.setString(++idx, item.getName());
					pstm.setInt(++idx, item.getCount());
					pstm.setInt(++idx, 0);
					pstm.setInt(++idx, item.getEnchantLevel());
					pstm.setInt(++idx, item.isIdentified() ? 1 : 0);
					pstm.setInt(++idx, item.get_durability());
					pstm.setInt(++idx, item.getChargeCount());
					pstm.setInt(++idx, item.getRemainingTime());
					pstm.setTimestamp(++idx, item.getLastUsed());
					pstm.setInt(++idx, item.getBless());
					pstm.setInt(++idx, item.getAttrEnchantLevel());
					pstm.setInt(++idx, item.getSpecialEnchant());
					pstm.setTimestamp(++idx, item.getEndTime());
					pstm.setInt(++idx, item.isPackage() ? 1 : 0);
					pstm.setInt(++idx, item.get_item_level());
					pstm.setString(++idx, item.getHotel_Town());
					pstm.setInt(++idx, item.get_Doll_Bonus_Value());
					pstm.setTimestamp(++idx, new Timestamp(System.currentTimeMillis() + 3600 * 24 * 1000));
					pstm.setInt(++idx, item.getItemId());
					pstm.setInt(++idx, ownerId);
					pstm.setString(++idx, item.getName());
					pstm.setInt(++idx, item.getCount());
					pstm.setInt(++idx, 0);
					pstm.setInt(++idx, item.getEnchantLevel());
					pstm.setInt(++idx, item.isIdentified() ? 1 : 0);
					pstm.setInt(++idx, item.get_durability());
					pstm.setInt(++idx, item.getChargeCount());
					pstm.setInt(++idx, item.getRemainingTime());
					pstm.setTimestamp(++idx, item.getLastUsed());
					pstm.setInt(++idx, item.getBless());
					pstm.setInt(++idx, item.getAttrEnchantLevel());
					pstm.setInt(++idx, item.getSpecialEnchant());
					pstm.setTimestamp(++idx, item.getEndTime());
					pstm.setInt(++idx, item.isPackage() ? 1 : 0);
					pstm.setInt(++idx, item.get_item_level());
					pstm.setString(++idx, item.getHotel_Town());
					pstm.setInt(++idx, item.get_Doll_Bonus_Value());
					pstm.setTimestamp(++idx, new Timestamp(System.currentTimeMillis() + 3600 * 24 * 1000));
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		};
	}
	
	public void Delete(L1PcInstance pc, int itemobj, int itemid){
		Updator.exec("delete from cpmw_bookquest_userinfo where itemobjid=? and item_id=? and owner_id=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, itemobj);
				pstm.setInt(++idx, itemid);
				pstm.setInt(++idx, pc.getId());
			}
		});
	}
}
