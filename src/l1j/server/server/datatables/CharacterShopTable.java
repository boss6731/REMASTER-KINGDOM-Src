 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import javolution.util.FastTable;
 import l1j.server.Base64;
 import l1j.server.Config;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.MJDShopSystem.MJDShopItem;
 import l1j.server.MJWarSystem.MJCastleWarBusiness;
 import l1j.server.server.model.Broadcaster;
 import l1j.server.server.model.Getback;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.model.map.L1Map;
 import l1j.server.server.model.map.L1WorldMap;
 import l1j.server.server.serverpackets.S_DoActionShop;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.utils.SQLUtil;

 public class CharacterShopTable
 {
   public static CharacterShopTable getInstance() {
     if (_instance == null) {
       _instance = new CharacterShopTable();
     }
     return _instance;
   }
   private static CharacterShopTable _instance;
   private CharacterShopTable() {
     load();
   }

   private L1PcInstance load2(String charName, byte[] shopTitle) {
     L1PcInstance pc = null;
     try {
       pc = L1PcInstance.load(charName);
       int currentHpAtLoad = pc.getCurrentHp();
       int currentMpAtLoad = pc.getCurrentMp();

       pc.setOnlineStatus(1);
       CharacterTable.updateOnlineStatus(pc);
       L1World.getInstance().storeObject((L1Object)pc);
       items(pc);
       L1Map map = L1WorldMap.getInstance().getMap(pc.getMapId());
       int tile = map.getTile(pc.getX(), pc.getY());
       if (Config.ServerAdSetting.GETBACKREST || !map.isInMap(pc.getX(), pc.getY()) || tile == 0 || tile == 4 || tile == 12) {
         int[] loc = Getback.GetBack_Location(pc, true);
         pc.setX(loc[0]);
         pc.setY(loc[1]);
         pc.setMap((short)loc[2]);
       }
       L1World.getInstance().addVisibleObject((L1Object)pc);
       pc.beginGameTimeCarrier();
       pc.sendVisualEffectAtLogin();

       pc.setSpeedHackCount(0);
       pc.getLight().turnOnOffLight();
       if (pc.getCurrentHp() > 0) {
         pc.setDead(false);
         pc.setStatus(0);
       } else {
         pc.setDead(true);
         pc.setStatus(8);
       }
       MJCastleWarBusiness.getInstance().viewNowCastleWarState(pc);
       if (pc.getClanid() != 0) {
         L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
         if (clan != null && (
           pc.getClanid() != clan.getClanId() ||
           !pc.getClanname().toLowerCase().equals(clan.getClanName().toLowerCase()))) {


           pc.setClanid(0);
           pc.setClanname("");
           pc.setClanRank(0);
           pc.save();
         }
       }

       if (currentHpAtLoad > pc.getCurrentHp()) {
         pc.setCurrentHp(currentHpAtLoad);
       }
       if (currentMpAtLoad > pc.getCurrentMp()) {
         pc.setCurrentMp(currentMpAtLoad);
       }
       pc.setNetConnection(null);

       pc.save();
       byte[] text = shopTitle;
       pc.setShopChat(text);
       pc.setPrivateShop(true);
       Broadcaster.broadcastPacket((L1Character)pc, (ServerBasePacket)new S_DoActionShop(pc.getId(), 70, text));
     }
     catch (Exception exception) {}

     return pc;
   }


   private void items(L1PcInstance pc) {
     CharacterTable.getInstance().restoreInventory(pc);
   }


   private void load() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     String name = null;
     FastTable<String> _charlist = new FastTable();

     int i = 0;
     try {
       System.out.print("CharacterShopTable Loading...");
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM character_shop_store");
       rs = pstm.executeQuery();
       while (rs.next()) {
         name = rs.getString("char_Name");
         if (!_charlist.contains(name)) {
           L1PcInstance l1PcInstance = load2(name, Base64.decode(rs.getString("shop_Title")));
           _charlist.add(name);
           if (l1PcInstance == null)
             continue;
           ShopListLoad(l1PcInstance);
           i++;
         }
       }
       deleteShop();
       System.out.println("OK! Count: " + i);
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       _charlist.clear();
       L1PcInstance pc = null;
       i = 0;
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }


   private void ShopListLoad(L1PcInstance pc) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     MJDShopItem ditem = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM character_shop WHERE objid=?");
       pstm.setInt(1, pc.getId());
       rs = pstm.executeQuery();
       while (rs.next()) {
         ditem = MJDShopItem.create(rs);
         if (ditem.isPurchase) {
           pc.addPurchasings(ditem); continue;
         }
         pc.addSellings(ditem);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public void deleteShop() {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("DELETE FROM character_shop");
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }
 }


