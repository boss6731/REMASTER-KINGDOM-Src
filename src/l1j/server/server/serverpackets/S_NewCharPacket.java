 package l1j.server.server.serverpackets;

 import l1j.server.server.model.Instance.L1PcInstance;

 public class S_NewCharPacket
   extends ServerBasePacket {
   private static final String _S__25_NEWCHARPACK = "[S] New Char Packet";

   public S_NewCharPacket(L1PcInstance pc) {
     buildPacket(pc);
   }

   private void buildPacket(L1PcInstance pc) {
     writeC(204);
     writeS(pc.getName());
     writeS("");
     writeC(pc.getType());
     writeC(pc.get_sex());
     writeH(pc.getLawful());
     writeH(pc.getMaxHp());
     writeH(pc.getMaxMp());
     writeC(pc.getAC().getAc());
     writeC(pc.getLevel());
     writeC(pc.getAbility().getStr());
     writeC(pc.getAbility().getDex());
     writeC(pc.getAbility().getCon());
     writeC(pc.getAbility().getWis());
     writeC(pc.getAbility().getCha());
     writeC(pc.getAbility().getInt());
     writeC(0);
     writeD(pc.getBirthDay());

     int code = pc.getLevel() ^ pc.getAbility().getStr() ^ pc.getAbility().getDex() ^ pc.getAbility().getCon() ^ pc.getAbility().getWis() ^ pc.getAbility().getCha() ^ pc.getAbility().getInt();
     writeC(code & 0xFF);
     writeD(0);
   }




   public byte[] getContent() {
     return getBytes();
   }

   public String getType() {
     return "[S] New Char Packet";
   }
 }


