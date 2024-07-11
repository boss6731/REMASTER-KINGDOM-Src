 package l1j.server.server.model.trap;

 import l1j.server.server.datatables.MonsterParalyzeDelay;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.poison.L1DamagePoison;
 import l1j.server.server.model.poison.L1ParalysisPoison;
 import l1j.server.server.model.poison.L1SilencePoison;
 import l1j.server.server.storage.TrapStorage;

 public class L1PoisonTrap extends L1Trap {
   private final String _type;
   private final int _delay;
   private final int _time;
   private final int _damage;

   public L1PoisonTrap(TrapStorage storage) {
     super(storage);

     this._type = storage.getString("poisonType");
     this._delay = storage.getInt("poisonDelay");
     this._time = storage.getInt("poisonTime");
     this._damage = storage.getInt("poisonDamage");
   }


   public void onTrod(L1PcInstance trodFrom, L1Object trapObj) {
     sendEffect(trapObj);

     if (this._type.equals("d")) {
       if (trodFrom.getZoneType() != 1) {
         L1DamagePoison.doInfection((L1Character)trodFrom, (L1Character)trodFrom, this._time, this._damage, false);
       }
     } else if (this._type.equals("s")) {
       L1SilencePoison.doInfection((L1Character)trodFrom);
     } else if (this._type.equals("p")) {
       MonsterParalyzeDelay.MonsterParalyze paralyze = new MonsterParalyzeDelay.MonsterParalyze();
       paralyze.paralyze_delay = this._delay;
       paralyze.paralyze_millis = this._time;
       paralyze.skill_id = 0;
       L1ParalysisPoison.doInfection((L1Character)trodFrom, paralyze);
     }
   }
 }


