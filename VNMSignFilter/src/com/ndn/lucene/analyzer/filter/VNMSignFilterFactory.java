package com.ndn.lucene.analyzer.filter;

import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

public class VNMSignFilterFactory extends TokenFilterFactory {

	public VNMSignFilterFactory(Map<String, String> args) {
		super(args);
	}

	@Override
	public TokenStream create(TokenStream input) {
		return new VNMSignFilter(input);
	}

}
