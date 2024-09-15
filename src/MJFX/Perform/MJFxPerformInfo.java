package MJFX.Perform;

import MJFX.MJMDIPanelHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.VBox;

import java.util.Calendar;
import java.util.TimeZone;

public class MJFxPerformInfo {
	public static MJFxPerformInfo newInstance(MJMDIPanelHelper parent_helper, VBox container, StackedAreaChart<String, Integer> chart, String formatter_string){
		return new MJFxPerformInfo(parent_helper, container, chart, formatter_string);
	}
	
	private MJMDIPanelHelper m_parent_helper;
	private VBox m_container;
	private StackedAreaChart<String, Integer> m_chart;
	private Series<String, Integer> m_series;
	private ObservableList<Data<String, Integer>> m_datas;
	private String m_formatter_string;
	
	private MJFxPerformInfo(MJMDIPanelHelper parent_helper, VBox container, StackedAreaChart<String, Integer> chart, String formatter_string){
		m_parent_helper = parent_helper;
		m_container = container;
		m_chart = chart;
		m_series = new Series<String, Integer>();
		m_formatter_string = formatter_string;
		
		m_chart.setAnimated(false);
		m_chart.prefWidthProperty().bind(m_container.prefWidthProperty());
		m_chart.prefHeightProperty().bind(m_container.prefHeightProperty());
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+9"));
		m_datas = FXCollections.observableArrayList();
		for(int i=9; i>=0; --i){
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			int min = cal.get(Calendar.MINUTE);
			int second = cal.get(Calendar.SECOND) - i;
			if(second < 0){
				second = 59;
				if(--min < 0){
					min = 59;
					if(--hour < 0)
						hour = 23;
				}
			}
			m_datas.add(new Data<String, Integer>(String.format("%02d:%02d:%02d", hour, min, second), 0));
		}
		m_series.setName(String.format(m_formatter_string, 0));
		m_parent_helper.set_title(String.format(m_formatter_string, 0));
		m_series.setData(m_datas);
		m_chart.getData().add(m_series);
	}
	
	public void on_update(String time, int val){
		Data<String, Integer> node = new Data<String, Integer>();
		node.setXValue(time);
		node.setYValue(val);
		m_datas.add(node);
		m_datas.remove(0);
		String s = String.format(m_formatter_string, val);
		m_parent_helper.set_title(s);
		m_series.setName(s);
	}
}
