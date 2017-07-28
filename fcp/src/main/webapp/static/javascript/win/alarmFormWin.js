window['fcp'] = window['fcp'] || {};
fcp.alarmFormWin = $('#alarmFormWin').formwin({
	saveUrl: 'alarm/deviceAlarm',
	fixedPostValue: function(obj){return [obj];}
});