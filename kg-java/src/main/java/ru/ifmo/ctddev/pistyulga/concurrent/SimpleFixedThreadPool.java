package ru.ifmo.ctddev.pistyulga.concurrent;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executor;

public class SimpleFixedThreadPool implements Executor, AutoCloseable {
	
	private volatile int threadsCount = 0;
	
	private final int maxThreadsCount;
	private final Executor self = this;
	
	private Queue<Runnable> tasks = new LinkedList<>();
	
	// TODO replace the worker by an array of working threads
	private final Thread worker = new Thread(new Runnable() {
		@Override
		public void run() {
			try {
				while (true) {
					Runnable task = null;
					synchronized (tasks) {
						while (tasks.isEmpty()) {
							tasks.wait();
						}
						task = tasks.poll();
					}
					synchronized (self) {
						while (threadsCount == maxThreadsCount) {
							self.wait();
						}
						threadsCount++;
					}
					Thread taskRunner = new Thread(task) {
						public void run() {
							super.run();
							synchronized (self) {
								threadsCount--;
								self.notifyAll();
							}
						};
					};
					taskRunner.start();
				}
			} catch (InterruptedException e) {}
		}
	});
	
	public SimpleFixedThreadPool(int maxThreadsCount) {
		this.maxThreadsCount = maxThreadsCount;
		worker.start();
	}

	@Override
	public void execute(Runnable command) {
		synchronized (tasks) {
			tasks.add(command);
			tasks.notifyAll();
		}
	}

	@Override
	public void close() throws InterruptedException {
		worker.interrupt();
	}
}
