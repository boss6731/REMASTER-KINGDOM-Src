 package l1j.server.server.serverpackets;

 import l1j.server.MJNetSafeSystem.MJNetSafeLoadManager;

 public class S_ServerVersion
   extends ServerBasePacket {
   private static final String S_SERVER_VERSION = "[S] ServerVersion";
   private static long _startMs = 0L;

   public S_ServerVersion() {
     super(128);
     if (_startMs <= 0L) {
       _startMs = System.currentTimeMillis() / 1000L;
     }
     writeC(19);
     writeH(821);
     writeC(8);
     writeC(0);
     writeC(16);
     writeC(0);
     writeC(24);
     writeBit(MJNetSafeLoadManager.NS_CLIENT_VERSION);
     writeC(32);
     writeBit(MJNetSafeLoadManager.NS_CLIENT_VERSION);
     writeC(40);
     writeBit(MJNetSafeLoadManager.NS_AUTHSERVER_VERSION);
     writeC(48);
     writeBit(MJNetSafeLoadManager.NS_CLIENT_VERSION);
     writeC(56);
     writeBit(_startMs);
     writeC(64);
     writeC(0);
     writeC(72);
     writeC(0);
     writeC(80);
     writeBit(MJNetSafeLoadManager.NS_CLIENT_SETTING_SWITCH);
     writeC(88);
     writeBit(System.currentTimeMillis() / 1000L);
     writeC(96);
     writeBit(MJNetSafeLoadManager.NS_CACHESERVER_VERSION);
     writeC(104);
     writeBit(MJNetSafeLoadManager.NS_TAMSERVER_VERSION);
     writeC(112);
     writeBit(MJNetSafeLoadManager.NS_ARCASERVER_VERSION);
     writeC(120);
     writeBit(MJNetSafeLoadManager.NS_HIBREED_INTERSERVER_VERSION);

     writeC(128);
     writeC(1);
     writeBit(MJNetSafeLoadManager.NS_ARENACOSERVER_VERSION);

     writeC(136);
     writeC(1);
     writeC(0);

     writeC(144);
     writeC(1);
     writeBit(220221101L);

     writeC(152);
     writeC(1);
     writeBit(10091L);


     writeH(0);
   }


   public byte[] getContent() {
     return getBytes();
   }


   public String getType() {
     return "[S] ServerVersion";
   }
 }


