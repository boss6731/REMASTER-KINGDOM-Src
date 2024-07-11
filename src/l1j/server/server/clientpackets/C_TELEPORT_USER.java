 package l1j.server.server.clientpackets;

 import l1j.server.MJTemplate.MJString;
 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1World;

 public class C_TELEPORT_USER
   extends ClientBasePacket {
   public C_TELEPORT_USER(byte[] data, GameClient client) throws Exception {
     super(data);
     try {
       L1PcInstance pc = client.getActiveChar();
       if (pc == null || !pc.isGm()) {
         return;
       }
       String targetName = readS();
       if (MJString.isNullOrEmpty(targetName)) {
         return;
       }

         try {
             // 從世界實例中根據目標名稱查找玩家對象
             L1PcInstance target = L1World.getInstance().getPlayer(targetName);

             // 如果目標玩家不存在，發送提示信息並返回
             if (target == null) {
                 pc.sendPackets(String.format("%s 無法找到。", new Object[] { targetName }));
                 return;
             }

             // 開始傳送到目標玩家的位置
             pc.start_teleport(target.getX(), target.getY(), target.getMapId(), pc.getHeading(), 18339, false, true);
         } catch (Exception e) {
             // 捕捉並打印異常信息
             e.printStackTrace();
         }


   public String getType() {
     return "[C] C_TELEPORT_USER";
   }
 }


