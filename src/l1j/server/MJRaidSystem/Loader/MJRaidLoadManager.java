package l1j.server.MJRaidSystem.Loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import l1j.server.MJRaidSystem.MJRaidObject;
import l1j.server.MJRaidSystem.MJRaidSpace;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_SystemMessage;

/** 管理 襲擊 載入器的 管理程序 類. **/
public class MJRaidLoadManager {
	private static MJRaidLoadManager _instance;
	public static MJRaidLoadManager getInstance(){
		if(_instance == null)
			_instance = new MJRaidLoadManager();
		return _instance;
	}
	
	private static final String 			_fileName 	= "./config/mjraid.properties";
	private static final SimpleDateFormat 	_dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
	private static Properties _settings;
	private MJRaidLoadManager(){
		
	}
	
	private static ArrayDeque<Integer> parseToIntArray(String s){
		String[] arr = s.split(" ");
		int size = arr.length;
		ArrayDeque<Integer> argsQ = new ArrayDeque<Integer>(size);
		for(int i=0; i<size; i++){
			try{
				argsQ.offer(Integer.parseInt(arr[i]));
			}catch(Exception e){
				break;
			}
		}
		return argsQ;
	}
	
	private static final S_SystemMessage _basicMenus = new S_SystemMessage(
			"[1. 狀態], [2. 重新加載], [3. 強制結束]\n" +"[4. 全體用戶初始化], [5. 用戶初始化]"
			);
	public static void commands(L1PcInstance gm, String param){
		try{
			ArrayDeque<Integer> argsQ = parseToIntArray(param);
			if(argsQ == null || argsQ.isEmpty())
				throw new Exception("");
			
			switch(argsQ.poll()){
			case 1:
				statusCommands(gm, argsQ);
				break;
			case 2:
				reloadCommands(gm, argsQ);
				break;
			case 3:
				closeCommands(gm, argsQ);
				break;
			case 4:
				allUserInitializeCommands(gm);
				break;
			case 5:
				String[] arrs = param.split(" ");
				userInitializeCommands(gm, arrs[arrs.length - 1]);
				break;
			default:
				throw new Exception("");
			}
			
		}catch(Exception e){
			gm.sendPackets(_basicMenus, false);
		}
	}
	
	private static final S_SystemMessage _statusMenus = new S_SystemMessage(
			"[使用方法].突襲 1\n" +
				"[1. 剩餘空間], [2. 開放地圖], [3. 詳細信息]"
			);
	StringBuilder sb = new StringBuilder(256);
switch (argsQ.poll()) {
		case 1: {
			sb.append("總共 ").append(MRS_COPYMAP_SIZE).append(" 個空間中\n");
					Iterator<Integer> it = MJRaidSpace.getInstance().getOpensMaps().iterator();
			Integer i;
			int cnt = 0;
			while (it.hasNext()) {
				i = it.next();
				if (!(i == null || i < MRS_COPYMAP_START_ID || i > MRS_COPYMAP_START_ID + MRS_COPYMAP_SIZE)) {
					if (MJRaidSpace.getInstance().getOpenObject(i) != null) {
						cnt++;
					}
				}
			}
			sb.append(cnt).append(" 個使用中。");
			break;
		}
		case 2: {
			Iterator<Integer> it = MJRaidSpace.getInstance().getOpensMaps().iterator();
			Integer i;
			sb.append("開放的地圖: ");
			while (it.hasNext()) {
				i = it.next();
				if (!(i == null || i < MRS_COPYMAP_START_ID || i > MRS_COPYMAP_START_ID + MRS_COPYMAP_SIZE)) {
					if (MJRaidSpace.getInstance().getOpenObject(i) != null) {
						sb.append(i).append(" ");
					}
				}
			}
			break;
		}
		case 3: {
			MJRaidObject obj = null;
			if (argsQ.isEmpty()) {
				sb.append("錯誤: [用法].突襲 1\n 3 [地圖ID]");
			} else {
				obj = MJRaidSpace.getInstance().getOpenObject(argsQ.poll());
				if (obj == null) {
					sb.append("該地圖未開放。");
				} else {
					sb.append(obj.toString());
				}
			}
			break;
		}
		default: {
			gm.sendPackets(_statusMenus, false);
			return;
		}
	}

gm.sendPackets(new S_SystemMessage(sb.toString()));
}

	private static final S_SystemMessage _statusMenus = new S_SystemMessage(
			"[使用方法].突襲 2\r" +
				"[1. 配置], [2. 類型], [3. 突襲鍵]\r" +
				"[4. 獎勵], [5. 生成], [6. 技能]\n" +
				"[7. 空間], [8. 動作], [9. 全部]"
	);
	private static void reloadCommands(L1PcInstance gm, ArrayDeque<Integer> argsQ){
		if(argsQ.isEmpty()){
			gm.sendPackets(_reloadMenus, false);
			return;
		}
		
		StringBuilder sb = new StringBuilder(256);
		switch(argsQ.poll()){
		case 1:
			loadConfig();
			sb.append("[配置重新加載完成。]");
			break;
			case 2:
				MJRaidTypesLoader.reload();
				sb.append("[類型加載完成。]");
				break;
			case 3:
				MJRaidCreatorLoader.reload();
				sb.append("[創建者鍵加載完成。]");
				break;
			case 4:
				MJRaidCompensatorLoader.reload();
				sb.append("[補償器加載完成。]");
				break;
			case 5:
				MJRaidSpawnLoader.reload();
				sb.append("[生成點加載完成。]");
				break;
			case 6:
				MJRaidBossSkillLoader.reload();
				sb.append("[技能加載完成。]");
				break;
			case 7:
				MJRaidSpace.reload();
				sb.append("[空間加載完成。]");
				break;
			case 8:
				//MJRaidBossSprLoader.reload();
				sb.append("[動作加載完成。]");
				break;
			case 9:
				_instance.reload();
				sb.append("[MJRaid系統全部加載完成。]");
				break;
			default:
				gm.sendPackets(_reloadMenus, false);
				return;
		}
		
		gm.sendPackets(new S_SystemMessage(sb.toString()));
	}

	private static final S_SystemMessage _closeMenus = new S_SystemMessage(
			"[使用方法].突襲 3\n [地圖ID]"
	);
	private static void closeCommands(L1PcInstance gm, ArrayDeque<Integer> argsQ){
		if(argsQ.isEmpty()){
			gm.sendPackets(_closeMenus, false);
			return;
		}
		StringBuilder sb = new StringBuilder(256);
		MJRaidObject obj = MJRaidSpace.getInstance().getOpenObject(argsQ.poll());
		if(obj == null)
			sb.append("該地圖尚未開放。");
		else{
			int mid = obj.getCopyMapId();
			obj.failRaid();
			sb.append(mid).append(" 實例已被強制終止。");
		}

		gm.sendPackets(new S_SystemMessage(sb.toString()));
	}
	
	private static void allUserInitializeCommands(L1PcInstance gm){
		Collection<L1PcInstance> pcs 	= L1World.getInstance().getAllPlayers();
		ArrayList<L1PcInstance> pcList 	= new ArrayList<L1PcInstance>(pcs.size());
		int size 						= pcList.size();
		Timestamp ts 					= new Timestamp(1);
		L1PcInstance pc					= null;
		pcList.addAll(pcs);
		
		for(int i=0; i<size; i++){
			pc = pcList.get(i);
			if(pc == null || pc.getAccount() == null)
				continue;
			
			pc.getAccount().setDragonRaid(ts);
			if(pc.hasSkillEffect(L1SkillId.DRAGONRAID_BUFF))
				pc.removeSkillEffect(L1SkillId.DRAGONRAID_BUFF);
		}

		GeneralThreadPool.getInstance().execute(new MJRaidUserTimeStore(pcList));
		gm.sendPackets(new S_SystemMessage("已初始化所有用戶的突襲時間。"));
	}

	private static void userInitializeCommands(L1PcInstance gm, String name){
		L1PcInstance pc = L1World.getInstance().getPlayer(name);
		StringBuilder sb = new StringBuilder(256);
		if(pc == null || pc.getAccount() == null){
			sb.append("[").append(name).append("] 無法找到該用戶。");
		}else{
			Timestamp ts = new Timestamp(1);
			pc.getAccount().setDragonRaid(ts);
			if(pc.hasSkillEffect(L1SkillId.DRAGONRAID_BUFF))
				pc.removeSkillEffect(L1SkillId.DRAGONRAID_BUFF);
			ArrayList<L1PcInstance> list = new ArrayList<L1PcInstance>();
			new MJRaidUserTimeStore(list).run();
			sb.append("[").append(name).append("] 已初始化突襲時間。");
		}

		gm.sendPackets(new S_SystemMessage(sb.toString()));
	}
	
	/** 單獨的load()方法以避免由於物件依賴而導致的異常. **/
	public void load(){
		loadConfig();
		MJRaidTypesLoader.getInstance();
		MJRaidCreatorLoader.getInstance();
		MJRaidCompensatorLoader.getInstance();
		MJRaidSpawnLoader.getInstance();
		MJRaidBossSkillLoader.getInstance();
		MJRaidSpace.getInstance();
		//MJRaidBossSprLoader.getInstance();
	}
	
	/** 重新載入 **/
	public void reload(){
		loadConfig();
		MJRaidTypesLoader.reload();
		MJRaidCreatorLoader.reload();
		MJRaidCompensatorLoader.reload();
		MJRaidBossSkillLoader.reload();
		MJRaidSpawnLoader.reload();
		MJRaidSpace.reload();
		//MJRaidBossSprLoader.reload();
	}
	
	/** 釋放資源. **/
	public void release(){
		MJRaidSpace.release();
		MJRaidTypesLoader.release();
		MJRaidCreatorLoader.release();
		MJRaidCompensatorLoader.release();
		MJRaidBossSkillLoader.release();
		MJRaidSpawnLoader.release();
		//MJRaidBossSprLoader.release();
	}
	
	/** mrs是MJ 突襲系統. **/
	public static long 		MRS_THREAD_CLOCK;
	public static long 		MRS_MESSAGE_DELAY;
	public static int		MRS_ERRBACK_MAPID;
	public static int		MRS_ERRBACK_X;
	public static int		MRS_ERRBACK_Y;
	public static long		MRS_BOSSSPAWN_DELAY;
	public static int 		MRS_COPYMAP_START_ID;
	public static int 		MRS_COPYMAP_SIZE;
	public static long		MRS_RAID_DELAY;
	public static double	MRS_RAID_BOSS_SPEEDRATE;
	/** CB是組合. **/
	public static int 	MRS_CB_DELAY;
	public static int 	MRS_CB_COUNT;
	
	/** BS是boss技能 **/
	public static int 	MRS_BS_USE_RATE;
	public static int 	MRS_BS_MIN_RATE;
	public static int 	MRS_BS_MAX_RATE;
	public static int 	MRS_BS_SPELL_RATE;
	public static int 	MRS_BS_STUN_MIN;
	public static int 	MRS_BS_STUN_MAX;
	public static int 	MRS_BS_STUN_EFFECTID;
	public static int 	MRS_BS_PAL_POISON_DELAY;
	public static int 	MRS_BS_PAL_POISON_TIME;
	public static int 	MRS_BS_DEATHHEAL_ID;
	public static int 	MRS_BS_DEATHHEAL_TIME;
	public static int 	MRS_BS_DEATHPOTION_ID;
	public static int 	MRS_BS_DEATHPOTION_TIME;
	public static int 	MRS_BS_REDUCEHEAL_ID;
	public static int 	MRS_BS_REDUCEHEAL_TIME;
	public static long	MRS_BS_RINDFLY_MAX;
	public static long 	MRS_BS_RINDFLY_MIN;
	public static int 	MRS_BS_VALA_ABSOLUTEBLADE;
	public static int 	MRS_BS_VALA_IMMUNEBLADE;
	
	

	@SuppressWarnings("unused")
	private static void storeConfig(){
		try {
			if(_settings != null){
				OutputStream os = new FileOutputStream(_fileName);
				Timestamp ts = new Timestamp(System.currentTimeMillis());
				storeSystemConfig(_settings);
				storeComboConfig(_settings);
				storeSpawnConfig(_settings);
				storeSkillConfig(_settings);
				_settings.store(os, _dateFormat.format(ts));
				os.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void loadConfig(){
		String column = "";
		try{
			_settings = new Properties();
			InputStream is = new FileInputStream(new File(_fileName));
			_settings.load(is);
			is.close();
			loadSystemConfig(_settings);
			loadComboConfig(_settings);
			loadSkillConfig(_settings);
			loadSpawnConfig(_settings);
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("MJ 突襲負載管理器::載入配置()...錯誤列 " + column);
		}
	}
	
	/** 系統設定 **/
	private static void loadSystemConfig(Properties settings){
		String column = "";
		try{
			column	= "ThreadClock";
			MRS_THREAD_CLOCK		= Long.parseLong(settings.getProperty(column, "1000"));
			
			column	= "MessageDelay";
			MRS_MESSAGE_DELAY		= Long.parseLong(settings.getProperty(column, "5000"));
			
			column	= "ErrorBackMapId";
			MRS_ERRBACK_MAPID		= Integer.parseInt(settings.getProperty(column, "4"));
			
			column	= "ErrorBackX";
			MRS_ERRBACK_X			= Integer.parseInt(settings.getProperty(column, "33437"));
			
			column	= "ErrorBackY";
			MRS_ERRBACK_Y			= Integer.parseInt(settings.getProperty(column, "32813"));
			
			column	= "BossSpawnDelay";
			MRS_BOSSSPAWN_DELAY		= Long.parseLong(settings.getProperty(column, "120000"));
			
			column	= "CopyMap_Start_Id";
			MRS_COPYMAP_START_ID 	= Integer.parseInt(settings.getProperty(column, "17000"));
			
			column	= "CopyMap_Size";
			MRS_COPYMAP_SIZE 		= Integer.parseInt(settings.getProperty(column, "200"));
			
			column	= "RaidSuccessDelay";
			MRS_RAID_DELAY			= Long.parseLong(settings.getProperty(column, "72")) * (3600000L);
			
			column	= "RaidBossSpeedRate";
			MRS_RAID_BOSS_SPEEDRATE	= ((double)Integer.parseInt(settings.getProperty(column, "1")) * 0.01);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/** 組合設定 **/
	private static void loadComboConfig(Properties settings){
		String column = "";
		try{
			column	= "BossComboDelay";
			MRS_CB_DELAY			= Integer.parseInt(settings.getProperty(column, "50"));
			
			column	= "BossComboCount";
			MRS_CB_COUNT			= Integer.parseInt(settings.getProperty(column, "3"));
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/** sp 已生成 **/
	public static int	MRS_SP_POISONCLOUD_ID;
	public static int	MRS_SP_POISONCLOUD_TIME;
	public static long	MRS_SP_PRODUCT_TIME;
	public static long 	MRS_SP_PRODUCT_DESTTIME;
	public static int 	MRS_SP_5COLOR_PEARL_ID;
	public static int 	MRS_SP_MYST_5COLOR_PEARL_ID;
	public static int 	MRS_SP_TORNAR_ID;
	public static int 	MRS_SP_SAEL1_ID;
	public static int 	MRS_SP_SAEL2_ID;
	public static long	MRS_SP_VALAKAS_READY_TIME;
	public static boolean MRS_SP_VALAKAS_GROUNDFIRE_IS_POSSPAWN;
	public static int	MRS_SP_VALAKAS_GROUNDFIRE1_ID;
	public static int	MRS_SP_VALAKAS_GROUNDFIRE2_ID;
	public static int	MRS_SP_VALAKAS_GROUNDFIRE3_ID;
	public static int	MRS_SP_VALAKAS_GROUNDFIRE4_ID;
	
	public static int 	MRS_SP_VALAKAS_GROUNDFIRE_NUM;
	public static int 	MRS_SP_VALAKAS_GROUNDFIRE_RANGE;
	public static int 	MRS_SP_VALAKAS_DUSTRAIN_ID;
	public static int 	MRS_SP_VALAKAS_EYE_ID;
	public static int 	MRS_SP_VALAKAS_PRODUCT_TIME;
	public static int 	MRS_SP_VALAKAS_PRODUCT_DESTTIME;
	public static int 	MRS_SP_VALAKAS_MPABSORB_ID;
	public static int 	MRS_SP_VALAKAS_MPABSORB_NUM;
	public static int 	MRS_SP_VALAKAS_HPABSORB_ID;
	public static int 	MRS_SP_VALAKAS_HPABSORB_NUM;
	public static int 	MRS_SP_VALAKAS_ABSORB_RANGE;
	public static int 	MRS_SP_VALAKAS_ABSORB_MPDRAIN;
	public static int 	MRS_SP_VALAKAS_ABSORB_HPDRAIN;
	
	/** 生成設定 **/
	private static void loadSpawnConfig(Properties settings){
		String column = "";
		try{
			
			column	= "PoisonCloudNpcId";
			MRS_SP_POISONCLOUD_ID		= Integer.parseInt(settings.getProperty(column, "14212110"));
			
			column	= "PoisonCloudTime";
			MRS_SP_POISONCLOUD_TIME		= Integer.parseInt(settings.getProperty(column, "20000"));
			
			column	= "FafuProductGenTime";
			MRS_SP_PRODUCT_TIME			= Long.parseLong(settings.getProperty(column, "120000"));
			
			column	= "FafuProductDestTime";
			MRS_SP_PRODUCT_DESTTIME		= Long.parseLong(settings.getProperty(column, "60000"));
			
			column	= "Fafu5ColorPearlId";
			MRS_SP_5COLOR_PEARL_ID		= Integer.parseInt(settings.getProperty(column, "4039001"));

			column	= "FafuMyst5ColorPearlId";
			MRS_SP_MYST_5COLOR_PEARL_ID	= Integer.parseInt(settings.getProperty(column, "4039002"));
			
			column	= "FafuTornarId";
			MRS_SP_TORNAR_ID			= Integer.parseInt(settings.getProperty(column, "4039003"));
			
			column	= "FafuSael1Id";
			MRS_SP_SAEL1_ID				= Integer.parseInt(settings.getProperty(column, "4039004"));
			
			column	= "FafuSael2Id";
			MRS_SP_SAEL2_ID				= Integer.parseInt(settings.getProperty(column, "4039005"));
			
			column	= "ValakasReadyTime";
			MRS_SP_VALAKAS_READY_TIME		= Long.parseLong(settings.getProperty(column, "60000")); 
			
			column	= "ValakasGroundFireIsPosSpawn";
			MRS_SP_VALAKAS_GROUNDFIRE_IS_POSSPAWN	= Boolean.parseBoolean(settings.getProperty(column, "false"));
			
			column	= "ValakasGroundFire1Id";
			MRS_SP_VALAKAS_GROUNDFIRE1_ID	= Integer.parseInt(settings.getProperty(column, "14212150"));

			column	= "ValakasGroundFire2Id";
			MRS_SP_VALAKAS_GROUNDFIRE2_ID	= Integer.parseInt(settings.getProperty(column, "14212151"));
			
			column	= "ValakasGroundFire3Id";
			MRS_SP_VALAKAS_GROUNDFIRE3_ID	= Integer.parseInt(settings.getProperty(column, "14212152"));
			
			column	= "ValakasGroundFire4Id";
			MRS_SP_VALAKAS_GROUNDFIRE4_ID	= Integer.parseInt(settings.getProperty(column, "14212153"));
			
			column	= "ValakasGroundFireNum";
			MRS_SP_VALAKAS_GROUNDFIRE_NUM	= Integer.parseInt(settings.getProperty(column, "20"));
			
			column	= "ValakasGroundFireRange";
			MRS_SP_VALAKAS_GROUNDFIRE_RANGE	= Integer.parseInt(settings.getProperty(column, "10"));
			
			column	= "ValakasDustRainId";
			MRS_SP_VALAKAS_DUSTRAIN_ID		= Integer.parseInt(settings.getProperty(column, "14212146"));
			
			column	= "ValakasEyeId";
			MRS_SP_VALAKAS_EYE_ID			= Integer.parseInt(settings.getProperty(column, "14212147"));
			
			column	= "ValakasAbsoluteBlade";
			MRS_BS_VALA_ABSOLUTEBLADE		= Integer.parseInt(settings.getProperty(column, "100"));
			
			column	= "ValakasImmuneBlade";
			MRS_BS_VALA_IMMUNEBLADE			= Integer.parseInt(settings.getProperty(column, "100"));
			
			column	= "ValakasProductGenTime";
			MRS_SP_VALAKAS_PRODUCT_TIME		= Integer.parseInt(settings.getProperty(column, "120000"));
			
			column	= "ValakasProductDestTime";
			MRS_SP_VALAKAS_PRODUCT_DESTTIME	= Integer.parseInt(settings.getProperty(column, "60000"));
			
			column	= "ValakasMpAbsorbId";
			MRS_SP_VALAKAS_MPABSORB_ID		= Integer.parseInt(settings.getProperty(column, "14212148"));
					
			column	= "ValakasHpAbsorbId";					
			MRS_SP_VALAKAS_HPABSORB_ID		= Integer.parseInt(settings.getProperty(column, "14212149"));
			
			column	= "ValakasMpAbsorbNum";
			MRS_SP_VALAKAS_MPABSORB_NUM		= Integer.parseInt(settings.getProperty(column, "3"));
					
			column	= "ValakasHpAbsorbNum";					
			MRS_SP_VALAKAS_HPABSORB_NUM		= Integer.parseInt(settings.getProperty(column, "3"));
			
			column	= "ValakasAbsorbRange";
			MRS_SP_VALAKAS_ABSORB_RANGE		= Integer.parseInt(settings.getProperty(column, "3"));
			
			column	= "ValakasAbsorbMPDrain";
			MRS_SP_VALAKAS_ABSORB_MPDRAIN	= Integer.parseInt(settings.getProperty(column, "50"));
			
			column	= "ValakasAbsorbHPDrain";
			MRS_SP_VALAKAS_ABSORB_HPDRAIN	= Integer.parseInt(settings.getProperty(column, "500"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/** 首領技能 **/
	private static void loadSkillConfig(Properties settings){
		String column = "";
		try{
			column	= "BossSkillUseRate";
			MRS_BS_USE_RATE			= Integer.parseInt(settings.getProperty(column, "30"));
			
			column	= "BossSkillMinRate";
			MRS_BS_MIN_RATE			= Integer.parseInt(settings.getProperty(column, "5"));
			
			column	= "BossSkillMaxRate";
			MRS_BS_MAX_RATE			= Integer.parseInt(settings.getProperty(column, "90"));
			
			column	= "BossSkillSpellRate";
			MRS_BS_SPELL_RATE		= Integer.parseInt(settings.getProperty(column, "70"));
			
			column	= "BossStunTimeMin";
			MRS_BS_STUN_MIN			= Integer.parseInt(settings.getProperty(column, "500"));
			
			column	= "BossStunTimeMax";
			MRS_BS_STUN_MAX			= Integer.parseInt(settings.getProperty(column, "6000"));
			
			column	= "BossStunEffectId";
			MRS_BS_STUN_EFFECTID	= Integer.parseInt(settings.getProperty(column, "81162"));
			
			column	= "BossParalysisPosionDelay";
			MRS_BS_PAL_POISON_DELAY = Integer.parseInt(settings.getProperty(column, "8000"));

			column	= "BossParalysisPosionTime";
			MRS_BS_PAL_POISON_TIME 	= Integer.parseInt(settings.getProperty(column, "15000"));
			
			column	= "BossDeathHealId";
			MRS_BS_DEATHHEAL_ID		= Integer.parseInt(settings.getProperty(column, "10518"));
			
			column	= "BossDeathHealTime";
			MRS_BS_DEATHHEAL_TIME	= Integer.parseInt(settings.getProperty(column, "12000"));

			column	= "BossDeathPotionId";
			MRS_BS_DEATHPOTION_ID		= Integer.parseInt(settings.getProperty(column, "10513"));
			
			column	= "BossDeathPotionTime";
			MRS_BS_DEATHPOTION_TIME		= Integer.parseInt(settings.getProperty(column, "12000"));

			column	= "BossReduceHealId";
			MRS_BS_REDUCEHEAL_ID		= Integer.parseInt(settings.getProperty(column, "10517"));

			column	= "BossReduceHealTime";
			MRS_BS_REDUCEHEAL_TIME		= Integer.parseInt(settings.getProperty(column, "12000"));

			column	= "RindFlyMaxTime";
			MRS_BS_RINDFLY_MAX			= Long.parseLong(settings.getProperty(column, "10000"));
			
			column	= "RindFlyMinTime";
			MRS_BS_RINDFLY_MIN			= Long.parseLong(settings.getProperty(column, "5000"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/** 系統設定 **/
	private static void storeSystemConfig(Properties settings){
		String column = "";
		try{
			
			column	= "ThreadClock";
			settings.setProperty(column, String.valueOf(MRS_THREAD_CLOCK));
			
			column	= "MessageDelay";
			settings.setProperty(column, String.valueOf(MRS_MESSAGE_DELAY));
			
			column	= "ErrorBackMapId";
			settings.setProperty(column, String.valueOf(MRS_ERRBACK_MAPID));
			
			column	= "ErrorBackX";
			settings.setProperty(column, String.valueOf(MRS_ERRBACK_X));
			
			column	= "ErrorBackY";
			settings.setProperty(column, String.valueOf(MRS_ERRBACK_Y));
			
			column	= "BossSpawnDelay";
			settings.setProperty(column, String.valueOf(MRS_BOSSSPAWN_DELAY));
			
			column	= "CopyMap_Start_Id";
			settings.setProperty(column, String.valueOf(MRS_COPYMAP_START_ID));
			
			column	= "CopyMap_Size";
			settings.setProperty(column, String.valueOf(MRS_COPYMAP_SIZE));
			
			column	= "RaidSuccessDelay";
			settings.setProperty(column, String.valueOf(MRS_RAID_DELAY));
			
			column	= "RaidBossSpeedRate";
			settings.setProperty(column, String.valueOf(MRS_RAID_BOSS_SPEEDRATE));
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/** 組合設定 **/
	private static void storeComboConfig(Properties settings){
		String column = "";
		try{
			column	= "BossComboDelay";
			settings.setProperty(column, String.valueOf(MRS_CB_DELAY));
			
			column	= "BossComboCount";
			settings.setProperty(column, String.valueOf(MRS_CB_COUNT));

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/** 生成設定 **/
	private static void storeSpawnConfig(Properties settings){
		String column = "";
		try{
			
			column	= "PoisonCloudNpcId";
			settings.setProperty(column, String.valueOf(MRS_SP_POISONCLOUD_ID));
			
			column	= "PoisonCloudTime";
			settings.setProperty(column, String.valueOf(MRS_SP_POISONCLOUD_TIME));
			
			column	= "FafuProductGenTime";
			settings.setProperty(column, String.valueOf(MRS_SP_PRODUCT_TIME));
			
			column	= "FafuProductDestTime";
			settings.setProperty(column, String.valueOf(MRS_SP_PRODUCT_DESTTIME));
			
			column	= "Fafu5ColorPearlId";
			settings.setProperty(column, String.valueOf(MRS_SP_5COLOR_PEARL_ID));
			
			column	= "FafuMyst5ColorPearlId";
			settings.setProperty(column, String.valueOf(MRS_SP_MYST_5COLOR_PEARL_ID));
			
			column	= "FafuTornarId";
			settings.setProperty(column, String.valueOf(MRS_SP_TORNAR_ID));
			
			column	= "FafuSael1Id";
			settings.setProperty(column, String.valueOf(MRS_SP_SAEL1_ID));
			
			column	= "FafuSael2Id";
			settings.setProperty(column, String.valueOf(MRS_SP_SAEL2_ID));
			
			column	= "ValakasReadyTime";
			settings.setProperty(column, String.valueOf(MRS_SP_VALAKAS_READY_TIME));
			
			column	= "ValakasGroundFireType";
			settings.setProperty(column, String.valueOf(MRS_SP_VALAKAS_GROUNDFIRE_IS_POSSPAWN));
			
			column	= "ValakasGroundFireNum";
			settings.setProperty(column, String.valueOf(MRS_SP_VALAKAS_GROUNDFIRE_NUM));
			
			column	= "ValakasGroundFireRange";
			settings.setProperty(column, String.valueOf(MRS_SP_VALAKAS_GROUNDFIRE_RANGE));
			
			column	= "ValakasDustRainId";
			settings.setProperty(column, String.valueOf(MRS_SP_VALAKAS_DUSTRAIN_ID));
			
			column	= "ValakasEyeId";
			settings.setProperty(column, String.valueOf(MRS_SP_VALAKAS_EYE_ID));
			
			column	= "ValakasAbsoluteBlade";
			settings.setProperty(column, String.valueOf(MRS_BS_VALA_ABSOLUTEBLADE));
			
			column	= "ValakasImmuneBlade";
			settings.setProperty(column, String.valueOf(MRS_BS_VALA_IMMUNEBLADE));
			
			column	= "ValakasProductGenTime";
			settings.setProperty(column, String.valueOf(MRS_SP_VALAKAS_PRODUCT_TIME));
			
			column	= "ValakasProductDestTime";
			settings.setProperty(column, String.valueOf(MRS_SP_VALAKAS_PRODUCT_DESTTIME));
			
			column	= "ValakasMpAbsorbId";
			settings.setProperty(column, String.valueOf(MRS_SP_VALAKAS_MPABSORB_ID));
				
			column	= "ValakasHpAbsorbId";					
			settings.setProperty(column, String.valueOf(MRS_SP_VALAKAS_HPABSORB_ID));
			
			column	= "ValakasMpAbsorbNum";
			settings.setProperty(column, String.valueOf(MRS_SP_VALAKAS_MPABSORB_NUM));
				
			column	= "ValakasHpAbsorbNum";					
			settings.setProperty(column, String.valueOf(MRS_SP_VALAKAS_HPABSORB_NUM));
			
			column	= "ValakasAbsorbRange";
			settings.setProperty(column, String.valueOf(MRS_SP_VALAKAS_ABSORB_RANGE));
			
			column	= "ValakasAbsorbMPDrain";
			settings.setProperty(column, String.valueOf(MRS_SP_VALAKAS_ABSORB_MPDRAIN));
			
			column	= "ValakasAbsorbHPDrain";
			settings.setProperty(column, String.valueOf(MRS_SP_VALAKAS_ABSORB_HPDRAIN));
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/** 首領技能 **/
	private static void storeSkillConfig(Properties settings){
		String column = "";
		try{
			column	= "BossSkillUseRate";
			settings.setProperty(column, String.valueOf(MRS_BS_USE_RATE));
			
			column	= "BossSkillMinRate";
			settings.setProperty(column, String.valueOf(MRS_BS_MIN_RATE));
			
			column	= "BossSkillMaxRate";
			settings.setProperty(column, String.valueOf(MRS_BS_MAX_RATE));
			
			column	= "BossSkillSpellRate";
			settings.setProperty(column, String.valueOf(MRS_BS_SPELL_RATE));
			
			column	= "BossStunTimeMin";
			settings.setProperty(column, String.valueOf(MRS_BS_STUN_MIN));
			
			column	= "BossStunTimeMax";
			settings.setProperty(column, String.valueOf(MRS_BS_STUN_MAX));
			
			column	= "BossStunEffectId";
			settings.setProperty(column, String.valueOf(MRS_BS_STUN_EFFECTID));
			
			column	= "BossParalysisPosionDelay";
			settings.setProperty(column, String.valueOf(MRS_BS_PAL_POISON_DELAY));
			
			column	= "BossParalysisPosionTime";
			settings.setProperty(column, String.valueOf(MRS_BS_PAL_POISON_TIME));
			
			column	= "BossDeathHealId";
			settings.setProperty(column, String.valueOf(MRS_BS_DEATHHEAL_ID));
			
			column	= "BossDeathHealTime";
			settings.setProperty(column, String.valueOf(MRS_BS_DEATHHEAL_TIME));
			
			column	= "BossDeathPotionId";
			settings.setProperty(column, String.valueOf(MRS_BS_DEATHPOTION_ID));
			
			column	= "BossDeathPotionTime";
			settings.setProperty(column, String.valueOf(MRS_BS_DEATHPOTION_TIME));
			
			column	= "BossReduceHealId";
			settings.setProperty(column, String.valueOf(MRS_BS_REDUCEHEAL_ID));
			
			column	= "BossReduceHealTime";
			settings.setProperty(column, String.valueOf(MRS_BS_REDUCEHEAL_TIME));
			
			column	= "RindFlyMaxTime";
			settings.setProperty(column, String.valueOf(MRS_BS_RINDFLY_MAX));
			
			column	= "RindFlyMinTime";
			settings.setProperty(column, String.valueOf(MRS_BS_RINDFLY_MIN));
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
