 package l1j.server.server.model;

 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.map.L1Map;
 import l1j.server.server.model.map.L1WorldMap;















 public class L1Astar
 {
   private L1Node OpenNode = null;
   private L1Node ClosedNode = null;


   private static final int LIMIT_LOOP = 1000;



   public void ResetPath() {
     while (this.OpenNode != null) {
       L1Node tmp = this.OpenNode.next;
       this.OpenNode = null;
       this.OpenNode = tmp;
     }
     while (this.ClosedNode != null) {
       L1Node tmp = this.ClosedNode.next;
       this.ClosedNode = null;
       this.ClosedNode = tmp;
     }
   }




   public L1Node FindPath(L1NpcInstance npc, L1Character target) {
     return FindPath((L1Object)npc, target.getX(), target.getY(), target.getMapId(), target);
   }

   public L1Node FindPath(L1Object npc, int tx, int ty, int mapId, L1Character target) {
     L1Node best = null;
     int count = 0;

     L1Node src = new L1Node();
     src.g = 0;
     src.h = (tx - npc.getX()) * (tx - npc.getX()) + (ty - npc.getY()) * (ty - npc.getY());
     src.f = src.h;
     src.x = npc.getX();
     src.y = npc.getY();
     this.OpenNode = src;

     while (count < 1000) {
       if (this.OpenNode == null) {
         return best;
       }
       best = this.OpenNode;
       if (best == null) {
         return null;
       }
       this.OpenNode = best.next;
       best.next = this.ClosedNode;
       this.ClosedNode = best;

       if (Math.max(Math.abs(tx - best.x), Math.abs(ty - best.y)) == 1) {
         return best;
       }
       if (MakeChild(best, tx, ty, npc.getMapId()) == '\000' && count == 0) {
         return null;
       }
       count++;
     }
     return best;
   }





   public char MakeChild(L1Node node, int tx, int ty, short m) {
     char flag = Character.MIN_VALUE;
     char[] cc = { Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE };

     int x = node.x;
     int y = node.y;

     cc[0] = IsMove(x, y + 1, m);
     cc[1] = IsMove(x - 1, y + 1, m);
     cc[2] = IsMove(x - 1, y, m);
     cc[3] = IsMove(x - 1, y - 1, m);
     cc[4] = IsMove(x, y - 1, m);
     cc[5] = IsMove(x + 1, y - 1, m);
     cc[6] = IsMove(x + 1, y, m);
     cc[7] = IsMove(x + 1, y + 1, m);

     if (cc[2] == '\001') {
       MakeChildSub(node, x - 1, y, tx, ty);
       flag = '\001';
     }
     if (cc[6] == '\001') {
       MakeChildSub(node, x + 1, y, tx, ty);
       flag = '\001';
     }
     if (cc[4] == '\001') {
       MakeChildSub(node, x, y - 1, tx, ty);
       flag = '\001';
     }
     if (cc[0] == '\001') {
       MakeChildSub(node, x, y + 1, tx, ty);
       flag = '\001';
     }
     if (cc[7] == '\001' && cc[6] == '\001' && cc[0] == '\001') {
       MakeChildSub(node, x + 1, y + 1, tx, ty);
       flag = '\001';
     }
     if (cc[3] == '\001' && cc[2] == '\001' && cc[4] == '\001') {
       MakeChildSub(node, x - 1, y - 1, tx, ty);
       flag = '\001';
     }
     if (cc[5] == '\001' && cc[4] == '\001' && cc[6] == '\001') {
       MakeChildSub(node, x + 1, y - 1, tx, ty);
       flag = '\001';
     }
     if (cc[1] == '\001' && cc[0] == '\001' && cc[2] == '\001') {
       MakeChildSub(node, x - 1, y + 1, tx, ty);
       flag = '\001';
     }

     return flag;
   }




   public char IsMove(int x, int y, short mapid) {
     L1Map map = L1WorldMap.getInstance().getMap(mapid);
     if (!map.isPassable(x, y)) {
       return Character.MIN_VALUE;
     }
     if (map.isExistDoor(x, y) == true) {
       return Character.MIN_VALUE;
     }

     return '\001';
   }






   public void MakeChildSub(L1Node node, int x, int y, int tx, int ty) {
     L1Node old = null, child = null;

     int g = node.g + 1;

     if ((old = IsOpen(x, y)) != null) {
       for (int i = 0; i < 8; i++) {
         if (node.direct[i] == null) {
           node.direct[i] = old;
           break;
         }
       }
       if (g < old.g) {
         old.prev = node;
         old.g = g;
         old.f = old.h + old.g;
       }

     }
     else if ((old = IsClosed(x, y)) != null) {
       for (int i = 0; i < 8; i++) {
         if (node.direct[i] == null) {
           node.direct[i] = old;
           break;
         }
       }
       if (g < old.g) {
         old.prev = node;
         old.g = g;
         old.f = old.h + old.g;

       }


     }
     else {

       child = new L1Node();

       child.prev = node;
       child.g = g;
       child.h = (x - tx) * (x - tx) + (y - ty) * (y - ty);
       child.f = child.h + child.g;
       child.x = x;
       child.y = y;


       InsertNode(child);

       for (int i = 0; i < 8; i++) {
         if (node.direct[i] == null) {
           node.direct[i] = child;
           break;
         }
       }
     }
   }




   public L1Node IsOpen(int x, int y) {
     L1Node tmp = this.OpenNode;
     while (tmp != null) {
       if (tmp.x == x && tmp.y == y) {
         return tmp;
       }
       tmp = tmp.next;
     }
     return null;
   }




   public L1Node IsClosed(int x, int y) {
     L1Node tmp = this.ClosedNode;
     while (tmp != null) {
       if (tmp.x == x && tmp.y == y) {
         return tmp;
       }
       tmp = tmp.next;
     }
     return null;
   }





   public void InsertNode(L1Node src) {
     L1Node old = null, tmp = null;
     if (this.OpenNode == null) {
       this.OpenNode = src;
       return;
     }
     tmp = this.OpenNode;
     while (tmp != null && tmp.f < src.f) {
       old = tmp;
       tmp = tmp.next;
     }
     if (old != null) {
       src.next = tmp;
       old.next = src;
     } else {
       src.next = tmp;
       this.OpenNode = src;
     }
   }
 }


