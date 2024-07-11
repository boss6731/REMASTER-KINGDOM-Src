package l1j.server.MJTemplate.Attribute;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.MJTemplate.MJReadWriteLock;
import l1j.server.MJTemplate.Attribute.MJAttrHashMap.AttrHashValue;

public class MJAttrConcurrentMap implements MJAttrMap{
	private final ConcurrentHashMap<MJAttrKey<?>, MJAttrValue<?>> attributes = new ConcurrentHashMap<>();
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> MJAttrValue<T> get(MJAttrKey<T> key){
		return (MJAttrValue<T>) attributes.get(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> MJAttrValue<T> getNotExistsNew(MJAttrKey<T> key) {
		MJAttrValue<T> value = (MJAttrValue<T>) attributes.get(key);
		if(value == null){
			value = new AttrHashValue<T>();
			attributes.put(key, value);
		}
		return value;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> MJAttrValue<T> remove(MJAttrKey<T> key) {
		return (MJAttrValue<T>) attributes.remove(key);
	}
	
	@Override
	public boolean has(MJAttrKey<?> key){
		return attributes.containsKey(key);
	}
	
	@Override
	public int numOfAttributes() {
		return attributes.size();
	}
	
	static class MJAttrConcurrentValue<T> implements MJAttrValue<T>{
		private T value;
		private final MJReadWriteLock lock;
		MJAttrConcurrentValue(){
			value = null;
			lock = new MJReadWriteLock();
		}
		
		@Override
		public void set(T v) {
			try{
				lock.writeLock();
				this.value = v;
			}finally{
				lock.writeUnlock();
			}
		}

		@Override
		public T get() {
			try{
				lock.readLock();
				return value;	
			}finally{
				lock.readUnlock();
			}
		}
	}
}
