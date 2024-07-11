package l1j.server.MJWebServer.Dispatcher.my.service.ncoin;

import java.text.DecimalFormat;

import MJNCoinSystem.MJNCoinDepositInfo;
import l1j.server.MJTemplate.MJString;

public class MJMyNcoinServiceItem {
	static MJMyNcoinServiceItem newInstance(MJNCoinDepositInfo nInfo){
		DecimalFormat _decF = new DecimalFormat("#,###");
		MJMyNcoinServiceItem item = new MJMyNcoinServiceItem();
		item.depositId = nInfo.get_deposit_id();
		item.category = nInfo.is_deposit();
		item.categoryTitle = MJMyNcoinCategory.findCategory(item.category).categoryTitle();
		item.depositName = nInfo.get_deposit_name();
		item.characterName = nInfo.get_character_name();
		item.accountName = nInfo.get_account_name();
		item.ncoin = _decF.format(nInfo.get_ncoin_value());
		item.lastModified = MJString.convert_datetime_millis(nInfo.get_generate_date());
		return item;
	}
	
	private int depositId;
	private int category;
	private String categoryTitle;
	private String depositName;
	private String characterName;
	private String accountName;
	private String ncoin;
	private long lastModified;
	MJMyNcoinServiceItem(){
		depositId = -1;
		category = -1;
		categoryTitle = MJString.EmptyString;
		lastModified = 0L;
	}
	
	public int depositId(){
		return depositId;
	}
	
	public int category(){
		return category;
	}
	
	public String categoryTitle(){
		return categoryTitle;
	}
	
	public String depositName(){
		return depositName;
	}
	
	public String characterName(){
		return characterName;
	}
	
	public String accountName(){
		return accountName;
	}
	
	public String ncoin(){
		return ncoin;
	}
	
	public long lastModified(){
		return lastModified;
	}
}
