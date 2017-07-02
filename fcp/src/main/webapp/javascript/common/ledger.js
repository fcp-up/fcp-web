window.ledger = window.ledger || {};

ledger.DutyGrid = $.extendClass($.GridWindow, {
	tpl: [
		'<tr nid="{nid}">',
			'<td class="check-column"><input class="checkbox" type="checkbox" /></td>',
			'<td class="text-center">',
				'<span class="anchor first" handler="mod">修改</span>',
				'<span class="anchor" handler="del">删除</span>',
			'</td>',
			'<td>{_sequence_}</td>',
			'<td class="gridEditor"><input disabled name="nid" value="{nid}"></td>',
			'<td class="gridEditor"><input disabled name="name" value="{name}"></td>',
		'</tr>'
	].join(''),
	loadData: function(){
		var cb, cs, cp;
		$.each(arguments, function(i, a){
			if($.isFunction(a) && a != $) {
				if(cb == null) cb = a;
				else if(cs == null) cs = a;
			}
			else if($.isArray(a)) {
				if(cp == null) cp = a;
				else if(cs == null) cs = a;
			}
			else if(cs == null) cs = a;
		});
		cb = cb || $.emptyFn;
		cp = cp || [];
		
		this.renderData(ledger.dutyList);
		
		cp.push(true, ledger.dutyList);
		cb.apply(cs || this, cp);
		
		if($.isFunction(this.afterLoadData)) this.afterLoadData();
		
		return this;
	},
	add: function(el){
		var ipt = this.appendRecord({_sequence_: this.getTableEl().children().children('tr').length}).find('input').disable(false).filter(':eq(1)');
		ipt.focus();
	},
	mod: function(el){
		var ipt = el.parentsUntil(this.el, 'tr').find('input[type!="checkbox"]').disable(false).first();
		ipt.focus().val(ipt.val());
	},
	save: function(el){
		var ipt, nid, name, oid;
		this.getTableEl().children().children('tr').each(function(i, tr){
			tr = $(tr);
			if(tr.hasClass('column-header')) return;
			ipt = tr.find('input');
			if(ipt.filter('[name="nid"]').disabled()) return;
			
			oid = tr.attr('nid') || '';
			nid = ipt.filter('[name="nid"]').val().trim();
			name = ipt.filter('[name="name"]').val().trim();
			
			if(/^\s*$/.test(nid) || /^\s*$/.test(name)) return;
			
			if(oid == '') {
				ledger.dutyList.push({nid: nid, name: name});
			}
			else {
				$.each(ledger.dutyList, function(i, d){
					if(d.nid == oid) {
						d.nid = nid;
						d.name = name;
					}
				});
			}
			
			tr.attr('nid', nid);
			ipt.filter('input[type!="checkbox"]').disable(true);
		});
		this.loadData();
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
		
		tools.msgbox({title: '删除职务', msg: '是否确认删除职务？', scope: this, negative: function(){}, positive: function(){
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
		if(/^input$/i.test(el.nodeName()) && el.attr('name') == 'name') {
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
});

ledger.dutyEditor = new ledger.DutyGrid({el: $('#dutyListWin')}).loadData();
