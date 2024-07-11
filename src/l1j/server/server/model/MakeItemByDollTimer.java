 package l1j.server.server.model;

 import java.util.TimerTask;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1DollInstance;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class MakeItemByDollTimer extends TimerTask {
   private static Logger _log = Logger.getLogger(MakeItemByDollTimer.class.getName());
   private final L1PcInstance _pc;
   private final L1DollInstance _doll;

   public MakeItemByDollTimer(L1PcInstance pc, L1DollInstance doll) {
     this._pc = pc;
     this._doll = doll;
   }


   public void run() {
     try {
       if (this._pc == null) {
         if (this._doll != null) {
           this._doll.deleteDoll();
         }
         return;
       }
       if (this._pc.isDead()) {
         if (this._doll != null) {
           this._doll.deleteDoll();
         }
         return;
       }
       makeItem();
     } catch (Throwable e) {
       _log.log(Level.WARNING, e.getLocalizedMessage(), e);
     }
   }

   public void makeItem() {
     L1ItemInstance temp = ItemTable.getInstance().createItem(this._doll.getDoll().getEffect().getMakeItemId());
     if (temp != null && this._pc.getInventory().checkAddItem(temp, 1) == 0) {
       L1ItemInstance item = this._pc.getInventory().storeItem(temp.getItemId(), 1);

       this._pc.sendPackets((ServerBasePacket)new S_ServerMessage(403, item.getItem().getName()));
     }
   }
 }


