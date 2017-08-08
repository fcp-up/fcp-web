window['fcp'] = window['fcp'] || {};
fcp.deviceAlarmList = $('#deviceAlarmList').gridpanel({
	url: '/fcp/alarm/deviceAlarm/list',
	tpl: [
		'<tr nid="{alarmId}">',
			'<td>{_sequence_}</td>', 
			'<td>{deviceNo}</td>', 
			'<td>{deviceAddr}</td>', 
			'<td>{terminalNo}</td>', 
			'<td>{terminalAddr}</td>', 
			'<td>{deviceState}</td>', 
			'<td>{time}</td>',
		'</tr>'
	].join(''),
	fixedRecord: function(r){
		if(r.time != null) r.time = new Date(r.time).format();
		return r;
	},
	udfInit: function(){
		var f = this.el.find('[_type="date"]');
		f.datetimepicker('remove');
		f.datetimepicker({format: 'yyyy-mm-dd', minView: 2}).readOnly(true).next('span.x-icon-datepicker').click(function(){
			$(this).prev().focus();
		});
	},
	getParams: function(){
		var p = {}, f = this.el.find('[_type="date"]'), x;
		
		x = f.filter('[name="startTime"]').val().split('-');
		if(x) p.startTime = new Date(x[0], x[1], x[2], 0, 0, 0).getTime();
		x = f.filter('[name="endTime"]').val().split('-');
		if(x) p.endTime = new Date(x[0], x[1], x[2], 0, 0, 0).getTime();
		
		return p;
	},
	fixParams: function(p){
		return tools.serializeParams(p);
	},
	clean: function(){
		this.renderData([]);
	}
}).loadData();