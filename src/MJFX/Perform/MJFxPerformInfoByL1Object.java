package MJFX.Perform;

import MJFX.MJMDIPanelHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.VBox;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.server.model.L1World;

import java.util.Calendar;
import java.util.TimeZone;

public class MJFxPerformInfoByL1Object {
	public static MJFxPerformInfoByL1Object newInstance(MJMDIPanelHelper parent_helper, VBox container, StackedAreaChart<String, Integer> chart){
		return new MJFxPerformInfoByL1Object(parent_helper, container, chart);
	}
	
	private MJMDIPanelHelper m_parent_helper;
	private VBox m_container;
	private StackedAreaChart<String, Integer> m_chart;
	private MJFxPerformInfoByL1Object(MJMDIPanelHelper parent_helper, VBox container, StackedAreaChart<String, Integer> chart) {
		m_parent_helper = parent_helper;
		m_container = container;
		m_chart = chart;

		m_chart.setAnimated(false);
		m_chart.prefWidthProperty().bind(m_container.prefWidthProperty());
		m_chart.prefHeightProperty().bind(m_container.prefHeightProperty());
		m_parent_helper.set_title("物件現況");
		for (MJEObjectType o : MJEObjectType.values())
			m_chart.getData().add(o.get_series());
	}

	public void on_update(String time) {
		MJEObjectType.PC.on_update(time, L1World.getInstance().get_player_size());
		MJEObjectType.NPC.on_update(time, L1World.getInstance().get_npc_size());
		MJEObjectType.ITEM.on_update(time, L1World.getInstance().get_item_size());
		m_parent_helper.set_title(String.format("物件現況 - %s, %s, %s",
				MJEObjectType.PC.get_series().getName(),
				MJEObjectType.NPC.get_series().getName(),
				MJEObjectType.ITEM.get_series().getName()
		));
	}

	enum MJEObjectType {
		PC(0, "玩家 : %d名"),
		NPC(1, "NPC : %d個"),
		ITEM(2, "物品 : %d個");
		
		private int m_val;
		private String m_formatter_string;
		private Series<String, Integer> m_series;
		private ObservableList<Data<String, Integer>> m_datas;
		MJEObjectType(int val, String formatter_string){
			m_val = val;
			m_formatter_string = formatter_string;
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
			m_datas = FXCollections.observableArrayList();
			m_series = new Series<String, Integer>();
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
				m_datas.add(new Data<String, Integer>(String.format("%02d:%02d:%02d", hour, min, second), MJRnd.next(1000)));
			}
			m_series.setName(String.format(m_formatter_string, 0));
			m_series.setData(m_datas);
		}
		Series<String, Integer> get_series(){
			return m_series;
		}
		
		int to_int(){
			return m_val;
		}
		
		void on_update(String time, int val){
			Data<String, Integer> node = new Data<String, Integer>();
			node.setXValue(time);
			node.setYValue(val / 10);
			m_datas.add(node);
			m_datas.remove(0);
			String s = String.format(m_formatter_string, val);
			m_series.setName(s);
		}
	}
}
