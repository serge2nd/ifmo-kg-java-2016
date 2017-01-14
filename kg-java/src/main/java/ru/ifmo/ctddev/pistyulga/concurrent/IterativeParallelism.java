package ru.ifmo.ctddev.pistyulga.concurrent;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

import info.kgeorgiy.java.advanced.concurrent.ListIP;
import ru.ifmo.ctddev.pistyulga.concurrent.util.ConcurrentSegmentTree;
import ru.ifmo.ctddev.pistyulga.concurrent.util.IPUtil;
import ru.ifmo.ctddev.pistyulga.concurrent.util.RunnableFunction;
import ru.ifmo.ctddev.pistyulga.concurrent.util.SimpleFixedThreadPool;

public class IterativeParallelism implements ListIP {
	@Override
	public <T> List<T> filter(int nThreads, List<? extends T> list, Predicate<? super T> predicate) throws InterruptedException {
		try(SimpleFixedThreadPool executor = new SimpleFixedThreadPool(nThreads)) {
			RunnableFunction<T, Boolean>[] tasks = IPUtil.executeOn(executor, list, (x) -> predicate.test(x));
			return IPUtil.filter(list, tasks);
		}
	}

	@Override
	public String join(int nThreads, List<?> list) throws InterruptedException {
		try(SimpleFixedThreadPool executor = new SimpleFixedThreadPool(nThreads)) {
			RunnableFunction<?, String>[] tasks = IPUtil.executeOn(executor, list, (x) -> x.toString());
			return IPUtil.join(tasks);
		}
	}

	@Override
	public <T, U> List<U> map(int nThreads, List<? extends T> list, Function<? super T, ? extends U> f)
			throws InterruptedException
	{
		try(SimpleFixedThreadPool executor = new SimpleFixedThreadPool(nThreads)) {
			RunnableFunction<T, U>[] tasks = IPUtil.executeOn(executor, list, f);
			return IPUtil.map(tasks);
		}
	}

	@Override
	public <T> boolean all(int nThreads, List<? extends T> list, Predicate<? super T> predicate) throws InterruptedException {
		try(SimpleFixedThreadPool executor = new SimpleFixedThreadPool(nThreads)) {
			RunnableFunction<T, Boolean>[] tasks = IPUtil.executeOn(executor, list, (x) -> predicate.test(x));
			return IPUtil.all(tasks);
		}
	}

	@Override
	public <T> boolean any(int nThreads, List<? extends T> list, Predicate<? super T> predicate) throws InterruptedException {
		try(SimpleFixedThreadPool executor = new SimpleFixedThreadPool(nThreads)) {
			RunnableFunction<T, Boolean>[] tasks = IPUtil.executeOn(executor, list, (x) -> predicate.test(x));
			return IPUtil.any(tasks);
		}
	}

	@Override
	public <T> T maximum(int nThreads, List<? extends T> list, Comparator<? super T> comp) throws InterruptedException {
		BinaryOperator<T> op = getOpGreatestFromComp(comp);
		return findResultWith(nThreads, op, list);
	}

	@Override
	public <T> T minimum(int nThreads, List<? extends T> list, Comparator<? super T> comp) throws InterruptedException {
		BinaryOperator<T> op = getOpGreatestFromComp(Collections.reverseOrder(comp));
		return findResultWith(nThreads, op, list);
	}
	
	protected static <T> BinaryOperator<T> getOpGreatestFromComp(Comparator<? super T> comp) {
		return (a, b) -> {
			int compResult = comp.compare(a, b);
			return (compResult > 0) ? a : b;
		};
	}
	
	private static <T> T findResultWith(int nThreads, BinaryOperator<T> op, List<? extends T> args) throws InterruptedException {
		try (SimpleFixedThreadPool executor = new SimpleFixedThreadPool(nThreads)) {
			ConcurrentSegmentTree<T> segmentTree = new ConcurrentSegmentTree<T>(args, op, executor);
			return segmentTree.calc();
		}
	}
}
