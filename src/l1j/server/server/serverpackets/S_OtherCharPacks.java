 package l1j.server.server.serverpackets;

 import l1j.server.MJInstanceSystem.MJInstanceObject;
 import l1j.server.MJInstanceSystem.MJInstanceSpace;
 import l1j.server.MJPassiveSkill.MJPassiveID;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.utils.StringUtil;




 public class S_OtherCharPacks
   extends ServerBasePacket
 {
   private static final String S_OTHER_CHAR_PACKS = "[S] S_OtherCharPacks";
   private byte[] _byte = null;
   private static final byte[] MINUS_BYTES = new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, 1 };
   public S_OtherCharPacks(L1PcInstance pc, L1PcInstance user) {
     buildPacket(pc, user);
   }

   private void buildPacket(L1PcInstance pc, L1PcInstance user) {
     writeC(19);
     writeH(119);

     writeC(8);
     writeBit(pc.getX(), pc.getY());

     writeC(16);
     writeBit(pc.getId());

     writeC(24);
     if (pc.isDead()) {
       writeBit(pc.getTempCharGfxAtDead());
     } else {
       writeBit(pc.getTempCharGfx());
     }

     writeC(32);
     if (pc.isDead()) {
       writeBit(pc.getStatus());
     } else if (pc.isPrivateShop()) {
       writeBit(70L);
     } else if (pc.isFishing()) {
       writeBit(71L);
     } else {
       writeBit(pc.getCurrentWeapon());
     }

     writeC(40);
     writeC(pc.getMoveState().getHeading());

     writeC(48);
     writeC(pc.getLight().getChaLightSize());

     writeC(56);
     writeC(1);

     writeC(64);
     writeBit(pc.getLawful());

     writeC(74);
     writeBytesWithLength(pc.getName().getBytes());

     writeC(82);
     if (StringUtil.isNullOrEmpty(pc.getTitle())) {
       writeC(0);
     } else {

       writeBytesWithLength(pc.getTitle().getBytes());
     }

     writeC(88);
     writeB(pc.isHaste());

     writeC(96);
     int brave = 0;
     if (pc.isBrave()) {
       brave = 1;
     } else if (pc.isElfBrave()) {
       brave = 3;
     } else if (pc.isFastMovable()) {
       brave = (pc.isPassive(MJPassiveID.MOVING_ACCELERATION_PASS.toInt()) || pc.isPassive(MJPassiveID.HOLY_WALK_EVOLUTION.toInt())) ? 3 : 4;
     }
     else if (pc.isFruit()) {
       brave = pc.isPassive(MJPassiveID.DARK_HORSE.toInt()) ? 3 : 4;
     }
     else if (pc.hasSkillEffect(186)) {

       brave = 6;
     } else if (pc.hasSkillEffect(178)) {

       brave = 9;
     } else if (pc.hasSkillEffect(177)) {

       brave = 13;
     }
     writeC(brave);

     writeC(104);
     writeC((pc.getPearl() == 1) ? 8 : 0);


     writeC(112);
     writeB(pc.isGhost());

     writeH(384);
     writeC(1);

     writeH(392);
     writeB((pc.isInvisble() || pc.isGmInvis()));

     writeH(408);
     writeBit((pc.getClanid() > 0) ? pc.getClan().getEmblemId() : 0L);

     writeH(418);
     if (pc.getClanid() == 0 || StringUtil.isNullOrEmpty(pc.getClanname())) {
       writeC(0);
     } else {
       writeBytesWithLength(pc.getClanname().getBytes());
     }

     writeH(426);
     writeC(0);

     writeH(432);
     writeC(0);

     writeH(440);
     if (pc.isInParty() && pc.getParty().isMember(user)) {
       writeC(100 * pc.getCurrentHp() / pc.getMaxHp());
     } else {
       writeByte(MINUS_BYTES);
     }

     writeH(448);
     writeC(0);

     writeH(458);
     if (pc.getShopChat() == null || (pc.getShopChat()).length <= 0) {
       writeC(0);
     } else {
       writeBytesWithLength(pc.getShopChat());
     }

     writeH(464);
     writeByte(MINUS_BYTES);

     writeH(472);
     writeC(0);

     writeH(480);
     int value = 0;
     if (pc.getLevel() >= 80) {
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

     writeH(496);
     if (pc.isInParty() && pc.getParty().isMember(user)) {
       writeC(100 * pc.getCurrentMp() / pc.getMaxMp());
     } else {
       writeByte(MINUS_BYTES);
     }


     writeH(640);
     writeC(0);


     writeH(648);

     MJInstanceObject instobj = MJInstanceSpace.getInstance().getOpenObject(pc.getMapId());
     if (instobj != null) {
       writeC(instobj.getMarkStatus(pc));
     } else if (pc.getMapId() == 99) {
       if (pc.getClanid() == user.getClanid()) {
         writeC(3);
       } else {
         writeC(9);
       }

     } else if (pc.getClanid() == 290040001) {
       writeC(7);
     } else {
       writeC(0);
     }


     writeH(752);
     writeC(pc.getType());

     writeH(912);
     writeB(pc.isFourgear());

     writeH(922);
     if (pc.isKnight() && pc.isPassive(MJPassiveID.RAISING_WEAPON.toInt()) && pc.getCurrentWeapon() == 50) {

       writeBytesWithLength(S_SpeedChange.RAIGING_WEAPONE_ATTACK_ON);
     } else if (pc.hasSkillEffect(5053)) {
       if (pc.getVanguardType()) {
         writeBytesWithLength(S_SpeedChange.BANGUARD_LONG_FORM_ON);
       } else if (!pc.getVanguardType()) {
         writeBytesWithLength(S_SpeedChange.BANGUARD_SHORT_FORM_ON);
       } else {
         writeC(0);
       }
     } else if (pc.hasSkillEffect(5157)) {
       writeBytesWithLength(S_SpeedChange.SHOCK_ATTACK_ON);
     } else if (pc.hasSkillEffect(97) && pc.isPassive(MJPassiveID.BLIND_HIDDING_ASSASSIN.toInt())) {
       writeBytesWithLength(S_SpeedChange.BLIND_HIDING_ON);
     } else {
       writeC(0);
     }

     writeH(0);
   }




   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] S_OtherCharPacks";
   }
 }


