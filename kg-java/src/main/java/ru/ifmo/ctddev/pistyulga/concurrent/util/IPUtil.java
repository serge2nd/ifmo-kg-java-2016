package ru.ifmo.ctddev.pistyulga.concurrent.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Function;

/**
 * This class contains static functions corresponding to most operations required by the task
 * and some utility actions for iterative parallelism
 * @author Serge
 */
public class IPUtil {
	private IPUtil() {}
	
	public static <T, R> RunnableFunction<T, R>[] executeOn(
			Executor executor, List<? extends T> list, Function<? super T, ? extends R> f)
	{
		@SuppressWarnings("unchecked")
		RunnableFunction<T, R>[] tasks = new RunnableFunction[list.size()];
		int i = 0;
		for (T arg : list) {
			tasks[i] = new RunnableFunction<>(f, arg);
			executor.execute(tasks[i++]);
		}
		return tasks;
	}
	
	public static <T> boolean all(RunnableFunction<T, Boolean>[] tasks) throws InterruptedException {
		for (int i = 0; i < tasks.length; i++) {
			boolean isRight = tasks[i].get();
			if (!isRight) {
				return false;
			}
		}
		return true;
	}
	
	public static <T> boolean any(RunnableFunction<T, Boolean>[] tasks) throws InterruptedException {
		for (int i = 0; i < tasks.length; i++) {
			boolean isRight = tasks[i].get();
			if (isRight) {
				return true;
			}
		}
		return false;
	}
	
	public static <T, R> List<R> map(RunnableFunction<T, R>[] tasks) throws InterruptedException {
		List<R> transformedList = new ArrayList<>();
		for (int i = 0; i < tasks.length; i++) {
			R result = tasks[i].get();
			transformedList.add(result);
		}
		return transformedList;
	}
	
	public static String join(RunnableFunction<?, String>[] tasks) throws InterruptedException {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < tasks.length; i++) {
			String str = tasks[i].get();
			stringBuilder.append(str);
		}
		return stringBuilder.toString();
	}
	
	public static <T, R> List<T> filter(List<? extends T> list, RunnableFunction<T, Boolean>[] tasks)
			throws InterruptedException
	{
		List<T> filteredList = new ArrayList<>();
		int i = 0;
		for (T arg : list) {
			boolean isRight = tasks[i++].get();
			if (isRight) {
				filteredList.add(arg);
			}
		}
		return filteredList;
	}
}
