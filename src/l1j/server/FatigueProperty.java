package l1j.server;

import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheConverter;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheModel;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheProvider;

public class FatigueProperty {
	private static FatigueProperty m_instance;
	public static FatigueProperty getInstance(){
		if(m_instance == null)
			m_instance = new FatigueProperty();
		return m_instance;
	}

	public static void reload() {
		//m_instance = new FatigueProperty();
	}

	private FatiguePropertyModel mModel;
	private FatigueProperty(){
		registeredCacheModel();
	}

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

	public boolean use_fatigue() {
		return mModel.USEFATIGUE;
	}

	public int get_fatigue_buff_icon_id() {
		return mModel.FATIGUEBUFFICONID;
	}

	public long get_fatigue_point_stackable_millis() {
		return mModel.FATIGUEPOINTSTACKABLESECONDS * 1000L;
	}

	public int get_fatigue_point_limit() {
		return mModel.FATIGUEPOINTLIMIT;
	}

	public long get_fatigue_effect_millis() {
		return mModel.FATIGUEEFFECTSECONDS * 1000L;
	}

	public double get_fatigue_effect_damage() {
		return mModel.FATIGUEEFFECTDAMAGE;
	}

	public double get_fatigue_effect_reduction() {
		return mModel.FATIGUEEFFECTREDUCTION;
	}

	public double get_fatigue_effect_exp() {
		return mModel.FATIGUEEFFECTEXP;
	}

	public double get_fatigue_effect_adena() {
		return mModel.FATIGUEEFFECTADENA;
	}

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

