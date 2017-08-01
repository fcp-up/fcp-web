window['fcp'] = window['fcp'] || {};
fcp.deviceList = $('#deviceList').gridpanel({
	url: 'list',
	tpl: [
		'<tr nid="{no}" term="{terminalNo}">',
			'<td class="x-check-column"><input class="x-checkbox" type="checkbox" /></td>',
			'<td class="x-text-center">{_sequence_}</td>',
			'<td class="x-widen">{no}</td>',
			'<td class="x-widen">{name}</td>',
			'<td class="x-widen">{terminalNo}</td>',
			'<td class="x-widen">{alarmPhone}</td>',
			'<td class="x-widen">{longitude}</td>',
			'<td class="x-widen">{latitude}</td>',
			'<td class="x-widen">{lastAlarmTime}</td>',
			'<td class="x-widen">{address}</td>',
		'</tr>'
	].join(''),
	fixedRecord: function(r){
		r.lastAlarmTime = r.lastAlarmTime && new Date(r.lastAlarmTime).format() || '';
		return r;
	},
	add: function(el){
		fcp.deviceFormWin.saveSuccess = function(){fcp.deviceList.loadData();};
		fcp.deviceFormWin.open().setSaveUrl('../device').form.no.focus();
	},
	del: function(el){
		var tr = el.parent().parent(), nid = tr.attr('nid');
		if(nid) {
			this._del([nid]);
		}
		else {
			tr.remove();
		}
	},
	del_batch: function(el){
		var nids = [];
		this.el.find('tr.x-selected').each(function(i, tr){
			nids.push($(tr).attr('nid'));
		});
		this._del(nids);
	},
	_del: function(nids){
		nids = nids || [];
		if(nids.length < 1) return this;
		
		tools.msgbox({title: '删除设备', msg: '是否确认删除设备？', scope: this, negative: function(){}, positive: function(){
			this.el.mask().progress('数据处理中...');
			$.ajax({
				url: '../device', type: 'delete', context: this, data: tools.serializeParams({noList: nids}), dataType:'json',
				success: function(){
					this.loadData();
				},
				error: function(){
					this.el.unprogress().unmask();
					tools.msgbox({title: '删除设备', msg: '删除设备失败。', positive: $.emptyFn});
				}
			});
		}});
		
		return this;
	},
	tstAlarm: function(){
		var nid = this.el.find('tr.x-selected:first');
		if(!nid.attr('nid')) {
			tools.msgbox({title: '设备报警测试', msg: '请选中一个设备', positive: $.emptyFn});
			return;
		}
		fcp.alarmFormWin.saveSuccess = function(){fcp.deviceList.loadData();};
		var win = fcp.alarmFormWin.open().setSaveUrl('../alarm/deviceAlarm');
		win.form.deviceNo.val(nid.attr('nid'));
		win.form.terminalNo.val(nid.attr('term'));
	}
}).loadData();