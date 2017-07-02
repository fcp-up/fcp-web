package com.defu.element.cache;

public class Flag<T> {
	private T f;
	
	public Flag(T flag){
		if(flag == null) throw new NullPointerException("数据标志不能为空");
		f = flag;
	}
	
	public T getFlag(){
		return f;
	}
	
	public void setFlag(T flag){
		f = flag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((f == null) ? 0 : f.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Flag other = (Flag) obj;
		if (f == null) {
			if (other.f != null)
				return false;
		} else if (!f.equals(other.f))
			return false;
		return true;
	}
	
}
