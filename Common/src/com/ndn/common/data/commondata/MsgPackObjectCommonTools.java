package com.ndn.common.data.commondata;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.msgpack.MessagePack;
import org.msgpack.packer.Packer;
import org.msgpack.template.AbstractTemplate;
import org.msgpack.unpacker.Unpacker;

final class MsgPackObjectCommonTools {
	private static final MessagePack msgpack = new MessagePack();
	private static final CommonDataTemplate template = new CommonDataTemplate();
	
	static CommonData fromMessagePack(byte[] bytes) throws IOException {
		if (bytes != null) {
			if (bytes.length == 0) {
				return new CommonObject();
			}
			ByteArrayInputStream in = new ByteArrayInputStream(bytes);
			Unpacker unpacker = msgpack.createUnpacker(in);
			return PuObjectTemplate.getInstance().read(unpacker, null);
		}
		return null;
	}
	
	private static class CommonDataTemplate extends AbstractTemplate<CommonData> {

		@Override
		public CommonData read(Unpacker u, CommonData to, boolean required)
				throws IOException {
			if (!required && u.trySkipNil()) {
				return null;
			}
			
			byte type = u.readByte();
			
			
		}

		@Override
		public void write(Packer pk, CommonData target, boolean required)
				throws IOException {
			
		}
		
	}
	
//	private static class CommonO
}
