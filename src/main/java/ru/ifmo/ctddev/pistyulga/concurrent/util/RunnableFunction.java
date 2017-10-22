package ru.ifmo.ctddev.pistyulga.concurrent.util;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

public class RunnableFunction<T, R> implements Runnable, Future<R> {
	private final Function<? super T, ? extends R> f;
	private final T arg;
	
	private volatile R result;
	private volatile boolean isDone;
	
	public RunnableFunction(Function<? super T, ? extends R> f, T arg) {
		this.f = f; this.arg = arg;
	}

	@Override
	public R get() throws InterruptedException {
		synchronized (this) {
			while (!this.isDone) {
				this.wait();
			}
		}
		
		return result;
	}

	@Override
	public R get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
		synchronized (this) {
			while (!this.isDone) {
				this.wait(unit.toMillis(timeout));
			}
		}
		
		if (!this.isDone) {
			throw new TimeoutException();
		}
		
		return result;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public void run() {
		this.result = f.apply(arg);
		this.isDone = true;
		synchronized (this) {
			this.notifyAll();
		}
	}
	
	@Override
	public boolean isCancelled() {
		return false;
	}
	
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		throw new UnsupportedOperationException();
	}
}
