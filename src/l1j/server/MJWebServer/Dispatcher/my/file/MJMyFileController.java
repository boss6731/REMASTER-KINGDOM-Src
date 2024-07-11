package l1j.server.MJWebServer.Dispatcher.my.file;

import MJFX.MJFxEntry;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import l1j.server.MJTemplate.MJFiles;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJWebServer.Dispatcher.MJHttpClosedException;
import l1j.server.MJWebServer.Dispatcher.my.MJMyBinaryModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyController;
import l1j.server.MJWebServer.Dispatcher.my.MJMyHeaderSetter;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Service.MJHttpRequest;
import l1j.server.server.utils.FileUtil;

class MJMyFileController extends MJMyController{

	private String filePath;
	private int maxAge;
	private boolean needLogin;
	private String contentType;
	protected MJMyFileController(MJHttpRequest request, String filePath, String fileName, int maxAge, boolean needLogin) {
		super(request);
		this.filePath = filePath;
		this.maxAge = maxAge;
		this.needLogin = needLogin;
		this.contentType = convertContentType(FileUtil.getExtension(fileName));
	}

	@Override
	public MJMyModel viewModel() throws MJHttpClosedException {
		if(needLogin && !loggedIn()){
			return needLogin();
		}
		
		if(emptyReferer() && !filePath.contains("/my/img/icon_64_dark.ico")){
			return notFound();
		}
		
		byte[] data = MJFiles.readAllBytes(filePath);
		if(data == null){
			return notFound();
		}
		return new MJMyBinaryModel(request(), data, contentType, new MJMyHeaderSetter(){
			@Override
			public void onHeaderSet(HttpResponse response) {
				if(!MJFxEntry.IS_DEBUG_MODE && maxAge > 0){
					response.headers().set(HttpHeaderNames.CACHE_CONTROL, String.format("max-age=%d", maxAge));
				}
			}
		});
	}
	
	private String convertContentType(String extension){
		if(!MJString.isNullOrEmpty(extension)){
			switch(extension){
			case "woff":
			case "woff2":
				return "application/x-font-woff";
			case "jpg":
			case "jpeg":
				return "image/jpeg";
			case "png":
				return "image/png";
			case "gif":
				return "image/gif";
			case "bmp":
				return "image/bmp";
			case "js":
				return "text/javascript; charset=utf-8";
			case "css":
				return "text/css; charset=utf-8";
			}
		}
		return MJString.EmptyString;
	}
}
