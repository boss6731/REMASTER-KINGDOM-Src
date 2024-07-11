package l1j.server.MJItemEnchantSystem;

import l1j.server.MJTemplate.Command.MJCommand;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.MJTemplate.Command.MJCommandTree;

public class MJItemEnchantSystemLoadManager implements MJCommand {
	private static MJItemEnchantSystemLoadManager _instance;

	public static MJItemEnchantSystemLoadManager getInstance() {
		if (_instance == null)
			_instance = new MJItemEnchantSystemLoadManager();
		return _instance;
	}

	public static void release() {
		if (_instance != null) {
			_instance.dispose();
			_instance = null;
		}
	}

	public static void reload() {
		MJItemEnchantSystemLoadManager old = _instance;
		_instance = new MJItemEnchantSystemLoadManager();
		if (old != null) {
			old.dispose();
			old = null;
		}
	}

	private MJCommandTree _commands;

	private MJItemEnchantSystemLoadManager() {
		_commands = createCommand();
	}

	public void load() {
		MJItemEnchanterLoader.getInstance();
		MJItemEnchanteeLoader.getInstance();
	}

	private MJCommandTree createCommand() {
		return new MJCommandTree(".強化系統", "1(重新載入) ->(1)強化師/(2)被強化物", null)
				.add_command(createReloadCommand());
	}

	private MJCommandTree createReloadCommand() {
		return new MJCommandTree("1", "執行重新載入相關命令。", null)
				.add_command(new MJCommandTree("1", "", null) {
					@override
					protected void to_handle_command(MJCommandArgs args) throws Exception {
						MJItemEnchanterLoader.reload();
						args.notify("已重新載入 tb_enchanters 表。");
					}
				})
				.add_command(new MJCommandTree("2", "", null) {
					@override
					protected void to_handle_command(MJCommandArgs args) throws Exception {
						MJItemEnchanteeLoader.reload();
						args.notify("已重新載入 tb_enchanties 表。");
					}
				});
	}

	public void dispose() {
	}

	@Override
	public void execute(MJCommandArgs args) {
		_commands.execute(args, new StringBuilder(256).append(_commands.to_operation()));
	}
}
