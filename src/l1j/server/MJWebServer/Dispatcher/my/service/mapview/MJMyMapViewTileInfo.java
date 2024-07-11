package l1j.server.MJWebServer.Dispatcher.my.service.mapview;

import l1j.server.server.datatables.MapsTable;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;

public class MJMyMapViewTileInfo {
	static MJMyMapViewTileInfo newMap(int mapId){
		L1Map m = L1WorldMap.getInstance().getMap((short)mapId);
		MapsTable table = MapsTable.getInstance();
		
		MJMyMapViewTileInfo vInfo = new MJMyMapViewTileInfo();
		vInfo.mapId = mapId;
		vInfo.startX = table.getStartX(mapId);
		vInfo.startY = table.getStartY(mapId);
		vInfo.endX = table.getEndX(mapId);
		vInfo.endY = table.getEndY(mapId);
		vInfo.width = m.getWidth();
		vInfo.height = m.getHeight();
		vInfo.centerX = vInfo.width / 2;
		vInfo.centerY = vInfo.height / 2;
		return vInfo;
	}
	
	private int mapId;
	private int startX;
	private int startY;
	private int endX;
	private int endY;
	private int width;
	private int height;
	private int centerX;
	private int centerY;
	private MJMyMapViewTileInfo(){
	}
	
	public int mapId(){
		return mapId;
	}
	
	public int startX(){
		return startX;
	}
	
	public int startY(){
		return startY;
	}
	
	public int endX(){
		return endX;
	}
	
	public int endY(){
		return endY;
	}
	
	public int width(){
		return width;
	}
	
	public int height(){
		return height;
	}	
	
	public int centerX(){
		return centerX;
	}
	
	public int centerY(){
		return centerY;
	}
}
