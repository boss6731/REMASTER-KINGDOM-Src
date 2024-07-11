 package l1j.server.server.command.executor;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.util.Random;
 import java.util.StringTokenizer;
 import l1j.server.Config;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.IdFactory;
 import l1j.server.server.datatables.CharacterTable;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_DoActionShop;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.utils.SQLUtil;

 public class L1AdShop
   implements L1CommandExecutor {
   private static Random _random = new Random(System.nanoTime());


   private static final int[] MALE_LIST = new int[] { 0, 20553, 138, 20278, 2786, 6658, 6671, 20567, 18520, 19296 };
   private static final int[] FEMALE_LIST = new int[] { 1, 48, 37, 20279, 2796, 6661, 6650, 20577, 18499, 19299 };



   public static L1CommandExecutor getInstance() {
     return new L1AdShop();
   }


   public void execute(L1PcInstance pc, String cmdName, String arg) {
     try {
       StringTokenizer stringtokenizer = new StringTokenizer(arg);
       String name = stringtokenizer.nextToken();

       if (CharacterTable.getInstance().isContainNameList(name) || L1World.getInstance().getPlayer(name) != null) {
         pc.sendPackets((ServerBasePacket)new S_SystemMessage("這是一個已經存在的角色名稱。"));

         return;
       }
       int sex = _random.nextInt(1);
       int type = _random.nextInt(MALE_LIST.length);
       int AccountName = 1;

       createAdShop(pc.getAccountName(), name, sex, type, pc.getX(), pc.getY(), pc.getHeading(), pc.getMapId());

       Connection con = null;
       PreparedStatement pstm = null;

       try {
         con = L1DatabaseFactory.getInstance().getConnection();
         pstm = con.prepareStatement("INSERT INTO adShop SET account = ?, name = ?, sex = ?, type = ?, x = ?, y = ?, heading = ?, map_id = ?");
         pstm.setInt(1, AccountName);
         pstm.setString(2, name);
         pstm.setInt(3, sex);
         pstm.setInt(4, type);
         pstm.setInt(5, pc.getX());
         pstm.setInt(6, pc.getY());
         pstm.setInt(7, pc.getHeading());
         pstm.setInt(8, pc.getMapId());
         pstm.execute();
       }
       catch (Exception e) {
         e.printStackTrace();
       } finally {
         SQLUtil.close(pstm);
         SQLUtil.close(con);
       }
     } catch (Exception e) {
       pc.sendPackets((ServerBasePacket)new S_SystemMessage("請輸入 .擺攤【角色名稱】開始營業。"));
     }
   }

   public static void createAdShop(String account, String name, int sex, int type, int x, int y, int heading, int mapId) {
     if (CharacterTable.getInstance().isContainNameList(name) || L1World.getInstance().getPlayer(name) != null) {
       return;
     }

     L1PcInstance newPc = new L1PcInstance();
     newPc.setAccountName(account);
     newPc.setId(IdFactory.getInstance().nextId());
     newPc.setName(name);
     newPc.setHighLevel(1);
     newPc.set_exp(0L);
     newPc.addBaseMaxHp((short)2000);
     newPc.setCurrentHp(2000);
     newPc.setDead(false);
     newPc.setStatus(0);
     newPc.addBaseMaxMp(2);
     newPc.setCurrentMp(2);
     newPc.getAbility().setBaseStr(16);
     newPc.getAbility().setStr(16);
     newPc.getAbility().setBaseCon(16);
     newPc.getAbility().setCon(16);
     newPc.getAbility().setBaseDex(11);
     newPc.getAbility().setDex(11);
     newPc.getAbility().setBaseCha(13);
     newPc.getAbility().setCha(13);
     newPc.getAbility().setBaseInt(12);
     newPc.getAbility().setInt(12);
     newPc.getAbility().setBaseWis(11);
     newPc.getAbility().setWis(11);

     int klass = 0;
     if (sex == 0) {
       klass = MALE_LIST[type];
     } else {
       klass = FEMALE_LIST[type];
     }







     newPc.setCurrentWeapon(0);
     newPc.setClassId(klass);
     newPc.setCurrentSprite(klass);
     newPc.set_sex(sex);
     newPc.setType(type);
     newPc.setHeading(heading);
     newPc.setX(x);
     newPc.setY(y);
     newPc.setMap((short)mapId);
     newPc.set_food(39);
     newPc.setLawful(0);
     newPc.setTitle("");
     newPc.setClanid(0);
     newPc.setClanname("");
     newPc.setClanRank(0);
     newPc.setElixirStats(0);
     newPc.setElfAttr(0);
     newPc.setGlory_Earth_Attr(0);
     newPc.set_PKcount(0);
     newPc.set_exp_res(0L);
     newPc.setPartnerId(0);
     newPc.setAccessLevel((short)0);
     newPc.setGm(false);
     newPc.setMonitor(false);
     newPc.setOnlineStatus(1);
     newPc.setHomeTownId(0);
     newPc.setContribution(0);
     newPc.setHellTime(0);
     newPc.setBanned(false);
     newPc.setKarma(0);
     newPc.setReturnStat(0L);
     newPc.refresh();
     newPc.setMoveSpeed(0);
     newPc.setBraveSpeed(0);
     newPc.setGmInvis(false);
     L1World.getInstance().storeObject((L1Object)newPc);
     L1World.getInstance().addVisibleObject((L1Object)newPc);
     newPc.setNetConnection(null);
     newPc.startObjectAutoUpdate();

     byte[] chat = Config.Message.PrivateShopChat.getBytes();

     newPc.setShopChat(chat);
     newPc.setPrivateShop(true);
     newPc.sendPackets((ServerBasePacket)new S_DoActionShop(newPc.getId(), 70, chat));
     newPc.broadcastPacket((ServerBasePacket)new S_DoActionShop(newPc.getId(), 70, chat));
   }
 }


