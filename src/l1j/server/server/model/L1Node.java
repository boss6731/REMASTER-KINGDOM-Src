 package l1j.server.server.model;













 public class L1Node
 {
   public int f;
   public int h;
   public int g;
   public int x;
   public int y;
   public L1Node prev;
   public L1Node[] direct = new L1Node[8];
   public L1Node() {
     for (int i = 0; i < 8; i++)
       this.direct[i] = null;
   }

   public L1Node next;
 }


