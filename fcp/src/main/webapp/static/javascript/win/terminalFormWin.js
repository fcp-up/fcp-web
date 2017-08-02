window['fcp'] = window['fcp'] || {};
fcp.terminalFormWin = $('#terminalFormWin').formwin({
	saveUrl: '../terminal',
	fixedPostValue: function(obj){
		if(obj === false) return false;
		
		obj.name = obj.name || '终端--' + obj.no;
		if(this.form.no.readOnlyed() || this.form.no.disabled()) {
			obj = {
				tag: {no: obj.no},
				obj: obj
			};
			delete obj.obj.no;
		}
		return obj;
	},
	getRequestType: function(){
		return (this.form.no.readOnlyed() || this.form.no.disabled()) ? 'put' : 'post';
	}
});