package l1j.server.Beginner.Model;

import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.matcher.Matcher;

class MJBeginnerCollectData{
	private HashMap<Integer, MJBeginnerCollectDataFromNpcTalk> collectsFromNpcTalks;
	private HashMap<Integer, MJBeginnerCollectDataFromMonsters> collectsFromMonsters;
	MJBeginnerCollectData(HashMap<Integer, MJBeginnerCollectDataFromNpcTalk> collectsFromNpcTalks, HashMap<Integer, MJBeginnerCollectDataFromMonsters> collectsFromMonsters){
		this.collectsFromNpcTalks = collectsFromNpcTalks;
		this.collectsFromMonsters = collectsFromMonsters;
	}
	
	boolean hasCollectsFromNpcTalks(){
		return collectsFromNpcTalks != null;
	}
	
	HashMap<Integer, MJBeginnerCollectDataFromNpcTalk> collectsFromNpcTalks(){
		return collectsFromNpcTalks;
	}
	
	boolean hasCollectsFromMonsters(){
		return collectsFromMonsters != null;
	}
	
	HashMap<Integer, MJBeginnerCollectDataFromMonsters> collectsFromMonsters(){
		return collectsFromMonsters;
	}
	
	
	static class MJBeginnerCollectDataFromNpcTalk implements Matcher<Integer>{
		private int itemAssetId;
		private int talkingNpc;
		private int suppliesCount;
		
		int itemAssetId(){
			return itemAssetId;
		}
		
		int talkingNpc(){
			return talkingNpc;
		}
		
		int suppliesCount(){
			return suppliesCount;
		}
		
		@Override
		public boolean matches(Integer i) {
			return i.intValue() == talkingNpc();
		}
	}
	
	static class MJBeginnerCollectDataFromMonsters implements Matcher<Integer>{
		private int itemAssetId;
		private ArrayList<Integer> suppliersNpc;
		private double probability;
		private int suppliesMinimum;
		private int suppliesMaximum;
		
		int itemAssetId(){
			return itemAssetId;
		}
		
		ArrayList<Integer> suppliersNpc(){
			return suppliersNpc;
		}
		
		double probability(){
			return probability;
		}
		
		int suppliesMinimum(){
			return suppliesMinimum;
		}
		
		int suppliesMaximum(){
			return suppliesMaximum;
		}

		@Override
		public boolean matches(Integer i) {
			for(Integer supplierNpcId : suppliersNpc()){
				if(i.intValue() == supplierNpcId.intValue()){
					return true;
				}
			}
			return false;
		}
		
		boolean selectProbability(){
			return probability() >= 1D || MJRnd.isWinning(1000000, (int)(probability() * 1000000));
		}
		
		int collectItemsCount(){
			return suppliesMinimum() == suppliesMaximum() ? 
					suppliesMinimum() : MJRnd.next(suppliesMinimum(), suppliesMaximum());
		}
	}
}
