 package l1j.server.server.types;

 public class Point
 {
   protected int _x = 0;
   protected int _y = 0;
   protected int _map = 0;

   public Point() {}

   public Point(int x, int y) {
     this._x = x;
     this._y = y;
   }

   public Point(Point pt) {
     this._x = pt._x;
     this._y = pt._y;
   }

   public Point(int x, int y, int map) {
     this._x = x;
     this._y = y;
     this._map = map;
   }

   public int getX() {
     return this._x;
   }

   public void setX(int x) {
     this._x = x;
   }

   public int getY() {
     return this._y;
   }

   public void setY(int y) {
     this._y = y;
   }

   public void set(Point pt) {
     this._x = pt._x;
     this._y = pt._y;
   }

   public void set(int x, int y) {
     this._x = x;
     this._y = y;
   }

   public int getMapId() {
     return this._map;
   }

   private static final int[] HEADING_TABLE_X = new int[] { 0, 1, 1, 1, 0, -1, -1, -1 };
   private static final int[] HEADING_TABLE_Y = new int[] { -1, -1, 0, 1, 1, 1, 0, -1 };

   public void forward(int heading) {
     this._x += HEADING_TABLE_X[heading];
     this._y += HEADING_TABLE_Y[heading];
   }

   public void backward(int heading) {
     this._x -= HEADING_TABLE_X[heading];
     this._y -= HEADING_TABLE_Y[heading];
   }

   public int getHeading(Point target) {
     int xDiff = target.getX() - this._x;
     int yDiff = target.getY() - this._y;

     for (int i = 0; i < 8; i++) {
       if (HEADING_TABLE_X[i] == xDiff && HEADING_TABLE_Y[i] == yDiff)
       {
         return i;
       }
     }

     return -1;
   }

   public double getLineDistance(Point pt) {
     long diffX = (pt.getX() - getX());
     long diffY = (pt.getY() - getY());
     return Math.sqrt((diffX * diffX + diffY * diffY));
   }

   public double getLineDistance(int x, int y) {
     long diffX = (x - getX());
     long diffY = (y - getY());
     return Math.sqrt((diffX * diffX + diffY * diffY));
   }

   public int getTileLineDistance(Point pt) {
     return Math.max(Math.abs(pt.getX() - getX()), Math.abs(pt.getY() - getY()));
   }

   public int getTileLineDistance(int x, int y) {
     return Math.max(Math.abs(x - getX()), Math.abs(y - getY()));
   }

   public int getTileDistance(Point pt) {
     return Math.abs(pt.getX() - getX()) + Math.abs(pt.getY() - getY());
   }

   public int getTileDistance(int x, int y) {
     return Math.abs(x - getX()) + Math.abs(y - getY());
   }

   public boolean isInScreen(Point pt) {
     int dist = getTileDistance(pt);

     if (dist > 32)
       return false;
     if (dist <= 29) {
       return true;
     }

     int dist2 = Math.abs(pt.getX() - getX() - 30) + Math.abs(pt.getY() - getY() - 30);
     if (23 <= dist2 && dist2 <= 56) {
       return true;
     }
     return false;
   }

















   public boolean isSamePoint(Point pt) {
     return (pt.getX() == getX() && pt.getY() == getY());
   }


   public int hashCode() {
     return 7 * getX() + getY();
   }


   public boolean equals(Object obj) {
     if (!(obj instanceof Point)) {
       return false;
     }
     Point pt = (Point)obj;
     return (getX() == pt.getX() && getY() == pt.getY());
   }


   public String toString() {
     return String.format("(%d, %d)", new Object[] { Integer.valueOf(this._x), Integer.valueOf(this._y) });
   }
 }


