package l1j.server.MJExpAmpSystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.model.Instance.L1ItemInstance;

public class MJItemExpBonus {
	private static HashMap<Integer, ArrayList<MJItemExpBonus>> m_exp_bonuses;
	public static void do_load(){
		HashMap<Integer, ArrayList<MJItemExpBonus>> exp_bonuses = new HashMap<Integer, ArrayList<MJItemExpBonus>>();
		Selector.exec("select * from item_exp_bonus", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					MJItemExpBonus o = newInstance(rs);
					ArrayList<MJItemExpBonus> bonuses = exp_bonuses.get(o.get_item_id());
					if(bonuses == null) {
						bonuses = new ArrayList<MJItemExpBonus>();
						exp_bonuses.put(o.get_item_id(), bonuses);
					}
					bonuses.add(o);
				}
			}
		});
		m_exp_bonuses = exp_bonuses;
	}
	
	public static double get_bonus_exp(L1ItemInstance item) {
		ArrayList<MJItemExpBonus> bonuses = m_exp_bonuses.get(item.getItemId());
		if(bonuses == null)
			return 0D;
		
		int enchant = item.getEnchantLevel();
		for(MJItemExpBonus bonus : bonuses) {
			if(bonus.get_enchant() != enchant)
				continue;
			
			if(bonus.get_is_equipped()) {
				if (!item.isEquipped())
					continue;
			}
			
			return bonus.get_exp_bonus();
		}
		return 0;
	}

	private static MJItemExpBonus newInstance(ResultSet rs) throws SQLException{
		return newInstance()
				.set_item_id(rs.getInt("item_id"))
				.set_enchant(rs.getInt("enchant"))
				.set_exp_bonus(rs.getDouble("exp_bonus"))
				.set_is_equipped(rs.getInt("is_equipped") != 0);
	}

	private static MJItemExpBonus newInstance(){
		return new MJItemExpBonus();
	}

	private int m_item_id;
	private int m_enchant;
	private double m_exp_bonus;
	private boolean m_is_equipped;
	private MJItemExpBonus(){}

	public MJItemExpBonus set_item_id(int item_id){
		m_item_id = item_id;
		return this;
	}
	public MJItemExpBonus set_enchant(int enchant){
		m_enchant = enchant;
		return this;
	}
	public MJItemExpBonus set_exp_bonus(double exp_bonus){
		m_exp_bonus = exp_bonus;
		return this;
	}
	public MJItemExpBonus set_is_equipped(boolean is_equipped){
		m_is_equipped = is_equipped;
		return this;
	}
	public int get_item_id(){
		return m_item_id;
	}
	public int get_enchant(){
		return m_enchant;
	}
	public double get_exp_bonus(){
		return m_exp_bonus;
	}
	public boolean get_is_equipped(){
		return m_is_equipped;
	}
}

