 package l1j.server.server.model;

 import MJFX.UIAdapter.MJUIAdapter;
 import java.io.File;
 import java.text.SimpleDateFormat;
 import java.util.Calendar;
 import java.util.Date;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Random;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 import javax.xml.bind.JAXBContext;
 import javax.xml.bind.Unmarshaller;
 import javax.xml.bind.annotation.XmlAccessType;
 import javax.xml.bind.annotation.XmlAccessorType;
 import javax.xml.bind.annotation.XmlAttribute;
 import javax.xml.bind.annotation.XmlElement;
 import javax.xml.bind.annotation.XmlRootElement;
 import l1j.server.server.datatables.BossSpawnTable;

 @XmlAccessorType(XmlAccessType.FIELD)
 public class L1BossCycle
 {
   @XmlAttribute(name = "Name")
   private String _name;
   @XmlElement(name = "Base")
   private Base _base;
   @XmlElement(name = "Cycle")
   private Cycle _cycle;

   @XmlAccessorType(XmlAccessType.FIELD)
   private static class Base
   {
     @XmlAttribute(name = "Date")
     private String _date;

     public String getDate() {
       return this._date;
     } @XmlAttribute(name = "Time")
     private String _time; private Base() {}
     public void setDate(String date) {
       this._date = date;
     }

     public String getTime() {
       return this._time;
     }

     public void setTime(String time) {
       this._time = time;
     }
   }


   @XmlAccessorType(XmlAccessType.FIELD)
   private static class Cycle
   {
     @XmlAttribute(name = "Period")
     private String _period;

     @XmlAttribute(name = "Start")
     private String _start;
     @XmlAttribute(name = "End")
     private String _end;

     public String getPeriod() {
       return this._period;
     }

     public String getStart() {
       return this._start;
     }

     public String getEnd() {
       return this._end;
     }
   }


   private static final Random _rnd = new Random();

   private Calendar _baseDate;
   private int _period;
   private int _periodDay;
   private int _periodHour;
   private int _periodMinute;
   private int _startTime;
   private int _endTime;
   private static SimpleDateFormat _sdfYmd = new SimpleDateFormat("yyyy/MM/dd");
   private static SimpleDateFormat _sdfTime = new SimpleDateFormat("HH:mm");
   private static SimpleDateFormat _sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");

   private static Date _initDate = new Date();
   private static String _initTime = "0:00";
   private static final Calendar START_UP = Calendar.getInstance();


   public void init() throws Exception {
     Base base = getBase();

     if (base == null) {
       setBase(new Base());
       getBase().setDate(_sdfYmd.format(_initDate));
       getBase().setTime(_initTime);
       base = getBase();
     } else {
       try {
         _sdfYmd.parse(base.getDate());
       } catch (Exception e) {
         base.setDate(_sdfYmd.format(_initDate));
       }
       try {
         _sdfTime.parse(base.getTime());
       } catch (Exception e) {
         base.setTime(_initTime);
       }
     }

       // 獲取當前時間的Calendar實例
       Calendar baseCal = Calendar.getInstance();

// 設置Calendar實例的時間為base物件的日期和時間
       baseCal.setTime(_sdf.parse(base.getDate() + " " + base.getTime()));

// 獲取周期實例
       Cycle spawn = getCycle();

// 如果spawn為空或spawn的周期為空，則拋出異常
       if (spawn == null || spawn.getPeriod() == null) {
           throw new Exception("Cycle的Period是必需的"); // 拋出異常提示
       }

     String period = spawn.getPeriod();
     this._periodDay = getTimeParse(period, "d");
     this._periodHour = getTimeParse(period, "h");
     this._periodMinute = getTimeParse(period, "m");

     String start = spawn.getStart();
     int sDay = getTimeParse(start, "d");
     int sHour = getTimeParse(start, "h");
     int sMinute = getTimeParse(start, "m");
     String end = spawn.getEnd();
     int eDay = getTimeParse(end, "d");
     int eHour = getTimeParse(end, "h");
     int eMinute = getTimeParse(end, "m");


     this._period = this._periodDay * 24 * 60 + this._periodHour * 60 + this._periodMinute;
     this._startTime = sDay * 24 * 60 + sHour * 60 + sMinute;
     this._endTime = eDay * 24 * 60 + eHour * 60 + eMinute;
     if (this._period <= 0) {
       throw new Exception("must be Period > 0");
     }

     if (this._startTime < 0 || this._period < this._startTime) {
       this._startTime = 0;
     }

     if (this._endTime < 0 || this._period < this._endTime || end == null) {
       this._endTime = this._period;
     }
     if (this._startTime > this._endTime) {
       this._startTime = this._endTime;
     }


     if (this._startTime == this._endTime) {
       if (this._endTime == this._period) {
         this._startTime--;
       } else {
         this._endTime++;
       }
     }


     while (!baseCal.after(START_UP)) {
       baseCal.add(5, this._periodDay);
       baseCal.add(11, this._periodHour);
       baseCal.add(12, this._periodMinute);
     }
     this._baseDate = baseCal;
   }



   private Calendar getBaseCycleOnTarget(Calendar target) {
     Calendar base = (Calendar)this._baseDate.clone();
     if (target.after(base))
     {
       while (target.after(base)) {
         base.add(5, this._periodDay);
         base.add(11, this._periodHour);
         base.add(12, this._periodMinute);
       }
     }
     if (target.before(base)) {
       while (target.before(base)) {
         base.add(5, -this._periodDay);
         base.add(11, -this._periodHour);
         base.add(12, -this._periodMinute);
       }
     }

     Calendar end = (Calendar)base.clone();
     end.add(12, this._endTime);
     if (end.before(target)) {
       base.add(5, this._periodDay);
       base.add(11, this._periodHour);
       base.add(12, this._periodMinute);
     }
     return base;
   }






   public Calendar calcSpawnTime(Calendar now) {
     Calendar base = getBaseCycleOnTarget(now);

     base.add(12, this._startTime);

     int diff = (this._endTime - this._startTime) * 60;
     int random = (diff > 0) ? _rnd.nextInt(diff) : 0;
     base.add(13, random);
     return base;
   }






   public Calendar getSpawnStartTime(Calendar now) {
     Calendar startDate = getBaseCycleOnTarget(now);

     startDate.add(12, this._startTime);
     return startDate;
   }






   public Calendar getSpawnEndTime(Calendar now) {
     Calendar endDate = getBaseCycleOnTarget(now);

     endDate.add(12, this._endTime);
     return endDate;
   }






   public Calendar nextSpawnTime(Calendar now) {
     Calendar next = (Calendar)now.clone();
     next.add(5, this._periodDay);
     next.add(11, this._periodHour);
     next.add(12, this._periodMinute);
     return calcSpawnTime(next);
   }






   public Calendar getLatestStartTime(Calendar now) {
     Calendar latestStart = getSpawnStartTime(now);
     if (now.before(latestStart)) {


       latestStart.add(5, -this._periodDay);
       latestStart.add(11, -this._periodHour);
       latestStart.add(12, -this._periodMinute);
     }

     return latestStart;
   }

   private static int getTimeParse(String target, String search) {
     if (target == null) {
       return 0;
     }
     int n = 0;
     Matcher matcher = Pattern.compile("\\d+" + search).matcher(target);
     if (matcher.find()) {
       String match = matcher.group();
       n = Integer.parseInt(match.replace(search, ""));
     }
     return n;
   }

   @XmlAccessorType(XmlAccessType.FIELD)
   @XmlRootElement(name = "BossCycleList")
   static class L1BossCycleList {
     @XmlElement(name = "BossCycle")
     private List<L1BossCycle> bossCycles;

     public List<L1BossCycle> getBossCycles() {
       return this.bossCycles;
     }

     public void setBossCycles(List<L1BossCycle> bossCycles) {
       this.bossCycles = bossCycles;
     }
   }

     public static void load() {
         try {
             // 創建 JAXB 上下文並初始化 L1BossCycleList 類
             JAXBContext context = JAXBContext.newInstance(new Class[] { L1BossCycleList.class });

             // 創建一個解組器（Unmarshaller）
             Unmarshaller um = context.createUnmarshaller();

             // 加載並解析 BossCycle.xml 文件
             File file = new File("./data/xml/Cycle/BossCycle.xml");
             L1BossCycleList bossList = (L1BossCycleList)um.unmarshal(file);

             // 遍歷每個 BossCycle 並初始化它們，然後將它們放入 _cycleMap 中
             for (L1BossCycle cycle : bossList.getBossCycles()) {
                 cycle.init();
                 _cycleMap.put(cycle.getName(), cycle);
             }

             // 填充 Boss 生成表
             BossSpawnTable.fillSpawnTable();
         } catch (Exception e) {
             // 如果發生異常，記錄錯誤並調用 MJUIAdapter 的 on_exit 方法
             _log.log(Level.SEVERE, "無法讀取 BossCycle", e);
             MJUIAdapter.on_exit();
         }
     }





     public void showData(Calendar now) {
         // 打印類型信息
         System.out.println("[類型]" + getName());

         // 打印出現期間標題
         System.out.print("[出現期間]");

         // 打印出現開始時間和結束時間
         System.out.print(_sdf.format(getSpawnStartTime(now).getTime()) + " - ");
         System.out.println(_sdf.format(getSpawnEndTime(now).getTime()));
     }

   private static HashMap<String, L1BossCycle> _cycleMap = new HashMap<>();

   public static L1BossCycle getBossCycle(String type) {
     return _cycleMap.get(type);
   }

   public String getName() {
     return this._name;
   }

   public void setName(String name) {
     this._name = name;
   }

   public Base getBase() {
     return this._base;
   }

   public void setBase(Base base) {
     this._base = base;
   }

   public Cycle getCycle() {
     return this._cycle;
   }

   public void setCycle(Cycle cycle) {
     this._cycle = cycle;
   }

   private static Logger _log = Logger.getLogger(L1BossCycle.class.getName());
 }


