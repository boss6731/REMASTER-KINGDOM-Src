         package l1j.server.server.command.executor;

         import java.util.Collection;
         import l1j.server.server.model.Instance.L1PcInstance;
         import l1j.server.server.model.L1World;
         import l1j.server.server.serverpackets.S_ChatPacket;
         import l1j.server.server.serverpackets.S_WhoAmount;
         import l1j.server.server.serverpackets.ServerBasePacket;



         public class L1Who
           implements L1CommandExecutor
         {
           public static L1CommandExecutor getInstance() {
             return new L1Who();
           }


           public void execute(L1PcInstance pc, String cmdName, String arg) {
             try {
               int CalcUser = L1UserCalc.getClacUser();
               Collection<L1PcInstance> players = L1World.getInstance().getAllPlayers();
               int robotcount = 0;
               int playercount = 0;
               int AutoShopUser = 0;
               for (L1PcInstance each : players) {
                 if (each.noPlayerCK || each.noPlayerck2) {
                   robotcount++; continue;
                 }  if (each.isPrivateShop() && each.getNetConnection() == null) {
                   AutoShopUser++; continue;
                 }
                 playercount++;
               }

               String amount = String.valueOf(playercount);
               S_WhoAmount s_whoamount = new S_WhoAmount(amount);
               pc.sendPackets((ServerBasePacket)s_whoamount);
               pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, "\\aD=========== 連結玩家數量 ==========="));
               pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, "\\aL[機器人] : " + robotcount));
               pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, "\\aH[使用者] 總人數：" + playercount));
               pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, "\\aJ【無人商店】：" + AutoShopUser));
               pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, "\\aE[彈出]：" + CalcUser));



               if (arg.equalsIgnoreCase("全部")) {
                 StringBuffer gmList = new StringBuffer();
                 StringBuffer playList = new StringBuffer();
                 StringBuffer shopList = new StringBuffer();
                 StringBuffer robotList = new StringBuffer();

                 int countGM = 0, countPlayer = 0, countShop = 0, countRobot = 0;

                 for (L1PcInstance each : players) {
                   if (each.isGm()) {
                     gmList.append(each.getName() + ", ");
                     countGM++;
                     continue;
                   }
                   if (each.noPlayerCK || each.noPlayerck2) {
                     robotList.append(each.getName() + ", ");
                     countRobot++;
                     continue;
                   }
                   if (!each.isPrivateShop()) {
                     playList.append(each.getName() + ", ");
                     countPlayer++;
                     continue;
                   }
                   if (each.isPrivateShop()) {
                     shopList.append(each.getName() + ", ");
                     countShop++;
                   }
                 }
                 if (gmList.length() > 0) {
                   pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, "-- 管理員 (" + countGM + "人數)"));
                   pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, gmList.toString()));
                 }

                 if (playList.length() > 0) {
                   pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, "-- 玩家(" + countPlayer + "人數)"));
                   pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, playList.toString()));
                 }
                 if (robotList.length() > 0) {
                   pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, "-- 機器人用戶 (" + countRobot + "人數)"));
                   pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, robotList.toString()));
                 }
                 if (shopList.length() > 0) {
                   pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, "-- 私人商店 (" + countShop + "人數)"));
                   pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, shopList.toString()));
                 }
               }
               players = null;
             } catch (Exception e) {
               pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, "請輸入“誰”[全部]。"));
             }
           }
         }


