 package l1j.server.server.model.Instance;

 import java.lang.reflect.Constructor;
 import l1j.server.server.IdFactory;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.datatables.NpcTable;
 import l1j.server.server.model.L1Attack;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_FollowerPack;
 import l1j.server.server.serverpackets.S_NPCTalkReturn;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.types.Point;

 public class L1FollowerInstance
   extends L1NpcInstance
 {
   private static final long serialVersionUID = 1L;

   public boolean noTarget() {
     L1NpcInstance npc = null;
     L1PcInstance pc = null;
     for (L1Object object : L1World.getInstance().getVisibleObjects((L1Object)this)) {
       if (object == null)
         continue;
       if (object instanceof L1NpcInstance) {
         npc = (L1NpcInstance)object;
         switch (npc.getNpcTemplate().get_npcId()) {
           case 70740:
           case 71093:
             setParalyzed(true);
             pc = (L1PcInstance)this._master;
             if (!pc.getInventory().checkItem(40593)) {
               createNewItem(pc, 40593, 1);
             }
             deleteMe();
             return true;
           case 71094:
             setParalyzed(true);
             pc = (L1PcInstance)this._master;
             if (!pc.getInventory().checkItem(40582)) {
               createNewItem(pc, 40582, 1);
             }
             deleteMe();
             return true;
           case 71061:
           case 71062:
             if (getLocation().getTileLineDistance((Point)this._master.getLocation()) < 3) {
               pc = (L1PcInstance)this._master;
               if (pc.getX() >= 32448 && pc.getX() <= 32452 && pc
                 .getY() >= 33048 && pc.getY() <= 33052 && pc.getMapId() == 440) {
                 setParalyzed(true);
                 if (!pc.getInventory().checkItem(40711)) {
                   createNewItem(pc, 40711, 1);
                   pc.getQuest().set_step(31, 3);
                 }
                 deleteMe();
                 return true;
               }
             }


           case 71074:
           case 71075:
             if (getLocation().getTileLineDistance((Point)this._master.getLocation()) < 3) {
               pc = (L1PcInstance)this._master;
               if (pc.getX() >= 32731 && pc.getX() <= 32735 && pc

                 .getY() >= 32854 && pc.getY() <= 32858 && pc.getMapId() == 480) {
                 setParalyzed(true);
                 if (!pc.getInventory().checkItem(40633)) {
                   createNewItem(pc, 40633, 1);
                   pc.getQuest().set_step(34, 2);
                 }
                 deleteMe();
                 return true;
               }
             }

           case 70957:
           case 70964:
             if (getLocation().getTileLineDistance((Point)this._master.getLocation()) < 3) {
               pc = (L1PcInstance)this._master;
               if (pc.getX() >= 32917 && pc.getX() <= 32921 && pc

                 .getY() >= 32974 && pc.getY() <= 32978 && pc.getMapId() == 410) {
                 setParalyzed(true);
                 createNewItem(pc, 41003, 1);
                 pc.getQuest().set_step(38, 0);
                 deleteMe();
                 return true;
               }
             }
         }


       }
     }
     if (this._master.isDead() || getLocation().getTileLineDistance((Point)this._master.getLocation()) > 10) {
       setParalyzed(true);
       spawn(getNpcTemplate().get_npcId(), getX(), getY(), getHeading(), getMapId());
       deleteMe();
       return true;
     }  if (this._master != null && this._master.getMapId() == getMapId() &&
       getLocation().getTileLineDistance((Point)this._master.getLocation()) > 2) {
       setSleepTime(setDirectionMoveSpeed(moveDirection(this._master.getMapId(), this._master.getX(), this._master.getY())));
     }

     return false;
   }

   public L1FollowerInstance(L1Npc template, L1NpcInstance target, L1Character master) {
     super(template);

     this._master = master;
     setId(IdFactory.getInstance().nextId());

     setMaster(master);
     setX(target.getX());
     setY(target.getY());
     setMap(target.getMapId());
     setHeading(target.getHeading());
     setLightSize(target.getLightSize());
     setMaxHp(500);
     setCurrentHp(500);

     target.setParalyzed(true);
     target.deleteMe();

     L1World.getInstance().storeObject((L1Object)this);
     L1World.getInstance().addVisibleObject((L1Object)this);
     for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer((L1Object)this)) {
       onPerceive(pc);
     }

     startAI();
     master.addFollower(this);
   }


   public synchronized void deleteMe() {
     this._master.removeFollower(this);
     getMap().setPassable((Point)getLocation(), true);
     super.deleteMe();
   }


   public void onAction(L1PcInstance pc) {
     L1Attack attack = new L1Attack(pc, this);
     if (attack.calcHit()) {
       attack.calcDamage();
       attack.addPcPoisonAttack(pc, this);
     }
     attack.action();
     attack.commit();
   }


   public void onTalkAction(L1PcInstance player) {
     if (isDead()) {
       return;
     }
     switch (getNpcTemplate().get_npcId()) {
       case 71093:
         if (this._master.equals(player)) {
           player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "searcherk2")); break;
         }
         player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "searcherk4"));
         break;

       case 71094:
         if (this._master.equals(player)) {
           player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "endiaq2")); break;
         }
         player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "endiaq4"));
         break;

       case 71062:
         if (this._master.equals(player)) {
           player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "kamit2")); break;
         }
         player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "kamit1"));
         break;

       case 71075:
         if (this._master.equals(player)) {
           player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "llizard2")); break;
         }
         player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "llizard1a"));
         break;

       case 70957:
         if (this._master.equals(player)) {
           player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "roi2")); break;
         }
         player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "roi2"));
         break;
     }
   }



   public void onPerceive(L1PcInstance perceivedFrom) {
     perceivedFrom.addKnownObject((L1Object)this);
     if (perceivedFrom.getAI() == null)
       perceivedFrom.sendPackets((ServerBasePacket)new S_FollowerPack(this, perceivedFrom));
   }

   private void createNewItem(L1PcInstance pc, int item_id, int count) {
     L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
     item.setCount(count);
     if (item != null) {
       if (pc.getInventory().checkAddItem(item, count) == 0) {
         pc.getInventory().storeItem(item);
       } else {
         L1World.getInstance().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(item);
       }
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(403, item.getLogName()));
     }
   }

   public void spawn(int npcId, int X, int Y, int H, short Map) {
     L1Npc l1npc = NpcTable.getInstance().getTemplate(npcId);
     if (l1npc != null) {
       L1NpcInstance mob = null;
       try {
         String implementationName = l1npc.getImpl();

         Constructor<?> _constructor = Class.forName("l1j.server.server.model.Instance. " + implementationName + "Instance").getConstructors()[0];
         mob = (L1NpcInstance)_constructor.newInstance(new Object[] { l1npc });
         mob.setId(IdFactory.getInstance().nextId());
         mob.setX(X);
         mob.setY(Y);
         mob.setHomeX(X);
         mob.setHomeY(Y);
         mob.setMap(Map);
         mob.setHeading(H);
         L1World.getInstance().storeObject((L1Object)mob);
         L1World.getInstance().addVisibleObject((L1Object)mob);
         L1Object object = L1World.getInstance().findObject(mob.getId());
         L1QuestInstance newnpc = (L1QuestInstance)object;
         newnpc.onNpcAI();
         newnpc.getLight().turnOnOffLight();
         newnpc.startChat(4);
         newnpc.startChat(0);
       } catch (Exception e) {
         e.printStackTrace();
       }
     }
   }
 }


