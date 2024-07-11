 package l1j.server.server.utils;

 import java.io.File;
 import java.io.FileInputStream;
 import java.io.InputStream;
 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.Timestamp;
 import java.text.SimpleDateFormat;
 import java.util.ArrayDeque;
 import java.util.ArrayList;
 import java.util.Date;
 import java.util.Properties;
 import java.util.Random;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.MJBotSystem.MJBotLocation;
 import l1j.server.MJPassiveSkill.MJPassiveID;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.model.map.L1Map;
 import l1j.server.server.model.map.L1WorldMap;
 import l1j.server.server.serverpackets.S_OnlyEffect;
 import l1j.server.server.serverpackets.S_Paralysis;
 import l1j.server.server.serverpackets.ServerBasePacket;











 public class MJCommons
 {
   private static final byte[] PK_T_LOG = new byte[] { 77, 93, 107, 75, 59, 26 };

   public static Date to_date(Timestamp ts) {
     Date date = new Date();
     date.setTime(ts.getTime());
     return date;
   }

   public static String to_string(Timestamp ts) {
     Date date = to_date(ts);
     return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
   }

   public static void encode_xor(byte[] source_array, byte[] key_array, int start_index, int size) {
     int key_size = key_array.length;
     for (int i = start_index; i < size; i++)
       source_array[i] = (byte)(source_array[i] ^ key_array[i % key_size]);
   }

   public static void PK_T_LOG(byte[] array) {
     encode_xor(array, PK_T_LOG, 0, array.length);
   }

   public static void zigzag(byte[] array, int start_index) {
     int size = array.length - start_index;
     if (size % 2 == 1) {
       size--;
     }
     for (int i = start_index; i < size; i += 2) {
       int flow_index = i + 1;
       byte b = array[i];
       array[i] = array[flow_index];
       array[flow_index] = b;
     }
   }

   public static void reverse(byte[] array, int start_index) {
     int lo = start_index;
     int hi = array.length - 1;
     while (lo < hi) {
       byte b = array[lo];
       array[lo++] = array[hi];
       array[hi--] = b;
     }
   }

   public static int[] parseToIntArrange(String s, String tok) {
     if (s == null || s.equals("")) {
       return null;
     }
     String[] arr = s.split(tok);
     int size = arr.length;
     int[] result = new int[size];
     for (int i = size - 1; i >= 0; i--) {
       try {
         result[i] = Integer.parseInt(arr[i]);
       } catch (Exception e) {
         break;
       }
     }
     return result;
   }

   public static double[] parseToDoubleArrange(String s, String tok) {
     if (s == null || s.equals("")) {
       return null;
     }
     String[] arr = s.split(tok);
     int size = arr.length;
     double[] result = new double[size];
     for (int i = size - 1; i >= 0; i--) {
       try {
         result[i] = Double.parseDouble(arr[i]);
       } catch (Exception e) {
         break;
       }
     }
     return result;
   }

   public static ArrayList<Integer> parseToIntArray(String s, String tok) {
     if (s == null || s.equals("")) {
       return null;
     }
     String[] arr = s.split(tok);
     int size = arr.length;
     ArrayList<Integer> list = new ArrayList<>(size);
     for (int i = 0; i < size; i++) {
       try {
         if (arr[i].startsWith("0x"))
         { list.add(Integer.valueOf(Integer.parseInt(arr[i].replaceFirst("^0x", ""), 16))); }
         else
         { list.add(Integer.valueOf(Integer.parseInt(arr[i]))); }
       } catch (Exception e) {
         break;
       }
     }
     return list;
   }

   public static ArrayList<Integer> parseToIntArray(String s) {
     return parseToIntArray(s, " ");
   }

   public static ArrayDeque<Integer> parseToIntQ(String s) {
     return parseToIntQ(s, " ");
   }

   public static ArrayDeque<Integer> parseToIntQ(String s, String tok) {
     if (s == null || s.equals("")) {
       return null;
     }
     String[] arr = s.split(tok);
     int size = arr.length;
     ArrayDeque<Integer> argsQ = new ArrayDeque<>(size);
     for (int i = 0; i < size; i++) {
       try {
         argsQ.offer(Integer.valueOf(Integer.parseInt(arr[i])));
       } catch (Exception e) {
         break;
       }
     }
     return argsQ;
   }

   public static Integer parseToFirstInt(String s) {
     String[] arr = s.split(",");
     try {
       return Integer.valueOf(Integer.parseInt(arr[0]));
     } catch (Exception exception) {


       return Integer.valueOf(0);
     }
   }
   public static final ServerBasePacket on_paralysis = (ServerBasePacket)new S_Paralysis(6, true);
   public static final ServerBasePacket off_paralysis = (ServerBasePacket)new S_Paralysis(6, false);

   protected static Random _rnd = new Random(System.nanoTime());

   public static int MCS_HITRATE_MAX;

   public static int MCS_HITRATE_MIN;

   public static double MCS_DEFENCE_ATTR_WEIGHT;

   public static double MCS_STSSPELL_STATADDER;

   public static double MCS_DYCSPELL_SPADDER;
   public static double MCS_DYCSPELL_INTADDER;
   public static double MCS_KEYLINK_PCPC_DMG_SPADDER;
   public static double MCS_KEYLINK_PCPC_DMG_INTADDER;
   public static double MCS_KEYLINK_PCNPC_DMG_SPADDER;
   public static double MCS_KEYLINK_PCNPC_DMG_INTADDER;
   private static double[] _mr_dmgDef_table;
   private static double[] _mr_ratioDef_table;

   public static void load() {
     String column = null;
     try {
       Properties settings = new Properties();
       InputStream is = new FileInputStream(new File("./config/mjcommons.properties"));
       settings.load(is);
       is.close();

       column = "HitRateMax";
       MCS_HITRATE_MAX = Integer.parseInt(settings.getProperty(column, "90"));

       column = "HitRateMin";
       MCS_HITRATE_MIN = Integer.parseInt(settings.getProperty(column, "5"));

       column = "MagicDefenceAttrWeight";
       MCS_DEFENCE_ATTR_WEIGHT = Double.parseDouble(settings.getProperty(column, "1")) / 100.0D;

       column = "StaticSpellStatAdder";
       MCS_STSSPELL_STATADDER = Double.parseDouble(settings.getProperty(column, "70")) / 100.0D;

       column = "DynamicSpellAdder";
       MCS_DYCSPELL_SPADDER = Double.parseDouble(settings.getProperty(column, "15")) / 100.0D;

       column = "DynamicIntAdder";
       MCS_DYCSPELL_INTADDER = Double.parseDouble(settings.getProperty(column, "150")) / 100.0D;

       column = "KeylinkPcPcSpellAdder";
       MCS_KEYLINK_PCPC_DMG_SPADDER = Double.parseDouble(settings.getProperty(column, "15"));

       column = "KeylinkPcPcIntAdder";
       MCS_KEYLINK_PCPC_DMG_INTADDER = Double.parseDouble(settings.getProperty(column, "150"));

       column = "KeylinkPcNpcSpellAdder";
       MCS_KEYLINK_PCNPC_DMG_SPADDER = Double.parseDouble(settings.getProperty(column, "1"));

       column = "KeylinkPcNpcIntAdder";
       MCS_KEYLINK_PCNPC_DMG_INTADDER = Double.parseDouble(settings.getProperty(column, "1"));

       loadSpellDef();
     } catch (Exception e) {
       e.printStackTrace();
     }
   }

   private static void loadSpellDef() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("select * from tb_mjspell_def");
       rs = pstm.executeQuery();
       int rows = SQLUtil.calcRows(rs);
       _mr_dmgDef_table = new double[rows];
       _mr_ratioDef_table = new double[rows];
       while (rs.next()) {
         int idx = rs.getInt("id");
         _mr_dmgDef_table[idx] = rs.getDouble("def_dmg") / 100.0D;
         _mr_ratioDef_table[idx] = rs.getDouble("def_ratio") / 100.0D;
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
   }

   public static boolean isDistance(int x, int y, int m, int tx, int ty, int tm, int loc) {
     if (m != tm) return false;
     return (loc >= getDistance(x, y, tx, ty));
   }

   public static int getDistance(int x, int y, int tx, int ty) {
     long dx = (tx - x);
     long dy = (ty - y);
     return (int)Math.sqrt((dx * dx + dy * dy));
   }

   public static final int[][] _headings = new int[][] { { 7, 0, 1 }, { 6, 1, 2 }, { 5, 4, 3 } };




   public static final int[] _headingReverseLines = new int[] { 3, 4, 5, 6, 7, 0, 1, 2 };


   public static final int[] _headingLines = new int[] { 7, 0, 1, 2, 3, 4, 5, 6 };


   public static final int[] _headingWideLines = new int[] { 6, 7, 0, 1, 2, 3, 4, 5 };



   public static final byte[] HEADING_TABLE_X = new byte[] { 0, 1, 1, 1, 0, -1, -1, -1 };
   public static final byte[] HEADING_TABLE_Y = new byte[] { -1, -1, 0, 1, 1, 1, 0, -1 };
   public static int calcheading(int myx, int myy, int tx, int ty) {
     return _headings[(ty > myy) ? 2 : ((ty < myy) ? 0 : 1)][(tx > myx) ? 2 : ((tx < myx) ? 0 : 1)];
   }

   public static int getReverseHeading(int h) {
     return _headingReverseLines[h];
   }

   public static boolean isReverseHeading(int myx, int myy, int tx, int ty, int h) {
     int th = calcheading(myx, myy, tx, ty);
     for (int i = 0; i < 3; i++) {
       if (th == _headingReverseLines[(h + i) % 8])
         return true;
     }
     return false;
   }

   public static int getSpaceHeadingAndCombatZone(L1Character c) {
     L1Map map = c.getMap();
     for (int h = 0; h < 8; ) {
       int cx = c.getX() + HEADING_TABLE_X[h];
       int cy = c.getY() + HEADING_TABLE_Y[h];
       if (!map.isCombatZone(cx, cy) || !map.isPassable(cx, cy)) {
         h++; continue;
       }
       return h;
     }
     return -1;
   }

   public static boolean isPassableLine(int sx, int sy, int dx, int dy, short mid) {
     if (sx == dx && sy == dy) {
       return true;
     }
     L1Map map = L1WorldMap.getInstance().getMap(mid);
     int cx = sx;
     int cy = sy;

     while (true) {
       int h = calcheading(cx, cy, dx, dy);
       if (!map.isArrowPassable(cx, cy, h)) {
         return false;
       }
       cx += HEADING_TABLE_X[h];
       cy += HEADING_TABLE_Y[h];
       if (cx == dx && cy == dy)
         return true;
     }
   }
   public static int checkObject(int x, int y, short m, int d) {
     L1Map map = L1WorldMap.getInstance().getMap(m);

     for (int i = 0; i < 3; i++) {
       int h = _headingLines[(i + d) % 8];
       if (map.isPassable(x, y, h))
         return h;
     }
     return -1;
   }

   public static int checkPassable(int x, int y, short m) {
     L1Map map = L1WorldMap.getInstance().getMap(m);
     for (int i = 0; i < 8; ) {
       int h = _headingLines[i];
       int cx = x + HEADING_TABLE_X[h];
       int cy = y + HEADING_TABLE_Y[h];
       if (!map.isPassable(cx, cy)) {
         i++; continue;
       }
       return h;
     }
     return -1;
   }

   public static int checkPassable(int x, int y, short m, int d) {
     L1Map map = L1WorldMap.getInstance().getMap(m);
     for (int i = 0; i < 3; ) {
       int h = _headingLines[(i + d) % 8];
       int cx = x + HEADING_TABLE_X[h];
       int cy = y + HEADING_TABLE_Y[h];
       if (!map.isPassable(cx, cy)) {
         i++; continue;
       }
       return h;
     }
     return -1;
   }

   public static int checkAroundPassable(L1PcInstance body, int d) {
     int h = _headingLines[d];
     if (!isVisibleObjects((L1Character)body, h)) {
       return h;
     }
     h = _headingLines[(d + 2) % 8];
     if (!isVisibleObjects((L1Character)body, h)) {
       return h;
     }
     return -1;
   }

   public static int checkReverseAroundPassable(L1Character body, int d) {
     int h = _headingLines[d];
     if (!isVisibleObjects(body, h)) {
       return h;
     }
     h = _headingLines[(d + 2) % 8];
     if (!isVisibleObjects(body, h))
       return h;
     return -1;
   }

   public static int checkAroundWidePassable(L1Character body, int d) {
     if (_rnd.nextBoolean()) {
       int h = _headingWideLines[(d + 1) % 8];
       if (!isVisibleObjects(body, h))
         return h;
       h = _headingWideLines[(d + 3) % 8];
       if (!isVisibleObjects(body, h))
         return h;
       h = _headingWideLines[d];
       if (!isVisibleObjects(body, h))
         return h;
       h = _headingWideLines[(d + 4) % 8];
       if (!isVisibleObjects(body, h))
         return h;
     } else {
       int h = _headingWideLines[(d + 3) % 8];
       if (!isVisibleObjects(body, h))
         return h;
       h = _headingWideLines[(d + 1) % 8];
       if (!isVisibleObjects(body, h))
         return h;
       h = _headingWideLines[(d + 4) % 8];
       if (!isVisibleObjects(body, h))
         return h;
       h = _headingWideLines[d];
       if (!isVisibleObjects(body, h)) {
         return h;
       }
     }
     if (_rnd.nextInt(100) < 5)
       return checkReverseAroundPassable(body, d);
     return -1;
   }

   public static boolean isAroundDirection(L1Character owner, L1Character target) {
     int oh = owner.getHeading();
     int th = calcheading(owner.getX(), owner.getY(), target.getX(), target.getY());
     return (oh == th || _headingLines[oh] == th || _headingLines[(oh + 2) % 8] == th);
   }

   public static boolean isWideAroundDirection(L1Character owner, L1Character target) {
     int oh = owner.getHeading();
     int th = calcheading(owner.getX(), owner.getY(), target.getX(), target.getY());
     return (oh == th || _headingWideLines[oh] == th || _headingWideLines[(oh + 1) % 8] == th || _headingWideLines[(oh + 2) % 8] == th || _headingWideLines[(oh + 3) % 8] == th || _headingWideLines[(oh + 4) % 8] == th);
   }

   public static boolean isVisibleObjects(L1Character body, int h) {
     L1Map map = L1WorldMap.getInstance().getMap(body.getMapId());
     int cx = body.getX() + HEADING_TABLE_X[h];
     int cy = body.getY() + HEADING_TABLE_Y[h];
     return !map.isPassable(cx, cy);
   }

   public static boolean isPassablePosition(MJBotLocation loc) {
     L1Map map = L1WorldMap.getInstance().getMap((short)loc.map);
     if (!map.isPassable(loc.x, loc.y)) {
       return false;
     }
     if (L1World.getInstance().isVisibleNpc(loc.x, loc.y, loc.map))
       return false;
     return true;
   }

   public static boolean isCounterMagic(L1Character c) {
     if (c.hasSkillEffect(31)) {
       c.removeSkillEffect(31);
       S_OnlyEffect eff = new S_OnlyEffect(c.getId(), 10702);
       c.sendPackets((ServerBasePacket)eff, false);
       c.broadcastPacket((ServerBasePacket)eff);
       return true;
     }
     return false;
   }

   public boolean isRock(L1PcInstance pc) {
     if (pc.hasSkillEffect(78)) {
       return true;
     }
     if (pc.hasSkillEffect(157) || pc
       .hasSkillEffect(70705)) {
       return true;
     }
     return false;
   }


   public static boolean isMagicSuccess(L1Character attacker, L1Character target, int default_ratio, int defenseMr) {
     int hit = attacker.getAbility().getMagicBonus() + attacker.getAbility().getMagicLevel();
     hit += attacker.getAbility().getTotalInt() + default_ratio;
     int mr = defenseMr;
     double pMr = 0.0D;

     int mridx = mr / 10 - 1;
     if (mridx < 0) {
       mridx = 0;
     } else if (mridx > _mr_ratioDef_table.length - 1) {
       mridx = _mr_ratioDef_table.length - 1;
     }
     if (mr != 0) {
       pMr = _mr_ratioDef_table[mridx];
     }
     hit -= (int)(hit * pMr);
     hit += attacker.getLevel() - target.getLevel();

     if (hit >= MCS_HITRATE_MAX) {
       hit = MCS_HITRATE_MAX;
     } else if (hit <= MCS_HITRATE_MIN) {
       hit = MCS_HITRATE_MIN;
     }
     if (_rnd.nextInt(100) < hit)
       return true;
     return false;
   }

   public static double getAttrDamage(double attrDamage, int attrDefense) {
     if (attrDefense == 0) {
       return attrDamage;
     }
     double defense = attrDefense * MCS_DEFENCE_ATTR_WEIGHT;
     return attrDamage - defense * attrDamage;
   }

   public static int getAttrDamage(int attrDamage, int attrDefense) {
     if (attrDefense == 0) {
       return attrDamage;
     }
     double defense = attrDefense * MCS_DEFENCE_ATTR_WEIGHT;
     return (int)(attrDamage - defense * attrDamage);
   }

   public static int getMagicDamage(double mdmg, int mdefen) {
     if (mdefen == 0) {
       return (int)mdmg;
     }
     int mridx = mdefen / 10 - 1;
     if (mridx < 0) {
       mridx = 0;
     } else if (mridx > _mr_dmgDef_table.length - 1) {
       mridx = _mr_dmgDef_table.length - 1;
     }
     double result = mdmg - mdmg * _mr_dmgDef_table[mridx];
     return (int)result;
   }

   public static int getMagicDamage(int mdmg, int mdefen) {
     return getMagicDamage(mdmg, mdefen);
   }

   public static boolean isMagicSuccess(L1Character attacker, L1Character target, int default_ratio) {
     return isMagicSuccess(attacker, target, default_ratio, target.getResistance().getMrAfterEraseRemove());
   }

   public static int getMagicDamage(L1Character attacker, L1Character target, int pure) {
     double dmg = pure * attacker.getAbility().getSp() * MCS_DYCSPELL_SPADDER;
     dmg += attacker.getAbility().getMagicBonus();
     dmg += attacker.getAbility().getTotalInt() * MCS_DYCSPELL_INTADDER;
     dmg += CalcStat.calcMagicDmg(attacker.getAbility().getTotalInt());
     dmg = getMagicDamage((int)dmg, target.getResistance().getMrAfterEraseRemove());
     return (int)dmg;
   }

   public static int getKeylinkPcPcDamage(L1Character attacker, L1Character target, int pure) {
     double dmg = pure;
     dmg += attacker.getAbility().getMagicBonus();
     dmg += attacker.getAbility().getSp() * MCS_KEYLINK_PCPC_DMG_SPADDER;
     dmg += attacker.getAbility().getTotalInt() * MCS_KEYLINK_PCPC_DMG_INTADDER;
     dmg += CalcStat.calcMagicDmg(attacker.getAbility().getTotalInt());
     dmg -= dmg * target.getResistance().getMrAfterEraseRemove() * 0.2D / 100.0D;
     return (int)dmg;
   }

   public static int getKeylinkPcNpcDamage(L1Character attacker, L1Character target, int pure) {
     double dmg = pure;
     dmg += attacker.getAbility().getMagicBonus();
     dmg += attacker.getAbility().getSp() * MCS_KEYLINK_PCNPC_DMG_SPADDER;
     dmg += attacker.getAbility().getTotalInt() * MCS_KEYLINK_PCNPC_DMG_INTADDER;
     dmg += CalcStat.calcMagicDmg(attacker.getAbility().getTotalInt());
     if (target.getResistance() != null)
       dmg -= dmg * target.getResistance().getMrAfterEraseRemove() * 0.2D / 100.0D;
     return (int)dmg;
   }

   public static int getMagicDamage(L1Character attacker, L1Character target, int min, int max) {
     int result = 0;
     try {
       int intel = attacker.getAbility().getTotalInt();
       int pure = _rnd.nextInt(max - min) + min;
       double dmg = pure + intel * MCS_STSSPELL_STATADDER;
       dmg += CalcStat.calcMagicDmg(attacker.getAbility().getTotalInt());
       result = getMagicDamage(dmg, target.getResistance().getMrAfterEraseRemove());
     } catch (Exception e) {
       e.printStackTrace();
       System.out.println(String.format("[tb_itemskill_model 設定值錯誤] %s -> %s MJCommons.getMagicDamage()", new Object[] { attacker.getName(), target.getName() }));
     }
     return result;
   }

   public static int calcDamage(L1Character attacker, L1PcInstance pc, int min, int max, boolean isMagic) {
     double imm = pc.getImmuneReduction();

     if (imm != 0.0D) {
       min -= (int)(min * imm);
       max -= (int)(max * imm);
     }

     int dmg = 0;
     if (isMagic) {
       dmg = getMagicDamage(attacker, (L1Character)pc, min, max);
       if (pc.hasSkillEffect(7672) || pc.hasSkillEffect(7678) || pc
         .hasSkillEffect(7676) || pc.hasSkillEffect(7675) || pc.hasSkillEffect(7688)) {
         int MaanMagicCri = _rnd.nextInt(100) + 1;
         if (MaanMagicCri <= 35)
           dmg /= 2;
       }
     } else {
       dmg = _rnd.nextInt(max - min) + min;
     }

     dmg -= pc.getDamageReductionByArmor();

     if (pc.isPassive(MJPassiveID.MAJESTY.toInt())) {
       int targetPcLvl = pc.getLevel();
       if (targetPcLvl < 80)
         targetPcLvl = 80;
       dmg -= (targetPcLvl - 80) / 2 + 1;
     }

     if (pc.hasSkillEffect(211)) {
       int targetPcLvl = pc.getLevel();
       if (targetPcLvl < 80)
         targetPcLvl = 80;
       dmg -= (targetPcLvl - 80) / 4 + 1;
     }

     if (pc.hasSkillEffect(219)) {
       dmg += dmg / 5;
     }

     if (pc.isPassive(MJPassiveID.DRAGON_SKIN_PASS.toInt()))
       dmg -= 3;
     return dmg;
   }


   public static boolean isInArea(L1Object obj, int x1, int y1, int x2, int y2, int leftRad, int rightRad, int mid) {
     if (x1 < x2) {
       return isInArea(obj, x1 - leftRad, y1 - leftRad, x2 + rightRad, y2 + rightRad, (short)mid);
     }
     return isInArea(obj, x2 - leftRad, y2 - leftRad, x1 + rightRad, y1 + rightRad, (short)mid);
   }

   public static boolean isInArea(L1Object obj, int x1, int y1, int x2, int y2, int rad, int mid) {
     if (x1 < x2) {
       return isInArea(obj, x1 - rad, y1 - rad, x2 + rad, y2 + rad, (short)mid);
     }
     return isInArea(obj, x2 - rad, y2 - rad, x1 + rad, y1 + rad, (short)mid);
   }

   public static boolean isInArea(L1Object obj, int x1, int y1, int x2, int y2, int mid) {
     if (x1 < x2) {
       return isInArea(obj, x1, y1, x2, y2, (short)mid);
     }
     return isInArea(obj, x2, y2, x1, y1, (short)mid);
   }

   public static boolean isInArea(L1Object obj, int x, int y, int rad, short mid) {
     return isInArea(obj, x - rad, y - rad, x + rad, y + rad, mid);
   }

   public static boolean isInArea(L1Object obj, int left, int top, int right, int bottom, short mid) {
     return (obj.getX() >= left && obj
       .getY() >= top && obj
       .getX() <= right && obj
       .getY() <= bottom && obj
       .getMapId() == mid);
   }

   public static <E> E getRandomListElement(ArrayList<E> list) {
     return list.get(_rnd.nextInt(list.size()));
   }

   public static ArrayList<L1Object> createKnownPlayers(L1PcInstance body) {
     return new ArrayList<>(body.getKnownPlayers());
   }

   public static ArrayList<L1Object> createKnownList(L1PcInstance body) {
     return new ArrayList<>(body.getKnownObjects());
   }

   public static ArrayDeque<L1Object> createKnownQ(L1PcInstance body) {
     return new ArrayDeque<>(body.getKnownObjects());
   }

   public static boolean isNonAction(L1Character c) {
     return (c.hasSkillEffect(87) || c
       .hasSkillEffect(51006) || c
       .hasSkillEffect(5003) || c
       .hasSkillEffect(123) || c
       .hasSkillEffect(208) || c.hasSkillEffect(367) || c
       .hasSkillEffect(242) || c.hasSkillEffect(100242) || c
       .hasSkillEffect(30081) || c
       .hasSkillEffect(40007) || c
       .hasSkillEffect(30010) || c
       .hasSkillEffect(30005) || c
       .hasSkillEffect(30006) || c
       .hasSkillEffect(707054) || c
       .hasSkillEffect(707041) || c
       .hasSkillEffect(707119) || c
       .hasSkillEffect(707099) || c
       .hasSkillEffect(22031) || c
       .hasSkillEffect(22055) || c
       .hasSkillEffect(707159) || c
       .hasSkillEffect(707152) || c
       .hasSkillEffect(707113) || c
       .hasSkillEffect(707056) || c
       .hasSkillEffect(157) || c
       .hasSkillEffect(70705) || c
       .hasSkillEffect(30004) || c
       .hasSkillEffect(77) || c
       .hasSkillEffect(30003) || c
       .hasSkillEffect(5027) || c
       .hasSkillEffect(230) || c
       .hasSkillEffect(5037) || c
       .hasSkillEffect(315) || c.hasSkillEffect(7320184) || c
       .hasSkillEffect(5056) || c.hasSkillEffect(350) || c
       .hasSkillEffect(995049) || c
       .isParalyzed() || c.isSleeped() || c.isLock());
   }

   public static boolean isUnbeatable(L1Character body) {
     return (body.hasSkillEffect(157) || body
       .hasSkillEffect(70705) || body
       .hasSkillEffect(30004) || body
       .hasSkillEffect(30003));
   }

   public static boolean isLock(L1Character body) {
     return (body.hasSkillEffect(157) || body
       .hasSkillEffect(51006) || body
       .hasSkillEffect(87) || body
       .hasSkillEffect(5003) || body
       .hasSkillEffect(123) || body
       .hasSkillEffect(242) || body
       .hasSkillEffect(70705) || body
       .hasSkillEffect(33) || body
       .hasSkillEffect(208) || body
       .hasSkillEffect(212) || body
       .hasSkillEffect(30004) || body
       .hasSkillEffect(5056) || body
       .hasSkillEffect(5003) || body
       .hasSkillEffect(77) || body
       .hasSkillEffect(30003));
   }

   public static boolean islockteleport(L1Character body) {
     return (body.hasSkillEffect(230) || body
       .hasSkillEffect(243) || body
       .hasSkillEffect(5027) || body
       .hasSkillEffect(157) || body
       .hasSkillEffect(87) || body
       .hasSkillEffect(5003) || body
       .hasSkillEffect(123) || body
       .hasSkillEffect(242) || body
       .hasSkillEffect(70705) || body
       .hasSkillEffect(33) || body
       .hasSkillEffect(208) || body
       .hasSkillEffect(212) || body
       .hasSkillEffect(30004) || body
       .hasSkillEffect(30003) || body
       .hasSkillEffect(30081) || body
       .hasSkillEffect(40007) || body
       .hasSkillEffect(30010) || body
       .hasSkillEffect(30005) || body
       .hasSkillEffect(30006) || body
       .hasSkillEffect(707054) || body
       .hasSkillEffect(707041) || body
       .hasSkillEffect(707099) || body
       .hasSkillEffect(22031) || body
       .hasSkillEffect(22055) || body
       .hasSkillEffect(707113) || body
       .hasSkillEffect(707056) || body
       .hasSkillEffect(77) || body
       .isParalyzed() || body.isSleeped() || body
       .hasSkillEffect(5002));
   }

   public static boolean isBlessed(L1Character body) {
     return body.hasSkillEffect(8000);
   }

   public static double calcHealing(L1Character c, double healing) {
     double rate = 0.0D;
     if (c.hasSkillEffect(230)) {
       int atlvl = c.Desperadolevel;
       int dflvl = c.getLevel();

       rate = 0.4D;
       if (atlvl > dflvl) {
         rate += (atlvl - dflvl) * 0.01D;
       }
     }
     if (c.hasSkillEffect(173)) {
       rate += 0.5D;
     }
     if (rate == 0.0D) {
       return healing;
     }
     double calc = healing - healing * rate;
     double min = healing * 0.1D;
     if (calc < min) { calc = min; }
     else if (calc > healing) { calc = healing; }

     return calc;
   }


   private static final int[] _classLindBlessingMp = new int[] { 9, 8, 16, 20, 16, 9, 20, 8, 8, 8 };

   public static int getClassLindBlessing(int classType) throws Exception {
     return _classLindBlessingMp[classType];
   }

   private static final int[] _chaotic_damage = new int[] { 1, 3, 5, 7 };



   public static int getChaoticDamage(int lawful) {
     if (lawful >= 0) {
       return 0;
     }
     return _chaotic_damage[lawful / -10000];
   }

   public static boolean isLetterOrDigitString(String s, int min, int max) {
     int len = s.length();
     if (len < min || len > max) {
       return false;
     }
     char[] chars = s.toCharArray();
     for (int i = chars.length - 1; i >= 0; i--) {
       if (!Character.isLetterOrDigit(chars[i]))
         return false;
     }
     return true;
   }

   public static boolean isNullOrEmpty(String a) {
     return (a == null || a.isEmpty());
   }

   public static int[] parseIntArray(String s) {
     String[] arr = s.split(",");
     int[] result = new int[arr.length];
     for (int i = 0; i < arr.length; i++) {
       try {
         result[i] = Integer.parseInt(arr[i]);
       } catch (Exception exception) {}
     }
     return result;
   }
 }


