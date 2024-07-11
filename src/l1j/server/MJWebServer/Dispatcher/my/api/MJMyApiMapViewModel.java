package l1j.server.MJWebServer.Dispatcher.my.api;

import java.util.List;

import l1j.server.MJWebServer.Dispatcher.my.service.gmtools.MJMyGmModel;
import l1j.server.MJWebServer.Dispatcher.my.service.mapview.MJMyMapViewTileInfo;

class MJMyApiMapViewModel extends MJMyApiModel{
	MJMyMapViewTileInfo tile;
	List<MJMyGmModel> objects;
	MJMyApiMapViewModel(){
		super();
	}
}
