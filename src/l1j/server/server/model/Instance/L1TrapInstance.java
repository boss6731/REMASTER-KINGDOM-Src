 package l1j.server.server.model.Instance;

 import java.util.List;
 import java.util.Random;
 import java.util.concurrent.CopyOnWriteArrayList;
 import l1j.server.server.model.L1Location;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.map.L1Map;
 import l1j.server.server.model.trap.L1Trap;
 import l1j.server.server.serverpackets.S_RemoveObject;
 import l1j.server.server.serverpackets.S_Trap;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.types.Point;




 public class L1TrapInstance
   extends L1Object
 {
   private static final long serialVersionUID = 1L;
   private final L1Trap _trap;
   private final Point _baseLoc = new Point();
   private final Point _rndPt = new Point();

   private final int _span;
   private boolean _isEnable = true;
   private final String _nameForView;
   private List<L1PcInstance> _knownPlayers = new CopyOnWriteArrayList<>();

   private static final Random _random = new Random(System.nanoTime());


   public L1TrapInstance(int id, L1Trap trap, L1Location loc, Point rndPt, int span) {
     setId(id);
     this._trap = trap;
     getLocation().set(loc);
     this._baseLoc.set((Point)loc);
     this._rndPt.set(rndPt);
     this._span = span;
     this._nameForView = "trap";

     resetLocation();
   }

   public L1TrapInstance(int id, L1Location loc) {
     setId(id);
     this._trap = L1Trap.newNull();
     getLocation().set(loc);
     this._span = 0;
     this._nameForView = "trap base";
   }

   public void resetLocation() {
     if (this._rndPt.getX() == 0 && this._rndPt.getY() == 0) {
       return;
     }
     L1Map map = null;
     for (int i = 0; i < 50; i++) {

       int rndX = _random.nextInt(this._rndPt.getX() + 1) * ((_random.nextInt(2) == 1) ? 1 : -1);

       int rndY = _random.nextInt(this._rndPt.getY() + 1) * ((_random.nextInt(2) == 1) ? 1 : -1);

       rndX += this._baseLoc.getX();
       rndY += this._baseLoc.getY();

       map = getLocation().getMap();
       if (map.isInMap(rndX, rndY) && map.isPassable(rndX, rndY)) {
         getLocation().set(rndX, rndY);
         break;
       }
     }
   }

   public void enableTrap() {
     this._isEnable = true;
   }

   public void disableTrap() {
     this._isEnable = false;

     for (L1PcInstance pc : this._knownPlayers) {
       pc.removeKnownObject(this);
       pc.sendPackets((ServerBasePacket)new S_RemoveObject(this));
     }
     this._knownPlayers.clear();
   }






   public boolean isEnable() {
     return this._isEnable;
   }

   public int getSpan() {
     return this._span;
   }

   public void onTrod(L1PcInstance trodFrom) {
     this._trap.onTrod(trodFrom, this);
   }

   public void onDetection(L1PcInstance caster) {
     this._trap.onDetection(caster, this);
   }


   public void onPerceive(L1PcInstance perceivedFrom) {
     if (perceivedFrom == null)
       return;
     if (perceivedFrom.hasSkillEffect(2002)) {
       perceivedFrom.addKnownObject(this);
       if (perceivedFrom.getAI() == null)
         perceivedFrom.sendPackets((ServerBasePacket)new S_Trap(this, this._nameForView));
       this._knownPlayers.add(perceivedFrom);
     }
   }
 }


