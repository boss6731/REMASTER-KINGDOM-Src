package l1j.server.MJTemplate.Regen;

import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1EffectInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;

public class MJReGenerator {
	static class BuffBonusInfo{
		int skill_id;
		int bonus;
		public BuffBonusInfo(int skill_id, int bonus){
			this.skill_id = skill_id;
			this.bonus = bonus;
		}
	}
	
	private static final long LATEST_ACTION_DELAY_MILLIS = 5000L;
	
	public static MJReGenerator newInstance(L1PcInstance pc) {
		MJReGenerator generator = new MJReGenerator();
		generator._is_stopped = false;
		generator.update_latest_action(MJRegeneratorLatestActions.LATEST_ACTION_STANDING);
		generator._hp_regenerator = MJHpRegenerator.newInstance(pc, generator);
		generator._mp_regenerator = MJMpRegenerator.newInstance(pc, generator);
		return generator.do_continue();
	}
	
	private MJHpRegenerator _hp_regenerator;
	private MJMpRegenerator _mp_regenerator;
	private MJRegeneratorLatestActions _latest_action;
	private long _latest_action_update_millis;
	private boolean _is_stopped;
	private MJReGenerator() {
		_latest_action = MJRegeneratorLatestActions.LATEST_ACTION_STANDING;
	}
	
	public MJRegeneratorLatestActions get_latest_action() {
		return System.currentTimeMillis() >= _latest_action_update_millis + LATEST_ACTION_DELAY_MILLIS ? 
				MJRegeneratorLatestActions.LATEST_ACTION_STANDING : 
					_latest_action;
	}
	
	public void update_latest_action(MJRegeneratorLatestActions latest_action) {
		_latest_action_update_millis = System.currentTimeMillis();
		_latest_action = latest_action;
	}
	
	public boolean get_is_stopped() {
		return _is_stopped;
	}
	
	public void set_is_stopped(boolean is_stopped) {
		_is_stopped = is_stopped;
	}
	
	public void dispose() {
		_hp_regenerator.dipose();
		_mp_regenerator.dipose();
	}
	
	private MJReGenerator do_continue() {
		_hp_regenerator.do_continue();
		_mp_regenerator.do_continue();
		return this;
	}
	
	static boolean is_under_water(L1PcInstance pc) {
		if(!pc.getMap().isUnderwater())
			return false;
		
		if(pc.hasSkillEffect(L1SkillId.STATUS_UNDERWATER_BREATH))
			return false;

		L1PcInventory inv = pc.getInventory();
		if(inv.checkEquipped(20207))
			return false;
		
		if (inv.checkEquipped(21048) && inv.checkEquipped(21049) && inv.checkEquipped(21050)) {
			return false;
		}
		return true;
	}
	
	static boolean is_over_weight(L1PcInstance pc) {
		if (pc.hasSkillEffect(L1SkillId.EXOTIC_VITALIZE) || 
				pc.hasSkillEffect(L1SkillId.ADDITIONAL_FIRE) || 
				pc.hasSkillEffect(L1SkillId.SCALES_WATER_DRAGON)) {
			return false;
		}
		
		if(pc.is_top_ranker() || is_inn(pc))
			return false;
		
		return (50 <= pc.getInventory().getWeight100());
	}
	
	static boolean is_inn(L1PcInstance pc) {
		int mapId = pc.getMapId();
		return (mapId == 16384 || mapId == 16896 || mapId == 17408 || mapId == 17492 || mapId == 17820
				|| mapId == 17920 || mapId == 18432 || mapId == 18944 || mapId == 19456 || mapId == 19968
				|| mapId == 20480 || mapId == 20992 || mapId == 21504 || mapId == 22016 || mapId == 22528
				|| mapId == 23040 || mapId == 23552 || mapId == 24064 || mapId == 24576 || mapId == 25088);	
	}
	
	static boolean is_in_lifestream(L1PcInstance pc) {
		L1Location loc = pc.getLocation();
		for (L1Object object : pc.getKnownObjects()) {
			if (!(object instanceof L1EffectInstance)) {
				continue;
			}
			
			L1EffectInstance effect = (L1EffectInstance) object;
			if (effect.getNpcId() == 81169 && effect.getLocation().getTileLineDistance(loc) < 4) {
				return true;
			}
		}
		return false;
	}
}
