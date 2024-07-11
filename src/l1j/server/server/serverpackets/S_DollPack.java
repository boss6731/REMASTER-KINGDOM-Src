     package l1j.server.server.serverpackets;

     import MJShiftObject.Battle.MJShiftBattleCharacterInfo;
     import l1j.server.MJTemplate.MJString;
     import l1j.server.server.model.Instance.L1DollInstance;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.L1Character;






     public class S_DollPack
       extends ServerBasePacket
     {
       private static final String S_DOLLPACK = "[S] S_DollPack";

       public S_DollPack(L1DollInstance pet, L1PcInstance player) {
         writeC(186);
         writeH(pet.getX());
         writeH(pet.getY());
         writeD(pet.getId());
         writeH(pet.getCurrentSpriteId());
         writeC(pet.getStatus());
         writeC(pet.getHeading());
         writeC(0);
         writeC(pet.getMoveSpeed());
         writeD(0);
         writeH(0);
         writeS(pet.getNameId());
         writeS(pet.getTitle());
         writeC((pet.getMaster() != null) ? (pet.getMaster().isInvisble() ? 2 : 0) : 0);


         writeD(0);
         writeS(null);

         String master_name = "";
         L1Character c = pet.getMaster();
         if (c != null) {
           master_name = c.getName();
           if (c instanceof L1PcInstance) {
             L1PcInstance pc = (L1PcInstance)c;
             if (pc.is_shift_battle()) {
               String server_description = pc.get_server_description();
               MJShiftBattleCharacterInfo cInfo = pc.get_battle_info();
               if (MJString.isNullOrEmpty(server_description) || cInfo == null) {
                 master_name = "身份不明的人";
               } else {
                 master_name = cInfo.to_name_pair();
               }
             }
           }
         }

         writeS(master_name);
         writeC(0);
         writeC(255);
         writeC(0);
         writeC(pet.getLevel());
         writeC(0);
         writeC(255);
         writeC(255);
         writeC(0);
         writeC(0);
         writeC(255);
         writeH(0);
       }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_DollPack";
       }
     }


