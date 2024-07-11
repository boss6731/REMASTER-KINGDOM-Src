         package l1j.server.server.serverpackets;

         import l1j.server.server.model.Instance.L1ItemInstance;

         public class S_ArrowsEquipment
           extends ServerBasePacket {
           public S_ArrowsEquipment(L1ItemInstance item) {
             writeC(108);
             writeC(205);
             writeD(item.getId());
             writeH(0);
           }


           public byte[] getContent() {
             return getBytes();
           }
         }


