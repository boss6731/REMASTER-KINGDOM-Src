 package l1j.server.server.utils;

 import java.util.Random;




 public class IntRange
 {
   private static final Random _rnd = new Random(System.nanoTime());
   private int _low;
   private int _high;

   public IntRange(int low, int high) {
     this._low = low;
     this._high = high;
   }

   public IntRange(IntRange range) {
     this(range._low, range._high);
   }

   public static int random(int number) {
     Random rnd = new Random();
     return rnd.nextInt(number);
   }



   public boolean includes(int i) {
     return (this._low <= i && i <= this._high);
   }

   public static boolean includes(int i, int low, int high) {
     return (low <= i && i <= high);
   }




   public int ensure(int i) {
     int r = i;
     r = (this._low <= r) ? r : this._low;
     r = (r <= this._high) ? r : this._high;
     return r;
   }

   public static int ensure(int n, int low, int high) {
     int r = n;
     r = (low <= r) ? r : low;
     r = (r <= high) ? r : high;
     return r;
   }


   public int randomValue() {
     return _rnd.nextInt(getWidth() + 1) + this._low;
   }

   public int getLow() {
     return this._low;
   }

   public int getHigh() {
     return this._high;
   }

   public int getWidth() {
     return this._high - this._low;
   }


   public static int getTotalValueRint(double val) {
     return (int)Math.rint(val);
   }


   public boolean equals(Object obj) {
     if (!(obj instanceof IntRange)) {
       return false;
     }
     IntRange range = (IntRange)obj;
     return (this._low == range._low && this._high == range._high);
   }


   public String toString() {
     return "low=" + this._low + ", high=" + this._high;
   }
 }


