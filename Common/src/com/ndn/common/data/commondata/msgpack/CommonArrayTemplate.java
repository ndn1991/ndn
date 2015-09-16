package com.ndn.common.data.commondata.msgpack;

import java.io.IOException;

import org.msgpack.MessageTypeException;
import org.msgpack.packer.Packer;
import org.msgpack.template.AbstractTemplate;
import org.msgpack.unpacker.Unpacker;

import com.ndn.common.data.commondata.CommonArray;

public final class CommonArrayTemplate extends AbstractTemplate<CommonArray> {
	private CommonArrayTemplate() {
		super();
	}
	
	private static CommonArrayTemplate instance = new CommonArrayTemplate();

	public static CommonArrayTemplate getInstance() {
		return instance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void write(Packer packer, CommonArray target, boolean required)
			throws IOException {
		if (target == null) {
			if (required) {
				throw new MessageTypeException("Attempted to write null");
			}
			packer.writeNil();
			return;
		}

		MsgPackTemplateFactory factory = MsgPackTemplateFactory.getInstance();
		packer.writeArrayBegin(target.size() * 2);
		for (Object e : target) {
			packer.write(factory.getType(e.getClass()));
			factory.getTemplate(factory.getType(e.getClass())).write(packer, e);
		}
		packer.writeArrayEnd();
	}

	@SuppressWarnings("unchecked")
	@Override
	public CommonArray read(Unpacker u, CommonArray to, boolean required)
			throws IOException {
		if (!required && u.trySkipNil()) {
			return null;
		}
		MsgPackTemplateFactory factory = MsgPackTemplateFactory.getInstance();
		CommonArray arr = to == null ? new CommonArray() : to;
		int n = u.readArrayBegin();
		for (int i = 0; i < n ; i+=2) {
			Byte type = u.readByte();
			Object data = factory.getTemplate(type).read(u, null);
			arr.add(data);
		}
		u.readArrayEnd();
		return arr;
	}

}
