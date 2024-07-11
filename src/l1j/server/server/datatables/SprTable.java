 package l1j.server.server.datatables;

 import java.io.File;
 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.HashMap;
 import java.util.logging.Logger;
 import javax.xml.parsers.DocumentBuilderFactory;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.utils.SQLUtil;
 import l1j.server.server.utils.StringUtil;
 import org.w3c.dom.Document;
 import org.w3c.dom.Element;
 import org.w3c.dom.Node;
 import org.w3c.dom.NodeList;






 public class SprTable
 {
   private static Logger _log = Logger.getLogger(SprTable.class.getName());

   private static class Frame {
     private int framecount = 1200;
     private int framerate = 1200; }

   private static class Spr { private Spr() {}

     private final HashMap<Integer, Integer> frameCount = new HashMap<>();
     private final HashMap<Integer, Integer> actionSpeed = new HashMap<>();
     private final HashMap<Integer, Integer> moveSpeed = new HashMap<>();
     private final HashMap<Integer, Integer> attackSpeed = new HashMap<>();
     private final HashMap<Integer, Integer> damageSpeed = new HashMap<>();
     private int nodirSpellSpeed = 1200;
     private int dirSpellSpeed = 1200; }



   private static final HashMap<Integer, Spr> _dataMap = new HashMap<>();
   private static final HashMap<Integer, PolyFrameSprite> _frameMap = new HashMap<>();
   private static final double BASIC_MILLIS = 1000.0D;
   private static final SprTable _instance = new SprTable(); private static final String POLY_FRAME_RATE_PATH = "./data/xml/PolyFrameRate/PolyFrameRate.xml";
   private SprTable() {
     loadSprAction();
     loadPolyFrameRate();
   }
   private Integer[] _levelToIdx;
   public static SprTable getInstance() {
     return _instance;
   }



   public double getActionSpeed(int sprite, int action, int boundaryLevel) {
     try {
       Spr spr = _dataMap.get(Integer.valueOf(sprite));
       if (spr == null) return getActionSpeed(sprite, action);
       int frameCount = ((Integer)spr.frameCount.get(Integer.valueOf(action))).intValue();
       if (frameCount <= 0) return getActionSpeed(sprite, action);
       PolyFrameSprite polySprite = _frameMap.get(Integer.valueOf(sprite));
       if (polySprite == null) return getActionSpeed(sprite, action);
       PolyFrameAction polyAction = polySprite.actionList.get(Integer.valueOf(action));
       if (polyAction == null) return getActionSpeed(sprite, action);
       PolyFrameLevelRate levelRate = polyAction.levelRateList.get(Integer.valueOf(boundaryLevel));
       if (levelRate == null) return getActionSpeed(sprite, action);
       return calcActionSpeed(frameCount, levelRate.rate.doubleValue());
     } catch (Exception e) {
       return 0.0D;
     }
   }


   public int getActionSpeed(int sprite, int actid) {
     try {
       boolean secondWeapone = false;
       if (actid == 89) {
         actid = 12;
         secondWeapone = true;
       }
       if (_dataMap.containsKey(Integer.valueOf(sprite)) && (_dataMap.get(Integer.valueOf(sprite))).actionSpeed.containsKey(Integer.valueOf(actid))) {
         if (secondWeapone) return (int)(((Integer)(_dataMap.get(Integer.valueOf(sprite))).actionSpeed.get(Integer.valueOf(actid))).intValue() * 0.828D);
         return ((Integer)(_dataMap.get(Integer.valueOf(sprite))).actionSpeed.get(Integer.valueOf(actid))).intValue();
       }
     } catch (Exception e) {
       return 0;
     }
     return 0;
   }









   public int getAttackSpeed(int sprite, int actid) {
     try {
       boolean secondWeapone = false;
       if (actid == 89) {
         actid = 12;
         secondWeapone = true;
       }
       if (_dataMap.containsKey(Integer.valueOf(sprite))) {
         if ((_dataMap.get(Integer.valueOf(sprite))).attackSpeed.containsKey(Integer.valueOf(actid))) {
           if (secondWeapone) {
             return (int)(((Integer)(_dataMap.get(Integer.valueOf(sprite))).attackSpeed.get(Integer.valueOf(actid))).intValue() * 0.828D);
           }
           return ((Integer)(_dataMap.get(Integer.valueOf(sprite))).attackSpeed.get(Integer.valueOf(actid))).intValue();
         }  if (actid == 1) {
           return 0;
         }
         if ((_dataMap.get(Integer.valueOf(sprite))).attackSpeed.get(Integer.valueOf(1)) != null) {
           return ((Integer)(_dataMap.get(Integer.valueOf(sprite))).attackSpeed.get(Integer.valueOf(1))).intValue();
         }
         return 0;
       }

     } catch (Exception e) {
       return 0;
     }
     return 0;
   }

   public int getMoveSpeed(int sprite, int actid) {
     try {
       if (_dataMap.containsKey(Integer.valueOf(sprite))) {
         if ((_dataMap.get(Integer.valueOf(sprite))).moveSpeed.containsKey(Integer.valueOf(actid)))
           return ((Integer)(_dataMap.get(Integer.valueOf(sprite))).moveSpeed.get(Integer.valueOf(actid))).intValue();
         if ((_dataMap.get(Integer.valueOf(sprite))).moveSpeed.containsKey(Integer.valueOf(0))) {
           return ((Integer)(_dataMap.get(Integer.valueOf(sprite))).moveSpeed.get(Integer.valueOf(0))).intValue();
         }
         return 0;
       }
     } catch (Exception e) {
       return 0;
     }
     return 0;
   }

   public int getDirSpellSpeed(int sprite) {
     if (_dataMap.containsKey(Integer.valueOf(sprite))) return (_dataMap.get(Integer.valueOf(sprite))).dirSpellSpeed;
     return 0;
   }

   public int getNodirSpellSpeed(int sprite) {
     if (_dataMap.containsKey(Integer.valueOf(sprite))) return (_dataMap.get(Integer.valueOf(sprite))).nodirSpellSpeed;
     return 0;
   }

   public int getDamageSpeed(int sprite, int actid) {
     try {
       if (_dataMap.containsKey(Integer.valueOf(sprite)) && (_dataMap.get(Integer.valueOf(sprite))).damageSpeed.containsKey(Integer.valueOf(actid))) {
         return ((Integer)(_dataMap.get(Integer.valueOf(sprite))).damageSpeed.get(Integer.valueOf(actid))).intValue();
       }
     } catch (Exception e) {
       return 0;
     }
     return 0;
   }

   private static class PolyFrameSprite
   {
     public int sprite;
     public HashMap<Integer, SprTable.PolyFrameAction> actionList;

     private PolyFrameSprite() {}
   }

   private static class PolyFrameAction {
     public int action;
     public HashMap<Integer, SprTable.PolyFrameLevelRate> levelRateList;

     private PolyFrameAction() {}
   }

   private static class PolyFrameLevelRate {
     public int level;
     public Double rate;

     private PolyFrameLevelRate() {}
   }

   private int calcActionSpeed(double frameCount, double frameRate) {
     return (int)fromRPS(frameCount, calcRps(frameRate));
   }

   private double fromRPS(double frameCount, double rps) {
     return frameCount * rps;
   }

   private double calcRps(double basic_millis, double rate) {
     return basic_millis / rate;
   }

   private double calcRps(double frameRate) {
     return calcRps(1000.0D, frameRate);
   }







   public void loadSprAction() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     Spr spr = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM spr_action");
       rs = pstm.executeQuery();
       while (rs.next()) {
         int key = rs.getInt("spr_id");
         if (!_dataMap.containsKey(Integer.valueOf(key))) {
           spr = new Spr();
           _dataMap.put(Integer.valueOf(key), spr);
         } else {
           spr = _dataMap.get(Integer.valueOf(key));
         }

         int actid = rs.getInt("act_id");
         int frameCount = rs.getInt("framecount");
         int frameRate = rs.getInt("framerate");
         int speed = calcActionSpeed(frameCount, frameRate);

         switch (actid) {
           case 0:
           case 4:
           case 11:
           case 20:
           case 24:
           case 40:
           case 46:
           case 50:
           case 54:
           case 58:
           case 62:
           case 83:
           case 88:
             spr.moveSpeed.put(Integer.valueOf(actid), Integer.valueOf(speed));

           case 18:
             spr.dirSpellSpeed = speed;


           case 19:
             spr.nodirSpellSpeed = speed;





           case 1:
           case 5:
           case 12:
           case 21:
           case 25:
           case 30:
           case 31:
           case 41:
           case 47:
           case 51:
           case 55:
           case 59:
           case 63:
           case 84:
           case 89:
             spr.attackSpeed.put(Integer.valueOf(actid), Integer.valueOf(speed));
           case 2:
           case 6:
           case 13:
           case 26:
           case 42:
           case 48:
           case 52:
           case 56:
           case 60:
           case 85:
           case 123:
             spr.damageSpeed.put(Integer.valueOf(actid), Integer.valueOf(speed));
         }



       }
     } catch (SQLException e) {
         e.printStackTrace(); // 註解: 打印 SQL 異常堆棧信息
     } finally {
         SQLUtil.close(rs); // 註解: 關閉結果集 (ResultSet)
         SQLUtil.close(pstm); // 註解: 關閉預處理語句 (PreparedStatement)
         SQLUtil.close(con); // 註解: 關閉數據庫連接 (Connection)
     }
       _log.config("SPR 數據 " + _dataMap.size() + "條加載完成");
// 註解: SPR 數據 " + _dataMap.size() + " 條加載完成
   }






   private void loadPolyFrameRate() {
     try {
       Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("./data/xml/PolyFrameRate/PolyFrameRate.xml"));

       loadLevelInformation(doc);
       loadActionRates(doc);
       loadExceptionActionRates(doc);
     } catch (Exception e) {
       e.printStackTrace();
     }
   }

   private void settingFrameRate(Integer action, Integer sprite, Double[] rates) {
     PolyFrameSprite spr = _frameMap.get(sprite);
     if (spr == null) {
       spr = new PolyFrameSprite();
       spr.sprite = sprite.intValue();
       spr.actionList = new HashMap<>();
       _frameMap.put(Integer.valueOf(spr.sprite), spr);
     }

     PolyFrameAction ac = spr.actionList.get(action);
     if (ac == null) {
       ac = new PolyFrameAction();
       ac.action = action.intValue();
       ac.levelRateList = new HashMap<>();
       spr.actionList.put(Integer.valueOf(ac.action), ac);
     }

     for (int l = 0; l < this._levelToIdx.length; l++) {
       Integer lvl = this._levelToIdx[l];
       PolyFrameLevelRate lvlRate = ac.levelRateList.get(lvl);
       if (lvlRate == null) {
         lvlRate = new PolyFrameLevelRate();
         lvlRate.level = lvl.intValue();
         lvlRate.rate = rates[l];
         ac.levelRateList.put(Integer.valueOf(lvlRate.level), lvlRate);
       }
     }
   }



   private void loadLevelInformation(Document doc) {
     Element element = (Element)doc.getElementsByTagName("Level").item(0);
     this._levelToIdx = parsingInteger(element.getAttribute("range"), ",");
   }


   private void loadActionRates(Document doc) {
     Integer[] targets = parsingNodeList(doc.getElementsByTagName("Target"));
     NodeList nodes = ((Element)doc.getElementsByTagName("PolyFrameRate").item(0)).getChildNodes();

     for (int i = nodes.getLength() - 1; i >= 0; i--) {
       Node node = nodes.item(i);
       if (1 == node.getNodeType()) {


         Element element = (Element)node;
         Double[] rates = parsingDouble(element.getAttribute("rate"), ",");
         Integer[] actions = parsingInteger(element.getAttribute("action"), ",");

         for (Integer action : actions) {
           for (Integer sprite : targets) {
             settingFrameRate(action, sprite, rates);
           }
         }
       }
     }
   }

   private void loadExceptionActionRates(Document doc) {
     Integer[] exceptionSprite = parsingInteger(((Element)doc.getElementsByTagName("Sprite").item(0)).getAttribute("target"), ",");
     NodeList nodes = ((Element)doc.getElementsByTagName("PolyFrameException").item(0)).getChildNodes();
     for (int i = nodes.getLength() - 1; i >= 0; i--) {
       Node node = nodes.item(i);
       if (1 == node.getNodeType())
       {
         if (node.getNodeName().equalsIgnoreCase("Frame")) {


           Element element = (Element)node;
           Double[] rates = parsingDouble(element.getAttribute("rate"), ",");
           Integer[] actions = parsingInteger(element.getAttribute("action"), ",");

           for (Integer action : actions) {
             for (Integer sprite : exceptionSprite)
               settingFrameRate(action, sprite, rates);
           }
           break;
         }  }
     }
   }

   private Integer[] parsingNodeList(NodeList nodes) {
     Integer[] result = new Integer[nodes.getLength()];
     for (int i = 0; i < result.length; i++) {
       result[i] = Integer.valueOf(Integer.parseInt(((Element)nodes.item(i)).getTextContent().trim()));
     }
     return result;
   }

   private Integer[] parsingInteger(String str, String splitStr) {
     if (StringUtil.isNullOrEmpty(str))
       return null;
     String[] array = str.split(splitStr);
     Integer[] result = new Integer[array.length];
     for (int i = 0; i < result.length; i++) {
       result[i] = Integer.valueOf(Integer.parseInt(array[i].trim()));
     }
     return result;
   }

   private Double[] parsingDouble(String str, String splitStr) {
     if (StringUtil.isNullOrEmpty(str))
       return null;
     String[] array = str.split(splitStr);
     Double[] result = new Double[array.length];
     for (int i = 0; i < result.length; i++) {
       result[i] = Double.valueOf(Double.parseDouble(array[i].trim()));
     }
     return result;
   }


   public int getBoundaryLevel(int level) {
     int result = 0;
     for (int i = this._levelToIdx.length - 1; i >= 0; i--) {
       if (level >= this._levelToIdx[i].intValue()) {
         return this._levelToIdx[i].intValue();
       }
     }
     return result;
   }



   private int calcActionSpeed(int frameCount, int frameRate) {
     return (int)((frameCount * 40) * 24.0D / frameRate);
   }
 }


