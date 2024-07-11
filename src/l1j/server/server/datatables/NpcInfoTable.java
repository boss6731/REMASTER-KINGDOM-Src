 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.StringTokenizer;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import javolution.util.FastMap;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.utils.SQLUtil;


 public class NpcInfoTable
 {
   private static Logger _log = Logger.getLogger(NpcInfoTable.class.getName());
   private final FastMap<Integer, NpcInfoData> _dataMap;
   public static final String TYPE_SELF = "self";
   public static final String TYPE_SCREEN = "screen";
   public static final String TYPE_MAP = "map";
   private static NpcInfoTable _instance;

   public enum ScriptType {
     NONE, NUMBER, TEXT;
   }


   public static NpcInfoTable getInstance() {
     if (_instance == null) _instance = new NpcInfoTable();
     return _instance;
   }

   public NpcInfoData getNpcInfo(int npcId) {
     return this._dataMap.containsKey(Integer.valueOf(npcId)) ? (NpcInfoData)this._dataMap.get(Integer.valueOf(npcId)) : null;
   }

   private NpcInfoTable() {
     this._dataMap = load();
   }

   private FastMap<Integer, NpcInfoData> load() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     FastMap<Integer, NpcInfoData> dataMap = new FastMap();
     NpcInfoData data = null;
     ScriptInfo script = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("select npcId, recall, spawnActionId, reward, rewardRange, rewardItemId, rewardItemCount, rewardEinhasad, rewardGfx, msgRange, spawnMsg, dieMsg, dieMsgPcList, autoLoot, transformChance, transformId, transformGfxId, scriptType, scriptContent from npc_info");
       rs = pstm.executeQuery();
       while (rs.next()) {
         int npcId = rs.getInt("npcId");
         boolean recall = Boolean.valueOf(rs.getString("recall")).booleanValue();
         int spawnActionId = rs.getInt("spawnActionId");
         boolean reward = Boolean.valueOf(rs.getString("reward")).booleanValue();
         String rewardRange = rs.getString("rewardRange");
         int rewardItemId = rs.getInt("rewardItemId");
         int rewardItemCount = rs.getInt("rewardItemCount");
         int rewardEinhasad = rs.getInt("rewardEinhasad");
         int rewardGfx = rs.getInt("rewardGfx");
         String msgRange = rs.getString("msgRange");

         String[] spawnMsgArray = null;
         String spawnMsg = rs.getString("spawnMsg");
         if (spawnMsg != null) spawnMsgArray = spawnMsg.split(",");

         String[] dieMsgArray = null;
         String dieMsg = rs.getString("dieMsg");
         if (dieMsg != null) dieMsgArray = dieMsg.split(",");

         boolean dieMsgPcList = Boolean.valueOf(rs.getString("dieMsgPcList")).booleanValue();
         boolean autoLoot = Boolean.valueOf(rs.getString("autoLoot")).booleanValue();
         int transformChance = rs.getInt("transformChance");
         int transformId = rs.getInt("transformId");
         int transformGfxId = rs.getInt("transformGfxId");

         ScriptType scriptType = getScriptType(rs.getString("scriptType"));
         if (scriptType != ScriptType.NONE) script = getScriptInfo(rs.getString("scriptContent"), scriptType);

         data = new NpcInfoData(npcId, recall, spawnActionId, reward, rewardRange, rewardItemId, rewardItemCount, rewardEinhasad, rewardGfx, msgRange, spawnMsgArray, dieMsgArray, dieMsgPcList, autoLoot, transformChance, transformId, transformGfxId, script);
         dataMap.put(Integer.valueOf(data._npcId), data);
       }
     } catch (SQLException e) {
       _log.log(Level.SEVERE, "NpcInfoTable[]Error", e);
       e.printStackTrace();
     } catch (Exception e) {
       _log.log(Level.SEVERE, "NpcInfoTable[]Error", e);
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
     return dataMap;
   }

   private ScriptInfo getScriptInfo(String str, ScriptType scriptType) {
     ScriptInfo script = new ScriptInfo();
     script._type = scriptType;
     StringTokenizer st = new StringTokenizer(str, "\r\n");
     while (st.hasMoreTokens()) {
       String temp = st.nextToken().trim();
       if (temp.startsWith("map:")) {
         temp = temp.substring(temp.indexOf(":") + 1).trim();
         script._map = (scriptType == ScriptType.NUMBER) ? Integer.valueOf(Integer.parseInt(temp)) : temp; continue;
       }  if (temp.startsWith("effect:")) {
         temp = temp.substring(temp.indexOf(":") + 1).trim();
         script._effect = (scriptType == ScriptType.NUMBER) ? Integer.valueOf(Integer.parseInt(temp)) : temp; continue;
       }  if (temp.startsWith("stay:")) {
         temp = temp.substring(temp.indexOf(":") + 1).trim();
         script._stay = (scriptType == ScriptType.NUMBER) ? Integer.valueOf(Integer.parseInt(temp)) : temp;
       }
     }
     return script;
   }

   private ScriptType getScriptType(String str) {
     switch (str) { case "number":
         return ScriptType.NUMBER;
       case "text": return ScriptType.TEXT; }
      return ScriptType.NONE;
   }


   public static void reload() {
     NpcInfoTable oldInstance = _instance;
     _instance = new NpcInfoTable();
     oldInstance._dataMap.clear();
     oldInstance = null;
   }


   public class NpcInfoData
   {
     public int _npcId;

     public boolean _recall;

     public int _spawnActionId;
     public boolean _reward;
     public String _rewardRange;
     public int _rewardItemId;
     public int _rewardItemCount;
     public int _rewardEinhasad;
     public int _rewardGfx;
     public String _msgRange;
     public String[] _spawnMsg;
     public String[] _dieMsg;
     public boolean _dieMsgPcList;
     public boolean _autoLoot;
     public int _transformChance;
     public int _transformId;
     public int _transformGfxId;
     public NpcInfoTable.ScriptInfo _scriptInfo;

     public NpcInfoData(int _npcId, boolean _recall, int _spawnActionId, boolean _reward, String _rewardRange, int _rewardItemId, int _rewardItemCount, int _rewardEinhasad, int _rewardGfx, String _msgRange, String[] _spawnMsg, String[] _dieMsg, boolean _dieMsgPcList, boolean _autoLoot, int _transformChance, int _transformId, int _transformGfxId, NpcInfoTable.ScriptInfo _scriptInfo) {
       this._npcId = _npcId;
       this._recall = _recall;
       this._spawnActionId = _spawnActionId;
       this._reward = _reward;
       this._rewardRange = _rewardRange;
       this._rewardItemId = _rewardItemId;
       this._rewardItemCount = _rewardItemCount;
       this._rewardEinhasad = _rewardEinhasad;
       this._rewardGfx = _rewardGfx;
       this._msgRange = _msgRange;
       this._spawnMsg = _spawnMsg;
       this._dieMsg = _dieMsg;
       this._dieMsgPcList = _dieMsgPcList;
       this._autoLoot = _autoLoot;
       this._transformChance = _transformChance;
       this._transformId = _transformId;
       this._transformGfxId = _transformGfxId;
       this._scriptInfo = _scriptInfo;
     }
   }

   public class ScriptInfo {
     public NpcInfoTable.ScriptType _type;
     public Object _map;
     public Object _effect;
     public Object _stay;
   }
 }


