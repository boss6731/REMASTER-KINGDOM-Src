package l1j.server.MJTemplate.ObServer;

import java.util.ArrayDeque;

import l1j.server.MJTemplate.Interface.MJInstanceObserver;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;

public class MJCopyMapObservable extends MJAbstractInstanceObservable<Short>{
	private static final int MAXIMUM_CAPACITY = 1000;
	public static final int RESET_X = 33445;
	public static final int RESET_Y = 32794;
	public static final short RESET_MAPID = 4;
	
	private static MJCopyMapObservable _instance;
	public static MJCopyMapObservable getInstance(){
		if(_instance == null)
			_instance = new MJCopyMapObservable();
		return _instance;
	}
	
	private ArrayDeque<Short> _mapQ;
	private MJCopyMapObservable(){
		initializeObservable(MAXIMUM_CAPACITY);
		_mapQ = new ArrayDeque<Short>(MAXIMUM_CAPACITY);
		for(int i = MAXIMUM_CAPACITY - 1; i>= 0; --i)
			_mapQ.push((short)(i + 30000));
	}
	
	private Short popId(){
		synchronized(_mapQ){
			return _mapQ.pop();
		}
	}
	
	@Override
	public MJInstanceObserver remove(Short key){
		pushId(key);
		return super.remove(key);
	}
	
	private void pushId(Short id){
		synchronized(_mapQ){
			_mapQ.push(id);
		}
	}
	
	@Override
	public Short register(Short key, MJInstanceObserver osv){
		if(!(osv instanceof MJAbstractCopyMapObserver) || _mapQ.isEmpty()){
			return null;
		}
		MJAbstractCopyMapObserver observer = (MJAbstractCopyMapObserver)osv;
		Short next = popId();
		@SuppressWarnings("unused")
		L1Map m = observer.initializeMap(key, next);
		super.register(next, observer);
		return next;
	}
	
	@Override
	public void onNotify(MJInstanceObserver observer){
	}
	
	@Override
	public void onNotify(MJInstanceObserver observer, int reason){
		remove((short) reason);
	}
	
	public short idenMap(short mapId){
		MJInstanceObserver osv = get(mapId);
		if(osv == null)
			return mapId;
		
		MJAbstractCopyMapObserver observer = (MJAbstractCopyMapObserver)osv;
		return observer._sourceMapId;
	}
	
	public void resetTeleport(L1PcInstance pc){
		if(pc.getMapId() >= 30000){
			pc.do_simple_teleport(RESET_X, RESET_Y, RESET_MAPID);
		}
	}
	
	public void resetPosition(L1PcInstance pc){
		if(pc.getMapId() >= 30000){
			resetAlwaysPositon(pc);
		}
	}
	
	public void resetAlwaysPositon(L1PcInstance pc){
		pc.setX(RESET_X);
		pc.setY(RESET_Y);
		pc.setMap(RESET_MAPID);
	}
}
