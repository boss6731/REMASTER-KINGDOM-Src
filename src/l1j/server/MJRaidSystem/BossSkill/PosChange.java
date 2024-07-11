package l1j.server.MJRaidSystem.BossSkill;

import java.util.ArrayList;

import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_OnlyEffect;

/** 更改使用者位置. **/
public class PosChange extends MJRaidBossSkill{
	@Override
	public String toString(){
		return PosChange.class.getName();
	}
	
	@Override
	public void action(L1NpcInstance own, ArrayList<L1PcInstance> pcs) {
		if(own == null || pcs == null || pcs.size() <= 0 || own.isParalyzed())
			return;
	
		L1PcInstance 	pc		= null;
		int 			size 	= pcs.size();
		try{
			synchronized(own.synchObject){
				own.setParalyzed(true);
				if(_actid >= 0)
					Broadcaster.broadcastPacket(own, new S_DoActionGFX(own.getId(), _actid));
				
				if((_type & BSRTYPE_ONCE) > 0){
					pc = pcs.get(_rnd.nextInt(size));
					if(isSkillSet(own, pc))
						processAction(own, pc);
				}else{
					for(int i=0; i<size; i++){
						pc = pcs.get(i);
						if(pc == null || pc.isDead() || !isRange(own, pc) || _rnd.nextInt(100) > _ratio)
							continue;
							
						processAction(own, pc);	
					}
				}
				sleepAction(own, _actid);
			}
		}catch(Exception e){
			System.out.println("職位變更：" +pcs.size());
			e.printStackTrace();
		}finally{
			own.setParalyzed(false);			
		}
	}
	
	@Override
	public void actionNoDelay(L1NpcInstance own, ArrayList<L1PcInstance> pcs) {
		if(own == null || pcs == null || pcs.size() <= 0)
			return;
		
		L1PcInstance 	pc		= null;
		int 			size 	= pcs.size();
		try{
			if(_actid >= 0)
				Broadcaster.broadcastPacket(own, new S_DoActionGFX(own.getId(), _actid));
			
			if((_type & BSRTYPE_ONCE) > 0){
				pc = pcs.get(_rnd.nextInt(size));
				if(isSkillSet(own, pc))
					processAction(own, pc);
			}else{
				for(int i=0; i<size; i++){
					pc = pcs.get(i);
					if(!isSkillSet(own, pc))
						continue;
					processAction(own, pc);	
				}
			}
			sleepAction(own, _actid);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void processAction(L1NpcInstance own, L1PcInstance pc){
		if(_effectId > 0){
			S_OnlyEffect oe = new S_OnlyEffect(pc.getId(), _effectId);
			pc.sendPackets(oe, false);
			Broadcaster.broadcastPacket(pc, oe);
		}
		
		int div = _range / 2;
		int x 	= _rnd.nextInt(div);
		int y	= _rnd.nextInt(div);
		if(_rnd.nextBoolean())
			x *= -1;
		if(_rnd.nextBoolean())
			y *= -1;

		pc.start_teleport(own.getX() + x, own.getY() + y, pc.getMapId(), pc.getHeading(), 18339, true, false);
	}
}
