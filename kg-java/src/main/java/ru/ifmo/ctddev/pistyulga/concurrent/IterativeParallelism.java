package ru.ifmo.ctddev.pistyulga.concurrent;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

import info.kgeorgiy.java.advanced.concurrent.ScalarIP;

public class IterativeParallelism implements ScalarIP {

	@Override
	public <T> boolean all(int nThreads, List<? extends T> list, Predicate<? super T> predicate) throws InterruptedException {
		try(SimpleFixedThreadPool executor = new SimpleFixedThreadPool(nThreads)) {
			RunnableFunction<T, Boolean>[] tasks = executeOn(executor, list, (x) -> predicate.test(x));
			
			for (int i = 0; i < tasks.length; i++) {
				boolean isRight = tasks[i].get();
				if (!isRight) {
					return false;
				}
			}
		}
		
		return true;
	}

	@Override
	public <T> boolean any(int nThreads, List<? extends T> list, Predicate<? super T> predicate) throws InterruptedException {
		try(SimpleFixedThreadPool executor = new SimpleFixedThreadPool(nThreads)) {
			RunnableFunction<T, Boolean>[] tasks = executeOn(executor, list, (x) -> predicate.test(x));
			
			for (int i = 0; i < tasks.length; i++) {
				boolean isRight = tasks[i].get();
				if (isRight) {
					return true;
				}
			}
		}
		
		return false;
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
	
	private <T, R> RunnableFunction<T, R>[] executeOn(Executor executor, List<? extends T> list, Function<T, R> f) {
		@SuppressWarnings("unchecked")
		RunnableFunction<T, R>[] tasks = new RunnableFunction[list.size()];
		int i = 0;
		for (T arg : list) {
			tasks[i] = new RunnableFunction<>(f, arg);
			executor.execute(tasks[i++]);
		}
		return tasks;
	}
	
	private <T> BinaryOperator<T> getOpGreatestFromComp(Comparator<? super T> comp) {
		return (a, b) -> {
			int compResult = comp.compare(a, b);
			return (compResult > 0) ? a : b;
		};
	}
	
	private <T> T findResultWith(int nThreads, BinaryOperator<T> op, List<? extends T> args) throws InterruptedException {
		try (SimpleFixedThreadPool executor = new SimpleFixedThreadPool(nThreads)) {
			ConcurrentSegmentTree<T> segmentTree = new ConcurrentSegmentTree<T>(args, op, executor);
			return segmentTree.calc();
		}
	}
}
