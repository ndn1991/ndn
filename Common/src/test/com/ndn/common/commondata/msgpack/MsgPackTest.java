package com.ndn.common.commondata.msgpack;

import java.io.IOException;
import java.util.ArrayList;

import com.ndn.common.data.commondata.CommonArray;
import com.ndn.common.data.commondata.CommonData;
import com.ndn.common.data.commondata.CommonObject;
import com.ndn.common.data.commondata.CommonObjectTools;

public class MsgPackTest {
	public static void main(String[] args) throws IOException {
		CommonObject obj = new CommonObject();
		obj.setString("name", "Iphone 6");
		obj.setInt("type", 1);
		obj.setCommonData("path", new CommonArray(1, 2, 3));
		obj.setCommonData("test", new CommonArray(
				new CommonObject("abc", "xyz"), new CommonObject("def", "ghi")));
		obj.setCommonData("test1", new CommonObject("a_123", 456));
		System.out.println(obj.toJsonString());

		byte[] ser = obj.toMsgPack();
		ArrayList<Byte> lst = new ArrayList<Byte>();
		for (byte b : ser)
			lst.add(b);
		System.out.println(lst);

		CommonData data = CommonObjectTools.fromMsgPack(ser);

		System.out.println(data.toJsonString());

		System.out.println(data.toJsonString().length());
		System.out.println(ser.length);
	}
}
