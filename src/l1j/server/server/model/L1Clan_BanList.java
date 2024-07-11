 package l1j.server.server.model;

 import java.util.ArrayList;

 public class L1Clan_BanList {
   private String _clanname;
   ArrayList<String> banlist = new ArrayList<>();
   private int _limit_level;

   public void set_ClanName(String clanname) {
     this._clanname = clanname;
   }
   public String get_ClanName() {
     return this._clanname;
   }

   public void add_Banlist(String name) {
     this.banlist.add(name);
   }
   public void del_Banlist(String name) {
     this.banlist.remove(name);
   }
   public boolean isBanlist(String name) {
     if (this.banlist.contains(name)) {
       return true;
     }
     return false;
   }
   public void setBanlist(ArrayList<String> list) {
     this.banlist = list;
   }
   public ArrayList<String> getBanList() {
     return this.banlist;
   }
   public void set_LimitLevel(int level) {
     this._limit_level = level;
   }
   public int get_LimitLevel() {
     return this._limit_level;
   }
 }


