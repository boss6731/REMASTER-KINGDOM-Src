 package l1j.server.server.model.trap;

 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.storage.TrapStorage;
 import l1j.server.server.utils.Dice;




 public class L1DamageTrap
   extends L1Trap
 {
   private final Dice _dice;
   private final int _base;
   private final int _diceCount;

   public L1DamageTrap(TrapStorage storage) {
     super(storage);

     this._dice = new Dice(storage.getInt("dice"));
     this._base = storage.getInt("base");
     this._diceCount = storage.getInt("diceCount");
   }


   public void onTrod(L1PcInstance trodFrom, L1Object trapObj) {
     sendEffect(trapObj);

     int dmg = this._dice.roll(this._diceCount) + this._base;

     trodFrom.receiveDamage((L1Character)trodFrom, dmg);
   }
 }


