package com.ndn.common.data.commondata;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.msgpack.MessagePack;
import org.msgpack.packer.Packer;
import org.msgpack.unpacker.Unpacker;

import com.ndn.common.data.commondata.msgpack.CommonDataTemplate;
import com.ndn.common.data.commondata.msgpack.MsgPackTemplateFactory;

final class MsgPackObjectCommonTools {
	private static final MessagePack msgpack = new MessagePack();
	private static final CommonDataTemplate template = MsgPackTemplateFactory
			.getInstance().getCommonDataTemplate();

	static CommonData fromMessagePack(byte[] bytes) throws IOException {
		if (bytes != null) {
			if (bytes.length == 0) {
				return new CommonObject();
			}
			ByteArrayInputStream in = new ByteArrayInputStream(bytes);
			Unpacker unpacker = msgpack.createUnpacker(in);
			return template.read(unpacker, null);
		}
		return null;
	}

	static byte[] toMsgPack(CommonData data) throws IOException {
		if (data == null) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Packer packer = msgpack.createPacker(out);
		template.write(packer, data);
		return out.toByteArray();
	}
}
