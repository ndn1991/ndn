package com.ndn.common.data.commondata;

import java.io.Serializable;

/**
 * Created by ndn on 7/21/2015.
 */
public interface CommonData extends Serializable {
	public default String toJsonString() {
		return JsonObjectCommonTools.toJsonString(this);
	}

	public void copyFrom(CommonData other);
}
