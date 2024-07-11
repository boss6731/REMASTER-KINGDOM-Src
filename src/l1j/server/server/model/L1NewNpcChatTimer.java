 package l1j.server.server.model;

 import java.util.Iterator;
 import java.util.Random;
 import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
 import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.S_NpcChatPacket;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1NewNpcChat;


 public class L1NewNpcChatTimer
   implements Runnable
 {
   private final L1NpcInstance _npc;
   private final L1NewNpcChat _npcChat;
   private boolean _chatActive = false;
   private long _repeat;
   private int chatTiming;
   private int chatInterval;
   private boolean isShout;
   private boolean isWorldChat;
   private boolean isNormalChat;
   private String chatMent;
   private int chatMentChance;
   private Random random = new Random(System.nanoTime());

   public L1NewNpcChatTimer(L1NpcInstance npc, L1NewNpcChat npcChat) {
     this._npc = npc;
     this._npcChat = npcChat;
     this._repeat = 0L;
   }

   public L1NewNpcChatTimer(L1NpcInstance npc, L1NewNpcChat npcChat, long repeat) {
     this._npc = npc;
     this._npcChat = npcChat;
     this._repeat = repeat;
   }


   public void startChat() {
     if (this._npc == null || this._npcChat == null) {
       return;
     }

     if (this._npc.getHiddenStatus() != 0 || this._npc._destroyed) {
       return;
     }


     this.chatTiming = this._npcChat.getChatPosition();
     this.chatInterval = this._npcChat.getChatInterval();
     this.isShout = this._npcChat.isShout();
     this.isWorldChat = this._npcChat.isWorldChat();
     this.isNormalChat = this._npcChat.isNormalChat();
     this.chatMent = this._npcChat.getMent();
     this.chatMentChance = this._npcChat.getMentChance();

     GeneralThreadPool.getInstance().schedule(this, 300L);
   }

   public void run() {
     try {
       if (!this.chatMent.equals("")) {
         if (!this._chatActive) {
           this._chatActive = true;
           GeneralThreadPool.getInstance().schedule(this, this.chatInterval);
           return;
         }
         this._chatActive = false;
         String[] ment = (String[])MJArrangeParser.parsing(this.chatMent, ",", MJArrangeParseeFactory.createStringArrange()).result();
         int rnd = this.random.nextInt(ment.length);
         if (ment.length > 1) {
           chat(this._npc, this.chatTiming, ment[rnd], this.isNormalChat, this.isShout, this.isWorldChat);
         } else {
           chat(this._npc, this.chatTiming, this.chatMent, this.isNormalChat, this.isShout, this.isWorldChat);
         }
       }
       if (this._repeat > 0L) {
         GeneralThreadPool.getInstance().schedule(this, this._repeat);
       }
     } catch (Throwable e) {
       e.printStackTrace();
     }
   }


   private void chat(L1NpcInstance npc, int chatTiming, String chatId, boolean isNormal, boolean isShout, boolean isWorldChat) {
     if (chatTiming == 0 && (npc.isDead() || npc.STATUS_Escape)) {
       return;
     }
     if (chatTiming == 1 && !npc.isDead()) {
       return;
     }
     if (chatTiming == 2 && npc.isDead()) {
       return;
     }
     if (chatTiming == 4 && npc.isDead()) {
       return;
     }
     if (chatTiming == 5 && (npc.isDead() || !npc.STATUS_Escape)) {
       return;
     }

     if (chatTiming != 5 &&
       this.chatMentChance > 0) {
       int rnd = this.random.nextInt(100) + 1;
       if (rnd > this.chatMentChance) {
         return;
       }
     }




     if (chatTiming == 4) {
       npc.broadcastPacket((ServerBasePacket)new S_DoActionGFX(npc.getId(), 1));
     }

     if (isNormal) {
       npc.broadcastPacket((ServerBasePacket)new S_NpcChatPacket(npc, chatId, 4));
     }

     if (isShout) {
       npc.wideBroadcastPacket((ServerBasePacket)new S_NpcChatPacket(npc, chatId, 2));
     }

     if (isWorldChat) {
       Iterator<L1PcInstance> iterator = L1World.getInstance().getAllPlayers().iterator(); if (iterator.hasNext()) { L1PcInstance pc = iterator.next();
         if (pc != null)
           pc.sendPackets((ServerBasePacket)new S_NpcChatPacket(npc, chatId, 3));  }

     }
   }
 }


