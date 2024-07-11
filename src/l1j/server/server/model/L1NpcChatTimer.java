 package l1j.server.server.model;

 import java.util.Iterator;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_NpcChatPacket;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1NpcChat;
















 public class L1NpcChatTimer
   implements Runnable
 {
   private static final Logger _log = Logger.getLogger(L1NpcChatTimer.class
       .getName());

   private final L1NpcInstance _npc;

   private final L1NpcChat _npcChat;

   private int _currentChat = 1;

   private boolean _chatActive = false;
   private long _repeat;
   private int chatTiming;
   private int chatInterval;
   private boolean isShout;
   private boolean isWorldChat;
   private String chatId1;
   private String chatId2;
   private String chatId3;
   private String chatId4;
   private String chatId5;

   public L1NpcChatTimer(L1NpcInstance npc, L1NpcChat npcChat) {
     this._npc = npc;
     this._npcChat = npcChat;
     this._repeat = 0L;
   }

   public L1NpcChatTimer(L1NpcInstance npc, L1NpcChat npcChat, long repeat) {
     this._npc = npc;
     this._npcChat = npcChat;
     this._repeat = repeat;
   }


   public void startChat(long delay) {
     if (this._npc == null || this._npcChat == null) {
       return;
     }

     if (this._npc.getHiddenStatus() != 0 || this._npc._destroyed) {
       return;
     }


     this.chatTiming = this._npcChat.getChatTiming();
     this.chatInterval = this._npcChat.getChatInterval();
     this.isShout = this._npcChat.isShout();
     this.isWorldChat = this._npcChat.isWorldChat();
     this.chatId1 = this._npcChat.getChatId1();
     this.chatId2 = this._npcChat.getChatId2();
     this.chatId3 = this._npcChat.getChatId3();
     this.chatId4 = this._npcChat.getChatId4();
     this.chatId5 = this._npcChat.getChatId5();

     GeneralThreadPool.getInstance().schedule(this, delay);
   }







   public void run() {
     try {
       if (this._currentChat == 1) {
         if (!this.chatId1.equals("")) {
           if (!this._chatActive) {
             this._chatActive = true;
             GeneralThreadPool.getInstance().schedule(this, this.chatInterval);
             return;
           }
           this._chatActive = false;
           chat(this._npc, this.chatTiming, this.chatId1, this.isShout, this.isWorldChat);
         }

         this._currentChat++;
       }

       if (this._currentChat == 2) {
         if (!this.chatId2.equals("")) {
           if (!this._chatActive) {
             this._chatActive = true;
             GeneralThreadPool.getInstance().schedule(this, this.chatInterval);
             return;
           }
           this._chatActive = false;
           chat(this._npc, this.chatTiming, this.chatId2, this.isShout, this.isWorldChat);
         }

         this._currentChat++;
       }

       if (this._currentChat == 3) {
         if (!this.chatId3.equals("")) {
           if (!this._chatActive) {
             this._chatActive = true;
             GeneralThreadPool.getInstance().schedule(this, this.chatInterval);
             return;
           }
           this._chatActive = false;
           chat(this._npc, this.chatTiming, this.chatId3, this.isShout, this.isWorldChat);
         }

         this._currentChat++;
       }

       if (this._currentChat == 4) {
         if (!this.chatId4.equals("")) {
           if (!this._chatActive) {
             this._chatActive = true;
             GeneralThreadPool.getInstance().schedule(this, this.chatInterval);
             return;
           }
           this._chatActive = false;
           chat(this._npc, this.chatTiming, this.chatId4, this.isShout, this.isWorldChat);
         }

         this._currentChat++;
       }

       if (this._currentChat == 5 &&
         !this.chatId5.equals("")) {
         if (!this._chatActive) {
           this._chatActive = true;
           GeneralThreadPool.getInstance().schedule(this, this.chatInterval);
           return;
         }
         this._chatActive = false;
         chat(this._npc, this.chatTiming, this.chatId5, this.isShout, this.isWorldChat);
       }



       this._currentChat = 1;

       if (this._repeat > 0L) {
         GeneralThreadPool.getInstance().schedule(this, this._repeat);
       }
     } catch (Throwable e) {
       _log.log(Level.WARNING, e.getLocalizedMessage(), e);
     }
   }


   private void chat(L1NpcInstance npc, int chatTiming, String chatId, boolean isShout, boolean isWorldChat) {
     if (chatTiming == 0 && npc
       .isDead()) {
       return;
     }
     if (chatTiming == 1 && !npc.isDead()) {
       return;
     }
     if (chatTiming == 2 && npc.isDead()) {
       return;
     }





     if (!isShout) {
       npc.broadcastPacket((ServerBasePacket)new S_NpcChatPacket(npc, chatId, 4));
     } else {
       npc.wideBroadcastPacket((ServerBasePacket)new S_NpcChatPacket(npc, chatId, 2));
     }

     if (isWorldChat) {
       Iterator<L1PcInstance> iterator = L1World.getInstance().getAllPlayers().iterator(); if (iterator.hasNext()) { L1PcInstance pc = iterator.next();
         if (pc != null)
           pc.sendPackets((ServerBasePacket)new S_NpcChatPacket(npc, chatId, 3));  }

     }
   }
 }


