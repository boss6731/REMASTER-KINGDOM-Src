package l1j.server.MJBotSystem.Business;

import java.util.ArrayList;
import java.util.Random;

import l1j.server.MJBotSystem.MJBotBrain;
import l1j.server.MJBotSystem.MJBotClanInfo;
import l1j.server.MJBotSystem.MJBotLocation;
import l1j.server.MJBotSystem.MJBotStatus;
import l1j.server.MJBotSystem.MJBotType;
import l1j.server.MJBotSystem.AI.MJBotAI;
import l1j.server.MJBotSystem.AI.MJBotIllusionAI;
import l1j.server.MJBotSystem.AI.MJBotMovableAI;
import l1j.server.MJBotSystem.AI.MJBotProtectorAI;
import l1j.server.MJBotSystem.AI.MJBotRedKnightAI;
import l1j.server.MJBotSystem.AI.MJBotSiegeLeaderAI;
import l1j.server.MJBotSystem.Loader.MJBotBrainLoader;
import l1j.server.MJBotSystem.Loader.MJBotLoadManager;
import l1j.server.MJBotSystem.Pool.MJBotFishAIPool;
import l1j.server.MJBotSystem.Pool.MJBotHuntAIPool;
import l1j.server.MJBotSystem.Pool.MJBotScarecrowAIPool;
import l1j.server.MJBotSystem.Pool.MJBotWanderAIPool;
import l1j.server.MJBotSystem.util.MJBotUtil;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.MJWarSystem.MJCastleWar;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.utils.MJCommons;

/**********************************
 *
 * MJ ai scheduler.
 * made by mjsoft, 2016.
 *
 **********************************/

public class MJAIScheduler {
	private static MJAIScheduler _instance;
	public static MJAIScheduler getInstance(){
		if(_instance == null)
			_instance = new MJAIScheduler();
		return _instance;
	}

	public static void release(){
		if(_instance != null){
			_instance.clear();
			_instance = null;
		}
	}

	public static void reload(){
		MJAIScheduler tmp = _instance;
		_instance = new MJAIScheduler();
		if(tmp != null){
			tmp.clear();
			tmp = null;
		}
	}

	private MJAIBusiness _huntB;
	private MJAIBusiness _scB;
	private MJAIBusiness _wanderB;
	private MJAIBusiness _siegeB;
	private MJAIBusiness _rkB;
	private MJAIBusiness _ptB;
	private MJAIScheduler(){
		_huntB 			= new MJAIHuntBusiness(MJBotLoadManager.MBO_HUNTAI_POOL_SIZE);
		_scB			= new MJAIScarecrowBusiness(MJBotLoadManager.MBO_SCARECROWAI_POOL_SIZE);
		_wanderB		= new MJAIWanderBusiness(MJBotLoadManager.MBO_WANDERAI_POOL_SIZE);
		_siegeB			= new MJAISiegeLeaderBusiness(8);
		_rkB			= new MJAIRedKnightBusiness(512);
		_ptB			= new MJAIProtectorBusiness(4);
	}

	public synchronized void removeSchedule(MJBotAI ai){
		if(ai == null)
			return;

		if(ai.getBotType() == MJBotType.HUNT){
			_huntB.removeWork((MJBotMovableAI)ai);
		}else if(ai.getBotType() == MJBotType.SCARECROW)
			_scB.removeWork((MJBotMovableAI)ai);
		else if(ai.getBotType() == MJBotType.FISH)
			MJBotFishAIPool.getInstance().push(ai);
		else if(ai.getBotType() == MJBotType.WANDER)
			_wanderB.removeWork((MJBotMovableAI)ai);
		else if(ai.getBotType() == MJBotType.ILLUSION){
			L1PcInstance pc = ai.getBody();
			ai.dispose();
			if(pc != null){
				L1Clan clan = pc.getClan();
				if(clan != null){
					clan.removeClanMember(pc.getName());
				}
				MJBotUtil.deleteBot(pc);
			}
		}
		else if(ai.getBotType() == MJBotType.SIEGELEADER)
			_siegeB.removeWork((MJBotMovableAI)ai);
		else if(ai.getBotType() == MJBotType.REDKNIGHT){
			_rkB.removeWork((MJBotMovableAI)ai);
		}else if(ai.getBotType() == MJBotType.PROTECTOR)
			_ptB.removeWork((MJBotMovableAI)ai);
	}

	// 設定釣魚計劃
	public synchronized MJBotLastError setFishSchedule(MJBotBrain brn) {
		MJBotLastError result = new MJBotLastError();
		try {
			// 創建釣魚角色
			L1PcInstance body = brn.createFishBody();
			if (body == null) {
				// 設定有效的機器人名稱已經耗盡
				result.message = "設定有效的機器人名稱已經耗盡。";
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public synchronized MJBotLastError setSiegeLeaderSchedule(MJBotClanInfo cInfo){
		MJBotLastError result    = new MJBotLastError();
		try{
			ArrayList<MJBotBrain> list = MJBotBrainLoader.getInstance().getClassBrains(0);
			if(list == null || list.size() <= 0){
				result.message = "找不到王族機器人.";
				return result;
			}
			return result;

			MJBotAI			ai 		= new MJBotSiegeLeaderAI();
			MJBotBrain brn;
			L1PcInstance 	body 	= brn.createCrownBody(cInfo);
			body.setX(cInfo.leaderX);
			body.setY(cInfo.leaderY);
			body.setHeading(4);
			body.addDmgup(MJBotLoadManager.MBO_ADDDMG_HUNT);
			body.addBowDmgup(MJBotLoadManager.MBO_ADDDMG_HUNT);
			body.addHitup(MJBotLoadManager.MBO_ADDHIT_HUNT);
			body.addBowHitup(MJBotLoadManager.MBO_ADDHIT_HUNT);
			body.addDamageReduction(MJBotLoadManager.MBO_ADDRDT_HUNT);
			body.addMaxHp(MJBotLoadManager.MBO_ADDHP_HUNT);
			body.setCurrentHp(body.getMaxHp());
			body.getAbility().addSp(MJBotLoadManager.MBO_ADDSP_HUNT);
			ai.setRemoved(false);
			ai.setBrain(brn);
			ai.setBody(body);
			ai.setStatus(MJBotStatus.SETTING);
			ai.setBotType(MJBotType.SIEGELEADER);
			body.setAI(ai);
			cInfo.leaderAI = ai;
			L1World.getInstance().storeObject(body);
			L1World.getInstance().addVisibleObject(body);
			_siegeB.addWork((MJBotMovableAI)ai);
			result.ai = ai;
			MJBotType.SIEGELEADER.add(ai);
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	class RedKnightOffenseProcessor implements Runnable{
		private L1Clan 	_clan;
		private int		_castleId;
		private boolean _isFirst;

		RedKnightOffenseProcessor(L1Clan clan, int castleId){
			_clan 		= clan;
			_castleId 	= castleId;
			_isFirst	= true;
		}

		@Override
		public void run() {
			try{
				if(!MJCastleWarBusiness.getInstance().isNowWar(_castleId))
					return null;

				int h = 0;
				if(_castleId == 1)
					h = 2;

				ArrayList<MJBotMovableAI> list 	= new ArrayList<MJBotMovableAI>(100);
				ArrayList<MJBotMovableAI> tmp	= null;

				tmp = mergeRedKnightBody(_clan, _castleId, MJBotLoadManager.MBO_LEVEL_RK, h, 4, 1, false, 200);
				if(tmp != null){
					list.addAll(tmp);
					tmp.clear();
				}

				tmp = mergeRedKnightBody(_clan, _castleId, MJBotLoadManager.MBO_LEVEL_RK, h, 4, 2, false, 200);
				if(tmp != null){
					list.addAll(tmp);
					tmp.clear();
				}
				tmp = mergeRedKnightBody(_clan, _castleId, MJBotLoadManager.MBO_LEVEL_RK, h, 4, 5, false, 200);
				if(tmp != null){
					list.addAll(tmp);
					tmp.clear();
				}

				for(MJBotMovableAI ai : list){
					L1PcInstance p = ai.getBody();
					if(p != null){
						p.removeSkillEffect(L1SkillId.ABSOLUTE_BARRIER);
						p.setMoveSpeed(1);
						p.setBraveSpeed(1);
						_rkB.addWork(ai);
					}
				}
				if(_isFirst){
					_isFirst = false;
					setProtectorSchedule(_clan, _castleId);
				}

				if(MJCastleWarBusiness.getInstance().isNowWar(_castleId))
					GeneralThreadPool.getInstance().schedule(this, MJBotLoadManager.MBO_REGEN_RK);
				else{
					for(L1PcInstance pc : L1World.getInstance().getAllPlayers()){
						if(pc == null || pc.getAI() == null)
							continue;

						if(pc.getAI().getBotType() == MJBotType.REDKNIGHT)
							removeSchedule(pc.getAI());
					}
					MJBotType.REDKNIGHT.dispose();
				}
				list.clear();

			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
	}

	public void setRKSchedule(L1Clan clan, int castleId, boolean isDefender){
		try{
			if(isDefender){
				int h = 6;
				if(castleId == 4 || castleId == 2)
					h = 4;

				mergeRedKnightBody(clan, castleId, MJBotLoadManager.MBO_LEVEL_RK, h, 4, 1, isDefender);
				mergeRedKnightBody(clan, castleId, MJBotLoadManager.MBO_LEVEL_RK, h, 4, 2, isDefender);
				mergeRedKnightBody(clan, castleId, MJBotLoadManager.MBO_LEVEL_RK, h, 4, 3, isDefender);
				mergeRedKnightBody(clan, castleId, MJBotLoadManager.MBO_LEVEL_RK, h, 4, 5, isDefender);
			}else{
				GeneralThreadPool.getInstance().execute(new RedKnightOffenseProcessor(clan, castleId));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	// 合併紅色騎士身體
	private ArrayList<MJBotMovableAI> mergeRedKnightBody(L1Clan clan, int castleId, int level, int h, int mid, int classType, boolean isDefender, long delay) throws Exception {
		// 獲取指定職業類型的機器人列表
		ArrayList<MJBotBrain> list = MJBotBrainLoader.getInstance().getClassBrains(classType);
		if (list == null || list.size() <= 0) {
			System.out.println("無效的機器人信息。");
			return null;
		}

		// 初始化隨機數生成器
		Random rnd = new Random(System.nanoTime());
		// 隨機選取一個機器人
		MJBotBrain brn = list.get(rnd.nextInt(list.size()));
		// 獲取紅色騎士的位置坐標
		Integer[] poses = MJBotUtil.getRKPoses(castleId, classType, isDefender);
		if (poses == null || poses.length <= 0) {
			System.out.println("無效的(1) 類型 1 / 坐標: " + poses);
			return null;
		}

		// 世界攻城戰變更添加
		if (castleId == L1CastleLocation.GIRAN_CASTLE_ID) {
			mid = 15482;
		}

		ArrayList<MJBotMovableAI> ais = new ArrayList<MJBotMovableAI>(poses.length / 2);
		int count = 0;
		for(int i=0; i<poses.length; i+=2){
			if(!MJCastleWarBusiness.getInstance().isNowWar(castleId))
				return ais;

			MJCastleWar war = MJCastleWarBusiness.getInstance().get(castleId);
			if(war.getDefenseClan().getClanName().equalsIgnoreCase("붉은 기사단"))
				return ais;

			++count;
			if(classType == 1 ){
				if(count > 10)
					break;
			}else{
				if(count > 5)
					break;
			}

			L1PcInstance 		body 	= brn.createRKBody(clan, level, poses[i], poses[i + 1], h, mid);
			body.addDmgup(MJBotLoadManager.MBO_ADDDMG_RK);
			body.addBowDmgup(MJBotLoadManager.MBO_ADDDMG_RK);
			body.addHitup(MJBotLoadManager.MBO_ADDHIT_RK);
			body.addBowHitup(MJBotLoadManager.MBO_ADDHIT_RK);
			body.addDamageReduction(MJBotLoadManager.MBO_ADDRDT_RK);
			body.addMaxHp(MJBotLoadManager.MBO_ADDHP_RK);
			body.setCurrentHp(body.getMaxHp());
			body.getAbility().addSp(MJBotLoadManager.MBO_ADDSP_RK);

			MJBotRedKnightAI	ai		= new MJBotRedKnightAI();
			ai.setRemoved(false);
			ai.setBrain(brn);
			ai.setBody(body);
			ai.setDefender(isDefender);
			ai.setWarCastle(castleId);
			ai.equipped();
			ai.setStatus(MJBotStatus.SETTING);
			ai.setBotType(MJBotType.REDKNIGHT);
			body.getMap().setPassable(body.getLocation(), false);
			body.setAI(ai);
			L1World.getInstance().storeObject(body);
			L1World.getInstance().addVisibleObject(body);
			if(isDefender)	body.setWorldObject(SC_WORLD_PUT_OBJECT_NOTI.redknight_make_stream(body));
			//body.setWorldObject(S_WorldPutObject.getRedKnight(body));
			body.broadcastPacket(new S_EffectLocation(body.getX(), body.getY(), 2236));
			MJBotType.REDKNIGHT.add(ai);
			ais.add(ai);
			body.setSkillEffect(L1SkillId.ABSOLUTE_BARRIER, 50000);
			Thread.sleep(delay);
		}
		return ais;
	}

	private void mergeRedKnightBody(L1Clan clan, int castleId, int level, int h, int mid, int classType, boolean isDefender) throws Exception{
		ArrayList<MJBotBrain> list = MJBotBrainLoader.getInstance().getClassBrains(classType);
		if(list == null || list.size() <= 0){
			System.out.println("無效的大腦訊息（列表）：" + list);
			return;
		}

		// 初始化隨機數生成器
		Random rnd = new Random(System.nanoTime());
		// 隨機選取一個機器人
		MJBotBrain brn = list.get(rnd.nextInt(list.size()));
		// 獲取紅色騎士的位置坐標
		Integer[] poses = MJBotUtil.getRKPoses(castleId, classType, isDefender);
		if (poses == null || poses.length <= 0) {
			System.out.println("無效的(2) 類型 1 / 坐標: " + poses);
			return;
		}

		// 世界攻城戰變更添加
		if (castleId == L1CastleLocation.GIRAN_CASTLE_ID) {
			mid = 15482;
		}

		for(int i=0; i<poses.length; i+=2){
			L1PcInstance 		body 	= brn.createRKBody(clan, level, poses[i], poses[i + 1], h, mid);
			body.addDmgup(MJBotLoadManager.MBO_ADDDMG_RK);
			body.addBowDmgup(MJBotLoadManager.MBO_ADDDMG_RK);
			body.addHitup(MJBotLoadManager.MBO_ADDHIT_RK);
			body.addBowHitup(MJBotLoadManager.MBO_ADDHIT_RK);
			body.addDamageReduction(MJBotLoadManager.MBO_ADDRDT_RK);
			body.addMaxHp(MJBotLoadManager.MBO_ADDHP_RK);
			body.setCurrentHp(body.getMaxHp());
			body.getAbility().addSp(MJBotLoadManager.MBO_ADDSP_RK);

			MJBotRedKnightAI	ai		= new MJBotRedKnightAI();
			ai.setRemoved(false);
			ai.setBrain(brn);
			ai.setBody(body);
			ai.setDefender(isDefender);
			ai.equipped();
			ai.setStatus(MJBotStatus.SETTING);
			ai.setBotType(MJBotType.REDKNIGHT);
			body.getMap().setPassable(body.getLocation(), false);
			body.setAI(ai);
			L1World.getInstance().storeObject(body);
			L1World.getInstance().addVisibleObject(body);
			if(isDefender)	body.setWorldObject(SC_WORLD_PUT_OBJECT_NOTI.redknight_make_stream(body));
			//body.setWorldObject(S_WorldPutObject.getRedKnight(body));
			_rkB.addWork(ai);
			MJBotType.REDKNIGHT.add(ai);
		}
	}

	public void setProtectorSchedule(L1Clan clan, int castleId){
		try{
			int h = 0;
			if(castleId == 1)
				h = 2;

			mergeProtectorBody(clan, castleId, MJBotLoadManager.MBO_LEVEL_PT, h, 4, 0);
			mergeProtectorBody(clan, castleId, MJBotLoadManager.MBO_LEVEL_PT, h, 4, MJBotUtil.createProtectorId());

			/*mergeProtectorBody(clan, castleId, 80, h, 4, 0);
			mergeProtectorBody(clan, castleId, 80, h, 4, 1);
			mergeProtectorBody(clan, castleId, 80, h, 4, 2);
			mergeProtectorBody(clan, castleId, 80, h, 4, 3);
			mergeProtectorBody(clan, castleId, 80, h, 4, 4);
			mergeProtectorBody(clan, castleId, 80, h, 4, 5);
			mergeProtectorBody(clan, castleId, 80, h, 4, 6);*/
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	// 合併保護者身體
	private void mergeProtectorBody(L1Clan clan, int castleId, int level, int h, int mid, int pid) throws Exception {
		// 將保護者ID轉換為職業類型
		int classType = MJBotUtil.protectorIdToClassType(pid);
		// 獲取指定職業類型的機器人列表
		ArrayList<MJBotBrain> list = MJBotBrainLoader.getInstance().getClassBrains(classType);
		if (list == null || list.size() <= 0) {
			System.out.println("無效的機器人信息。");
			return;
		}

		Random 				rnd		= new Random(System.nanoTime());
		MJBotBrain			brn		= list.get(rnd.nextInt(list.size()));
		MJBotLocation 		loc		= MJBotUtil.createCastleLocation(castleId);
		L1PcInstance 		body 	= brn.createProtectorBody(clan, level, loc.x, loc.y, h, mid, pid);
		MJBotProtectorAI	ai		= new MJBotProtectorAI(pid);
		body.setMoveSpeed(1);
		body.setBraveSpeed(1);
		body.addDmgup(MJBotLoadManager.MBO_ADDDMG_PT);
		body.addBowDmgup(MJBotLoadManager.MBO_ADDDMG_PT);
		body.addHitup(MJBotLoadManager.MBO_ADDHIT_PT);
		body.addHitup(MJBotLoadManager.MBO_ADDHIT_PT);
		body.addDamageReduction(MJBotLoadManager.MBO_ADDRDT_PT);
		body.addMaxHp(MJBotLoadManager.MBO_ADDHP_PT);
		body.setCurrentHp(body.getMaxHp());
		body.getAbility().addSp(MJBotLoadManager.MBO_ADDSP_PT);
		ai.setRemoved(false);
		ai.setBrain(brn);
		ai.setBody(body);
		ai.equipped();
		body.setAttackRang(body.getAttackRang() + 1);
		ai.setWarCastle(castleId);
		ai.setStatus(MJBotStatus.SETTING);
		ai.setBotType(MJBotType.PROTECTOR);
		body.getMap().setPassable(body.getLocation(), false);
		body.setAI(ai);
		L1World.getInstance().storeObject(body);
		L1World.getInstance().addVisibleObject(body);
		_ptB.addWork(ai);
		MJBotType.PROTECTOR.add(ai);
	}

	// 設置計劃
	public synchronized MJBotLastError setSchedule(int bid, MJBotType type, int level, int x, int y, int mid, boolean is_fixed_location) {
		MJBotLastError result = new MJBotLastError();
		try {
			// 獲取指定ID的機器人
			MJBotBrain brn = MJBotBrainLoader.getInstance().get(bid);
			if (brn == null) {
				result.message = String.format("未指定機器人ID。 [%d]", bid);
				return result;
			}

			MJBotAI            ai         = null;
			L1PcInstance       body       = null;
			if(type == MJBotType.FISH)
				body = brn.createFishBody();
			else if(type == MJBotType.ILLUSION)
				body = is_fixed_location ? brn.createIllusionBodyFixedLocation(x, y, mid) : brn.createIllusionBody(x, y, mid);
			else
				body = is_fixed_location ? brn.createBodyFixedLocation(level, x, y, mid) : brn.createBody(level, x, y, mid);

			if(body == null){
				result.message = "所有可配置的機器人名稱都已耗盡。";
				return result;
			}

			if(type == MJBotType.HUNT)
				ai = MJBotHuntAIPool.getInstance().pop();
			else if(type == MJBotType.SCARECROW)
				ai = MJBotScarecrowAIPool.getInstance().pop();
			else if(type == MJBotType.FISH)
				ai = MJBotFishAIPool.getInstance().pop();
			else if(type == MJBotType.WANDER)
				ai = MJBotWanderAIPool.getInstance().pop();
			else if(type == MJBotType.ILLUSION){
				ai = new MJBotIllusionAI();
				body.getMap().setPassable(body.getLocation(), false);
			}
			if (ai == null) {
				result.message = String.format("[%s] 無法找到AI。", type.name());
				return result;
			}

			ai.setRemoved(false);
			ai.setBrain(brn);
			ai.setBody(body);
			ai.equipped();
			ai.setStatus(MJBotStatus.SETTING);
			ai.setBotType(type);
			body.setAI(ai);
			L1World.getInstance().storeObject(body);
			L1World.getInstance().addVisibleObject(body);
			if(ai.getBotType() == MJBotType.HUNT){
				body.addDmgup(MJBotLoadManager.MBO_ADDDMG_HUNT);
				body.addHitup(MJBotLoadManager.MBO_ADDHIT_HUNT);
				body.addDamageReduction(MJBotLoadManager.MBO_ADDRDT_HUNT);
				body.addMaxHp(MJBotLoadManager.MBO_ADDHP_HUNT);
				body.setCurrentHp(body.getMaxHp());
				body.getAbility().addSp(MJBotLoadManager.MBO_ADDSP_HUNT);
				_huntB.addWork((MJBotMovableAI)ai);
			}else if(ai.getBotType() == MJBotType.SCARECROW)
				_scB.addWork((MJBotMovableAI)ai);
			else if(ai.getBotType() == MJBotType.WANDER) {
				_wanderB.addWork((MJBotMovableAI)ai);
			}
			result.ai = ai;
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	public void clear(){
		if(_huntB != null){
			_huntB.stop();
			_huntB = null;
		}

		if(_scB != null){
			_scB.stop();
			_scB = null;
		}

		if(_wanderB != null){
			_wanderB.stop();
			_wanderB = null;
		}

		if(_rkB != null){
			_rkB.stop();
			_rkB = null;
		}

		if(_ptB != null){
			_ptB.stop();
			_ptB = null;
		}
	}
}
