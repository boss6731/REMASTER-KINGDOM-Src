 package l1j.server.server.serverpackets;

 public class S_UnityIcon
   extends ServerBasePacket
 {
   public S_UnityIcon(byte[] datas) {
     writeC(108);
     writeC(20);
     writeC(126);
     writeByte(datas);
   }

   public S_UnityIcon(int DECREASE, int DECAY_POTION, int SILENCE, int VENOM_RESIST, int WEAKNESS, int DISEASE, int DRESS_EVASION, int BERSERKERS, int NATURES_TOUCH, int WIND_SHACKLE, int ERASE_MAGIC, int ADDITIONAL_FIRE, int ELEMENTAL_FALL_DOWN, int ELEMENTAL_FIRE, int STRIKER_GALE, int SOUL_OF_FLAME, int POLLUTE_WATER, int EXP_POTION, int SCROLL, int SCROLLTPYE, int CONCENTRATION, int INSIGHT, int PANIC, int MORTAL_BODY, int HORROR_OF_DEATH, int FEAR, int PATIENCE, int GUARD_BREAK, int DRAGON_SKIN, int STATUS_FRUIT, int COMA, int COMA_TYPE, int CRAY_TIME, int CRAY, int MAAN_TIME, int MAAN, int FEATHER_BUFF, int FEATHER_TYPE, int SCROLL_TIME) {
     writeC(108);
     writeC(20);
     writeC(126);
     writeC(0);
     writeC(0);
     writeD(0);
     writeC(DECREASE);
     writeC(DECAY_POTION);
     writeC(0);
     writeC(SILENCE);
     writeC(VENOM_RESIST);

     writeC(WEAKNESS);
     writeC(DISEASE);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(DRESS_EVASION);

     writeC(BERSERKERS);
     writeC(NATURES_TOUCH);
     writeC(WIND_SHACKLE);
     writeC(ERASE_MAGIC);
     writeC(0);
     writeC(ADDITIONAL_FIRE);
     writeC(ELEMENTAL_FALL_DOWN);
     writeC(0);
     writeC(ELEMENTAL_FIRE);
     writeC(0);

     writeC(0);
     writeC(0);
     writeC(STRIKER_GALE);
     writeC(SOUL_OF_FLAME);
     writeC(POLLUTE_WATER);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);

     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(EXP_POTION);
     writeC(SCROLL);
     writeC(SCROLLTPYE);

     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(CONCENTRATION);
     writeC(INSIGHT);
     writeC(PANIC);
     writeC(MORTAL_BODY);
     writeC(HORROR_OF_DEATH);
     writeC(FEAR);

     writeC(PATIENCE);
     writeC(GUARD_BREAK);
     writeC(DRAGON_SKIN);
     writeC(STATUS_FRUIT);
     writeC(20);
     writeC(0);
     writeC(COMA);
     writeC(COMA_TYPE);
     writeC(0);
     writeC(0);

     writeC(38);
     writeC(1);
     writeC(0);
     writeC(0);
     writeD(System.currentTimeMillis());
     writeC(CRAY_TIME);
     writeC(CRAY);

     writeC(MAAN_TIME);
     writeC(MAAN);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);

     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(FEATHER_BUFF);
     writeC(FEATHER_TYPE);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);

     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(134);
     writeC(1);
     writeC(SCROLL_TIME);
     writeC(0);

     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);

     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(186);
     writeC(36);
     writeC(0);
     writeC(0);

     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);

     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);

     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(227);
     writeC(0);

     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);

     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);

     writeC(0);
     writeC(0);
     writeC(0);
     writeC(180);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);

     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);

     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(0);
     writeC(227);
     writeC(6);

     writeC(0);
     writeC(0);
     writeC(0);
     writeH(0);
   }


   public byte[] getContent() {
     return getBytes();
   }
 }


