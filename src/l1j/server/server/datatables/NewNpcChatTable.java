 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.templates.L1NewNpcChat;
 import l1j.server.server.utils.SQLUtil;




 public class NewNpcChatTable
 {
   private static NewNpcChatTable _instance;
   private HashMap<Integer, L1NewNpcChat> _npcChatWalk = new HashMap<>();
   private HashMap<Integer, L1NewNpcChat> _npcChatDead = new HashMap<>();
   private HashMap<Integer, L1NewNpcChat> _npcChatHide = new HashMap<>();
   private HashMap<Integer, L1NewNpcChat> _npcChatSpawn = new HashMap<>();
   private HashMap<Integer, L1NewNpcChat> _npcChatEscape = new HashMap<>();

   public static NewNpcChatTable getInstance() {
     if (_instance == null) {
       _instance = new NewNpcChatTable();
     }
     return _instance;
   }

   private NewNpcChatTable() {
     FillNpcChatTable();
   }

   public static void reload() {
     NewNpcChatTable oldInstance = _instance;
     _instance = new NewNpcChatTable();
     oldInstance._npcChatWalk.clear();
     oldInstance._npcChatDead.clear();
     oldInstance._npcChatHide.clear();
     oldInstance._npcChatSpawn.clear();
     oldInstance._npcChatEscape.clear();
   }

   private void FillNpcChatTable() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM npc_chat");
       rs = pstm.executeQuery();
       L1NewNpcChat npcChat = null;
       while (rs.next()) {
         npcChat = new L1NewNpcChat();
         npcChat.setNpcId(rs.getInt("npcid"));
         npcChat.setChatPosition(ChatPosition(rs.getString("chat_position")));

         if (rs.getString("chat_type").equalsIgnoreCase("Shout")) {
           npcChat.setShout(true);
         } else if (rs.getString("chat_type").equalsIgnoreCase("World")) {
           npcChat.setWorldChat(true);
         } else if (rs.getString("chat_type").equalsIgnoreCase("Normal")) {
           npcChat.setNormalChat(true);
         }
         if (rs.getString("repeat").equalsIgnoreCase("true"))
           npcChat.setRepeat(true);
         if (rs.getString("repeat").equalsIgnoreCase("false")) {
           npcChat.setRepeat(false);
         }
         npcChat.setChatInterval(rs.getInt("delay"));
         npcChat.setMent(rs.getString("ment"));
         npcChat.setMentChance(rs.getInt("ment_chance"));

         if (npcChat.getChatPosition() == 0) {
           this._npcChatWalk.put(new Integer(npcChat.getNpcId()), npcChat); continue;
         }  if (npcChat.getChatPosition() == 1) {
           this._npcChatDead.put(new Integer(npcChat.getNpcId()), npcChat); continue;
         }  if (npcChat.getChatPosition() == 2) {
           this._npcChatHide.put(new Integer(npcChat.getNpcId()), npcChat); continue;
         }  if (npcChat.getChatPosition() == 4) {
           this._npcChatSpawn.put(new Integer(npcChat.getNpcId()), npcChat); continue;
         }  if (npcChat.getChatPosition() == 5) {
           this._npcChatEscape.put(new Integer(npcChat.getNpcId()), npcChat);
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

   public int ChatPosition(String p) {
     int type = 0;
     if (p.equalsIgnoreCase("Walk")) {
       type = 0;
     } else if (p.equalsIgnoreCase("Dead")) {
       type = 1;
     } else if (p.equalsIgnoreCase("Hide")) {
       type = 2;
     } else if (p.equalsIgnoreCase("Spawn")) {
       type = 4;
     } else if (p.equalsIgnoreCase("Escape")) {
       type = 5;
     } else if (p.equalsIgnoreCase("Attack")) {
       type = 6;
     }  return type;
   }

   public L1NewNpcChat getTemplateWalk(int i) {
     return this._npcChatWalk.get(new Integer(i));
   }

   public L1NewNpcChat getTemplateDead(int i) {
     return this._npcChatDead.get(new Integer(i));
   }

   public L1NewNpcChat getTemplateHide(int i) {
     return this._npcChatHide.get(new Integer(i));
   }

   public L1NewNpcChat getTemplateSpawn(int i) {
     return this._npcChatSpawn.get(new Integer(i));
   }

   public L1NewNpcChat getTemplateEscape(int i) {
     return this._npcChatEscape.get(new Integer(i));
   }
 }


