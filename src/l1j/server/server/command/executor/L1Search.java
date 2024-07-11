         package l1j.server.server.command.executor;

         import java.sql.Connection;
         import java.sql.PreparedStatement;
         import java.sql.ResultSet;
         import java.util.StringTokenizer;
         import java.util.logging.Logger;
         import l1j.server.L1DatabaseFactory;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.serverpackets.S_SystemMessage;
         import l1j.server.server.serverpackets.ServerBasePacket;
         import l1j.server.server.utils.SQLUtil;

         public class L1Search
           implements L1CommandExecutor {
           private static Logger _log = Logger.getLogger(L1Search.class.getName());




           public static L1CommandExecutor getInstance() {
             return new L1Search();
           }


           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               StringTokenizer st = new StringTokenizer(arg);
               String type = "";
               String real_name_id_view = "";
               String add = "";
               boolean simpleS = true;
               int itCount = 0;
               while (st.hasMoreTokens()) {
                 if (itCount == 1) {
                   add = "%";
                 }
                 String tempVar = st.nextToken();
                 if (itCount == 0 && (tempVar
                   .equals("盔甲") || tempVar.equals("武器") || tempVar
                   .equals("ETC") || tempVar
                   .equals("轉換") || tempVar
                   .equals("NPC"))) {
                   simpleS = false;
                   type = tempVar;
                 } else {
                   real_name_id_view = real_name_id_view + add + tempVar;
                 }
                 itCount++;
               }
               if (!simpleS) {
                 find_object(pc, type, real_name_id_view);
               } else {
                 find_object(pc, real_name_id_view);
               }
             } catch (Exception e) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("請輸入.find [盔甲、武器等、變身、NPC]"));
             }
           }


           private void find_object(L1PcInstance pc, String type, String real_name_id_view) {
             String str1 = null;
             String str2 = null;
             int bless = 0;
             int count = 0;
             Connection con = null;
             PreparedStatement statement = null;
             ResultSet rs = null;
             try {
               con = L1DatabaseFactory.getInstance().getConnection();
               boolean error = false;

               pc.sendPackets((ServerBasePacket)new S_SystemMessage(" "));

               if (type.equals("盔甲")) {
                 statement = con.prepareStatement("SELECT item_id,real_name_id_view,bless FROM armor WHERE real_name_id_view Like '%" + real_name_id_view + "%'");
               } else if (type.equals("武器")) {
                 statement = con.prepareStatement("SELECT item_id,real_name_id_view,bless FROM weapon WHERE real_name_id_view Like '%" + real_name_id_view + "%'");
               } else if (type.equals("ETC")) {
                 statement = con.prepareStatement("SELECT item_id,real_name_id_view,bless FROM etcitem WHERE real_name_id_view Like '%" + real_name_id_view + "%'");
               } else if (type.equals("轉換")) {
                 statement = con.prepareStatement("SELECT polyid,real_name_id_view FROM polymorphs WHERE real_name_id_view Like '%" + real_name_id_view + "%'");
               } else if (type.equals("NPC")) {
                 statement = con.prepareStatement("SELECT npcid,real_name_id_view FROM npc WHERE real_name_id_view Like '%" + real_name_id_view + "%'");
               } else {
                 error = true;
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("請輸入.find[盔甲、武器等、變身、NPC]。"));
               }
               String blessed = null;
               if (!error) {
                 rs = statement.executeQuery();
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("上線搜尋'" + real_name_id_view.replace("%", " ") + " ' 是" + type + "姓名..."));
                 while (rs.next()) {
                   str1 = rs.getString(1);
                   str2 = rs.getString(2);
                   if (type.equals("盔甲") || type.equals("武器") || type
                     .equals("ETC")) {
                     bless = rs.getInt(3);
                     if (bless == 1) {
                       blessed = "";
                     } else if (bless == 0) {
                       blessed = "\\fR";
                     } else {
                       blessed = "\\fY";
                     }
                     pc.sendPackets((ServerBasePacket)new S_SystemMessage(blessed + "數字：" + str1 + ", " + str2));
                   } else {

                     pc.sendPackets((ServerBasePacket)new S_SystemMessage("數字：" + str1 + ", " + str2));
                   }

                   count++;
                 }
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("尋找" + count + " 上線項目" + type + "類型。"));
               }

             } catch (Exception e) {
               e.printStackTrace();
             } finally {
               SQLUtil.close(rs, statement, con);
             }
           }

           private void find_object(L1PcInstance pc, String real_name_id_view) {
             String str1 = null;
             String str2 = null;
             int bless = 0;
             String blessed = null;
             Connection con = null;
             PreparedStatement statement = null;
             ResultSet rs = null;
             int count1 = 0;
             int count2 = 0;
             int count3 = 0;
             int count4 = 0;
             int count5 = 0;
             try {
               con = L1DatabaseFactory.getInstance().getConnection();

               pc.sendPackets((ServerBasePacket)new S_SystemMessage(" "));
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("從行檢索的物件名稱： " + real_name_id_view.replace("%", " ") + "'"));

               statement = con.prepareStatement("SELECT item_id,real_name_id_view,bless FROM armor WHERE real_name_id_view Like '%" + real_name_id_view + "%'");
               rs = statement.executeQuery();
               while (rs.next()) {
                 if (count1 == 0) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("盔甲："));
                 }
                 str1 = rs.getString(1);
                 str2 = rs.getString(2);
                 bless = rs.getInt(3);
                 if (bless == 1) {
                   blessed = "";
                 } else if (bless == 0) {
                   blessed = "\\fR";
                 } else {
                   blessed = "\\fY";
                 }
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage(blessed + "數字：" + str1 + ", " + str2));

                 count1++;
               }
             } catch (Exception e) {
               e.printStackTrace();
             } finally {
               SQLUtil.close(rs);
               SQLUtil.close(statement);
               SQLUtil.close(con);
             }
             try {
               con = L1DatabaseFactory.getInstance().getConnection();
               statement = con.prepareStatement("SELECT item_id,real_name_id_view,bless FROM weapon WHERE real_name_id_view Like '%" + real_name_id_view + "%'");
               rs = statement.executeQuery();
               while (rs.next()) {
                 if (count2 == 0) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("武器："));
                 }
                 str1 = rs.getString(1);
                 str2 = rs.getString(2);
                 bless = rs.getInt(3);
                 if (bless == 1) {
                   blessed = "";
                 } else if (bless == 0) {
                   blessed = "\\fR";
                 } else {
                   blessed = "\\fY";
                 }
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage(blessed + "數字：" + str1 + ", " + str2));

                 count2++;
               }
             } catch (Exception e) {
               e.printStackTrace();
             } finally {
               SQLUtil.close(rs);
               SQLUtil.close(statement);
               SQLUtil.close(con);
             }
             try {
               con = L1DatabaseFactory.getInstance().getConnection();
               statement = con.prepareStatement("SELECT item_id,real_name_id_view,bless FROM etcitem WHERE real_name_id_view Like '%" + real_name_id_view + "%'");
               rs = statement.executeQuery();
               while (rs.next()) {
                 if (count3 == 0) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("ETC:"));
                 }
                 str1 = rs.getString(1);
                 str2 = rs.getString(2);
                 bless = rs.getInt(3);
                 if (bless == 1) {
                   blessed = "";
                 } else if (bless == 0) {
                   blessed = "\\fR";
                 } else {
                   blessed = "\\fY";
                 }
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage(blessed + "數字：" + str1 + ", " + str2));

                 count3++;
               }
             } catch (Exception e) {
               e.printStackTrace();
             } finally {
               SQLUtil.close(rs);
               SQLUtil.close(statement);
               SQLUtil.close(con);
             }

             try {
               con = L1DatabaseFactory.getInstance().getConnection();
               statement = con.prepareStatement("SELECT polyid,name FROM polymorphs WHERE name Like '%" + real_name_id_view + "%'");
               rs = statement.executeQuery();
               while (rs.next()) {
                 if (count4 == 0) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("轉換："));
                 }
                 str1 = rs.getString(1);
                 str2 = rs.getString(2);
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("數字：" + str1 + ", " + str2));
                 count4++;
               }
             } catch (Exception e) {
               e.printStackTrace();
             } finally {
               SQLUtil.close(rs);
               SQLUtil.close(statement);
               SQLUtil.close(con);
             }

             try {
               con = L1DatabaseFactory.getInstance().getConnection();
               statement = con.prepareStatement("SELECT npcid, desc_view FROM npc WHERE desc_view Like '%" + real_name_id_view + "%'");
               rs = statement.executeQuery();
               while (rs.next()) {
                 if (count5 == 0) {
                   pc.sendPackets((ServerBasePacket)new S_SystemMessage("NPC:"));
                 }
                 str1 = rs.getString(1);
                 str2 = rs.getString(2);
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("數字：" + str1 + ", " + str2));
                 count5++;
               }
             } catch (Exception e) {
               e.printStackTrace();
             } finally {
               SQLUtil.close(rs, statement, con);
             }

             try {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("搜尋結果："));
               String found = "";
               if (count1 > 0) {
                 found = found + "盔甲：" + count1 + "、";
               }
               if (count2 > 0) {
                 found = found + "武器：" + count2 + "、";
               }
               if (count3 > 0) {
                 found = found + "ETC: " + count3 + "、";
               }
               if (count4 > 0) {
                 found = found + "轉換：" + count4 + "、";
               }
               if (count5 > 0) {
                 found = found + "NPC: " + count5 + "。";
               }
               if (found.length() > 0) {
                 found = found.substring(0, found.length() - 1) + "。";
               } else {
                 found = " 找到 0 件商品";
               }
               pc.sendPackets((ServerBasePacket)new S_SystemMessage(found));
             } catch (Exception exception) {}
           }
         }


