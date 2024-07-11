     package l1j.server.server.serverpackets;

     import java.util.logging.Logger;
     import l1j.server.server.model.Instance.L1NpcShopInstance;
     import l1j.server.server.model.Instance.L1PcInstance;




     public class S_WhoCharinfo
       extends ServerBasePacket
     {
       private static final String S_WHO_CHARINFO = "[S] S_WhoCharinfo";
       private static Logger _log = Logger.getLogger(S_WhoCharinfo.class.getName());

       public S_WhoCharinfo(L1PcInstance pc) {
         _log.fine("Who charpack for : " + pc.getName());

         String lawfulness = "";


         float win = 0.0F;
         float lose = 0.0F;
         float total = 0.0F;
         float winner = 0.0F;
         if (pc.getKDA() != null) {
           win = (pc.getKDA()).kill;
           lose = (pc.getKDA()).death;
         }
         total = win + lose;
         winner = win * 100.0F / total;


         int lawful = pc.getLawful();
         if (lawful < 0) {
           lawfulness = "(Chaotic)";
         } else if (lawful >= 0 && lawful < 500) {
           lawfulness = "(Neutral)";
         } else if (lawful >= 500) {
           lawfulness = "(Lawful)";
         }

         writeC(153);
         writeC(8);

         String title = "";
         String clan = "無血"; // 無血

// 如果玩家有頭銜，將其設置為 title 變數
         if (!pc.getTitle().equalsIgnoreCase("")) {
           title = pc.getTitle() + " ";
         }

// 如果玩家有幫派 ID，則設置 clan 變數為幫派名稱
         if (pc.getClanid() > 0) {
           clan = "[" + pc.getClanname() + "]";
         }

// 寫入玩家信息，包括頭銜、名稱、合法性、幫派名稱、擊殺數、死亡數和勝率
         writeS(title + "[" + pc.getName() + "] " + lawfulness + " 血盟:" + clan + "擊殺:: " + (int)win + " / 死亡: " + (int)lose + " / 獲勝的幾率:" + winner + "%");
         writeD(0);


       public S_WhoCharinfo(L1NpcShopInstance shopnpc) {
         _log.fine("charpack 的物件： " + shopnpc.getName());

         String lawfulness = "";

         float win = 0.0F;
         float lose = 0.0F;
         float total = 0.0F;
         float winner = 0.0F;

         win = 0.0F;
         lose = 0.0F;
         total = win + lose;
         winner = win * 100.0F / total;

         int lawful = shopnpc.getLawful();
         if (lawful < 0) {
           lawfulness = "(Chaotic)";
         } else if (lawful >= 0 && lawful < 500) {
           lawfulness = "(Neutral)";
         } else if (lawful >= 500) {
           lawfulness = "(Lawful)";
         }

         writeC(153);
         writeC(8);

         String title = "";

         if (!shopnpc.getTitle().equalsIgnoreCase("")) {
           title = shopnpc.getTitle() + " ";
         }

         writeS(title + "[" + shopnpc.getName() + "] " + lawfulness + "\n\r擊殺:: 0 / 死亡: 0 / 獲勝的幾率:" + winner + "%");
         writeD(0);
       }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_WhoCharinfo";
       }
     }


