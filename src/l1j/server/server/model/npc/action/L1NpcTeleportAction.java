 package l1j.server.server.model.npc.action;

 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Location;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.npc.L1NpcHtml;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import org.w3c.dom.Element;


 public class L1NpcTeleportAction
   extends L1NpcXmlAction
 {
   private final L1Location _loc;
   private final int _heading;
   private final int _price;
   private final boolean _effect;

   public L1NpcTeleportAction(Element element) {
     super(element);

     int x = L1NpcXmlParser.getIntAttribute(element, "X", -1);
     int y = L1NpcXmlParser.getIntAttribute(element, "Y", -1);
     int mapId = L1NpcXmlParser.getIntAttribute(element, "Map", -1);
     this._loc = new L1Location(x, y, mapId);

     this._heading = L1NpcXmlParser.getIntAttribute(element, "Heading", 5);

     this._price = L1NpcXmlParser.getIntAttribute(element, "Price", 0);
     this._effect = L1NpcXmlParser.getBoolAttribute(element, "Effect", true);
   }



   public L1NpcHtml execute(String actionName, L1PcInstance pc, L1Object obj, byte[] args) {
     if (this._loc.getMapId() == 2010 && (pc.getLevel() < 45 || pc.getLevel() > 51)) {
       return L1NpcHtml.HTML_CLOSE;
     }

     if (!pc.getInventory().checkItem(40308, this._price)) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(5359));
       return L1NpcHtml.HTML_CLOSE;
     }

     L1Location location = new L1Location();
     location.set(this._loc);

     if (location.getMapId() == 340 || location.getMapId() == 350 || location.getMapId() == 360 || location
       .getMapId() == 370) {
       location = L1Location.randomLocation(this._loc, 1, 10, true);
     }

     pc.getInventory().consumeItem(40308, this._price);
     pc.start_teleport(location.getX(), location.getY(), location.getMapId(), this._heading, 18339, this._effect, false);


     return null;
   }
 }


