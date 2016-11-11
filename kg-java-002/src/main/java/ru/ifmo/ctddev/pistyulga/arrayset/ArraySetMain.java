package ru.ifmo.ctddev.pistyulga.arrayset;

import java.util.Arrays;
import java.util.NavigableSet;
import java.util.SortedSet;

import ru.ifmo.ctddev.pistyulga.arrayset.util.ArraySet;

public class ArraySetMain {

	public static void main(String[] args) {
		ArraySet<Integer> as = new ArraySet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7)),
							asBackup = new ArraySet<>(as);
		
		System.out.println(as.add(1));
		System.out.println(as.remove(3));
		System.out.println(as.remove(0));
		System.out.println(as.contains(2));
		System.out.println(as.contains(8));
		System.out.println(as.retainAll(Arrays.asList(3, 4, 5, 7, 9)));
		System.out.println(as + " " + asBackup);
		as = new ArraySet<>(asBackup);
		System.out.println();
		
		NavigableSet<Integer> descAs = as.descendingSet();
		System.out.println(descAs);
		descAs.add(10); descAs.remove(5);
		System.out.println(descAs.contains(5));
		System.out.println(descAs.contains(10));
		System.out.println(as + " " + descAs.descendingSet());
		as = new ArraySet<>(asBackup);
		System.out.println();
		
		SortedSet<Integer> subAs = as.subSet(2, 5);
		System.out.println(subAs);
		System.out.println(subAs.add(2));
		System.out.println(subAs.remove(3));
		try { System.out.println(subAs.add(1)); } catch(Exception e) { System.out.println(e); }
		try { System.out.println(subAs.add(5)); } catch(Exception e) { System.out.println(e); }
		System.out.println(subAs.subSet(1, 6));
	}
}
