package l1j.server.MJItemEnchantSystem;

import l1j.server.MJTemplate.Command.MJCommand;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.MJTemplate.Command.MJCommandTree;

public class MJItemEnchantSystemLoadManager implements MJCommand{
	private static MJItemEnchantSystemLoadManager _instance;
	public static MJItemEnchantSystemLoadManager getInstance(){
		if(_instance == null)
			_instance = new MJItemEnchantSystemLoadManager();
		return _instance;
	}

	public static void release(){
		if(_instance != null){
			_instance.dispose();
			_instance = null;
		}
	}

	public static void reload(){
		MJItemEnchantSystemLoadManager old = _instance;
		_instance = new MJItemEnchantSystemLoadManager();
		if(old != null){
			old.dispose();
			old = null;
		}
	}
	
	private final MJCommandTree _commands;
	private MJItemEnchantSystemLoadManager(){
		_commands = createCommand();
	}
	
	public void load(){
		MJItemEnchanterLoader.getInstance();
		MJItemEnchanteeLoader.getInstance();
	}

	private MJCommandTree createCommand(){
		return new MJCommandTree(".強化系統", "1(重載) ->(1)強化者/(2)被強化物品", null);
			._commands.add_command(createReloadCommand());
	}
	
	private MJCommandTree createReloadCommand(){
		return new MJCommandTree("1", "執行重載相關命令。", null);
		._commands.add_command(new MJCommandTree("1", "", null){
				@Override
				protected void to_handle_command(MJCommandArgs args) throws Exception{
					MJItemEnchanterLoader.reload();
					args.notify("已重新載入 tb_enchanters 資料表。");
				}
			})
			.add_command(new MJCommandTree("2", "", null){
				@Override
				protected void to_handle_command(MJCommandArgs args) throws Exception{
					MJItemEnchanteeLoader.reload();
					args.notify("已重新載入 tb_enchanting 資料表。");
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
