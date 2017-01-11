package ru.ifmo.ctddev.pistyulga.concurrent;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.BinaryOperator;

import static java.lang.Math.log;
import static java.lang.Math.ceil;

/**
 * @author Serge
 * @see <a href="https://neerc.ifmo.ru/wiki/index.php?title=Дерево_отрезков._Построение">Дерево отрезков. Построение</a>
 */
public class ConcurrentSegmentTree<T> {
	private final Node[] tree;
	private final BinaryOperator<T> op;
	private final Executor executor;
	
	private static int calcSegmentTreeSize(int length) {
		int upperPowerOf2 = (int)ceil(log(length)/log(2));
		return (1 << (upperPowerOf2 + 1)) - (1 << upperPowerOf2) + length - 1;
	}
	
	public ConcurrentSegmentTree(List<? extends T> list, BinaryOperator<T> op, Executor executor) {
		@SuppressWarnings("unchecked")
		Node[] tree = new ConcurrentSegmentTree.Node[calcSegmentTreeSize(list.size())];
		this.tree = tree;
		
		this.executor = executor;
		this.op = op;
		
		int i = tree.length - 1;
		for (Iterator<? extends T> it = list.iterator(); it.hasNext(); ) {
			tree[i] = new Node(i, it.next(), true);
			i--;
		}
		while (i >= 0) {
			tree[i] = new Node(i);
			i--;
		}
	}
	
	public T calc() throws InterruptedException {
		tree[0].calc();
		synchronized (tree[0]) {
			while (!tree[0].isReady) {
				tree[0].wait();
			}
		}
		return tree[0].val;
	}
	
	private class Node {
		private final int index;
		private volatile T val;
		private volatile boolean isReady;
		private volatile boolean isEmpty;
		
		public Node(int index) {
			this(index, null, false);
		}
		
		public Node(int index, T val, boolean isReady) {
			this.index = index;
			this.val = val;
			this.isReady = isReady;
		}
		
		public void calc() throws InterruptedException {
			if (this.isReady) {
				return;
			}
			
			// Indexes of children
			int leftIndex = 2*index + 1;
			int rightIndex = 2*index + 2;
			
			T left = null, right = null;
			boolean existsLeft = false, existsRight = false;
			
			// Init parallel calculation
			if (leftIndex < tree.length) {
				tree[leftIndex].calc();
			}
			if (rightIndex < tree.length) {
				tree[rightIndex].calc();
			}
			
			// Wait for results
			if (existsLeft = awaitReady(leftIndex)) {
				left = tree[leftIndex].val;
			}
			if (existsRight = awaitReady(rightIndex)) {
				right = tree[rightIndex].val;
			}
			
			// Calc in new thread if both values are presented
			final Node self = this;
			if (existsLeft && existsRight) {
				final T a = left, b = right;
				executor.execute(new Runnable() {
					@Override
					public void run() {
						val = op.apply(a, b);
						isReady = true;
						synchronized (self) {
							self.notifyAll();
						}
					}
				});
				return;
			}
			
			// If this node does not have at least one child,
			// the result is another child value or empty
			if (existsLeft || existsRight) {
				if (existsLeft)
					this.val = left;
				else
					this.val = right;
			} else {
				this.isEmpty = true;
			}
			
			this.isReady = true;
		}
		
		private boolean awaitReady(int index) throws InterruptedException {
			if (index < tree.length) {
				synchronized (tree[index]) {
					while (!tree[index].isReady) {
						tree[index].wait();
					}
				}
				return !tree[index].isEmpty;
			}
			return false;
		}
	}
}
