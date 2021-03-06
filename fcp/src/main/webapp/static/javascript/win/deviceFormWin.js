window['fcp'] = window['fcp'] || {};
fcp.deviceFormWin = $('#deviceFormWin').formwin({
	saveUrl: 'device',
	loadRecord: function(rcd){
		$.FormWindow.prototype.loadRecord.apply(this, arguments);
		this.oldTerminalNo = rcd.terminalNo;
		return this;
	},
	slctTerm: function(el){
		var cbl = $('#combolist');
		cbl.setClass('x-show x-option').position({my: 'left top', at: 'left bottom+1', of: el}).
		css({height: 62, width: 200}).html('').mask().progress('数据加载中');
		tools.CBH = {
			scope: this,
			tag: el,
			elclick: function(el, h, evt){
				if(evt = el.attr('no')) {
					this.form.terminalName.val(el.html());
					this.form.terminalNo.val(el.attr('no'));
				}				
			}
		};
		$.ajax({
			url: '/fcp/terminal/list', type: 'get', dataType: 'json', context: this,
			success: function(rs) {
				rs = rs || {};
				rs = rs.data || [];
				var b, 
					tpl = '<div class="x-item" no="{no}">{name}</div>', 
					hs = [];
				
				$.each(rs || [], function(i, r){
					r.name = (r.name || '终端') + '-' + r.no;
					hs.push(tpl.replace(/{(\w+)}/g, function(a, b){return r[b] == null ? '' : r[b];}));
				});
				cbl.html(hs.join(''));
				hs = document.body.clientHeight - cbl.offset().top - 10;
				$.defer(0.05, function(){
					cbl.css('height', cbl.scrollHeight() > hs ? hs : 'auto');
				});
			},
			error: function(){
				tools.msgbox({msg: '数据加载失败', positive: $.emptyFn});
				cbl.unprogress().unmask();
			}
		});
	},
	fixedPostValue: function(obj){
		if(obj === false) return false;
		
		obj.name = obj.name || '设备--' + obj.no;
		if(this.form.no.readOnlyed() || this.form.no.disabled()) {
			obj = {
				tag: {no: obj.no},
				obj: obj
			};
			if(this.oldTerminalNo) obj.tag.terminalNo = this.oldTerminalNo;
			delete obj.obj.no;
		}
		return obj;
	},
	getRequestType: function(){
		return (this.form.no.readOnlyed() || this.form.no.disabled()) ? 'put' : 'post';
	}
});