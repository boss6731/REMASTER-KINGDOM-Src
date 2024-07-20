package MJFX.Perform;

import MJFX.MJMDIPanelHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;

import java.util.Calendar;
import java.util.TimeZone;

public class MJFxPerformInfo {
	// 父輔助對象
	private MJMDIPanelHelper m_parent_helper;

	// 私有容器
	private VBox m_container;
	// 堆疊區域圖表
	private StackedAreaChart<String, Integer> m_chart;
	// 圖表系列
	private XYChart.Series<String, Integer> m_series;
	// 可觀察的數據列表
	private ObservableList<XYChart.Data<String, Integer>> m_datas;
	// 格式化字符串
	private String m_formatter_string;

	// 私有構造函數
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
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			int min = cal.get(Calendar.MINUTE);
			int second = cal.get(Calendar.SECOND) - i;
			if (second < 0) {
				second = 59;
				if (--min < 0) {
					min = 59;
					if (--hour < 0)
						hour = 23;
				}
			}
			this.m_datas.add(new XYChart.Data<>(String.format("%02d:%02d:%02d", hour, min, second), 0));
		}
		this.m_series.setName(String.format(this.m_formatter_string, 0));
		this.m_parent_helper.set_title(String.format(this.m_formatter_string, 0));
		this.m_series.setData(this.m_datas);
		this.m_chart.getData().add(this.m_series);
	}

	// 靜態方法來創建新實例
	public static MJFxPerformInfo newInstance(MJMDIPanelHelper parent_helper, VBox container, StackedAreaChart<String, Integer> chart, String formatter_string) {
		return new MJFxPerformInfo(parent_helper, container, chart, formatter_string);
	}

	// 更新圖表數據的方法
	public void on_update(String time, int val) {
		XYChart.Data<String, Integer> node = new XYChart.Data<>();
		node.setXValue(time);
		node.setYValue(val);

		// 添加新數據點到數據列表，同時移除最早的數據點
		this.m_datas.add(node);
		this.m_datas.remove(0);

		// 更新標題和數據系列名稱
		String s = String.format(this.m_formatter_string, val);
		this.m_parent_helper.set_title(s);
		this.m_series.setName(s);
	}
}


