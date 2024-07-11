     package l1j.server.server;

     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.L1World;
     import l1j.server.server.serverpackets.S_ChatPacket;
     import l1j.server.server.serverpackets.S_Paralysis;
     import l1j.server.server.serverpackets.S_Poison;
     import l1j.server.server.serverpackets.ServerBasePacket;




     public class BugKick
     {
       private static BugKick _instance;

       public static BugKick getInstance() {
         if (_instance == null) {
           _instance = new BugKick();
         }
         return _instance;
       }


         public void KickPlayer(L1PcInstance pc) {
             try {
                 // 將玩家傳送到指定坐標
                 pc.start_teleport(32737, 32796, 99, 5, 18339, true, false);

                 // 向玩家發送中毒效果的封包
                 pc.sendPackets((ServerBasePacket)new S_Poison(pc.getId(), 2));

                 // 廣播中毒效果封包給其他玩家
                 pc.broadcastPacket((ServerBasePacket)new S_Poison(pc.getId(), 2));

                 // 向玩家發送癱瘓效果的封包
                 pc.sendPackets((ServerBasePacket)new S_Paralysis(5, true));

                 // 停止玩家的技能效果計時器
                 pc.killSkillEffectTimer(87);

                 // 設置一個新的技能效果，持續86400000毫秒 (1天)
                 pc.setSkillEffect(87, 86400000L);

                 // 向玩家發送聊天訊息
                 pc.sendPackets((ServerBasePacket)new S_ChatPacket(pc, "如果你沒有使用漏洞，你就不應該來到這裡吧？"));

                 // 向全服廣播消息
                 L1World.getInstance().broadcastServerMessage("\fY漏洞使用者 [" + pc.getName() + "] 請舉報!!");
             } catch (Exception e) {
                 // 捕捉異常並打印錯誤消息
                 System.out.println(pc.getName() + " 處罰登記錯誤");
             }
         }


