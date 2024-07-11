 package l1j.server.server.model.item.smelting;public class SmeltingScrollInfo { private int _type; private int _descId; private Timestamp _deltime; private int _hpRegen; private int _mpRegen; private int _pvp_magic_reduc; private int _pvp_reduction_per; private int _armor_magic_pro; private int _status_time_reduce; private int _portion_ration; private int _dominion_tel;
   private int _item_id;
   private String _item_name;
   private int _str;
   private int _dex;
   private int _con;
   private int _wis;
   private int _int;
   private int _cha;
   private int _all_Stat;
   private int _short_dmg;
   private int _long_dmg;
   private int _short_hit;
   private int _long_hit;
   private int _short_critical;
   private int _long_critical;
   private int _reduction;
   private int _pvp_dmg;
   private int _pvp_reduction;

   static SmeltingScrollInfo newInstance(ResultSet rs) throws SQLException {
     SmeltingScrollInfo pInfo = newInstance();
     pInfo._item_id = rs.getInt("item_id");
     pInfo._item_name = rs.getString("item_name");
     pInfo._type = rs.getInt("類型");
     pInfo._str = rs.getInt("力量");
     pInfo._dex = rs.getInt("敏捷");
     pInfo._con = rs.getInt("體質");
     pInfo._wis = rs.getInt("精神");
     pInfo._int = rs.getInt("智力");
     pInfo._cha = rs.getInt("魅力");
     pInfo._all_Stat = rs.getInt("all_stat");
     pInfo._short_dmg = rs.getInt("short_dmg");
     pInfo._long_dmg = rs.getInt("long_dmg");
     pInfo._short_hit = rs.getInt("short_hit");
     pInfo._long_hit = rs.getInt("long_hit");
     pInfo._short_critical = rs.getInt("short_critical");
     pInfo._long_critical = rs.getInt("long_critical");
     pInfo._reduction = rs.getInt("reduction");
     pInfo._pvp_dmg = rs.getInt("pvp_dmg");
     pInfo._pvp_reduction = rs.getInt("pvp_reduction");
     pInfo._dg = rs.getInt("dg");
     pInfo._er = rs.getInt("er");
     pInfo._ac = rs.getInt("ac");
     pInfo._mr = rs.getInt("mr");
     pInfo._sp = rs.getInt("sp");
     pInfo._weight = rs.getInt("weight");
     pInfo._max_hp = rs.getInt("max_hp");
     pInfo._max_mp = rs.getInt("max_mp");
     pInfo._resis_fire = rs.getInt("resis_fire");
     pInfo._resis_earth = rs.getInt("resis_earth");
     pInfo._resis_water = rs.getInt("resis_water");
     pInfo._resis_wind = rs.getInt("resis_wind");
     pInfo._resis_all = rs.getInt("resis_all");
     pInfo._m_resis_ability = rs.getInt("m_resis_ability");
     pInfo._m_resis_spirit = rs.getInt("m_resis_spirit");
     pInfo._m_resis_dragon = rs.getInt("m_resis_dragon");
     pInfo._m_resis_fear = rs.getInt("m_resis_fear");
     pInfo._m_resis_all = rs.getInt("m_resis_all");
     pInfo._m_hit_ability = rs.getInt("m_hit_ability");
     pInfo._m_hit_spirit = rs.getInt("m_hit_spirit");
     pInfo._m_hit_dragon = rs.getInt("m_hit_dragon");
     pInfo._m_hit_fear = rs.getInt("m_hit_fear");
     pInfo._m_hit_all = rs.getInt("m_hit_all");
     pInfo._magichit = rs.getInt("magichit");
     pInfo._attack_speed = rs.getInt("attack_speed");
     pInfo._exp = rs.getInt("exp");
     pInfo._dragon_pear = rs.getInt("dragon_pear");
     pInfo._me = rs.getInt("me");
     pInfo._magic_critical = rs.getInt("magic_critical");
     pInfo._reduction_per = rs.getInt("reduction_per");
     pInfo._dominion_tel = rs.getInt("dominion_tel");
     pInfo._portion_ration = rs.getInt("portion_ration");
     pInfo._hpRegen = rs.getInt("hp_regen");
     pInfo._mpRegen = rs.getInt("mp_regen");
     pInfo._pvp_magic_reduc = rs.getInt("pvp_magic_reduction_per");
     pInfo._pvp_reduction_per = rs.getInt("pvp_reduction_per");
     pInfo._armor_magic_pro = rs.getInt("armor_magic_pro");
     pInfo._status_time_reduce = rs.getInt("status_time_reduce");
     pInfo._descId = rs.getInt("desc_id");

     return pInfo;
   }
   private int _dg; private int _er; private int _ac; private int _mr; private int _sp; private int _weight; private int _max_hp; private int _max_mp; private int _resis_fire; private int _resis_earth; private int _resis_water; private int _resis_wind; private int _resis_all; private int _m_resis_ability; private int _m_resis_spirit; private int _m_resis_dragon; private int _m_resis_fear; private int _m_resis_all; private int _m_hit_ability; private int _m_hit_spirit; private int _m_hit_dragon; private int _m_hit_fear; private int _m_hit_all; private int _exp; private int _magichit; private int _magic_critical; private int _me; private int _attack_speed; private int _dragon_pear; private int _reduction_per;
   private static SmeltingScrollInfo newInstance() {
     return new SmeltingScrollInfo();
   }

   public int getType() {
     return this._type;
   }



   public int get_descId() {
     return this._descId;
   }



   public Timestamp get_deltime() {
     return this._deltime;
   }
   public void set_deltime(Timestamp time) {
     this._deltime = time;
   }

   public int get_hpRegen() {
     return this._hpRegen;
   }

   public int get_mpRegen() {
     return this._mpRegen;
   }

   public int get_pvp_magic_reduc() {
     return this._pvp_magic_reduc;
   }

   public int get_pvp_reduction_per() {
     return this._pvp_reduction_per;
   }

   public int get_armor_magic_pro() {
     return this._armor_magic_pro;
   }

   public int get_status_time_reduce() {
     return this._status_time_reduce;
   }





   public int get_portion_ration() {
     return this._portion_ration;
   }



   public int get_dominion_tel() {
     return this._dominion_tel;
   }



   public int get_item_id() {
     return this._item_id;
   }



   public String get_item_name() {
     return this._item_name;
   }



   public int get_str() {
     return this._str;
   }



   public int get_dex() {
     return this._dex;
   }



   public int get_con() {
     return this._con;
   }



   public int get_wis() {
     return this._wis;
   }



   public int get_int() {
     return this._int;
   }


   public int get_cha() {
     return this._cha;
   }


   public int get_all_Stat() {
     return this._all_Stat;
   }



   public int get_short_dmg() {
     return this._short_dmg;
   }



   public int get_long_dmg() {
     return this._long_dmg;
   }



   public int get_short_hit() {
     return this._short_hit;
   }



   public int get_long_hit() {
     return this._long_hit;
   }



   public int get_short_critical() {
     return this._short_critical;
   }



   public int get_long_critical() {
     return this._long_critical;
   }



   public int get_reduction() {
     return this._reduction;
   }



   public int get_pvp_dmg() {
     return this._pvp_dmg;
   }



   public int get_pvp_reduction() {
     return this._pvp_reduction;
   }



   public int get_dg() {
     return this._dg;
   }



   public int get_er() {
     return this._er;
   }



   public int get_ac() {
     return this._ac;
   }



   public int get_mr() {
     return this._mr;
   }



   public int get_sp() {
     return this._sp;
   }



   public int get_weight() {
     return this._weight;
   }



   public int get_max_hp() {
     return this._max_hp;
   }



   public int get_max_mp() {
     return this._max_mp;
   }



   public int get_resis_fire() {
     return this._resis_fire;
   }



   public int get_resis_earth() {
     return this._resis_earth;
   }



   public int get_resis_water() {
     return this._resis_water;
   }



   public int get_resis_wind() {
     return this._resis_wind;
   }



   public int get_resis_all() {
     return this._resis_all;
   }



   public int get_m_resis_ability() {
     return this._m_resis_ability;
   }



   public int get_m_resis_spirit() {
     return this._m_resis_spirit;
   }



   public int get_m_resis_dragon() {
     return this._m_resis_dragon;
   }



   public int get_m_resis_fear() {
     return this._m_resis_fear;
   }



   public int get_m_resis_all() {
     return this._m_resis_all;
   }



   public int get_m_hit_ability() {
     return this._m_hit_ability;
   }



   public int get_m_hit_spirit() {
     return this._m_hit_spirit;
   }



   public int get_m_hit_dragon() {
     return this._m_hit_dragon;
   }



   public int get_m_hit_fear() {
     return this._m_hit_fear;
   }



   public int get_m_hit_all() {
     return this._m_hit_all;
   }



   public int get_exp() {
     return this._exp;
   }


   public int get_magichit() {
     return this._magichit;
   }


   public int get_magic_critical() {
     return this._magic_critical;
   }



   public int get_me() {
     return this._me;
   }



   public int get_attackspeed() {
     return this._attack_speed;
   }



   public int get_dragon_pear() {
     return this._dragon_pear;
   }


   public int get_reduction_per() {
     return this._reduction_per;
   }


   public static byte[] getItemView(SmeltingScrollInfo info) {
     BinaryOutputStream os = new BinaryOutputStream();
     try {
       if (info != null) {
         if (info.get_str() != 0) {
           os.writeC(8);
           os.writeC(info.get_str());
         }
         if (info.get_dex() != 0) {
           os.writeC(9);
           os.writeC(info.get_dex());
         }
         if (info.get_con() != 0) {
           os.writeC(10);
           os.writeC(info.get_con());
         }
         if (info.get_wis() != 0) {
           os.writeC(11);
           os.writeC(info.get_wis());
         }
         if (info.get_int() != 0) {
           os.writeC(12);
           os.writeC(info.get_int());
         }
         if (info.get_cha() != 0) {
           os.writeC(13);
           os.writeC(info.get_cha());
         }
         if (info.get_all_Stat() != 0) {
           os.writeC(8);
           os.writeC(info.get_all_Stat());
           os.writeC(9);
           os.writeC(info.get_all_Stat());
           os.writeC(10);
           os.writeC(info.get_all_Stat());
           os.writeC(11);
           os.writeC(info.get_all_Stat());
           os.writeC(12);
           os.writeC(info.get_all_Stat());
           os.writeC(13);
           os.writeC(info.get_all_Stat());
         }
         if (info.get_short_dmg() != 0) {
           os.writeC(47);
           os.writeC(info.get_short_dmg());
         }
         if (info.get_long_dmg() != 0) {
           os.writeC(35);
           os.writeC(info.get_long_dmg());
         }
         if (info.get_short_hit() != 0) {
           os.writeC(48);
           os.writeC(info.get_short_hit());
         }
         if (info.get_long_hit() != 0) {
           os.writeC(24);
           os.writeC(info.get_long_hit());
         }
         if (info.get_short_critical() != 0) {
           os.writeC(100);
           os.writeC(info.get_short_critical());
         }
         if (info.get_long_critical() != 0) {
           os.writeC(99);
           os.writeC(info.get_long_critical());
         }
         if (info.get_reduction() != 0) {
           os.writeC(63);
           os.writeC(info.get_reduction());
         }
         if (info.get_pvp_dmg() != 0) {
           os.writeC(59);
           os.writeC(info.get_pvp_dmg());
         }
         if (info.get_pvp_reduction() != 0) {
           os.writeC(60);
           os.writeC(info.get_pvp_reduction());
         }
         if (info.get_dg() != 0) {
           os.writeC(51);
           os.writeC(info.get_dg());
         }
         if (info.get_er() != 0) {
           os.writeC(93);
           os.writeC(info.get_er());
         }
         if (info.get_ac() != 0) {
           os.writeC(56);
           os.writeC(-info.get_ac());
         }
         if (info.get_mr() != 0) {
           os.writeC(15);
           os.writeH(info.get_mr());
         }
         if (info.get_sp() != 0) {
           os.writeC(17);
           os.writeC(info.get_sp());
         }
         if (info.get_weight() != 0) {
           os.writeC(90);
           os.writeH(info.get_weight());
         }
         if (info.get_max_hp() != 0) {
           os.writeC(14);
           os.writeH(info.get_max_hp());
         }
         if (info.get_max_mp() != 0) {
           os.writeC(32);
           os.writeH(info.get_max_mp());
         }
         if (info.get_resis_fire() != 0) {
           os.writeC(27);
           os.writeC(info.get_resis_fire());
         }
         if (info.get_resis_earth() != 0) {
           os.writeC(30);
           os.writeC(info.get_resis_earth());
         }
         if (info.get_resis_water() != 0) {
           os.writeC(28);
           os.writeC(info.get_resis_water());
         }
         if (info.get_resis_wind() != 0) {
           os.writeC(29);
           os.writeC(info.get_resis_wind());
         }
         if (info.get_resis_all() != 0) {
           os.writeC(155);
           os.writeC(info.get_resis_all());
         }
         if (info.get_m_resis_ability() != 0) {
           os.writeC(117);
           os.writeC(info.get_m_resis_ability());
         }
         if (info.get_m_resis_spirit() != 0) {
           os.writeC(118);
           os.writeC(info.get_m_resis_spirit());
         }
         if (info.get_m_resis_dragon() != 0) {
           os.writeC(119);
           os.writeC(info.get_m_resis_dragon());
         }
         if (info.get_m_resis_fear() != 0) {
           os.writeC(120);
           os.writeC(info.get_m_resis_fear());
         }
         if (info.get_m_resis_all() != 0) {
           os.writeC(121);
           os.writeC(info.get_m_resis_all());
         }
         if (info.get_m_hit_ability() < 0) {
           os.writeC(122);
           os.writeC(info.get_m_hit_ability());
         }
         if (info.get_m_hit_spirit() != 0) {
           os.writeC(123);
           os.writeC(info.get_m_hit_spirit());
         }
         if (info.get_m_hit_dragon() != 0) {
           os.writeC(124);
           os.writeC(info.get_m_hit_dragon());
         }
         if (info.get_m_hit_fear() != 0) {
           os.writeC(125);
           os.writeC(info.get_m_hit_fear());
         }
         if (info.get_m_hit_all() != 0) {
           os.writeC(126);
           os.writeC(info.get_m_hit_all());
         }
         if (info.get_exp() != 0) {
           os.writeC(36);
           os.writeC(info.get_exp());
         }
         if (info.get_magichit() != 0) {
           os.writeC(40);
           os.writeC(info.get_magichit());
         }
         if (info.get_me() != 0) {
           os.writeC(89);
           os.writeD(info.get_me());
         }
         if (info.get_magic_critical() != 0) {
           os.writeC(50);
           os.writeH(info.get_magic_critical());
         }
         if (info.get_reduction_per() != 0) {
           os.writeC(192);
           os.writeH(info.get_reduction_per());
         }
                // 如果攻擊速度不為0
           if (info.get_attackspeed() != 0) {
               os.writeC(39); // 寫入控制碼 39
               os.writeS("\fI效果:\aA 攻擊速度增加 "); // 寫入描述攻擊速度增加的字符串
           }
         if (info.get_hpRegen() != 0) {
           os.writeC(205);
           os.writeH(info.get_hpRegen());
         }


         if (info.get_mpRegen() != 0) {
           os.writeC(204);
           os.writeH(info.get_mpRegen());
         }


         if (info.get_pvp_magic_reduc() != 0) {
           os.writeC(166);
           os.writeC(info.get_pvp_magic_reduc());
         }
         if (info.get_pvp_reduction_per() != 0) {
           os.writeC(193);
           os.writeC(info.get_pvp_reduction_per());
         }
         if (info.get_armor_magic_pro() != 0) {
           os.writeC(244);
           os.writeH(info.get_armor_magic_pro());
         }
         if (info.get_status_time_reduce() != 0) {
           os.writeC(241);
           os.writeH(info.get_status_time_reduce());
         }
         if (info.get_portion_ration() != 0) {
           os.writeC(65);
           os.writeC(info.get_portion_ration());
           os.writeC(info.get_portion_ration());
         }


         os.writeC(247);
         os.writeC(0);

         return os.getBytes();
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       if (os != null) {
         try {
           os.close();
         } catch (IOException e) {
           e.printStackTrace();
         }
       }
     }
     return null;
   } }


