package l1j.server.MJTemplate.ObjectEvent;

public class MJObjectEventProvider {
	private static final MJObjectEventProvider provider = new MJObjectEventProvider();
	public static MJObjectEventProvider provider(){
		return provider;
	}
	
	private MJNpcEventFactory npcEventFactory;
	private MJMonsterEventFactory monsterEventFactory;
	private MJInventoryEventFactory inventoryEventFactory;
	private MJPcEventFactory pcEventFactory;
	private MJObjectEventProvider(){
		npcEventFactory = new MJNpcEventFactory();
		monsterEventFactory = new MJMonsterEventFactory();
		inventoryEventFactory = new MJInventoryEventFactory();
		pcEventFactory = new MJPcEventFactory();
	}
	
	public MJObjectEventHandler newHandler(){
		return new MJObjectEventHandler();
	}
	
	public MJNpcEventFactory npcEventFactory(){
		return npcEventFactory;
	}
	
	public MJMonsterEventFactory monsterEventFactory(){
		return monsterEventFactory;
	}
	
	public MJInventoryEventFactory inventoryEventFactory(){
		return inventoryEventFactory;
	}
	
	public MJPcEventFactory pcEventFactory(){
		return pcEventFactory;
	}
}
