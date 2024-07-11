     package l1j.server.server.serverpackets;

     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.L1Clan;
     import l1j.server.server.model.L1World;

     public class S_EinhasadClanBuff
       extends ServerBasePacket
     {
       private static final String _S_EinhasadClanBuff = "[S] S_EinhasadClanBuff";

       public S_EinhasadClanBuff(L1PcInstance pc) {
         writeC(19);
         writeC(251);
         writeC(3);

         L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
         if (clan == null) {
           writeC(251);
           writeC(3);
           writeC(10);
           writeC(0);
           writeH(0);

           return;
         }
         if (clan.getBuffFirst() != 0) {
           writeC(10);
           writeC(5);
           writeC(8);
           writeBit(clan.getBuffFirst());
           writeC(16);
           if (clan.getEinhasadBlessBuff() == 0)
           { writeC(1); }
           else if (clan.getEinhasadBlessBuff() == clan.getBuffFirst())
           { writeC(2); }
           else { writeC(3); }

         }

         if (clan.getBuffSecond() != 0) {
           writeC(10);
           writeC(5);
           writeC(8);
           writeBit(clan.getBuffSecond());
           writeC(16);
           if (clan.getEinhasadBlessBuff() == 0)
           { writeC(1); }
           else if (clan.getEinhasadBlessBuff() == clan.getBuffSecond())
           { writeC(2); }
           else { writeC(3); }

         }

         if (clan.getBuffThird() != 0) {
           writeC(10);
           writeC(5);
           writeC(8);
           writeBit(clan.getBuffThird());
           writeC(16);
           if (clan.getEinhasadBlessBuff() == 0)
           { writeC(1); }
           else if (clan.getEinhasadBlessBuff() == clan.getBuffThird())
           { writeC(2); }
           else { writeC(3); }

         }
         writeC(16);
         writeC(1);

         writeH(0);
       }


       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_EinhasadClanBuff";
       }
     }


