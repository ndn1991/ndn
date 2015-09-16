package com.ndn.common.data.commondata.msgpack;

import java.io.IOException;

import org.msgpack.MessageTypeException;
import org.msgpack.packer.Packer;
import org.msgpack.template.AbstractTemplate;
import org.msgpack.unpacker.Unpacker;

import com.ndn.common.data.commondata.CommonArray;
import com.ndn.common.data.commondata.CommonData;
import com.ndn.common.data.commondata.CommonObject;

public final class CommonDataTemplate extends AbstractTemplate<CommonData> {
	private static CommonDataTemplate instance = new CommonDataTemplate();
	
	private CommonDataTemplate() {
		
	}
	
	static CommonDataTemplate getInstance() {
		return instance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void write(Packer p, CommonData target, boolean required)
			throws IOException {
		if (target == null) {
			if (required) {
				throw new MessageTypeException("Attempted to write null");
			}
			p.writeNil();
			return;
		}

		if (target instanceof CommonObject) {
			p.write(MsgPackTemplateFactory.COMMON_OBJECT);
			MsgPackTemplateFactory.getInstance()
					.getTemplate(MsgPackTemplateFactory.COMMON_OBJECT)
					.write(p, target);
		} else if (target instanceof CommonArray) {
			p.write(MsgPackTemplateFactory.COMMON_ARRAY);
			MsgPackTemplateFactory.getInstance()
					.getTemplate(MsgPackTemplateFactory.COMMON_ARRAY)
					.write(p, target);
		} else {
			throw new RuntimeException("Do not support this class: "
					+ target.getClass());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public CommonData read(Unpacker u, CommonData to, boolean required)
			throws IOException {
		if (!required && u.trySkipNil()) {
			return null;
		}

		byte type = u.readByte();
		if (type == MsgPackTemplateFactory.COMMON_OBJECT
				|| type == MsgPackTemplateFactory.COMMON_ARRAY) {
			return ((CommonData) MsgPackTemplateFactory.getInstance()
					.getTemplate(type).read(u, null));
		} else {
			throw new RuntimeException("Unknow type: " + type);
		}
	}

}
