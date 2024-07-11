 package l1j.server.server.templates;

 import l1j.server.Config;

 public class L1Command {
   private final String _name;
   private final int _level;
   private final String _executorClassName;

   public L1Command(String name, int level, String executorClassName) {
     this._name = name;
     this._level = Config.ServerAdSetting.GMCODE;
     this._executorClassName = executorClassName;
   }

   public String getName() {
     return this._name;
   }

   public int getLevel() {
     return this._level;
   }

   public String getExecutorClassName() {
     return this._executorClassName;
   }
 }


