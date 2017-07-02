package com.defu.element;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 树形结构，节点封装
 * @author glj
 *
 * @param <T> 节点数据类型
 */
public class Node<T> {
	private T data;
	private Node<T> parent;
	private ArrayList<Node<T>> children;
	
	/**
	 * 销毁一个节点
	 */
	public void destory() {
		data = null;
		parent = null;
		children.clear();
		children = null;
	}
	
	/**
	 * 返回节点级别，根节点为1
	 * @return 节点级别
	 */
	public int getLevel() {
		int l = 1;
		Node<T> p = parent;
		while(p != null) {
			l++;
			p = p.getParent();
		}
		return l;
	}
	
	/**
	 * 移除一个孩子节点，并返回该节点
	 * @param c 被移除的孩子节点
	 */
	public Node<T> removeChild(Node<T> c) {
		if(c == null) return this;
		c.setParent(null);
		synchronized(children) {
			children.remove(c);
		}
		return this;
	}
	
	/**
	 * 是否是一个根节点。没有父节点的节点被认为是一个根节点
	 * @return 是否是根节点
	 */
	public boolean isRoot() {
		return parent == null;
	}
	
	/**
	 * 是否是第一个节点。父节点的第一个子节点和根节点被认为是第一个节点。
	 * @return 是否是第一个节点
	 */
	public boolean isFirst() {
		if(isRoot()) return true;
		return parent.getChildren().get(0) == this;
	}

	/**
	 * 是否是最后一个节点。父节点的最后一个子节点和根节点被认为是最后一个节点。
	 * @return 是否是最后一个节点
	 */
	public boolean isLast() {
		if(isRoot()) return true;
		List<Node<T>> cs = parent.getChildren();
		return cs.get(cs.size() - 1) == this;
	}
	
	/**
	 * 获取下一个节点，如果是最后一个节点，返回null
	 * @return 下一个节点
	 */
	public Node<T> nextSibling() {
		if(isLast()) return null;
		ArrayList<Node<T>> bs = parent.getChildren();
		return bs.get(bs.indexOf(this) + 1);
	}

	/**
	 * 获取上一个节点，如果是第一个节点，返回null
	 * @return 上一个节点
	 */
	public Node<T> previousSibling() {
		if(isFirst()) return null;
		ArrayList<Node<T>> bs = parent.getChildren();
		return bs.get(bs.indexOf(this) - 1);
	}
	
	/**
	 * 是否是一个叶子节点，没有子节点的节点被认为是一个叶子节点
	 * @return 是否是叶子节点
	 */
	public boolean isLeaf() {
		return children.size() < 1;
	}
	
	/**
	 * 追加一个子节点，并返回该节点
	 * @param c 子节点
	 * @return 该节点
	 */
	public Node<T> appendChild(Node<T> c) {
		if(c == this || c == null) return this;
		synchronized(children) {
			children.add(c);
		}
		c.setParent(this);
		return this;
	}
	
	/**
	 * 把追加多个子节点，返回该节点
	 * @param cs 子节点数组
	 * @return 该节点
	 */
	public Node<T> appendChildren(Node<T>[] cs) {
		if(cs != null) for(Node<T> c: cs) appendChild(c);
		return this;
	}
	
	/**
	 * 把追加多个子节点，返回该节点
	 * @param cs 子节点数组
	 * @return 该节点
	 */
	public Node<T> appendChildren(List<Node<T>> cs) {
		if(cs != null) for(Node<T> c: cs) appendChild(c);
		return this;
	}
	
	/**
	 * 在指定位置插入一个子节点，并返回该节点
	 * @param c 子节点
	 * @param index 指定位置。若指定位置超出了子节点列表尺寸，则添加为最后一个子节点；若指定位置小于0，则添加为第一个子节点
	 * @return 该节点
	 */
	public Node<T> insert(Node<T> c, int index) {
		if(c == null) return this;
		if(index < 0) {
			synchronized(children) {
				children.add(0, c);
			}
			c.setParent(this);
		}
		else if(index > children.size()) appendChild(c);
		else {
			synchronized(children) {
				children.add(index, c);
			}
			c.setParent(this);
		}
		return this;
	}

	/**
	 * 在指定子节点前插入子节点，并返回该节点
	 * @param c 子节点
	 * @param t 指定子节点
	 * @return 该节点
	 * @throws NoSuchElementException 若不存在指定子节点，抛出异常
	 */
	public Node<T> insertBefore(Node<T> c, Node<T> t) throws NoSuchElementException {
		int i = children.indexOf(t);
		if(i < -1) throw new NoSuchElementException("不存在子节点" + t);
		insert(c, i - 1);
		return this;
	}

	/**
	 * 在指定子节点后插入子节点，并返回该节点
	 * @param c 子节点
	 * @param t 指定子节点
	 * @return 该节点
	 * @throws NoSuchElementException 若不存在指定子节点，抛出异常
	 */
	public Node<T> insertAfter(Node<T> c, Node<T> t) throws NoSuchElementException {
		int i = children.indexOf(t);
		if(i < -1) throw new NoSuchElementException("不存在子节点" + t);
		insert(c, i + 1);
		return this;
	}
	
	private List<Node<T>> allChildren(Node<T> pn) {
		List<Node<T>> rs = new ArrayList<Node<T>>();
		List<Node<T>> ochd = pn.getChildren();
		Node<T> t;
		for(int i = 0, il = ochd.size(); i < il; i++) {
			t = ochd.get(i);
			rs.add(t);
			rs.addAll(allChildren(t));
		}
		return rs;
	}
	
	/**
	 * 获取节点所在分支的所有节点集合
	 * @return 节点集合
	 */
	public List<Node<T>> branchNodes() {
		Node<T> t;
		List<Node<T>> rs = allChildren();
		rs.add(this);
		t = this.parent;
		while(t != null) {
			rs.add(t);
			t = t.parent;
		}
		return rs;
	}
	
	/**
	 * 获取一个节点下的所有子孙节点集合
	 * @return 子孙节点集合
	 */
	public List<Node<T>> allChildren(){
		return allChildren(this);
	}
	
	/**
	 * 构造一个节点
	 * @param o 节点值
	 * @throws NullPointerException 若节点值为空，抛出异常
	 */
	public Node(T o) throws NullPointerException {
		if(o == null) throw new NullPointerException("节点值不能为空");
		data = o;
		children = new ArrayList<Node<T>>();
	}
	
	public String toString() {
		return data.toString();
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Node<T> getParent() {
		return parent;
	}

	public void setParent(Node<T> parent) {
		this.parent = parent;
	}

	public ArrayList<Node<T>> getChildren() {
		return children;
	}

	
}
