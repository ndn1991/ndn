package com.ndn.expression.operator;

import static com.ndn.expression.operator.OperatorFactory.AND_PRIORITY;
import static com.ndn.expression.operator.OperatorFactory.OR;

public class OROperator extends BaseOperator {
	public OROperator() {
		this.op = OR;
		this.priority = AND_PRIORITY;
	}

	@Override
	public boolean impact(Object... args) {
		baseVerify(args);

		if (args[0] instanceof Boolean && args[1] instanceof Boolean) {
			return ((Boolean) args[0]) || ((Boolean) args[1]);
		} else {
			throw new IllegalArgumentException("args[0]: " + args[0] + " - args[1]" + args[1]);
		}
	}

}
