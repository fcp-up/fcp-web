package com.defu.fcp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
	private final static ExecutorService pool = Executors.newCachedThreadPool();
	public static void execute(Runnable command) {
		pool.execute(command);
	}
}
