package l1j.server.MJWebServer.Dispatcher.my.page.command;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import l1j.server.MJTemplate.MJString;

public class MJMyPageGmCommandModel {
	String title;
	String targetAccount;
	String targetCharacterName;
	List<MyGmCommandDataModel> dataItems;
	
	MJMyPageGmCommandModel(){
		this.title = MJString.EmptyString;
		this.targetAccount = MJString.EmptyString;
		this.targetCharacterName = MJString.EmptyString;
		this.dataItems = new LinkedList<>();
	}
	
	MJMyPageGmCommandModel(String title){
		this.title = title;
		this.dataItems = Collections.emptyList();
	}
	
	static class MyGmCommandDataModel{
		String className;
		String key;
		String value;
		MyGmCommandDataModel(){
		}
		
		MyGmCommandDataModel(String className, String key, String value){
			this.className = className;
			this.key = key;
			this.value = value;
		}
		
		MyGmCommandDataModel(String className, String key, int value){
			this(className, key, String.valueOf(value));
		}
		
		MyGmCommandDataModel(String className, String key, Object value){
			this(className, key, String.valueOf(value));
		}
	}
	
	static class MyGmCommandDataInvModel extends MyGmCommandDataModel{
		int objectId;
		boolean hasButton;
		MyGmCommandDataInvModel(int objectId, String className, String key, String value, boolean hasButton){
			super(className, key, value);
			this.objectId = objectId;
			this.hasButton = hasButton;
		}
		
		MyGmCommandDataInvModel(int objectId, String className, String key, int value, boolean hasButton){
			this(objectId, className, key, String.valueOf(value), hasButton);
		}
		
		MyGmCommandDataInvModel(int objectId, String className, String key, Object value, boolean hasButton){
			this(objectId, className, key, String.valueOf(value), hasButton);
		}
	}
}
