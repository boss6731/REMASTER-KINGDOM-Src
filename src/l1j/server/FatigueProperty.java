package l1j.server;

import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheConverter;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheModel;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheProvider;

/**
 * 疲勞屬性
 */
public class FatigueProperty {
	private static FatigueProperty m_instance;
	
	/**
	 * 獲取單例實例
	 * @return FatigueProperty 單例實例
	 */
	public static FatigueProperty getInstance(){
		if(m_instance == null)
			m_instance = new FatigueProperty();
		return m_instance;
	}
	
	/**
	 * 重新加載設置
	 */
	public static void reload() {
		// m_instance = new FatigueProperty();
	}
	
	private FatiguePropertyModel mModel;
	
	private FatigueProperty(){
		registeredCacheModel();
	}
	
	/**
	 * 註冊緩存模型
	 */
	private void registeredCacheModel() {
		MJMonitorCacheModel<FatiguePropertyModel> model = MJMonitorCacheProvider.newJsonFileCacheModel("mj-fatigue", "./config/fatigue.json", FatiguePropertyModel.class, MJEncoding.MS949);
		model.cacheListener(new MJMonitorCacheConverter<FatiguePropertyModel>() {
			@Override
			public FatiguePropertyModel onNewCached(FatiguePropertyModel t, long modifiedMillis) {
				mModel = t;
				return null;
			}
		});
		MJMonitorCacheProvider.monitorCache().appendCacheModel(model);
	}
	
	/**
	 * 是否使用疲勞系統
	 * @return boolean 返回 true 表示使用疲勞系統，false 表示不使用
	 */
	public boolean use_fatigue() {
		return mModel.USEFATIGUE;
	}

	/**
	 * 獲取疲勞增益圖示 ID
	 * @return int 疲勞增益圖示 ID
	 */
	public int get_fatigue_buff_icon_id() {
		return mModel.FATIGUEBUFFICONID;
	}
	
	/**
	 * 獲取疲勞點可堆疊時間（毫秒）
	 * @return long 疲勞點可堆疊時間（毫秒）
	 */
	public long get_fatigue_point_stackable_millis() {
		return mModel.FATIGUEPOINTSTACKABLESECONDS * 1000L;
	}
	
	/**
	 * 獲取疲勞點上限
	 * @return int 疲勞點上限
	 */
	public int get_fatigue_point_limit() {
		return mModel.FATIGUEPOINTLIMIT;
	}
	
	/**
	 * 獲取疲勞效果持續時間（毫秒）
	 * @return long 疲勞效果持續時間（毫秒）
	 */
	public long get_fatigue_effect_millis() {
		return mModel.FATIGUEEFFECTSECONDS * 1000L;
	}
	
	/**
	 * 獲取疲勞效果傷害值
	 * @return double 疲勞效果傷害值
	 */
	public double get_fatigue_effect_damage() {
		return mModel.FATIGUEEFFECTDAMAGE;
	}
	
	/**
	 * 獲取疲勞效果減傷比例
	 * @return double 疲勞效果減傷比例
	 */
	public double get_fatigue_effect_reduction() {
		return mModel.FATIGUEEFFECTREDUCTION;
	}
	
	/**
	 * 獲取疲勞效果經驗值加成比例
	 * @return double 疲勞效果經驗值加成比例
	 */
	public double get_fatigue_effect_exp() {
		return mModel.FATIGUEEFFECTEXP;
	}
	
	/**
	 * 獲取疲勞效果金幣獲得率加成比例
	 * @return double 疲勞效果金幣獲得率加成比例
	 */
	public double get_fatigue_effect_adena() {
		return mModel.FATIGUEEFFECTADENA;
	}
	
	/**
	 * 疲勞屬性模型
	 */
	static class FatiguePropertyModel{
		boolean USEFATIGUE;		
		int FATIGUEBUFFICONID;
		long FATIGUEPOINTSTACKABLESECONDS;
		int FATIGUEPOINTLIMIT;
		long FATIGUEEFFECTSECONDS;
		double FATIGUEEFFECTDAMAGE;
		double FATIGUEEFFECTREDUCTION;
		double FATIGUEEFFECTEXP;
		double FATIGUEEFFECTADENA;
		
		FatiguePropertyModel(){
			USEFATIGUE = true;
			FATIGUEBUFFICONID = 6437;
			FATIGUEPOINTSTACKABLESECONDS = 60;
			FATIGUEPOINTLIMIT = 600;
			FATIGUEEFFECTSECONDS = 36000;
			FATIGUEEFFECTDAMAGE = .2D;
			FATIGUEEFFECTREDUCTION = .2D;
			FATIGUEEFFECTEXP = .60D;
			FATIGUEEFFECTADENA = .60D;
		}
	}
}
