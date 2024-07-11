     package l1j.server.server.command.executor;

     import java.lang.reflect.Constructor;
     import java.util.StringTokenizer;
     import l1j.server.server.GMCommands;
     import l1j.server.server.IdFactory;
     import l1j.server.server.datatables.NpcTable;
     import l1j.server.server.model.Instance.L1NpcInstance;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.L1Object;
     import l1j.server.server.model.L1World;
     import l1j.server.server.serverpackets.S_SystemMessage;
     import l1j.server.server.serverpackets.ServerBasePacket;
     import l1j.server.server.templates.L1Npc;


     public class L1GfxId
       implements L1CommandExecutor
     {
       public static L1CommandExecutor getInstance() {
         return new L1GfxId();
       }


       public void execute(L1PcInstance pc, String cmdName, String param) {
         try {
           StringTokenizer st = new StringTokenizer(param);
           int gfxid = Integer.parseInt(st.nextToken(), 10);
           int count = Integer.parseInt(st.nextToken(), 10);
           int loc_x = pc.getX();
           int loc_y = pc.getY();

           for (int i = 0; i < count; i++) {
             L1Npc l1npc = NpcTable.getInstance().getTemplate(45001);
             if (l1npc != null) {
               String s = l1npc.getImpl();
               Constructor<?> constructor = Class.forName("l1j.server.server.model.Instance." + s + "Instance").getConstructors()[0];
               Object[] aobj = { l1npc };
               L1NpcInstance npc = (L1NpcInstance)constructor.newInstance(aobj);
               npc.setId(IdFactory.getInstance().nextId());
               npc.setCurrentSprite(gfxid + i);
               npc.setNameId("" + (gfxid + i) + "");
               npc.setMap(pc.getMapId());

               int k = i % 5;
               if (k == 0 && i > 0) {
                 loc_x -= 4;
                 loc_y += 4;
               }
               npc.setX(loc_x + k * 2);
               npc.setY(loc_y + k * 2);



               npc.setHomeX(npc.getX());
               npc.setHomeY(npc.getY());
               npc.setHeading(5);
               npc.setStatus(0);
               L1World.getInstance().storeObject((L1Object)npc);
               L1World.getInstance().addVisibleObject((L1Object)npc);

               GMCommands.getInstance().add_list(npc);
             }
           }
         } catch (Exception exception) {
           pc.sendPackets((ServerBasePacket)new S_SystemMessage(cmdName + " 請輸入[id]作為[出現的號碼]。"));
         }
       }
     }


