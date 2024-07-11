package l1j.server.MJTemplate.Attribute;

import java.util.HashMap;

public class MJAttrHashMap implements MJAttrMap{
	private HashMap<MJAttrKey<?>, MJAttrValue<?>> attributes = new HashMap<>();

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
	
	static class AttrHashValue<T> implements MJAttrValue<T>{
		private T value;
		AttrHashValue(){
			value = null;
		}
		
		@Override
		public void set(T v) {
			this.value = v;
		}

		@Override
		public T get() {
			return value;
		}	
	}

}
