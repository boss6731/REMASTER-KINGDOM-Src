 package l1j.server.server.model;

 import java.lang.reflect.Constructor;
 import l1j.server.server.IdFactory;
 import l1j.server.server.datatables.NpcTable;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.templates.L1Npc;






 public class L1WarSpawn
 {
     private static L1WarSpawn _instance;
     private Constructor<?> _constructor;

     public static L1WarSpawn getInstance() {
         if (_instance == null) {
             _instance = new L1WarSpawn();
         }
         return _instance;
     }

     public void SpawnTower(int castleId) {
         int npcId = 81111;
         if (castleId == 7) {
             npcId = 81189;
         }

         int gfxId = -1;
         if (castleId == 1) {
             gfxId = 17622;
         } else if (castleId == 2) {
             gfxId = 17619;
         } else if (castleId == 3) {
             gfxId = 10461;
         } else if (castleId == 4) {
             gfxId = 17615;
         } else if (castleId == 5) {
             gfxId = 10469;
         } else if (castleId == 6) {
             gfxId = 10473;
         } else if (castleId == 7) {
             gfxId = 17920;
         } else if (castleId == 8) {
             gfxId = 17622;
         }
         L1Npc l1npc = NpcTable.getInstance().getTemplate(npcId);

         int[] loc = new int[3];
         loc = L1CastleLocation.getTowerLoc(castleId);
         SpawnWarObject(l1npc, loc[0], loc[1], (short)loc[2], gfxId);
         if (castleId == 7) {
             spawnSubTower();
         }
     }


     private void spawnSubTower() {
         int[] loc = new int[3];
         for (int i = 1; i <= 4; i++) {
             L1Npc l1npc = NpcTable.getInstance().getTemplate(81189 + i);
             loc = L1CastleLocation.getSubTowerLoc(i);
             SpawnWarObject(l1npc, loc[0], loc[1], (short)loc[2]);
         }
     }

     public void SpawnCrown(int castleId) {
         L1Npc l1npc = NpcTable.getInstance().getTemplate(81125);
         int[] loc = new int[3];
         loc = L1CastleLocation.getTowerLoc(castleId);
         SpawnWarObject(l1npc, loc[0], loc[1], (short)loc[2]);
     }

     public void SpawnFlag(int castleId) {
         L1Npc l1npc = NpcTable.getInstance().getTemplate(81122);
         int[] loc = new int[5];
         loc = L1CastleLocation.getWarArea(castleId);
         int x = 0;
         int y = 0;
         int locx1 = loc[0];
         int locx2 = loc[1];
         int locy1 = loc[2];
         int locy2 = loc[3];
         short mapid = (short)loc[4];

         int gfxId = -1;
         if (castleId == 1) {
             gfxId = 10457;
         } else if (castleId == 2) {
             gfxId = 10453;
         } else if (castleId == 3) {
             gfxId = 10461;
         } else if (castleId == 4) {
             gfxId = 10477;
         } else if (castleId == 5) {
             gfxId = 10469;
         } else if (castleId == 6) {
             gfxId = 10473;
         } else if (castleId == 7) {
             gfxId = 10449;
         } else if (castleId == 8) {
             gfxId = 10457;
         }
         if (castleId >= 1 && castleId <= 8)
         {
             gfxId = 10477;
         }

         for (x = locx1, y = locy1; x <= locx2; x += 8) {
             SpawnWarObject(l1npc, x, y, mapid, gfxId);
         }
         for (x = locx2, y = locy1; y <= locy2; y += 8) {
             SpawnWarObject(l1npc, x, y, mapid, gfxId + 2);
         }
         for (x = locx2, y = locy2; x >= locx1; x -= 8) {
             SpawnWarObject(l1npc, x, y, mapid, gfxId);
         }
         for (x = locx1, y = locy2; y >= locy1; y -= 8) {
             SpawnWarObject(l1npc, x, y, mapid, gfxId + 2);
         }
     }

     private L1NpcInstance SpawnWarObject(L1Npc l1npc, int locx, int locy, short mapid) {
         return SpawnWarObject(l1npc, locx, locy, mapid, -1);
     }
     private L1NpcInstance SpawnWarObject(L1Npc l1npc, int locx, int locy, short mapid, int gfxId) {
         try {
             if (l1npc != null) {
                 String s = l1npc.getImpl();
                 this

                         ._constructor = Class.forName("l1j.server.server.model.Instance." + s + "Instance").getConstructors()[0];
                 Object[] aobj = { l1npc };
                 L1NpcInstance npc = (L1NpcInstance)this._constructor.newInstance(aobj);
                 npc.setId(IdFactory.getInstance().nextId());
                 npc.setX(locx);
                 npc.setY(locy);
                 npc.setHomeX(locx);
                 npc.setHomeY(locy);
                 npc.setHeading(0);
                 npc.setMap(mapid);
                 if (gfxId != -1) {
                     npc.setCurrentSprite(gfxId);
                 }
                 L1World.getInstance().storeObject((L1Object)npc);
                 L1World.getInstance().addVisibleObject((L1Object)npc);









                 return npc;
             }
         } catch (Exception exception) {}

         return null;
     }
 }


