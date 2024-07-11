package l1j.server.MJBotSystem.Business;

import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJBotSystem.MJBotLocation;
import l1j.server.MJBotSystem.MJBotStatus;
import l1j.server.MJBotSystem.AI.MJBotMovableAI;
import l1j.server.MJBotSystem.AI.MJBotRedKnightAI;
import l1j.server.MJBotSystem.util.MJBotUtil;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.MJCommons;

public class MJAIRedKnightBusiness extends MJAIBusiness{

	public MJAIRedKnightBusiness(int capacity) {
		super(capacity);
	}
	
	protected void toOffenseWork(MJBotMovableAI ai, long time){
		L1PcInstance 	body 		= ai.getBody();
		MJBotStatus 	status 		= ai.getStatus();
		
		if(ai.getStatus() != MJBotStatus.DEAD && body.isDead())
			ai.setStatus(MJBotStatus.DEAD);
		
		status = ai.getStatus();
		int castleId = ai.getWarCastle();
		if(status != MJBotStatus.SETTING){
			if(!MJBotUtil.isInCastle(body, castleId) && !MJBotUtil.isInCastleStartup(body, castleId)){
				ai.setStatus(MJBotStatus.WALK);
				MJBotLocation loc = MJBotUtil.createCastleLocation(castleId);
				ai.teleport(loc.x, loc.y, (short)loc.map);
				ai.setActSleepMs(time + ai.getBrain().getDecisionTime());
				return;
			}else if(castleId == 4){
				if(MJCommons.isInArea(body, 33612, 32735, 33619, 32726, 15482) || MJCommons.isInArea(body, 33645, 32726, 33651, 32737, 15482)){
					ai.setStatus(MJBotStatus.WALK);
					MJBotLocation loc = MJBotUtil.createCastleLocation(castleId);
					ai.teleport(loc.x, loc.y, (short)loc.map);
					ai.setActSleepMs(time + ai.getBrain().getDecisionTime());
					return;
				}
			}
		}
		
		long interval 	= 0;
		if(status == MJBotStatus.SETTING){
			ai.settingOffense(time);
			interval = ai.getBrain().getDecisionTime();
		}else if(status == MJBotStatus.WALK){
			ai.walkOffense(time);
			interval = body.getCurrentSpriteInterval(EActionCodes.walk);
		}else if(status == MJBotStatus.ATTACK){
			ai.attackOffense(time);
			if(ai.getLastStatus() == MJBotStatus.ATTACK)
				interval = body.getCurrentSpriteInterval(EActionCodes.attack);
			else
				interval = body.getCurrentSpriteInterval(EActionCodes.walk);
		}else if(status == MJBotStatus.DEAD){
			ai.death();
			interval = body.getCurrentSpriteInterval(EActionCodes.death);
		}else{
			ai.setStatus(MJBotStatus.WALK);
			interval = ai.getBrain().getDecisionTime();
		}
		ai.setActSleepMs(time + interval);
	}
	
	protected void toDefenseWork(MJBotMovableAI ai, long time){
		MJBotStatus status 	= ai.getStatus();	
		if(status != MJBotStatus.ATTACK){
			ai.settingDefense(time);
			ai.setActSleepMs(time + ai.getBrain().getDecisionTime());
		}else{
			L1PcInstance body = ai.getBody();
			ai.attackDefense(time);
			ai.setActSleepMs(time + body.getCurrentSpriteInterval(EActionCodes.attack));
		}
	}

	@Override
	protected void toWork(MJBotMovableAI ai, long time){
		L1PcInstance body 	= ai.getBody();
		
		if(MJCommons.isLock(body)){
			long interval = ai.getBrain().getDecisionTime();
			ai.setActSleepMs(time + interval);
			super.toWork(ai, time);
			return;
		}
		
		if(body.isDead()){
			long 			interval = body.getCurrentSpriteInterval(EActionCodes.death);
			ai.setActSleepMs(time + interval);
			ai.death();
			super.toWork(ai, time);
			return;
		}
		
		if(((MJBotRedKnightAI)ai).isDefender())
			toDefenseWork(ai, time);
		else
			toOffenseWork(ai, time);
		super.toWork(ai, time);
	}
}
