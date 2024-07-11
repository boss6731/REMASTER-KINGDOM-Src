 package l1j.server.server.model;

 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_CHANGE_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_INFO_NOTI;
 import l1j.server.server.RepeatTask;
 import l1j.server.server.datatables.EventTimeTable;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class EventAlramTick
   extends RepeatTask {
   private final L1PcInstance _pc;

   public EventAlramTick(L1PcInstance pc, long inteval, boolean onOff) {
     super(inteval);
     this._pc = pc;
   }


   public void execute() {
     try {
       EventTimeTable.getInstance().reload();
       SC_NOTIFICATION_CHANGE_NOTI.reload(this._pc);
       this._pc.sendPackets(SC_NOTIFICATION_INFO_NOTI.make_stream(this._pc, 0, false));
     }
     catch (Exception exception) {}
   }
 }


