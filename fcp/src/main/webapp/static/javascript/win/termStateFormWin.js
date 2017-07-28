window['fcp'] = window['fcp'] || {};
fcp.termStateFormWin = $('#termStateFormWin').formwin({
	saveUrl: 'terminal/postOnline',
	fixedPostValue: function(obj){return [obj];}
});