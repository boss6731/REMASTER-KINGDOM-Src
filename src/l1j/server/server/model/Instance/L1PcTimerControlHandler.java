 package l1j.server.server.model.Instance;

 import java.util.HashMap;
 import java.util.Timer;
 import java.util.TimerTask;
 import l1j.server.MJPassiveSkill.MJPassiveID;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Spell.SC_SPELL_PASSIVE_ONOFF_ACK;
 import l1j.server.server.datatables.SkillsTable;
 import l1j.server.server.model.L1World;


 public class L1PcTimerControlHandler
 {
   private static L1PcTimerControlHandler _instance;

   public enum TimerType
   {
     BLOOD_TO_SOUL, ASURA, AURA;
   }

   public static L1PcTimerControlHandler getInstance() {
     if (_instance == null) {
       _instance = new L1PcTimerControlHandler();
     }
     return _instance;
   }

   private HashMap<String, Timer> _timerList = new HashMap<>();





   public void begin(String name, TimerType type) {
     stop(name);
     Timer t = new Timer(true);
     int BLOOD_TO_SOUL_TIME = SkillsTable.getInstance().getTemplate(146).getProbabilityValue();
     int ASURA_TIME = SkillsTable.getInstance().getTemplate(5017).getProbabilityValue();
     if (type.equals(TimerType.BLOOD_TO_SOUL)) {
       t.scheduleAtFixedRate(new BloodToSoul(name), BLOOD_TO_SOUL_TIME, BLOOD_TO_SOUL_TIME);
     } else if (type.equals(TimerType.ASURA)) {
       t.scheduleAtFixedRate(new L1Asura(name), ASURA_TIME, ASURA_TIME);
     } else if (type.equals(TimerType.AURA)) {

     }
     this._timerList.put(name, t);
   }

   public void stop(String name) {
     Timer t = this._timerList.get(name);
     if (t != null) {
       t.cancel();
       t = null;
       this._timerList.remove(name);
     }
   }

   private class BloodToSoul
     extends TimerTask {
     private String _name;

     public BloodToSoul(String name) {
       this._name = name;
     }


     public void run() {
       try {
         int BLOODY_SOUL_HP = SkillsTable.getInstance().getTemplate(146).getHpConsume();
         int BLOODY_SOUL_MP = SkillsTable.getInstance().getTemplate(146).getMpConsume();
         int BLOODY_SOUL_EFFECT = SkillsTable.getInstance().getTemplate(146).getCastGfx();
         int BLOODY_SOUL_ITEM = SkillsTable.getInstance().getTemplate(146).getItemConsumeId();
         int BLOODY_SOUL_ITEM_COUNT = SkillsTable.getInstance().getTemplate(146).getItemConsumeCount();
         L1PcInstance pc = L1World.getInstance().getPlayer(this._name);
         if (pc == null || pc.isDead()) {
           L1PcTimerControlHandler.this.stop(this._name);
           SC_SPELL_PASSIVE_ONOFF_ACK.send(pc, MJPassiveID.BLOODY_SOUL_NEW.toInt(), false);
           return;
         }
         int curHp = pc.getCurrentHp();
         int curMp = pc.getCurrentMp();

         if (curHp <= 25) {
           pc.sendPackets(279);

           return;
         }
         if (pc.getMaxMp() == curMp) {
           return;
         }

         if (!pc.getInventory().consumeItem(BLOODY_SOUL_ITEM, BLOODY_SOUL_ITEM_COUNT) && !pc.getInventory().consumeItem(30078, 1)) {
           pc.sendPackets(299);

           return;
         }
         pc.setCurrentHp(curHp - BLOODY_SOUL_HP);
         pc.setCurrentMp(curMp + BLOODY_SOUL_MP);
         pc.send_effect(BLOODY_SOUL_EFFECT);
       } catch (Exception e) {
         e.printStackTrace();
       }
     }
   }

   private class L1Asura extends TimerTask {
     private int _limiteCount = 0;
     private String _name;

     public L1Asura(String name) {
       this._name = name;
     }






     public void run() {
       try {
         int ASURA_TIME_MP_COUNT = SkillsTable.getInstance().getTemplate(5017).getProbabilityDice();
         int ASURA_TIME_MP_HEL = SkillsTable.getInstance().getTemplate(5017).getDamageDiceCount();
         int ASURA_EFFECT = SkillsTable.getInstance().getTemplate(5017).getCastGfx();
         L1PcInstance pc = L1World.getInstance().getPlayer(this._name);
         if (pc == null || pc.isDead() || this._limiteCount >= ASURA_TIME_MP_COUNT) {
           L1PcTimerControlHandler.this.stop(this._name);

           return;
         }

         pc.setCurrentMp(pc.getCurrentMp() + ASURA_TIME_MP_HEL);
         pc.send_effect(ASURA_EFFECT);
         this._limiteCount++;
       } catch (Exception e) {
         e.printStackTrace();
       }
     }
   }
 }


