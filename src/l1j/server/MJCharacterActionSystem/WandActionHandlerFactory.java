package l1j.server.MJCharacterActionSystem;


import l1j.server.MJCharacterActionSystem.Wand.WandActionHandler;
import l1j.server.server.model.Instance.L1ItemInstance;

public class WandActionHandlerFactory {
	public static WandActionHandler create(L1ItemInstance item){
		switch(item.getItemId()){
			case 40007:
			case 40412:
				return new WandActionHandler(item, 10, 0.16D);
			case 40006:
			case 140006:
				return new WandActionHandler(item, 11736, 0.5D);
			case 420104:
				return new WandActionHandler(item, 762, 0.5D);
			case 420108:
				return new WandActionHandler(item, 16060, 0.5D);
			case 420111:
				return new WandActionHandler(item, 3924, 0.5D);

		}
		return null;
		}
	
/*	public static AreaWandActionHandler create(L1ItemInstance item){
		switch(item.getItemId()){
			case 420103:
			case 420104:
			case 420105:
			case 420106:
			case 420107:
			case 420108:
			case 420109:
			case 420110:
			case 420111:
				return new  AreaWandActionHandler(item, 11736, 100);
		}
		return null;
	}*/
}
			
			
