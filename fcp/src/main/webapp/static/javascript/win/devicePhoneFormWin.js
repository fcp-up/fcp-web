window['fcp'] = window['fcp'] || {};
fcp.devicePhoneFormWin = $('#devicePhoneFormWin').formwin({
	saveUrl: 'device/alarmPhone',
	fixedPostValue: function(obj){return [obj];}
});