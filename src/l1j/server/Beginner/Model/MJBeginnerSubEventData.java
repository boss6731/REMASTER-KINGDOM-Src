package l1j.server.Beginner.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import l1j.server.MJTemplate.MJRnd;
import l1j.server.server.model.Instance.L1PcInstance;

class MJBeginnerSubEventData {
	private MJBeginnerGerengEventData gereng;
	
	HashMap<Integer, MJBeginnerSubEventListener> subEventModels(){
		HashMap<Integer, MJBeginnerSubEventListener> subEventModels = new HashMap<>(2);
		if(hasGereng()){
			Collections.sort(gereng.rewards);
			subEventModels.put(gereng.questId(), gereng);
		}
		return subEventModels;
	}
	
	boolean hasGereng(){
		return gereng != null;
	}
	
	MJBeginnerGerengEventData gereng(){
		return gereng;
	}
	
	static class MJBeginnerSubEventExpReward implements Comparable<MJBeginnerSubEventExpReward>{
		private int highLevel;
		private double percent;
		private boolean usePenalty;
		
		int highLevel(){
			return highLevel;
		}
		
		double percent(){
			return percent;
		}
		
		boolean usePenalty(){
			return usePenalty;
		}

		@Override
		public int compareTo(MJBeginnerSubEventExpReward o) {
			return Integer.compare(highLevel, o.highLevel);
		}
	}
	
	static abstract class MJBeginnerSubEventListener{
		abstract int questId();
		abstract void onQuest(L1PcInstance pc, int questId);
	}
	
	static class MJBeginnerGerengEventData extends MJBeginnerSubEventListener{
		private int questId;
		private int gerengId;
		private int crystalBallId;
		private int crystalBallLimit;
		private int pocketId;
		private long pocketClickDelayMillis;
		private int soulFragmentId;
		private int soulFragmentAmount;
		private ArrayList<MJBeginnerSubEventExpReward> rewards;
		private HashMap<Integer, MJCrystalBallProviderData> crystalBallProviders;

		int gerengId(){
			return gerengId;
		}
		
		@Override
		int questId(){
			return questId;
		}
		
		int crystalBallId(){
			return crystalBallId;
		}
		
		int crystalBallLimit(){
			return crystalBallLimit;
		}
		
		int pocketId(){
			return pocketId;
		}
		
		long pocketClickDelayMillis(){
			return pocketClickDelayMillis;
		}
		
		int soulFragmentId(){
			return soulFragmentId;
		}
		
		int soulFragmentAmount(){
			return soulFragmentAmount;
		}
		
		ArrayList<MJBeginnerSubEventExpReward> rewards(){
			return rewards;
		}
		
		MJCrystalBallProviderData crystalBallProviders(int monsterNpcId){
			return crystalBallProviders.get(monsterNpcId);
		}
		
		@Override
		void onQuest(L1PcInstance pc, int questId) {
			new MJBeginnerSubEventHandlers.MJBeginnerGerengEventHandler(pc);
		}
		
		static class MJCrystalBallProviderData{
			private int suppliersNpc;
			private double probability;
			private int suppliesMinimum;
			private int suppliesMaximum;
			
			int suppliersNpc(){
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
			
			boolean selectProbability(){
				return probability() >= 1D || MJRnd.isWinning(1000000, (int)(probability() * 1000000));
			}
			
			int collectItemsCount(){
				return suppliesMinimum() == suppliesMaximum() ? 
						suppliesMinimum() : MJRnd.next(suppliesMinimum(), suppliesMaximum());
			}
		}
	}
}
