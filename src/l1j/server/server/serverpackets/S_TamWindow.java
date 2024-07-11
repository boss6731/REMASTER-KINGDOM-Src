 package l1j.server.server.serverpackets;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.Timestamp;
 import java.util.StringTokenizer;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.utils.SQLUtil;



 public class S_TamWindow
   extends ServerBasePacket
 {
   private static final String S_TAMWINDOW = "S_TAMWINDOW";
   public static final int ACCOUNT_TAM = 205;
   public static final int Buff = 110;

   public S_TamWindow(long time, int type) {
     writeC(19);
     writeC(110);
     String s = "00 08 02 10 e0 11 18";
     StringTokenizer st = new StringTokenizer(s);
     while (st.hasMoreTokens()) {
       writeC(Integer.parseInt(st.nextToken(), 16));
     }
     byteWrite(time / 1000L);
     if (type == 1) {
       s = "20 08 28 d4 2f 30 00 38 03 40";
     } else if (type == 2) {
       s = "20 08 28 93 33 30 00 38 03 40";
     } else if (type == 3) {
       s = "20 08 28 92 33 30 00 38 03 40";
     } else {
       s = "20 08 28 d4 2f 30 00 38 03 40";
     }
     st = new StringTokenizer(s);
     while (st.hasMoreTokens()) {
       writeC(Integer.parseInt(st.nextToken(), 16));
     }

     if (type == 1) {
       writeH(7881);
       s = "48 d5 20 50 00 58 01";
     } else if (type == 2) {
       writeH(8402);
       s = "48 d6 20 50 00 58 01";
     } else if (type == 3) {
       writeH(8403);
       s = "48 d7 20 50 00 58 01";
     } else {
       writeH(7881);
       s = "48 d5 20 50 00 58 01";
     }
     st = new StringTokenizer(s);
     while (st.hasMoreTokens()) {
       writeC(Integer.parseInt(st.nextToken(), 16));
     }
     writeH(96);
     writeH(104);
     writeH(112);

     writeH(0);
   }
   public S_TamWindow(String account) {
     writeC(19);
     writeC(205);
     writeC(1);
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     int _level = 0;
     int _class = 0;
     int _sex = 0;
     String _Name = null;
     Timestamp tamtime = null;
     int _objid = 0;

     long time = 0L;
     long sysTime = System.currentTimeMillis();

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT objid, TamEndTime, level, char_name, Type,Sex FROM characters WHERE account_name=? ORDER BY `TamEndTime` DESC, `EXP` DESC");
       pstm.setString(1, account);
       rs = pstm.executeQuery();
       while (rs.next()) {

         int tamcount = 0;
         int objidcount = 0;
         time = 0L;
         tamtime = null;

         _objid = rs.getInt("objid");
         _level = rs.getInt("level");
         _class = rs.getInt("Type");
         _sex = rs.getInt("Sex");
         _Name = rs.getString("char_name");
         tamtime = rs.getTimestamp("TamEndTime");

         if (tamtime != null &&
           sysTime < tamtime.getTime()) {
           time = tamtime.getTime() - sysTime;
         }

         if (time == 0L) {
           tamcount = 1;
         } else {
           tamcount = byteWriteCount(time / 1000L);
         }
         objidcount = byteWriteCount(_objid);

         writeC(10);

         writeC((_Name.getBytes()).length + 14 + objidcount + tamcount);

         writeC(8);
         writeC(0);
         writeC(16);
         byteWrite(_objid);


         writeC(24);
         if (time == 0L) {
           writeC(0);
         } else {
           byteWrite(time / 1000L);
         }

         writeC(32);
         writeC(tamwaitcount(_objid));
         writeC(42);
         writeC((_Name.getBytes()).length);
         writeByte(_Name.getBytes());
         writeC(48);
         writeC(_level);
         writeC(56);
         writeC(_class);
         writeC(64);
         writeC(_sex);
       }
       writeC(16);
       writeC(3);
       writeC(24);
       writeC(0);
       writeC(32);
       writeC(0);
       writeH(0);
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public static final int[] hextable = new int[] { 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255 };




   private void byteWrite(long value) {
     long temp = value / 128L;
     if (temp > 0L) {
       writeC(hextable[(int)value % 128]);
       while (temp >= 128L) {
         writeC(hextable[(int)temp % 128]);
         temp /= 128L;
       }
       if (temp > 0L) {
         writeC((int)temp);
       }
     } else if (value == 0L) {
       writeC(0);
     } else {
       writeC(hextable[(int)value]);
       writeC(0);
     }
   }

   private int byteWriteCount(long value) {
     long temp = value / 128L;
     int count = 0;
     if (temp > 0L) {
       count++;
       while (temp >= 128L) {
         count++;
         temp /= 128L;
       }
       if (temp > 0L) {
         count++;
       }
     } else if (value == 0L) {
       count++;
     } else {
       count += 2;
     }

     return count;
   }
   private int tamwaitcount(int obj) {
     int count = 0;
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM tam WHERE objid = ?");



       pstm.setInt(1, obj);
       rs = pstm.executeQuery();
       while (rs.next()) {
         count++;
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
     return count;
   }


   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "S_TAMWINDOW";
   }
 }


