package ru.ifmo.ctddev.pistyulga.concurrent;

import java.util.List;
import java.util.function.Function;

import ru.ifmo.ctddev.pistyulga.concurrent.util.IPUtil;
import ru.ifmo.ctddev.pistyulga.concurrent.util.RunnableFunction;
import ru.ifmo.ctddev.pistyulga.concurrent.util.SimpleFixedThreadPool;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

public class ParallelMapperImpl implements ParallelMapper {
	private final SimpleFixedThreadPool executor;
	
	public ParallelMapperImpl(int nThreads) {
		this.executor = new SimpleFixedThreadPool(nThreads);
	}
	
	@Override
	public <T, R> List<R> map(Function<? super T, ? extends R> f, List<? extends T> list)
			throws InterruptedException
	{
		RunnableFunction<T, R>[] tasks = IPUtil.executeOn(executor, list, f);
		return IPUtil.map(tasks);
	}

	@Override
	public void close() throws InterruptedException {
		executor.close();
	}
}
