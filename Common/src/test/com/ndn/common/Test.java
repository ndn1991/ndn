package com.ndn.common;

import java.util.HashMap;
import java.util.Map;

public class Test {
	public static void main(String[] args) {
		Map<Class<?>, Byte> map = new HashMap<Class<?>, Byte>();
		map.put(null, (byte) 0);
		map.put(Integer.class, (byte) 1);
		map.put(Double.class, (byte) 2);
		System.out.println(map);
		System.out.println(map.get(Double.class));
	}
}
