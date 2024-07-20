package MJFX.Perform;

import MJFX.MJMDIPanelHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.server.model.L1World;

import java.util.Calendar;
import java.util.TimeZone;

public class MJFxPerformInfoByL1Object {
	// 父輔助對象
	private MJMDIPanelHelper m_parent_helper;

	// 私有容器
	private VBox m_container;
	// 堆疊區域圖表
	private StackedAreaChart<String, Integer> m_chart;

	// 私有構造函數
	private MJFxPerformInfoByL1Object(MJMDIPanelHelper parent_helper, VBox container, StackedAreaChart<String, Integer> chart) {
		this.m_parent_helper = parent_helper;
		this.m_container = container;
		this.m_chart = chart;

		this.m_chart.setAnimated(false);
		this.m_chart.prefWidthProperty().bind(this.m_container.prefWidthProperty());
		this.m_chart.prefHeightProperty().bind(this.m_container.prefHeightProperty());
		this.m_parent_helper.set_title("物品狀態");

		for (MJEObjectType o : MJEObjectType.values()) {
			this.m_chart.getData().add(o.get_series());
		}
	}

	// 靜態方法來創建新實例
	public static MJFxPerformInfoByL1Object newInstance(MJMDIPanelHelper parent_helper, VBox container, StackedAreaChart<String, Integer> chart) {
		return new MJFxPerformInfoByL1Object(parent_helper, container, chart);
	}

	// 更新不同物件數據的方法
	public void on_update(String time) {
		MJEObjectType.PC.on_update(time, L1World.getInstance().get_player_size());
		MJEObjectType.NPC.on_update(time, L1World.getInstance().get_npc_size());
		MJEObjectType.ITEM.on_update(time, L1World.getInstance().get_item_size());

		this.m_parent_helper.set_title(String.format("物品狀態 - %s, %s, %s",
				MJEObjectType.PC.get_series().getName(),
				MJEObjectType.NPC.get_series().getName(),
				MJEObjectType.ITEM.get_series().getName()));
	}

	// 枚舉類型 MJEObjectType
	enum MJEObjectType {
		PC(0, "玩家：%d人"),
		NPC(1, "NPC : %d"),
		ITEM(2, "物品：%d");

		private int m_val;
		private String m_formatter_string;
		private XYChart.Series<String, Integer> m_series;
		private ObservableList<XYChart.Data<String, Integer>> m_datas;

		// 构造函数
		MJEObjectType(int val, String formatter_string) {
			this.m_val = val;
			this.m_formatter_string = formatter_string;
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
			this.m_datas = FXCollections.observableArrayList();

			this.m_series = new XYChart.Series<>();

			for (int i = 9; i >= 0; i--) {
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				int min = cal.get(Calendar.MINUTE);
				int second = cal.get(Calendar.SECOND) - i;

				// 調整時間
				if (second < 0) {
					second = 59;
					if (--min < 0) {
						min = 59;
						if (--hour < 0) {
							hour = 23;
						}
					}
				}

				// 添加初始數據點到數據列表
				this.m_datas.add(new XYChart.Data<>(
						String.format("%02d:%02d:%02d", hour, min, second),
						Integer.valueOf(MJRnd.next(1000))
				));
			}

			// 設置數據系列名稱
			this.m_series.setName(String.format(this.m_formatter_string, 0));
			this.m_series.setData(this.m_datas);
		}

		// 獲取數據系列的方法
		XYChart.Series<String, Integer> get_series() {
			return this.m_series;
		}

		// 將枚舉轉換為整數的方法
		int to_int() {
			return this.m_val;
		}

		// 更新數據的方法
		void on_update(String time, int val) {
			XYChart.Data<String, Integer> node = new XYChart.Data<>();
			node.setXValue(time);
			node.setYValue(val / 10);

			// 添加新數據點到數據列表，同時移除最早的數據點
			this.m_datas.add(node);
			this.m_datas.remove(0);

			// 更新標題和數據系列名稱
			String s = String.format(this.m_formatter_string, val);
			this.m_series.setName(s);
		}
	}
}


