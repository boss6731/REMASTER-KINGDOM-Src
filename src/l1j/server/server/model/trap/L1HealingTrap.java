 package l1j.server.server.model.trap;

 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.storage.TrapStorage;
 import l1j.server.server.utils.Dice;

















 public class L1HealingTrap
   extends L1Trap
 {
   private final Dice _dice;
   private final int _base;
   private final int _diceCount;

   public L1HealingTrap(TrapStorage storage) {
     super(storage);

     this._dice = new Dice(storage.getInt("dice"));
     this._base = storage.getInt("base");
     this._diceCount = storage.getInt("diceCount");
   }


   public void onTrod(L1PcInstance trodFrom, L1Object trapObj) {
     sendEffect(trapObj);

     int pt = this._dice.roll(this._diceCount) + this._base;

     trodFrom.healHp(pt);
   }
 }


