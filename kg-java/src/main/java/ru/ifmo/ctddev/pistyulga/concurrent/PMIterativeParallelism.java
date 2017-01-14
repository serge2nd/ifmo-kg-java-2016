package ru.ifmo.ctddev.pistyulga.concurrent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Predicate;
import static java.lang.Math.min;

import ru.ifmo.ctddev.pistyulga.concurrent.util.IPUtil;
import ru.ifmo.ctddev.pistyulga.concurrent.util.RunnableFunction;
import ru.ifmo.ctddev.pistyulga.concurrent.util.SimpleFixedThreadPool;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

public class PMIterativeParallelism extends IterativeParallelism {
	private ParallelMapper parallelMapper;
	private final Executor synchronousExecutor = new SimpleFixedThreadPool(0);
	
	public PMIterativeParallelism() {}
	
	public PMIterativeParallelism(ParallelMapper parallelMapper) {
		this.parallelMapper = parallelMapper;
	}
	
	@Override
	public <T> boolean all(int nThreads, List<? extends T> list, Predicate<? super T> predicate)
			throws InterruptedException
	{
		if (parallelMapper == null)
			return super.all(nThreads, list, predicate);
		
		List<List<? extends T>> subLists = getSubLists(nThreads, list);
		Function<List<? extends T>, Boolean> f = (subList) -> {
			RunnableFunction<T, Boolean>[] subTasks = IPUtil.executeOn(synchronousExecutor, subList, (x) -> predicate.test(x));
			try {
				return IPUtil.all(subTasks);
			} catch (InterruptedException e) {
				return false;
			}
		};
		
		List<Boolean> result = parallelMapper.map(f, subLists);
		for (Boolean b : result) {
			if (!b)
				return false;
		}
		
		return true;
	}
	
	@Override
	public <T> boolean any(int nThreads, List<? extends T> list, Predicate<? super T> predicate)
			throws InterruptedException
	{
		if (parallelMapper == null)
			return super.any(nThreads, list, predicate);
		
		List<List<? extends T>> subLists = getSubLists(nThreads, list);
		Function<List<? extends T>, Boolean> f = (subList) -> {
			RunnableFunction<T, Boolean>[] subTasks = IPUtil.executeOn(synchronousExecutor, subList, (x) -> predicate.test(x));
			try {
				return IPUtil.any(subTasks);
			} catch (InterruptedException e) {
				return false;
			}
		};
		
		List<Boolean> result = parallelMapper.map(f, subLists);
		for (Boolean b : result) {
			if (b)
				return true;
		}
		
		return false;
	}
	
	@Override
	public <T> List<T> filter(int nThreads, List<? extends T> list, Predicate<? super T> predicate)
			throws InterruptedException
	{
		if (parallelMapper == null)
			return super.filter(nThreads, list, predicate);
		
		List<List<? extends T>> subLists = getSubLists(nThreads, list);
		Function<List<? extends T>, List<T>> f = (subList) -> {
			RunnableFunction<T, Boolean>[] subTasks = IPUtil.executeOn(synchronousExecutor, subList, (x) -> predicate.test(x));
			try {
				return IPUtil.filter(subList, subTasks);
			} catch (InterruptedException e) {
				return null;
			}
		};
		
		List<T> result = new ArrayList<>();
		List<List<T>> resultSubLists = parallelMapper.map(f, subLists);
		for (List<T> l : resultSubLists) {
			result.addAll(l);
		}
		
		return result;
	}
	
	@Override
	public String join(int nThreads, List<?> list)
			throws InterruptedException
	{
		if (parallelMapper == null)
			return super.join(nThreads, list);
		
		List<List<?>> subLists = getSubLists(nThreads, list);
		Function<List<?>, String> f = (subList) -> {
			RunnableFunction<?, String>[] subTasks = IPUtil.executeOn(synchronousExecutor, subList, (x) -> x.toString());
			try {
				return IPUtil.join(subTasks);
			} catch (InterruptedException e) {
				return null;
			}
		};
		
		StringBuilder result = new StringBuilder();
		List<String> resultStrings = parallelMapper.map(f, subLists);
		for (String str : resultStrings) {
			result.append(str);
		}
		
		return result.toString();
	}
	
	@Override
	public <T, U> List<U> map(int nThreads, List<? extends T> list, Function<? super T, ? extends U> f)
			throws InterruptedException
	{
		if (parallelMapper == null)
			return super.map(nThreads, list, f);
		
		List<List<? extends T>> subLists = getSubLists(nThreads, list);
		Function<List<? extends T>, List<U>> subMappingFunc = (subList) -> {
			RunnableFunction<T, U>[] subTasks = IPUtil.executeOn(synchronousExecutor, subList, f);
			try {
				return IPUtil.map(subTasks);
			} catch (InterruptedException e) {
				return null;
			}
		};
		
		List<U> result = new ArrayList<>();
		List<List<U>> resultSubLists = parallelMapper.map(subMappingFunc, subLists);
		for (List<U> l : resultSubLists) {
			result.addAll(l);
		}
		
		return result;
	}
	
	@Override
	public <T> T maximum(int nThreads, List<? extends T> list, Comparator<? super T> comp) throws InterruptedException {
		if (parallelMapper == null)
			return super.maximum(nThreads, list, comp);
		
		List<T> result = new ArrayList<>();
		result.addAll(list);
		
		while (result.size() > 1) {
			List<List<? extends T>> subLists = getSubLists(nThreads, result);
			result = parallelMapper.map((subList) -> Collections.max(subList, comp), subLists);
			nThreads /= 2;
		}
		
		return result.get(0);
	}
	
	@Override
	public <T> T minimum(int nThreads, List<? extends T> list, Comparator<? super T> comp) throws InterruptedException {
		if (parallelMapper == null)
			return super.minimum(nThreads, list, comp);
		
		return this.maximum(nThreads, list, Collections.reverseOrder(comp));
	}
	
	private static <T> List<List<? extends T>> getSubLists(int nSubLists, List<? extends T> list) {
		int tasksCount = min(nSubLists, list.size());
		List<List<? extends T>> subLists = new ArrayList<>();
		
		int fragmentSize = list.size() / tasksCount;
		int i;
		for (i = 0; i < tasksCount; i++) {
			subLists.add(list.subList(i*fragmentSize, (i + 1)*fragmentSize));
		}
		if (i*fragmentSize < list.size()) {
			subLists.add(list.subList(i*fragmentSize, list.size()));
		}
		
		return subLists;
	}
}
