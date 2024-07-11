 package MJNCoinSystem;

 import MJNCoinSystem.Commands.MJCoinCompleteDepositExecutor;
 import MJNCoinSystem.Commands.MJNCoinAdenaCommissionReporter;
 import MJNCoinSystem.Commands.MJNCoinAdenaCustomExecutor;
 import MJNCoinSystem.Commands.MJNCoinAdenaProviderCancel;
 import MJNCoinSystem.Commands.MJNCoinAdenaProviderExecutor;
 import MJNCoinSystem.Commands.MJNCoinCompleteRefundExecutor;
 import MJNCoinSystem.Commands.MJNCoinDepositExecutor;
 import MJNCoinSystem.Commands.MJNCoinExecutor;
 import MJNCoinSystem.Commands.MJNCoinRefundExecutor;
 import java.util.HashMap;
 import l1j.server.MJTemplate.Command.MJCommandArgs;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class MJNCoinCommandComposite
 {
   public static final MJNCoinCommandComposite DEFAULT = new MJNCoinCommandComposite();


   private HashMap<String, MJNCoinExecutor> m_executors;


   public void load_commands() {
     this.m_executors = new HashMap<>();
     generate_commands((MJNCoinExecutor)new MJNCoinAdenaCustomExecutor());
     generate_commands((MJNCoinExecutor)new MJNCoinAdenaProviderCancel());
     generate_commands((MJNCoinExecutor)new MJNCoinAdenaProviderExecutor());
     generate_commands((MJNCoinExecutor)new MJNCoinDepositExecutor());
     generate_commands((MJNCoinExecutor)new MJNCoinRefundExecutor());
     generate_commands((MJNCoinExecutor)new MJNCoinCompleteRefundExecutor());
     generate_commands((MJNCoinExecutor)new MJNCoinAdenaCommissionReporter());
     generate_commands((MJNCoinExecutor)new MJCoinCompleteDepositExecutor());
   }

   private void generate_commands(MJNCoinExecutor executor) {
     this.m_executors.put(executor.get_command_name(), executor);
   }

   public boolean execute(L1PcInstance pc, String command, String param) {
     MJNCoinExecutor executor = this.m_executors.get(command);
     if (executor != null) {
       executor.execute((new MJCommandArgs()).setOwner(pc).setParam(param));
       return true;
     }
     return false;
   }
 }


