 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Map;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.utils.SQLUtil;

 public class CharacterBalance
 {
   private static CharacterBalance _instance;

   public static CharacterBalance getInstance() {
     if (_instance == null) {
       _instance = new CharacterBalance();
     }
     return _instance;
   }

   private Map<Integer, ArrayList<L1PvpDmg>> _list = new HashMap<>();

   public void reload() {
     Map<Integer, ArrayList<L1PvpDmg>> list = new HashMap<>();
     loadList(list);
     this._list = list;
   }

   private CharacterBalance() {
     loadList(this._list);
   }

     private void loadList(Map<Integer, ArrayList<L1PvpDmg>> list) {
         Connection con = null; // 聲明數據庫連接
         PreparedStatement pstm = null; // 聲明預處理語句
         ResultSet rs = null; // 聲明結果集
         ArrayList<L1PvpDmg> edlist = null; // 聲明PvP傷害列表
         L1PvpDmg ed = null; // 聲明PvP傷害對象

         try {
             con = L1DatabaseFactory.getInstance().getConnection(); // 獲取數據庫連接
             pstm = con.prepareStatement("SELECT * FROM characters_balance"); // 準備SQL查詢語句
             rs = pstm.executeQuery(); // 執行查詢並返回結果集

             while (rs.next()) { // 遍歷結果集
                 ed = new L1PvpDmg(); // 創建新的PvP傷害對象
                 int type = rs.getInt("等級類型"); // 獲取"클래스타입"字段的值
                 edlist = list.get(Integer.valueOf(type)); // 從列表中獲取對應類型的PvP傷害列表

                 if (edlist == null) { // 如果該類型的列表為空，則創建一個新的列表
                     edlist = new ArrayList<>();
                     list.put(Integer.valueOf(type), edlist); // 將新的列表放入映射中
                 }

                 double dmg_rate = rs.getInt("傷害倍數率") * 0.01D; // 獲取並計算"대미지배율"字段的值
                 double magic_dmg_rate = rs.getInt("魔法傷害倍數") * 0.01D; // 獲取並計算"마법대미지배율"字段的值

                 ed.setTargetType(rs.getInt("目標類型")); // 設置目標類型
                 ed.setDmg(rs.getInt("附加傷害")); // 設置附加傷害
                 ed.setHit(rs.getInt("附加命中")); // 設置附加命中
                 ed.setMagicDmg(rs.getInt("魔法附加傷害")); // 設置魔法附加傷害
                 ed.setMagicHit(rs.getInt("魔法附加命中")); // 設置魔法附加命中
                 ed.setDmgRate(dmg_rate); // 設置傷害比例
                 ed.setMagicDmgRate(magic_dmg_rate); // 設置魔法傷害比例

                 edlist.add(ed); // 將PvP傷害對象添加到列表中
             }
         } catch (Exception e) {
             e.printStackTrace(); // 捕獲並打印異常堆棧跟蹤
         } finally {
             SQLUtil.close(rs); // 關閉結果集
             SQLUtil.close(pstm); // 關閉預處理語句
             SQLUtil.close(con); // 關閉連接
         }
     }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
   }

   public int getDmg(int type, int target_type) {
     ArrayList<L1PvpDmg> edlist = this._list.get(Integer.valueOf(type));
     if (edlist != null) {
       for (L1PvpDmg ed : edlist) {
         if (ed.getTargetType() == target_type) {
           return ed.getDmg();
         }
       }
     }

     return 0;
   }

   public int getHit(int type, int target_type) {
     ArrayList<L1PvpDmg> edlist = this._list.get(Integer.valueOf(type));
     if (edlist != null) {
       for (L1PvpDmg ed : edlist) {
         if (ed.getTargetType() == target_type) {
           return ed.getHit();
         }
       }
     }

     return 0;
   }

   public int getMagicDmg(int type, int target_type) {
     ArrayList<L1PvpDmg> edlist = this._list.get(Integer.valueOf(type));
     if (edlist != null) {
       for (L1PvpDmg ed : edlist) {
         if (ed.getTargetType() == target_type) {
           return ed.getMagicDmg();
         }
       }
     }

     return 0;
   }

   public int getMagicHit(int type, int target_type) {
     ArrayList<L1PvpDmg> edlist = this._list.get(Integer.valueOf(type));
     if (edlist != null) {
       for (L1PvpDmg ed : edlist) {
         if (ed.getTargetType() == target_type) {
           return ed.getMagicHit();
         }
       }
     }

     return 0;
   }

   public double getDmgRate(int type, int target_type) {
     double rate = 0.0D;
     ArrayList<L1PvpDmg> edlist = this._list.get(Integer.valueOf(type));
     if (edlist != null) {
       for (L1PvpDmg ed : edlist) {
         if (ed.getTargetType() == target_type) {
           rate += ed.getDmgRate();
           break;
         }
       }
     }
     return rate;
   }

   public double getMagicDmgRate(int type, int target_type) {
     double rate = 0.0D;
     ArrayList<L1PvpDmg> edlist = this._list.get(Integer.valueOf(type));
     if (edlist != null) {
       for (L1PvpDmg ed : edlist) {
         if (ed.getTargetType() == target_type) {
           rate += ed.getMagicDmgRate();

           break;
         }
       }
     }
     return rate;
   }


   public class L1PvpDmg
   {
     private int _target;
     private int _dmg;
     private int _hit;

     public int getTargetType() {
       return this._target;
     }
     private int _magicdmg; private int _magichit; private double _rate; private double _magicrate;
     public void setTargetType(int i) {
       this._target = i;
     }

     public int getDmg() {
       return this._dmg;
     }

     public void setDmg(int i) {
       this._dmg = i;
     }

     public int getHit() {
       return this._hit;
     }

     public void setHit(int i) {
       this._hit = i;
     }

     public int getMagicDmg() {
       return this._magicdmg;
     }

     public void setMagicDmg(int i) {
       this._magicdmg = i;
     }

     public int getMagicHit() {
       return this._magichit;
     }

     public void setMagicHit(int i) {
       this._magichit = i;
     }



     public double getDmgRate() {
       return this._rate;
     }

     public void setDmgRate(double i) {
       this._rate = i;
     }



     public double getMagicDmgRate() {
       return this._magicrate;
     }

     public void setMagicDmgRate(double i) {
       this._magicrate = i;
     }
   }
 }


