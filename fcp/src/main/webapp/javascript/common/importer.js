$('#importerWin .file-field-wrap>input').change(function(e){
	e = $(this);
	e.prevAll('.filename').html(e.val().replace(/.*?([^\/\\]+)$/g, '$1'));
});
var importer = $('#importerWin').window({
	initComponent: function(){
		var body = this.el.children('.panel-body').children();
		this.hint = body.eq(0);
		this.form = body.eq(1);
		this.fileNameSpan = this.form.find('.filename');
		this.fileLabel = this.form.find('.label');
		this.fileField = this.form.find('[type="file"]');
	},
	reset: function(){
		this.fileNameSpan.html('');
		this.form[0].reset();
	},
	/**
	 * 导入
	 * @param {} opt 配置
	 * 必选配置
	 * url: ''
	 * 可选配置
	 * hint String: 导入窗口提示
	 * fileLabel String: 导入选择文件的标签
	 * validate Function(win): 验证函数，默认验证为判断是否选择了xls文件，参数为导入窗口
	 * beforeClose Function(win): 关闭导入窗口时执行，参数为导入窗口
	 * fixRspRst Function(responseText, win, frm): 修正请求响应结果，请求响应结果为字符串，函数参数为该字符串、导入窗口、接受响应结果的iframe。默认行为为将该字符串做json转换，转为javascript对象
	 * process Function(response, win, frm): 导入请求后处理，参数有三个，分别为请求响应结果、导入窗口、接受响应结果的iframe
	 * scope Object: 以上函数执行时函数作用域，默认为导入窗口
	 * accept String: 允许导入的文件类型，文件后缀名带.，多个以;分割
	 * params String: 附带参数
	 */
	imports: function(opt){
		opt = opt || {};
		if(!opt.url) return;
		if(opt.hint) this.hint.html(opt.hint);
		if(opt.fileLabel) this.fileLabel.html(opt.fileLabel);
		if(opt.accept) this.fileField.attr('accept', opt.accept);
		this._validate = opt.validate;
		this._beforeClose = opt.beforeClose;
		this._process = opt.process;
		this._fixRspRst = opt.fixRspRst;
		this.scope = opt.scope || this;
		this.form.attr('action', opt.url);
		if(opt.params){
			this.el.find('[name="params"]').val(opt.params);
		}
		else {
			this.el.find('[name="params"]').val('');
		}
		this.open();
	},
	scope: null,
	_validate: null,
	_beforeClose: null,
	_process: null,
	_fixRspRst: null,
	beforeClose: function(){
		if($.isFunction(this._beforeClose)) this._beforeClose.call(this.scope, this);
		this._validate = null;
		this._beforeClose = null;
		this._process = null;
		this._fixRspRst = null;
		this.scope = null;
		this.reset();
	},
	validate: function(){
		if($.isFunction(this._validate)) return this._validate.call(this.scope, this);
		if(!/\.xls$/i.test(this.fileNameSpan.html().replace(/\&nbsp;/gi, ''))) {
			tools.msgbox({
				title: '导入信息', msg: '只能使用excel文件，请重新选择', modal: false, scope: this, positive: function(){
					this.reset();
					if(!$.ie7 && !$.ie8 && !$.ie6) this.el.find('input').click();
				}
			});
			return false;
		}
		return true;
	},	
	save: function(){
		var me = this, el = me.el;
		if(!this.validate()) {
			return;
		}
		el.mask().progress('文件处理中...');
		tools.onUpload = function(frm){
			var importResult;
			try {
				var rst = frm.contentWindow.document.body.innerHTML.replace(/<[^>]+\s*\/?>/gi, '');
				el.unprogress().unmask();
				var paramsValue = me.el.find('[name="params"]').val();
				me.reset();
				me.el.find('[name="params"]').val(paramsValue);
				
				if($.isFunction(me._fixRspRst)) rst = me._fixRspRst.call(me.scope || me, rst, me, frm);
				else rst = JSON.parse(rst);
				
				if($.isFunction(me._process)) me._process.call(me.scope || me, rst, me, frm);
			}
			catch(ex) {
				el.unprogress().unmask();
				if($.isFunction(me._process)) me._process.call(me.scope || me, {state: -999}, me, frm);
			}
		};
		this.form[0].submit();
	},
	cancel: function(){
		this.reset();
		this.close();
	}
});