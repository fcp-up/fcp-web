var MessageDigest=function(){
	var MD5={
		chrsz:8,
		b64pad:'',
		hexcase:0,
		core:function(x,len){
			x[len >> 5] |= 0x80 << ((len) % 32);
			x[(((len + 64) >>> 9) << 4) + 14] = len;
			var a =  1732584193;
			var b = -271733879;
			var c = -1732584194;
			var d =  271733878;
			
			for(var i = 0; i < x.length; i += 16){
				var olda = a;
				var oldb = b;
				var oldc = c;
				var oldd = d;
				
				a = this.ff(a, b, c, d, x[i+ 0], 7 , -680876936);
				d = this.ff(d, a, b, c, x[i+ 1], 12, -389564586);
				c = this.ff(c, d, a, b, x[i+ 2], 17,  606105819);
				b = this.ff(b, c, d, a, x[i+ 3], 22, -1044525330);
				a = this.ff(a, b, c, d, x[i+ 4], 7 , -176418897);
				d = this.ff(d, a, b, c, x[i+ 5], 12,  1200080426);
				c = this.ff(c, d, a, b, x[i+ 6], 17, -1473231341);
				b = this.ff(b, c, d, a, x[i+ 7], 22, -45705983);
				a = this.ff(a, b, c, d, x[i+ 8], 7 ,  1770035416);
				d = this.ff(d, a, b, c, x[i+ 9], 12, -1958414417);
				c = this.ff(c, d, a, b, x[i+10], 17, -42063);
				b = this.ff(b, c, d, a, x[i+11], 22, -1990404162);
				a = this.ff(a, b, c, d, x[i+12], 7 ,  1804603682);
				d = this.ff(d, a, b, c, x[i+13], 12, -40341101);
				c = this.ff(c, d, a, b, x[i+14], 17, -1502002290);
				b = this.ff(b, c, d, a, x[i+15], 22,  1236535329);
				
				a = this.gg(a, b, c, d, x[i+ 1], 5 , -165796510);
				d = this.gg(d, a, b, c, x[i+ 6], 9 , -1069501632);
				c = this.gg(c, d, a, b, x[i+11], 14,  643717713);
				b = this.gg(b, c, d, a, x[i+ 0], 20, -373897302);
				a = this.gg(a, b, c, d, x[i+ 5], 5 , -701558691);
				d = this.gg(d, a, b, c, x[i+10], 9 ,  38016083);
				c = this.gg(c, d, a, b, x[i+15], 14, -660478335);
				b = this.gg(b, c, d, a, x[i+ 4], 20, -405537848);
				a = this.gg(a, b, c, d, x[i+ 9], 5 ,  568446438);
				d = this.gg(d, a, b, c, x[i+14], 9 , -1019803690);
				c = this.gg(c, d, a, b, x[i+ 3], 14, -187363961);
				b = this.gg(b, c, d, a, x[i+ 8], 20,  1163531501);
				a = this.gg(a, b, c, d, x[i+13], 5 , -1444681467);
				d = this.gg(d, a, b, c, x[i+ 2], 9 , -51403784);
				c = this.gg(c, d, a, b, x[i+ 7], 14,  1735328473);
				b = this.gg(b, c, d, a, x[i+12], 20, -1926607734);
				
				a = this.hh(a, b, c, d, x[i+ 5], 4 , -378558);
				d = this.hh(d, a, b, c, x[i+ 8], 11, -2022574463);
				c = this.hh(c, d, a, b, x[i+11], 16,  1839030562);
				b = this.hh(b, c, d, a, x[i+14], 23, -35309556);
				a = this.hh(a, b, c, d, x[i+ 1], 4 , -1530992060);
				d = this.hh(d, a, b, c, x[i+ 4], 11,  1272893353);
				c = this.hh(c, d, a, b, x[i+ 7], 16, -155497632);
				b = this.hh(b, c, d, a, x[i+10], 23, -1094730640);
				a = this.hh(a, b, c, d, x[i+13], 4 ,  681279174);
				d = this.hh(d, a, b, c, x[i+ 0], 11, -358537222);
				c = this.hh(c, d, a, b, x[i+ 3], 16, -722521979);
				b = this.hh(b, c, d, a, x[i+ 6], 23,  76029189);
				a = this.hh(a, b, c, d, x[i+ 9], 4 , -640364487);
				d = this.hh(d, a, b, c, x[i+12], 11, -421815835);
				c = this.hh(c, d, a, b, x[i+15], 16,  530742520);
				b = this.hh(b, c, d, a, x[i+ 2], 23, -995338651);
				
				a = this.ii(a, b, c, d, x[i+ 0], 6 , -198630844);
				d = this.ii(d, a, b, c, x[i+ 7], 10,  1126891415);
				c = this.ii(c, d, a, b, x[i+14], 15, -1416354905);
				b = this.ii(b, c, d, a, x[i+ 5], 21, -57434055);
				a = this.ii(a, b, c, d, x[i+12], 6 ,  1700485571);
				d = this.ii(d, a, b, c, x[i+ 3], 10, -1894986606);
				c = this.ii(c, d, a, b, x[i+10], 15, -1051523);
				b = this.ii(b, c, d, a, x[i+ 1], 21, -2054922799);
				a = this.ii(a, b, c, d, x[i+ 8], 6 ,  1873313359);
				d = this.ii(d, a, b, c, x[i+15], 10, -30611744);
				c = this.ii(c, d, a, b, x[i+ 6], 15, -1560198380);
				b = this.ii(b, c, d, a, x[i+13], 21,  1309151649);
				a = this.ii(a, b, c, d, x[i+ 4], 6 , -145523070);
				d = this.ii(d, a, b, c, x[i+11], 10, -1120210379);
				c = this.ii(c, d, a, b, x[i+ 2], 15,  718787259);
				b = this.ii(b, c, d, a, x[i+ 9], 21, -343485551);
				
				a = this.safe_add(a, olda);
				b = this.safe_add(b, oldb);
				c = this.safe_add(c, oldc);
				d = this.safe_add(d, oldd);
			}
			return Array(a, b, c, d);
		},
		core_hmac:function(key, data){
			var bkey = this.str2binl(key);
			if(bkey.length > 16) bkey = core_md5(bkey, key.length * this.chrsz);
			
			var ipad = Array(16), opad = Array(16);
			for(var i = 0; i < 16; i++){
				ipad[i] = bkey[i] ^ 0x36363636;
				opad[i] = bkey[i] ^ 0x5C5C5C5C;
			}
			
			var hash = this.core(ipad.concat(this.str2binl(data)), 512 + data.length * this.chrsz);
			return this.core(opad.concat(hash), 512 + 128);
		},
		safe_add:function(x, y){
			var lsw = (x & 0xFFFF) + (y & 0xFFFF);
			var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
			return (msw << 16) | (lsw & 0xFFFF);
		},
		bit_rol:function(num, cnt){
			return (num << cnt) | (num >>> (32 - cnt));
		},
		ii:function(a, b, c, d, x, s, t){
			return this.cmn(c ^ (b | (~d)), a, b, x, s, t);
		},
		hh:function(a, b, c, d, x, s, t){
			return this.cmn(b ^ c ^ d, a, b, x, s, t);
		},
		gg:function(a, b, c, d, x, s, t){
			return this.cmn((b & d) | (c & (~d)), a, b, x, s, t);
		},
		ff:function(a, b, c, d, x, s, t){
			return this.cmn((b & c) | ((~b) & d), a, b, x, s, t);
		},
		cmn:function(q, a, b, x, s, t){
			return this.safe_add(this.bit_rol(this.safe_add(this.safe_add(a, q), this.safe_add(x, t)), s),b);
		},
		str2binl:function(str){
			var bin = Array();
			var mask = (1 << this.chrsz) - 1;
			for(var i = 0; i < str.length * this.chrsz; i += this.chrsz)
				bin[i>>5] |= (str.charCodeAt(i / this.chrsz) & mask) << (i%32);
			return bin;
		},
		binl2str:function(bin){
			var str = "";
			var mask = (1 << this.chrsz) - 1;
			for(var i = 0; i < bin.length * 32; i += this.chrsz)
				str += String.fromCharCode((bin[i>>5] >>> (i % 32)) & mask);
			return str;
		},
		binl2hex:function(binarray){
			var hex_tab = this.hexcase ? "0123456789ABCDEF" : "0123456789abcdef";
			var str = "";
			for(var i = 0; i < binarray.length * 4; i++){
				str += hex_tab.charAt((binarray[i>>2] >> ((i%4)*8+4)) & 0xF) +
					hex_tab.charAt((binarray[i>>2] >> ((i%4)*8  )) & 0xF);
			}
			return str;
		},
		binl2b64:function(binarray){
			var tab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
			var str = "";
			for(var i = 0; i < binarray.length * 4; i += 3){
				var triplet = (((binarray[i   >> 2] >> 8 * ( i   %4)) & 0xFF) << 16)
					| (((binarray[i+1 >> 2] >> 8 * ((i+1)%4)) & 0xFF) << 8 )
					|  ((binarray[i+2 >> 2] >> 8 * ((i+2)%4)) & 0xFF);
				for(var j = 0; j < 4; j++){
					if(i * 8 + j * 6 > binarray.length * 32) str += this.b64pad;
					else str += tab.charAt((triplet >> 6*(3-j)) & 0x3F);
				}
			}
			return str;
		},
//		hex_hmac:function(key, data) { return this.binl2hex(this.core_hmac(key, data)); },
//		b64_hmac:function(key, data) { return this.binl2b64(this.core_hmac(key, data)); },
//		str_hmac:function(key, data) { return this.binl2str(this.core_hmac(key, data)); },
		
		hex:function(s){return this.binl2hex(this.core(this.str2binl(s), s.length * this.chrsz));},
//		b64:function(s){return this.binl2b64(this.core(this.str2binl(s), s.length * this.chrsz));},
//		str:function(s){return this.binl2str(this.core(this.str2binl(s), s.length * this.chrsz));}
		str:function(s){return this.hex(s).substring(8,24)}
	};
	var SHA1={
		chrsz:8,
		b64pad:'',
		hexcase:0,
		core:function(x,len){
			x[len >> 5] |= 0x80 << (24 - len % 32);
			x[((len + 64 >> 9) << 4) + 15] = len;
			
			var w = Array(80);
			var a =  1732584193;
			var b = -271733879;
			var c = -1732584194;
			var d =  271733878;
			var e = -1009589776;
			
			for(var i = 0; i < x.length; i += 16){
				var olda = a;
				var oldb = b;
				var oldc = c;
				var oldd = d;
				var olde = e;
				
				for(var j = 0; j < 80; j++){
					if(j < 16) w[j] = x[i + j];
					else w[j] = this.rol(w[j-3] ^ w[j-8] ^ w[j-14] ^ w[j-16], 1);
					var t = this.safe_add(this.safe_add(this.rol(a, 5), this.ft(j, b, c, d)),
					this.safe_add(this.safe_add(e, w[j]), this.kt(j)));
					e = d;
					d = c;
					c = this.rol(b, 30);
					b = a;
					a = t;
				}
				
				a = this.safe_add(a, olda);
				b = this.safe_add(b, oldb);
				c = this.safe_add(c, oldc);
				d = this.safe_add(d, oldd);
				e = this.safe_add(e, olde);
			}
			return Array(a, b, c, d, e);
		},
		ft:function(t, b, c, d){
			if(t < 20) return (b & c) | ((~b) & d);
			if(t < 40) return b ^ c ^ d;
			if(t < 60) return (b & c) | (b & d) | (c & d);
			return b ^ c ^ d;
		},
		kt:function(t){
			return (t < 20) ?  1518500249 : (t < 40) ?  1859775393 :
				(t < 60) ? -1894007588 : -899497514;
		},
		core_hmac:function(key, data){
			var bkey = this.str2binb(key);
			if(bkey.length > 16) bkey = this.core(bkey, key.length * this.chrsz);
			
			var ipad = Array(16), opad = Array(16);
			for(var i = 0; i < 16; i++){
				ipad[i] = bkey[i] ^ 0x36363636;
				opad[i] = bkey[i] ^ 0x5C5C5C5C;
			}
			
			var hash = this.core(ipad.concat(this.str2binb(data)), 512 + data.length * this.chrsz);
			return this.core(opad.concat(hash), 512 + 160);
		},
		safe_add:function(x, y){
			var lsw = (x & 0xFFFF) + (y & 0xFFFF);
			var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
			return (msw << 16) | (lsw & 0xFFFF);
		},
		rol:function(num, cnt){
			return (num << cnt) | (num >>> (32 - cnt));
		},
		str2binb:function(str){
			var bin = Array();
			var mask = (1 << this.chrsz) - 1;
			for(var i = 0; i < str.length * this.chrsz; i += this.chrsz)
				bin[i>>5] |= (str.charCodeAt(i / this.chrsz) & mask) << (32 - this.chrsz - i%32);
			return bin;
		},
		binb2str:function(bin){
			var str = "";
			var mask = (1 << this.chrsz) - 1;
			for(var i = 0; i < bin.length * 32; i += this.chrsz)
				str += String.fromCharCode((bin[i>>5] >>> (32 - this.chrsz - i%32)) & mask);
			return str;
		},
		binb2hex:function(binarray){
			var hex_tab = this.hexcase ? "0123456789ABCDEF" : "0123456789abcdef";
			var str = "";
			for(var i = 0; i < binarray.length * 4; i++){
				str += hex_tab.charAt((binarray[i>>2] >> ((3 - i%4)*8+4)) & 0xF) +
					hex_tab.charAt((binarray[i>>2] >> ((3 - i%4)*8  )) & 0xF);
			}
			return str;
		},
		binb2b64:function(binarray){
			var tab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
			var str = "";
			for(var i = 0; i < binarray.length * 4; i += 3){
				var triplet = (((binarray[i   >> 2] >> 8 * (3 -  i   %4)) & 0xFF) << 16)
					| (((binarray[i+1 >> 2] >> 8 * (3 - (i+1)%4)) & 0xFF) << 8 )
					|  ((binarray[i+2 >> 2] >> 8 * (3 - (i+2)%4)) & 0xFF);
				for(var j = 0; j < 4; j++){
					if(i * 8 + j * 6 > binarray.length * 32) str += this.b64pad;
					else str += tab.charAt((triplet >> 6*(3-j)) & 0x3F);
				}
			}
			return str;
		},
//		hex_hmac:function(key, data){ return this.binb2hex(this.core_hmac(key, data));},
//		b64_hmac:function(key, data){ return this.binb2b64(this.core_hmac(key, data));},
//		str_hmac:function(key, data){ return this.binb2str(this.core_hmac(key, data));},
		
		hex:function(s){return this.binb2hex(this.core(this.str2binb(s),s.length * this.chrsz));},
//		b64:function(s){return this.binb2b64(this.core(this.str2binb(s),s.length * this.chrsz));},
//		str:function(s){return this.binb2str(this.core(this.str2binb(s),s.length * this.chrsz));}
		str:function(s){return this.hex(s).substring(8,24)}
	};
	return {
		MD5:MD5,
		SHA1:SHA1
	}
}();







