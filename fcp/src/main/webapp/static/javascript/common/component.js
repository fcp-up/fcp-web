/**
 * <pre>
 * 组件循环引用控制：
 * 1.一个组件cpt至少需要一个元素el。
 * 2.组件可能有事件evt，事件需要用到组件本身。
 * 3.事件必须依赖元素el。
 * 4.循环：cpt->el<->evt->cpt.
 * 5.切断循环：cpt提供destroy方法，供beforeunload事件调用，在方法中：
 * 		1.先注销所有事件，切断el对cpt的引用。
 * 			1.若事件处理函数中要能用到cpt，则cpt必须提供一个生成事件处理函数的方法，该方法不得不闭包。
 * 			2.注销事件，切断了el对事件处理函数的引用，使得事件处理函数可以被回收。
 *			3.事件处理函数被回收，则切断了事件处理函数对生成处理函数的方法的引用，从而释放闭包。
 *			4.释放闭包，使得生成处理函数的生成方法不再持有对cpt和临时变量的引用。
 * 			5.经过2到4，彻底断开el对cpt的引用。
 * 		2.切断cpt对el的引用。
 * </pre>
 */
$.__COMPONENT = [];

$.Cpt = function(opt){
	opt = opt || {};
	$.copy(this, opt);
	this.initComponent();
	this.initEvents();
	this.init();
	$.__COMPONENT.push(this);
};
$.Cpt.prototype = {
	type: 'component',
	events:['click', 'dblclick', 'contextmenu', 'keyup', 'keydown', 'mousewheel', 'mouseover', 'mouseout', 'mouseenter', 'mouseleave', 'mousemove', 'windowresize'],
	dblclickspace: 0.2,
	__clicktime: 0,
	__dblclicktime: 0,
	initComponent:$.emptyFn,
	init:$.emptyFn,
	initEvents:function(){
		var me = this;
		$.each(me.events, function(i, e){
			if($.isFunction(me[e]) && me[e] != $) me.deligateEvent(e);
		});
		me = null;
	},
	/**
	 * 事件委托（注册事件）。
	 * @param en 事件名
	 */
	deligateEvent:function(en){
		this['__el' + en] = function(obj){
			return function(evt){
				if(en == 'click') {
					if($.isFunction(obj.dblclick)) {
						obj.__clicktime = new Date().getTime();
						$.defer(function(){
							if(obj.__clicktime - obj.__dblclicktime > obj.dblclickspace * 1000) obj.click(evt, this);
						}, obj.dblclickspace);
					}
					else {
						obj.click(evt);
					}
				}
				else if(en == 'dblclick') {
					obj.__dblclicktime = new Date().getTime();
					obj.dblclick(evt);
				}
				else {
					obj[en](evt);
				}
			};
		}(this);
		if(en == 'windowresize') $(window).on('resize', this['__el' + en]);
		else {
			if(this.el.on) this.el.on(en, this['__el' + en]);
			else this.el.bind(en, this['__el' + en]);
		}
	},
	/**
	 * 反事件委托（注销事件）。
	 * @param e 事件名
	 */
	unDeligateEvent:function(en){
		if(en == 'windowresize') $(window).off('resize', this['__el' + en]);
		else this.el && this.el.off(en, this['__el' + en]);
		delete this['__el' + en];
	},
	destroy:function(){
		var me = this;
		if(me.beforeDestroy() === false) return;
		$.each(this.events, function(i, e){
			if($.isFunction(me[e])) me.unDeligateEvent(e);
		});
		for(var p in this) delete this[p];
	},
	elclick: function(el, evt){
//		if(!/input/.test(el.nodeName())) this.el.attr('tabindex', 0).focus();
		var hel;
		if(el.attr('handler')) {
			hel = el;
		}
		else {
			hel = el.parentsUntil(this.el, '[handler]');		
		}
		if(hel.length > 0) {
			var hdl = hel.attr('handler');
			if($.isFunction(this[hdl])) {
				if(this[hdl](hel, evt, el) !== true) {
					evt.stopPropagation();
				}
			}
		}
	},
	beforeDestroy: $.emptyFn,
	isDisableEl: function(el){
		if(el.hasClass('x-disabled') || el.disabled()) return true;
		el = el.parent();
		if(el.hasClass('x-disabled')) {
			if(el.hasClass('x-combo') || el.hasClass('x-button')) return true;
		}
		return false;
	}
};

$.Panel = $.extendClass($.Cpt, {
	type:'panel',
	click:function(evt){
		var el = $(evt.target);
		if(this.isDisableEl(el)) return;
		this.elclick(el, evt);
		return this;
	}
});

$.Window = $.extendClass(function(){
	arguments.callee.superclass.constructor.apply(this, arguments);
	this.el.draggable({handle: this.moveHandlerSelector});
	if(this.grouped !== false) {
		$.__windows = $.__windows || [];
		this.__zIndex = $.__windows.length + $.Window.prototype.startZIndex;
		$.__windows.push(this);
	}
}, $.Panel, {
	grouped: false,
	startZIndex: 1000,
	type:'window',
	moveHandlerSelector: '.x-panel-title',
	showClass: 'x-show',
	modal: true,
	zIndex: function(zIndex){
		if(zIndex != null) {
			this.__zIndex = zIndex;
			if(this.el.hasClass(this.showClass)) this.el.css('zIndex', zIndex);
			return this;
		}
		return this.__zIndex;
	},
	initEvents: function(){
		$.Window.superclass.initEvents.apply(this, arguments);
		this.el.on('mousedown', this.onMouseDown = function(cpt){
			return function(evt){
				if(cpt.grouped !== false) cpt.toFront();
			};
		}(this));
		this.el.children(this.moveHandlerSelector).on('dblclick', this.onTitleDblclick = function(cpt){
			return function(evt){
				if(cpt.el.hasClass('x-maxima')) {
					cpt.restory(evt, cpt.el.children('.x-restory'));
				}
				else {
					cpt.maxima(evt, cpt.el.children('.x-maxima'));
				}
			};
		}(this));
		if(this.enableResize) this.el.resizable({containment: this.el.parent()});
		return this;
	},
	toFront: function(){
		var ci = this.zIndex();
		$.each($.__windows, function(i, w){
			i = w.zIndex();
			if(i > ci) w.zIndex(i - 1);
		});
		this.zIndex($.__windows.length + $.Window.prototype.startZIndex);
		return this;
	},
	destroy: function(){
		this.el.off('mousedown', this.onMouseDown);
		this.el.children(this.moveHandlerSelector).off('dblclick', this.onTitleDblclick);
		$.Window.superclass.destroy.apply(this, arguments);
		return this;
	},
	click:function(evt){
		var el = $(evt.target);
		if(this.isDisableEl(el)) return;
		if(el.is('.x-window>.x-close')) {
			this.close(evt, el);
			return;
		}
		else if(el.is('.x-window>.x-maxima')) {
			this.maxima(evt, el);
			return;
		}
		else if(el.is('.x-window>.x-minima')) {
			this.minima(evt, el);
			return;
		}
		else if(el.is('.x-window>.x-restory')) {
			this.restory(evt, el);
			return;
		}
		this.elclick(el, evt);
		return this;
	},
	close:function(evt){
		if(this.beforeClose(evt) !== false) {
			if(this.modal) $(document.body).unmask();
			this.el.removeClass(this.showClass);
			this.onClosed();
			if(this.grouped !== false) this.el.css('zIndex', -1);
		}
		return this;
	},
	open: function(){
		if(this.beforeOpen() !== false) {
			if(this.modal) $(document.body).mask();
			this.el.addClass(this.showClass);
			this.onOpened();
			if(this.grouped !== false) this.el.css('zIndex', this.__zIndex);
		}
		return this;
	},
	maxima: function(evt, el){
		if(this.beforeMaxima(evt, el) !== false) {
			this.__width = this.el.css('width');
			this.__height = this.el.css('height');
			this.__top = this.el.css('top');
			this.__left = this.el.css('left');
			this.__marginLeft = this.el.css('marginLeft');
			this.__marginTop = this.el.css('marginTop');
			this.el.css({
				top: 0, right: 0, bottom: 0, left: 0, width: 'auto', height: 'auto', marginLeft: 0, marginTop: 0
			}).addClass('x-maxima');
			el.replaceClass('x-maxima', 'x-restory');
			this.onMaxima();
		}
		return this;
	},
	minima: function(evt, el){
		if(this.beforeMinima(evt, el) !== false) {
			this.el.removeClass(this.showClass);
			this.onMinima();
			this.el.css('zIndex', -1);
		}
		return this;
	},
	restory: function(evt, el){
		if(this.beforeRestory(evt, el) !== false) {
			this.el.css({
				top: this.__top, right: 'auto', bottom: 'auto', left: this.__left, width: this.__width, height: this.__height, marginLeft: this.__marginLeft, marginTop: this.__marginTop
			}).removeClass('x-maxima');
			el.replaceClass('x-restory', 'x-maxima');
			this.onRestory();
		}
		return this;
	},
	cancel: function(){
		this.close();
		return this;
	},
	
	beforeMaxima: $.emptyFn,
	beforeMinima: $.emptyFn,
	beforeRestory: $.emptyFn,
	beforeOpen: $.emptyFn,
	beforeClose: $.emptyFn,
	
	onMinima: $.emptyFn,
	onMaxima: $.emptyFn,
	onRestory: $.emptyFn,
	onOpened: $.emptyFn,
	onClosed: $.emptyFn
});

$.Tab = $.extendClass(function(){
	var c = this.chain = [];
	arguments.callee.superclass.constructor.apply(this, arguments);
	var x = -1, ac = this.tabElActiveClass;
	this.tabWrapEl = this.getTabWrapEl();
	this.tabScrollLeftEl = this.getTabScrollLeftEl();
	this.tabScrollRightEl = this.getTabScrollRightEl();
	this.tabEls().each(function(i, e){
		if($(e).hasClass(ac)) {
			x = i;
		}
		else c.push(i);
	});
	if(x > -1) {
		this.chain.push(x);
		this.activeTab(x);
	}	
	$.cycle(function(){
		var w = this.tabWrapEl.width(), aw = this.el.width();
		if(w > aw) {
			this.tabScrollLeftEl.removeClass('x-hidden');
			this.tabScrollRightEl.removeClass('x-hidden');
		}
		if(w > 0) return false;
	}, this, 0.1);
}, $.Cpt, {
	type: 'tab',
	tabElSelector: '.x-tab',
	tabElActiveClass: 'x-active',
	tabCloseElSelector: '.x-tab>.x-close',
	tabWrapSelector: '.x-tab-wrap',
	tabScrollLeftClass: 'x-tabbar-scroll-left',
	tabScrollRightClass: 'x-tabbar-scroll-right',
	_activedtabel: null,
	_tabEls: null,
	getTabWrapEl: function(){
		if(this.__tabWrapEl != null) return this.__tabWrapEl;
		return this.__tabWrapEl = this.el.children().filter(this.tabWrapSelector);
	},
	getTabScrollLeftEl: function(){
		if(this.__tabScrollLeftEl != null) return this.__tabScrollLeftEl;
		return this.__tabScrollLeftEl = this.el.children().filter('.' + this.tabScrollLeftClass);
	},
	getTabScrollRightEl: function(){
		if(this.__tabScrollRightEl != null) return this.__tabScrollRightEl;
		return this.__tabScrollRightEl = this.el.children().filter('.' + this.tabScrollRightClass);
	},
	windowresize: function(){
		this.judgeScrollEl();
	},
	tabEls: function(){
		if(this._tabEls == null) this._tabEls = this.tabWrapEl.children(this.tabElSelector);
		return this._tabEls;
	},
	judgeScrollEl: function(){
		var w = this.tabWrapEl.width(), aw = this.el.width();
		if(w > aw) {
			this.tabScrollLeftEl.removeClass('x-hidden');
			this.tabScrollRightEl.removeClass('x-hidden');
			if(this.tabWrapEl.position().left == 0) this.tabWrapEl.css('left', 18);
			
		}
		else {
			this.tabScrollLeftEl.addClass('x-hidden').addClass('x-disabled');
			this.tabScrollRightEl.addClass('x-hidden').removeClass('x-disabled');
			this.tabWrapEl.css('left', 0);
		}
	},
	activedTabEl: function(){return this._activedtabel;},
	activedIndex: function(){
		var index = -1;
		this.tabEls().each(function(i, x){
			if($(x).hasClass('x-active')) {
				index = i;
				return false;
			}
		});
		return index;
	},
	click:function(evt){
		var tab = $(evt.target);
		var el = tab, b = true;
		if(this.isDisableEl(el)) return;
		if(tab.hasClass(this.tabScrollLeftClass)) {
			var w = this.tabWrapEl.width(), l = this.tabWrapEl.position().left, aw = this.el.width() - tab.width() * 2 - 6;
			l = l + 100;
			l = l > 18 ? 18 : l;
			this.tabWrapEl.animate(
				{left: l + 'px'}, 'fast'
			);
			aw = l >= 18;
			tab[aw ? 'addClass' : 'removeClass']('x-disabled');
			tab.siblings('.' + this.tabScrollRightClass)['removeClass']('x-disabled');
			return;
		}
		if(tab.hasClass(this.tabScrollRightClass)) {
			var w = this.tabWrapEl.width(), l = this.tabWrapEl.position().left, aw = this.el.width() - tab.width() * 2 - 6;
			l = l - 100;
			w = aw - w;
			l = l < w ? w : l;
			this.tabWrapEl.animate(
				{left: l + 'px'}, 'fast'
			);
			aw = l <= w;
			tab[aw ? 'addClass' : 'removeClass']('x-disabled');
			tab.siblings('.' + this.tabScrollLeftClass)['removeClass']('x-disabled');
			return;
		}
		if(tab.is(this.tabCloseElSelector)) {
			b = false;
			tab = tab.parent();
			var nr = [], di = -1;
			this.tabEls().each(function(k, i){
				if(i == tab[0]) {
					di = k;
					return false;
				}
			});
			if(di < 0 || this.beforeRemoveTab(di, el, evt) === false) return;
			$.each(this.chain, function(k, i){
				if(i != di) {
					if(i > di) nr.push(i - 1);
					else nr.push(i);
				}
			});
			this.chain = nr;
			tab.remove();
			this.tabRemoved(di);
			this._tabEls = this.el.children(this.tabElSelector);
			if(tab.hasClass(this.tabElActiveClass) && this.chain.length > 0) {
				di = this.chain[this.chain.length - 1];
				this.activeTab(di);
			}
		}
		tab = this.getTabEl(tab);
		if(tab.is(this.tabElSelector)) {
			b = false;
			var x = -1, oi = -1;
			this.tabEls().each(function(i, e){
				if(e == tab[0]) {
					x = i;
					return false;
				}
			});
			this.activeTab(x, el, evt);			
		}
		this.elclick(el, evt);
		return this;
	},
	getTabEl: function(el){
		while(el.length > 0 && el[0] != this.el[0]) {
			if(el.is(this.tabElSelector)) return el;
			el = el.parent();
		}
		return el;
	},
	activeTab: function(x, el, evt){
		var oi = -1, ac = this.tabElActiveClass, me = this;
		this.tabEls().each(function(i, e){
			if($(e).hasClass(ac)) {oi = i; return false;}
		});
		if(x > -1 && this.beforeActiveTab(x, el, evt) !== false) {
			this.tabEls().each(function(i, e){
				e = $(e);
				if(e.hasClass(ac)) e.removeClass(ac);
				if(i == x) {
					e.addClass(ac);
					me._activedtabel = e;
				}
			});
			
			var nr = [];
			$.each(this.chain, function(k, i){
				if(i != x) nr.push(i);
			});
			nr.push(x);
			this.chain = nr;
			
			if(oi != x) this.tabChanged(oi, x, el, evt);
			this.tabActived(x, el, evt);
		}
		return this;
	},
	addTab:function(html){
		html = $(html).appendTo(this.el);
		var i = this.tabEls().length - 1;
		if(i < 0) i = 0;
		this._tabEls = this.el.children(this.tabElSelector);
		if(html.hasClass(this.tabElActiveClass)) {
			this.chain.push(i);
			this.activeTab(i);
		}
		else this.chain.unshift(i);
		return this;
	},
	beforeRemoveTab: $.emptyFn, //function(int i, el, evt)
	beforeActiveTab: $.emptyFn, //function(int i, el, evt)
	tabRemoved: $.emptyFn, //function(int i, el, evt)
	tabChanged:$.emptyFn, //function(int oi, int ni, el, evt)
	tabActived:$.emptyFn //function(int i, el, evt)
});

$.TreePanel = $.extendClass(function(){
	arguments.callee.superclass.constructor.apply(this, arguments);
}, $.Panel, {
	type: 'treepanel',
	pkey: 'nid',
	fkey: 'pid',
	dplkey: 'name',
	selectedNode: null,
	isEcEl: function(el){return el.is('.x-node .x-icon-ec');},
	isLeafNode: function(nel){return nel.hasClass(this.leafNodeElClass);},
	isExpandNode: function(nel){return nel.hasClass(this.expandNodeElClass);},
	isLastNode: function(nel){return nel.hasClass(this.lastNodeElClass);},
	leafNodeElClass: 'x-leaf',
	expandNodeElClass: 'x-expand',
	lastNodeElClass: 'x-last',
	click:function(evt){
		var el = $(evt.target), b = true, nel = this.getNodeEl(el);
		if(this.isDisableEl(el)) return;
		if(this.isEcEl(el)) {
			if(!this.isLeafNode(nel)) {
				if(this.isExpandNode(nel)) {
					this.collapseNode(nel);
				}
				else {
					this.expandNode(nel);
				}
				b = false;
			}
		}
		//不是展开收缩操作
		if(b) {
			if(nel) {
				if(this.beforeNodeClick(nel, evt, el) !== false) {
					this.selectNode(nel, evt, el);
					this.nodeClicked(nel, evt, el);
				}
			}
			$.TreePanel.superclass.click.apply(this, arguments);
		}
		return this;
	},
	collapseNode: function(nel){
		if(this.beforeCollapse(nel) !== false) {
			this.onCollapse(nel);
			nel.removeClass(this.expandNodeElClass);
			nel.next().removeClass(this.expandNodeElClass);
			this.treeMap[nel.attr(this.pkey)].expanded = false;
			this.onCollapsed(nel);
		}
		return this;
	},
	expandNode: function(nel){
		if(this.beforeExpand(nel) != false) {
			this.onExpand(nel);
			nel.addClass(this.expandNodeElClass);
			nel.next().addClass(this.expandNodeElClass);
			this.treeMap[nel.attr(this.pkey)].expanded = true;
			this.onExpanded(nel);
		}
		return this;
	},
	getContentEl: function(){
		return this.el;
	},
	getRenderAtrs: $.emptyFn,
	afterGenTreeMap: $.emptyFn,
	getRenderTpl: $.emptyFn,
	beforeGenTreeMap: null,
	nodeSorter: null,
	nodeFixeder: null,
	afterRenderNode: $.emptyFn,
	genTreeOption: function(data, opt){
		var atrs = this.getRenderAtrs() || [], lastMap = this.treeMap;
		atrs.push(this.pkey);
		return $.copy({
			data: data || [], key: this.pkey, fkey: this.fkey, dplkey: this.dplkey, atrs: atrs, scope: this, 
			afterGenTreeMap: function(nodeMap){
				this.treeMap = nodeMap;
				this.afterGenTreeMap.apply(this, arguments);
			},
			beforeGenTreeMap: this.beforeGenTreeMap,
			sort: this.nodeSorter,
			fixed: function(node){
				var n = this.nodeFixeder && this.nodeFixeder(node);
				n = n || node;
				if(lastMap && lastMap[node[this.pkey]].expanded != null) n.expanded = lastMap[node[this.pkey]].expanded;
				else n.expanded = n.__level__ == 1;
				return n;
			},
			tpl: this.getRenderTpl()
		}, opt || {}, false, true);
	},
	renderNodes: function(nodes, parentEl){
		if(parentEl) {
			parentEl.next().html('');
			this.append(parentEl, nodes);
		}
		else {
			this.getContentEl().html(tools.genTree(this.genTreeOption(nodes)));
		}
		if(this.selectedNode) {
			var node = this.el.find(['.x-node[', this.pkey, '="', this.selectedNode[this.pkey], '"]'].join('')).addClass('x-selected');
			this.selectNode(node);
			node = node.parent().parent().prev();
			while(node.length > 0) {
				this.expandNode(node);
				node = node.parent().parent().prev();
			}
		}
		this.afterRenderNode();
		return this;
	},
	append: function(parentEl, data){
		var dplkey = this.dplkey, pkey = this.pkey, atr = this.getRenderAtrs(), fixed = this.nodeFixeder, fixedEl = this.afterRenderNode, 
			parent = this.treeMap[parentEl.attr(pkey)], me = this;
		dplkey = dplkey || 'name';
		var ct, cs = [], cn, me = this, last = me.isLastNode(parentEl);
		if(this.isLeafNode(parentEl)) {
			parentEl.removeClass(this.leafNodeElClass).addClass(this.expandNodeElClass).parent().append(ct = $('<div class="x-nodelist x-expand"></div>'));
		}
		else {
			parentEl.addClass(this.expandNodeElClass);
			ct = parentEl.next().addClass(this.expandNodeElClass);
			if(ct.length < 1) parentEl.parent().append(ct = $('<div class="x-nodelist x-expand"></div>'));
		}
		fixed = fixed || function(r){return $.copy({}, r);};
		fixedEl = fixedEl || $.emptyFn;
		data = data || [];
		if(data.length < 1) {
			parentEl.addClass('x-leaf');
		}
		parent.children = parent.children || [];
		var ln = data.length, cl = parent.children.length;
		if(cl > 0) parent.children[cl - 1].last = false;
		
		$.each(data, function(i, r){
			ct.children().last().children('.x-node').removeClass('x-last');
			r = fixed.call(me, r, parentEl);
			
			r.leaf = true;
			r.children = [];
			r.last = ln - 1 == i;
			r.first = (i + cl) == 0;
			r.__index__ = cl++;
			r.__level__ = parent.__level__ + 1;
			r.parent = parent;
			
			me.treeMap[r[pkey]] = r;
			
			cn = parentEl.clone();
			cn.children('.txt').html(r[dplkey]);
			$(last ? '<span class="x-icon-blank"></span>' : '<span class="x-icon-elbow"></span>').insertBefore(cn.children('.x-icon-ec'));
			ct.append($('<div class="x-nodeitem"></div>').append(cn = $('<div class="x-node x-leaf"></div>').append(cn.children())));
			$.each(atr || [], function(j, a){
				if(r[a] != null) cn.attr(a, r[a]);
			});
			fixedEl(cn, r, i);
		});
		ct.children().last().children('.x-node').addClass('x-last');
		return this;
	},
	beforeUpNode: $.emptyFn,
	afterUpNode: $.emptyFn,
	upNode: function(nodeEl){
		var node = this.treeMap[nodeEl.attr(this.pkey)];
		if(this.beforeUpNode(nodeEl, node) == false) return;
		var prevEl = nodeEl.parent().prev().children('.x-node:first'), 
			prev = this.treeMap[prevEl.attr(this.pkey)];
		
		var order = prev.__index__;
		prev.__index__ == node.__index__;
		node.__index__ = order;
		
		if(prev.first) {
			prev.first = false;
			node.first = true;
		}
		
		if(node.last) {
			prev.last = true;
			node.last = false;
		}
		
		prevEl.parent().before(nodeEl.parent());
		this.afterUpNode(nodeEl, node, prev);
		return this;
	},
	beforeDownNode: $.emptyFn,
	afterDownNode: $.emptyFn,
	downNode: function(nodeEl){
		var node = this.treeMap[nodeEl.attr(this.pkey)];
		if(this.beforeDownNode(nodeEl, node) == false) return;
		var nextEl = nodeEl.parent().next().children('.x-node:first'), 
			next = this.treeMap[nextEl.attr(this.pkey)];
		
		var order = next.__index__;
		next.__index__ == node.__index__;
		node.__index__ = order;
		
		if(next.last) {
			next.last = false;
			node.last = true;
		}
		
		if(node.first) {
			next.first = true;
			node.first = false;
		}
		
		nextEl.parent().after(nodeEl.parent());
		this.afterDownNode(nodeEl, node, next);
		return this;
	},
	afterDelNode: $.emptyFn,
	beforeDelNode: $.emptyFn,
	delNode: function(nodeEl){
		var nid, node = this.treeMap[nid = nodeEl.attr(this.pkey)];
		if(this.beforeDelNode(nodeEl, node) == false) return;
		delete this.treeMap[nid];
		var list = [], c = 0, ss = node.parent && node.parent.children;
		
		if(ss) {
			$.each(ss, function(i, d){
				if(d.nid != nid) {
					list.push(d);
					d.__index__ = c++;
				}
			});
			node.parent.children = list;
			if(list.length > 0) {
				list[0].first = true;
				list[--c].last = true;
			}
			else {
				node.parent.leaf = true;
				nodeEl.parent().parent().prev().addClass('x-leaf');
			}
		}
		nodeEl.remove();
		this.afterDelNode(nodeEl, node);
		return this;
	},
	selectNode: function(nel, evt, el){
		if(!this.isLeafNode(nel)) {
			if(!this.isExpandNode(nel)) {
				this.expandNode(nel);
			}
			else if(nel.hasClass('x-selected')) {
				this.collapseNode(nel);
			}
		}
		var x = this.el.find('.x-node.x-selected:first');
		x.removeClass('x-selected');
		nel.addClass('x-selected');
		this.selectedNode = this.treeMap[x.attr(this.pkey)];
		this.onNodeSelected(nel, this.selectedNode);
		return;
		if(x[0] == nel[0]) {
			nel.toggleClass('x-selected');
		}
		else {
			x.removeClass('x-selected');
			nel.addClass('x-selected');
		}
		return this;
	},
	getNodeEl:function(el){
		while(el[0] && el[0] != this.el[0]) {
			if(el.hasClass('x-node')) return el;
			el = el.parent();
		}
		return null;
	},
	beforeNodeClick: $.emptyFn,		//function(nodeEl, event, el){}点击节点前执行，如果返回false，则节点不会被选中，不会触发nodeclick
	nodeClicked: $.emptyFn,		//function(nodeEl, event, el){}点击节点后执行，节点元素JQ、点击事件、点击元素JQ
	beforeCollapse: $.emptyFn,	//function(nodeEl){}收缩前执行，若返回false，则不收缩
	beforeExpand: $.emptyFn,	//function(nodeEl){}展开前执行，若返回false，则不展开
	onCollapse: $.emptyFn,		//function(nodeEl){}收缩开始时执行
	onCollapsed: $.emptyFn,		//function(nodeEl){}收缩完成后执行
	onExpand: $.emptyFn,		//function(nodeEl){}展开开始时执行
	onExpanded: $.emptyFn,		//function(nodeEl){}展开完成时执行
	onNodeSelected: $.emptyFn	//function(nodeEl, node){}节点被选中时执行
});

$.GridPanel = $.extendClass($.Panel, {
	type: 'gridpanel',
	click:function(evt){
		var el = $(evt.target), hdl;
		if(el.is('.x-column-header .x-check-column') && (el = el.children('input')) && !el.disabled()) {
			var chd;
			el.check(chd = !el.checked());
			this.checkedAll(chd);
		}
		else if(el.is('.x-column-header .x-check-column input') && !el.disabled()){
			this.checkedAll(el[0].checked);
		}
		else if(el.is('.x-gridEditor input') && !el.disabled()) return;
		else if(el.is('tr *') && this.beforeSelectRow(el, evt) !== false) {
			el = el.parentsUntil(this.el, 'tr');
			if(el.hasClass('x-column-header')) return;
			this.selectRow(el, evt);
		}
		$.GridPanel.superclass.click.apply(this, arguments);
		return this;
	}
});

$.GridWindow = $.extendClass($.Window, {
	type: 'gridwindow',
	click:function(evt){
		var el = $(evt.target), hdl;
		if(el.is('.x-column-header .x-check-column') && (el = el.children('input')) && !el.disabled()) {
			var chd;
			el.check(chd = !el.checked());
			this.checkedAll(chd);
		}
		else if(el.is('.x-column-header .x-check-column input') && !el.disabled()){
			this.checkedAll(el[0].checked);
		}
		else if(el.is('.x-gridEditor input') && !el.disabled()) return;
		else if(el.is('tr *') && this.beforeSelectRow(el, evt) !== false) {
			el = el.parentsUntil(this.el, 'tr');
			if(el.hasClass('x-column-header')) return;
			this.selectRow(el, evt);
		}
		$.GridWindow.superclass.click.apply(this, arguments);
		return this;
	}
});

(function(){
	var prototype = {
		multiSelected: false,
		requestType: 'GET',
		initPageSize: function(){
			var el = this.el, h;
			$.cycle(function(){
				if((h = el.innerHeight()) > 0) {
					this.autoPageSize();
					el = h = null;
					return false;
				}
			}, 0.5, this);
			return this;
		},
		autoPageSize: function(){
			var s = this.innerHeight() / this.getRowHeight();
			s = (s >> 0);
			s = ((s / 5) >> 0) * 5;
			this.el.find('.x-panel-footer>[name="pageSize"]').val(s);
			return this;
		},
		getRowHeight: function(){
			return 23;
		},
		getTableEl: function(){
			if(this.__tableEl != null) return this.__tableEl;
			var el = this.el;
			if(!/^table$/i.test(el.nodeName())) {
				el = el.find('table:first');
			}
			return this.__tableEl = el;
		},
		innerHeight: function(){
			if(this.__innerHeight == null) return this.__innerHeight;
			var el = this.getTableEl(), h;
			h = el.parent().innerHeight();
			el.children().children('tr.x-column-header').each(function(i, f){
				h = h - $(f).offsetHeight();				
			});
			return this.__innerHeight = h;
		},
		windowresize: function(){
			var el = this.getTableEl(), h;
			h = el.parent().innerHeight();
			el.children().children('tr.x-column-header').each(function(i, f){
				h = h - $(f).offsetHeight();				
			});
			this.__innerHeight = h;
		},
		selectRow: function(el, evt){
			this.scrollRow(this.lastFocusRow(), el, evt.shiftKey);
			if(el.hasClass('x-selected')) {
				el.removeClass('x-selected x-focus-row');
				el = el.find('.x-check-column input');
				!el.hasClass('x-disabled') && (el = el[0]) && el.disabled == false && (el.checked = false);
			}
			else {
				if(!this.multiSelected) {
					el.siblings().removeClass('x-selected').find('.x-check-column input').check(false);
				}
				el.addClass('x-selected x-focus-row');
				el = el.find('.x-check-column input');
				!el.hasClass('x-disabled') && (el = el[0]) && el.disabled == false && (el.checked = true);
			}
			this.rowSelected();
			return this;
		},
		checkedAll:function(chd){
			this.el.find('.x-check-column input').each(function(i, e){
				if(e.disabled) return;
				i = $(e);
				if(i.hasClass('x-disabled') || i.attr('for') == 'all') return;
				e.checked = chd;
				if(chd) i.parents('tr:first').addClass('x-selected');
				else i.parents('tr:first').removeClass('x-selected');
			});
			this.rowSelected();
			return this;
		},
		initEvents:function(){
			$.GridPanel.superclass.initEvents.apply(this, arguments);
			this.el.find('.x-data-area:first').on('scroll', this.__onContentScroll);
			this.initPageSize();
			var htrEl = this.el.find('tr.x-column-header'), tblEl = htrEl.parentsUntil(this.el, 'table:first');
			tblEl.css({width: this.__tableWidth = tblEl.width()});
			this.__headerTableEl = tblEl;
			this.el.find('tr.x-column-header td.x-widen').resizable({
				handles: 'e', 
				resize: function(cpt){
					return function(evt, ui){
						ui.element.parentsUntil(cpt.el, 'table:first').css({width: cpt.__tableWidth + ui.size.width - ui.originalSize.width});
						cpt.onColumnWidthDrag({
							tag: ui.element,
							event: evt,
							start: ui.originalPosition,
							stop: ui.position,
							begin: ui.originalSize,
							end: ui.size
						});
					};
				}(this),
				start: function(cpt){
					return function(evt, ui){
						evt = ui.element.parentsUntil(cpt.el, 'table:first');
						evt.css('width', cpt.__tableWidth = evt.width());
					}
				}(this)
			});
			return this;
		},
		getHeaderTableEl: function(){
			return this.__headerTableEl;
		},
		/**
		 * 当可变宽度列被拖动时执行
		 * @param drag<pre>{
		 * 	tag: $(td), 被拖动列的JQuery对象
		 * 	event: event, 拖地事件
		 * 	start: {top: num, left: num}, 被拖动列的起始位置
		 * 	stop: {top: num, left: num}, 被拖动列的结束位置
		 * 	begin: {width: num, height: num}, 被拖动列的起始尺寸
		 * 	end: {width: num, height: num} 被拖动列的结束尺寸
		 * }</pre>
		 */
		onColumnWidthDrag: function(tag){},
		__onContentScroll:function(evt) {
			$(this.parentNode).find('.x-column-header table:first').css('left', 0 - this.scrollLeft + 'px');
		},
		destroy:function(){
			this.el.find('.x-data-area:first').off('scroll', this.__onContentScroll);
			this.el.find('tr.x-column-header td.x-column-width-draggable div').resizable('destroy');
			this.constructor.superclass.destroy.apply(this, arguments);
			return this;
		},
		/**
		 * 1.取第一个函数作为回调函数。
		 * 2.取第一个数组作为回调函数实参。会把数据加载结果填充到数组最后两个元素中，如果成功，填充(true, 加载结果)；如果失败，填充(false, xhr)
		 * 3.取第一个非上述参数作为回调函数作用域。默认为GridPanel对象
		 */
		loadData:function(){
			var url = this.getUrl(), cb, cs, cp;
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
			if(!url) return this;
			var p = this.getParams(), el, ps;
			if(p === false) return this;
			p = p || {};
			
			var psel = this.el.find('.x-panel-footer [name="pageSize"]'), ps;
			if(psel.length > 0) {
				ps = ps || psel.val() - 0 || 30;
				psel.val(ps);
				
				var piel = this.el.find('.x-panel-footer [name="pageIndex"]'), pi = piel.val() - 0 || 1;
				piel.val(pi);
				p.start = ps * (pi - 1);
				p.limit = ps;
			}
			
			this.el.mask();
			this.el.progress('数据加载中...');
			this.renderData([]);
			$.ajax({
				url:url, type: this.requestType, data:this.fixParams(p), dataType:'json', context: this, 
				success:function(d){
					this.el.find('input[for="all"]').check(false);
					d = this.fixData(d);
					if(d === false) {
						this.el.unprogress();
						this.el.unmask();
						cp.push(true, d);
						cb.apply(cs || this, cp);
						return;
					}
					if(ps > 0) {
						pi = this.fixPagingBar(d.total, pi, ps);
					}
					this.renderData(d.data, pi);
					this.el.unprogress();
					this.el.unmask();
					cp.push(true, d);
					cb.apply(cs || this, cp);
				},
				error:function(xhr,status,error){
					this.el.unprogress();
					this.el.unmask();
					this.error(xhr,status,error);
					cp.push(false, xhr);
					cb.apply(cs || this, cp);
				}
			});
			return this;
		},
		fixPagingBar: function(t, pi, ps){
			this.el.find('[name="total"]').val(t);
			if(pi == null) pi = this.el.find('.x-panel-footer [name="pageIndex"]').val() - 0 || 0;
			if(ps == null) ps = this.el.find('.x-panel-footer [name="pageSize"]').val() - 0 || 0;
			var pn = t / ps >> 0, op;
			if(t % ps != 0) pn++;
			this.el.find('.x-panel-footer [name="pageNum"]').html(pn);
			
			if(pi < 2) op = 'addClass';
			else op = 'removeClass';
			this.el.find('.x-panel-footer [handler="firstPage"]')[op]('x-disabled').children()[op]('x-disabled');
			this.el.find('.x-panel-footer [handler="prevPage"]')[op]('x-disabled').children()[op]('x-disabled');
			
			if(pi >= pn) op = 'addClass';
			else op = 'removeClass';
			this.el.find('.x-panel-footer [handler="nextPage"]')[op]('x-disabled').children()[op]('x-disabled');
			this.el.find('.x-panel-footer [handler="lastPage"]')[op]('x-disabled').children()[op]('x-disabled');
			
			pn = (pi - 1) * ps;
			op = pn + ps;
			op = Math.min(op, t);
			
			this.el.find('.x-panel-footer [name="display"]').html(['显示', pn + 1, '-', op, '，共', t, '条'].join(''));
			return pn + 1;
		},
		fixParams: function(p){return p;},
		fixData: function(d){
			if(d.code != 0) {
				tools.msgbox({msg: '加载数据异常，请联系管理员', positive: $.emptyFn});
				return false;
			}
			return d;
		},
		getParams:$.emptyFn,
		error:function(xhr,status,error){
			tools.msgbox({msg: '加载数据异常，请联系管理员', positive: $.emptyFn});
			return this;
		},
		renderData: function(data, pi){
			var hs = [], me = this;
			pi = pi || 1;
			$.each(data || [], function(i, r){
				r._sequence_ = pi++;
				hs.push(me.renderRecord(r));
			});
			me = me.el.find('tr.x-column-header:last');
			me.find('input[for="all"]').check(false);
			me.nextAll().remove();
			me.parent().append(hs);
			this.el.find('[handler="del_batch"]').addClass('x-disabled');
			return this;
		},
		/**
		 * 渲染record[json对象]，每个record会内部处理新增一个_sequence_属性，表示该record在当前表格中的序号
		 */
		renderRecord: function(r){
			r = this.fixedRecord(r) || {};
			return this.getTpl(r).replace(/{(\w+)}/g, function(a, b){return r[b] == null ? '' : r[b];});
		}, 
		replaceRecord: function(tr, r){
			$(this.renderRecord(r)).insertBefore(tr);
			tr.remove();
			return this;
		},
		appendRecord: function(r){
			var tr = $(this.renderRecord(r));
			tr.appendTo(this.el.find('tr.x-column-header:last').parent());
			return tr;
		},
		getTpl: function(r){
			return this.tpl || '';
		}, 
		fixedRecord: function(r){
			return r;
		},
		firstPage:function(){
			this.el.find('.x-panel-footer [name="pageIndex"]').val(1);
			return this.loadData();
		},
		prevPage:function(){
			var el = this.el.find('.x-panel-footer [name="pageIndex"]'), p = el.val() - 1;
			if(p < 1) return;
			el.val(p);
			return this.loadData();
		},
		nextPage:function(){
			var el = this.el.find('.x-panel-footer [name="pageIndex"]'), p = el.val() - 0 + 1;
			if(p > this.el.find('.x-panel-footer [name="pageNum"]').html().replace(/\&nbsp;/g, '')) return;
			el.val(p);
			return this.loadData();
		},
		lastPage:function(){
			this.el.find('.x-panel-footer [name="pageIndex"]').val(this.el.find('.x-panel-footer [name="pageNum"]').html().replace(/\&nbsp;/g, ''));
			return this.loadData();
		},
		gotoPage:function(){return this.loadData();},
		ref:function(){return this.loadData();},
		getUrl:function(){return this.url;},
		rowSelected: function(){
			this.el.find('[handler="del_batch"]')[this.el.find('tr.x-selected').length > 0 ? 'removeClass' : 'addClass']('x-disabled');
			return this;
		},
		beforeSelectRow: function(el){
			return !el.attr('handler');
		},
		keyup:function(evt){
			this.scrollRowing = false;
			var el = $(evt.target);
			if(el.is('.x-panel-footer input')) {
				if(evt.which == 13) this.loadData();
				else {
					el.val(el.val().replace(/[^\d]+/g, ''));
				}
			}
			else switch(evt.which){
				case 40://↓
					this.nextRow(evt.shiftKey);
					break;
				case 38://↑
					this.prevRow(evt.shiftKey);
					break;
				case 37://←
					break;
				case 39://→
					break;
				case 34://page down
					evt.shiftKey ? this.lastPage() : this.nextPage();
					break;
				case 33://page up
					evt.shiftKey ? this.firstPage() : this.prevPage();
					break;
				case 32://space
					this.selectFocusRow();
					break;
			}
		},
		keydown: function(evt){
			var el = $(evt.target);
			if(!el.is('.x-panel-footer input')) {
				switch(evt.which){
				case 40://↓
					this.scrollRowing = true;
					$.defer(this, function(){
						if(this.scrollRowing) this.nextRow(evt.shiftKey);
					}, 0.25);
					break;
				case 38://↑
					this.scrollRowing = true;
					$.defer(this, function(){
						if(this.scrollRowing) this.prevRow(evt.shiftKey);
					}, 0.25);
					break;
				}
			}
		},
		selectFocusRow: function(){
			var sed = this.el.find('tr.x-focus-row').hasClass('x-selected');
			this.el.find('tr.x-focus-row')[sed ? 'removeClass' : 'addClass']('x-selected').find('.x-check-column input').check(!sed);
			this.rowSelected();
		},
		/**
		 * @param lstR 最后一次获取焦点的行
		 * @param nR 本次获取焦点的行
		 * @param shiftKey 是否按下shift键盘
		 */
		scrollRow: function(lstR, nR, shiftKey, dir){
			var lhf = lstR.hasClass('x-focus-row'), nhf = nR.hasClass('x-focus-row');
			nR.addClass('x-last-focus-row');
			var scrollTop = nR.position().top - this.innerHeight();
			if(scrollTop > 0) {
				this.getTableEl().parent().scrollTop(scrollTop);
			}
			else {
				this.getTableEl().parent().scrollTop(0);
			}
			if(lstR.length < 1) {
				nR[nhf ? 'removeClass' : 'addClass']('x-focus-row');
			}
			else if(!shiftKey || !this.multiSelected) {
				nR.siblings().removeClass('x-focus-row x-last-focus row');
				nR[nhf ? 'removeClass' : 'addClass']('x-focus-row');
			}
			else if(lhf == nhf) {
				lstR[lhf ? 'removeClass' : 'addClass']('x-focus-row');
			}
			else {
				nR[nhf ? 'removeClass' : 'addClass']('x-focus-row');
			}
		},
		lastFocusRow: function(){
			return this.el.find('tr.x-last-focus-row').removeClass('x-last-focus-row');
		},
		nextRow: function(shiftKey){
			var lstR = this.lastFocusRow(), nR;
			nR = lstR.next();
			if(nR.length < 1) nR = this.el.find('tr.x-column-header:last').next();
			this.scrollRow(lstR, nR, shiftKey, 0);
		},
		prevRow: function(shiftKey){
			var lstR = this.lastFocusRow(), nR;
			nR = lstR.prev();
			if(nR.length < 1 || nR.hasClass('x-column-header')) nR = this.el.find('tr:last');
			this.scrollRow(lstR, nR, shiftKey, 1);
		},
		mousewheel: function(evt) {
		}
	};
	
	$.copy($.GridWindow.prototype, prototype);
	$.copy($.GridPanel.prototype, prototype);
}());

$.TreeGridPanel = $.extendClass($.TreePanel, $.copy({
	selectRow: $.emptyFn,
	append: $.emptyFn,
	initComponent: function(){
		$.TreePanel.prototype.initComponent.apply(this, arguments);
		$.GridPanel.prototype.initComponent.apply(this, arguments);
	},
	init: function(){
		$.TreePanel.prototype.init.apply(this, arguments);
		$.GridPanel.prototype.init.apply(this, arguments);
	},
	click: function(){
		$.TreePanel.prototype.click.apply(this, arguments);
		$.GridPanel.prototype.click.apply(this, arguments);
	}
}, $.GridPanel.prototype));

$.FormPanel = $.extendClass($.Panel, {
	type: 'formpanel',
	labels: {}
});

$.FormWindow = $.extendClass($.Window, {
	type: 'formwin',
	labels: {}
});

(function(){
	var prototype = {
		/**
		 * 在保存时使用遮罩
		 */
		maskOnSaving: true,
		/**
		 * 允许文本框中展示线索
		 */
		enableHint: false,
		/**
		 * 是否在输入回车后提交执行保存方法
		 */
		saveOnEnter: true,
		/**
		 * 是否允许用字段的title属性来显示输入提示
		 */
		enableTitleHint: true,
		/**
		 * 每次是否验证所有字段，如果是，则在某个字段验证失败后会继续验证其他字段；否则不再验证其他字段
		 * @type Boolean
		 */
		validateAllField: false,
		/**
		 * 每次是否验证字段所有属性，如果是，则在某个属性验证失败后会继续验证字段的其他属性；否则不再验证其他属性。
		 * @type Boolean
		 */
		validateAllAttr: false,
		/**
		 * 验证字段属性
		 * @param {String} a 属性名
		 * @param {String} v 属性值
		 * @param {String} value 字段值
		 * @param {label: String, el: JQEL, value: String, key: String} field 字段
		 * @return {Boolean} 是否合法
		 */
		validateFieldAttr: function(a, v, value, field) {
			if(a == 'required') return value != null && !/^\s*$/.test(value);
			else if(value != null && !/^\s*$/.test(value)) switch(a) {
			case 'length': return value.length == v;
			case 'maxLength': return value.length <= v;
			case 'minLength': return value.length >= v;
			case 'max': return value - 0 <= v - 0;
			case 'min': return value - 0 >= v - 0;
			case 'reg': return new RegExp(v).test(value);
			}
			return true;
		},
		/**
		 * 验证字段
		 * @param {label: String, el: JQEL, value: String, key: String} field
		 * @return {verdict: Boolean, detail: {attr|String: {validate: Boolean, value: String}}} 是否合法 {结论: 合法性, 详细: {属性: {validate: 合法性, value: String}}}
		 */
		validateField: function(field) {
			var a, el = field.el, v = field.value, rst = {}, verdictRst = true, me = this, av;
			$.each(['trim', 'required', 'length', 'maxLength', 'minLength', 'max', 'min', 'reg'], function(i, a){
				av = el.attr(a);
				if(av == null) return;
				if(me.validateFieldAttr(a, av, v, field)) {
					rst[a] = {validate: true, value: av};
				}
				else {
					rst[a] = {validate: false, value: av};
					verdictRst = false;
					if(!me.validateAllAttr) {
						return false;
					}
				}
			});
			return {verdict: verdictRst, detail: rst};
		},
		/**
		 * form验证
		 * @param {} obj 通过form获取到的初始json对象，传递到后台的json对象
		 * @param {} form form表单
		 * @return {} 返回false表示验证失败，验证成功返回传递到后台的json对象，默认返回通过form获取到的初始json对象
		 */
		validate: function(obj, form){
			var el, rst = true, me = this, reg, robj = {}, field = {}, x, a, vd;
			$.each(obj, function(k, v){
				el = form[k];
				if(el.attr('trim') != null) {
					el.val(v = obj[k] = v.trim());
				}
				if(v == el.attr('hint') && el.hasClass('x-input-hint')) v = obj[k] = '';
				field = {
					label: me.getLabel(k),
					key: k,
					el: el,
					value: v
				};
				vd = me.validateField(field);
				if(v == '' && (x = el.attr('hint'))) {
					if(this.enableHint) el.addClass('x-input-hint').val(x);
				}
				if(!vd.verdict) {
					me.invalidFieldMsg(field, vd.detail, obj, form);
					rst = false;
					if(!me.validateAllField) return false;
				}
				else if(!/^\s*$/.test(v) || !me.ingoreBlankField(field)) {
					robj[k] = v;
				}
			});
			if(rst === false) return false;
			return robj;
		},
		getLabel: function(k){
			k = this.el.find('[for="' + k + '"]').html() || '';
			k = k.replace(/\s*/g, '');
			return k || this.labels[k] || '';
		},
		/**
		 * 字段属性验证失败时的消息
		 * @param {} a 字段非法属性
		 * @param {} av 字段非法属性的属性值
		 * @param {JQEL} el 非法字段对应JQ元素
		 * @param {String} n 非法字段name属性值
		 * @param {} obj 原始请求参数
		 * @param {} form 请求表单
		 * @return {String} 返回的消息
		 */
		getInvalidRegAttrMsg: function(a, av, el, n, obj, form) {return '输入不合法';},
		/**
		 * 字段属性验证失败时的消息
		 * @param {label: String, el: JQEL, value: String, validation: int, key: String} field 非法字段
		 * @param {} a 字段非法属性
		 * @param {} av 字段非法属性的属性值
		 * @param {} obj 原始请求参数
		 * @param {} form 请求表单
		 * @return {String} 返回的消息
		 */
		invalidFieldAttrMsg: function(field, a, av, obj, form) {
			switch(a) {
			case 'required': return '不能为空，必须填写';
			case 'length': return av + '个字符';
			case 'maxLength': return '最多' + av + '个字符';
			case 'minLength': return '最少' + av + '个字符';
			case 'max': return '最大' + av;
			case 'min': return '最小' + av;
			case 'reg': return this.getInvalidRegAttrMsg(a, av, field.el, field.key, obj, form);
			}
		},
		/**
		 * 字段验证失败时的消息
		 * @param {label: String, el: JQEL, value: String, key: String} field 非法字段
		 * @param {属性: {validate: 合法性, value: 属性值}} validateRst 非法验证结果 
		 * @param {} obj 原始请求参数
		 * @param {} form 请求表单
		 */
		invalidFieldMsg: function(field, validateRst, obj, form){
			this.el.mask();
			var msg = [], me = this;
			$.each(validateRst, function(a, rst){
				if(!rst.validate) {
					msg.push(me.invalidFieldAttrMsg(field, a, rst.value, obj, form));
				}
			});
			tools.msgbox({
				title: this.invalidFiledMsgTitle(field, obj, form), msg: field.label + '验证失败。<br />' + msg.join('<br />'), modal: false, scope: this, 
				positive: function(){
					this.el.unmask();
					field.el.focus();
				},
				close: function(){
					this.el.unmask();
				}
			});
			return this;
		},
		/**
		 * 获取表单验证失败时的消息标题
		 * @param {label: String, el: JQEL, value: String, key: String} field 非法字段
		 * @param {} obj 原始请求参数
		 * @param {} form 请求表单
		 * @return {String}
		 */
		invalidFiledMsgTitle: function(field, obj, form) {return '';},
		/**
		 * 是否忽略空白值字段
		 * @param {label: String, el: JQEL, key: String, value: String, key: String} field
		 * @return {Boolean}
		 */
		ingoreBlankField: function(field){
			return true;
		},
		/**
		 * 获取字段正则表达式属性线索
		 * @param {String} a 属性名
		 * @param {String} v 属性值
		 * @param {JQEL} f 字段
		 * @param {String} n 字段的name属性
		 * @return {}
		 */
		getRegAttrHint: function(a, v, f, n) {
			return v;
		},
		/**
		 * 获取字段属性线索
		 * @param {String} a 属性名
		 * @param {String} v 属性值
		 * @param {JQEL} f 字段
		 * @param {String} n 字段的name属性
		 * @return {}
		 */
		getAttrHint: function(a, v, f, n) {
			switch(a) {
			case 'required': return '必须填写';
			case 'trim': return '首尾空格无效';
			case 'length': return v + '个字符';
			case 'maxLength': return '最多' + v + '个字符';
			case 'minLength': return '最少' + v + '个字符';
			case 'max': return '最大' + v;
			case 'min': return '最小' + v;
			case 'reg': return this.getRegAttrHint(a, v, f, n);
			}
		},
		/**
		 * 获取字段线索
		 * @param {JQEL} f 字段
		 * @param {String} n 字段的name属性
		 * @return {Array}
		 */
		getHint: function(f, n) {
			var me = this, av;
			var hint = [];
			$.each(['trim', 'length', 'maxLength', 'minLength', 'max', 'min', 'reg'], function(i, a){
				av = f.attr(a);
				if(av == null) return;
				i = me.getAttrHint(a, av, f, n);
				if(i) hint.push(i);
			});
			return hint;
		},
		init: function(){
			if(this.beforeDestroy != $.emptyFn && this.beforeDestroy != $.FormWindow.prototype.beforeDestroy) {
				this._beforeDestroy = this.beforeDestroy;
				delete this.beforeDestroy;
			}
			
			this.form = {};
			this._defaults = {};
			this._initDisabled = {};
			
			var me = this,
				form = this.form,
				dft = this._defaults,
				idis = this._initDisabled,
				hint, fields = this.el.find('[name]');
				
			fields.each(function(i, f){
				f = $(f);
				i = f.attr('name');
				if(/^(checkbox|radio)$/i.test(f.type())) {
					form[i] = form[i] || fields.filter('[name="' + i + '"]');
					dft[i] = form[i].filter(':checked').val();
				}
				else {
					form[i] = f;
					dft[i] = f.val();
				}
				idis[i] = f.disabled();
				
				f.on('focus', function(evt){
					var f = $(this);
					if(f.val() == f.attr('hint')) f.val('');
					f.removeClass('x-input-hint');
					if(f.readOnlyed()) f.click();
					me.onFieldFocus(f, evt);
				}).on('blur', function(evt){
					var f = $(this);
					if(f.val() == '') f.addClass('x-input-hint').val(f.attr('hint'));
					me.onFieldBlur(f, evt);
				});
						
				hint = me.getHint(f, i) || '';
				if(!$.isArray(hint)) hint = [hint];
				if(hint.length > 0) {
					hint = hint.join('<br />');
					if(me.enableHint) {
						f.addClass('x-input-hint').val(hint).attr('hint', hint);
					}
					if(me.enableTitleHint) {
						f.attr({'title': hint, 'data-html': "true"}).tooltip();
					}
				}
			});
			
			this.setDateTimePicker();
			this.buttons = this.el.find('.x-button');
			
			return this;
		},
		defaults: function(dfts){
			if(dfts == null) return this._defaults;
			this._defaults = dfts;
			return this;
		},
		keyup: function(evt){
			var el = $(evt.target);
			if(evt.which == 13) {
				var cbl = $('#combolist');
				if(cbl.hasClass('x-show') && cbl.hasClass('x-option')) {
					var ii = cbl.children('.x-item'), ci = ii.filter('.x-selected');
					ci.click();
					return this;
				}
				if(this.saveOnEnter && !evt.ctrlKey && !evt.shiftKey) {
					this.save();
				}
			}
			this.onFieldKeyup(el, evt);
			return this;
		},
		keydown: function(evt){
			var el = $(evt.target);
			if(evt.which == 40 || evt.which == 38) {
				var cbl = $('#combolist');
				if(cbl.hasClass('x-show') && cbl.hasClass('x-option')) {
					var ii = cbl.children('.x-item'), ci = ii.filter('.x-selected'), ni;
					switch(evt.which) {
					case 40:
						if(ci.length < 1 || ci.next().length < 1) ni = ii.first();
						else ni = ci.next();
						break;
					case 38:
						if(ci.length < 1 || ci.prev().length < 1) ni = ii.last();
						else ni = ci.prev();
						break;
					}
					if(ni != null && ni.length > 0) {
						ii.removeClass('x-selected');
						ni.addClass('x-selected');
						var scrollTop = ni.position().top + ni.offsetHeight() + cbl.scrollTop();
						scrollTop = scrollTop - cbl.innerHeight();
						if(scrollTop > 0) {
							cbl.scrollTop(scrollTop);
						}
						else {
							cbl.scrollTop(0);
						}
					}
				}
				else {
					el.click();
				}
			}
			else if(evt.which == 9) {
				el.mouseup();
			}
			this.onFieldKeydown(el, evt);
			return this;
		},
		/**
		 * function(f, evt){}
		 * 当字段失去焦点时调用，参数为失去焦点的文本域的JQ对象, 事件对象
		 */
		onFieldBlur: $.emptyFn,
		/**
		 * function(f, evt){}
		 * 当字段获取焦点时调用，参数为获取焦点的文本域的JQ对象, 事件对象
		 */
		onFieldFocus: $.emptyFn,
		/**
		 * function(f, evt){}
		 * 当字段按键弹起时调用，参数为获取焦点的文本域的JQ对象, 事件对象
		 */
		onFieldKeyup: $.emptyFn,
		/**
		 * function(f, evt){}
		 * 当字段按键按下时调用，参数为获取焦点的文本域的JQ对象, 事件对象
		 */
		onFieldKeydown: $.emptyFn,
		_beforeDestroy: $.emptyFn,
		setDateTimePicker: function(){
			$.each(this.form, function(i, f){
				switch(f.attr('_type')) {
				case 'datetime':
					f.datetimepicker('remove');
					f.datetimepicker({format: 'yyyy-mm-dd hh:ii:ss', minuteStep: 1}).readOnly(true).next('span.x-icon-datepicker').click(function(){
						$(this).prev().focus();
					});
					break;
				case 'date':
					f.datetimepicker('remove');
					f.datetimepicker({format: 'yyyy-mm-dd', minView: 2}).readOnly(true).next('span.x-icon-datepicker').click(function(){
						$(this).prev().focus();
					});
					break;
				}
			});
			return this;
		},
		beforeDestroy: function() {
			$.each(this.form, function(k, f){
				f.off('focus blur');
				f.datetimepicker('remove');
			});
			delete this.form;
			this._beforeDestroy.apply(this, arguments);
			this.constructor.superclass.beforeDestroy.apply(this, arguments);
			return this;
		},
		getFormValue: function(){
			var obj = {}, me = this;
			$.each(this.form, function(k, f){
				obj[k] = me.getFieldValue(k, f);
			});
			return this.fixedFormValue(obj);
		},
		fixedFormValue: function(obj){
			return obj;
		},
		fixedPostValue: function(obj){
			return obj;
		},
		getFieldValue: function(k, f){
			var t = f.type() || '';
			switch(t.toLowerCase()) {
			case 'radio': return f.filter(':checked').val();
			case 'checkbox': 
				var vs = [];
				f.filter(':checked').each(function(i, f){vs.push($(f).val());});
				return vs;
			default: return f.val();
			}
		},
		getSaveUrl: function(){return this.saveUrl;},
		setSaveUrl: function(url){this.saveUrl = url; return this;},
		/**
		 * 获取请求类型
		 * @param {} obj 请求参数
		 * @param {} form 请求form表单
		 * @return 'post'|'put'
		 */
		getRequestType: function(obj, form){
			return 'post';
		},
		save: function(){
			var vd = this.fixedPostValue(this.validate(this.getFormValue(), this.form));
			if(vd === false) return;
			if(this.maskOnSaving) this.el.mask().progress('数据保存中...');
			$.ajax({
				url: this.getSaveUrl(vd, this.form), data: tools.serializeParams(vd), type: this.getRequestType(vd, this.form), dataType: 'json', context: this, 
				success: function(d){
					if(this.maskOnSaving) this.el.unprogress().unmask();
					if(d.code == 0) {
						if(this.saveSuccess(d.data, vd) !== false) {
							this.close();
						}
					}
					else {
						this.saveError(d.data, vd);
					}
				},
				error: function(){
					if(this.maskOnSaving) this.el.unprogress().unmask();
					this.saveError('请求后台出错。');
				}
			});
			return this;
		},
		/**
		 * function(msg){}
		 * 保存请求后台失败后调用
		 * @param msg 失败的原因
		 */
		saveError: function(msg) {
			tools.msgbox({
				title: '数据保存', msg: '数据保存失败。<br />' + msg, positive: $.emptyFn, modal: false
			});
			return this;
		},
		beforeClose:function(){
			return this.reset();
		},
		/**
		 * 重置form
		 */
		reset: function(){
			this.loadRecord(this._defaults);
			this.resetDisable();
			return this;
		},
		/**
		 * function(responseBody.json, requestBody.json){}
		 * 保存请求后台成功后调用
		 * @param responseBody.json 参数为后台响应的json:Object
		 * @param requestBody.json请求的json:Object
		 * 
		 * 注：后台响应的json中，键state值为非0表示失败
		 * 注：如果该函数返回false，则不会自动关闭窗口
		 */
		saveSuccess: $.emptyFn,
		/**
		 * 加载一条记录到form
		 */
		loadRecord: function(rcd){
			var form = this.form;
			
			$.each(form, function(k, f){
				var t = f.type() || '';
				
				switch(t.toLowerCase()) {
				case 'radio': 
					f.filter('[value="' + rcd[k] + '"]').check(true);
					break;
				case 'checkbox': 
					f.check(false);
					var fs = [];
					$.each(rcd[k] || [], function(i, item){
						fs.push('[value="' + item + '"]');
					});
					if(fs.length > 0) f.filter(fs.join(',')).check(true);
					return vs;
				default: f.val(rcd[k]);
				}
				
			});
			return this;
			
		},
		disable: function(dsb){
			var _set = $.isObject(dsb);
			
			$.each(this.form, function(k, f){
				f.disable(_set ? !!dsb[k] : !!dsb);
			});
			
			if(!_set) {
				if(!dsb) {
					this.buttons.removeClass('x-disabled');
				}
				else {
					this.buttons.filter('[handler!="cancel"]').addClass('x-disabled');
				}
			}
			return this;
		},
		resetDisable: function(){
			this.disable(this._initDisabled);
			this.buttons.removeClass('x-disabled');
			return this;
		}
	};

	$.copy($.FormWindow.prototype, prototype);
	$.copy($.FormPanel.prototype, prototype);
}());


$.SocketClient = $.extendClass(function(opt){
	var constructor = window.WebSocket || window.MozWebSocket;
	if(constructor == null) throw {message: '构造webSocket时错误，浏览器不支持'};
	
	opt = opt || {};
	$.copy(this, opt);
	if(!/^wss?:/.test(this.url)) throw {message: '构造webSocket时错误，url协议不对'};
	
	this.bind();
	
}, {
	bind: function(so){
		var constructor = window.WebSocket || window.MozWebSocket;
		if(constructor == null) return;
	
		var skt = this.__socket__ = new constructor(this.url);
		skt.__owner__ = this;
		
		skt.onopen = function(evt){
			this.__owner__.onopen(evt, this);
		};
		skt.onclose = function(evt){
			this.__owner__.onclose(evt, this);
		};
		skt.onerror = function(evt){
			this.__owner__.onerror(evt, this);
		};
		skt.onmessage = function(evt){
			this.__owner__.onmessage(evt, this);
		};
		
		skt = null;
	},
	unbind: function(){
		try{
			this.__socket__.__owner__ = null;
			this.__socket__.close();
			this.__socket__ = null;
		}
		catch(ex){}
	},
	send: function(msg){
		if(this.__socket__ != null && this.__socket__.readyState > 1) {
			this.unbind();
			this.bind();
		}
		else if(this.__socket__.readyState != 1) {
			$.cycle(function(){
				if(this.__socket__.readyState == 1) {
					this.__socket__.send(msg);
					return false;
				}
			}, this, 0.5);
		}
		else this.__socket__.send(msg);
	},
	destroy: function(){
		this.beforeDestroy();
		this.unbind();
		for(var p in this) {
			if($.isFunction(this[p].destroy)) {
				try{
					this[p].destroy();
				}
				catch(ex){}
			}
			delete this[p];
		}		
	},
	beforeDestroy: $.emptyFn,//function(){}
	onopen: $.emptyFn,		//function(event, webSocketClient){}
	onclose: $.emptyFn,		//function(event, webSocketClient){}
	onerror: $.emptyFn,		//function(event, webSocketClient){}
	onmessage: $.emptyFn	//function(event, webSocketClient){}
});

$.fn.extend({
	cpt: function(opt){
		return new $.Cpt($.copy({el: this}, opt, false, true));
	},
	window: function(opt){
		return new $.Window($.copy({el: this}, opt, false, true));
	},
	tab: function(opt){
		return new $.Tab($.copy({el: this}, opt, false, true));
	},
	panel: function(opt){
		return new $.Panel($.copy({el: this}, opt, false, true));
	},
	treepanel: function(opt){
		return new $.TreePanel($.copy({el: this}, opt, false, true));
	},
	gridpanel: function(opt){
		return new $.GridPanel($.copy({el: this}, opt, false, true));
	},
	drawerpanel: function(opt){
		return new $.DrawerPanel($.copy({el: this}, opt, false, true));
	},
	multidrawerpanel: function(opt){
		return new $.MultiDrawerPanel($.copy({el: this}, opt, false, true));
	},
	formwin: function(opt) {
		return new $.FormWindow($.copy({el: this}, opt, false, true));
	},
	formpanel: function(opt){
		return new $.FormPanel($.copy({el: this}, opt, false, true));
	},
	gridwin: function(opt){
		return new $.GridWindow($.copy({el: this}, opt, false, true));
	},
	treegrid: function(opt){
		return new $.TreeGridPanel($.copy({el: this}, opt, false, true));
	}
});

$.beforeUnload(function(){
	return;
	$.each($.__COMPONENT, function(i, c){
		try{
			c.destroy();	
		}
		catch(ex){
			var a = c;
		}
	});
	delete $.__COMPONENT;
});







