package l1j.server.MJWebServer.Dispatcher.my.service.character;

import l1j.server.server.templates.L1Item;

public enum MJMyCharInvItemCategory {
	all(0, "��ü"),
	equip(1, "���"),
	etc(2, "��Ÿ"),
	;
	
	private int index;
	private String text;
	MJMyCharInvItemCategory(int index, String text){
		this.index = index;
		this.text = text;
	}
	
	public int index(){
		return index;
	}
	
	public String text(){
		return text;
	}
	
	public static MJMyCharInvItemCategory fromTemplate(L1Item item){
		if(item == null){
			return etc;
		}
		switch(item.getType2()){
		case 1:
		case 2:
			return equip;			
		}
		return etc;
	}
}
