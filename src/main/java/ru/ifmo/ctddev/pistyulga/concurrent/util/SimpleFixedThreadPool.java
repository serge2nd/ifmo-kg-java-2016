package ru.ifmo.ctddev.pistyulga.concurrent.util;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.ifmo.ctddev.pistyulga.common.log.LogService;

public class SimpleFixedThreadPool implements Executor, AutoCloseable {
	private static final Logger LOG = LogService.getLogger();
	
	private Queue<Runnable> tasks = new LinkedList<>();
	private Thread[] threads;
	
	public SimpleFixedThreadPool(int maxThreadsCount) {
		this.threads = new Thread[maxThreadsCount];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread("kgipThread" + i) {
				private volatile boolean isInterrupted;
				@Override
				public void run() {
					try {
						while (!Thread.currentThread().isInterrupted()) {
							Runnable task = null;
							synchronized (tasks) {
								while (tasks.isEmpty()) {
									tasks.wait();
								}
								task = tasks.poll();
							}
							if (task != null)
								task.run();
						}
					} catch (Exception e) {
						LOG.log(Level.SEVERE, "Error in " + this.getName() + ": " + e.getMessage(), e);
					}
				}
				@Override
				public void interrupt() {
					this.isInterrupted = true;
					synchronized (tasks) {
						tasks.notifyAll();
					}
				}
				@Override
				public boolean isInterrupted() {
					return this.isInterrupted;
				}
			};
			threads[i].start();
		}
	}

	@Override
	public void execute(Runnable command) {
		if (threads.length == 0) {
			command.run();
		} else {
			synchronized (tasks) {
				tasks.add(command);
				tasks.notifyAll();
			}
		}
	}

	@Override
	public void close() throws InterruptedException {
		for (Thread thread : threads)
			thread.interrupt();
	}
}
