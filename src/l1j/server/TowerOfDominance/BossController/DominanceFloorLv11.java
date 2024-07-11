 package l1j.server.TowerOfDominance.BossController;

 import java.util.ArrayList;
 import l1j.server.Config;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.IdFactory;
 import l1j.server.server.datatables.NpcTable;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_DisplayEffect;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.types.Point;




 public class DominanceFloorLv11
   implements Runnable
 {
   private boolean _event = false;
   private boolean _event2 = false;
   private boolean _event3 = false;
   private int _npcid;
   private int _mapx;
   private int _mapy;
   private int _mapid;
   private boolean _isment;
   private String _ment;
   private boolean _effectuse;
   private int _effid;
   private boolean _EVENT1 = false;
   private boolean _END = false;
   private int stage = 1;

   private static final int EVENT = 1;

   private static final int EVENT2 = 2;

   private static final int END = 3;
   private boolean Running = false;
   private int Time = 3600;


   public DominanceFloorLv11(int bossid, int x, int y, int mapid, boolean mentuse, String ment, boolean effectuse, int effectid) {
     this._npcid = bossid;
     this._mapx = x;
     this._mapy = y;
     this._mapid = mapid;
     this._isment = mentuse;
     this._ment = ment;
     this._effectuse = effectuse;
     this._effid = effectid;
   }

   public void setReady(boolean flag) {
     this.Running = flag;
   }

   public boolean isReady() {
     return this.Running;
   }


   public void run() {
     while (this.Running) {
       try {
         TimeCheck();
         switch (this.stage) {
           case 1:
             for (L1PcInstance pc : PcCK()) {
               if (pc.getX() >= 32721 && pc.getX() <= 32753 && pc.getY() >= 32874 && pc.getY() <= 32904) {
                 pc.start_teleport(32713, 32913, this._mapid, pc.getHeading(), 18339, true, false);
               }
             }
             spawn(32723, 32904, (short)this._mapid, 0, 8500132, 11);
             spawn(32723, 32903, (short)this._mapid, 0, 8500133, 11);
             spawn(32722, 32902, (short)this._mapid, 0, 8500134, 11);
             spawn(32721, 32902, (short)this._mapid, 0, 8500135, 11);
             spawn(32722, 32903, (short)this._mapid, 0, 8500301, 11);
             for (L1PcInstance pc : PcCK()) {
               pc.sendPackets((ServerBasePacket)new S_PacketBox(84, "\\fV$27679"));
             }
             spawn(32713, 32902, (short)this._mapid, 0, 8500130, 4);
             spawn(32722, 32913, (short)this._mapid, 0, 8500131, 4);
             Thread.sleep(7000L);
             for (L1PcInstance pc : PcCK()) {
               pc.sendPackets((ServerBasePacket)new S_PacketBox(84, "\\fY$27689"));
             }
             this.stage = 2;
           case 2:
             if (this._EVENT1 == true) {
               Thread.sleep(15000L);
               for (L1PcInstance pc : PcCK()) {
                 pc.sendPackets((ServerBasePacket)new S_PacketBox(84, "\\fY$27699"));
               }
               Thread.sleep(3000L);
               spawn(this._mapx, this._mapy, (short)this._mapid, 0, 8500137, 4);
               L1NpcInstance l1NpcInstance = L1World.getInstance().findNpc(8500137);
               if (l1NpcInstance != null && l1NpcInstance instanceof L1NpcInstance && !(l1NpcInstance instanceof l1j.server.server.model.Instance.L1DollInstance)) {
                 L1NpcInstance npc = l1NpcInstance;
                 deleteNpc(npc);
               }
               Thread.sleep(10000L);
               spawn(this._mapx, this._mapy, (short)this._mapid, 0, this._npcid, 0);
               this.stage = 3;
               break;
             }
             Object_Check();
             break;
           case 3:
             if (this._END == true) {
               for (L1PcInstance pc : PcCK()) {
                 pc.sendPackets((ServerBasePacket)new S_PacketBox(84, "\\fY$27707"));
               }
               Thread.sleep(7000L);
               for (L1PcInstance pc : PcCK()) {
                 pc.sendPackets((ServerBasePacket)new S_PacketBox(84, "\\fY$27708"));
               }
               this.Running = false;
               break;
             }
             Object_Check();
             break;
         }


       } catch (Exception e) {
         e.printStackTrace();
       } finally {
         try {
           Thread.sleep(1000L);
         } catch (Exception e) {
           e.printStackTrace();
         }
       }
     }
     reset();
   }

   public void Start() {
     GeneralThreadPool.getInstance().schedule(this, 5000L);
     reset();
     setReady(true);
     if (!Config.Login.StandbyServer) {
       if (this._isment) {
         L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_SystemMessage(this._ment));
         L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_PacketBox(84, this._ment));
       }

       S_DisplayEffect effect = null;
       if (this._effectuse) {
         if (this._effid > 0)
           effect = S_DisplayEffect.newInstance(this._effid);
         for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
           if (pc.isPrivateShop() || !pc.is_world())
             continue;
           if (effect != null)
             pc.sendPackets((ServerBasePacket)effect, false);
         }
         if (effect != null)
           effect.clear();
       }
     }
   }

   private void TimeCheck() {
     if (this.Time > 0) {
       this.Time--;
     }
     if (this.Time == 0) {
       this.Running = false;
     }
   }

   private void Object_Check() {
     L1NpcInstance mob = null;
     for (L1Object object : L1World.getInstance().getVisibleObjects(this._mapid).values()) {
       if (object instanceof l1j.server.server.model.Instance.L1MonsterInstance) {
         mob = (L1NpcInstance)object;
         int npc = mob.getNpcTemplate().get_npcId();
         switch (npc) {
           case 8500130:
             if (mob != null && mob.isDead() && !this._event) {
               for (L1PcInstance pc : PcCK()) {
                 pc.sendPackets((ServerBasePacket)new S_PacketBox(84, "\\fV$27688"));
               }
               this._event = true;
             }
             break;
           case 8500131:
             if (mob != null && mob.isDead() && !this._event2) {
               for (L1PcInstance pc : PcCK()) {
                 pc.sendPackets((ServerBasePacket)new S_PacketBox(84, "\\fY$27698"));
               }
               this._event2 = true;
             }
             break;
         }


         if (npc == this._npcid &&
           mob != null && mob.isDead() && !this._event3) {
           this._event3 = true;
         }
       }
     }

     if (this._event == true && this._event2 == true) {
       L1NpcInstance l1NpcInstance1 = L1World.getInstance().findNpc(8500301);
       if (l1NpcInstance1 != null && l1NpcInstance1 instanceof L1NpcInstance && !(l1NpcInstance1 instanceof l1j.server.server.model.Instance.L1DollInstance)) {
         L1NpcInstance npc = l1NpcInstance1;
         deleteNpc(npc);
       }
       L1NpcInstance l1NpcInstance2 = L1World.getInstance().findNpc(8500132);
       if (l1NpcInstance2 != null && l1NpcInstance2 instanceof L1NpcInstance && !(l1NpcInstance2 instanceof l1j.server.server.model.Instance.L1DollInstance)) {
         L1NpcInstance npc = l1NpcInstance2;
         deleteNpc(npc);
       }
       L1NpcInstance l1NpcInstance3 = L1World.getInstance().findNpc(8500133);
       if (l1NpcInstance3 != null && l1NpcInstance3 instanceof L1NpcInstance && !(l1NpcInstance3 instanceof l1j.server.server.model.Instance.L1DollInstance)) {
         L1NpcInstance npc = l1NpcInstance3;
         deleteNpc(npc);
       }
       L1NpcInstance l1NpcInstance4 = L1World.getInstance().findNpc(8500134);
       if (l1NpcInstance4 != null && l1NpcInstance4 instanceof L1NpcInstance && !(l1NpcInstance4 instanceof l1j.server.server.model.Instance.L1DollInstance)) {
         L1NpcInstance npc = l1NpcInstance4;
         deleteNpc(npc);
       }
       L1NpcInstance l1NpcInstance5 = L1World.getInstance().findNpc(8500135);
       if (l1NpcInstance5 != null && l1NpcInstance5 instanceof L1NpcInstance && !(l1NpcInstance5 instanceof l1j.server.server.model.Instance.L1DollInstance)) {
         L1NpcInstance npc = l1NpcInstance5;
         deleteNpc(npc);
       }
       this._EVENT1 = true;
     }
     if (this._event3 == true) {
       this._event3 = false;
       this._END = true;
     }
   }

   private void reset() {
     L1NpcInstance l1NpcInstance1 = L1World.getInstance().findNpc(8500301);
     if (l1NpcInstance1 != null && l1NpcInstance1 instanceof L1NpcInstance && !(l1NpcInstance1 instanceof l1j.server.server.model.Instance.L1DollInstance)) {
       L1NpcInstance npc = l1NpcInstance1;
       deleteNpc(npc);
     }
     L1NpcInstance l1NpcInstance2 = L1World.getInstance().findNpc(8500130);
     if (l1NpcInstance2 != null && l1NpcInstance2 instanceof L1NpcInstance && !(l1NpcInstance2 instanceof l1j.server.server.model.Instance.L1DollInstance)) {
       L1NpcInstance npc = l1NpcInstance2;
       deleteNpc(npc);
     }
     L1NpcInstance l1NpcInstance3 = L1World.getInstance().findNpc(8500131);
     if (l1NpcInstance3 != null && l1NpcInstance3 instanceof L1NpcInstance && !(l1NpcInstance3 instanceof l1j.server.server.model.Instance.L1DollInstance)) {
       L1NpcInstance npc = l1NpcInstance3;
       deleteNpc(npc);
     }
     L1NpcInstance l1NpcInstance4 = L1World.getInstance().findNpc(8500132);
     if (l1NpcInstance4 != null && l1NpcInstance4 instanceof L1NpcInstance && !(l1NpcInstance4 instanceof l1j.server.server.model.Instance.L1DollInstance)) {
       L1NpcInstance npc = l1NpcInstance4;
       deleteNpc(npc);
     }
     L1NpcInstance l1NpcInstance5 = L1World.getInstance().findNpc(8500133);
     if (l1NpcInstance5 != null && l1NpcInstance5 instanceof L1NpcInstance && !(l1NpcInstance5 instanceof l1j.server.server.model.Instance.L1DollInstance)) {
       L1NpcInstance npc = l1NpcInstance5;
       deleteNpc(npc);
     }
     L1NpcInstance l1NpcInstance6 = L1World.getInstance().findNpc(8500134);
     if (l1NpcInstance6 != null && l1NpcInstance6 instanceof L1NpcInstance && !(l1NpcInstance6 instanceof l1j.server.server.model.Instance.L1DollInstance)) {
       L1NpcInstance npc = l1NpcInstance6;
       deleteNpc(npc);
     }
     L1NpcInstance l1NpcInstance7 = L1World.getInstance().findNpc(8500135);
     if (l1NpcInstance7 != null && l1NpcInstance7 instanceof L1NpcInstance && !(l1NpcInstance7 instanceof l1j.server.server.model.Instance.L1DollInstance)) {
       L1NpcInstance npc = l1NpcInstance7;
       deleteNpc(npc);
     }
     L1NpcInstance l1NpcInstance8 = L1World.getInstance().findNpc(this._npcid);
     if (l1NpcInstance8 != null && l1NpcInstance8 instanceof L1NpcInstance && !(l1NpcInstance8 instanceof l1j.server.server.model.Instance.L1DollInstance)) {
       L1NpcInstance npc = l1NpcInstance8;
       deleteNpc(npc);
     }
   }

   public ArrayList<L1PcInstance> PcCK() {
     ArrayList<L1PcInstance> _pc = new ArrayList<>();
     for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
       if (pc.getMapId() == this._mapid)
         _pc.add(pc);
     }
     return _pc;
   }

   private static void spawn(int x, int y, short MapId, int Heading, int npcId, int actioncode) {
     try {
       L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
       npc.setId(IdFactory.getInstance().nextId());
       npc.setMap(MapId);
       int tryCount = 0;
       do {
         tryCount++;
         npc.setX(x);
         npc.setY(y);
         if (npc.getMap().isInMap((Point)npc.getLocation()) && npc.getMap().isPassable((Point)npc.getLocation())) {
           break;
         }
         Thread.sleep(1L);
       } while (tryCount < 50);
       if (tryCount >= 50) {
         npc.getLocation().forward(Heading);
       }
       npc.setHomeX(npc.getX());
       npc.setHomeY(npc.getY());
       npc.setHeading(Heading);
       if (npcId == 8500301 || (npcId >= 8500132 && npcId <= 8500135)) {
         npc.setPassObject(false);
       }

       for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer((L1Object)npc)) {
         npc.onPerceive(pc);
         S_DoActionGFX gfx = new S_DoActionGFX(npc.getId(), actioncode);
         pc.sendPackets((ServerBasePacket)gfx, false);
       }

       L1World.getInstance().storeObject((L1Object)npc);
       L1World.getInstance().addVisibleObject((L1Object)npc);

       npc.getLight().turnOnOffLight();
       npc.startChat(4);
       npc.startChat(0);
     }
     catch (Exception e) {
       e.printStackTrace();
     }
   }

   private void deleteNpc(L1NpcInstance npc) {
     if (npc.getNpcId() == 8500301) {
       for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer((L1Object)npc)) {
         npc.onPerceive(pc);
         S_DoActionGFX gfx = new S_DoActionGFX(npc.getId(), 28);
         pc.sendPackets((ServerBasePacket)gfx, false);
       }
     }
     npc.getMap().setPassable(npc.getX(), npc.getY(), true);
     npc.deleteMe();
   }
 }


