package l1j.server.MJBotSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import l1j.server.Config;
import l1j.server.MJBotSystem.Business.MJAIScheduler;
import l1j.server.MJBotSystem.Business.MJBotLastError;
import l1j.server.MJBotSystem.Loader.MJBotClanInfoLoader;
import l1j.server.MJBotSystem.Loader.MJBotDollLoader;
import l1j.server.MJBotSystem.Loader.MJBotFishInfoLoader;
import l1j.server.MJBotSystem.Loader.MJBotLoadManager;
import l1j.server.MJBotSystem.Loader.MJBotNameLoader;
import l1j.server.MJBotSystem.util.MJBotUtil;
import l1j.server.MJKDASystem.MJKDALoader;
import l1j.server.MJRankSystem.Loader.MJRankUserLoader;
import l1j.server.server.ActionCodes;
import l1j.server.server.IdFactory;
import l1j.server.server.clientpackets.C_CreateChar;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Clan.ClanMember;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_Fishing;
import l1j.server.server.serverpackets.S_SummonPack;
import l1j.server.server.utils.MJCommons;

/**********************************
 * 
 * MJ Bot Brain.
 * made by mjsoft, 2016.
 *  
 **********************************/
public class MJBotBrain {
	private static Random 		_rnd = new Random(System.nanoTime());
	
	private static final HashMap<Integer, Integer[]> 	_polyMaps;
	private static final int[] 							_polyIdxs;
	static {
		_polyIdxs = new int[128];
		setIndexMap(0, 69, 0);
		setIndexMap(70, 74, 1);
		setIndexMap(75, 79, 2);
		setIndexMap(80, 81, 3);
		setIndexMap(82, 83, 4);
		setIndexMap(84, 127, 5);
		///TODO로봇 변신
		_polyMaps = new HashMap<Integer, Integer[]>();
		//   1~69,  70~74, 75~79, 80~81,82~83, 84~127	
		_polyMaps.put(4, new Integer[]	{11392, 15850, 12702, 12681, 13153, 13152});	// sword
//		_polyMaps.put(4, new Integer[]	{13152, 13152, 13152, 12681, 13153, 13152});	// sword
		
		_polyMaps.put(11, new Integer[]	{11392, 15850, 12702, 12681, 13153, 13152});	// blunt
//		_polyMaps.put(11, new Integer[]	{13152, 13152, 12702, 12681, 13153, 13152});	// blunt
		
		_polyMaps.put(20, new Integer[]	{11394, 15848, 13346, 15814, 13631, 13635});	// bow
//		_polyMaps.put(20, new Integer[]	{13635, 13635, 13346, 15814, 13631, 13635});	// bow
		
		_polyMaps.put(24, new Integer[]	{11447, 15599, 15834, 15534, 15537, 15539});	// chainsword
//		_polyMaps.put(24, new Integer[]	{15539, 15539, 15834, 15534, 15537, 15539});	// chainsword
		
		_polyMaps.put(40, new Integer[]	{11395, 15849, 15865, 15545, 15548, 15550});	// staff
//		_polyMaps.put(40, new Integer[]	{15550, 15550, 15865, 15545, 15548, 15550});	// staff
		
		_polyMaps.put(46, new Integer[]	{11392, 15850, 12702, 12681, 13153, 13152});	// dagger
//		_polyMaps.put(46, new Integer[]	{13152, 13152, 12702, 12681, 13153, 13152});	// dagger
		
		_polyMaps.put(50, new Integer[]	{11392, 15850, 12702, 12681, 13153, 13152});	// towhandsword
//		_polyMaps.put(50, new Integer[]	{13152, 13152, 12702, 12681, 13153, 13152});	// towhandsword
		
		_polyMaps.put(54, new Integer[]	{6158, 15847, 11381, 15866, 11389, 15868});		// edoryu
//		_polyMaps.put(54, new Integer[]	{15868, 15868, 11381, 15866, 11389, 15868});		// edoryu
		
		_polyMaps.put(58, new Integer[]	{11392, 15850, 12702, 12681, 13153, 13152});	// claw / keylink
//		_polyMaps.put(58, new Integer[]	{13152, 13152, 12702, 12681, 13153, 13152});	// claw / keylink
		
		
		/*_polyMaps.put(4, new Integer[]	{ 13152 });	// sword
		_polyMaps.put(11, new Integer[]	{ 13152 });	// blunt
		_polyMaps.put(20, new Integer[]	{ 13635 });	// bow
		_polyMaps.put(24, new Integer[]	{ 15833 });	// chainsword
		_polyMaps.put(40, new Integer[]	{ 15550 });	// staff
		_polyMaps.put(46, new Integer[]	{ 13152 });	// dagger
		_polyMaps.put(50, new Integer[]	{ 13152 });	// towhandsword
		_polyMaps.put(54, new Integer[]	{ 15868 });		// edoryu
		_polyMaps.put(58, new Integer[]	{ 13152 });	// claw / keylink
*/	}	
	private static void setIndexMap(int start, int end, int val) {
		for (int i = start; i <= end; i++)
			_polyIdxs[i] = val;
	}

	private int 					_bid;				// bid
	private int					_age;				// age
	private int					_hormon;			// hormon
	private int					_pride;				// pride
	private int					_sense;				// sense
	private int					_decisionTime;		// decisionTime
	private int 					_weaponId;			// weaponId
	private int 					_classType;			// classType
	private int					_settingCutLine;
	private int					_potionCutLine;
	private int					_searchCount;
	private int					_pickupCount;
	private int					_moveCount;
	private int					_deadCount;
	private int					_sightRange;
	private int					_addBuffDice;
	private int					_addSkillDice;
	private ArrayList<Integer> 	_armorIds;			// armorIds
	
	public MJBotBrain(){
		_settingCutLine = -1;
		_potionCutLine 	= -1;
		_searchCount	= -1;
		_pickupCount	= -1;
		_moveCount		= -1;
		_deadCount		= -1;
		_addBuffDice	= -1;
		_addSkillDice	= -1;
		_sightRange		= -1;
	}
	
	/** brain id **/
	public int getBrainId(){
		return _bid;
	}
	public void setBrainId(int i){
		_bid = i;
	}

    /** 年齡 **/
    public void setAge(int i) {
        _age = i;
        if (_age > MJBotLoadManager.MBO_BRAIN_MAX_FIGURE)
            _age = MJBotLoadManager.MBO_BRAIN_MAX_FIGURE;
        else if (_age < MJBotLoadManager.MBO_BRAIN_MIN_FIGURE)
            _age = MJBotLoadManager.MBO_BRAIN_MIN_FIGURE;

        _decisionTime = _age * MJBotLoadManager.MBO_DPS;
    }
	
	public int getAge(){
		return _age;
	}
	public int getDecisionTime(){
		return _decisionTime;
	}
	
	public int getHormon(){
		return _hormon;
	}
	public void setHormon(int i){
		_hormon = i;
		if(_hormon > MJBotLoadManager.MBO_BRAIN_MAX_FIGURE)
			_hormon = MJBotLoadManager.MBO_BRAIN_MAX_FIGURE;
		else if(_hormon < MJBotLoadManager.MBO_BRAIN_MIN_FIGURE)
			_hormon = MJBotLoadManager.MBO_BRAIN_MIN_FIGURE;
	}
	public int getPride(){
		return _pride;
	}
	public void setPride(int i){
		_pride = i;
		if(_pride > MJBotLoadManager.MBO_BRAIN_MAX_FIGURE)
			_pride = MJBotLoadManager.MBO_BRAIN_MAX_FIGURE;
		else if(_pride < MJBotLoadManager.MBO_BRAIN_MIN_FIGURE)
			_pride = MJBotLoadManager.MBO_BRAIN_MIN_FIGURE;
	}
	public int getSense(){
		return _sense;
	}
	public void setSense(int i){
		_sense = i;
		if(_sense > MJBotLoadManager.MBO_BRAIN_MAX_FIGURE)
			_sense = MJBotLoadManager.MBO_BRAIN_MAX_FIGURE;
		else if(_sense < MJBotLoadManager.MBO_BRAIN_MIN_FIGURE)
			_sense = MJBotLoadManager.MBO_BRAIN_MIN_FIGURE;
	}
	
	/** itemInfo. **/
	public int getWeaponId(){
		return _weaponId;
	}
	public void setWeaponId(int i){
		_weaponId = i;
	}
	
	public ArrayList<Integer> getArmorIds(){
		return _armorIds;
	}
	public void setArmorIds(ArrayList<Integer> armorIds){
		_armorIds = armorIds;
	}
	
	public int toRand(int i){
		if(i <= 0)
			return 0;
		return _rnd.nextInt(i);
	}
	
	public boolean toBoolean(){
		return _rnd.nextBoolean();
	}
	
	public int getClassType(){
		return _classType;
	}
	public void setClassType(int i){
		_classType = i;
	}
	
	public int getPotionCutLine(){
		if(_potionCutLine == -1){
			_potionCutLine = 100 - getPride() + (getSense() / 10);
			if(_potionCutLine > 90)
				_potionCutLine = 90;
			else if(_potionCutLine < 50)
				_potionCutLine = 50;
		}
		return _potionCutLine;
	}
	
	public int getSettingCutLine(){
		if(_settingCutLine == -1){
			_settingCutLine = 25 - (getPride() - getSense());
			if(_settingCutLine > 30)
				_settingCutLine = 30;
			else if(_settingCutLine < 20)
				_settingCutLine = 20;
		}
		return _settingCutLine;
	}
	
	public int getSearchCount(){
		if(_searchCount == -1){
			_searchCount = 10 - (getHormon() / 10);
			if(_searchCount > MJBotLoadManager.MBO_SEARCH_MAX_COUNT)
				_searchCount = MJBotLoadManager.MBO_SEARCH_MAX_COUNT;
			else if(_searchCount < MJBotLoadManager.MBO_SEARCH_MIN_COUNT)
				_searchCount = MJBotLoadManager.MBO_SEARCH_MIN_COUNT;
		}
		return _searchCount;
	}
	
	public int getPickupCount(){
		if(_pickupCount == -1){
			_pickupCount = 10 - (getPride() / 10);
			if(_pickupCount > MJBotLoadManager.MBO_SEARCH_MAX_COUNT)
				_pickupCount = MJBotLoadManager.MBO_SEARCH_MAX_COUNT;
			else if(_pickupCount < MJBotLoadManager.MBO_SEARCH_MIN_COUNT)
				_pickupCount = MJBotLoadManager.MBO_SEARCH_MIN_COUNT;
		}
		return _pickupCount;
	}
	
	public int getMoveCount(){
		if(_moveCount == -1){
			_moveCount = 10 - (getAge() / 10);
			if(_moveCount > MJBotLoadManager.MBO_SEARCH_MAX_COUNT)
				_moveCount = MJBotLoadManager.MBO_SEARCH_MAX_COUNT;
			else if(_moveCount < MJBotLoadManager.MBO_SEARCH_MIN_COUNT)
				_moveCount = MJBotLoadManager.MBO_SEARCH_MIN_COUNT;
		}
		return _moveCount;
	}
	
	public int getDeadCount(){
		if(_deadCount == -1){
			_deadCount = 10 - (getPride() / 10);
			if(_deadCount > MJBotLoadManager.MBO_SEARCH_MAX_COUNT)
				_deadCount = MJBotLoadManager.MBO_SEARCH_MAX_COUNT;
			else if(_deadCount < MJBotLoadManager.MBO_SEARCH_MIN_COUNT)
				_deadCount = MJBotLoadManager.MBO_SEARCH_MIN_COUNT;
		}
		return _deadCount;
	}
	
	public int getAddBuffDice(){
		if(_addBuffDice == -1)
			_addBuffDice = (getSense() / 10) * 2;
		return _addBuffDice;
	}
	
	public int getAddSkillDice(){
		if(_addSkillDice == -1)
			_addSkillDice = (getHormon() / 10) * 2;
		return _addSkillDice;
	}
	
	public int getSightRange(){
		if(_sightRange == -1){
			_sightRange = (getSense() / 10) + (getAge() / 10);
			if(_sightRange > 18)
				_sightRange = 18;
			else if(_sightRange < 8)
				_sightRange = 8;
		}
		return _sightRange;
	}
	
	public int getPolyId(L1PcInstance body){
		Integer[] arr = _polyMaps.get(body.getCurrentWeapon());
		if(arr == null || arr.length <= 0)
			return 11392;
		
		if(arr.length == 1)
			return arr[0];
		
		int idx = _polyIdxs[body.getLevel()];
		if(getPride() > 80)
			return arr[idx];
		return arr[_rnd.nextInt(idx+1)];
	}

	public L1PcInstance createIllusionBody(int x, int y, int mid) throws Exception {
		// 從 MJBotNameLoader 獲取一個 MJBotName 對象
		MJBotName bName = MJBotNameLoader.getInstance().get();
		if (bName == null || bName.name == null || bName.name.equals(""))
			return null;

		// 創建新的 L1PcInstance 對象
		L1PcInstance body = new L1PcInstance();
		body.setAccountName("MJBOT"); // 設置帳號名稱為 "MJBOT"
		body.setName(bName.name); // 設置名稱為 bName.name
		body.setClanname(""); // 設置血盟名稱為空字符串
		body.setId(IdFactory.getInstance().nextId()); // 設置 ID 為下一個生成的 ID
		body.setType(_classType); // 設置類型為 _classType
		body.set_sex(_hormon > 50 ? 0 : 1); // 根據 _hormon 設置性別，如果 _hormon 大於 50 則為男性 (0)，否則為女性 (1)
		body.setHighLevel(56); // 設置高等級為 56
		body.setLevel(56); // 設置等級為 56
		body.set_exp(0); // 設置經驗值為 0

		// 設置正義值
		if (Config.Login.StandbyServer)
			body.setLawful(0); // 如果是待機服務器，設置正義值為 0
		else {
			if (bName.lawful < 0)
				body.setLawful(_rnd.nextInt(32767)); // 如果 bName.lawful 小於 0，隨機設置正義值
			else
				body.setLawful(bName.lawful); // 否則設置正義值為 bName.lawful
		}

		// 這段代碼有剩餘的部分應該繼續處理，但以上部分已經翻譯並檢查完畢
		return body;
	}


		MJBotClanInfo cInfo = MJBotClanInfoLoader.getInstance().get(bName.cName);
		// 從 MJBotClanInfoLoader 獲取血盟信息

		if(cInfo != null){
			// 如果血盟信息不為空

			if(cInfo.leaderAI == null){
				// 如果血盟領導者的 AI 為空

				MJBotLastError result = MJAIScheduler.getInstance().setSiegeLeaderSchedule(cInfo);
				// 設置圍城領導者計劃

				if(result.ai == null)
					// 如果 AI 為空

					System.out.println("MJBotBrain:" +result.message);
				// 打印錯誤信息

			}

			if(cInfo.clanObject != null){
				// 如果血盟對象不為空

				body.setClanid(cInfo.clanObject.getClanId());
				// 設置血盟 ID

				body.setClanname(cInfo.clanName);
				// 設置血盟名稱

				body.setClanRank(L1Clan.普通成員);
				// 設置血盟等級為普通成員

				body.setClanMemberNotes(String.format(""));
				// 設置血盟成員備註

				ClanMember cm = new ClanMember(body.getName(), body.getClanRank(), body.getLevel(), body.getClanMemberNotes(), body.getId(), body.getType(), body.getOnlineStatus() == 1, body);
				// 創建新的血盟成員對象

				cInfo.clanObject.addClanMember(cm);
				// 將成員添加到血盟對象中

			}

		}
		body.setTitle(bName.title);
		body.setClanRank(0);
		body.setClanMemberNotes(String.format(""));
		MJBotLocation loc = null;
		int idx	=0;
		do{
			loc = MJBotUtil.createRandomLocation(x, y, mid);
			idx++;
		}while(idx < 20 || !MJCommons.isPassablePosition(loc));
		
		body.setX(loc.x);
		body.setY(loc.y);
		body.setMap((short)mid);
		body.setHeading(toRand(8));
		body.getMoveState().setHeading(body.getHeading());
		if (body.get_sex() == 0) 	body.setClassId(C_CreateChar.MALE_LIST[body.getType()]);
		else						body.setClassId(C_CreateChar.FEMALE_LIST[body.getType()]);
		body.setCurrentSprite(body.getClassId());
		body.noPlayerCK 	= true;
		body.noPlayerRobot 	= true;
		body.setNetConnection(null);
		CharacterTable.getInstance().storeNewCharacter(body);
		body.refresh();
		return body;
	}

	public L1PcInstance createIllusionBodyFixedLocation(int x, int y, int mid) throws Exception {
		// 從 MJBotNameLoader 獲取一個 MJBotName 對象
		MJBotName bName = MJBotNameLoader.getInstance().get();
		if (bName == null || bName.name == null || bName.name.equals(""))
			return null;

		// 創建新的 L1PcInstance 對象
		L1PcInstance body = new L1PcInstance();
		body.setAccountName("MJBOT"); // 設置帳號名稱為 "MJBOT"
		body.setName(bName.name); // 設置名稱為 bName.name
		body.setClanname(""); // 設置血盟名稱為空字符串
		body.setId(IdFactory.getInstance().nextId()); // 設置 ID 為下一個生成的 ID
		body.setType(_classType); // 設置類型為 _classType
		body.set_sex(_hormon > 50 ? 0 : 1); // 根據 _hormon 設置性別，如果 _hormon 大於 50 則為男性 (0)，否則為女性 (1)
		body.setHighLevel(56); // 設置高等級為 56
		body.setLevel(56); // 設置等級為 56
		body.set_exp(0); // 設置經驗值為 0

		// 設置正義值
		if (Config.Login.StandbyServer)
			body.setLawful(0); // 如果是待機服務器，設置正義值為 0
		else {
			if (bName.lawful < 0)
				body.setLawful(_rnd.nextInt(32767)); // 如果 bName.lawful 小於 0，隨機設置正義值
			else
				body.setLawful(bName.lawful); // 否則設置正義值為 bName.lawful
		}

		// 設置角色的位置及地圖 ID
		body.setX(x); // 設置 X 坐標
		body.setY(y); // 設置 Y 坐標
		body.setMap(mid); // 設置地圖 ID

		// 返回創建的角色對象
		return body;
	}

        MJBotClanInfo cInfo = MJBotClanInfoLoader.getInstance().get(bName.cName);

    if (cInfo != null) {

        if (cInfo.leaderAI == null) {
            MJBotLastError result = MJAIScheduler.getInstance().setSiegeLeaderSchedule(cInfo);
            if (result.ai == null) {
                System.out.println("MJBotBrain:" + result.message);
            }

        }

        if (cInfo.clanObject != null) {
            body.setClanid(cInfo.clanObject.getClanId());
            body.setClanname(cInfo.clanName);
            body.setClanRank(L1Clan.普通成員);
            body.setClanMemberNotes(String.format(""));
            ClanMember cm = new ClanMember(
                    body.getName(),
            body.getClanRank(),
            body.getLevel(),
            body.getClanMemberNotes(),
            body.getId(),
            body.getType(),
            body.getOnlineStatus() == 1,
            body
            );
            cInfo.clanObject.addClanMember(cm);

        }

    }

    	body.setTitle(bName.title);
    	body.setClanRank(0);
    	body.setClanMemberNotes(String.format(""));
    	body.setX(x);
    	body.setY(y);
    	body.setMap((short) mid);
    	body.setHeading(toRand(8));
    	body.getMoveState().setHeading(body.getHeading());

    	if (body.get_sex() == 0) {
    		body.setClassId(C_CreateChar.MALE_LIST[body.getType()]);
    	} else {
    		body.setClassId(C_CreateChar.FEMALE_LIST[body.getType()]);
    	}

    	body.setCurrentSprite(body.getClassId());
    	body.noPlayerCK = true;
    	body.noPlayerRobot = true;
    	body.setNetConnection(null);

    	CharacterTable.getInstance().storeNewCharacter(body);
    	body.refresh();

    	return body;

    	public L1PcInstance createFishBody() throws Exception{
    		MJBotName bName	= MJBotNameLoader.getInstance().get();
    		if(bName == null || bName.name == null || bName.name.equals(""))
    			return null;

    		MJBotFishInfo fInfo = MJBotFishInfoLoader.next();
    		if(fInfo == null)
    			return null;

    		L1PcInstance body 	= new L1PcInstance();
    		body.setAccountName("MJBOT");
    		body.setName(bName.name);
    		body.setClanname("");
    		body.setId(IdFactory.getInstance().nextId());
    		body.setType(_classType);
    		body.set_sex(_hormon > 50 ? 0 : 1);
    		body.setHighLevel(56);
    		body.setLevel(56);
    		body.set_exp(0);
    		if (Config.Login.StandbyServer)
    			body.setLawful(0);
    		else {
    			if (bName.lawful < 0)
    				body.setLawful(_rnd.nextInt(32767));
    			else
    				body.setLawful(bName.lawful);
    		}

    		MJBotClanInfo cInfo = MJBotClanInfoLoader.getInstance().get(bName.cName);
    		if(cInfo != null){
    			if(cInfo.leaderAI == null){
    				MJBotLastError result = MJAIScheduler.getInstance().setSiegeLeaderSchedule(cInfo);
    				if(result.ai == null)
    					System.out.println("MJBotBrain:" +result.message);
    			}
    			if(cInfo.clanObject != null){
    				body.setClanid(cInfo.clanObject.getClanId());
    				body.setClanname(cInfo.clanName);
    				body.setClanRank(L1Clan.普通成員);
    				body.setClanMemberNotes(String.format(""));
    				ClanMember cm = new ClanMember(body.getName(), body.getClanRank(), body.getLevel(), body.getClanMemberNotes(), body.getId(), body.getType(), body.getOnlineStatus() == 1, body);
    				cInfo.clanObject.addClanMember(cm);
    			}
    		}
    		body.setTitle(bName.title);
    		body.setClanRank(0);
    		body.setClanMemberNotes(String.format(""));
    		body.setX(fInfo.x);
    		body.setY(fInfo.y);
    		body.setMap((short)fInfo.mid);
    		body.setHeading(fInfo.h);
    		body.getMoveState().setHeading(fInfo.h);
    		if (body.get_sex() == 0) 	body.setClassId(C_CreateChar.MALE_LIST[body.getType()]);
    		else						body.setClassId(C_CreateChar.FEMALE_LIST[body.getType()]);
    		body.setCurrentSprite(body.getClassId());
    		body.noPlayerCK 	= true;
    		body.noPlayerRobot 	= true;
    		body.setFishing(true);
    		body._fishingX = fInfo.fx;
    		body._fishingY = fInfo.fy;
    		Broadcaster.broadcastPacket(body, new S_Fishing(body.getId(), ActionCodes.ACTION_Fishing, fInfo.fx, fInfo.fy));
    		body.setNetConnection(null);
    		CharacterTable.getInstance().storeNewCharacter(body);
    		body.refresh();
    		return body;
    	}

    	private static int _rkNum = 0;
    	public L1PcInstance createRKBody(L1Clan clan, int level, int x, int y, int h, int mid) throws Exception{
    		L1PcInstance body = new L1PcInstance();
    		body.setAccountName("MJBOT");
    		body.setName(String.format("RK%d", _rkNum++));
    		body.setClanname("");
    		body.setId(IdFactory.getInstance().nextId());
    		body.setType(_classType);
    		body.set_sex(_hormon > 50 ? 0 : 1);
    		body.setLevel(level);
    		body.setHighLevel(level);
    		body.set_exp(ExpTable.getExpByLevel(level));
    		MJBotUtil.calcStat(this, body);
    		body.setLawful(0);
    		body.setMap((short)mid);
    		body.setX(x);
    		body.setY(y);
    		body.setHeading(h);
    		body.getMoveState().setHeading(h);
    		body.setClanid(clan.getClanId());
    		body.setRedKnightClanId(clan.getClanId());
    		body.setClanname(clan.getClanName());
    		body.setClanRank(L1Clan.普通成員);
    		body.setClanMemberNotes("");
    		ClanMember cm = new ClanMember(body.getName(), body.getClanRank(), body.getLevel(), body.getClanMemberNotes(), body.getId(), body.getType(), body.getOnlineStatus() == 1, body);
    		clan.addClanMember(cm);
    		body.setTitle("");
    		if (body.get_sex() == 0) 	body.setClassId(C_CreateChar.MALE_LIST[body.getType()]);
    		else						body.setClassId(C_CreateChar.FEMALE_LIST[body.getType()]);
    		body.setCurrentSprite(MJBotUtil.getRedKnightGfx(body.getType()));
    		body.set_food(39);
    		body.setGm(false);
    		body.setMonitor(false);
    		body.setGmInvis(false);
    		body.setStatus(0);
    		body.setAccessLevel((short)0);
    		body.resetBaseMr();
    		body.setElfAttr(0);
    		body.set_PKcount(0);
    		body.set_exp_res(0);
    		body.setPartnerId(0);
    		body.setOnlineStatus(1);
    		body.setHomeTownId(0);
    		body.setContribution(0);
    		body.setBanned(false);
    		body.setKarma(0);
    		body.setReturnStat(0);
    		MJKDALoader.getInstance().install(body, true);
    		body.setNetConnection(null);
    		body.sendVisualEffectAtLogin();
    		body.setDead(false);
    		body.noPlayerCK 	= true;
    		body.noPlayerRobot 	= true;
    		body.setEmblemId(21);
    		body.setRedknightType(true);
    		CharacterTable.getInstance().storeNewCharacter(body);
    		body.refresh();
    		return body;
    	}

    	public L1PcInstance createProtectorBody(L1Clan clan, int level, int x, int y, int h, int mid, int pid) throws Exception{
    		L1PcInstance body = new L1PcInstance();
    		body.setAccountName("MJBOT");
    		body.setName(MJBotUtil.getProtectorName(pid));
    		body.setId(IdFactory.getInstance().nextId());
    		body.setClanname("");
    		body.setType(_classType);
    		body.set_sex(_hormon > 50 ? 0 : 1);
    		body.setLevel(level);
    		body.setHighLevel(level);
    		body.set_exp(ExpTable.getExpByLevel(level));
    		MJBotUtil.calcStat(this, body);
    		body.setLawful(0);
    		body.setMap((short)mid);
    		body.setX(x);
    		body.setY(y);
    		body.setHeading(h);
    		body.getMoveState().setHeading(h);
    		body.setClanid(clan.getClanId());
    		body.setRedKnightClanId(clan.getClanId());
    		body.setClanname(clan.getClanName());
    		body.setClanRank(L1Clan.普通成員);
    		body.setClanMemberNotes("");
    		ClanMember cm = new ClanMember(body.getName(), body.getClanRank(), body.getLevel(), body.getClanMemberNotes(), body.getId(), body.getType(), body.getOnlineStatus() == 1, body);
    		clan.addClanMember(cm);
    		body.setTitle("");
    		if (body.get_sex() == 0) 	body.setClassId(C_CreateChar.MALE_LIST[body.getType()]);
    		else						body.setClassId(C_CreateChar.FEMALE_LIST[body.getType()]);
    		body.setCurrentSprite(MJBotUtil.getProtectorGfx(pid));
    		body.set_food(39);
    		body.setGm(false);
    		body.setMonitor(false);
    		body.setGmInvis(false);
    		body.setStatus(0);
    		body.setAccessLevel((short)0);
    		body.resetBaseMr();
    		body.setElfAttr(0);
    		body.set_PKcount(0);
    		body.set_exp_res(0);
    		body.setPartnerId(0);
    		body.setOnlineStatus(1);
    		body.setHomeTownId(0);
    		body.setContribution(0);
    		body.setBanned(false);
    		body.setKarma(0);
    		body.setReturnStat(0);
    		MJKDALoader.getInstance().install(body, true);
    		body.setNetConnection(null);
    		body.sendVisualEffectAtLogin();
    		body.setDead(false);
    		body.noPlayerCK 	= true;
    		body.noPlayerRobot 	= true;
    		body.setEmblemId(21);
    		CharacterTable.getInstance().storeNewCharacter(body);
    		body.refresh();
    		return body;
    	}

    	public L1PcInstance createBody(int level, int x, int y, int mid) throws Exception{
    		MJBotName bName			= MJBotNameLoader.getInstance().get();
    		if(bName == null || bName.name == null || bName.name.equals(""))
    			return null;

    		L1PcInstance body 	= new L1PcInstance();
    		body.setAccountName("MJBOT");
    		body.setName(bName.name);
    		body.setId(IdFactory.getInstance().nextId());
    		body.setClanname("");
    		body.setClanMemberNotes(String.format(""));
    		body.setType(_classType);
    		body.set_sex(_hormon > 50 ? 0 : 1);
    		body.setLevel(level);
    		body.setHighLevel(level);
    		body.set_exp(ExpTable.getExpByLevel(level));
    		MJBotUtil.calcStat(this, body);

    		if (Config.Login.StandbyServer)
    			body.setLawful(0);
    		else {
    			if (bName.lawful < 0)
    				body.setLawful(_rnd.nextInt(32767));
    			else
    				body.setLawful(bName.lawful);
    		}
    		body.setMap((short)mid);
    		MJBotUtil.setPosition(this, body, x, y);

    		MJBotClanInfo cInfo = MJBotClanInfoLoader.getInstance().get(bName.cName);
    		if(cInfo != null){
    			if(cInfo.leaderAI == null){
    				MJBotLastError result = MJAIScheduler.getInstance().setSiegeLeaderSchedule(cInfo);
    				if(result.ai == null)
    					System.out.println("MJBotBrain:" +result.message);
    			}
    			if(cInfo.clanObject != null){
    				body.setClanid(cInfo.clanObject.getClanId());
    				body.setClanname(cInfo.clanName);
    				body.setClanRank(L1Clan.普通成員);
    				body.setClanMemberNotes(String.format(""));
    				ClanMember cm = new ClanMember(body.getName(), body.getClanRank(), body.getLevel(), body.getClanMemberNotes(), body.getId(), body.getType(), body.getOnlineStatus() == 1, body);
    				cInfo.clanObject.addClanMember(cm);
    			}
    		}

    		body.setTitle(bName.title);
    		if (body.get_sex() == 0) 	body.setClassId(C_CreateChar.MALE_LIST[body.getType()]);
    		else						body.setClassId(C_CreateChar.FEMALE_LIST[body.getType()]);
    		body.setCurrentSprite(body.getClassId());
    		body.setHeading(_rnd.nextInt(8));
    		body.getMoveState().setHeading(body.getHeading());
    		body.set_food(39);
    		body.setGm(false);
    		body.setMonitor(false);
    		body.setGmInvis(false);
    		body.setStatus(0);
    		body.setAccessLevel((short)0);
    		body.resetBaseMr();
    		body.setElfAttr(0);
    		body.set_PKcount(0);
    		body.set_exp_res(0);
    		body.setPartnerId(0);
    		body.setOnlineStatus(1);
    		body.setHomeTownId(0);
    		body.setContribution(0);
    		body.setBanned(false);
    		body.setKarma(0);
    		body.setReturnStat(0);
    		MJKDALoader.getInstance().install(body, true);
    		body.setNetConnection(null);
    		body.sendVisualEffectAtLogin();
    		body.setDead(false);
    		body.noPlayerCK 	= true;
    		body.noPlayerRobot 	= true;

    		for (L1SummonInstance summon : L1World.getInstance().getAllSummons()) {
    			if (summon.getMaster().getId() == body.getId()) {
    				summon.setMaster(body);
    				body.addPet(summon);
    				for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(summon))
    					visiblePc.sendPackets(new S_SummonPack(summon, visiblePc));
    			}
    		}

    		MJRankUserLoader.getInstance().onUser(body);
    		CharacterTable.getInstance().storeNewCharacter(body);
    		body.refresh();
    		return body;
    	}

    	public L1PcInstance createBodyFixedLocation(int level, int x, int y, int mid) throws Exception{
    		MJBotName bName			= MJBotNameLoader.getInstance().get();
    		if(bName == null || bName.name == null || bName.name.equals(""))
    			return null;

    		L1PcInstance body 	= new L1PcInstance();
    		body.setAccountName("MJBOT");
    		body.setName(bName.name);
    		body.setClanname("");
    		body.setClanMemberNotes(String.format(""));
    		body.setId(IdFactory.getInstance().nextId());
    		body.setType(_classType);
    		body.set_sex(_hormon > 50 ? 0 : 1);
    		body.setLevel(level);
    		body.setHighLevel(level);
    		body.set_exp(ExpTable.getExpByLevel(level));
    		MJBotUtil.calcStat(this, body);

    		if (Config.Login.StandbyServer)
    			body.setLawful(0);
    		else {
    			if (bName.lawful < 0)
    				body.setLawful(_rnd.nextInt(32767));
    			else
    				body.setLawful(bName.lawful);
    		}
    		body.setMap((short)mid);
    		body.setX(x);
    		body.setY(y);

    		MJBotClanInfo cInfo = MJBotClanInfoLoader.getInstance().get(bName.cName);
    		if(cInfo != null){
    			if(cInfo.leaderAI == null){
    				MJBotLastError result = MJAIScheduler.getInstance().setSiegeLeaderSchedule(cInfo);
    				if(result.ai == null)
    					System.out.println("MJBotBrain:" +result.message);
    			}
    			if(cInfo.clanObject != null){
    				body.setClanid(cInfo.clanObject.getClanId());
    				body.setClanname(cInfo.clanName);
    				body.setClanRank(L1Clan.普通成員);
    				body.setClanMemberNotes(String.format(""));
    				ClanMember cm = new ClanMember(body.getName(), body.getClanRank(), body.getLevel(), body.getClanMemberNotes(), body.getId(), body.getType(), body.getOnlineStatus() == 1, body);
    				cInfo.clanObject.addClanMember(cm);
    			}
    		}

    		body.setTitle(bName.title);
    		if (body.get_sex() == 0) 	body.setClassId(C_CreateChar.MALE_LIST[body.getType()]);
    		else						body.setClassId(C_CreateChar.FEMALE_LIST[body.getType()]);
    		body.setCurrentSprite(body.getClassId());
    		body.setHeading(_rnd.nextInt(8));
    		body.getMoveState().setHeading(body.getHeading());
    		body.set_food(39);
    		body.setGm(false);
    		body.setMonitor(false);
    		body.setGmInvis(false);
    		body.setStatus(0);
    		body.setAccessLevel((short)0);
    		body.resetBaseMr();
    		body.setElfAttr(0);
    		body.set_PKcount(0);
    		body.set_exp_res(0);
    		body.setPartnerId(0);
    		body.setOnlineStatus(1);
    		body.setHomeTownId(0);
    		body.setContribution(0);
    		body.setBanned(false);
    		body.setKarma(0);
    		body.setReturnStat(0);
    		MJKDALoader.getInstance().install(body, true);
    		body.setNetConnection(null);
    		body.sendVisualEffectAtLogin();
    		body.setDead(false);
    		body.noPlayerCK 	= true;
    		body.noPlayerRobot 	= true;

    		for (L1SummonInstance summon : L1World.getInstance().getAllSummons()) {
    			if (summon.getMaster().getId() == body.getId()) {
    				summon.setMaster(body);
    				body.addPet(summon);
    				for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(summon))
    					visiblePc.sendPackets(new S_SummonPack(summon, visiblePc));
    			}
    		}

    		MJRankUserLoader.getInstance().onUser(body);
    		CharacterTable.getInstance().storeNewCharacter(body);
    		body.refresh();
    		return body;
    	}

    	public L1PcInstance createCrownBody(MJBotClanInfo cInfo) throws Exception{
    		L1PcInstance body 	= L1PcInstance.load(cInfo.leaderName);
    		boolean isNew 		= false;
    		if(body == null){
    			body = new L1PcInstance();
    			isNew = true;
    		}
    		body.setAccountName("MJBOT");
    		body.setName(cInfo.leaderName);
    		body.setClanMemberNotes(String.format(""));
    		body.setClanname("");
    		body.setId(IdFactory.getInstance().nextId());
    		body.setType(_classType);
    		body.set_sex(_hormon > 50 ? 0 : 1);
    		body.setLevel(70);
    		body.setHighLevel(70);
    		body.set_exp(ExpTable.getExpByLevel(70));
    		MJBotUtil.calcStat(this, body);

    		if (Config.Login.StandbyServer)
    			body.setLawful(0);
    		else
    			body.setLawful(_rnd.nextInt(32767));

    		MJBotLocation loc = MJBotUtil.createRandomLocation(
    				MJBotLoadManager.MBO_WANDER_MAT_LEFT,
    				MJBotLoadManager.MBO_WANDER_MAT_TOP,
    				MJBotLoadManager.MBO_WANDER_MAT_RIGHT,
    				MJBotLoadManager.MBO_WANDER_MAT_BOTTOM,
    				MJBotLoadManager.MBO_WANDER_MAT_MAPID
    			);
    		body.setMap((short)loc.map);
    		body.setX(loc.x);
    		body.setY(loc.y);
    		body.setTitle(cInfo.clanName);
    		body.setClanRank(0);
    		body.setClanMemberNotes(String.format(""));
    		if (body.get_sex() == 0) 	body.setClassId(C_CreateChar.MALE_LIST[body.getType()]);
    		else						body.setClassId(C_CreateChar.FEMALE_LIST[body.getType()]);
    		body.setCurrentSprite(body.getClassId());
    		body.setHeading(_rnd.nextInt(8));
    		body.getMoveState().setHeading(body.getHeading());
    		body.set_food(39);
    		body.setGm(false);
    		body.setMonitor(false);
    		body.setGmInvis(false);
    		body.setStatus(0);
    		body.setAccessLevel((short)0);
    		body.resetBaseMr();
    		body.setElfAttr(0);
    		body.set_PKcount(0);
    		body.set_exp_res(0);
    		body.setPartnerId(0);
    		body.setOnlineStatus(1);
    		body.setHomeTownId(0);
    		body.setContribution(0);
    		body.setBanned(false);
    		body.setKarma(0);
    		body.setReturnStat(0);
    		MJKDALoader.getInstance().install(body, true);
    		body.setNetConnection(null);
    		body.sendVisualEffectAtLogin();
    		body.setDead(false);
    		body.noPlayerCK 	= true;
    		body.noPlayerRobot 	= true;

    		for (L1SummonInstance summon : L1World.getInstance().getAllSummons()) {
    			if (summon.getMaster().getId() == body.getId()) {
    				summon.setMaster(body);
    				body.addPet(summon);
    				for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(summon))
    					visiblePc.sendPackets(new S_SummonPack(summon, visiblePc));
    			}
    		}
    		MJRankUserLoader.getInstance().onUser(body);
    		body.setClanname("");
    		if(isNew)
    			CharacterTable.getInstance().storeNewCharacter(body);
    		else
    			CharacterTable.getInstance().storeCharacter(body);
    		body.refresh();

    		for (L1Clan cln : L1World.getInstance().getAllClans()) {
    			if (cln.getClanName().equalsIgnoreCase(cInfo.clanName)) {
    				cInfo.clanObject = cln;
    				cln.setLeaderId(body.getId());
    				cln.setLeaderName(body.getName());
    				body.setClanid(cInfo.clanObject.getClanId());
    				body.setClanname(cInfo.clanName);
    				body.setClanRank(L1Clan.군주);
    				body.setClanMemberNotes(String.format(""));
    				ClanMember cm = new ClanMember(body.getName(), body.getClanRank(), body.getLevel(), body.getClanMemberNotes(), body.getId(), body.getType(), body.getOnlineStatus() == 1, body);
    				cInfo.clanObject.addClanMember(cm);
    				CharacterTable.getInstance().storeCharacter(body);
    				break;
    			}
    		}

    		if(cInfo.clanObject == null){
    			cInfo.clanObject = ClanTable.getInstance().createClan(body, cInfo.clanName, true);
    			body.setClanid(cInfo.clanObject.getClanId());
    			body.setClanname(cInfo.clanName);
    			body.setClanRank(L1Clan.군주);
    			body.setClanMemberNotes(String.format(""));
    			CharacterTable.getInstance().storeCharacter(body);

    		}
    		if(cInfo.clanObject != null){
    			cInfo.clanObject.setEmblemId(cInfo.emblemId);
    			cInfo.clanObject.setEmblemStatus(1);
    			ClanTable.getInstance().updateClan(cInfo.clanObject);

    		}
    		return body;
    	}

    	public int getDollId(){
    		ArrayList<Integer> list = MJBotDollLoader.getInstance().get(_classType);
    		if(list == null || list.size() <= 0)
    			return 0;

    		return list.get(_rnd.nextInt(list.size()));
    	}

    	@Override
    	public String toString(){
    		StringBuilder sb = new StringBuilder(256);
    		sb.append("[brain id : ").append(_bid).append("]\n");
    		sb.append("[age : ]").append(_age).append("]\n");
    		sb.append("[decision time : ").append(_decisionTime).append("]\n");
    		sb.append("[hormon : ").append(_hormon).append("]\n");
    		sb.append("[pride : ").append(_pride).append("]\n");
    		sb.append("[sense : ").append(_sense).append("]");
    		return sb.toString();
    	}
    }
