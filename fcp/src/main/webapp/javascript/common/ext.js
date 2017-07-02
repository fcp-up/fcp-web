;(function(){
	String.prototype.trim = String.prototype.trim || function(){return this.replace(/^\s+|\s+$/g, '');}
	$.emptyFn = function(){};
	/**
	 * 文档卸载前执行，只执行一次，传递给本函数的实参会在执行后销毁一次，调用方需要调用后销毁实参一次。
	 * 参数可以是一个函数，也可以是一个对象。如果是一个函数，会被转为一个对象。如果是一个对象，调用函数为对象的fn属性。
	 * 对象可选属性：scope，调用时函数内部this作用域；params，传递给函数的实参。
	 * 若提供了执行函数实参，则把对象和load事件的事件对象追加到实参后面作为后续参数。否则把对象和load事件的事件对象作为函数的两个实参。
	 */
	$.beforeUnload = function(opt){
		if(this.isFunction(opt) && opt != this) {
			var x = opt;
			opt = {};
			opt.fn = x;
		}
		else if(!this.isObject(opt)) return;
		opt.fn = opt.fn || this.emptyFn;
		opt.scope = opt.scope || opt;
		opt.params = opt.params || [];
		$(window).bind('beforeunload', function(evt){
			if(!opt) return;
			opt.params.push(opt, evt);
			opt.fn.apply(opt.scope, opt.params);
			opt = null;
		});
	};
	$.isBool = $.isBool || function(obj){return Object.prototype.toString.apply(obj) == '[object Boolean]';};
	$.isObject = $.isObject || function(obj){return Object.prototype.toString.apply(obj) == '[object Object]';};
	$.isNumeric = $.isNumeric || function(obj){return Object.prototype.toString.apply(obj) == '[object Number]';};
	$.isString = $.isString || function(obj){return Object.prototype.toString.apply(obj) == '[object String]';};
	$.isDate = $.isDate || function(obj){return Object.prototype.toString.apply(obj) == '[object Date]';};
	$.isArray = $.isArray || function(obj){return Object.prototype.toString.apply(obj) == '[object Array]';};
	$.isPrimitive = $.isPrimitive || function(obj){return /^(boolean|string|number)$/i.test(typeof obj);};
	/**
	 * <pre>
	 * 延时执行，参数若干
	 * 1.取第一个function为延时执行的目标函数。
	 * 2.取第一个number为延时时长，单位为秒。
	 * 3.取第一个array为执行时函数参数。
	 * 4.取第一个非以上类型的参数为执行时函数中this作用域。
	 * 
	 * 返回一个周期执行的ID
	 * </pre>
	 */
	$.defer = function(){
		var fn = null, tl = null, scope = null, ps = null;
		var $ = this;
		this.each(arguments, function(i, arg){
			if($.isFunction(arg) && arg != $) fn = fn || arg;
			else if($.isNumeric(arg)) tl = tl || arg;
			else if($.isArray(arg)) ps = ps || arg;
			else if(scope == null) scope = arg;
		});
		if(fn != null) {
			tl = tl || 0;
			ps = ps || [];
			return setTimeout(function(){
				fn.apply(scope || window, ps);
				fn = tl = scope = ps = null;
			}, tl * 1000);
		}
	};
	/**
	 * <pre>
	 * 周期执行，参数若干
	 * 1.取第一个function为周期执行的目标函数。如果该函数返回false，则退出执行。
	 * 2.取第一个number为周期时长，单位为秒。
	 * 3.取第二个number为执行总时间长度，单位为秒。类似超时设置，未提供则不做控制。
	 * 4.取第一个array为执行时函数参数。
	 * 5.取第一个boolean为是否立即执行函数。默认是
	 * 6.取第一个非以上类型的参数为执行时函数中this作用域。
	 * 
	 * 返回一个周期执行的ID
	 * </pre>
	 */
	$.cycle = function(){
		var t = this.__cycleTask = this.__cycleTask || {};
		var fn = null, rl = null, tl = null, scope = null, ps = null, im = null;
		var $ = this, st = new Date().getTime();
		$.each(arguments, function(i, arg){
			if($.isFunction(arg) && arg != $) fn = fn || arg;
			else if($.isNumeric(arg)) {
				if(rl == null) rl = arg;
				else if(tl == null) tl = arg;
			}
			else if($.isArray(arg)) ps = ps || arg;
			else if((arg === true || arg === false) && im == null) im = arg;
			else if(scope == null) scope = arg;
		});
		if(fn != null && rl > 0) {
			ps = ps || [];
			if(im !== false) {
				if(fn.apply(scope || window, ps) === false) return;
				if(tl != null && new Date().getTime() - st > tl * 1000) return;
			}
			if(t[rl] == null) {
				t[rl] = {id: null, tasks: [{fn: fn, scope: scope, ps: ps, tl: tl, st: st}]};
				t[rl].id = setInterval(function(c){
					return function(){
						var ts = $.__cycleTask[c].tasks, i = ts.length, x;
						if(i == 0) {
							clearInterval($.__cycleTask[c].id);
							delete $.__cycleTask[c];
						}
						else for(i = i - 1; i > -1; i--) {
							x = ts[i];
							if(
								x.fn.apply(x.scope || window, x.ps) === false || 
								(x.tl != null && new Date().getTime() - x.st > x.tl * 1000)
							) {
								ts.splice(i, 1);
							}
						}
					};
				}(rl), rl * 1000);
			}
			else {
				var idx = -1;
				for(var i = 0, il = t[rl].tasks.length; i < il; i++) {
					im = t[rl].tasks[i];
					if(im.scope == scope && im.fn + '' == fn + '') {
						idx = i;
						break;
					}
				}
				if(idx < 0) t[rl].tasks.push({fn: fn, scope: scope, ps: ps, tl: tl, st: new Date().getTime()});
			}
		}
		t = null;
	};
	$.progress = function(el, text){
		if(!el.append) el = $(el);
		var pe = el.children('.progress');
		if(pe.length > 0) pe.html(text);
		else el.append('<div class="progress">' + text + '</div>');
	};
	$.unprogress = function(el){
		if(!el.append) el = $(el);
		el.children('.progress').remove();
	};
	$.mask = function(el){
		if(!el.append) el = $(el);
		if(el.children('.masker').length < 1) el.append('<div class="masker"></div>');
	};
	$.unmask = function(el){
		if(!el.append) el = $(el);
		el.children('.masker').remove();
	};
	$.replaceClass = function(el, ocn, ncn){
		var cn = el.className || '', reg = new RegExp('(^|\\s+)' + ocn + '($|\\s+)', 'g');
		if(reg.test(cn)) {
			cn = cn.replace(reg, '$1' + ncn + '$2');
		}
		else {
			cn += ' ' + ncn;
		}
		el.className = cn.trim();
	};
	$.setClass = function(el, cn){
		el.className = cn.trim();
	};
	
	
	$.fn.extend({
		innerHeight: function(){
			return this[0] && this[0].clientHeight || null;
		},
		offsetHeight: function(){
			return this[0] && this[0].offsetHeight || null;
		},
		progress: function(text){
			$.progress(this, text);
			return this;
		},
		unprogress: function(text){
			$.unprogress(this);
			return this;
		},
		mask: function(){
			$.mask(this);
			return this;
		},
		unmask: function(){
			$.unmask(this);
			return this;
		},
		replaceClass: function(ocn, ncn){
			this.each(function(i, e){$.replaceClass(e, ocn, ncn);});
			return this;
		},
		setClass: function(cn){
			this.each(function(i, e){$.setClass(e, cn);});
			return this;
		},
		className: function(){
			return this[0] && this[0].className || '';
		},
		type: function(){return this[0] && this[0].type || '';},
		nodeName: function(){return this[0] && this[0].nodeName || '';},
		check:function(checked){
			if(checked == null) checked = true;
			this.each(function(i, e){e.checked = checked;});
			return this;
		},
		checked: function(){return this[0] && this[0].checked;},
		disable: function(disabled){
			if(disabled == null) disabled = true;
			this.each(function(i, e){e.disabled = disabled;});
			return this;		
		},
		disabled: function(){return this[0] && this[0].disabled;},
		scrollHeight: function(){return this[0] && this[0].scrollHeight || 0;},
		scrollWidth: function(){return this[0] && this[0].scrollWidth || 0;},
		horizontalScrollBarHeight: function(){
			var x = this[0];
			if(!x) return 0;
			return x.offsetHeight - x.clientHeight;
		},
		verticalScrollBarWidth: function(){
			var x = this[0];
			if(!x) return 0;
			return x.offsetWidth - x.clientWidth;
		},
		readOnly: function(readOnly){
			if(readOnly == null) readOnly = true;
			this.each(function(i, e){e.readOnly = readOnly;});
			return this;
		},
		readOnlyed: function(){return this[0] && this[0].readOnly;},
		indeterminate: function(im){
			if(im == null) im = true;
			this.each(function(i, e){e.indeterminate = im;});
			return this;
		},
		indeterminated: function(){return this[0] && this[0].indeterminate;},
		attrs: function(){
			var n = this[0];
			if(n == null) return null;
			n = n.attributes;
			var map = {}, an;
			for(var i = 0, il = n.length; i < il; i++) {
				an = n.item(i);
				map[an.nodeName] = an.nodeValue;
				//IE7 html attribute and user define attribute
			}
			return map;
		}
	});
	
	$.ie7 = /msie\s*7/gi.test(navigator.appVersion);
	$.ie6 = /msie\s*6/gi.test(navigator.appVersion);
	$.ie8 = /msie\s*8/gi.test(navigator.appVersion);
		
	/**
	 * <pre>
	 * 迭代一个对象的属性，参数若干
	 * 1.取第一个函数参数为迭代函数。
	 * 2.取第一个boolean参数为是否迭代原型链上的属性，默认为不迭代。
	 * 3.取第一个非函数、非boolean参数为迭代对象。
	 * 4.后续第一个非boolean、非函数的参数为迭代时迭代函数的this作用域，默认为迭代对象。
	 * 5.若还有后续参数，则所有后续参数将作为后续参数传递到遍历函数中。
	 * 
	 * 遍历函数前3个参数：属性名、属性值、迭代对象
	 * 
	 * 如果迭代函数处理任意一个属性返回false将退出迭代。
	 * </pre>
	 */
	$.iterate = function(){
		var obj = null, fn = null, ip = null, s = null, fp = [null, null, null], me = this;
		
		this.each(arguments, function(k, i){
			if(me.isFunction(i) && i != me) fn = fn || i;
			else if(me.isBool(i)) {
				if(ip == null) ip = ip || i;
			}
			else if(obj == null) obj = obj || i;
			else if(s == null) s = i;
			else fp.push(i);
		});

		for(var p in obj) if(obj.hasOwnProperty(p) || ip) {
			fp[0] = p; fp[1] = obj[p]; fp[2] = obj;
			if(fn.apply(s || obj, fp) === false) break;
		}
	};
	/**
	 * 复制另外一个对象到目标对象上，返回处理后的目标对象
	 * @param obj 目标对象
	 * @param nobj 被复制对象
	 * @param cp 是否复制原型属性，默认不复制
	 * @param ovd 是否覆盖同名属性，默认不覆盖
	 * 
	 * @return obj返回目标对象
	 */
	$.copy = function(obj, nobj, cp, ovd){
		var oc = obj.constructor;
		if(oc == null) oc = Object.prototype.constructor;
		this.iterate(nobj, cp, function(k, v){
			if(ovd || !obj.hasOwnProperty(k)) {
				obj[k] = v;
				return;
				if(v == null || $.isPrimitive(v) || $.isFunction(v)) obj[k] = v;
				else if($.isDate(v)) obj[k] = new Date(v.getTime());
				else if($.isArray(v)) {
					obj[k] = [];
					$.each(v, function(i, x){
						if(x == null || $.isPrimitive(x) || $.isFunction(x)) obj[k][i] = x;
					});
				}
				else if($.isObject(v)) obj[k] = $.copy({}, v);
			}
		});
		obj.constructor = oc;
		return obj;
	};
	
	/**
	 * 扩展类：
	 * 原型1：extendClass(Function sub, Function sup, Object prototype)：
	 * 		如果子类sub已经存在一个超类，那么会把伪父类sup的原型去同名覆盖到sub类的原型上。
	 * 		如果子类sub不存在超类，那么会使得伪父类sup变为sub的父类。sub的superclass为sup.prototype。
	 * 		如果提供了prototype参数，则把prototye含同名覆盖到sub类的原型上。
	 * 原型2：extendClass(Function sup, Object prototype)：
	 * 		会产生父类sup的一个子类，这个子类的原型为prototype覆盖过的一个sup实例。子类的superclass为sup.prototype。
	 * 		如果提供的prototype不是一个Object实例，则子类的构造函数为prototype的构造函数，否则子类的构造函数为一个新函数。
	 */
	$.extendClass = function(){
		var sub = null, sup = null, p = null, me = this;
		this.each(arguments, function(i, a){
			if(me.isFunction(a) && a != me){
				if(sub == null) sub = a;
				else if(sup == null) sup = a;
			}
			else if(me.isObject(a)) p = p || a;
		});
		
		if(sub == null) return null;
		if(sup == null && p == null) return sub;
		
		if(sup == null) {
			var oc = Object.prototype.constructor;
			sup = sub;
			sub = p.constructor == oc ? function(){arguments.callee.superclass.constructor.apply(this, arguments);} : p.constructor;
			oc = null;
		}
		
		if(sup.prototype.constructor != sup) sup.prototype.constructor = sup;
		if(sub.superclass) {
			this.copy(sub.prototype, sup.prototype);
		}
		else {
			var tmp = sub.prototype;
			try{
				sub.prototype = new sup();
			}
			catch(ex){
				var F = function(){}; F.prototype = sup.prototype;
				sub.prototype = new F();
				F = null;
			}
			this.copy(sub.prototype, tmp);
			sub.superclass = sup.prototype;
			tmp = null;
		}
		this.copy(sub.prototype, p, false, true);
		sub.prototype.constructor = sub;
		return sub;
	};
	
	//ajax请求拦截
	$.__ajax = $.ajax;
	$.ajax = function(opt){
		var os = opt.success || $.emptyFn,
			oe = opt.error || $.emptyFn;
		if(/^(put|delete)$/i.test(opt.type)) {
			opt.data = opt.data || {};
			opt.data._method = opt.type;
			opt.type = 'post';
		}
		opt.cache = false;
		opt.success = function(d){
			var x = d;
			if(/AC_TIMEOUT|AC_INVALID/gi.test(x)) {
				lamp.msgbox({title: '使用提示', msg: '登录超时，请重新登录', positive: function(){
					window.top.location.href = lamp.root + '/login';
				}});
			}
			else os.apply(opt.context || window, arguments);
		};
		opt.error = function(xhr,status,error){
			var x = xhr.responseText;
			if(/AC_TIMEOUT|AC_INVALID/gi.test(x)) {
				lamp.msgbox({title: '使用提示', msg: '登录超时，请重新登录', positive: function(){
					window.top.location.href = lamp.root + '/login';
				}});
			}
			else oe.apply(opt.context || window, arguments);
		};
		this.__ajax.call(this, opt);
	};

	$.depend$ui = function(){
		var ps = Array.prototype.slice.call(arguments);
		ps.unshift($.ui ? [] : ['javascript/jquery-ui/jquery-ui.min.js', 'javascript/jquery-ui/jquery-ui.min.css']);
		$.depend.apply(this, ps);
		ps = null;
	};
	
	$.dependTimepicker = function(){
		var ps = Array.prototype.slice.call(arguments);
		ps.unshift(($.ui && $.ui.timepicker) ? [] : ['javascript/timepicker/jquery-ui-timepicker-addon-min.js', 'javascript/timepicker/jquery-ui-timepicker-addon.css']);
		$.depend$ui(this, function(){
			$.depend.apply(this, ps);
			ps = null;
		});
	};
	
	/**
	 * 前台依赖加载
	 * 1.取第一个数组作为待加载文件。
	 * 2.取第一个函数作为加载完毕后的回调函数。
	 * 3.取第二个数组作为回调函数的实参。
	 * 4.取不满足上述条件的后续第一个参数作为回调函数的作用域。
	 */
	$.depend = function(){
		//status: {undefined: 未加载, 1: 正在加载, 2: 已加载}
		var c = $.depend.cache = $.depend.cache || {};
		var fs, cb, cs, cp;
		$.each(arguments, function(i, a){
			if($.isFunction(a) && a != $) {
				if(cb == null) cb = a;
				else cs = cs || a;
			}
			else if($.isArray(a)) {
				if(fs == null) fs = a;
				else if(ps == null) ps = a;
				else cs = cs || a;
			}
			else cs = cs || a;
		});
		
		if(!fs || !fs.length) {
			if(cb) cb.apply(cs || window, cp || []);
			return;
		}
		
		var map = {}, checkCpt = function(){
			for(var x in map) if(!map[x]) return;
			if(cb) cb.apply(cs || window, cp || []);
			fs = cb = cs = cp = c = map = checkCpt = dom = null;
		}, dom;
		this.each(fs, function(i, f){
			map[f] = false;
		});
		
		this.each(fs, function(i, f){
			if(c[f] == 2) {
				map[f] = true;
				checkCpt();
			}
			else if(c[f] == 1) {
				$.cycle(function(){
					if(c[f] == 2) {
						map[f] = true;
						checkCpt();
						return false;
					}
				}, 0.05);
			}
			else {
				c[f] = 1;
				if(/js(\?.*)?$/i.test(f)) {
					dom = document.createElement('script');
					dom.src = f;
				}
				else if(/css(\?.*)?$/i.test(f)) {
					dom = document.createElement('link');
					dom.href = f;
					dom.rel = "stylesheet";
				}
				dom.onload = dom.onreadystatechange = function(){
					if(!this.readyState || /complete|loaded/.test(this.readyState)){
						this.onload = this.onreadystatechange = null;
						c[f] = 2;
						map[f] = true;
						checkCpt();
					}
				};
				
				document.body.appendChild(dom);
			}
		});
		
	};

	/**
	 * 获取一个按键事件的按键字面值，不考虑输入法，不考虑大写锁定键。
	 * 不检查事件类型，这个方法只在按键事件中有效，其他事件有些也能用，但结果是错的。
	 */
	$.Event.prototype.getText = function(){
		var c = this.which, s = this.shiftKey, t = this.type;
		switch(c){
		case 48: return s ? ')' : '0';
		case 49: return s ? '!' : '1';
		case 50: return s ? '@' : '2';
		case 51: return s ? '#' : '3';
		case 52: return s ? '$' : '4';
		case 53: return s ? '%' : '5';
		case 54: return s ? '^' : '6';
		case 55: return s ? '&' : '7';
		case 56: return s ? '*' : '8';
		case 57: return s ? '(' : '9';
		case 96: return s ? null : '0';
		case 97: return s ? null : '1';
		case 98: return s ? null : '2';
		case 99: return s ? null : '3';
		case 100: return s ? null : '4';
		case 101: return s ? null : '5';
		case 102: return s ? null : '6';
		case 103: return s ? null : '7';
		case 104: return s ? null : '8';
		case 105: return s ? null : '9';
		
		case 190: return s ? '>' : '.';
		case 110: return s ? null : '.';
		}

		if(c > 64 && c < 91) {
			if(!s) c = c + 32;
			return String.fromCharCode(c);
		}
		else if(c == 32 || c == 229) {
			return ' ';
		}
		return null;
	};
}());

(function(){
	//类型扩展
	$.copy(Date.prototype, {
		year: function(y){
			if(isNaN(y)) return this.getFullYear();
			else this.setFullYear(y);
			return this;
		},
		month: function(m) {
			if(isNaN(m)) return this.getMonth();
			else this.setMonth(m);
			return this;
		},
		date: function(d) {
			if(isNaN(d)) return this.getDate();
			else this.setDate(d);
			return this;
		},
		hour: function(h) {
			if(isNaN(h)) return this.getHours();
			else this.setHours(h);
			return this;
		},
		minute: function(h) {
			if(isNaN(h)) return this.getMinutes();
			else this.setMinutes(h);
			return this;
		},
		second: function(h) {
			if(isNaN(h)) return this.getSeconds();
			else this.setSeconds(h);
			return this;
		},
		//时间格式化，格式化模式：yyyy MM dd HH mm ss，默认为yyyy-MM-dd HH:mm:ss
		format: function(f){
			f = f || 'yyyy-MM-dd HH:mm:ss';
			var me = this,
			r = f.replace(/(yyyy|MM|dd|HH|mm|ss)/g, function(a, b){
				switch(b) {
				case 'yyyy':	return me.getFullYear();
				case 'MM':		return (me.getMonth() + 1 + '').replace(/^(\d)$/, '0$1');
				case 'dd':		return (me.getDate() + '').replace(/^(\d)$/, '0$1');
				case 'HH':		return (me.getHours() + '').replace(/^(\d)$/, '0$1');
				case 'mm':		return (me.getMinutes() + '').replace(/^(\d)$/, '0$1');
				case 'ss':		return (me.getSeconds() + '').replace(/^(\d)$/, '0$1');
				}
			});
			me = null;
			return r;
		},
		//复制时间
		clone: function(){return new Date(this.getTime());},
		//时间增减操作
		add: function(map){
			var n = this.clone(), m, d, dl = 24 * 3600000;
			$.each(map, function(k, v){
				switch(k) {
				case 'yyyy':
					m = n.getMonth();
					n.setFullYear(n.getFullYear() + v);
					if(n.getMonth() != m) {
						n = new Date(n.getTime() - dl);
					}
					break;
				case 'MM':
					m = n.getMonth();
					d = m + v;
					m = (d / 12) >> 0;
					if(d < 0 && d % 12 != 0) m -= 1;
					n.setFullYear(n.getFullYear() + m);
					d = d % 12;
					if(d < 0) d += 12;
					n.setMonth(d);
					if(n.getMonth() != d) {
						n.setDate(1);
						n = new Date(n.getTime() - dl);
					}
					break;
				case 'dd':
					n = new Date(n.getTime() + v * dl);
					break;
				case 'HH':
					n = new Date(n.getTime() + v * 3600000);
					break;
				case 'mm':
					n = new Date(n.getTime() + v * 60000);
					break;
				case 'ss':
					n = new Date(n.getTime() + v * 1000);
					break;
				}
			});
			return n;
		}
	}, false, null);
	
	/**
	 * 数字格式化
	 * @params l 小数部分位数；若l为字符串，表示数字补，针对整数，不足位数左补0，最终数字字符串长度与l的长度一致
	 * @params s 千位符显示控制符，若为空，则不存在千位符控制
	 */
	Number.prototype.format = function(l, s){
		if(isNaN(this)) return 'NaN';
		if(arguments.length < 1) return this + '';
		if(Object.prototype.toString.apply(l) == '[object String]') return (l.replace(/./g, '0') + this).replace(new RegExp('^0+(\\d*\\d{' + l.length + '})$'), '$1');
		l = this.toFixed(l) + '';
		s = s == null ? '' : s;
		return l.indexOf('.') > -1 ? l.replace(/(?!^)(?=(\d{3})+\.\d+$)/g, s) : l.replace(/(?!^)(?=(\d{3})+$)/g, s);
	};
	
	String.prototype.trim = String.prototype.trim || function(){
		return this.replace(/^\s+|\s+$/, '');
	};
	
	Function.prototype.defer = function(){
		var args = [this];
		$.each(arguments, function(i, arg){
			if(!$.isFunction(arg)) args.push(arg);
		});
		$.defer.apply($, args);
	};
}());

	
	
	
	
	
	
	
	
	
	