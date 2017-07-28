window['fcp'] = window['fcp'] || {};
fcp.terminalFormWin = $('#terminalFormWin').formwin({
	saveUrl: '../terminal',
	fixedPostValue: function(obj){
		if(obj !== false) {
			obj.name = obj.name || '终端--' + obj.no;
		}
		return obj;
	}
});