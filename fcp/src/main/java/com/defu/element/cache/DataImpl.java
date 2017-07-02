package com.defu.element.cache;

public class DataImpl<T> implements IData<T> {
	protected T d;
	protected boolean reading;
	protected Exception ex;
	protected long lt;

	public DataImpl(T d){
		this.d = d;
		lt = System.currentTimeMillis();
		reading = false;
		ex = null;
	}
	
	@Override
	public void update(T d) {
		// TODO Auto-generated method stub
		lt = System.currentTimeMillis();
		this.d = d;
	}

	@Override
	public T getData() {
		// TODO Auto-generated method stub
		return d;
	}

	@Override
	public long getLastUpdateTime() {
		// TODO Auto-generated method stub
		return lt;
	}

	@Override
	public boolean isReading() {
		// TODO Auto-generated method stub
		return reading;
	}

	@Override
	public void setReading(boolean isReading) {
		// TODO Auto-generated method stub
		reading = isReading;
	}

	@Override
	public Exception getReadEx() {
		// TODO Auto-generated method stub
		return ex;
	}

	@Override
	public void setReadEx(Exception readEx) {
		// TODO Auto-generated method stub
		ex = readEx;
	}

}
