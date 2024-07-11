 package l1j.server.server.templates;
 public class L1NewNpcChat {
   private int _npcId;
   private int _chat_position;
   private String _chat_type;
   private boolean _isRepeat;
   private int _chatInterval;

   public int getNpcId() {
     return this._npcId;
   }
   private String _ment; private int _ment_chance; private boolean _isShout; private boolean _isWorldChat; private boolean _isNormalChat;
   public void setNpcId(int i) {
     this._npcId = i;
   }



   public int getChatPosition() {
     return this._chat_position;
   }

   public void setChatPosition(int s) {
     this._chat_position = s;
   }



   public String getChatType() {
     return this._chat_type;
   }

   public void setChatType(String s) {
     this._chat_type = s;
   }



   public boolean isRepeat() {
     return this._isRepeat;
   }

   public void setRepeat(boolean flag) {
     this._isRepeat = flag;
   }



   public int getChatInterval() {
     return this._chatInterval;
   }

   public void setChatInterval(int i) {
     this._chatInterval = i;
   }



   public String getMent() {
     return this._ment;
   }

   public void setMent(String s) {
     this._ment = s;
   }



   public int getMentChance() {
     return this._ment_chance;
   }

   public void setMentChance(int i) {
     this._ment_chance = i;
   }



   public boolean isShout() {
     return this._isShout;
   }

   public void setShout(boolean flag) {
     this._isShout = flag;
   }



   public boolean isWorldChat() {
     return this._isWorldChat;
   }

   public void setWorldChat(boolean flag) {
     this._isWorldChat = flag;
   }



   public boolean isNormalChat() {
     return this._isNormalChat;
   }

   public void setNormalChat(boolean flag) {
     this._isNormalChat = flag;
   }
 }


