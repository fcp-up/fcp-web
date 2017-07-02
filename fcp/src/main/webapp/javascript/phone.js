$('#phoneList').gridpanel({
	url: 'list',
	tpl: [
		'<tr nid="{id}">',
			'<td class="check-column"><input class="checkbox" type="checkbox" /></td>',
			'<td class="text-center">',
				'<span class="anchor first" handler="mod">修改</span>',
				'<span class="anchor" handler="del">删除</span>',
			'</td>',
			'<td>{_sequence_}</td>',
			'<td class="gridEditor"><input disabled name="phoneNo" value="{phoneNo}"></td>',
		'</tr>'
	].join(''),
	add: function(el){
		var ipt = this.appendRecord({_sequence_: this.getTableEl().children().children('tr').length}).find('input').disable(false).filter(':eq(1)');
		ipt.focus();
	},
	mod: function(el){
		var ipt = el.parentsUntil(this.el, 'tr').find('input[type!="checkbox"]').disable(false).first();
		ipt.focus().val(ipt.val());
	},
	save: function(el){
		var ipt, name, oid, upds = [], adds = [];
		this.getTableEl().children().children('tr').each(function(i, tr){
			tr = $(tr);
			if(tr.hasClass('column-header')) return;
			ipt = tr.find('input');
			
			oid = tr.attr('nid') || '';
			name = ipt.filter('[name="phoneNo"]').val().trim();
			
			if(/^\s*$/.test(name)) return;
			
			if(oid == '') {
				adds.push({phoneNo: name});
			}
			else {
				upds.push({tag: {id: oid}, obj: {phoneNo: name}});
			}
			
		});
		if(upds.length < 1 && adds.length < 1) return;
		
		ipt = true;
		name = true;
		
		this.el.mask().progress('数据处理中...');
		if(upds.length > 0) {
			ipt = false;
			$.ajax({
				url: 'list', type: 'put', dataType:'json', context: this, data: tools.serializeParams(upds), success: function(){
					ipt = true;
					if(name) {
						this.loadData();
					}
				}
			});
		}
		if(adds.length > 0) {
			name = false;
			$.ajax({
				url: 'list', type: 'post', dataType:'json', context: this, data: tools.serializeParams(adds), success: function(){
					name = true;
					if(ipt) {
						this.loadData();
					}
				}
			});
		}
	},
	del: function(el){
		var tr = el.parent().parent();
		if(!tr.attr('nid')) {
			tr.remove();
		}
		else {
			this._del([el.parent().parent().attr('nid')]);
		}
	},
	del_batch: function(el){
		var nids = [];
		this.el.find('tr.selected').each(function(i, tr){
			nids.push($(tr).attr('nid'));
		});
		this._del(nids);
	},
	_del: function(nids){
		nids = nids || [];
		if(nids.length < 1) return this;
		
		tools.msgbox({title: '删除电话', msg: '是否确认删除电话？', scope: this, negative: function(){}, positive: function(){
			var map = {};
			$.each(nids, function(i, nid){map[nid] = 1});
			
			var es = [];
			$.each(ledger.dutyList, function(i, r){
				if(!map[r.nid]) {
					es.push(r);
				}
			});
			
			this.loadData(ledger.dutyList = es);
		}});
		
		return this;
	},
	keydown: function(evt) {
		var el = $(evt.target);
		if(/^input$/i.test(el.nodeName()) && el.attr('name') == 'phoneNo') {
			switch(evt.which) {
			case 13:
				this.save();
				evt.preventDefault();
				break;
			case 9:
				this.add();
				evt.preventDefault();
				break;
			}
		}
	}
})//.loadData();