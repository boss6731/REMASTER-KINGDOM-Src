     package l1j.server.server.serverpackets;

     import l1j.server.server.model.Instance.L1FollowerInstance;
     import l1j.server.server.model.Instance.L1PcInstance;


     public class S_FollowerPack
       extends ServerBasePacket
     {
       private static final String S_FOLLOWER_PACK = "[S] S_FollowerPack";
       private static final int STATUS_POISON = 1;

       public S_FollowerPack(L1FollowerInstance follower, L1PcInstance pc) {
         writeC(186);
         writeH(follower.getX());
         writeH(follower.getY());
         writeD(follower.getId());
         writeH(follower.getCurrentSpriteId());
         writeC(follower.getStatus());
         writeC(follower.getHeading());
         writeC(follower.getLight().getChaLightSize());
         writeC(follower.getMoveSpeed());
         writeD(0);
         writeH(0);
         writeS(follower.getNameId());
         writeS(follower.getTitle());
         int status = 0;
         if (follower.getPoison() != null &&
           follower.getPoison().getEffectId() == 1) {
           status |= 0x1;
         }

         writeC(status);
         writeD(0);
         writeS(null);
         writeS(null);
         writeC(0);
         writeC(255);
         writeC(0);
         writeC(follower.getLevel());
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
         return "[S] S_FollowerPack";
       }
     }


