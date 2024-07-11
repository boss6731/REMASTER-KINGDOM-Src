package l1j.server.MJRaidSystem.Util;

import java.util.Random;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.poison.L1DamagePoison;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.templates.L1Npc;

@SuppressWarnings("serial")
public class L1MJRaidPoisonInstance extends L1NpcInstance{
	private static final int	INTERVAL	= 0x3e8;
	private static Random 		_rnd = new Random(System.nanoTime());
	private L1MonsterInstance 	_owner;
	public L1MJRaidPoisonInstance(L1Npc template){
		super(template);
	}
	
	public void setOwner(L1MonsterInstance owner){
		_owner = owner;
	}
	
	public L1MonsterInstance getOwner(){
		return _owner;
	}
	
	@Override
	public void deleteMe() {
		_destroyed = true;
		if (getInventory() != null) {
			getInventory().clearItems();
		}
		allTargetClear();
		_master = null;
		this.setCubePc(null);
		L1World.getInstance().removeVisibleObject(this);
		L1World.getInstance().removeObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			if (pc == null) continue;
			pc.removeKnownObject(this);
			pc.sendPackets(new S_RemoveObject(this));
		}
		removeAllKnownObjects();
	}
	
	class poisonTickTimer implements Runnable{
		private L1MJRaidPoisonInstance _poison;
		public poisonTickTimer(L1MJRaidPoisonInstance poison){
			_poison = poison;
		}
		@Override 
		public void run(){
			if(_destroyed)
				return;
			
			try{	
				for(L1PcInstance pc : L1World.getInstance().getAllPlayers()){
					if(pc == null || getMapId() != pc.getMapId() || getX() != pc.getX() || getY() != pc.getY() || pc.isDead()  || pc.isGhost() || pc.isGm())
						continue;
					
					int dmg = _rnd.nextInt(150) + 50;
					L1DamagePoison.doInfection(getOwner() == null ? _poison : getOwner(), pc, 3000, dmg, false);
				}
				GeneralThreadPool.getInstance().schedule(this, INTERVAL);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
