package com.ndn.vnm;

import java.util.Comparator;

public class VNMComparator implements Comparator<String> {
	@Override
	public int compare(String o1, String o2) {
		int sz1 = o1.length();
		int sz2 = o2.length();
		int sz = Math.min(sz1, sz2);
		
		char[] cs1 = VNMNormalizer.toBuiltinUnicode(o1.toLowerCase().toCharArray());
		char[] cs2 = VNMNormalizer.toBuiltinUnicode(o2.toLowerCase().toCharArray());
		
		for (int i = 0; i < sz; i++) {
			int compare = VNMNormalizer.compare(cs1[i], cs2[i]);
			if (compare != 0) {
				return compare;
			}
		}
		return Integer.compare(sz1, sz2);
	}
}
