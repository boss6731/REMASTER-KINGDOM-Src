 package l1j.server.server.model;


















 public class L1ObjectAmount<T>
 {
     private final T _obj;
     private final int _amount;
     private final int _en;

     public L1ObjectAmount(T obj, int amount, int en) {
         this._obj = obj;
         this._amount = amount;
         this._en = en;
     }
     public T getObject() {
         return this._obj;
     } public int getAmount() {
         return this._amount;
     } public int getEnchant() {
         return this._en;
     }
 }


