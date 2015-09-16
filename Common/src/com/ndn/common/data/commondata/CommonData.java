package com.ndn.common.data.commondata;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by ndn on 7/21/2015.
 */
public interface CommonData extends Serializable {
	public default String toJsonString() {
		return JsonObjectCommonTools.toJsonString(this);
	}
	
	public default byte[] toMsgPack() throws IOException {
		return MsgPackObjectCommonTools.toMsgPack(this);
	}

	public void copyFrom(CommonData other);
}
