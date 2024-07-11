 package l1j.server.server.serverpackets;
 
 import java.util.StringTokenizer;
 import l1j.server.server.datatables.AdenShopTable;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.templates.L1AdenShopItem;
 
 
 public class S_SurvivalCry
   extends ServerBasePacket
 {
   private static final String S_SURVIVAL_CRY = "[S] S_SurvivalCry";
   public static final int LIST = 0;
   public static final int EMAIL = 1;
   public static final int POINT = 2;
   public static final int OTP_SHOW = 4;
   public static final int OTP_CHECK_MSG = 5;
   public static final int 連接未知 = 15;
   
   public S_SurvivalCry(int value) {
     buildPacket(value);
   }
   
   public S_SurvivalCry(int value, boolean ck) {
     buildPacket(value, ck);
   }
   
   private void buildPacket(int value, boolean ck) {
     writeC(109);
     writeC(15);
     writeD(0);
     writeC(0);
     
     writeC(112);
     writeC(23);
     
     writeH(0);
     
     writeH(0);
   }
 
 
 
 
   
   private void buildPacket(int value) {
     writeC(109);
     writeD(15);
     writeH(0);
     writeD(value);
     writeH(0);
   }
 
   
   public S_SurvivalCry(long value) {
     writeC(109);
     writeD(15);
     writeH(0);
     writeD(value);
     writeH(0);
   }
   
   public S_SurvivalCry(int value, L1PcInstance pc) {
     try {
       if (value == 0) {
         writeC(109);
         writeC(2);
         writeH(0);
         writeD(0);
         writeH(AdenShopTable.getInstance().Size());
         writeH(AdenShopTable.data_length);
         writeH(AdenShopTable.data_length);
         for (L1AdenShopItem item : AdenShopTable.getInstance().toArray()) {
           writeD(item.getItemId());
           writeH((item.get_icon_id() > 0) ? item.get_icon_id() : item.getItem().getGfxId());
           writeH(0);
           String name = item.getItem().getName();
           if (item.getPackCount() > 1)
             name = name + "(" + item.getPackCount() + ")"; 
           if (item.getItem().getMaxUseTime() > 0) {
             name = name + " [" + item.getItem().getMaxUseTime() + "]";
           }

           writeH((name.getBytes("UTF-16LE")).length + 2);
           writeSU16(name);
           String html = item.getHtml();
           int ii = 2;
           if (!html.equalsIgnoreCase("")) {
             byte[] test = html.getBytes("UTF-8");
             for (int i = 0; i < test.length; ) {
               if ((test[i] & 0xFF) >= 127) {
                 i += 2;
               } else {
                 i++;
               }  ii += 2;
             } 
           } 
           writeH(ii);
           writeSS(html);
           writeD(item.getPrice());
           writeH(item.getType());
           writeH(item.getStatus());
           writeD(789951);
           writeD(99);
         } 
       } else if (value == 1) {
         writeC(109);

         
         String s = "0c 00 26 00 6e 00 75 00 6c 00 6c 00 40 00 6e 00 75 00 6c 00 6c 00 2e 00 63 00 6f 00 6d 00 00 00 20 b8";
         
         StringTokenizer st = new StringTokenizer(s);
         while (st.hasMoreTokens()) {
           writeC(Integer.parseInt(st.nextToken(), 16));
         }
       } else if (value == 2) {
         writeC(109);
         writeH(3);
         writeH(1);
         writeH(4);
         writeD(pc.getNcoin());
         
         writeH(0);
       } else if (value == 3) {
         writeC(109);
         String s = "02 00 00 f4 ff ff ff 00 00 00 00 00 00 99 17";
         StringTokenizer st = new StringTokenizer(s);
         while (st.hasMoreTokens()) {
           writeC(Integer.parseInt(st.nextToken(), 16));
         }
       } else if (value == 4) {
         writeC(109);
         writeD(51);
         writeH(0);
       } else if (value == 5) {
         writeC(109);
         writeH(5);
 
 
         
         writeH(0);
         writeD(0);
         writeC(0);
       } 
     } catch (Exception exception) {}
   }
 
 
   
   public byte[] getContent() {
     return getBytes();
   }
 
   
   public String getType() {
     return "[S] S_SurvivalCry";
   }
 }


