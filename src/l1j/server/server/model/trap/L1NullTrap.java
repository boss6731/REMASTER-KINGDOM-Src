 package l1j.server.server.model.trap;

 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Object;



 class L1NullTrap
   extends L1Trap
 {
   public L1NullTrap() {
     super(0, 0, false);
   }

   public void onTrod(L1PcInstance trodFrom, L1Object trapObj) {}
 }


