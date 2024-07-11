package l1j.server.MJTemplate.Chain.Etc;

import l1j.server.Config;
import l1j.server.FatigueProperty;
import l1j.server.MJTemplate.Chain.MJAbstractActionChain;
import l1j.server.server.Account;
import l1j.server.server.datatables.SpecialMapTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJAdenaPickupChain extends MJAbstractActionChain<MJIAdenaPickupHandler>{
	public static void setup_blessing_effect(){
		getInstance().add_handler(new BlessingEffectHandler());
	}
	
	private static MJAdenaPickupChain _instance;
	public static MJAdenaPickupChain getInstance(){
		if(_instance == null)
			_instance = new MJAdenaPickupChain();
		return _instance;
	}
	
	private MJAdenaPickupChain(){
		super();
	}
	
	public int pickup(L1PcInstance owner, L1ItemInstance item, int amount){
		int destination_amount = amount;
		for(MJIAdenaPickupHandler handler : m_handlers){
			destination_amount = handler.do_pickup(owner, item, amount);				
			if(destination_amount <= 0)
				destination_amount = 1;
		}
		return destination_amount;
	}
	
	// 一張提供高品質護仙效果的地圖
	public static boolean is_blessing_effect_map(int mapId) {
		return (mapId == 4);
	}
	
	// 不提供獎勵效果的地圖
	public static boolean none_blessing_effect_map(int mapId) {
		return (mapId == 4);
	}
	
	private static class BlessingEffectHandler implements MJIAdenaPickupHandler {
		@Override
		public int do_pickup(L1PcInstance owner, L1ItemInstance item, int amount) {
			double bonus 	= 1;
			double minus 	= 1;
			double doll_bonus = 1;
			
			// if (is_blessing_effect_map(owner.getMapId())) {
			if (owner.getInventory().checkItem(4100121)) {
				bonus += Config.ServerAdSetting.Blessing;
			} else if (owner.getInventory().checkItem(4100529) && owner.getLevel() <= Config.ServerAdSetting.NEWPLAYERLEVELPROTECTION) {
				bonus += Config.ServerAdSetting.Blessing;
			}
			// }
			
			if (owner.getAdenBonus() > 0)
				doll_bonus += owner.getAdenBonus();

			if (FatigueProperty.getInstance().use_fatigue()) {
				Account account = owner.getAccount();
				if (account != null && owner.getAI() == null && account.has_fatigue()) {
					minus = FatigueProperty.getInstance().get_fatigue_effect_adena();
				}
			}
			
			return is_blessing_effect(owner, item) ? (int) (amount * bonus * minus * doll_bonus) : amount;
		}
		
		private static boolean is_blessing_effect(L1PcInstance pc, L1ItemInstance item){
			if(item.isGiveItem())
				return false;
			
			/*if (none_blessing_effect_map(pc.getMapId()))
				return false;*/
			
			/*if (is_blessing_effect_map(pc.getMapId()))
				return true;*/
			
			if (pc.getAdenBonus() > 0 || SpecialMapTable.getInstance().isSpecialMap(pc.getMapId()) || pc.getInventory().checkItem(4100121) || pc.getInventory().checkItem(4100529)) {
				return true;
			}

			return false;
		}
	}

}
