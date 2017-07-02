package com.defu.util.impl;


import com.defu.util.ICrypto;
/**
 * 此实现不支持对中文的加密<br/>
 * 需要加密中文时,先把中文变base64即可
 */
public class RC4 implements ICrypto {
	private static char[] key = { 0xcc, 0x32, 0xe2, 0x42, 0xf8, 0xc2, 0xa6, 0x9b };

	private static class CRc4Ctx
	{
		int x =0, y =0;
		int[] m = new int[256];
	}
	
	
	private static byte[] toByteArray(char[] data) throws Exception{
		int len = data.length;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++) {
			result[i] = (byte) (data[i]&0xff);
		}
		return result;
	}
	
	private static char[] toCharArray(byte[] data) throws Exception{
		int len = data.length;
		char[] result = new char[len];
		for(int i = 0; i < len; i++){
			result[i] = (char) (data[i] & 0xff);
		}
		return result;
	}

	private static  void initKey(CRc4Ctx ctx, char[] Key)
	{
		int i, j, k, a;
		ctx.x = 0;
		ctx.y = 0;
		int[] m = ctx.m;
		
		for (i = 0; i < 256; i++) {
			m[i] = i;
		}
		
		j = k = 0;
		
		for (i = 0; i < 256; i++) {
			a = m[i];
			j = (j + a + Key[k]) % 256;
			m[i] = m[j];
			m[j] = a;
			if (++k >= Key.length)
				k = 0;
		}
	}

	private static void rc4_crypt(CRc4Ctx ctx, char[] data)
	{
		int i, x, y;
		int a, b;
		x = ctx.x;
		y = ctx.y;
		int[] m = ctx.m;
		for (i = 0; i < data.length; i++) {
			x = (x + 1) % 256;
			a = m[x];
			y = (y + a) % 256;
			m[x] = b = m[y];
			m[y] = a;
			data[ i] ^= m[(a + b) % 256];
		}
		ctx.x = x;
		ctx.y = y;
	}

	private static char[] encryptRC4(char[] pData, char[] pKey)
	{
		CRc4Ctx s = new CRc4Ctx();
		initKey(s, pKey);
		rc4_crypt(s, pData);
		return pData;
	}
	
	@Override
	public String encrypt(String content) throws Exception {
		if(content == null || content.isEmpty()){
			return null;
		}
		char[] rc4 = encryptRC4(content.toCharArray(),key);
		byte[] secret = toByteArray(rc4);
		return new String(org.apache.commons.codec.binary.Base64.encodeBase64(secret));
	}

	@Override
	public String decrypt(String content) throws Exception {
		if(content == null || content.isEmpty()){
			return null;
		}
		char[] rc4 = toCharArray(org.apache.commons.codec.binary.Base64.decodeBase64(content.getBytes()));
		return new String(encryptRC4(rc4,key));
	}

}
