package MJNCoinSystem;

import java.util.HashMap;

import MJNCoinSystem.Commands.MJCoinCompleteDepositExecutor;
import MJNCoinSystem.Commands.MJNCoinAdenaCommissionReporter;
import MJNCoinSystem.Commands.MJNCoinAdenaCustomExecutor;
import MJNCoinSystem.Commands.MJNCoinAdenaProviderCancel;
import MJNCoinSystem.Commands.MJNCoinAdenaProviderExecutor;
import MJNCoinSystem.Commands.MJNCoinCompleteRefundExecutor;
import MJNCoinSystem.Commands.MJNCoinDepositExecutor;
import MJNCoinSystem.Commands.MJNCoinExecutor;
import MJNCoinSystem.Commands.MJNCoinRefundExecutor;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJNCoinCommandComposite{
	public static final MJNCoinCommandComposite DEFAULT = new MJNCoinCommandComposite();
	
	private HashMap<String, MJNCoinExecutor> m_executors;
	private MJNCoinCommandComposite(){
	}
	
	public void load_commands(){
		m_executors = new HashMap<String, MJNCoinExecutor>();
		generate_commands(new MJNCoinAdenaCustomExecutor());
		generate_commands(new MJNCoinAdenaProviderCancel());
		generate_commands(new MJNCoinAdenaProviderExecutor());
		generate_commands(new MJNCoinDepositExecutor());
		generate_commands(new MJNCoinRefundExecutor());
		generate_commands(new MJNCoinCompleteRefundExecutor());
		generate_commands(new MJNCoinAdenaCommissionReporter());
		generate_commands(new MJCoinCompleteDepositExecutor());
	}
	
	private void generate_commands(MJNCoinExecutor executor){
		m_executors.put(executor.get_command_name(), executor);
	}
	
	public boolean execute(L1PcInstance pc, String command, String param) {
		MJNCoinExecutor executor = m_executors.get(command);
		if(executor != null){
			executor.execute(new MJCommandArgs().setOwner(pc).setParam(param));
			return true;
		}
		return false;
	}
}
