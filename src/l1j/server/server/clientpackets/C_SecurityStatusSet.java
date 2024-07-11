 package l1j.server.server.clientpackets;

 import l1j.server.MJWarSystem.MJCastleWar;
 import l1j.server.MJWarSystem.MJCastleWarBusiness;
 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_CloseList;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class C_SecurityStatusSet extends ClientBasePacket {
   private static final String C_SECURITYSTATUSSET = "[C] C_SecurityStatusSet";

   public C_SecurityStatusSet(byte[] abyte0, GameClient client) {
     super(abyte0);

     int objid = readD();
     int type = readC();

     int unknow = readD();

     L1PcInstance pc = client.getActiveChar();
     if (pc == null)
       return;
     L1Clan clan = L1World.getInstance().getClan(pc.getClanid());

     MJCastleWar war = MJCastleWarBusiness.getInstance().get(clan.getCastleId());
     if (war.getCastleSecurity() == type) {
       return;
     }
     int money = war.getPublicMoney();
     if (money < 100000) {
       return;
     }
     if (type == 1)
       war.setPublicMoney(money - 100000);
     war.setCastleSecurity(type);
     MJCastleWarBusiness.getInstance().updateCastle(clan.getCastleId());
     pc.sendPackets((ServerBasePacket)new S_CloseList(objid));
   }


   public String getType() {
     return "[C] C_SecurityStatusSet";
   }
 }


