     package l1j.server.server.command.executor;

     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.L1Question;



     public class L1GMQuestion
       implements L1CommandExecutor
     {
       public static L1CommandExecutor getInstance() {
         return new L1GMQuestion();
       }


       public void execute(L1PcInstance pc, String cmdName, String arg) {
         try {
           if (L1Question.mainstart) {
             pc.sendPackets("目前正在進行一項調查。");
             pc.sendPackets("正在進行的調查詳情：" + L1Question.maintext);
           }
           L1Question.getInstance(arg);
         } catch (Exception e) {
           pc.sendPackets("請輸入.調查【調查詳情】");
         }
       }
     }


