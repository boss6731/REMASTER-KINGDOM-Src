 package l1j.server.server.model.Instance;

 import java.util.Random;
 import l1j.server.server.serverpackets.S_AttackPacket;
 import l1j.server.server.serverpackets.S_SkillSound;
 import l1j.server.server.serverpackets.ServerBasePacket;



 public class L1MagicInstance
 {
   public void UseMagicAttacke(L1PcInstance player, L1MonsterInstance npc) {
     int targetobjid = npc.getId();
     int magicMob = npc.getAbility().getTotalInt();
     int rangeMonster = npc.getNpcTemplate().get_ranged();
     int mobDmg = npc.getLevel() + 6;
     int randomMobDmg = 0;
     int useMagic = 0;
     Random random = new Random(System.nanoTime());

     if (rangeMonster == 1) {


       npc.broadcastPacket((ServerBasePacket)new S_AttackPacket(player, targetobjid, 18), player);

     }
     else if (magicMob == 99) {

       useMagic = random.nextInt(100) + 1;
     }


     if (useMagic > 80) {
       int useMagicA = random.nextInt(100) + 1;
       if (useMagicA < 25) {


         npc.broadcastPacket((ServerBasePacket)new S_AttackPacket(player, targetobjid, 18), player);

         randomMobDmg = (random.nextInt(mobDmg) + 1) * 2;

         player.receiveDamage(npc, randomMobDmg, 0);
       } else if (useMagicA >= 25 && useMagicA < 50) {


         npc.broadcastPacket((ServerBasePacket)new S_AttackPacket(player, targetobjid, 19), player);

         randomMobDmg = (random.nextInt(mobDmg) + 1) * 3;

         player.receiveDamage(npc, randomMobDmg, 0);
       } else {
         int bDmg = 5;
         int mortalBlow = 0;

         if (mortalBlow != 0)
         {

           npc.broadcastPacket((ServerBasePacket)new S_SkillSound(player.getId(), mortalBlow));

           randomMobDmg = (random.nextInt(mobDmg) + 1) * bDmg;

           player.receiveDamage(npc, randomMobDmg, 0);
         }
         else
         {
           npc.broadcastPacket((ServerBasePacket)new S_AttackPacket(player, targetobjid, 30), player);

           randomMobDmg = (random.nextInt(mobDmg) + 1) * 2;

           player.receiveDamage(npc, randomMobDmg, 0);
         }

       }
     } else {

       npc.broadcastPacket((ServerBasePacket)new S_AttackPacket(player, targetobjid, 1), player);

       randomMobDmg = random.nextInt(mobDmg) + 1;

       player.receiveDamage(npc, randomMobDmg, 0);
     }
   }
 }


