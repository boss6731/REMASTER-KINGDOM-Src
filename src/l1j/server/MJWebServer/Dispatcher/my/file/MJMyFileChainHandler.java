package l1j.server.MJWebServer.Dispatcher.my.file;

import l1j.server.MJTemplate.MJEncoding;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheConverter;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheModel;
import l1j.server.MJWebServer.Dispatcher.cache.monitor.MJMonitorCacheProvider;
import l1j.server.MJWebServer.Dispatcher.my.MJMyController;
import l1j.server.MJWebServer.Dispatcher.my.MJMyUriChainHandler;
import l1j.server.MJWebServer.Dispatcher.my.file.MJMyFilesTemplate.MJMyFilesDirectoryInfo;
import l1j.server.MJWebServer.Dispatcher.my.file.MJMyFilesTemplate.MJMyFilesFileInfo;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.utils.FileUtil;

public class MJMyFileChainHandler implements MJMyUriChainHandler{
	private MJMyFilesTemplate template;
	public MJMyFileChainHandler(){
		MJMonitorCacheModel<MJMyFilesTemplate> model = 
				MJMonitorCacheProvider.newJsonFileCacheModel("mj-my-files", "./appcenter/my/json/mj-files.json", MJMyFilesTemplate.class, MJEncoding.MS949);
		model.cacheListener(new MJMyFileConveter());
		MJMonitorCacheProvider.monitorCache().appendCacheModel(model);
	}

	@Override
	public MJMyController onRequest(MJHttpRequest request, String requestUri) {
		if(template == null){
			return null;
		}
		
		for(MJMyFilesDirectoryInfo dInfo : template.directories){
			if(!requestUri.startsWith(dInfo.uri)){
				continue;
			}
			String fileName = FileUtil.getFileName(requestUri);
			if(MJString.isNullOrEmpty(fileName)){
				continue;
			}
			if(dInfo.effectiveFiles != null && dInfo.effectiveFiles.size() > 0){
				for(MJMyFilesFileInfo fInfo : dInfo.effectiveFiles){
					if(fInfo.fileName.equals(fileName)){
						return new MJMyFileController(request, String.format("./appcenter%s", requestUri), fileName, fInfo.maxAge, fInfo.needLogin);
					}
				}
			}
			return new MJMyFileController(request, String.format("./appcenter%s", requestUri), fileName, dInfo.maxAge, dInfo.needLogin);
		}
		return null;
	}
	
	private class MJMyFileConveter implements MJMonitorCacheConverter<MJMyFilesTemplate>{
		@Override
		public MJMyFilesTemplate onNewCached(MJMyFilesTemplate t, long modifiedMillis) {
			template = t;
			return t;
		}
		
	}
}
