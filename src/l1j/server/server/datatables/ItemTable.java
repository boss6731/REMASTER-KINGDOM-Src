 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.LinkedList;
 import java.util.List;
 import java.util.Map;
 import java.util.concurrent.ConcurrentHashMap;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.Config;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.MJTemplate.FindItemTable.TableFindItemFilter;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.CraftCommonBin;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ALCHEMY_DESIGN_ACK;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_SYNTHESIS_SMELTING_DESIGN_ACK;
 import l1j.server.MJTemplate.matcher.Matcher;
 import l1j.server.server.IdFactory;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.templates.L1Armor;
 import l1j.server.server.templates.L1EtcItem;
 import l1j.server.server.templates.L1Item;
 import l1j.server.server.templates.L1RaceTicket;
 import l1j.server.server.templates.L1Weapon;
 import l1j.server.server.templates.WareHouseLeaveType;
 import l1j.server.server.utils.SQLUtil;




 public class ItemTable
 {
   private static Logger _log = Logger.getLogger(ItemTable.class.getName());
   private static ItemTable _instance;
   private HashMap<Integer, L1Item> _allTemplates;
   public final Map<Integer, L1EtcItem> _etcitems;

   public HashMap<Integer, L1Item> getAllTemplates() {
     return this._allTemplates;
   }


   public final Map<Integer, L1Armor> _armors;

   public final Map<Integer, L1Weapon> _weapons;
   public final ConcurrentHashMap<Integer, List<L1Item>> _descCached;
   private final Map<Integer, L1RaceTicket> _race = new HashMap<>(64);

   public static ItemTable getInstance() {
     if (_instance == null) {
       _instance = new ItemTable();
     }
     return _instance;
   }

   private ItemTable() {
     this._etcitems = allEtcItem();
     this._weapons = allWeapon();
     this._armors = allArmor();
     this._descCached = new ConcurrentHashMap<>(64);

     buildFastLookupTable();
   }

   public static void reload() {
     ItemTable oldInstance = _instance;
     ItemTable newInstance = new ItemTable();
     newInstance.subLoad();
     _instance = newInstance;
     oldInstance._etcitems.clear();
     oldInstance._weapons.clear();
     oldInstance._armors.clear();
     oldInstance._allTemplates.clear();
     oldInstance._descCached.clear();
   }

   private Map<Integer, L1EtcItem> allEtcItem() {
     Map<Integer, L1EtcItem> result = new HashMap<>(5120);

     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     L1EtcItem item = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("select * from etcitem");

       rs = pstm.executeQuery();
       while (rs.next()) {
         item = new L1EtcItem();
         item.setItemId(rs.getInt("item_id"));
         item.setItemDescId(rs.getInt("desc_id"));
         item.setGfxId(rs.getInt("icon_id"));
         item.setGroundGfxId(rs.getInt("sprite"));
         item.setNameId(rs.getString("real_name_id"));
         item.setName(rs.getString("real_name_id_view"));
         item.setNameView(rs.getString("name_id_view"));
         item.setType(parseEtcType(rs.getString("item_type")));
         item.setUseType(parseUseType(rs.getString("use_type")));
         item.setType2(0);
         item.setMaterial(parseMaterial(rs.getString("material")));
         item.setUseRoyal(!(rs.getInt("use_royal") == 0));
         item.setUseKnight(!(rs.getInt("use_knight") == 0));
         item.setUseElf(!(rs.getInt("use_elf") == 0));
         item.setUseMage(!(rs.getInt("use_mage") == 0));
         item.setUseDarkelf(!(rs.getInt("use_darkelf") == 0));
         item.setUseDragonKnight(!(rs.getInt("use_dragonknight") == 0));
         item.setUseBlackwizard(!(rs.getInt("use_blackwizard") == 0));
         item.setUse전사(!(rs.getInt("use_warrior") == 0));
         item.setUseFencer(!(rs.getInt("use_fencer") == 0));
         item.setUseLancer(!(rs.getInt("use_lancer") == 0));
         item.setWeight(rs.getInt("weight_1000_ea"));
         item.setMinLevel(rs.getInt("min_lvl"));
         item.setMaxLevel(rs.getInt("max_lvl"));
         item.setBless(rs.getInt("bless"));
         item.setTradable((rs.getInt("trade") == 0));
         item.setCantDelete((rs.getInt("cant_delete") == 1));
         item.setHitModifier(rs.getInt("hit"));
         item.setDmgModifier(rs.getInt("dmg"));
         item.set_stackable((rs.getInt("stackable") == 1));
         item.setMaxChargeCount(rs.getInt("max_charge_count"));
         item.set_locx(rs.getInt("locx"));
         item.set_locy(rs.getInt("locy"));
         item.set_mapid(rs.getShort("mapid"));
         item.set_delayid(rs.getInt("delay_id"));
         item.set_delaytime(rs.getInt("delay_time"));
         item.set_delayEffect(rs.getInt("delay_effect"));
         item.setFoodVolume(rs.getInt("food_volume"));
         item.setToBeSavedAtOnce((rs.getInt("save_at_once") == 1));
         item.setEndedTimeMessage(rs.getBoolean("isEndedTimeMessage"));
         item.setUseEffetId(rs.getInt("use_effect_id"));
         item.setOverlaySurfId(rs.getInt("overlay_surf_id"));

         item.setWareHouseLimitType(WareHouseLeaveType.fromString(rs.getString("warehouse_type")));
         item.setWareHouseLimitLevel(rs.getInt("warehouse_limit_enchant"));

           if (Config.ServerAdSetting.IsValidItemId) { // 如果 ServerAdSetting 的 IsValidItemId 為真
               if (this._weapons != null && this._weapons.containsKey(Integer.valueOf(item.getItemId())))
                   System.out.println(String.format("ETCITEM %d(%s) 與武器重複。", new Object[] { Integer.valueOf(item.getItemId()), item.getName() }));
               if (this._armors != null && this._armors.containsKey(Integer.valueOf(item.getItemId())))
                   System.out.println(String.format("ETCITEM %d(%s) 與防具重複。", new Object[] { Integer.valueOf(item.getItemId()), item.getName() }));
           }
           result.put(new Integer(item.getItemId()), item); // 將物品放入結果中
       }
     } catch (NullPointerException e) {
         _log.log(Level.SEVERE, item.getName() + "(" + item.getItemId() + ")" + " 讀取失敗。"); // 捕獲並記錄 NullPointerException 異常
     } catch (Exception e) {
         e.printStackTrace(); // 捕獲並打印其他異常
     } finally {
         SQLUtil.close(rs); // 關閉 ResultSet
         SQLUtil.close(pstm); // 關閉 PreparedStatement
         SQLUtil.close(con); // 關閉 Connection
     }
       return result; // 返回結果
   }

   private Map<Integer, L1Weapon> allWeapon() {
     Map<Integer, L1Weapon> result = new HashMap<>(1024);

     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     L1Weapon weapon = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("select * from weapon");

       rs = pstm.executeQuery();
       while (rs.next()) {
         weapon = new L1Weapon();
         weapon.setItemId(rs.getInt("item_id"));
         weapon.setItemDescId(rs.getInt("desc_id"));
         weapon.setGfxId(rs.getInt("icon_id"));
         weapon.setGroundGfxId(rs.getInt("sprite"));
         weapon.setNameId(rs.getString("real_name_id"));
         weapon.setName(rs.getString("real_name_id_view"));
         weapon.setNameView(rs.getString("name_id_view"));
         weapon.setType(parseWeaponType(rs.getString("type")));
         weapon.setType1(parseWeaponId(rs.getString("type")));
         weapon.setType2(1);
         weapon.setUseType(1);
         weapon.setMaterial(parseMaterial(rs.getString("material")));
         weapon.setWeight(rs.getInt("weight_1000_ea"));
         weapon.setDmgSmall(rs.getInt("dmg_small"));
         weapon.setDmgLarge(rs.getInt("dmg_large"));
         weapon.set_safeenchant(rs.getInt("safenchant"));
         weapon.setUseRoyal(!(rs.getInt("use_royal") == 0));
         weapon.setUseKnight(!(rs.getInt("use_knight") == 0));
         weapon.setUseElf(!(rs.getInt("use_elf") == 0));
         weapon.setUseMage(!(rs.getInt("use_mage") == 0));
         weapon.setUseDarkelf(!(rs.getInt("use_darkelf") == 0));
         weapon.setUseDragonKnight(!(rs.getInt("use_dragonknight") == 0));
         weapon.setUseBlackwizard(!(rs.getInt("use_blackwizard") == 0));
         weapon.setUse전사(!(rs.getInt("use_warrior") == 0));
         weapon.setUseFencer(!(rs.getInt("use_fencer") == 0));
         weapon.setUseLancer(!(rs.getInt("use_lancer") == 0));
         weapon.setHitModifier(rs.getInt("hitmodifier"));
         weapon.setDmgModifier(rs.getInt("dmgmodifier"));
         weapon.set_adddex(rs.getByte("add_dex"));
         weapon.set_addstr(rs.getByte("add_str"));
         weapon.set_addint(rs.getByte("add_int"));
         weapon.set_addwis(rs.getByte("add_wis"));
         weapon.set_addcon(rs.getByte("add_con"));
         weapon.set_addcha(rs.getByte("add_cha"));
         weapon.set_addhp(rs.getInt("add_hp"));
         weapon.set_addmp(rs.getInt("add_mp"));
         weapon.set_addhpr(rs.getInt("add_hpr"));
         weapon.set_addmpr(rs.getInt("add_mpr"));
         weapon.set_addsp(rs.getInt("add_sp"));
         weapon.setAddExp(rs.getInt("add_exp"));
         weapon.set_mdef(rs.getInt("m_def"));
         weapon.setDoubleDmgChance(rs.getInt("double_dmg_chance"));
         weapon.set_double_dmg_enchant_value(rs.getInt("double_dmg_enchant_value"));
         weapon.set_weak_point_chance(rs.getInt("weak_point_chance"));
         weapon.set_weak_point_enchant_value(rs.getInt("weak_point_enchant_value"));
         weapon.setMagicDmgModifier(rs.getInt("magicdmgmodifier"));
         weapon.set_canbedmg(rs.getInt("canbedmg"));
         weapon.setMinLevel(rs.getInt("min_lvl"));
         weapon.setMaxLevel(rs.getInt("max_lvl"));
         weapon.setBless(rs.getInt("bless"));
         weapon.setTradable((rs.getInt("trade") == 0));
         weapon.setCantDelete((rs.getInt("cant_delete") == 1));
         weapon.setHasteItem(!(rs.getInt("haste_item") == 0));
         weapon.setMaxUseTime(rs.getInt("max_use_time"));
         weapon.setMagicName(rs.getString("Magic_name"));
         weapon.setEndedTimeMessage(rs.getBoolean("isEndedTimeMessage"));
         weapon.set_missile_critical_probability(rs.getInt("missile_critical_probability"));
         weapon.set_melee_critical_probability(rs.getInt("melee_critical_probability"));
         weapon.set_magic_critical_probability(rs.getInt("magic_critical_probability"));
         weapon.setWeaponReductionCancel(rs.getInt("add_reduction_cancel"));
         weapon.setPVPWeaponReductionCancel(rs.getInt("PVPDmgReducIgnore"));
         weapon.setPVPMagicReduction(rs.getInt("PVPMdmgReduction"));
         weapon.setPVPMagicReductionCancel(rs.getInt("PVPMdmgReducIgnore"));
         weapon.setIIg(rs.getInt("ImmuneIgnore"));
         weapon.setSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY, rs.getInt("ability_resis"));
         weapon.setSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY, rs.getInt("ability_pierce"));
         weapon.setSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT, rs.getInt("spirit_resis"));
         weapon.setSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT, rs.getInt("spirit_pierce"));
         weapon.setSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL, rs.getInt("dragonS_resis"));
         weapon.setSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL, rs.getInt("dragonS_pierce"));
         weapon.setSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR, rs.getInt("fear_resis"));
         weapon.setSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR, rs.getInt("fear_pierce"));
         weapon.setSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, rs.getInt("all_resis"));
         weapon.setSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, rs.getInt("all_pierce"));
         weapon.setTitanPercent(rs.getInt("titan_percent"));
         weapon.setMagicHitup(rs.getInt("magic_hit_up"));
         weapon.setOverlaySurfId(rs.getInt("overlay_surf_id"));

         weapon.setWareHouseLimitType(WareHouseLeaveType.fromString(rs.getString("warehouse_type")));
         weapon.setWareHouseLimitLevel(rs.getInt("warehouse_limit_enchant"));


         weapon.setAttackDelayRate(rs.getInt("attack_speed"));
         weapon.setMoveDelayRate(rs.getInt("move_speed"));

           if (Config.ServerAdSetting.IsValidItemId) { // 如果 Config.ServerAdSetting 的 IsValidItemId 為真
               if (this._etcitems != null && this._etcitems.containsKey(Integer.valueOf(weapon.getItemId()))) {
                   System.out.println(String.format("武器 %d(%s) 與 etcItem 重複。", new Object[] { Integer.valueOf(weapon.getItemId()), weapon.getName() }));
               }
               if (this._armors != null && this._armors.containsKey(Integer.valueOf(weapon.getItemId()))) {
                   System.out.println(String.format("武器 %d(%s) 與防具重複。", new Object[] { Integer.valueOf(weapon.getItemId()), weapon.getName() }));
               }
           }
           result.put(new Integer(weapon.getItemId()), weapon); // 將武器放入結果中
       }
     } catch (NullPointerException e) {
         e.printStackTrace(); // 捕獲並打印 NullPointerException 異常
         _log.log(Level.SEVERE, weapon.getName() + "(" + weapon.getItemId() + ")" + " 讀取失敗。");
     } catch (Exception e) {
         e.printStackTrace(); // 捕獲並打印其他異常
     } finally {
         SQLUtil.close(rs); // 關閉 ResultSet
         SQLUtil.close(pstm); // 關閉 PreparedStatement
         SQLUtil.close(con); // 關閉 Connection
     }

       return result; // 返回結果
   }
   private Map<Integer, L1Armor> allArmor() {
     Map<Integer, L1Armor> result = new HashMap<>(2048);
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     L1Armor armor = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("select * from armor");

       rs = pstm.executeQuery();
       while (rs.next()) {
         armor = new L1Armor();
         armor.setItemId(rs.getInt("item_id"));
         armor.setItemDescId(rs.getInt("desc_id"));
         armor.setGfxId(rs.getInt("icon_id"));
         armor.setGroundGfxId(rs.getInt("sprite"));
         armor.setNameId(rs.getString("real_name_id"));
         armor.setName(rs.getString("real_name_id_view"));
         armor.setNameView(rs.getString("name_id_view"));
         armor.setType(parseArmorType(rs.getString("type")));
         armor.setType2(2);
         armor.setUseType(parseUseType(rs.getString("type")));
         armor.setGrade(rs.getInt("grade"));
         armor.setMaterial(parseMaterial(rs.getString("material")));
         armor.setWeight(rs.getInt("weight_1000_ea"));
         armor.set_ac(rs.getInt("ac"));
         armor.set_safeenchant(rs.getInt("safenchant"));
         armor.setUseRoyal(!(rs.getInt("use_royal") == 0));
         armor.setUseKnight(!(rs.getInt("use_knight") == 0));
         armor.setUseElf(!(rs.getInt("use_elf") == 0));
         armor.setUseMage(!(rs.getInt("use_mage") == 0));
         armor.setUseDarkelf(!(rs.getInt("use_darkelf") == 0));
         armor.setUseDragonKnight(!(rs.getInt("use_dragonknight") == 0));
         armor.setUseBlackwizard(!(rs.getInt("use_blackwizard") == 0));
         armor.setUse전사(!(rs.getInt("use_warrior") == 0));
         armor.setUseFencer(!(rs.getInt("use_fencer") == 0));
         armor.setUseLancer(!(rs.getInt("use_lancer") == 0));
         armor.set_adddex(rs.getByte("add_dex"));
         armor.set_addstr(rs.getByte("add_str"));
         armor.set_addint(rs.getByte("add_int"));
         armor.set_addwis(rs.getByte("add_wis"));
         armor.set_addcon(rs.getByte("add_con"));
         armor.set_addcha(rs.getByte("add_cha"));
         armor.set_addhp(rs.getInt("add_hp"));
         armor.set_addmp(rs.getInt("add_mp"));
         armor.set_addhpr(rs.getInt("add_hpr"));
         armor.set_addmpr(rs.getInt("add_mpr"));
         armor.set_addsp(rs.getInt("add_sp"));
         armor.setAddExp(rs.getInt("add_exp"));
         armor.set_addeinhasadper(rs.getInt("add_einhasadper"));
         armor.setMinLevel(rs.getInt("min_lvl"));
         armor.setMaxLevel(rs.getInt("max_lvl"));
         armor.set_mdef(rs.getInt("m_def"));
         armor.setWeightReduction(rs.getInt("weight_reduction"));
         armor.set_damage_reduction(rs.getInt("damage_reduction"));
         armor.setHitRate(rs.getInt("hit_rate"));
         armor.setDmgRate(rs.getInt("dmg_rate"));
         armor.setBowHitRate(rs.getInt("bow_hit_rate"));
         armor.setBowDmgRate(rs.getInt("bow_dmg_rate"));
         armor.setHasteItem(!(rs.getInt("haste_item") == 0));
         armor.setBless(rs.getInt("bless"));
         armor.setTradable((rs.getInt("trade") == 0));
         armor.setCantDelete((rs.getInt("cant_delete") == 1));
         armor.set_defense_earth(rs.getInt("defense_earth"));
         armor.set_defense_water(rs.getInt("defense_water"));
         armor.set_defense_wind(rs.getInt("defense_wind"));
         armor.set_defense_fire(rs.getInt("defense_fire"));
         armor.set_defense_all(rs.getInt("defense_all"));
         armor.setSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY, rs.getInt("ability_resis"));
         armor.setSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY, rs.getInt("ability_pierce"));
         armor.setSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT, rs.getInt("spirit_resis"));
         armor.setSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT, rs.getInt("spirit_pierce"));
         armor.setSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL, rs.getInt("dragonS_resis"));
         armor.setSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL, rs.getInt("dragonS_pierce"));
         armor.setSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR, rs.getInt("fear_resis"));
         armor.setSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR, rs.getInt("fear_pierce"));
         armor.setSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, rs.getInt("all_resis"));
         armor.setSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, rs.getInt("all_pierce"));

         armor.set_regist_calcPcDefense(rs.getInt("PVPcalcPcDefense"));
         armor.set_regist_PVPweaponTotalDamage(rs.getInt("PVPweaponTotalDamage"));
         armor.setMaxUseTime(rs.getInt("max_use_time"));
         armor.setMagicName(rs.getString("Magic_name"));
         armor.setEndedTimeMessage(rs.getBoolean("isEndedTimeMessage"));
         armor.set_missile_critical_probability(rs.getInt("missile_critical_probability"));
         armor.set_melee_critical_probability(rs.getInt("melee_critical_probability"));
         armor.set_magic_critical_probability(rs.getInt("magic_critical_probability"));
         armor.setMagicHitup(rs.getInt("magic_hit_up"));
         armor.setArmorReductionCancel(rs.getInt("add_reduction_cancel"));
         armor.setPVPWeaponReductionCancel(rs.getInt("PVPDmgReducIgnore"));
         armor.setPVPMagicReduction(rs.getInt("PVPMdmgReduction"));
         armor.setPVPMagicReductionCancel(rs.getInt("PVPMdmgReducIgnore"));
         armor.setIIg(rs.getInt("ImmuneIgnore"));
         armor.setTitanPercent(rs.getInt("titan_percent"));
         armor.setOverlaySurfId(rs.getInt("overlay_surf_id"));

         armor.setWareHouseLimitType(WareHouseLeaveType.fromString(rs.getString("warehouse_type")));
         armor.setWareHouseLimitLevel(rs.getInt("warehouse_limit_enchant"));


         armor.setAttackDelayRate(rs.getInt("attack_speed"));
         armor.setMoveDelayRate(rs.getInt("move_speed"));
         armor.setMpAr16(rs.getInt("mpAr16"));


           if (Config.ServerAdSetting.IsValidItemId) { // 如果 Config.ServerAdSetting 的 IsValidItemId 為真
               if (this._etcitems != null && this._etcitems.containsKey(Integer.valueOf(armor.getItemId()))) {
                   System.out.println(String.format("防具 %d(%s) 與 etcItem 重複。", new Object[] { Integer.valueOf(armor.getItemId()), armor.getName() }));
               }
               if (this._weapons != null && this._weapons.containsKey(Integer.valueOf(armor.getItemId()))) {
                   System.out.println(String.format("防具 %d(%s) 與武器重複。", new Object[] { Integer.valueOf(armor.getItemId()), armor.getName() }));
               }
           }
           result.put(new Integer(armor.getItemId()), armor); // 將防具放入結果中
       }
     } catch (NullPointerException e) {
         _log.log(Level.SEVERE, armor.getName() + "(" + armor.getItemId() + ")" + " 讀取失敗。"); // 捕獲並記錄 NullPointerException 異常
     } catch (Exception e) {
         e.printStackTrace(); // 捕獲並打印其他異常
     } finally {
         SQLUtil.close(rs); // 關閉 ResultSet
         SQLUtil.close(pstm); // 關閉 PreparedStatement
         SQLUtil.close(con); // 關閉 Connection
     }

       return result; // 返回結果
   }
   public void initRace() {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();

       pstm = con.prepareStatement("delete from character_items where item_id > 8000000 and item_id < 12000000");
       pstm.execute();
       pstm.close();


       pstm = con.prepareStatement("delete from character_elf_warehouse where item_id > 8000000 and item_id < 12000000");
       pstm.execute();
       pstm.close();


       pstm = con.prepareStatement("delete from character_warehouse where item_id > 8000000 and item_id < 12000000");
       pstm.execute();
       pstm.close();


       pstm = con.prepareStatement("delete from clan_warehouse where item_id > 8000000 and item_id < 12000000");
       pstm.execute();
       pstm.close();


       pstm = con.prepareStatement("delete from character_package_warehouse where item_id > 8000000 and item_id < 12000000");
       pstm.execute();
       pstm.close();

       pstm = null;
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }


   private void buildFastLookupTable() {
     this._allTemplates = new HashMap<>(this._etcitems.size() + this._weapons.size() + this._armors.size() + this._race.size());
     this._allTemplates.putAll(this._etcitems);
     this._allTemplates.putAll(this._weapons);
     this._allTemplates.putAll(this._armors);
     this._allTemplates.putAll(this._race);
   }






   private void subLoad() {}






   public L1Item getTemplate(int id) {
     return this._allTemplates.get(Integer.valueOf(id));
   }

   public L1Item findDescId(int descId, int bless) {
     for (L1Item item : this._allTemplates.values()) {
       if (item != null && item.getItemDescId() == descId && item.getBless() == bless)
         return item;
     }
     return null;
   }

   public L1ItemInstance createItem(int itemId) {
     return createItem(itemId, true);
   }

   public L1ItemInstance createItem(int itemId, int objId, boolean identified) {
     L1Item temp = getTemplate(itemId);
     if (temp == null) {
       return null;
     }
     L1ItemInstance item = new L1ItemInstance();
     item.setId(objId);
     item.setItem(temp);
     item.setBless(temp.getBless());
     item.setIdentified(identified);

     L1World.getInstance().storeObject((L1Object)item);
     return item;
   }

   public L1ItemInstance createItem(int itemId, boolean identified) {
     L1Item temp = getTemplate(itemId);
     if (temp == null) {
       return null;
     }
     L1ItemInstance item = new L1ItemInstance();
     item.setId(IdFactory.getInstance().nextId());
     item.setItem(temp);
     item.setBless(temp.getBless());
     item.setIdentified(identified);

     L1World.getInstance().storeObject((L1Object)item);
     return item;
   }

   public L1ItemInstance createItem(L1Item temp) {
     if (temp == null)
       return null;
     L1ItemInstance item = new L1ItemInstance();
     item.setId(IdFactory.getInstance().nextId());
     item.setItem(temp);
     item.setBless(temp.getBless());
     L1World.getInstance().storeObject((L1Object)item);
     return item;
   }

   public ArrayList<L1Item> findItemByName(String name) {
     ArrayList<L1Item> result = new ArrayList<>(4);
     int nl = name.length();
     for (L1Item item : this._allTemplates.values()) {
       if (item == null) {
         continue;
       }
       String s = item.getName();
       if (s.length() != nl) {
         continue;
       }
       if (s.equals(name))
         result.add(item);
     }
     return result;
   }

   public L1Item findItem(Matcher<L1Item> matcher) {
     for (L1Item item : this._allTemplates.values()) {
       if (item == null) {
         continue;
       }
       if (matcher.matches(item)) {
         return item;
       }
     }
     return null;
   }

   public L1Item findStoreCachedItem(int descid, int bless) {
     List<L1Item> items = findDescCachedItems(descid);
     if (items == null) {
       return null;
     }
     for (L1Item item : items) {

       if (item.getBless() != bless) {
         continue;
       }
       return item;
     }
     return null;
   }

   public L1ItemInstance createItem(String itemName) {
     int itemId = findItemIdByName(itemName);
     L1Item temp = getTemplate(itemId);
     if (temp == null) {
       return null;
     }
     L1ItemInstance item = new L1ItemInstance();
     item.setId(IdFactory.getInstance().nextId());
     item.setItem(temp);
     item.setBless(temp.getBless());
     L1World.getInstance().storeObject((L1Object)item);
     return item;
   }

   public String findItemIdByName(int itemid) {
     L1Item item = this._allTemplates.get(Integer.valueOf(itemid));
     if (item != null)
       return item.getName();
     return null;
   }

   public int findItemIdByName(String name) {
     int nl = name.length();
     for (L1Item item : this._allTemplates.values()) {
       if (item == null) {
         continue;
       }
       String s = item.getName();
       if (s.length() != nl) {
         continue;
       }
       if (s.equals(name))
         return item.getItemId();
     }
     return 0;
   }

   public int findItemIdByNameWithoutSpace(String name) {
     for (L1Item item : this._allTemplates.values()) {
       if (item == null) {
         continue;
       }
       String s = item.getName();
       String new_name = name.replace(" ", "");
       if (s.replace(" ", "").equalsIgnoreCase(new_name))
         return item.getItemId();
     }
     return 0;
   }

   public int findItemIdByNameWithoutSpace(String name, int bless) {
     for (L1Item item : this._allTemplates.values()) {
       if (item == null) {
         continue;
       }
       String s = item.getName();
       String new_name = name.replace(" ", "");
       if (s.replace(" ", "").equalsIgnoreCase(new_name) && item.getBless() == bless)
         return item.getItemId();
     }
     return 0;
   }


   public void AddTicket(L1RaceTicket race) {
     this._race.put(new Integer(race.getItemId()), race);
     this._allTemplates.put(Integer.valueOf(race.getItemId()), race);
   }

   public int GetIssuedTicket() {
     return this._race.size();
   }

   public L1Item create_dogfight_ticket(L1Item item, int new_item_id, String name) {
     L1EtcItem etc = new L1EtcItem();
     etc.setItemId(new_item_id);
     etc.setItemDescId(item.getItemDescId());
     etc.setGfxId(item.getGfxId());
     etc.setGroundGfxId(item.getGroundGfxId());
     etc.setName(name);
     etc.setNameId(name);
     etc.setType(item.getType());
     etc.setUseType(item.getUseType());
     etc.setType2(0);
     etc.setMaterial(item.getMaterial());
     etc.setUseRoyal(item.isUseRoyal());
     etc.setUseKnight(item.isUseKnight());
     etc.setUseMage(item.isUseMage());
     etc.setUseElf(item.isUseElf());
     etc.setUseDarkelf(item.isUseDarkelf());
     etc.setUseDragonKnight(item.isUseDragonKnight());
     etc.setUseBlackwizard(item.isUseBlackwizard());
     etc.setUse전사(item.isUse전사());
     etc.setUseFencer(item.isUseFencer());
     etc.setUseLancer(item.isUseLancer());
     etc.setWeight(item.getWeight());
     etc.set_stackable(item.isStackable());
     etc.setMaxChargeCount(item.getMaxChargeCount());

     etc.setHitModifier(item.getHitModifier());
     etc.setDmgModifier(item.getDmgModifier());
     etc.setMinLevel(item.getMinLevel());
     etc.setMaxLevel(item.getMaxLevel());

     etc.set_locx(item.get_locx());
     etc.set_locy(item.get_locy());
     etc.set_mapid(item.get_mapid());

     etc.setBless(item.getBless());

     etc.setTradable(item.isTradable());
     etc.setWareHouseLimitType(item.getWareHouseLimitType());
     etc.setWareHouseLimitLevel(item.getWareHouseLimitLevel());
     etc.setCantDelete(item.isCantDelete());
     etc.set_delayid(item.get_delayid());
     etc.set_delaytime(item.get_delaytime());
     etc.set_delayEffect(item.get_delayEffect());
     etc.setFoodVolume(item.getFoodVolume());
     etc.setToBeSavedAtOnce(item.isToBeSavedAtOnce());
     etc.setEndedTimeMessage(item.isEndedTimeMessage());
     etc.setUseEffetId(item.getUseEffectId());



     this._allTemplates.put(Integer.valueOf(new_item_id), etc);
     this._etcitems.put(Integer.valueOf(new_item_id), etc);

     return (L1Item)etc;
   }



   public L1Item clone(L1Item item, String name) {
     if (item.getType2() == 1) {
       L1Weapon weapon = new L1Weapon();
       weapon.setItemId(item.getItemId());
       weapon.setName(item.getName());
       weapon.setNameId(item.getNameId());
       weapon.setType(item.getType());
       weapon.setType1(item.getType1());
       weapon.setType2(1);
       weapon.setUseType(1);
       weapon.setMaterial(item.getMaterial());
       weapon.setWeight(item.getWeight());
       weapon.setGfxId(item.getGfxId());
       weapon.setGroundGfxId(item.getGroundGfxId());
       weapon.setItemDescId(item.getItemDescId());
       weapon.setDmgSmall(item.getDmgSmall());
       weapon.setDmgLarge(item.getDmgLarge());
       weapon.set_safeenchant(item.get_safeenchant());
       weapon.setUseRoyal(item.isUseRoyal());
       weapon.setUseKnight(item.isUseKnight());
       weapon.setUseElf(item.isUseElf());
       weapon.setUseMage(item.isUseMage());
       weapon.setUseDarkelf(item.isUseDarkelf());
       weapon.setUseDragonKnight(item.isUseDragonKnight());
       weapon.setUseBlackwizard(item.isUseBlackwizard());
       weapon.setUse전사(item.isUse전사());
       weapon.setHitModifier(item.getHitModifier());
       weapon.setDmgModifier(item.getDmgModifier());
       weapon.set_addstr(item.get_addstr());
       weapon.set_adddex(item.get_adddex());
       weapon.set_addcon(item.get_addcon());
       weapon.set_addint(item.get_addint());
       weapon.set_addwis(item.get_addwis());
       weapon.set_addcha(item.get_addcha());
       weapon.set_addhp(item.get_addhp());
       weapon.set_addmp(item.get_addmp());
       weapon.set_addhpr(item.get_addhpr());
       weapon.set_addmpr(item.get_addmpr());
       weapon.set_addsp(item.get_addsp());
       weapon.set_mdef(item.get_mdef());
       weapon.setDoubleDmgChance(item.getDoubleDmgChance());
       weapon.set_double_dmg_enchant_value(item.get_double_dmg_enchant_value());
       weapon.set_weak_point_chance(item.get_weak_point_chance());
       weapon.set_weak_point_enchant_value(item.get_weak_point_enchant_value());
       weapon.setMagicDmgModifier(item.getMagicDmgModifier());
       weapon.set_canbedmg(item.get_canbedmg());
       weapon.setMinLevel(item.getMinLevel());
       weapon.setMaxLevel(item.getMaxLevel());
       weapon.setBless(item.getBless());
       weapon.setTradable(item.isTradable());
       weapon.setCantDelete(item.isCantDelete());
       weapon.setHasteItem(item.isHasteItem());
       weapon.setMaxUseTime(item.getMaxUseTime());
       weapon.set_missile_critical_probability(item.get_missile_critical_probability());
       weapon.set_melee_critical_probability(item.get_melee_critical_probability());
       return (L1Item)weapon;
     }
     if (item.getType2() == 2) {
       L1Armor armor = new L1Armor();
       armor.setItemId(item.getItemId());
       armor.setName(item.getName());
       armor.setNameId(item.getNameId());
       armor.setType(item.getType());
       armor.setType2(2);
       armor.setUseType(item.getUseType());
       armor.setMaterial(item.getMaterial());
       armor.setWeight(item.getWeight());
       armor.setGfxId(item.getGfxId());
       armor.setGroundGfxId(item.getGroundGfxId());
       armor.setItemDescId(item.getItemDescId());
       armor.set_ac(item.get_ac());
       armor.set_safeenchant(item.get_safeenchant());
       armor.setUseRoyal(item.isUseRoyal());
       armor.setUseKnight(item.isUseKnight());
       armor.setUseElf(item.isUseElf());
       armor.setUseMage(item.isUseMage());
       armor.setUseDarkelf(item.isUseDarkelf());
       armor.setUseDragonKnight(item.isUseDragonKnight());
       armor.setUseBlackwizard(item.isUseBlackwizard());
       armor.setUse전사(item.isUse전사());
       armor.set_addstr(item.get_addstr());
       armor.set_addcon(item.get_addcon());
       armor.set_adddex(item.get_adddex());
       armor.set_addint(item.get_addint());
       armor.set_addwis(item.get_addwis());
       armor.set_addcha(item.get_addcha());
       armor.set_addhp(item.get_addhp());
       armor.set_addmp(item.get_addmp());
       armor.set_addhpr(item.get_addhpr());
       armor.set_addmpr(item.get_addmpr());
       armor.set_addsp(item.get_addsp());
       armor.set_addeinhasadper(item.get_addeinhasadper());
       armor.setMinLevel(item.getMinLevel());
       armor.setMaxLevel(item.getMaxLevel());
       armor.set_mdef(item.get_mdef());
       armor.set_damage_reduction(item.get_damage_reduction());
       armor.setWeightReduction(item.getWeightReduction());
       armor.setBowHitRate(item.getBowHitRate());
       armor.setHasteItem(item.isHasteItem());
       armor.setBless(item.getBless());
       armor.setTradable(item.isTradable());
       armor.setCantDelete(item.isCantDelete());
       armor.set_defense_earth(item.get_defense_earth());
       armor.set_defense_water(item.get_defense_water());
       armor.set_defense_wind(item.get_defense_wind());
       armor.set_defense_fire(item.get_defense_fire());
       armor.set_defense_all(item.get_defense_all());
       armor.setSpecialResistanceMap(item.getSpecialResistanceMap());
       armor.setSpecialPierceMap(item.getSpecialPierceMap());
       armor.set_regist_calcPcDefense(item.get_regist_calcPcDefense());
       armor.set_regist_PVPweaponTotalDamage(item.get_regist_PVPweaponTotalDamage());
       armor.setMaxUseTime(item.getMaxUseTime());
       armor.set_missile_critical_probability(item.get_missile_critical_probability());
       armor.set_melee_critical_probability(item.get_melee_critical_probability());
       armor.setHitModifier(item.getHitModifier());
       armor.setDmgModifier(item.getDmgModifier());
       armor.setMagicHitup(item.getMagicHitup());
       armor.setArmorReductionCancel(item.getArmorReductionCancel());
       return (L1Item)armor;
     }  if (item.getType2() == 0) {
       L1EtcItem etc = new L1EtcItem();
       etc.setItemId(item.getItemId());
       etc.setName(name);
       etc.setNameId(item.getNameId());
       etc.setType(item.getType());
       etc.setUseType(item.getUseType());
       etc.setType2(0);
       etc.setMaterial(item.getMaterial());
       etc.setUseRoyal(item.isUseRoyal());
       etc.setUseKnight(item.isUseKnight());
       etc.setUseElf(item.isUseElf());
       etc.setUseMage(item.isUseMage());
       etc.setUseDarkelf(item.isUseDarkelf());
       etc.setUseDragonKnight(item.isUseDragonKnight());
       etc.setUseBlackwizard(item.isUseBlackwizard());
       etc.setUse전사(item.isUse전사());
       etc.setWeight(item.getWeight());
       etc.setGfxId(item.getGfxId());
       etc.setGroundGfxId(item.getGroundGfxId());
       etc.setItemDescId(item.getItemDescId());
       etc.setMinLevel(item.getMinLevel());
       etc.setMaxLevel(item.getMaxLevel());
       etc.setBless(item.getBless());
       etc.setTradable(item.isTradable());
       etc.setCantDelete(item.isCantDelete());
       etc.setDmgSmall(item.getDmgSmall());
       etc.setDmgLarge(item.getDmgLarge());
       etc.set_stackable(item.isStackable());
       etc.setMaxChargeCount(item.getMaxChargeCount());
       etc.set_locx(item.get_locx());
       etc.set_locy(item.get_locy());
       etc.set_mapid(item.get_mapid());
       etc.set_delayid(item.get_delayid());
       etc.set_delaytime(item.get_delaytime());
       etc.set_delayEffect(item.get_delayEffect());
       etc.setFoodVolume(item.getFoodVolume());
       etc.setToBeSavedAtOnce(item.isToBeSavedAtOnce());
       etc.set_missile_critical_probability(item.get_missile_critical_probability());
       etc.set_melee_critical_probability(item.get_melee_critical_probability());
       etc.setHitModifier(item.getHitModifier());
       etc.setDmgModifier(item.getDmgModifier());
       return (L1Item)etc;
     }
     return null;
   }

   public boolean findItemId(int id) {
     return this._allTemplates.containsKey(Integer.valueOf(id));
   }

   public static int parseWeaponId(String s) {
     if (s.equalsIgnoreCase("sword"))
       return 4;
     if (s.equalsIgnoreCase("dagger"))
       return 46;
     if (s.equalsIgnoreCase("tohandsword"))
       return 50;
     if (s.equalsIgnoreCase("bow"))
       return 20;
     if (s.equalsIgnoreCase("spear"))
       return 24;
     if (s.equalsIgnoreCase("blunt"))
       return 11;
     if (s.equalsIgnoreCase("staff"))
       return 40;
     if (s.equalsIgnoreCase("throwingknife"))
       return 2922;
     if (s.equalsIgnoreCase("arrow"))
       return 66;
     if (s.equalsIgnoreCase("gauntlet"))
       return 62;
     if (s.equalsIgnoreCase("claw"))
       return 58;
     if (s.equalsIgnoreCase("edoryu"))
       return 54;
     if (s.equalsIgnoreCase("singlebow"))
       return 20;
     if (s.equalsIgnoreCase("singlespear"))
       return 24;
     if (s.equalsIgnoreCase("tohandblunt"))
       return 11;
     if (s.equalsIgnoreCase("tohandstaff"))
       return 40;
     if (s.equalsIgnoreCase("keyring"))
       return 58;
     if (s.equalsIgnoreCase("chainsword"))
       return 24;
     System.out.println(String.format("無效的 物品 武器 ID %s", new Object[] { s }));
     return 0;
   }

   public static int parseWeaponType(String s) {
     if (s.equalsIgnoreCase("sword"))
       return 1;
     if (s.equalsIgnoreCase("dagger"))
       return 2;
     if (s.equalsIgnoreCase("tohandsword"))
       return 3;
     if (s.equalsIgnoreCase("bow"))
       return 4;
     if (s.equalsIgnoreCase("spear"))
       return 5;
     if (s.equalsIgnoreCase("blunt"))
       return 6;
     if (s.equalsIgnoreCase("staff"))
       return 7;
     if (s.equalsIgnoreCase("throwingknife"))
       return 8;
     if (s.equalsIgnoreCase("arrow"))
       return 9;
     if (s.equalsIgnoreCase("gauntlet"))
       return 10;
     if (s.equalsIgnoreCase("claw"))
       return 11;
     if (s.equalsIgnoreCase("edoryu"))
       return 12;
     if (s.equalsIgnoreCase("singlebow"))
       return 13;
     if (s.equalsIgnoreCase("singlespear"))
       return 14;
     if (s.equalsIgnoreCase("tohandblunt"))
       return 15;
     if (s.equalsIgnoreCase("tohandstaff"))
       return 16;
     if (s.equalsIgnoreCase("keyring"))
       return 17;
     if (s.equalsIgnoreCase("chainsword"))
       return 18;
     System.out.println(String.format("無效的 物品 武器 類型%s", new Object[] { s }));
     return 0;
   }

   public static int parseArmorType(String s) {
     if (s.equalsIgnoreCase("none"))
       return 0;
     if (s.equalsIgnoreCase("helm"))
       return 1;
     if (s.equalsIgnoreCase("armor"))
       return 2;
     if (s.equalsIgnoreCase("T"))
       return 3;
     if (s.equalsIgnoreCase("cloak"))
       return 4;
     if (s.equalsIgnoreCase("glove"))
       return 5;
     if (s.equalsIgnoreCase("boots"))
       return 6;
     if (s.equalsIgnoreCase("shield"))
       return 7;
     if (s.equalsIgnoreCase("amulet"))
       return 8;
     if (s.equalsIgnoreCase("ring"))
       return 9;
     if (s.equalsIgnoreCase("belt"))
       return 10;
     if (s.equalsIgnoreCase("ring2"))
       return 11;
     if (s.equalsIgnoreCase("earring"))
       return 12;
     if (s.equalsIgnoreCase("garder"))
       return 13;
     if (s.equalsIgnoreCase("ron"))
       return 25;
     if (s.equalsIgnoreCase("pair"))
       return 15;
     if (s.equalsIgnoreCase("ron2"))
       return 30;
     if (s.equalsIgnoreCase("should"))
       return 31;
     if (s.equalsIgnoreCase("badge"))
       return 32;
     if (s.equalsIgnoreCase("Pendant")) {
       return 33;
     }

     if (s.equalsIgnoreCase("ron_bonus"))
       return 90;
     if (s.equalsIgnoreCase("ron_bonus1"))
       return 91;
     if (s.equalsIgnoreCase("ron_bonus2"))
       return 92;
     if (s.equalsIgnoreCase("ron_bonus3"))
       return 93;
     if (s.equalsIgnoreCase("ron_bonus4"))
       return 94;
     if (s.equalsIgnoreCase("ron_bonus5"))
       return 95;
     if (s.equalsIgnoreCase("ron_bonus6"))
       return 96;
     if (s.equalsIgnoreCase("ron_bonus7"))
       return 97;
     if (s.equalsIgnoreCase("ron_bonus8"))
       return 98;
     if (s.equalsIgnoreCase("ron_bonus9")) {
       return 99;
     }
     System.out.println(String.format("無效的 物品 盔甲類型 %s", new Object[] { s }));
     return 0;
   }

   public static int parseEtcType(String s) {
     if (s.equalsIgnoreCase("arrow"))
       return 0;
     if (s.equalsIgnoreCase("wand"))
       return 1;
     if (s.equalsIgnoreCase("light"))
       return 2;
     if (s.equalsIgnoreCase("gem"))
       return 3;
     if (s.equalsIgnoreCase("totem"))
       return 4;
     if (s.equalsIgnoreCase("firecracker"))
       return 5;
     if (s.equalsIgnoreCase("potion"))
       return 6;
     if (s.equalsIgnoreCase("food"))
       return 7;
     if (s.equalsIgnoreCase("scroll"))
       return 8;
     if (s.equalsIgnoreCase("questitem"))
       return 9;
     if (s.equalsIgnoreCase("spellbook"))
       return 10;
     if (s.equalsIgnoreCase("petitem"))
       return 11;
     if (s.equalsIgnoreCase("other"))
       return 12;
     if (s.equalsIgnoreCase("material"))
       return 13;
     if (s.equalsIgnoreCase("event"))
       return 14;
     if (s.equalsIgnoreCase("sting"))
       return 15;
     if (s.equalsIgnoreCase("treasure_box"))
       return 16;
     if (s.equalsIgnoreCase("teleport_scroll"))
       return 17;
     if (s.equalsIgnoreCase("protect"))
       return 21;
     if (s.equalsIgnoreCase("spwan_wand"))
       return 74;
     if (s.equalsIgnoreCase("meterialchoice"))
       return 80;
     if (s.equalsIgnoreCase("box_item"))
       return 81;
     if (s.equalsIgnoreCase("armor_smelting_stone"))
       return 100;
     if (s.equalsIgnoreCase("smelting_remove"))
       return 101;
     if (s.equalsIgnoreCase("weapon_smelting_stone"))
       return 102;
     if (s.equalsIgnoreCase("temp_skill"))
       return 201;
     if (s.equalsIgnoreCase("week_box"))
       return 301;
     if (s.equalsIgnoreCase("dol_potential"))
       return 401;
     if (s.equalsIgnoreCase("bless_assign"))
       return 501;
     if (s.equalsIgnoreCase("select_item")) {
       return 601;
     }
     System.out.println(String.format("無效的 項目 等類型 %s", new Object[] { s }));
     return -1;
   }

     public static int parseMaterial(String s) {
         if (s.equalsIgnoreCase("NONE(未知)")) // 如果 s 等於 "NONE(모름)"（無，未知）
             return 0; // 返回 0
         if (s.equalsIgnoreCase("LIQUID(液體)")) // 如果 s 等於 "LIQUID(액체)"（液體）
             return 1; // 返回 1
         if (s.equalsIgnoreCase("WAX(蠟)")) // 如果 s 等於 "WAX(밀랍)"（蠟）
             return 2; // 返回 2
         if (s.equalsIgnoreCase("VEGGY(植物性)")) // 如果 s 等於 "VEGGY(식물性)"（植物性）
             return 3; // 返回 3
         if (s.equalsIgnoreCase("FLESH(動物性)")) // 如果 s 等於 "FLESH(動物性)"（動物性）
             return 4; // 返回 4
         if (s.equalsIgnoreCase("PAPER(紙)")) // 如果 s 等於 "PAPER(紙)"（紙）
             return 5; // 返回 5
         if (s.equalsIgnoreCase("CLOTH(布)")) // 如果 s 等於 "CLOTH(布)"（布）
             return 6; // 返回 6
         if (s.equalsIgnoreCase("LEATHER(皮革)")) // 如果 s 等於 "LEATHER(皮革)"（皮革）
             return 7; // 返回 7
         if (s.equalsIgnoreCase("WOOD(木材)")) // 如果 s 等於 "WOOD(木)"（木材）
             return 8; // 返回 8
         if (s.equalsIgnoreCase("BONE(骨頭)")) // 如果 s 等於 "BONE(骨)"（骨頭）
             return 9; // 返回 9
         if (s.equalsIgnoreCase("DRAGON_HIDE(龍鱗)")) // 如果 s 等於 "DRAGON_HIDE(龍鱗)"（龍鱗）
             return 10; // 返回 10
         if (s.equalsIgnoreCase("IRON(鐵)")) // 如果 s 等於 "IRON(鐵)"（鐵）
             return 11; // 返回 11
         if (s.equalsIgnoreCase("METAL(金屬)")) // 如果 s 等於 "METAL(金屬)"
             return 12; // 返回 12
         if (s.equalsIgnoreCase("COPPER(銅)")) // 如果 s 等於 "COPPER(銅)"
             return 13; // 返回 13
         if (s.equalsIgnoreCase("SILVER(銀)")) // 如果 s 等於 "SILVER(銀)"
             return 14; // 返回 14
         if (s.equalsIgnoreCase("GOLD(金)")) // 如果 s 等於 "GOLD(金)"
             return 15; // 返回 15
         if (s.equalsIgnoreCase("PLATINUM(白金)")) // 如果 s 等於 "PLATINUM(白金)"
             return 16; // 返回 16
         if (s.equalsIgnoreCase("MITHRIL(秘銀)")) // 如果 s 等於 "MITHRIL(秘銀)"
             return 17; // 返回 17
         if (s.equalsIgnoreCase("PLASTIC(塑膠)")) // 如果 s 等於 "PLASTIC(塑膠)"
             return 18; // 返回 18
         if (s.equalsIgnoreCase("GLASS(玻璃)")) // 如果 s 等於 "GLASS(玻璃)"
             return 19; // 返回 19
         if (s.equalsIgnoreCase("GEMSTONE(寶石)")) // 如果 s 等於 "GEMSTONE(寶石)"
             return 20; // 返回 20
         if (s.equalsIgnoreCase("MINERAL(礦物)")) // 如果 s 等於 "MINERAL(礦物)"
             return 21; // 返回 21
         if (s.equalsIgnoreCase("ORIHARUKON(奧里哈魯根)")) // 如果 s 等於 "ORIHARUKON(奧里哈魯根)"
             return 22; // 返回 22
         if (s.equalsIgnoreCase("DRANIUM(德萊姆)")) // 如果 s 等於 "DRANIUM(德萊姆)"
             return 23; // 返回 23
     System.out.println(String.format("無效的 物品材料 %s", new Object[] { s }));
     return 0;
   }

   public static int parseUseType(String s) {
     if (s.equalsIgnoreCase("none"))
       return -1;
     if (s.equalsIgnoreCase("normal"))
       return 0;
     if (s.equalsIgnoreCase("weapon"))
       return 1;
     if (s.equalsIgnoreCase("armor"))
       return 2;
     if (s.equalsIgnoreCase("spell_long"))
       return 5;
     if (s.equalsIgnoreCase("ntele"))
       return 6;
     if (s.equalsIgnoreCase("identify"))
       return 7;
     if (s.equalsIgnoreCase("res"))
       return 8;
     if (s.equalsIgnoreCase("teleport"))
       return 9;
     if (s.equalsIgnoreCase("letter"))
       return 12;
     if (s.equalsIgnoreCase("letter_w"))
       return 13;
     if (s.equalsIgnoreCase("choice"))
       return 14;
     if (s.equalsIgnoreCase("instrument"))
       return 15;
     if (s.equalsIgnoreCase("sosc"))
       return 16;
     if (s.equalsIgnoreCase("spell_short"))
       return 17;
     if (s.equalsIgnoreCase("T"))
       return 18;
     if (s.equalsIgnoreCase("cloak"))
       return 19;
     if (s.equalsIgnoreCase("glove"))
       return 20;
     if (s.equalsIgnoreCase("boots"))
       return 21;
     if (s.equalsIgnoreCase("helm"))
       return 22;
     if (s.equalsIgnoreCase("ring"))
       return 23;
     if (s.equalsIgnoreCase("amulet"))
       return 24;
     if (s.equalsIgnoreCase("shield") || s.equalsIgnoreCase("garder"))
       return 25;
     if (s.equalsIgnoreCase("dai"))
       return 26;
     if (s.equalsIgnoreCase("zel"))
       return 27;
     if (s.equalsIgnoreCase("blank"))
       return 28;
     if (s.equalsIgnoreCase("btele"))
       return 29;
     if (s.equalsIgnoreCase("spell_buff"))
       return 30;
     if (s.equalsIgnoreCase("ccard"))
       return 31;
     if (s.equalsIgnoreCase("ccard_w"))
       return 32;
     if (s.equalsIgnoreCase("vcard"))
       return 33;
     if (s.equalsIgnoreCase("vcard_w"))
       return 34;
     if (s.equalsIgnoreCase("wcard"))
       return 35;
     if (s.equalsIgnoreCase("wcard_w"))
       return 36;
     if (s.equalsIgnoreCase("belt"))
       return 37;
     if (s.equalsIgnoreCase("spell_long2"))
       return 39;
     if (s.equalsIgnoreCase("earring"))
       return 40;
     if (s.equalsIgnoreCase("fishing_rod"))
       return 42;
     if (s.equalsIgnoreCase("ron"))
       return 44;
     if (s.equalsIgnoreCase("acczel"))
       return 46;
     if (s.equalsIgnoreCase("healing"))
       return 51;
     if (s.equalsIgnoreCase("spell_extractor"))
       return 64;
     if (s.equalsIgnoreCase("tam"))
       return 68;
     if (s.equalsIgnoreCase("pair"))
       return 70;
     if (s.equalsIgnoreCase("magic_doll"))
       return 73;
     if (s.equalsIgnoreCase("ron2"))
       return 74;
     if (s.equalsIgnoreCase("should"))
       return 75;
     if (s.equalsIgnoreCase("pet_potion"))
       return 77;
     if (s.equalsIgnoreCase("badge"))
       return 78;
     if (s.equalsIgnoreCase("poly_controll"))
       return 79;
     if (s.equalsIgnoreCase("Pendant")) {
       return 80;
     }
     if (s.equalsIgnoreCase("ron_bonus"))
       return 90;
     if (s.equalsIgnoreCase("ron_bonus1"))
       return 91;
     if (s.equalsIgnoreCase("ron_bonus2"))
       return 92;
     if (s.equalsIgnoreCase("ron_bonus3"))
       return 93;
     if (s.equalsIgnoreCase("ron_bonus4"))
       return 94;
     if (s.equalsIgnoreCase("ron_bonus5"))
       return 95;
     if (s.equalsIgnoreCase("ron_bonus6"))
       return 96;
     if (s.equalsIgnoreCase("ron_bonus7"))
       return 97;
     if (s.equalsIgnoreCase("ron_bonus8"))
       return 98;
     if (s.equalsIgnoreCase("ron_bonus9"))
       return 99;
     if (s.equalsIgnoreCase("use_time")) {
       return 100;
     }
     System.out.println(String.format("無效的 項目 使用類型 %s", new Object[] { s }));
     return -1;
   }

   public List<L1Item> findItemId(TableFindItemFilter filter) {
     List<L1Item> list = new LinkedList<>();
     for (L1Item item : this._allTemplates.values()) {
       if (item == null) {
         continue;
       }
       if (!filter.isFilter(item))
         list.add(item);
     }
     return list;
   }

   public List<L1Item> findDescCachedItems(final int itemDescId) {
     List<L1Item> items = this._descCached.get(Integer.valueOf(itemDescId));
     if (items != null) {
       return items;
     }
     items = findItemId(new TableFindItemFilter()
         {
           public boolean isFilter(L1Item item) {
             return (item.getItemDescId() != itemDescId);
           }
         });

     if (items == null || items.size() <= 0) {
       return null;
     }
     this._descCached.put(Integer.valueOf(itemDescId), items);
     return items;
   }

   public L1Item findDescCachedItemsAtOnce(int itemDescId) {
     List<L1Item> list = findDescCachedItems(itemDescId);
     return (list == null || list.size() <= 0) ? null : list.get(0);
   }


     public L1Item findSmeltingCachedItem(SC_SYNTHESIS_SMELTING_DESIGN_ACK.Item item) {
         List<L1Item> items = findDescCachedItems(item.get_name_id()); // 根據名稱ID查找緩存的物品列表
         if (items == null) { // 如果找不到對應的物品列表
             System.out.println(String.format("[(再煉石合成)找不到對應的物品。] 名稱ID：%d, 圖標ID：%d", new Object[] { Integer.valueOf(item.get_name_id()), Integer.valueOf(item.get_icon()) }));
             return null;
         }

         int iconId = item.get_icon(); // 獲取圖標ID
         for (L1Item l1Item : items) { // 遍歷緩存的物品列表
             if (l1Item.getGfxId() == iconId) { // 如果物品的圖形ID與圖標ID匹配
                 return l1Item; // 返回找到的物品
             }
         }
         System.out.println(String.format("[(再煉石合成)找不到對應的物品。] 名稱ID：%d, 圖標ID：%d", new Object[] { Integer.valueOf(item.get_name_id()), Integer.valueOf(item.get_icon()) }));
         return null; // 如果沒有找到匹配的物品，返回 null
     }

     public L1Item findAlchemyCachedItem(SC_ALCHEMY_DESIGN_ACK.Item item) {
         List<L1Item> items = findDescCachedItems(item.get_name_id()); // 根據名稱ID查找緩存的物品列表
         if (items == null) { // 如果找不到對應的物品列表
             System.out.println(String.format("[(人形合成)找不到對應的物品。] 名稱ID：%d, 圖標ID：%d", new Object[] { Integer.valueOf(item.get_name_id()), Integer.valueOf(item.get_icon()) }));
             return null;
         }

         int iconId = item.get_icon(); // 獲取圖標ID
         for (L1Item l1Item : items) { // 遍歷緩存的物品列表
             if (l1Item.getGfxId() == iconId) { // 如果物品的圖形ID與圖標ID匹配
                 return l1Item; // 返回找到的物品
             }
         }
         System.out.println(String.format("[(人形合成)找不到對應的物品。] 名稱ID：%d, 圖標ID：%d", new Object[] { Integer.valueOf(item.get_name_id()), Integer.valueOf(item.get_icon()) }));
         return null; // 如果沒有找到匹配的物品，返回 null
     }

   public L1Item findCraftCachedItem(CraftCommonBin.SC_CRAFT_LIST_ALL_ACK.Craft.CraftOutputList.CraftOutputSFList.CraftOutputItem outputItem) {
     List<L1Item> items = findDescCachedItems(outputItem.get_name_id());
     if (items == null) {
       return null;
     }
     int iconId = outputItem.get_iconId();
     int bless = outputItem.get_bless();
     for (L1Item item : items) {
       if (item.getGfxId() != iconId) {
         continue;
       }

       if (bless != 3 &&
         item.getBless() != bless) {
         continue;
       }


       return item;
     }
     return null;
   }

   public L1Item findCraftCachedFavorItem(CraftCommonBin.SC_CRAFT_LIST_ALL_ACK.Craft.CraftOutputList.CraftOutputSFList.CraftOutputItem outputItem) {
     List<L1Item> items = findDescCachedItems(outputItem.get_name_id());
     if (items == null) {
       return null;
     }
     int iconId = outputItem.get_iconId();
     int bless = outputItem.get_bless();
     for (L1Item item : items) {
       if (item.getGfxId() != iconId) {
         continue;
       }

       if (item.getType() != 16 && bless != 3 &&
         item.getBless() != bless) {
         continue;
       }


       return item;
     }
     return null;
   }
 }


