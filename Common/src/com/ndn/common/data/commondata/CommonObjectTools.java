package com.ndn.common.data.commondata;

import java.io.IOException;

final public class CommonObjectTools {
	public static CommonData fromJson(String json) {
		return JsonObjectCommonTools.fromJson(json);
	}

	public static CommonData fromMsgPack(byte[] bytes) throws IOException {
		return MsgPackObjectCommonTools.fromMessagePack(bytes);
	}
}
