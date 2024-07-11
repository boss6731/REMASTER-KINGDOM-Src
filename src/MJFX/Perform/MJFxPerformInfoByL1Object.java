 package MJFX.Perform;

 import MJFX.MJMDIPanelHelper;
 import java.util.Calendar;
 import java.util.TimeZone;
 import javafx.collections.FXCollections;
 import javafx.collections.ObservableList;
 import javafx.scene.chart.StackedAreaChart;
 import javafx.scene.chart.XYChart;
 import javafx.scene.layout.VBox;
 import l1j.server.MJTemplate.MJRnd;
 import l1j.server.server.model.L1World;

 public class MJFxPerformInfoByL1Object {
   private MJMDIPanelHelper m_parent_helper;

   public static MJFxPerformInfoByL1Object newInstance(MJMDIPanelHelper parent_helper, VBox container, StackedAreaChart<String, Integer> chart) {
     return new MJFxPerformInfoByL1Object(parent_helper, container, chart);
   }

   private VBox m_container;
   private StackedAreaChart<String, Integer> m_chart;

   private MJFxPerformInfoByL1Object(MJMDIPanelHelper parent_helper, VBox container, StackedAreaChart<String, Integer> chart) {
     this.m_parent_helper = parent_helper;
     this.m_container = container;
     this.m_chart = chart;

     this.m_chart.setAnimated(false);
     this.m_chart.prefWidthProperty().bind(this.m_container.prefWidthProperty());
     this.m_chart.prefHeightProperty().bind(this.m_container.prefHeightProperty());
     this.m_parent_helper.set_title("物件狀態");
     for (MJEObjectType o : MJEObjectType.values())
       this.m_chart.getData().add(o.get_series());
   }

   public void on_update(String time) {
     MJEObjectType.PC.on_update(time, L1World.getInstance().get_player_size());
     MJEObjectType.NPC.on_update(time, L1World.getInstance().get_npc_size());
     MJEObjectType.ITEM.on_update(time, L1World.getInstance().get_item_size());
     this.m_parent_helper.set_title(String.format("物件狀態 - %s, %s, %s", new Object[] { MJEObjectType.PC
             .get_series().getName(), MJEObjectType.NPC
             .get_series().getName(), MJEObjectType.ITEM
             .get_series().getName() }));
   }

   enum MJEObjectType
   {
     PC(0, "玩家：%d人"),
     NPC(1, "NPC : %d"),
     ITEM(2, "物品：%d");
     private int m_val;
     private String m_formatter_string;
     private XYChart.Series<String, Integer> m_series;
     private ObservableList<XYChart.Data<String, Integer>> m_datas;

     MJEObjectType(int val, String formatter_string) {
       this.m_val = val;
       this.m_formatter_string = formatter_string;
       Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
       this.m_datas = FXCollections.observableArrayList();
       this.m_series = new XYChart.Series<>();
       for (int i = 9; i >= 0; i--) {
         int hour = cal.get(11);
         int min = cal.get(12);
         int second = cal.get(13) - i;
         if (second < 0) {
           second = 59;
           if (--min < 0) {
             min = 59;
             if (--hour < 0)
               hour = 23;
           }
         }
         this.m_datas.add(new XYChart.Data<>(String.format("%02d:%02d:%02d", new Object[] { Integer.valueOf(hour), Integer.valueOf(min), Integer.valueOf(second) }), Integer.valueOf(MJRnd.next(1000))));
       }
       this.m_series.setName(String.format(this.m_formatter_string, new Object[] { Integer.valueOf(0) }));
       this.m_series.setData(this.m_datas);
     }
     XYChart.Series<String, Integer> get_series() {
       return this.m_series;
     }

     int to_int() {
       return this.m_val;
     }

     void on_update(String time, int val) {
       XYChart.Data<String, Integer> node = new XYChart.Data<>();
       node.setXValue(time);
       node.setYValue(Integer.valueOf(val / 10));
       this.m_datas.add(node);
       this.m_datas.remove(0);
       String s = String.format(this.m_formatter_string, new Object[] { Integer.valueOf(val) });
       this.m_series.setName(s);
     }
   }
 }


