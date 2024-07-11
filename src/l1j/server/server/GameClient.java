     package l1j.server.server;
     import MJFX.MJFxEntry;
     import MJShiftObject.MJEShiftObjectType;
     import MJShiftObject.Object.MJShiftObject;
     import io.netty.channel.Channel;
     import io.netty.channel.ChannelFuture;
     import io.netty.channel.ChannelFutureListener;
     import io.netty.util.concurrent.Future;
     import io.netty.util.concurrent.GenericFutureListener;
     import java.net.InetAddress;
     import java.net.InetSocketAddress;
     import java.util.Collection;
     import java.util.StringTokenizer;
     import java.util.Timer;
     import java.util.TimerTask;
     import java.util.concurrent.ArrayBlockingQueue;
     import java.util.concurrent.BlockingQueue;
     import java.util.concurrent.LinkedBlockingQueue;
     import java.util.concurrent.TimeUnit;
     import l1j.server.Config;
     import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
     import l1j.server.MJNetSafeSystem.MJNetSafeLoadManager;
     import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
     import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
     import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
     import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_USER_DATA_EXTEND;
     import l1j.server.MJTemplate.MJString;
     import l1j.server.MJWebServer.Dispatcher.Login.MJHttpLoginInfo;
     import l1j.server.server.Controller.LoginController;
     import l1j.server.server.model.Instance.L1DollInstance;
     import l1j.server.server.model.Instance.L1FollowerInstance;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.Instance.L1PetInstance;
     import l1j.server.server.model.Instance.L1SummonInstance;
     import l1j.server.server.model.L1Trade;
     import l1j.server.server.serverpackets.S_CommonNews;
     import l1j.server.server.serverpackets.S_Disconnect;
     import l1j.server.server.serverpackets.S_ServerMessage;
     import l1j.server.server.serverpackets.S_SystemMessage;
     import l1j.server.server.serverpackets.ServerBasePacket;

     public class GameClient {
       private static Timer _observerTimer = new Timer();

       public boolean isAuthPass;

       public int[] charStat;

       private ClientThreadObserver observer;


       private Account account;


       private Channel chnnel;


       private L1PcInstance activeCharInstance;


       private L1PcInstance latestCharacterInstance;


       private boolean close;

       private int chatCount;

       private SendBusiness _business;

       private SendBusinessBlock _businessb;

       private InetAddress _inetAddress;

       private boolean _isLoginRecord;

       private boolean _isUpdate;

       private MJClientStatus _status;

       private long _version;

       private MJHttpLoginInfo m_login_info;

       private String m_latest_accounts;

       private MJEShiftObjectType m_shift_type;

       private String m_server_identity;

       private String m_server_description;

       private int m_second_password_failure_count;

       private MJShiftObject m_shift_object;

       private long latestRestartMillis;

       private String _auth_token;




       public void latestCharacterInstance(L1PcInstance pc) {
         this.latestCharacterInstance = pc;
       }


       public L1PcInstance latestCharacterInstance() {
         return this.latestCharacterInstance;
       }


       private class ChannelRemoveListener
         implements ChannelFutureListener
       {
         private ChannelRemoveListener() {}


         public void operationComplete(ChannelFuture future) throws Exception {
           if (GameClient.this.chnnel == null) {
             return;
           }
           GameClient.this.chnnel.closeFuture().removeListener((GenericFutureListener)this);
           GameClient.this.close();
           if (MJString.isNullOrEmpty(GameClient.this.m_latest_accounts)) {
             LoginController.getInstance().removeClientByAccounts(GameClient.this.m_latest_accounts);
           }
         }
       }

       public void set_login_info(MJHttpLoginInfo login_info) {
         this.m_login_info = login_info;
       }


       public MJHttpLoginInfo get_login_info() {
         return this.m_login_info;
       }

       public Channel getChannel() {
         return this.chnnel;
       }


       public void set_version(long version) {
         this._version = version;
       }

       public long get_version() {
         return this._version;
       }


       public void get_client_close(GameClient gc) {
         (new S_CommonNews()).UpDate(gc.getAccountName(), "0");
       }

       public void packetwaitgo(byte[] bb) {
         if (bb == null) {
           return;
         }
         try {
           this._status.process(this, bb);
         } catch (Exception e) {
           e.printStackTrace();
         }
       }

       public void kick() {
         sendPacket((ServerBasePacket)new S_Disconnect());
         try {
           close();
         } catch (Exception e) {
           e.printStackTrace();
         }
       }



       public void close(long delayMillis) {
         GeneralThreadPool.getInstance().schedule(new Runnable()
             {
               public void run() {
                 try {
                   GameClient.this.close();
                 } catch (Exception e) {
                   e.printStackTrace();
                 }
               }
             },  delayMillis);
       }



       public void close() throws Exception {
         if (!this.close) {
           this.close = true;
           try {
             if (this.activeCharInstance != null) {
               quitGame(this.activeCharInstance);
               synchronized (this.activeCharInstance) {
                 if (!this.activeCharInstance.isPrivateShop()) {
                   this.activeCharInstance.logout();
                 }
                 setActiveChar(null);
               }
             }
           } catch (Exception e) {
             e.printStackTrace();
           }
           try {
             LoginController.getInstance().logout(this);
             stopObsever();
           } catch (Exception e) {
             e.printStackTrace();
           }
           try {
             if (this.chnnel != null) {
               this.chnnel.close();
             }
           } catch (Exception exception) {}
         }
       }


       public void setActiveChar(L1PcInstance pc) {
         this.activeCharInstance = pc;
       }

       public L1PcInstance getActiveChar() {
         return this.activeCharInstance;
       }

       public void setAccount(Account account) {
         this.account = account;
         if (account != null) {
           this.m_latest_accounts = account.getName();
         }
       }


       public Account getAccount() {
         return this.account;
       }



       public String getAccountName() {
         if (this.account == null) {
           return null;
         }
         String name = this.account.getName();
         return name;
       }


       public static void quitGame(L1PcInstance pc) {
         pc.remove_companion();
         if (pc.getTradeID() != 0) {
           L1Trade trade = new L1Trade();
           trade.TradeCancel(pc);
         }
         if (pc.isInParty()) {
           pc.getParty().leaveMember(pc);
         }
         if (pc.isInChatParty()) {
           pc.getChatParty().leaveMember(pc);
         }
         Object[] petList = pc.getPetList().values().toArray();
         for (Object petObject : petList) {
           if (petObject instanceof L1PetInstance) {
             L1PetInstance pet = (L1PetInstance)petObject;
             pet.unloadMaster();
           }
           if (petObject instanceof L1SummonInstance) {
             L1SummonInstance summon = (L1SummonInstance)petObject;
             summon.onLeaveMaster();
           }
         }
         L1DollInstance doll = pc.getMagicDoll();
         if (doll != null) {
           doll.deleteDoll();
         }
         Object[] followerList = pc.getFollowerList().values().toArray();
         for (Object followerObject : followerList) {
           L1FollowerInstance follower = (L1FollowerInstance)followerObject;
           follower.setParalyzed(true);
           follower.spawn(follower.getNpcTemplate().get_npcId(), follower.getX(), follower.getY(), follower.getHeading(), follower.getMapId());
           follower.deleteMe();
         }
         pc.stopEtcMonitor();
         pc.setOnlineStatus(0);
         try {
           pc.save();
           pc.saveInventory();
         } catch (Exception e) {
           e.printStackTrace();
         }
         try {
           SC_ATTENDANCE_USER_DATA_EXTEND userData = pc.getAttendanceData();
           if (userData != null) {
             SC_ATTENDANCE_USER_DATA_EXTEND.update(pc.getAccountName(), userData);
             pc.setAttendanceData(null);
           }
         } catch (Exception e) {
           e.printStackTrace();
         }
       }



       public String getIp() {
         if (this._inetAddress == null) {
           return null;
         }
         return this._inetAddress.getHostAddress();
       }



       public int getIpBigEndian() {
         if (MJString.isNullOrEmpty(getIp())) {
           return 0;
         }
         int bigendian = 0;
         StringTokenizer tok = new StringTokenizer(getIp());
         for (int i = 3; i >= 0; i--) {
           int bit = i * 8;
           bigendian |= Integer.parseInt(tok.nextToken(".")) << bit & 255 << bit;
         }
         return bigendian;
       }

       public String getHostname() {
         if (this._inetAddress == null) {
           return null;
         }
         return this._inetAddress.getHostAddress();
       }


       public InetAddress getAddress() {
         return this._inetAddress;
       }


       public boolean isConnected() {
         return (this.chnnel != null && this.chnnel.isActive());
       }


           public void stopObsever() {
         this.observer.cancel();
       }


       public boolean isClosed() {
         if (!this.chnnel.isActive()) {
           return true;
         }
         return false;
       }


       class ClientThreadObserver
         extends TimerTask
       {
         private int _checkct = 1;


         private final int _disconnectTimeMillis;


         public ClientThreadObserver(int disconnectTimeMillis) {
           this._disconnectTimeMillis = disconnectTimeMillis;
         }



         public void start() {
           GameClient._observerTimer.scheduleAtFixedRate(this, this._disconnectTimeMillis, this._disconnectTimeMillis);
         }



           public void run() {
               try {
                    // 將 NS_CLIENT_VERSION 轉換為無符號整數並解析為長整數
                   long ns_client_version = Long.parseLong(Integer.toUnsignedString(MJNetSafeLoadManager.NS_CLIENT_VERSION));

                    // 如果客戶端版本與 ns_client_version 不匹配
                   if (GameClient.this._version != ns_client_version) {
                    // 發送伺服器消息 3362
                       GameClient.this.sendPacket((ServerBasePacket)new S_ServerMessage(3362));

                    // 發送系統消息，通知玩家伺服器版本已更新，並請求重新啟動客戶端並重新下載補丁
                       GameClient.this.sendPacket((ServerBasePacket)new S_SystemMessage("伺服器版本已更新，並請求重新啟動客戶端並重新下載補丁."));
                   }
               } // 刪除不必要的右大括號
             if (MJFxEntry.IS_DEBUG_MODE) {
               return;
             }
             if (!GameClient.this.chnnel.isActive()) {
               cancel();
               return;
             }
             if (this._checkct > 0) {
               this._checkct = 0;
               return;
             }
             if (GameClient.this.getStatus().toInt() != MJClientStatus.CLNT_STS_ENTERWORLD.toInt()) {
               GameClient.this.close();
             }
           } catch (Exception e) {
             e.printStackTrace();
             cancel();
           }
         }

         public void packetReceived() {
           this._checkct++;
         }
       }



       public void handle(byte[] data) {
         this.observer.packetReceived();
         this._status.process(this, data);
       }


       public void sendPacketNonClear(ServerBasePacket bp) {
         if (Config.Connection.SendBusiness) {
           if (this._businessb == null) {
             this._businessb = new SendBusinessBlock();
             GeneralThreadPool.getInstance().execute(this._businessb);
           }
           this._businessb.in(bp.getBytes());
         } else {
           if (this._business == null) {
             this._business = new SendBusiness();
             GeneralThreadPool.getInstance().execute(this._business);
           }
           this._business.in(bp.getBytes());
         }
       }



       public void sendPacket(ServerBasePacket bp, boolean isClear) {
         if (Config.Connection.SendBusiness) {
           if (this._businessb == null) {
             this._businessb = new SendBusinessBlock();
             GeneralThreadPool.getInstance().execute(this._businessb);
           }
           this._businessb.in(bp.getBytes());
         } else {
           if (this._business == null) {
             this._business = new SendBusiness();
             GeneralThreadPool.getInstance().execute(this._business);
           }
           this._business.in(bp.getBytes());
         }
         if (isClear) {
           bp.clear();
         }
       }



       public void sendPacket(ServerBasePacket bp) {
         sendPacket(bp, true);
       }



       public void sendPacket(MJIProtoMessage message, int messageId) {
         sendPacket(message, messageId, true);
       }



       public void sendPacket(MJIProtoMessage message, int messageId, boolean isClear) {
         if (message.isInitialized()) {
           sendPacket(message.writeTo(MJEProtoMessages.fromInt(messageId)), isClear);
           if (isClear) {
             message.dispose();
           }
         } else {
           MJEProtoMessages.printNotInitialized((getActiveChar() == null) ? getIp() : getActiveChar().getName(), messageId, message.getInitializeBit());
         }
       }

       public GameClient(Channel channel)
       {
         this.m_shift_type = MJEShiftObjectType.NONE;
         this.m_server_identity = "";
         this.m_server_description = "";

         this.m_second_password_failure_count = 0;

         this.latestRestartMillis = 0L;

         this._auth_token = ""; this.m_login_info = null; this._version = 0L; this.isAuthPass = false; this.close = false; this._isLoginRecord = false; this._isUpdate = false; this.chnnel = channel; this.charStat = new int[6]; this.m_latest_accounts = ""; InetSocketAddress inetAddr = (InetSocketAddress)channel.remoteAddress(); this._inetAddress = inetAddr.getAddress(); this.observer = new ClientThreadObserver(Config.Connection.AutomaticKick * 60 * 1000);
         if (Config.Connection.AutomaticKick > 0)
           this.observer.start();
         channel.closeFuture().addListener((GenericFutureListener)new ChannelRemoveListener()); } public String get_Auth_Token() { return this._auth_token; }
        public void sendPacket(ProtoOutputStream stream) {
         sendPacket(stream, true);
       } public void set_Auth_Token(String account) {
         this._auth_token = account;
       }

       public void sendPacket(ProtoOutputStream stream, boolean isClear) {
         if (Config.Connection.SendBusiness) {
           if (this._businessb == null) {
             this._businessb = new SendBusinessBlock();
             GeneralThreadPool.getInstance().execute(this._businessb);
           }
           if (!stream.isCreated())
             stream.createProtoBytes();
           this._businessb.in(stream.getProtoBytes());
         } else {
           if (this._business == null) {
             this._business = new SendBusiness();
             GeneralThreadPool.getInstance().execute(this._business);
           }
           if (!stream.isCreated())
             stream.createProtoBytes();
           this._business.in(stream.getProtoBytes());
         }
         if (isClear)
           stream.dispose();
       }

       public void directSendPacket(Collection<ProtoOutputStream> col) {
         for (ProtoOutputStream stream : col) {
           if (!stream.isCreated())
             stream.createProtoBytes();
           if (Config.Connection.SendBusiness) {
             this._businessb.in(stream.getProtoBytes());
             continue;
           }
           this._business.in(stream.getProtoBytes());
         }
       }

       public void directSendPacket(Collection<ProtoOutputStream> col, long delay_millis) throws InterruptedException {
         for (ProtoOutputStream stream : col) {
           if (!stream.isCreated())
             stream.createProtoBytes();
           if (Config.Connection.SendBusiness) {
             this._businessb.in(stream.getProtoBytes());
           } else {
             this._business.in(stream.getProtoBytes());
           }
           if (delay_millis > 0L)
             Thread.sleep(delay_millis);
         }
       }

       class SendBusiness implements Runnable {
         private ArrayBlockingQueue<byte[]> _workQ = (ArrayBlockingQueue)new ArrayBlockingQueue<>(256);

         public void in(byte[] data) {
           if (data != null) {
             if ((data[0] & 0xFF) == 180 && (data[1] & 0xFF) == 9 && (data[2] & 0xFF) == 2)
               try {
                 Thread.sleep(50L);
               } catch (InterruptedException e) {
                 e.printStackTrace();
               }
             this._workQ.offer(data);
           }
         }

         public void run() {
           int write_count = 0;
           while (GameClient.this.chnnel.isActive()) {
             boolean isFlush = false;
             try {
               byte[] data = this._workQ.poll(3000L, TimeUnit.MILLISECONDS);
               if (data != null && GameClient.this.chnnel.isWritable())
                 if (this._workQ.size() <= 0) {
                   GameClient.this.chnnel.writeAndFlush(data);
                   isFlush = true;
                 } else if (++write_count >= 100) {
                   GameClient.this.chnnel.writeAndFlush(data);
                   write_count = 0;
                 } else {
                   GameClient.this.chnnel.write(data);
                 }
             } catch (InterruptedException interruptedException) {

             } catch (Exception e) {
               e.printStackTrace();
             }
             if (GameClient.this.chnnel != null && GameClient.this.chnnel.isActive() && !isFlush)
               GameClient.this.chnnel.flush();
             write_count = 0;
           }
           this._workQ.clear();
         }
       }

       class SendBusinessBlock implements Runnable {
         private final BlockingQueue<byte[]> _workQ;

         public SendBusinessBlock() {
           this._workQ = (BlockingQueue)new LinkedBlockingQueue<>();
         }

         public SendBusinessBlock(int i) {
           this._workQ = (BlockingQueue)new LinkedBlockingQueue<>(i);
         }

         public void in(byte[] data) {
           if (data != null)
             this._workQ.offer(data);
         }

         public void run() {
           boolean isFlush = false;
           while (GameClient.this.chnnel.isActive()) {
             try {
               byte[] data = this._workQ.poll(3000L, TimeUnit.MILLISECONDS);
               if (data != null && GameClient.this.chnnel.isActive())
                 if (this._workQ.size() <= 0) {
                   GameClient.this.chnnel.writeAndFlush(data);
                   isFlush = true;
                 } else {
                   GameClient.this.chnnel.write(data);
                 }
               if (GameClient.this.chnnel != null && GameClient.this.chnnel.isActive() && !isFlush)
                 GameClient.this.chnnel.flush();
             } catch (Exception e) {
               e.printStackTrace();
             }
           }
           this._workQ.clear();
         }
       }

       private String HexToDex(int data, int digits) {
         String number = Integer.toHexString(data);
         for (int i = number.length(); i < digits; i++)
           number = "0" + number;
         return number;
       }

       public String DataToPacket(byte[] data, int len) {
         StringBuffer result = new StringBuffer();
         int counter = 0;
         for (int i = 0; i < len; i++) {
           if (counter % 16 == 0)
             result.append(HexToDex(i, 4) + ": ");
           result.append(HexToDex(data[i] & 0xFF, 2) + " ");
           counter++;
           if (counter == 16) {
             result.append("   ");
             int charpoint = i - 15;
             for (int a = 0; a < 16; a++) {
               int t1 = data[charpoint++];
               if (t1 > 31 && t1 < 128) {
                 result.append((char)t1);
               } else {
                 result.append('.');
               }
             }
             result.append("\n");
             counter = 0;
           }
         }
         int rest = data.length % 16;
         if (rest > 0) {
           for (int j = 0; j < 17 - rest; j++)
             result.append("   ");
           int charpoint = data.length - rest;
           for (int a = 0; a < rest; a++) {
             int t1 = data[charpoint++];
             if (t1 > 31 && t1 < 128) {
               result.append((char)t1);
             } else {
               result.append('.');
             }
           }
           result.append("\n");
         }
         return result.toString();
       }

       public int getChatCount() {
         return this.chatCount;
       }

       public void setChatCount(int i) {
         this.chatCount = i;
       }

       public void setLoginRecord(boolean b) {
         this._isLoginRecord = b;
       }

       public boolean isLoginRecord() {
         return this._isLoginRecord;
       }

       public boolean isUpdate() {
         return this._isUpdate;
       }

       public void setUpdate(boolean b) {
         this._isUpdate = b;
       }

       public MJClientStatus getStatus() {
         return this._status;
       }

       public boolean is_non_handshake() {
         return (this._status != null && this._status.toInt() != MJClientStatus.CLNT_STS_HANDSHAKE.toInt());
       }

       public void setStatus(MJClientStatus sts) {
         if (sts.toInt() == MJClientStatus.CLNT_STS_AUTHLOGIN.toInt() && this._status.toInt() == MJClientStatus.CLNT_STS_ENTERWORLD.toInt())
           try {
             throw new Exception();
           } catch (Exception e) {
             e.printStackTrace();
           }
         this._status = sts;
       }

       public void setStatus2(MJClientStatus sts) {
         this._status = sts;
       }

       public MJEShiftObjectType get_shift_type() {
         return this.m_shift_type;
       }

       public void set_shift_type(MJEShiftObjectType shift_type) {
         this.m_shift_type = shift_type;
       }

       public boolean is_shift_client() {
         return !this.m_shift_type.equals(MJEShiftObjectType.NONE);
       }

       public boolean is_shift_transfer() {
         return this.m_shift_type.equals(MJEShiftObjectType.TRANSFER);
       }

       public boolean is_shift_battle() {
         return this.m_shift_type.equals(MJEShiftObjectType.BATTLE);
       }

       public String get_server_identity() {
         return this.m_server_identity;
       }

       public void set_server_identity(String server_identity) {
         this.m_server_identity = server_identity;
       }

       public void set_server_description(String server_description) {
         this.m_server_description = server_description;
       }

       public String get_server_description() {
         return this.m_server_description;
       }

       public int get_second_password_failure_count() {
         return this.m_second_password_failure_count;
       }

       public int inc_second_password_failure_count() {
         return ++this.m_second_password_failure_count;
       }

       public void reset_second_password_failure_count() {
         this.m_second_password_failure_count = 0;
       }

       public MJShiftObject get_shift_object() {
         return this.m_shift_object;
       }

       public void set_shift_object(MJShiftObject sobject) {
         this.m_shift_object = sobject;
       }

       public long latestRestartMillis() {
         return this.latestRestartMillis;
       }

       public void latestRestartMillis(long latestRestartMillis) {
         this.latestRestartMillis = latestRestartMillis;
       }
     }


