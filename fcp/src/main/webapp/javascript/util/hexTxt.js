var hexTxt = {
	bytesHex: function(f){
		for(var i = f.length - 1; i > -1; i--) {
			f[i] = (f[i] & 0xff).toString(16);
			f[i] = ('00' + f[i]).replace(/^0+([\dA-F]*[\dA-F]{2})$/i, '$1').toUpperCase();
		}
		return f.join(' ');
	}
};