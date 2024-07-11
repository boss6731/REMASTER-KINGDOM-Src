     package l1j.server.server.serverpackets;

     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.L1Object;
     import l1j.server.server.model.L1World;

     public class S_DoActionGFX
       extends ServerBasePacket
     {
       private static final String S_DOACTIONGFX = "[S] S_SkillGFX";
       public static int ACTION_MAGIC = 22;

       public S_DoActionGFX(int objectId, int actionId) {
         writeC(226);
         writeD(objectId);
         writeC(actionId);

         L1Object obj = L1World.getInstance().findObject(objectId);
         if (obj instanceof L1PcInstance) {
           L1PcInstance owner = (L1PcInstance)obj;
           long interval = owner.getCurrentSpriteInterval(actionId);
           owner.setLastMotionMillis(interval);
         }
       }


       public byte[] getContent() {
         return getBytes();
       }


       public String getType() {
         return "[S] S_SkillGFX";
       }
     }


