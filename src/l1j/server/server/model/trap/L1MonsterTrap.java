 package l1j.server.server.model.trap;

 import java.lang.reflect.Constructor;
 import java.util.ArrayList;
 import java.util.List;
 import l1j.server.server.IdFactory;
 import l1j.server.server.datatables.NpcTable;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Location;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.model.map.L1Map;
 import l1j.server.server.storage.TrapStorage;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.types.Point;

 public class L1MonsterTrap
   extends L1Trap
 {
   private final int _npcId;
   private final int _count;
   private L1Npc _npcTemp = null;
   private Constructor<?> _constructor = null;

   public L1MonsterTrap(TrapStorage storage) {
     super(storage);

     this._npcId = storage.getInt("monsterNpcId");
     this._count = storage.getInt("monsterCount");
   }

   private void addListIfPassable(List<Point> list, L1Map map, Point pt) {
     if (map.isPassable(pt)) {
       list.add(pt);
     }
   }

   private List<Point> getSpawnablePoints(L1Location loc, int d) {
     List<Point> result = new ArrayList<>();
     L1Map m = loc.getMap();
     int x = loc.getX();
     int y = loc.getY();
     for (int i = 0; i < d; i++) {
       addListIfPassable(result, m, new Point(d - i + x, i + y));
       addListIfPassable(result, m, new Point(-(d - i) + x, -i + y));
       addListIfPassable(result, m, new Point(-i + x, d - i + y));
       addListIfPassable(result, m, new Point(i + x, -(d - i) + y));
     }
     return result;
   }

   private Constructor<?> getConstructor(L1Npc npc) throws ClassNotFoundException {
     return Class.forName("l1j.server.server.model.Instance." + npc
         .getImpl() + "Instance")
       .getConstructors()[0];
   }

   private L1NpcInstance createNpc() throws Exception {
     if (this._npcTemp == null) {
       this._npcTemp = NpcTable.getInstance().getTemplate(this._npcId);
     }
     if (this._constructor == null) {
       this._constructor = getConstructor(this._npcTemp);
     }

     return (L1NpcInstance)this._constructor
       .newInstance(new Object[] { this._npcTemp });
   }

   private void spawn(L1Location loc) throws Exception {
     L1NpcInstance npc = createNpc();
     npc.setId(IdFactory.getInstance().nextId());
     npc.getLocation().set(loc);
     npc.setHomeX(loc.getX());
     npc.setHomeY(loc.getY());
     L1World.getInstance().storeObject((L1Object)npc);
     L1World.getInstance().addVisibleObject((L1Object)npc);

     npc.onNpcAI();
     npc.getLight().turnOnOffLight();
     npc.startChat(4);
     npc.startChat(0);
   }

   public void onTrod(L1PcInstance trodFrom, L1Object trapObj) {
     // Byte code:
     //   0: aload_0
     //   1: aload_2
     //   2: invokevirtual sendEffect : (Ll1j/server/server/model/L1Object;)V
     //   5: aload_0
     //   6: aload_2
     //   7: invokevirtual getLocation : ()Ll1j/server/server/model/L1Location;
     //   10: iconst_5
     //   11: invokespecial getSpawnablePoints : (Ll1j/server/server/model/L1Location;I)Ljava/util/List;
     //   14: astore_3
     //   15: aload_3
     //   16: invokeinterface isEmpty : ()Z
     //   21: ifeq -> 25
     //   24: return
     //   25: iconst_0
     //   26: istore #4
     //   28: aload_3
     //   29: invokeinterface iterator : ()Ljava/util/Iterator;
     //   34: astore #5
     //   36: aload #5
     //   38: invokeinterface hasNext : ()Z
     //   43: ifeq -> 91
     //   46: aload #5
     //   48: invokeinterface next : ()Ljava/lang/Object;
     //   53: checkcast l1j/server/server/types/Point
     //   56: astore #6
     //   58: aload_0
     //   59: new l1j/server/server/model/L1Location
     //   62: dup
     //   63: aload #6
     //   65: aload_2
     //   66: invokevirtual getMap : ()Ll1j/server/server/model/map/L1Map;
     //   69: invokespecial <init> : (Ll1j/server/server/types/Point;Ll1j/server/server/model/map/L1Map;)V
     //   72: invokespecial spawn : (Ll1j/server/server/model/L1Location;)V
     //   75: iinc #4, 1
     //   78: aload_0
     //   79: getfield _count : I
     //   82: iload #4
     //   84: if_icmpgt -> 88
     //   87: return
     //   88: goto -> 36
     //   91: goto -> 28
     //   94: astore #4
     //   96: aload #4
     //   98: invokevirtual printStackTrace : ()V
     //   101: return
     // Line number table:
     //   Java source line number -> byte code offset
     //   #88	-> 0
     //   #90	-> 5
     //   #92	-> 15
     //   #93	-> 24
     //   #97	-> 25
     //   #99	-> 28
     //   #100	-> 58
     //   #101	-> 75
     //   #102	-> 78
     //   #103	-> 87
     //   #105	-> 88
     //   #107	-> 94
     //   #108	-> 96
     //   #110	-> 101
     // Local variable table:
     //   start	length	slot	name	descriptor
     //   58	30	6	pt	Ll1j/server/server/types/Point;
     //   28	66	4	cnt	I
     //   96	5	4	e	Ljava/lang/Exception;
     //   0	102	0	this	Ll1j/server/server/model/trap/L1MonsterTrap;
     //   0	102	1	trodFrom	Ll1j/server/server/model/Instance/L1PcInstance;
     //   0	102	2	trapObj	Ll1j/server/server/model/L1Object;
     //   15	87	3	points	Ljava/util/List;
     // Local variable type table:
     //   start	length	slot	name	signature
     //   15	87	3	points	Ljava/util/List<Ll1j/server/server/types/Point;>;
     // Exception table:
     //   from	to	target	type
     //   25	87	94	java/lang/Exception
     //   88	94	94	java/lang/Exception
   }
 }


