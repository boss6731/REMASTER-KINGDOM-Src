 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import java.util.Map;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_System.SC_FREE_BUFF_SHIELD_INFO_ACK;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.templates.L1FreeShield;
 import l1j.server.server.utils.SQLUtil;


 public class CharacterFreeShieldTable
 {
   public static CharacterFreeShieldTable _instance;
   public Map<Integer, L1FreeShield> _free_shield_info = new HashMap<>();

   public static CharacterFreeShieldTable getInstance() {
     if (_instance == null) {
       _instance = new CharacterFreeShieldTable();
     }
     return _instance;
   }

   public static void reload() {
     CharacterFreeShieldTable oldInstance = _instance;
     _instance = new CharacterFreeShieldTable();
     oldInstance._free_shield_info.clear();
   }

   private CharacterFreeShieldTable() {
     loadCharacterFreeShield();
   }

   private void loadCharacterFreeShield() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM character_free_shield");
       rs = pstm.executeQuery();
       while (rs.next()) {
         L1FreeShield freeshield = new L1FreeShield();
         freeshield.set_Account_Id(rs.getInt("account_id"));
         freeshield.set_Account_name(rs.getString("account_name"));
         freeshield.set_Pc_Gaho(rs.getInt("pc_gaho"));
         freeshield.set_Pc_Gaho_use(rs.getInt("pc_gaho_use"));
         freeshield.set_Free_Gaho(rs.getInt("free_gaho"));
         freeshield.set_Free_Gaho_use(rs.getInt("free_gaho_use"));
         freeshield.set_Event_Gaho(rs.getInt("event_gaho"));
         freeshield.set_Event_Gaho_use(rs.getInt("event_gaho_use"));
         this._free_shield_info.put(Integer.valueOf(freeshield.get_Account_Id()), freeshield);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
   }



   private void InsertCharacterFreeShield(final L1PcInstance pc) {
     Updator.exec("insert into character_free_shield set account_id=?, account_name=?, pc_gaho=?, pc_gaho_use=?, free_gaho=?, free_gaho_use=?, event_gaho=?, event_gaho_use=?", new Handler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             int idx = 0;
             pstm.setInt(++idx, pc.getAccount().getAccountId());
             pstm.setString(++idx, pc.getAccountName());
             pstm.setInt(++idx, 0);
             pstm.setInt(++idx, 0);
             pstm.setInt(++idx, 0);
             pstm.setInt(++idx, 0);
             pstm.setInt(++idx, 0);
             pstm.setInt(++idx, 0);
           }
         });
   }


   private void InsertCharacterFreeShieldLogin(final int login, final String name) {
     Updator.exec("insert into character_free_shield set account_id=?, account_name=?, pc_gaho=?, pc_gaho_use=?, free_gaho=?, free_gaho_use=?, event_gaho=?, event_gaho_use=?", new Handler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             int idx = 0;
             pstm.setInt(++idx, login);
             pstm.setString(++idx, name);
             pstm.setInt(++idx, 0);
             pstm.setInt(++idx, 0);
             pstm.setInt(++idx, 0);
             pstm.setInt(++idx, 0);
             pstm.setInt(++idx, 0);
             pstm.setInt(++idx, 0);
           }
         });
   }
   public void updateFreeShileld(final L1PcInstance pc) {
     final int accountid = pc.getAccount().getAccountId();
     Updator.exec("update character_free_shield set pc_gaho=?, pc_gaho_use=?, free_gaho=?, free_gaho_use=? where account_id=?", new Handler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             int idx = 0;
             pstm.setInt(++idx, CharacterFreeShieldTable.this.getPcGaho(pc));
             pstm.setInt(++idx, CharacterFreeShieldTable.this.getPcGahoUse(pc));
             pstm.setInt(++idx, CharacterFreeShieldTable.this.getFreeGaho(pc));
             pstm.setInt(++idx, CharacterFreeShieldTable.this.getFreeGahoUse(pc));
             pstm.setInt(++idx, accountid);
           }
         });
   }

   public void UpdateFreeShieldInfo(L1PcInstance pc) {
     if (getFreeShield(pc) == null) {
       L1FreeShield shield = new L1FreeShield();
       shield.set_Account_Id(pc.getAccount().getAccountId());
       shield.set_Account_name(pc.getAccountName());
       shield.set_Pc_Gaho(0);
       shield.set_Pc_Gaho_use(0);
       shield.set_Free_Gaho(0);
       shield.set_Free_Gaho_use(0);
       shield.set_Event_Gaho(0);
       shield.set_Event_Gaho_use(0);
       this._free_shield_info.put(Integer.valueOf(pc.getAccount().getAccountId()), shield);
       InsertCharacterFreeShield(pc);
     }
   }

   public void UpdateFreeShieldInfoLogin(int login, String name) {
     L1FreeShield shield = new L1FreeShield();
     shield.set_Account_Id(login);
     shield.set_Account_name(name);
     shield.set_Pc_Gaho(0);
     shield.set_Pc_Gaho_use(0);
     shield.set_Free_Gaho(0);
     shield.set_Free_Gaho_use(0);
     shield.set_Event_Gaho(0);
     shield.set_Event_Gaho_use(0);
     this._free_shield_info.put(Integer.valueOf(login), shield);
     InsertCharacterFreeShieldLogin(login, name);
   }



   public void getTam(L1PcInstance pc, int tamcount) {
     if (getFreeShield(pc) == null) {

       L1FreeShield shield = new L1FreeShield();
       shield.set_Account_Id(pc.getAccount().getAccountId());
       shield.set_Account_name(pc.getAccountName());
       shield.set_Pc_Gaho(0);
       shield.set_Pc_Gaho_use(0);
       shield.set_Free_Gaho(0);
       shield.set_Free_Gaho_use(0);
       shield.set_Event_Gaho(0);
       shield.set_Event_Gaho_use(0);
       this._free_shield_info.put(Integer.valueOf(pc.getAccount().getAccountId()), shield);
       InsertCharacterFreeShield(pc);
     }
     if (tamcount >= 1 && tamcount < 3) {
       if (((L1FreeShield)this._free_shield_info.get(Integer.valueOf(pc.getAccount().getAccountId()))).get_Free_Gaho_use() == 0) {
         ((L1FreeShield)this._free_shield_info.get(Integer.valueOf(pc.getAccount().getAccountId()))).set_Free_Gaho(1);
       } else {
         ((L1FreeShield)this._free_shield_info.get(Integer.valueOf(pc.getAccount().getAccountId()))).set_Free_Gaho(1 - ((L1FreeShield)this._free_shield_info.get(Integer.valueOf(pc.getAccount().getAccountId()))).get_Free_Gaho_use());
       }
     } else if (tamcount >= 3) {
       if (((L1FreeShield)this._free_shield_info.get(Integer.valueOf(pc.getAccount().getAccountId()))).get_Free_Gaho_use() == 0) {
         ((L1FreeShield)this._free_shield_info.get(Integer.valueOf(pc.getAccount().getAccountId()))).set_Free_Gaho(2);
       } else {
         ((L1FreeShield)this._free_shield_info.get(Integer.valueOf(pc.getAccount().getAccountId()))).set_Free_Gaho(2 - ((L1FreeShield)this._free_shield_info.get(Integer.valueOf(pc.getAccount().getAccountId()))).get_Free_Gaho_use());
       }
     }
     SC_FREE_BUFF_SHIELD_INFO_ACK.send(pc);
     updateFreeShileld(pc);
   }

   public void getPCBuff(L1PcInstance pc) {
     if (getFreeShield(pc) == null) {

       L1FreeShield shield = new L1FreeShield();
       shield.set_Account_Id(pc.getAccount().getAccountId());
       shield.set_Account_name(pc.getAccountName());
       shield.set_Pc_Gaho(0);
       shield.set_Pc_Gaho_use(0);
       shield.set_Free_Gaho(0);
       shield.set_Free_Gaho_use(0);
       shield.set_Event_Gaho(0);
       shield.set_Event_Gaho_use(0);
       this._free_shield_info.put(Integer.valueOf(pc.getAccount().getAccountId()), shield);
       InsertCharacterFreeShield(pc);
     }
     if (getPcGahoUse(pc) == 3) {
       ((L1FreeShield)this._free_shield_info.get(Integer.valueOf(pc.getAccount().getAccountId()))).set_Pc_Gaho(0);
     } else {
       ((L1FreeShield)this._free_shield_info.get(Integer.valueOf(pc.getAccount().getAccountId()))).set_Pc_Gaho(3 - getPcGahoUse(pc));
       ((L1FreeShield)this._free_shield_info.get(Integer.valueOf(pc.getAccount().getAccountId()))).set_Pc_Gaho_use(getPcGahoUse(pc));
     }
     updateFreeShileld(pc);
   }

   public int getPcGaho(L1PcInstance pc) {
     int accountid = pc.getAccount().getAccountId();
     int gaho = ((L1FreeShield)this._free_shield_info.get(Integer.valueOf(accountid))).get_Pc_Gaho();
     return gaho;
   }
   public int getPcGahoUse(L1PcInstance pc) {
     int accountid = pc.getAccount().getAccountId();
     int gahouse = ((L1FreeShield)this._free_shield_info.get(Integer.valueOf(accountid))).get_Pc_Gaho_use();
     return gahouse;
   }

   public void usePcGaho(L1PcInstance pc) {
     final int accountid = pc.getAccount().getAccountId();
     final int gahocount = getPcGaho(pc) - 1;
     final int gahousecount = getPcGahoUse(pc) + 1;
     ((L1FreeShield)this._free_shield_info.get(Integer.valueOf(accountid))).set_Pc_Gaho(gahocount);
     ((L1FreeShield)this._free_shield_info.get(Integer.valueOf(accountid))).set_Pc_Gaho_use(gahousecount);
     Updator.exec("update character_free_shield set pc_gaho=?, pc_gaho_use=? where account_id=?", new Handler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             int idx = 0;
             pstm.setInt(++idx, gahocount);
             pstm.setInt(++idx, gahousecount);
             pstm.setInt(++idx, accountid);
           }
         });
   }
   public int getFreeGaho(L1PcInstance pc) {
     int accountid = pc.getAccount().getAccountId();
     int gaho = ((L1FreeShield)this._free_shield_info.get(Integer.valueOf(accountid))).get_Free_Gaho();
     return gaho;
   }
   public int getFreeGahoUse(L1PcInstance pc) {
     int accountid = pc.getAccount().getAccountId();
     int gahouse = ((L1FreeShield)this._free_shield_info.get(Integer.valueOf(accountid))).get_Free_Gaho_use();
     return gahouse;
   }

   public void useFreeGaho(L1PcInstance pc) {
     final int accountid = pc.getAccount().getAccountId();
     final int gahocount = getFreeGaho(pc) - 1;
     final int gahousecount = getFreeGahoUse(pc) + 1;
     ((L1FreeShield)this._free_shield_info.get(Integer.valueOf(accountid))).set_Free_Gaho(gahocount);
     ((L1FreeShield)this._free_shield_info.get(Integer.valueOf(accountid))).set_Free_Gaho_use(gahousecount);
     Updator.exec("update character_free_shield set free_gaho=?, free_gaho_use=? where account_id=?", new Handler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             int idx = 0;
             pstm.setInt(++idx, gahocount);
             pstm.setInt(++idx, gahousecount);
             pstm.setInt(++idx, accountid);
           }
         });
   }

   public int getEventGaho(L1PcInstance pc) {
     int accountid = pc.getAccount().getAccountId();
     int gaho = ((L1FreeShield)this._free_shield_info.get(Integer.valueOf(accountid))).get_Event_Gaho();
     return gaho;
   }
   public int getEventGahoUse(L1PcInstance pc) {
     int accountid = pc.getAccount().getAccountId();
     int gahouse = ((L1FreeShield)this._free_shield_info.get(Integer.valueOf(accountid))).get_Event_Gaho_use();
     return gahouse;
   }


   private void updateInfo(L1PcInstance pc) {}


   public void useEventGaho(L1PcInstance pc) {
     final int accountid = pc.getAccount().getAccountId();
     final int gahocount = getEventGaho(pc) - 1;
     final int gahousecount = getEventGahoUse(pc) + 1;
     ((L1FreeShield)this._free_shield_info.get(Integer.valueOf(accountid))).set_Event_Gaho(gahocount);
     ((L1FreeShield)this._free_shield_info.get(Integer.valueOf(accountid))).set_Event_Gaho_use(gahousecount);
     Updator.exec("update character_free_shield set event_gaho=?, event_gaho_use=? where account_id=?", new Handler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             int idx = 0;
             pstm.setInt(++idx, gahocount);
             pstm.setInt(++idx, gahousecount);
             pstm.setInt(++idx, accountid);
           }
         });
   }

   public void resetGaho(final L1PcInstance pc) {
     Updator.exec("update character_free_shield set pc_gaho=?, pc_gaho_use=?, free_gaho=?, free_gaho_use=? where account_id=?", new Handler()
         {
           public void handle(PreparedStatement pstm) throws Exception {
             int pccount = 0;
             if (pc.isPcBuff()) {
               pccount = 3;
             }
             int tamcount = pc.tamcount();
             int freegahocount = 0;
             if (tamcount >= 3) {
               freegahocount = 2;
             } else if (tamcount >= 1 && tamcount < 3) {
               freegahocount = 1;
             }

             if (CharacterFreeShieldTable.this._free_shield_info.get(Integer.valueOf(pc.getAccount().getAccountId())) != null) {
               ((L1FreeShield)CharacterFreeShieldTable.this._free_shield_info.get(Integer.valueOf(pc.getAccount().getAccountId()))).set_Pc_Gaho(pccount);
               ((L1FreeShield)CharacterFreeShieldTable.this._free_shield_info.get(Integer.valueOf(pc.getAccount().getAccountId()))).set_Pc_Gaho_use(0);
               ((L1FreeShield)CharacterFreeShieldTable.this._free_shield_info.get(Integer.valueOf(pc.getAccount().getAccountId()))).set_Free_Gaho(freegahocount);
               ((L1FreeShield)CharacterFreeShieldTable.this._free_shield_info.get(Integer.valueOf(pc.getAccount().getAccountId()))).set_Free_Gaho_use(0);
             } else {
               L1FreeShield shield = new L1FreeShield();
               shield.set_Account_Id(pc.getAccount().getAccountId());
               shield.set_Account_name(pc.getAccountName());
               shield.set_Pc_Gaho(pccount);
               shield.set_Pc_Gaho_use(0);
               shield.set_Free_Gaho(freegahocount);
               shield.set_Free_Gaho_use(0);
               shield.set_Event_Gaho(0);
               shield.set_Event_Gaho_use(0);
               CharacterFreeShieldTable.this._free_shield_info.put(Integer.valueOf(pc.getAccount().getAccountId()), shield);
             }



             int idx = 0;
             pstm.setInt(++idx, pccount);
             pstm.setInt(++idx, 0);
             pstm.setInt(++idx, freegahocount);
             pstm.setInt(++idx, 0);
             pstm.setInt(++idx, pc.getAccount().getAccountId());
           }
         });
   }
   public L1FreeShield getFreeShieldLogin(int login, String name) {
     if (this._free_shield_info.get(Integer.valueOf(login)) == null) {
       UpdateFreeShieldInfoLogin(login, name);
     }
     if (this._free_shield_info.containsKey(Integer.valueOf(login))) {
       return this._free_shield_info.get(Integer.valueOf(login));
     }

     return null;
   }


   public L1FreeShield getFreeShield(L1PcInstance pc) {
     int accountid = pc.getAccount().getAccountId();

     if (this._free_shield_info.get(Integer.valueOf(accountid)) == null) {
       UpdateFreeShieldInfo(pc);
     }
     if (this._free_shield_info.containsKey(Integer.valueOf(accountid))) {
       return this._free_shield_info.get(Integer.valueOf(accountid));
     }
     return null;
   }
 }


