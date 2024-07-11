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




 public class DominanceFloorLv7
   implements Runnable
 {
   private boolean _event = false;
   private int _npcid;
   private int _mapx;
   private int _mapy;
   private int _mapid;
   private boolean _isment;
   private String _ment;
   private boolean _effectuse;
   private int _effid;
   private boolean _END = false;
   private int stage = 1;

   private static final int EVENT = 1;

   private static final int END = 2;

   private boolean Running = false;
   private int Time = 3600;


   public DominanceFloorLv7(int bossid, int x, int y, int mapid, boolean mentuse, String ment, boolean effectuse, int effectid) {
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

       try { TimeCheck();
         switch (this.stage) {
           case 1:
             for (L1PcInstance pc : PcCK()) {
               pc.sendPackets((ServerBasePacket)new S_PacketBox(84, "\\fY$27482"));
             }
             spawn(this._mapx, this._mapy, (short)this._mapid, 0, this._npcid, 4);
             Thread.sleep(7000L);
             this.stage = 2;
           case 2:
             if (this._END == true) {
               for (L1PcInstance pc : PcCK()) {
                 pc.sendPackets((ServerBasePacket)new S_PacketBox(84, "\\fY$27490"));
               }
               Thread.sleep(7000L);
               for (L1PcInstance pc : PcCK()) {
                 pc.sendPackets((ServerBasePacket)new S_PacketBox(84, "\\fY$27491"));
               }
               this.Running = false;
               break;
             }
             Object_Check();
             break;
         }

          }
       catch (Exception exception)

       {
         try { Thread.sleep(1000L); }
         catch (Exception e)
         { e.printStackTrace(); }  } finally { try { Thread.sleep(1000L); } catch (Exception e) { e.printStackTrace(); }
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
         if (npc == this._npcid &&
           mob != null && mob.isDead() && !this._event) {
           this._event = true;
         }
       }
     }

     if (this._event == true) {
       this._event = false;
       this._END = true;
     }
   }

   private void reset() {
     L1NpcInstance l1NpcInstance = L1World.getInstance().findNpc(this._npcid);
     if (l1NpcInstance != null && l1NpcInstance instanceof L1NpcInstance && !(l1NpcInstance instanceof l1j.server.server.model.Instance.L1DollInstance)) {
       L1NpcInstance npc = l1NpcInstance;
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

       for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer((L1Object)npc)) {
         npc.onPerceive(pc);
         S_DoActionGFX gfx = new S_DoActionGFX(npc.getId(), actioncode);
         pc.sendPackets((ServerBasePacket)gfx);
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
     npc.getMap().setPassable(npc.getX(), npc.getY(), true);
     npc.deleteMe();
   }
 }


