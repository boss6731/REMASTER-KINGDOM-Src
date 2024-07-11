 package l1j.server.server;

 import l1j.server.server.clientpackets.C_ActionUi;
 import l1j.server.server.clientpackets.C_AddBookmark;
 import l1j.server.server.clientpackets.C_AddBuddy;
 import l1j.server.server.clientpackets.C_Amount;
 import l1j.server.server.clientpackets.C_Attack;
 import l1j.server.server.clientpackets.C_AttackNew;
 import l1j.server.server.clientpackets.C_Attr;
 import l1j.server.server.clientpackets.C_AuthLogin;
 import l1j.server.server.clientpackets.C_BanClan;
 import l1j.server.server.clientpackets.C_BanParty;
 import l1j.server.server.clientpackets.C_Board;
 import l1j.server.server.clientpackets.C_BoardBack;
 import l1j.server.server.clientpackets.C_BoardDelete;
 import l1j.server.server.clientpackets.C_BoardRead;
 import l1j.server.server.clientpackets.C_BoardWrite;
 import l1j.server.server.clientpackets.C_Buddy;
 import l1j.server.server.clientpackets.C_CallPlayer;
 import l1j.server.server.clientpackets.C_ChangeHeading;
 import l1j.server.server.clientpackets.C_CharcterConfig;
 import l1j.server.server.clientpackets.C_Chat;
 import l1j.server.server.clientpackets.C_ChatParty;
 import l1j.server.server.clientpackets.C_CheckPK;
 import l1j.server.server.clientpackets.C_Clan;
 import l1j.server.server.clientpackets.C_ClanAttention;
 import l1j.server.server.clientpackets.C_ClanMatching;
 import l1j.server.server.clientpackets.C_Craft;
 import l1j.server.server.clientpackets.C_CreateChar;
 import l1j.server.server.clientpackets.C_CreateClan;
 import l1j.server.server.clientpackets.C_CreateParty;
 import l1j.server.server.clientpackets.C_DelBuddy;
 import l1j.server.server.clientpackets.C_DeleteBookmark;
 import l1j.server.server.clientpackets.C_DeleteChar;
 import l1j.server.server.clientpackets.C_DeleteInventoryItem;
 import l1j.server.server.clientpackets.C_Deposit;
 import l1j.server.server.clientpackets.C_Door;
 import l1j.server.server.clientpackets.C_Drawal;
 import l1j.server.server.clientpackets.C_DropItem;
 import l1j.server.server.clientpackets.C_Emblem;
 import l1j.server.server.clientpackets.C_EnterPortal;
 import l1j.server.server.clientpackets.C_ExitGhost;
 import l1j.server.server.clientpackets.C_ExtraCommand;
 import l1j.server.server.clientpackets.C_Fight;
 import l1j.server.server.clientpackets.C_FishClick;
 import l1j.server.server.clientpackets.C_FixWeaponList;
 import l1j.server.server.clientpackets.C_GiveItem;
 import l1j.server.server.clientpackets.C_Horun;
 import l1j.server.server.clientpackets.C_HorunOK;
 import l1j.server.server.clientpackets.C_ItemUSe;
 import l1j.server.server.clientpackets.C_ItemUSe2;
 import l1j.server.server.clientpackets.C_JoinClan;
 import l1j.server.server.clientpackets.C_KeepALIVE;
 import l1j.server.server.clientpackets.C_LeaveClan;
 import l1j.server.server.clientpackets.C_LeaveParty;
 import l1j.server.server.clientpackets.C_LoginToServerOK;
 import l1j.server.server.clientpackets.C_LoginToServerWrap;
 import l1j.server.server.clientpackets.C_MailBox;
 import l1j.server.server.clientpackets.C_MoveChar;
 import l1j.server.server.clientpackets.C_NPCAction;
 import l1j.server.server.clientpackets.C_NPCTalk;
 import l1j.server.server.clientpackets.C_NewCharSelect;
 import l1j.server.server.clientpackets.C_Party;
 import l1j.server.server.clientpackets.C_PetMenu;
 import l1j.server.server.clientpackets.C_PickUpItem;
 import l1j.server.server.clientpackets.C_Pledge;
 import l1j.server.server.clientpackets.C_PledgeContent;
 import l1j.server.server.clientpackets.C_Propose;
 import l1j.server.server.clientpackets.C_Rank;
 import l1j.server.server.clientpackets.C_ReadNews;
 import l1j.server.server.clientpackets.C_Report;
 import l1j.server.server.clientpackets.C_Restart;
 import l1j.server.server.clientpackets.C_Result;
 import l1j.server.server.clientpackets.C_ReturnStaus;
 import l1j.server.server.clientpackets.C_ReturnToLogin;
 import l1j.server.server.clientpackets.C_SecurityStatus;
 import l1j.server.server.clientpackets.C_SecurityStatusSet;
 import l1j.server.server.clientpackets.C_SelectList;
 import l1j.server.server.clientpackets.C_SelectTarget;
 import l1j.server.server.clientpackets.C_Shop;
 import l1j.server.server.clientpackets.C_ShopList;
 import l1j.server.server.clientpackets.C_SkillBuy;
 import l1j.server.server.clientpackets.C_SkillBuyOK;
 import l1j.server.server.clientpackets.C_TaxRate;
 import l1j.server.server.clientpackets.C_Teleport;
 import l1j.server.server.clientpackets.C_Title;
 import l1j.server.server.clientpackets.C_Trade;
 import l1j.server.server.clientpackets.C_TradeAddItem;
 import l1j.server.server.clientpackets.C_TradeCancel;
 import l1j.server.server.clientpackets.C_TradeOK;
 import l1j.server.server.clientpackets.C_UsePetItem;
 import l1j.server.server.clientpackets.C_UseSkill;
 import l1j.server.server.clientpackets.C_War;
 import l1j.server.server.clientpackets.C_WhPw;
 import l1j.server.server.clientpackets.C_Who;
 import l1j.server.server.clientpackets.C_아덴상점;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_CharPass;
 import l1j.server.server.serverpackets.ServerBasePacket;



 public class PacketHandler
 {
   private final GameClient _client;

   public PacketHandler(GameClient clientthread) {
     this._client = clientthread;
   }
   public void handlePacket(byte[] abyte0, L1PcInstance object) throws Exception {
     C_LoginToServerWrap wrap;
     int i = abyte0[0] & 0xFF;




     switch (i) {
       case 157:
         new C_ActionUi(abyte0, this._client);
         new C_Craft(abyte0, this._client);
         break;
       case 201:
         new C_ClanAttention(abyte0, this._client);
         break;
       case 140:
         new C_PledgeContent(abyte0, this._client);
         break;
       case 130:
         new C_AttackNew(abyte0, this._client);
         break;
       case 53:
         new C_아덴상점(abyte0, this._client);
         break;
       case 128:
         new C_CharcterConfig(abyte0, this._client);
         break;
       case 131:
         new C_Door(abyte0, this._client);
         break;
       case 214:
         new C_Title(abyte0, this._client);
         break;
       case 28:
         new C_Clan(abyte0, this._client);
         break;
       case 221:
         new C_ClanMatching(abyte0, this._client);
         break;
       case 222:
         new C_BoardDelete(abyte0, this._client);
         break;
       case 135:
         new C_Pledge(abyte0, this._client);
         break;
       case 79:
         new C_ChangeHeading(abyte0, this._client);
         break;

       case 227:
         new C_NPCAction(abyte0, this._client);
         break;
       case 216:
         new C_UseSkill(abyte0, this._client);
         break;
       case 20:
         new C_Emblem(abyte0, this._client);
         break;
       case 195:
         new C_TradeCancel(abyte0, this._client);
         break;
       case 84:
         new C_AddBookmark(abyte0, this._client);
         break;
       case 26:
         new C_CreateClan(abyte0, this._client);
         break;
       case 21:
         new C_Propose(abyte0, this._client);
         break;
       case 24:
         new C_SkillBuy(abyte0, this._client);
         break;
       case 6:
         new C_BoardBack(abyte0, this._client);
         break;
       case 98:
         new C_Shop(abyte0, this._client);
         break;
       case 93:
         new C_BoardRead(abyte0, this._client);
         break;
       case 5:
         new C_Trade(abyte0, this._client);
         break;
       case 81:
         if (this._client.getAccount().getCPW() != null) {
           this._client.getAccount().setwaitpacket(abyte0);
           this._client.sendPacket((ServerBasePacket)new S_CharPass(20));
           return;
         }
         new C_DeleteChar(abyte0, this._client);
         break;
       case 176:
         new C_KeepALIVE(abyte0, this._client);
         break;
       case 33:
         new C_Attr(abyte0, this._client);
         break;
       case 85:
         new C_AuthLogin(abyte0, this._client);
         break;
       case 89:
         new C_Result(abyte0, this._client);
         break;
       case 36:
         new C_Deposit(abyte0, this._client);
         break;
       case 59:
         new C_Drawal(abyte0, this._client);
         break;
       case 184:
         new C_LoginToServerOK(abyte0, this._client);
         break;
       case 123:
         new C_SkillBuyOK(abyte0, this._client);
         break;
       case 247:
         new C_TradeAddItem(abyte0, this._client);
         break;
       case 138:
         new C_AddBuddy(abyte0, this._client);
         break;
       case 235:
         new C_ReturnToLogin(abyte0, this._client);
         break;
       case 111:
         new C_TradeOK(abyte0, this._client);
         break;
       case 9:
         new C_CheckPK(abyte0, this._client);
         break;
       case 41:
         new C_TaxRate(abyte0, this._client);
         break;
       case 194:
         new C_NewCharSelect(abyte0, this._client);
         break;
       case 58:
         new C_Restart(abyte0, this._client);
         break;
       case 56:
         new C_Buddy(abyte0, this._client);
         break;
       case 97:
         new C_DropItem(abyte0, this._client);
         break;
       case 119:
         new C_LeaveParty(abyte0, this._client);
         break;
       case 4:
       case 43:
         new C_Attack(abyte0, this._client);
         break;



       case 105:
         new C_BanClan(abyte0, this._client);
         break;
       case 121:
         new C_Board(abyte0, this._client);
         break;
       case 198:
         new C_DeleteInventoryItem(abyte0, this._client);
         break;
       case 108:
         new C_Party(abyte0, this._client);
         break;
       case 23:
         new C_PickUpItem(abyte0, this._client);
         break;
       case 174:
         new C_Who(abyte0, this._client);
         break;
       case 137:
         new C_GiveItem(abyte0, this._client);
         break;
       case 175:
         new C_MoveChar(abyte0, this._client);
         break;
       case 106:
         new C_DeleteBookmark(abyte0, this._client);
         break;
       case 112:
         new C_LeaveClan(abyte0, this._client);
         break;
       case 51:
         new C_NPCTalk(abyte0, this._client);
         break;
       case 65:
         new C_BanParty(abyte0, this._client);
         break;
       case 124:
         new C_DelBuddy(abyte0, this._client);
         break;
       case 197:
         new C_War(abyte0, this._client);
         break;
       case 168:
         wrap = new C_LoginToServerWrap(abyte0, this._client);
         wrap.clear();
         wrap = null;
         break;
       case 86:
         new C_ShopList(abyte0, this._client);
         break;
       case 141:
         new C_JoinClan(abyte0, this._client);
         break;
       case 110:
         new C_ReadNews(abyte0, this._client);
         break;


       case 35:
         new C_CreateChar(abyte0, this._client);
         break;
       case 50:
         new C_ExtraCommand(abyte0, this._client);
         break;
       case 91:
         new C_BoardWrite(abyte0, this._client);
         break;
       case 208:
         new C_ItemUSe(abyte0, this._client);
         new C_ItemUSe2(abyte0, this._client);
         break;
       case 42:
         new C_CreateParty(abyte0, this._client);
         break;
       case 200:
         new C_EnterPortal(abyte0, this._client);
         break;
       case 188:
         new C_Amount(abyte0, this._client);
         break;
       case 233:
         new C_FixWeaponList(abyte0, this._client);
         break;

       case 228:
         new C_SelectList(abyte0, this._client);
         break;
       case 179:
         new C_ExitGhost(abyte0, this._client);
         break;
       case 104:
       case 164:
         new C_CallPlayer(abyte0, this._client);
         break;
       case 146:
         new C_FishClick(abyte0, this._client);
         break;
       case 45:
         new C_SelectTarget(abyte0, this._client);
         break;
       case 160:
         new C_PetMenu(abyte0, this._client);
         break;
       case 37:
         new C_UsePetItem(abyte0, this._client);
         break;
       case 147:
         new C_Teleport(abyte0, this._client);
         break;
       case 10:
         new C_Rank(abyte0, this._client);
         break;
       case 170:
         new C_Chat(abyte0, this._client);
         break;
       case 120:
         new C_ChatParty(abyte0, this._client);
         break;
       case 118:
         new C_Fight(abyte0, this._client);
         break;



       case 95:
         new C_MailBox(abyte0, this._client);
         break;
       case 232:
         new C_ReturnStaus(abyte0, this._client);
         break;
       case 71:
         new C_WhPw(abyte0, this._client);
         break;
       case 125:
         new C_Horun(abyte0, this._client);
         break;
       case 210:
         new C_HorunOK(abyte0, this._client);
         break;
       case 55:
         new C_Report(abyte0, this._client);
         break;
       case 171:
         new C_SecurityStatus(abyte0, this._client);
         break;
       case 63:
         new C_SecurityStatusSet(abyte0, this._client);
         break;
     }
   }



   public String DataToPacket(byte[] data, int len) {
     StringBuffer result = new StringBuffer();
     int counter = 0;
     for (int i = 0; i < len; i++) {
       if (counter % 16 == 0) {
         result.append(HexToDex(i, 4) + ": ");
       }
       result.append(HexToDex(data[i] & 0xFF, 2) + " ");
       counter++;
       if (counter == 16) {
         result.append("   ");
         int charpoint = i - 15;
         for (int a = 0; a < 16; a++) {
           int t1 = data[charpoint++];
           if (t1 > 31 && t1 < 128) {
             result.append((char)t1);
           } else {
             result.append('.');
           }
         }
         result.append("\n"); counter = 0;
       }
     }
     int rest = data.length % 16;
     if (rest > 0) {
       for (int j = 0; j < 17 - rest; j++) {
         result.append("   ");
       }
       int charpoint = data.length - rest;
       for (int a = 0; a < rest; a++) {
         int t1 = data[charpoint++];
         if (t1 > 31 && t1 < 128) {
           result.append((char)t1);
         } else {
           result.append('.');
         }
       }
       result.append("\n");
     }
     return result.toString();
   }

   private String HexToDex(int data, int digits) {
     String number = Integer.toHexString(data);
     for (int i = number.length(); i < digits; ) { number = "0" + number; i++; }
      return number;
   }
 }


