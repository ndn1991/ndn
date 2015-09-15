package com.ndn.common.data;

/**
 * Created by ndn on 7/23/2015.
 */
public class Tuple3<A, B, C> {
	public A _1;
	public B _2;
	public C _3;

	public Tuple3(A a, B b, C c) {
		this._1 = a;
		this._2 = b;
		this._3 = c;
	}

	@Override
	public String toString() {
		return "Tuple3 [_1=" + _1 + ", _2=" + _2 + ", _3=" + _3 + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_1 == null) ? 0 : _1.hashCode());
		result = prime * result + ((_2 == null) ? 0 : _2.hashCode());
		result = prime * result + ((_3 == null) ? 0 : _3.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tuple3 other = (Tuple3) obj;
		if (_1 == null) {
			if (other._1 != null)
				return false;
		} else if (!_1.equals(other._1))
			return false;
		if (_2 == null) {
			if (other._2 != null)
				return false;
		} else if (!_2.equals(other._2))
			return false;
		if (_3 == null) {
			if (other._3 != null)
				return false;
		} else if (!_3.equals(other._3))
			return false;
		return true;
	}

}
