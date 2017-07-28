window['fcp'] = window['fcp'] || {};
fcp.termPhoneFormWin = $('#termPhoneFormWin').formwin({
	saveUrl: 'terminal/alarmPhone',
	fixedPostValue: function(obj){return [obj];}
});