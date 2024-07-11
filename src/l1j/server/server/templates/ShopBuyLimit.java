 package l1j.server.server.templates;

 import java.sql.Timestamp;



 public class ShopBuyLimit
 {
   private int _objid;
   private int _item_id;
   private String _item_name;

   public int get_objid() {
     return this._objid;
   } private String _account_name; private int _count; private eShopBuyLimitType _type; private Timestamp _end_time;
   public void set_objid(int _objid) {
     this._objid = _objid;
   }
   public int get_item_id() {
     return this._item_id;
   }
   public void set_item_id(int _item_id) {
     this._item_id = _item_id;
   }
   public String get_item_name() {
     return this._item_name;
   }
   public void set_item_name(String _item_name) {
     this._item_name = _item_name;
   }
   public int get_count() {
     return this._count;
   }
   public void set_count(int _count) {
     this._count = _count;
   }
   public eShopBuyLimitType get_type() {
     return this._type;
   }
   public void set_type(eShopBuyLimitType _type) {
     this._type = _type;
   }
   public Timestamp get_end_time() {
     return this._end_time;
   }
   public void set_end_time(Timestamp _end_time) {
     this._end_time = _end_time;
   }
   public String get_account_name() {
     return this._account_name;
   }
   public void set_account_name(String _account_name) {
     this._account_name = _account_name;
   }
 }


