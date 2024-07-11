package l1j.server.MJWebServer.Dispatcher.my.service.mapview;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import l1j.server.MJTemplate.MJFiles;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;

class MJMyMapViewTileProvider {
	private static final String storePath = "./appcenter/my/maps";
	private static final MJMyMapViewTileProvider provider = new MJMyMapViewTileProvider();
	static MJMyMapViewTileProvider provider(){
		return provider;
	}
	
	private ConcurrentHashMap<Integer, MJMyMapViewTileInfo> cachedMaps;
	private MJMyMapViewTileProvider(){	
		cachedMaps = new ConcurrentHashMap<>();
	}
	
	MJMyMapViewTileInfo mapView(int mapId){
		MJMyMapViewTileInfo vInfo = cachedMaps.get(mapId);
		if(vInfo != null){
			return vInfo;
		}
		vInfo = MJMyMapViewTileInfo.newMap(mapId);
		cachedMaps.put(mapId, vInfo);
		try {
			writeMap(vInfo);
		} catch (Exception e) {
			System.out.println(String.format("mapid : %d", mapId));
			e.printStackTrace();
		}
		return vInfo;
	}
	
	void writeMap(MJMyMapViewTileInfo vInfo) throws IOException{
		L1Map map = L1WorldMap.getInstance().getMap((short)vInfo.mapId());
		int width = vInfo.width();
		int height = vInfo.height();
		int sx = vInfo.startX();
		int sy = vInfo.startY();
		int cx = vInfo.centerX();
		BufferedImage img = new BufferedImage((int)(width + (width * 0.5)), (int)(width + (width * 0.5)), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = img.createGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		g.dispose();
		int through = throughColor();
		int nonThrough = nonThroughColor();
		
		for(int x=0; x<width; ++x){
			int offsetX = (x + 1) / 2;
			int offsetY = cx - (x > 0 ? x / 2 : 0);
			
			for(int y=0; y<height; ++y){
				int drawX = offsetX + y;
				int drawY = offsetY + y;
				if(map.isPassable(x + sx, y + sy)){
					img.setRGB(drawX, drawY, through);
				}else{
					img.setRGB(drawX, drawY, nonThrough);
				}
			}
		}
		checkDirectory();
		String path = String.format("%s/%d.png", storePath, vInfo.mapId());
		ImageIO.write(img, "PNG", new File(path));
	}
	
	private int nonThroughColor(){
		Color c = new Color(0, 160, 0);
		return c.getRGB();
	}
	
	private int throughColor(){
		return Color.WHITE.getRGB();
	}
	
	private void checkDirectory(){
		MJFiles.createDirectory(storePath);
	}
}
