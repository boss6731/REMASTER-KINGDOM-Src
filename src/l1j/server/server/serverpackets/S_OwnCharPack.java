     package l1j.server.server.serverpackets;

     import l1j.server.Config;
     import l1j.server.MJInstanceSystem.MJInstanceObject;
     import l1j.server.MJInstanceSystem.MJInstanceSpace;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.L1Clan;





     public class S_OwnCharPack
       extends ServerBasePacket
     {
       private static final String S_OWN_CHAR_PACK = "[S] S_OwnCharPack";
       private static final int STATUS_INVISIBLE = 2;
       private static final int STATUS_PC = 4;
       private static final int STATUS_FREEZE = 8;
       private static final int STATUS_BRAVE = 16;
       private static final int STATUS_ELFBRAVE = 32;
       private static final int STATUS_FASTMOVABLE = 64;
       private static final int STATUS_GHOST = 128;
       private static final int BLOOD_LUST = 16;
       private static final int DANCING_BLADES = 16;

       public S_OwnCharPack(L1PcInstance pc) {
         if (pc == null) {
           return;
         }


         int mid = pc.getMapId();
         if (mid == 1708 && mid <= 1710) {
           buildPacket(pc);
         }
         else if (MJInstanceSpace.isInInstance(mid)) {
           buildPacket(pc);
         } else if (pc.getRedKnightClanId() != 0) {
           buildPacket(pc);
         } else {
           oldBuildPacket(pc);
         }
       }
       private void oldBuildPacket(L1PcInstance pc) {
         int status = 4;

         if (pc.isInvisble() || pc.isGmInvis()) {
           status |= 0x2;
         }
         if (pc.isBrave()) {
           status |= 0x10;
         }
         if (pc.isElfBrave()) {
           status |= 0x10;
           status |= 0x20;
         }
         if (pc.isBlood_lust()) {
           status |= 0x10;
         }
         if (pc.isElfBraveMagicShort() || pc.isElfBraveMagicLong() || pc.hasSkillEffect(177)) {
           status |= 0x10;
         }
         if (pc.isFastMovable() || pc.isFruit()) {
           status |= 0x40;
         }
         if (pc.isGhost()) {
           status |= 0x80;
         }
         if (pc.isParalyzed()) {
           status |= 0x8;
         }

         writeC(186);
         writeH(pc.getX());
         writeH(pc.getY());
         writeD(pc.getId());
         if (pc.isDead()) {
           writeH(pc.getTempCharGfxAtDead());
         } else if (pc.isPrivateShop()) {
           if (pc.상점변신 != 0)
             writeH(pc.상점변신);
         } else {
           writeH(pc.getCurrentSpriteId());
         }
         if (pc.isDead()) {
           writeBit(8L);
         } else if (pc.isPrivateShop()) {
           writeC(70);
         } else {
           writeC(pc.getCurrentWeapon());
         }
         writeC(pc.getHeading());
         writeC(pc.getLight().getOwnLightSize());
         writeC(pc.getMoveSpeed());
         writeD(pc.get_exp());
         writeH(pc.getLawful());
         writeS(pc.getName());
         writeS(pc.getTitle());
         writeC(status);
         if (pc.getClanid() > 0 && pc.getClan() != null) { writeD(pc.getClan().getEmblemId()); }
         else { writeD(0); }
          writeS(pc.getClanname());
         writeS(null);
         writeC(0);
         if (pc.isInParty()) {

           writeC(100 * pc.getCurrentHp() / pc.getMaxHp());
         } else {
           writeC(255);
         }
         writeC(pc.hasSkillEffect(22017) ? 8 : 0);
         writeC(0);
         writeC(0);
         writeC(255);
         writeC(255);
         writeC(0);
         int value = 0;
         if (Config.ServerAdSetting.PolyEvent) {
           value = 11;
         }
         else if (pc.getLevel() >= 80) {
           value = 11;
         } else if (pc.getLevel() >= 55) {
           value = (pc.getLevel() - 25) / 5;
         } else if (pc.getLevel() >= 52) {
           value = 5;
         } else if (pc.getLevel() >= 50) {
           value = 4;
         } else if (pc.getLevel() >= 15) {
           value = pc.getLevel() / 15;
         }

         writeC(value);
         if (pc.isInParty()) {

           writeC(100 * pc.getCurrentMp() / pc.getMaxMp());
         } else {
           writeC(255);
         }
         writeH(0);
       }

       private void buildPacket(L1PcInstance pc) {
         int status = 4;

         if (pc.isInvisble() || pc.isGmInvis()) {
           status |= 0x2;
         }
         if (pc.isBrave()) {
           status |= 0x10;
         }
         if (pc.isElfBrave()) {
           status |= 0x10;
           status |= 0x20;
         }
         if (pc.isBlood_lust()) {
           status |= 0x10;
         }
         if (pc.isElfBraveMagicShort() || pc.isElfBraveMagicLong() || pc.hasSkillEffect(177)) {
           status |= 0x10;
         }
         if (pc.isFastMovable() || pc.isFruit()) {
           status |= 0x40;
         }
         if (pc.isGhost()) {
           status |= 0x80;
         }
         if (pc.isParalyzed()) {
           status |= 0x8;
         }

         writeC(19);
         writeH(119);
         writeC(8);
         writeBit(pc.getX(), pc.getY());

         writeC(16);
         writeBit(pc.getId());

         writeC(24);
         writeBit(pc.isDead() ? pc.getTempCharGfxAtDead() : pc.getCurrentSpriteId());

         writeC(32);
         if (pc.isDead()) {
           writeBit(8L);
         } else if (pc.isPrivateShop()) {
           writeBit(70L);
         } else if (pc.isFishing()) {
           writeBit(71L);
         } else {
           writeBit(pc.getCurrentWeapon());
         }

         writeC(40);
         writeBit(pc.getHeading());

         writeC(48);
         writeBit(pc.getLight().getOwnLightSize());

         writeC(56);
         writeBit(1L);

         writeC(64);
         writeBit(pc.getLawful());

         writeC(74);
         writeC((pc.getName().getBytes()).length);
         writeByte(pc.getName().getBytes());

         writeC(82);
         if (pc.getTitle() == null || pc.getTitle().equals("")) {
           writeC(0);
         } else {
           writeC((pc.getTitle().getBytes()).length);
           writeByte(pc.getTitle().getBytes());
         }

         writeC(88);
         writeBit(pc.isHaste() ? 1L : 0L);

         writeC(96);
         if (pc.isBrave() || pc.isBlood_lust() || pc.isElfBraveMagicShort()) {
           writeBit(1L);
         } else if (pc.isElfBrave()) {
           writeBit(3L);
         } else if (pc.isFastMovable() || pc.isFruit()) {
           writeBit(4L);
         } else {
           writeBit(0L);
         }

         writeC(104);
         writeBit(0L);

         writeC(112);
         writeBit(pc.isGhost() ? 1L : 0L);

         writeC(120);
         writeBit((pc.getParalysis() != null) ? 1L : 0L);

         writeC(128);
         writeC(1);
         writeBit(1L);

         writeC(136);
         writeC(1);
         writeBit((pc.isInvisble() || pc.isGmInvis()) ? 1L : 0L);

         writeC(144);
         writeC(1);
         writeBit(0L);

         writeC(152);
         writeC(1);
         int emblemId = 0;
         if (pc.getClanid() > 0) {
           L1Clan clan = pc.getClan();
           if (clan != null)
             emblemId = clan.getEmblemId();
         }
         writeBit(emblemId);

         writeC(162);
         writeC(1);
         String clanName = pc.getClanname();
         if (pc.getRedKnightClanId() != 0) {
           clanName = "紅騎士團";
         } else if ((pc.getMapId() >= 1005 && pc.getMapId() <= 1070) || pc.getMapId() == 501 || pc
           .getMapId() == 2009) {
           clanName = String.valueOf(pc.getMapId());
         }
         if (clanName == null || clanName.equals("")) {
           writeC(0);
         } else {
           writeC((clanName.getBytes()).length);
           writeByte(clanName.getBytes());
         }

         writeC(170);
         writeC(1);
         writeBit(0L);

         writeC(176);
         writeC(1);
         writeBit(0L);

         writeC(184);
         writeC(1);

         writeC(255);
         writeC(255);
         writeC(255);
         writeC(255);
         writeC(255);
         writeC(255);
         writeC(255);
         writeC(255);
         writeC(255);
         writeC(1);

         writeC(192);
         writeC(1);
         writeBit(0L);

         writeC(202);
         writeC(1);
         if (pc.getShopChat() == null || (pc.getShopChat()).length <= 0) {
           writeBit(0L);
         } else {
           writeC((pc.getShopChat()).length);
           writeByte(pc.getShopChat());
         }

         writeC(208);
         writeC(1);
         writeC(255);
         writeC(255);
         writeC(255);
         writeC(255);
         writeC(255);
         writeC(255);
         writeC(255);
         writeC(255);
         writeC(255);
         writeC(1);

         writeC(216);
         writeC(1);
         writeBit(0L);

         writeC(224);
         writeC(1);
         int value = 0;
         if (Config.ServerAdSetting.PolyEvent) {
           value = 11;
         }
         else if (pc.getLevel() >= 80) {
           value = 11;
         } else if (pc.getLevel() >= 55) {
           value = (pc.getLevel() - 25) / 5;
         } else if (pc.getLevel() >= 52) {
           value = 5;
         } else if (pc.getLevel() >= 50) {
           value = 4;
         } else if (pc.getLevel() >= 15) {
           value = pc.getLevel() / 15;
         }

         writeC(value);

         writeC(240);
         writeC(1);
         writeC(255);
         writeC(255);
         writeC(255);
         writeC(255);
         writeC(255);
         writeC(255);
         writeC(255);
         writeC(255);
         writeC(255);
         writeC(1);

         writeC(128);
         writeC(2);
         writeC(0);

         writeC(136);
         writeC(2);


         MJInstanceObject instobj = MJInstanceSpace.getInstance().getOpenObject(pc.getMapId());
         if (instobj != null) {
           writeC(instobj.getMarkStatus(pc));
         } else if (pc.getRedKnightClanId() != 0) {
           writeC(7);
         } else if (pc.getMapId() == 99) {
           writeC(30);
         } else {
           writeC(29);
         }

         writeC(152);
         writeC(2);
         writeC(0);

         writeC(128);
         writeC(2);
         writeBit(0L);

         writeC(0);
         writeC(0);
       }


       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_OwnCharPack";
       }
     }


