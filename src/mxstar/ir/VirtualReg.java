package mxstar.ir;

public class VirtualReg extends IRRegister {
	private String name;

	private PhysicalReg physicalReg = null;

	public VirtualReg(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setPhysicalReg(PhysicalReg forced) {
		physicalReg = forced;
	}

	public PhysicalReg getPhysicalReg() {
		return physicalReg;
	}

	@Override
	public VirtualReg copy() {
		return new VirtualReg(name);
	}

	public void accept(IRVisitor visitor) {
		visitor.visit(this);
	}
}
