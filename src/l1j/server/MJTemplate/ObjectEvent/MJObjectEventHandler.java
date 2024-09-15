package l1j.server.MJTemplate.ObjectEvent;

import l1j.server.MJTemplate.Attribute.MJAttrKey;
import l1j.server.MJTemplate.Attribute.MJAttrMap;

/**
 * @author mjsoft
 * <p>별도의 이벤트 큐를 가진 핸들러가 아니기 때문에, add remove 등의 write에서 ns단위의 동기화 불균형이 발생할 수 있다.<p>.
 * <p>현재 서버 패턴이 중구난방인 점도 한 몫함..</p>
 * <p>향후 ns 단위의 튜닝이나, 핸들러 사용이 많아진다면 별도의 이벤트 큐를 구현해서 사용할 것.</p>
 * <p>ns 단위의 동기화 불균형을 없애고 싶다면 removeListener에서 attribute.remove를 제거할 것.</p>
 **/
public class MJObjectEventHandler {
	private MJAttrMap attribute;
	MJObjectEventHandler(){
		attribute = MJAttrMap.newConcurrentHash();
	}
	
	public <A extends MJObjectEventArgs> void fire(MJAttrKey<MJObjectEventComposite<A>> key, A args){
		if(!attribute.has(key)){
			return;
		}
		MJObjectEventComposite<A> composite = attribute.get(key).get();
		if(composite.fire(args) <= 0){
			attribute.remove(key);
		}
	}
	
	public <A extends MJObjectEventArgs> void addListener(MJAttrKey<MJObjectEventComposite<A>> key, MJObjectEventListener<A> listener){
		if(!attribute.has(key)){
			newComposite(key);
		}
		attribute.get(key).get().addListener(listener);
	}
	
	public <A extends MJObjectEventArgs> void removeListener(MJAttrKey<MJObjectEventComposite<A>> key, MJObjectEventListener<A> listener){
		if(!attribute.has(key)){
			return;
		}
		MJObjectEventComposite<A> composite = attribute.get(key).get();
		if(composite.removeListener(listener) <= 0){
			attribute.remove(key);
		}
	}
	
	private <A extends MJObjectEventArgs> void newComposite(MJAttrKey<MJObjectEventComposite<A>> key){
		attribute.getNotExistsNew(key).set(new MJObjectEventComposite<A>());
	}
	
	public int numOfEvents(){
		return attribute.numOfAttributes();
	}
}
