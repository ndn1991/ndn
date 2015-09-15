package com.ndn.common.data.commondata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ndn on 7/21/2015.
 */
public class CommonArray implements CommonData, Iterable<Object> {

	private static final long serialVersionUID = -2924138158989680210L;

	private ArrayList<Object> datas = new ArrayList<Object>();

	public CommonArray() {
		super();
	}

	/**
	 * @param objects
	 *            must be one of types (Long, Integer, Double, Float, String,
	 *            Boolean, Character, CommonInterface)
	 */
	public CommonArray(List<Object> objects) {
		super();
		objects.forEach(this::add);
	}

	/**
	 * @param objects
	 *            must be one of types (Long, Integer, Double, Float, String,
	 *            Boolean, Character, CommonInterface)
	 */
	public CommonArray(Object... objects) {
		super();
		for (Object o : objects) {
			this.add(o);
		}
	}

	/**
	 * @param o
	 *            must be one of types (Long, Integer, Double, Float, String,
	 *            Boolean, Character, CommonInterface)
	 */
	public boolean add(Object o) {
		if (CommonObject.typeAccepted(o)) {
			return datas.add(o);
		} else {
			throw new IllegalArgumentException(
					"This object has type not be accepted: " + o);
		}
	}

	@Override
	public void copyFrom(CommonData other) {
		if (other instanceof CommonArray) {
			datas.clear();
			datas.addAll(((CommonArray) other).datas);
		} else {
			throw new IllegalArgumentException("input is not a CommonArray");
		}
	}

	@Override
	public Iterator<Object> iterator() {
		return datas.iterator();
	}

	public Object get(int index) {
		return datas.get(index);
	}
}
