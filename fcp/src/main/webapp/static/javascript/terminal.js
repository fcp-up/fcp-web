window['fcp'] = window['fcp'] || {};
fcp.terminalList = $('#terminalList').gridpanel({
	data: {},
	url: 'list',
	tpl: [
		'<tr nid="{no}">',
			'<td class="x-check-column"><input class="x-checkbox" type="checkbox" /></td>',
			'<td class="x-text-center">{_sequence_}</td>',
			'<td class="x-widen">{no}</td>',
			'<td class="x-widen">{name}</td>',
			'<td class="x-widen">{alarmPhone}</td>',
			'<td class="x-text-center x-widen{stateCls}">{stateStr}</td>',
			'<td class="x-widen">{longitude}</td>',
			'<td class="x-widen">{latitude}</td>',
			'<td class="x-widen">{address}</td>',
			'<td class="x-widen">{lastOnlineTime}</td>',
		'</tr>'
	].join(''),
	clean: function(){
		this.data = {};
		this.renderData([]);
	},
	fixedRecord: function(r){
		this.data[r.no] = r;
		var online = r.lastOnlineState == 1;
		r.stateStr = online ? '在线' : '离线';
		r.stateCls = online ? ' x-color-green' : ' x-color-red';
		r.lastOnlineTime = r.lastOnlineTime && new Date(r.lastOnlineTime).format() || '';
		return r;
	},
	update: function(el){
		var no = this.el.find('tr.x-selected:first').attr('nid');
		if(!no) return;
		fcp.terminalFormWin.saveSuccess = function(){fcp.terminalList.loadData();};
		fcp.terminalFormWin.loadRecord(this.data[no]).disable({no: true}).open().setSaveUrl('../terminal').form.name.focus();
	},
	add: function(el){
		fcp.terminalFormWin.saveSuccess = function(){fcp.terminalList.loadData();};
		fcp.terminalFormWin.open().setSaveUrl('../terminal').form.no.focus();
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
		
		tools.msgbox({title: '删除终端', msg: '是否确认删除终端？', scope: this, negative: function(){}, positive: function(){
			this.el.mask().progress('数据处理中...');
			$.ajax({
				url: '../terminal', type: 'delete', context: this, data: tools.serializeParams({noList: nids}), dataType:'json',
				success: function(){
					this.loadData();
				},
				error: function(){
					this.el.unprogress().unmask();
					tools.msgbox({title: '删除终端', msg: '删除终端失败。', positive: $.emptyFn});
				}
			});
		}});
		
		return this;
	},
	setPhone: function(el){
		var nid = this.el.find('tr.x-selected:first').attr('nid');
		if(!nid) {
			tools.msgbox({title: '设置终端报警电话', msg: '请选中一个终端', positive: $.emptyFn});
			return;
		}
		fcp.termPhoneFormWin.saveSuccess = function(){fcp.terminalList.loadData();};
		var win = fcp.termPhoneFormWin.open().setSaveUrl('alarmPhone');
		win.form.terminalNo.val(nid);
		win.form.phoneNo.focus();
	},
	tstOnline: function(){
		var nid = this.el.find('tr.x-selected:first').attr('nid');
		if(!nid) {
			tools.msgbox({title: '终端上下线测试', msg: '请选中一个终端', positive: $.emptyFn});
			return;
		}
		fcp.termStateFormWin.saveSuccess = function(){fcp.terminalList.loadData();};
		var win = fcp.termStateFormWin.open().setSaveUrl('postOnline');
		win.form.terminalNo.val(nid);
	}
}).loadData();