 package MJFX.Perform;

 import MJFX.MJMDIPanelHelper;
 import java.util.Calendar;
 import java.util.TimeZone;
 import javafx.collections.FXCollections;
 import javafx.collections.ObservableList;
 import javafx.scene.chart.StackedAreaChart;
 import javafx.scene.chart.XYChart;
 import javafx.scene.layout.VBox;

 public class MJFxPerformInfo {
   private MJMDIPanelHelper m_parent_helper;

   public static MJFxPerformInfo newInstance(MJMDIPanelHelper parent_helper, VBox container, StackedAreaChart<String, Integer> chart, String formatter_string) {
     return new MJFxPerformInfo(parent_helper, container, chart, formatter_string);
   }


   private VBox m_container;
   private StackedAreaChart<String, Integer> m_chart;
   private XYChart.Series<String, Integer> m_series;
   private ObservableList<XYChart.Data<String, Integer>> m_datas;
   private String m_formatter_string;

   private MJFxPerformInfo(MJMDIPanelHelper parent_helper, VBox container, StackedAreaChart<String, Integer> chart, String formatter_string) {
     this.m_parent_helper = parent_helper;
     this.m_container = container;
     this.m_chart = chart;
     this.m_series = new XYChart.Series<>();
     this.m_formatter_string = formatter_string;

     this.m_chart.setAnimated(false);
     this.m_chart.prefWidthProperty().bind(this.m_container.prefWidthProperty());
     this.m_chart.prefHeightProperty().bind(this.m_container.prefHeightProperty());
     Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
     this.m_datas = FXCollections.observableArrayList();
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
       this.m_datas.add(new XYChart.Data<>(String.format("%02d:%02d:%02d", new Object[] { Integer.valueOf(hour), Integer.valueOf(min), Integer.valueOf(second) }), Integer.valueOf(0)));
     }
     this.m_series.setName(String.format(this.m_formatter_string, new Object[] { Integer.valueOf(0) }));
     this.m_parent_helper.set_title(String.format(this.m_formatter_string, new Object[] { Integer.valueOf(0) }));
     this.m_series.setData(this.m_datas);
     this.m_chart.getData().add(this.m_series);
   }

   public void on_update(String time, int val) {
     XYChart.Data<String, Integer> node = new XYChart.Data<>();
     node.setXValue(time);
     node.setYValue(Integer.valueOf(val));
     this.m_datas.add(node);
     this.m_datas.remove(0);
     String s = String.format(this.m_formatter_string, new Object[] { Integer.valueOf(val) });
     this.m_parent_helper.set_title(s);
     this.m_series.setName(s);
   }
 }


