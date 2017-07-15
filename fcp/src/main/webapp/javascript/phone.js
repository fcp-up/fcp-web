$('#phoneList').gridpanel({
	url: 'list',
	tpl: [
		'<tr nid="{id}">',
			'<td class="x-check-column"><input class="x-checkbox" type="checkbox" /></td>',
			'<td class="x-text-center">',
				'<span class="x-anchor x-first" handler="mod">修改</span>',
				'<span class="x-anchor" handler="del">删除</span>',
			'</td>',
			'<td>{_sequence_}</td>',
			'<td class="x-gridEditor"><input disabled name="phoneNo" value="{phoneNo}"></td>',
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
			if(tr.hasClass('x-column-header')) return;
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
		
		tools.msgbox({title: '删除电话', msg: '是否确认删除电话？', scope: this, negative: function(){}, positive: function(){
			this.el.mask().progress('数据处理中...');
			$.ajax({
				url: '../phone', type: 'delete', context: this, data: tools.serializeParams({idList: nids}), dataType:'json',
				success: function(){
					this.loadData();
				},
				error: function(){
					this.el.unprogress().unmask();
					tools.msgbox({title: '删除电话', msg: '删除电话失败。', positive: $.emptyFn});
				}
			});
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
		else {
			
		}
	}
}).loadData();