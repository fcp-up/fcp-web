/**
 * 
 * @type 
 */
window['tools'] = window['tools'] || {};
$.copy(tools, {
	CBH: { //下拉框相关所有事件，依赖javacript/jquery-1.11.2.js javascript/ext.js
		elclick: $.emptyFn, //function(el, cbh, event){}	元素点击时执行
		onHide: $.emptyFn,	//function(cbh){}				下拉框被隐藏后执行
		onMouseOver: $.emptyFn 	//function(el){}			
	},
	/**
	 * 通过一个树形列表获取一个树的html串
	 * @param {} opt 参数配置<pre>{
	 * 	data	: [] 	树形json列表，默认为[]
	 * 	key		: '' 	主键属性，必选
	 * 	fkey	: '' 	可选的父节点关联属性
	 * 	dplkey	: '' 	展示在界面中的属性，默认为name属性
	 * 	atrs	: []	附加到html节点中的对象属性，默认为[]
	 * 	tpl		: ''|fn	
	 * 		1.可选的模板，模板供每个节点渲染调用，一个div标签，模板以{}标记一个属性，可以在模板中任意位置使用这些标记，除了对象属性标记以外，额外提供3个标记：
	 * 			__atr__:附加属性标记。
	 * 			__content__:节点内容标记，节点内容主要包括节点名称及其前面的图标、空格等。
	 * 			__children__:子节点渲染标记，子节点渲染标记包括了父节点所有子节点渲染出来的html串。
	 * 			__level__:节点级别，根节点为1。无父节点的所有节点都是根节点。
	 * 			__index__:节点序号，从0开始。
	 * 		2.可选渲染函数：function(node, tpl){}
	 * 			参数为扩展后的node，扩展node将多包括上述5个属性；tpl为默认模板，传入模板不可作为渲染函数的模版参数。
	 * 			函数必须返回一个html片段
	 * 
	 * 	fixed	: fn	可选node修正函数
	 * 		function(node, opt){return node;}参数为上述扩展node节点对象，默认返回节点对象本身，可设置node节点对象的任意属性。
	 * 		函数将在使用模板或渲染函数前执行。
	 * 		本函数内部使用node节点对象属性有
	 * 		{
	 * 			expanded: true|fasle|undefined, 为true表示节点是展开的，否则节点不展开
	 * 			leaf: true|false, 是否是叶子节点，本函数会处理判断节点是否是叶子节点，没有孩子节点的节点都是叶子节点，若提供的fixed函数修改了这个属性，可能会导致渲染异常
	 * 			last: true|false, 是否是同级中最后一个节点，本函数会处理判断节点是否是最后一个节点，若提供的fixed函数修改了这个属性，可能会导致渲染异常
	 * 			children: [], 所有孩子节点集合，本函数会处理得到节点的所有子节点，若提供的fixed函数修改了这个属性，可能会导致渲染异常
	 * 			class: '', 节点渲染后的样式类，本函数会处理设置这个属性，若提供的fixed函数修改了这个属性，可能会导致渲染异常
	 * 			iconCls: '', 节点图标样式，默认为icon-node，若要提供图标，图标必须为16x16格式
	 * 			parent: {node} 节点的父节点，本函数会处理得到节点的父节点，若提供的fixed函数修改了这个属性，可能会导致渲染异常
	 * 		}
	 * 	sort	: fn	可选的排序函数
	 * 		function(a, b){return int;}函数返回0表示两个节点相当，大于0表示a节点比b节点大，否则a节点比b节点小
	 * 	beforeGenTreeMap: function(data, opt){}			树结构生成后执行方法，参数为原始树形json列表
	 * 	afterGenTreeMap: function(nodesMap, nodes, opt){}	树结构生成后执行方法，参数为生成的树节点map和树节点列表
	 * 	scope: Object		上述非sort函数的作用域
	 * }</pre>
	 * @return {} html片段
	 */
	genTree: function(opt){
		opt = opt || {};
		var d = opt.data, key = opt.key, fk = opt.fkey, dspatr = opt.dplkey, atr = opt.atrs, me = opt.scope || window, 
			t = opt.tpl, fixed = opt.fixed, sort = opt.sort, ct = opt.contentTpl;
		if(key == null || d == null || d.length < 1) return '';
		var pno = null, evt = {}, root = {parent: null, children: [], last: true}, tpl, nodes = [], ctpl;
		atr = atr || [];
		dspatr = dspatr || 'name';
		if($.isFunction(opt.beforeGenTreeMap)) opt.beforeGenTreeMap.call(me, d, opt);
		$.each(d || [], function(i, n){
			i = n[key];
			if(i == null) i = '_____nullkey';
			evt[i] = $.copy({}, n);
			evt[i].children = [];
			evt[i].leaf = true;
		});
		$.iterate(evt, function(k, v){
			pno = fk && v[fk];
			nodes.push(v);
			if(pno != null && evt[pno] != null) {
				evt[pno].children.push(v);
				evt[pno].leaf = false;
				v.parent = evt[pno];
			}
			else {
				v.parent = root;
//				v.__root__ = true;
				root.children.push(v);
			}
		});
		if($.isFunction(opt.afterGenTreeMap)) opt.afterGenTreeMap.call(me, evt, nodes, opt);
		if($.isFunction(sort)) root.children.sort(sort);
		pno = [];
		if($.isString(t)) tpl = t;
		if(tpl == null) tpl = [
			'<div class="x-nodeitem">',
				'<div class="{class}"{__atr__}>{__content__}</div>{__children__}',
			'</div>'
		].join('');
		
		if($.isString(ct)) ctpl = ct;
		if(ctpl == null) ctpl = [
			'{',
				dspatr,
			'}'
		].join('');
		
		fixed = fixed || function(node) {node.expanded = node.__level__ == 1; return node;};
		
		if(!$.isFunction(t)) t = function(node, tpl){
			if(!node[dspatr]) return '';
			return tpl.replace(/{(\w+)}/g, function(a, b){return node[b] == null ? '&nbsp;' : node[b];});
		};
		
		if(!$.isFunction(ct)) ct = function(node, ctpl) {
			return ctpl.replace(/{(\w+)}/g, function(a, b){return node[b] == null ? '&nbsp;' : node[b];});
		}
		
		evt = function(node, parent){
			node = fixed.call(me, node, parent, opt);
			var p = node.parent, hs = [], ecc;
			if(node['class']) node['class'] += ' ';
			else node['class'] = '';
			node['class'] += 'x-node' + (node.leaf ? ' x-leaf' : node.expanded ? ' x-expand' : '') + (node.last ? ' last' : '');
//			ecc = 'x-icon-ec-' + (node.leaf ? 'l' : node.expanded ? 'e' : 'c') + (node.last ? 'l' : '');IE6
			$.each(atr, function(i, a){
				if(node[a] != null) hs.push(a + '="' + node[a] + '"');
			});
			node.__atr__ = hs.length > 0 ? ' ' + hs.join(' ') : '';
			
			hs = [];
			while(p && p != root) {
//				if(p.parent && $.inArray(p, p.parent.children) == p.parent.children.length - 1) hs.unshift('<span class="x-icon-blank"></span>');
				if(p && p.last) hs.unshift('<span class="x-icon-blank"></span>');
				else hs.unshift('<span class="x-icon-elbow"></span>');
				p = p.parent;
			}
			if(node[dspatr]) {
//				hs.push('<span class="x-icon-ec ', ecc, '"></span><span class="', node.iconCls || 'x-icon-node', '"></span><span class="x-txt">', node[dspatr], '</span>');IE6
				hs.push('<span class="x-icon-ec"></span><span class="', node.iconCls || 'x-icon-node', '"></span><span class="x-txt">', ct(node, ctpl), '</span>');
			}
			
			node.__content__ = hs.join('');
			
			if(node.children.length > 0) {
				if($.isFunction(sort)) node.children.sort(sort);
				hs = [];
				for(var cs = node.children, il = cs.length - 1, i = il, c, h; i > -1; i--) {
					c = node.children[i];
					c.last = c.last || i == il;
					c.first = c.first || i == 0;
					c.__level__ = node.__level__ + 1;
					c.__index__ = i;
					h = evt(c, node);
					!h && c.last && i > 0 && (cs[i - 1].last = true);
					hs.unshift(h);
				}
//				$.each(node.children, function(i, y){
//					y.__index__ = i;
//					y.__level__ = node.__level__ + 1;
//					y.last = i == node.children.length - 1;
//					hs.push(evt(y));
//				});
				if(hs.join('').length > 0) {
					hs.unshift('<div class="x-nodelist' + (node.expanded ? ' x-expand' : '') + '">');
					hs.push('</div>');
				}
				node.__children__ = hs.join('');
			}
			else {
				node.__children__ = '';
			}
			
			hs = t(node, tpl);

			delete node.__atr__;
			delete node.__content__;
			delete node.__children__;
			
			return hs;
		};
		for(var cs = root.children, il = cs.length - 1, i = il, c, h; i > -1; i--) {
			c = root.children[i];
			c.last = c.last || i == il;
			c.first = c.first || i == 0;
			c.__level__ = 1;
			c.__index__ = i;
			h = evt(c, root);
			!h && c.last && i > 0 && (cs[i - 1].last = true);
			pno.unshift(h);
		}
//		$.each(root.children, function(i, c){
//			c.__index__ = i;
//			c.__level__ = 1;
//			c.last = i == root.children.length - 1;
//			pno.push(evt(c));
//		});
		pno.unshift('<div class="x-nodelist x-expand">');
		pno.push('</div>');
		root = evt = t = null;
		return pno.join('');
	},
	/**
	 * 简单消息提示，参数若干，依赖javacript/jquery-1.11.2.js javascript/ext.js
	 * @param opt {} 配置。
	 * 可配置属性：
	 * msg: String 消息
	 * msgCls: String 消息框样式
	 * txtCls: String 消息文本样式
	 * iconCls: String 图标样式
	 * timeLength: float 消息显示持续时长，单位为秒
	 * callback: Function() 回调函数，消息消失后触发
	 * params: [] 传递给回调函数的参数
	 * scope: String 回调函数作用域
	 */
	simple:function(opt) {
		opt = opt || {};
		
		var msg = opt.msg, tl = opt.timeLength, fn = opt.callback, ps = opt.params, scope = opt.scope;
		
		if(msg == null) return;
		
		tl = tl || 2;
		fn = fn || $.emptyFn;
		
		$('#simplemsg').setClass(opt.msgCls || '').
			children('.x-text').html(msg).setClass([opt.txtCls || '', 'x-text'].join(' ')).
			prev().setClass(opt.iconCls || '').
			parent().fadeIn().delay(tl * 1000).fadeOut(function(){
				fn.apply(scope || window, ps || []);
			});
		
		return;
		$('#simplemsg').html(msg).animate({bottom: '2px'}, 'x-slow').delay(tl * 1000).animate({bottom: '-310px'}, 'x-slow', function(){
			fn.apply(scope || window, ps || []);
		});
	},
	//messagebox handler
	MBH:{},
	/**
	 * 弹出一个消息框，依赖javacript/jquery-1.11.2.js javascript/ext.js
	 * @param opt {} 配置。
	 * 可配置属性：
	 * title: String 消息框的标题
	 * msg: String 消息
	 * iconCls: String 消息图标
	 * negative: Function(evt) 点击取消按钮执行的方法，参数为点击事件
	 * positive: Function(evt) 点击确定按钮执行的方法，参数为点击事件
	 * close: Function(evt) 关闭提示框执行的方法，参数为点击事件
	 * other: Function(evt) 点击第三个按钮执行的方法，最多3个按钮，参数为点击事件
	 * negativeText: String 取消按钮的文本
	 * positiveText: String 确定按钮的文本
	 * otherText: String 第三个按钮的文本
	 * socpe: Object 方法执行作用域
	 * modal: Boolean 是否是模态窗口，默认是，非模态窗口设置modal为false
	 */
	msgbox:function(opt){
		var box = $('#messagebox'), focusEl = opt.focusBtn && $('#messagebox').find('.' + opt.focusBtn);
		if(box.length < 1) return;
		
		var btn1 = $('#messagebox .x-positive'), btn2 = $('#messagebox .x-negative'), btn3 = $('#messagebox .x-other'), btn4 = $('#messagebox .x-close');
		
		$('#messagebox .x-panel-title>.x-text').html(opt.title || '');
		$('#messagebox .x-panel-body>.x-text').html(opt.msg || '').prev().setClass(opt.iconCls || '');
		btn1.html(opt.positiveText || '确认');
		btn2.html(opt.negativeText || '取消');
		btn3.html(opt.otherText || '');
		
		if(opt.positiveTitle) btn1.attr('title', opt.positiveTitle);
		else btn1.removeAttr('title');
		if(opt.negativeTitle) btn2.attr('title', opt.negativeTitle);
		else btn2.removeAttr('title');
		if(opt.otherTitle) btn3.attr('title', opt.otherTitle);
		else btn3.removeAttr('title');
		
		if(!$.isFunction(opt.close)) btn4.addClass('x-hidden');
		else {
			btn4.removeClass('x-hidden');
		}
		
		if(!$.isFunction(opt.other)) btn3.addClass('x-hidden');
		else {
			btn3.removeClass('x-hidden');
			focusEl = focusEl || btn3;
		}
		
		if(!$.isFunction(opt.negative)) btn2.addClass('x-hidden');
		else {
			btn2.removeClass('x-hidden');
			focusEl = focusEl || btn2;
		}
		
		if(!$.isFunction(opt.positive)) btn1.addClass('x-hidden');
		else {
			btn1.removeClass('x-hidden');
			focusEl = focusEl || btn1;
		}
		
		this.MBH = opt;
		
		if(opt.modal !== false) $(document.body).mask();
		box.addClass('x-show');
		if(focusEl) focusEl.focus();
	},
	CFBH: {},
	/**
	 * 弹出一个输入消息框，依赖javacript/jquery-1.11.2.js javascript/ext.js
	 * @param opt {} 配置。
	 * 可配置属性：
	 * title: String 消息框的标题
	 * message: String 消息
	 * negative: Function(txt, evt) 点击取消按钮执行的方法，参数为点击事件
	 * positive: Function(txt, evt) 点击确定按钮执行的方法，参数为点击事件
	 * close: Function(evt) 关闭提示框执行的方法，参数为点击事件
	 * negativeText: String 取消按钮的文本
	 * positiveText: String 确定按钮的文本
	 * multiline：boolean 多行或单行输入控制
	 * socpe: Object 方法执行作用域
	 * modal: Boolean 是否是模态窗口，默认是，非模态窗口设置modal为false
	 */
	prompt: function(opt){
		var box = $('#promptbox'), el;
		if(box.length < 1) return;
		
		$('#promptbox .x-panel-title span.x-text').html(opt.title || '');
		$('#promptbox .x-panel-body span.x-text').html(opt.msg || '').prev().setClass(opt.iconCls || '');
		$('#promptbox .x-negative').html(opt.negativeText || '取消');
		$('#promptbox .x-positive').html(opt.positiveText || '确认');
		
		$('#promptbox .x-panel-body .x-input-wrap').html(opt.multiline ? '<textarea name="txt"></textarea>' : '<input name="txt" />');
		
		$('#promptbox').css('height', opt.multiline ? 180 : 130);
		$('#promptbox .x-panel-body').css('height', opt.multiline ? 110 : 50);
		
		$('#promptbox .x-close')[!$.isFunction(opt.close) ? 'addClass' : 'removeClass']('x-hidden');
		$('#promptbox .x-negative')[!$.isFunction(opt.negative) ? 'addClass' : 'removeClass']('x-hidden');
		$('#promptbox .x-positive')[!$.isFunction(opt.positive) ? 'addClass' : 'removeClass']('x-hidden');
		
		this.CFBH = opt;
		
		if(opt.modal !== false) $(document.body).mask();
		box.addClass('x-show');
		$('#promptbox .x-panel-body .x-input-wrap [name="txt"]').focus();
	},
	/**
	 * 前端js打印a4报告
	 * @param {} opt 参数配置<pre>{
	 * 	content:$()		报告容器		待打印的报告的容器节点的JQ对象
	 * 	before:	Function可选		打印前动作，函数接受一个参数，为该配置对象
	 * 	after:	Function可选		打印后动作，函数接受一个参数，为该配置对象
	 * 	scope:	Object	可选		上述函数作用于
	 * </pre>
	 */
	printA4Report1: function(opt){
		opt = opt || {};
		var content = opt.content, html;
		if(!content || content.length < 1) return;
		
		var html = document.body.innerHTML, before, after, scope;
		before = opt.before || $.emptyFn;
		after = opt.after || $.emptyFn;
		scope = opt.scope || window;
		
		before.call(scope, opt);
		
//		var id = content.attr('id');
//		if(!id) {
//			id = '__report_' + new Date().getTime();
//			content.attr('id', id);
//		}
//		content.children('div.a4').css({margin: 0, boxShadow: 'none'});
//		document.body.innerHTML = content.html().replace(/<div class="bottom\-section\-placeholder"><\/div>/, '');
//		$(document.body).css({overflow: 'auto'});
//		window.print();
//		document.body.innerHTML = html;
//		content = $('#' + id);
//		content.children('div.a4').css({margin: '10px auto 0 auto', boxShadow: '0 0 10px 5px #aaa'});
//		$(document.body).css({overflow: 'hidden'});
		//打印
		var options = {},printArea;
		options.standard = "loose";
		options.extraHead = '<meta charset="utf-8" />';
		printArea = content.children('.a4');
		if(printArea && printArea.length > 0){
			printArea.clone().css({margin: 0, boxShadow: 'none'}).printArea(options);
		}
		else {
			printArea = content.find('.printArea');
			if(printArea && printArea.length > 0){
				printArea.clone().find('td[name="checkRule"]').addClass("x-hidden").end().css({height: "643px;", width: "981px"}).printArea(options);
			}
		}
		after.call(scope, opt);
	},
	printA4Report: function(opt){
		opt = opt || {};
		var content = opt.content, html;
		if(!content || content.length < 1) return;
		
		var printArea;
		printArea = content.children('.a4');
		if(printArea && printArea.length > 0){
			opt.content = printArea.clone().css({margin: 0, boxShadow: 'none'});
		}
		else {
			printArea = content.find('.x-printArea');
			if(printArea && printArea.length > 0){
				opt.content = printArea.clone().find('td[name="checkRule"]').addClass("x-hidden").end().css({height: "643px;", width: "981px"});
			}
		}
		this.print$Dom(opt);
	},
	/**
	 * 前端js打印dom节点
	 * @param {} opt 参数配置<pre>{
	 * 	area:	$()		待打印的报告的容器节点的JQ对象
	 * 	before:	Function可选		打印前动作，函数接受一个参数，为该配置对象
	 * 	after:	Function可选		打印后动作，函数接受一个参数，为该配置对象
	 * 	scope:	Object	可选		上述函数作用于
	 * </pre>
	 */
	print$Dom: function(opt){
		opt = opt || {};
		var content = opt.content.clone();
		if(!content || content.length < 1) return;
		
		var before, after, scope;
		before = opt.before || $.emptyFn;
		after = opt.after || $.emptyFn;
		scope = opt.scope || window;
		
		before.call(scope, opt);
		
		var body = $('body, html'), hd = body.children('.x-hidden');
		body.css({overflow: 'auto', width: 'auto', height: 'auto'});
		hd.siblings().addClass('x-hidden x-print-hidden-dom');
		body.filter('body').append(content);
		window.print();
		content.remove();
		body.css({overflow: 'hidden', width: '100%', height: '100%'}).children('.x-print-hidden-dom').removeClass('x-hidden x-print-hidden-dom');
		
	},
	serializeParams: function(map){
		return map == null ? null : {
			params : JSON.stringify(map)
		};
	},
	__dynamicInt: 0,
	dynamicInt: function(){
		if(this.__dynamicInt >= 1 << 30) this.__dynamicInt = 0;
		return this.__dynamicInt++;
	}
});

$(function(){
	$.defer(function(){
		$('#combolist').click(function(evt){
			var h = tools.CBH, el = $(evt.target), nel, me = $(this);
			if(me.hasClass('x-tree')) {
				if(h.treeDelegate != null) {
					evt.stopPropagation();
					return;
				}
				if(el.hasClass('x-icon-ec')) {
					nel = el.parentsUntil('#combolist', '.x-node:first');
					if(!nel.hasClass('x-leaf')) {
						if(nel.hasClass('x-expand')) {
							nel.removeClass('x-expand');
							nel.next().removeClass('x-expand');
							el.setClass('x-icon-ec x-icon-ec-c' + (nel.hasClass('last') ? 'l' : ''));
						}
						else {
							nel.addClass('x-expand');
							nel.next().addClass('x-expand');
							el.setClass('x-icon-ec x-icon-ec-e' + (nel.hasClass('last') ? 'l' : ''));
						}
					}
					evt.stopPropagation();
					return;
				}
				
				if(!el.hasClass('x-node')) while(el[0] && el[0] != this) {
					if(el.hasClass('x-node')) break;
					el = el.parent();
				}
				if(el.hasClass('x-node')) {
					$(this).find('.x-node.x-selected:first').removeClass('x-selected');
					el.addClass('x-selected');
				}
			}
			if(el[0] != this && h.elclick && h.elclick.call(h.scope || h, el, h, evt) === false) evt.stopPropagation();
			else {
				if(me.hasClass('x-option') && el[0] != this) {
					if(!el.hasClass('x-item')) el = el.parents('.x-item:first');
					el.addClass('x-selected').siblings().removeClass('x-selected');
				}
				me.removeClass('x-show');
				h.onHide && h.onHide.call(h.scope || h, h);
				tools.CBH = {};
			}
		}).mouseover(function(evt){
			var h = tools.CBH, el = $(evt.target);
			h.onMouseOver && h.onMouseOver.call(h.scope || h, el);
		});
		
		$(document.body).click(function(evt){
			var el = $(evt.target), handler = el.attr('handler'), pel = el.parent();
			if(el.hasClass('x-disabled') || el.disabled()) {
				if(!el.parent().hasClass('combo') || el.parent().hasClass('x-disabled')) return;
			}
			
			if(el.attr('handler') == 'print_report_a4') {
				tools.printA4Report({content: el.parent()});
			}
			
			if(el.parent().hasClass('x-plate-side-ec')) {
				el = el.parent();
			}
			if(el.hasClass('x-plate-side-ec')){
				var s = el.parent(), b = s.next(), e = el.children().hasClass('x-icon-prev'), w, cmin = 0;
				if(e) {
					w = s.width();
					s.attr('_width', w);
				}
				else {
					w = s.attr('_width') - 0;
				}
				if(cmin = s.attr('_cmin')) cmin = cmin - 0;
				else cmin = 0;
				s.animate({left: e ? cmin - w : 2}, 'fast');
				b.animate({left: e ? 20 : w + 22}, 'fast');
				el.children().replaceClass(e ? 'x-icon-prev' : 'x-icon-next', e ? 'x-icon-next' : 'x-icon-prev');
			}
			
			if(el.parent().hasClass('x-plate-west-ec')) {
				el = el.parent();
			}
			else if(el.hasClass('x-plate-west-ec')){
				var s = el.parent(), b = s.next(), e = el.children().hasClass('x-icon-next'), w, cmin = 0;
				if(e) {
					w = s.width();
					s.attr('_width', w);
				}
				else {
					w = s.attr('_width') - 0;
				}
				if(cmin = s.attr('_cmin')) cmin = cmin - 0;
				else cmin = 0;
				s.animate({right: e ? cmin - w : 2}, 'fast');
				b.animate({right: e ? 20 : w + 22}, 'fast');
				el.children().replaceClass(e ? 'x-icon-next' : 'x-icon-prev', e ? 'x-icon-prev' : 'x-icon-next');
			}
		}).mousedown(function(evt){
			var el = $(evt.target);
			if(el.hasClass('x-radio-group-item')) {
				el = el.children('input');
				if(el.disabled() || el.hasClass('x-disabled')) return;
				if(/^radio$/i.test(el.type())) el.check(true);
				else el.check(!el.checked());
			}
		}).mouseup(function(evt){
			var cbl = $('#combolist');
			if(!cbl.hasClass('x-show')) return;
			evt = evt.target;
			var cb = true;
			while(evt) {
				if(evt.id == 'combolist') {
					cb = false;
					break;
				}
				evt = evt.parentNode;
			}
			if(cb) {
				cb = tools.CBH;
				cbl.removeClass('x-show');
				cb && cb.onHide && cb.onHide.call(cb.scope || cb, cb);
				tools.CBH = {};
			}
		});

		$('#messagebox').window({
			grouped: false,
			elclick: function(el, evt){
				if(el.hasClass('x-button')) {
					var fn, fs = tools.MBH.scope || tools.MBH;
					if(el.hasClass('x-negative')) fn = tools.MBH.negative;
					else if(el.hasClass('x-positive')) fn = tools.MBH.positive;
					else if(el.hasClass('x-other')) fn = tools.MBH.other;
					
					this.close(evt);
					if(fn) fn.call(fs, evt);
				} else {
					var hel;
					if(el.attr('handler')) {
						hel = el;
					}
					else {
						hel = el.parentsUntil(this.el, '[handler]');		
					}
					if(hel.length > 0) {
						var hdl = hel.attr('handler');
						if($.isFunction(tools.MBH[hdl])) {
							if(tools.MBH[hdl](hel, evt, el) !== true) {
								evt.stopPropagation();
							}
						}
					}
				}
			},
			close:function(evt){
				var fn = tools.MBH.close, fs = tools.MBH.scope || tools.MBH;
				
				if(tools.MBH.modal !== false) $(document.body).unmask();
				this.el.removeClass(this.showClass);
				
				if(fn) fn.call(fs, evt);
				tools.MBH = {};
				return this;
			}
		});
		
		$('#promptbox').window({
			grouped: false,
			elclick: function(e, evt){
				if(e.hasClass('x-button')) {
					var fn, fs = tools.CFBH.scope || tools.CFBH;
					if(e.hasClass('x-negative')) fn = tools.CFBH.negative;
					else if(e.hasClass('x-positive')) fn = tools.CFBH.positive;
					
					if(fn) fn.call(fs, $('#promptbox [name="txt"]').val(), evt);
					this.close(evt);
				}
			},
			beforeClose: function(e){
				var fn = tools.CFBH.close, fs = tools.CFBH.scope || tools.CFBH;
				
				if(tools.CFBH.modal !== false) $(document.body).unmask();
				if(fn) fn.call(fs, $('#promptbox [name="txt"]').val(), e);
				tools.CFBH = {};
			},
			keyup: function(evt){
				if(evt.which == 13) {
					if(!tools.CFBH.multiline || evt.ctrlKey) {
						if($.isFunction(tools.CFBH.positive)) this.el.find('.x-button.x-positive').click();
						else this.close(evt);
					}
				}
			}
		});
		
	});
});










