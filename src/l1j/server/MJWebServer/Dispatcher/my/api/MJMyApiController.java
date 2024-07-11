package l1j.server.MJWebServer.Dispatcher.my.api;

import l1j.server.MJWebServer.Dispatcher.my.MJMyController;
import l1j.server.MJWebServer.Dispatcher.my.MJMyJsonModel;
import l1j.server.MJWebServer.Dispatcher.my.MJMyModel;
import l1j.server.MJWebServer.Dispatcher.my.resource.MJMyResource;
import l1j.server.MJWebServer.Service.MJHttpRequest;

abstract class MJMyApiController extends MJMyController{

	protected MJMyApiController(MJHttpRequest request) {
		super(request);
	}

	@Override
	protected MJMyModel needLogin(){
		return redirect(MJMyResource.construct().auth().authUri());
	}
	
	@Override
	protected MJMyModel redirect(String uri){
		MJMyApiModel model = new MJMyApiModel();
		model.code = MJMyApiModel.REDIRECT;
		model.nextLocation = uri;
		return new MJMyJsonModel(request(), model, null);
	}
	
	protected MJMyModel failModel(String message){
		MJMyApiModel model = new MJMyApiModel();
		model.code = MJMyApiModel.FAILURE;
		model.message = message;
		return new MJMyJsonModel(request(), model, null);
	}
	
	@Override
	public MJMyModel viewModel() {
		if(!post()){
			return notFound();
		}
		if(!containsContentType("application/x-www-form-urlencoded")){
			return notFound();
		}
		if(emptyReferer()){
			return notFound();
		}
		if(isNeedLogin() && !loggedIn()){
			return needLogin();
		}
		
		MJMyModel model = responseModel();
		return model == null ? 
				notFound() : model;
	}
	
	protected abstract boolean isNeedLogin();
	protected abstract MJMyModel responseModel();
	
}
