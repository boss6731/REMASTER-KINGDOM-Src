package l1j.server.MJCaptchaSystem.Loader;

import l1j.server.MJCaptchaSystem.MJCaptcha;
import l1j.server.MJTemplate.MJPropertyReader;
import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.Command.MJCommand;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.MJTemplate.Command.MJCommandTree;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Ability;
import l1j.server.server.serverpackets.S_SystemMessage;

public class MJCaptchaLoadManager implements MJCommand{
	private static MJCaptchaLoadManager _instance;
	public static MJCaptchaLoadManager getInstance(){
		if(_instance == null)
			_instance = new MJCaptchaLoadManager();
		return _instance;
	}

	public static void release(){
		if(_instance != null){
			_instance.dispose();
			_instance = null;
		}
	}

	public static void reload(){
		MJCaptchaLoadManager old = _instance;
		_instance = new MJCaptchaLoadManager();
		if(old != null){
			old.dispose();
			old = null;
		}
	}

	public static boolean CAPTCHA_IS_RUNNING;
	public static int CAPTCHA_SHOW_PROBABILITY_BYMILLIMON;
	public static int CAPTCHA_INPUT_DURATION;
	public static int CAPTCHA_RELAY_COUNT;
	public static int CAPTCHA_FAIL_COUNT;
	public static int[] CAPTCHA_TELEPORT_INFO;
	public static MJSimpleRgb CAPTCHA_MESSAGE_COLOR;
	public static long CAPTCHA_DELAY_MILLIS;
	private MJCommandTree _commands;
	private MJCaptchaLoadManager(){
		_commands = createCommand();
	}
	
	public void load(){
		loadConfig();
		MJCaptchaDataLoader.getInstance();
	}
	
	private void loadConfig(){
		MJPropertyReader reader = null;
		try{
			
			reader = new MJPropertyReader("./config/mj_captcha.properties");
			CAPTCHA_IS_RUNNING = reader.readBoolean("CaptchaIsRunning", "true");
			CAPTCHA_SHOW_PROBABILITY_BYMILLIMON = reader.readInt("ShowProbability", "200");
			CAPTCHA_INPUT_DURATION = reader.readInt("InputDurationSecond", "10");
			CAPTCHA_RELAY_COUNT = reader.readInt("MonsterKillIgnoreCount", "5");
			CAPTCHA_FAIL_COUNT = reader.readInt("TotalFailCount", "5");
			CAPTCHA_DELAY_MILLIS = reader.readInt("DelayMillisecond", "10000");
			CAPTCHA_TELEPORT_INFO = new int[]{
					reader.readInt("Assert_X", "32736"),
					reader.readInt("Assert_Y", "32799"),
					reader.readInt("Assert_mapId", "213")
			};
			CAPTCHA_MESSAGE_COLOR = MJSimpleRgb.fromRgb(
					reader.readInt("MessageColor_R", "255"), 
					reader.readInt("MessageColor_G", "50"), 
					reader.readInt("MessageColor_B", "50"));
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(reader != null)
				reader.dispose();			
		}
	}
	
	public boolean is_in_assert(L1PcInstance pc){
		return pc.getMapId() == CAPTCHA_TELEPORT_INFO[2];
	}

	public void do_assert(L1PcInstance pc) {
		if (pc == null)
			return;

		pc.sendPackets(new S_Ability(3, false)); // 发送一个关闭能力（ID: 3）的数据包给玩家
		pc.sendPackets(new S_SystemMessage("在此地圖上無法使用光照功能。")); // 发送系统消息，告知玩家在当前地图不能使用光照
		pc.do_simple_teleport(CAPTCHA_TELEPORT_INFO[0], CAPTCHA_TELEPORT_INFO[1], CAPTCHA_TELEPORT_INFO[2]); // 简单传送玩家到指定坐标
	}
	
	public boolean do_auth_captcha(L1PcInstance pc, String text){
		MJCaptcha captcha = pc.get_captcha();
		if(captcha == null)
			captcha = pc.create_captcha();
		
		if(!captcha.is_keep_captcha())
			return false;
		
		try{
			Integer answer = Integer.parseInt(text);
			if(answer >= 0 && answer <= 9){
				captcha.auth_captcha(pc, answer);
				return true;
			}
		}catch(Exception e){}
		return false;
	}

	private MJCommandTree createCommand() {
		return new MJCommandTree(".驗證碼", "執行與驗證碼相關的命令。", null)
				.add_command(createReloadCommand())
				.add_command(createUserInitializeCommand())
				.add_command(createAllInitializeCommand());
	}

	private MJCommandTree createUserInitializeCommand() {
		return new MJCommandTree("角色初始化", "初始化選定角色的驗證碼信息。", new String[]{"角色名稱"}) {
			@override
			protected void to_handle_command(MJCommandArgs args) throws Exception {
				String character_name = args.nextString();
				L1PcInstance pc = L1World.getInstance().getPlayer(character_name);
				if (pc == null) {
					args.notify(String.format("找不到角色名稱為 %s 的玩家。", character_name));
					return;
				}

				MJCaptcha captcha = pc.get_captcha();
				if (captcha != null) {
					captcha
							.set_c_data(null)
							.set_fail_count(0)
							.set_relay_count(0)
							.set_is_answer(true);
				}
				args.notify(String.format("已初始化 %s 的驗證碼信息。", character_name));
			}
		};
	}

	private MJCommandTree createAllInitializeCommand() {
		return new MJCommandTree("全部初始化", "初始化所有用戶的驗證碼信息。", null) {
			@override
			protected void to_handle_command(MJCommandArgs args) throws Exception {
				L1World.getInstance().getAllPlayerStream()
						.filter((L1PcInstance pc) -> {
							return pc != null && pc.getAI() == null;
						})
						.forEach((L1PcInstance pc) -> {
							MJCaptcha captcha = pc.get_captcha();
							if (captcha != null) {
								captcha
										.set_c_data(null)
										.set_fail_count(0)
										.set_relay_count(0)
										.set_is_answer(true);
							}
						});
			}
		};
	}

	private MJCommandTree createReloadCommand() {
		return new MJCommandTree("重載", "執行重載相關命令。", null)
				.add_command(new MJCommandTree("信息", "", null) {
					@override
					protected void to_handle_command(MJCommandArgs args) throws Exception {
						MJCaptchaDataLoader.reload();
						args.notify("已重載 tb_captcha 表。");
					}
				})
				.add_command(new MJCommandTree("配置", "", null) {
					@override
					protected void to_handle_command(MJCommandArgs args) throws Exception {
						loadConfig();
						args.notify("已重載 mj_captcha.properties 配置文件。");
					}
				});
	}
	
	public void dispose(){
	}

	@Override
	public void execute(MJCommandArgs args) {
		_commands.execute(args, new StringBuilder(256).append(_commands.to_operation()));
	}
}
