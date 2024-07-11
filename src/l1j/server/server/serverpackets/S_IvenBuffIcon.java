 package l1j.server.server.serverpackets;


 public class S_IvenBuffIcon
   extends ServerBasePacket
 {
   public static final int SHOW_INVEN_BUFFICON = 110;

   public S_IvenBuffIcon(int skillId, boolean on, int msgNum, int time) {
     writeC(19);
     writeH(110);

     writeC(8);
     writeBit(on ? 2L : 3L);

     writeC(16);
     writeBit(skillId);

     writeC(24);
     writeBit(time);

     writeC(32);




     int duration_show_type = 8;
     if (skillId >= 4075 && skillId <= 4094)
       duration_show_type = 9;
     writeBit(duration_show_type);

     writeC(40);

     int iven_icon = skillId;
     if (skillId >= 4075 && skillId <= 4094)
       iven_icon = 6679;
     writeBit(iven_icon);

     writeC(48);
     writeBit(0L);

     writeC(56);



     int skill_order_number = 3;
     if (skillId >= 4075 && skillId <= 4094)
       skill_order_number = 0;
     writeBit(skill_order_number);

     writeC(64);
     writeBit(msgNum);

     writeC(72);
     writeBit(0L);

     writeC(80);
     writeBit(0L);

     writeC(88);
     writeBit(1L);

     writeC(96);
     int overlap_buff_icon = 0;
     if (skillId >= 4075 && skillId <= 4094)
       overlap_buff_icon = 1;
     writeBit(overlap_buff_icon);

     writeC(104);
     int main_tooltip_str_id = 0;
     if (skillId >= 4075 && skillId <= 4094)
       main_tooltip_str_id = 4328;
     writeBit(main_tooltip_str_id);

     writeC(112);
     int buff_icon_priority = 0;
     if (skillId >= 4075 && skillId <= 4094)
       buff_icon_priority = 1;
     writeBit(buff_icon_priority);

     writeH(0);
   }





   public S_IvenBuffIcon(boolean on) {
     writeC(19);
     writeH(110);
     writeC(8);
     writeBit(on ? 2L : 3L);
     writeC(16);
     writeBit(8463L);
     if (on) {
       writeC(24);
       writeBit(60L);
       writeC(32);




       writeBit(10L);
       writeC(40);
       writeBit(8463L);
       writeC(48);
       writeBit(0L);
       writeC(56);



       writeBit(3L);
       writeC(64);
       writeBit(5119L);
       writeC(72);
       writeBit(0L);
       writeC(80);
       writeBit(0L);
       writeC(88);
       writeBit(1L);
       writeC(96);
       writeBit(0L);
       writeC(104);
       writeBit(0L);
       writeC(112);
       writeBit(0L);
     } else {
       writeC(48);
       writeBit(0L);
       writeC(80);
       writeBit(0L);
     }
     writeH(0);
   }

   public byte[] getContent() {
     return getBytes();
   }
 }


