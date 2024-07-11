 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.templates.L1NpcChat;
 import l1j.server.server.utils.SQLUtil;





 public class NpcChatTable
 {
   private static NpcChatTable _instance;
   private HashMap<Integer, L1NpcChat> _npcChatAppearance = new HashMap<>();
   private HashMap<Integer, L1NpcChat> _npcChatDead = new HashMap<>();
   private HashMap<Integer, L1NpcChat> _npcChatHide = new HashMap<>();
   private HashMap<Integer, L1NpcChat> _npcChatGameTime = new HashMap<>();


   public static NpcChatTable getInstance() {
     if (_instance == null) {
       _instance = new NpcChatTable();
     }
     return _instance;
   }

   private NpcChatTable() {
     FillNpcChatTable();
   }

   public static void reload() {
     NpcChatTable oldInstance = _instance;
     _instance = new NpcChatTable();
     oldInstance._npcChatAppearance.clear();
     oldInstance._npcChatDead.clear();
     oldInstance._npcChatHide.clear();
     oldInstance._npcChatGameTime.clear();
   }

   private void FillNpcChatTable() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM npcchat");
       rs = pstm.executeQuery();
       L1NpcChat npcChat = null;
       while (rs.next()) {
         npcChat = new L1NpcChat();
         npcChat.setNpcId(rs.getInt("npc_id"));
         npcChat.setChatTiming(rs.getInt("chat_timing"));
         npcChat.setStartDelayTime(rs.getInt("start_delay_time"));
         npcChat.setChatId1(rs.getString("chat_id1"));
         npcChat.setChatId2(rs.getString("chat_id2"));
         npcChat.setChatId3(rs.getString("chat_id3"));
         npcChat.setChatId4(rs.getString("chat_id4"));
         npcChat.setChatId5(rs.getString("chat_id5"));
         npcChat.setChatInterval(rs.getInt("chat_interval"));
         npcChat.setShout(rs.getBoolean("is_shout"));
         npcChat.setWorldChat(rs.getBoolean("is_world_chat"));
         npcChat.setRepeat(rs.getBoolean("is_repeat"));
         npcChat.setRepeatInterval(rs.getInt("repeat_interval"));
         npcChat.setGameTime(rs.getInt("game_time"));

         if (npcChat.getChatTiming() == 0) {
           this._npcChatAppearance.put(new Integer(npcChat.getNpcId()), npcChat); continue;
         }  if (npcChat.getChatTiming() == 1) {
           this._npcChatDead.put(new Integer(npcChat.getNpcId()), npcChat); continue;
         }  if (npcChat.getChatTiming() == 2) {
           this._npcChatHide.put(new Integer(npcChat.getNpcId()), npcChat); continue;
         }  if (npcChat.getChatTiming() == 3) {
           this._npcChatGameTime.put(new Integer(npcChat.getNpcId()), npcChat);
         }
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public L1NpcChat getTemplateAppearance(int i) {
     return this._npcChatAppearance.get(new Integer(i));
   }

   public L1NpcChat getTemplateDead(int i) {
     return this._npcChatDead.get(new Integer(i));
   }

   public L1NpcChat getTemplateHide(int i) {
     return this._npcChatHide.get(new Integer(i));
   }

   public L1NpcChat getTemplateGameTime(int i) {
     return this._npcChatGameTime.get(new Integer(i));
   }

   public L1NpcChat[] getAllGameTime() {
     return (L1NpcChat[])this._npcChatGameTime.values()
       .toArray((Object[])new L1NpcChat[this._npcChatGameTime.size()]);
   }
 }


