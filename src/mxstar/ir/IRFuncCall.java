package mxstar.ir;

import java.util.List;

public class IRFuncCall extends IRInstruction {
	private IRFunction func;
	private IRRegister dest;
	private List<RegValue> args;

	public IRFuncCall(IRFunction func, IRRegister dest, List<RegValue> args, BasicBlock parentBB) {
		super(parentBB);
		this.func = func;
		this.dest = dest;
		this.args = args;
	}

	public IRRegister getDest() {
		return dest;
	}

	public IRFunction getFunc() {
		return func;
	}

	public List<RegValue> getArgs() {
		return args;
	}

	public void accept(IRVisitor visitor) {
		visitor.visit(this);
	}
}
