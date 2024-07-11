 package l1j.server.server.utils;

 public class KeyValuePair<T, K>
 {
   public T key;

   public KeyValuePair(T key, K value) {
     this.key = key;
     this.value = value;
   }

   public K value;

   public KeyValuePair() {}
 }


