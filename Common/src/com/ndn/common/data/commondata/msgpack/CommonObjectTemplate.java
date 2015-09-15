package com.ndn.common.data.commondata.msgpack;

import java.io.IOException;

import org.msgpack.packer.Packer;
import org.msgpack.template.AbstractTemplate;
import org.msgpack.unpacker.Unpacker;

import com.ndn.common.data.commondata.CommonObject;

public class CommonObjectTemplate extends AbstractTemplate<CommonObject> {

	public static CommonObjectTemplate getInstance() {
		return null;
	}

	@Override
	public void write(Packer packer, CommonObject t, boolean required)
			throws IOException {
		packer.wr
	}

	@Override
	public CommonObject read(Unpacker paramUnpacker, CommonObject paramT,
			boolean paramBoolean) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
