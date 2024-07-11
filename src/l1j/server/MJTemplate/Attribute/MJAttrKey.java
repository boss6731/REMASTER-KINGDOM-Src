package l1j.server.MJTemplate.Attribute;

/**
 * <b>表示屬性映射的鍵.</b>
 * @author mjsoft
 * @see MJAttrMap
 * @see MJAttrValue
 **/
public class MJAttrKey<T> implements Comparable<MJAttrKey<T>>{


	
	
	/**
	 * 產生金鑰.
	 * @param name 鍵的名稱字串
	 **/
	public static <T> MJAttrKey<T> newInstance(String name){
		return new MJAttrKey<T>(name.hashCode(), name);
	}
	
	
	
	
	int id;
	String name;
	private MJAttrKey(int id, String name){
		this.id = id;
		this.name = name;
	}


	
	
	/**
	 * {@inheritDoc}
	 **/
	@Override
	public int hashCode(){
		return id;
	}


	
	
	/**
	 * {@inheritDoc}
	 **/
	@Override
	public int compareTo(MJAttrKey<T> o) {
		if(this == o){
			return 0;
		}
		
		return id - o.id;
	}


	
	
	/**
	 * {@inheritDoc}
	 **/
	@Override
	public boolean equals(Object o){
		if(this == o){
			return true;
		}
		
		if(hashCode() != o.hashCode() || !(o instanceof MJAttrKey)){
			return false;
		}
		return true;
	}


	
	
	/**
	 * {@inheritDoc}
	 **/
	@Override
	public String toString(){
		return new StringBuilder(32 + name.length())
				.append("[MJAttrKey ")
				.append("id : ")
				.append(id)
				.append(", name : ")
				.append(name)
				.append("]")
				.toString();
	}
}
