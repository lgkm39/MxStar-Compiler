package mxstar.ir;

import mxstar.nasm.NASMRegisterSet;
import mxstar.symbol.scope.FuncEntity;

import java.util.*;

public class IRRoot {
	private Map<String, IRFunction> functionMap = new HashMap<>();
	private Map<String, IRFunction> builtInFunctionMap = new HashMap<>();
	private List<IRFunction> functionList = new ArrayList<>();
	private List<IRFunction> builtInFunctionList = new ArrayList<>();
	private List<StaticData> staticDataList = new ArrayList<>();
	private int maxArgsNum = 0;
	private PhysicalReg reversedRegister = null;
	public Map<String, StaticStr> staticStrMap = new HashMap<>();


	public static class ForRecord {
		public BasicBlock condBB, stepBB, bodyBB, afterBB;
		public boolean removed = false;

		public ForRecord(BasicBlock condBB,
						 BasicBlock stepBB,
						 BasicBlock bodyBB,
						 BasicBlock afterBB) {
			this.condBB = condBB;
			this.stepBB = stepBB;
			this.bodyBB = bodyBB;
			this.afterBB = afterBB;
		}
	}

	public List<ForRecord> forRecordList = new ArrayList<>();

	public IRRoot() {}

	public void addBuiltInFunctions(List<FuncEntity> funcEntities) {
		for(FuncEntity funcEntity : funcEntities) {
			IRFunction irFunction = new IRFunction(funcEntity);
			irFunction.setBuiltIn(true);
			builtInFunctionMap.put(irFunction.getName(), irFunction);
			builtInFunctionList.add(irFunction);
			irFunction.usedPhysicalGeneralRegs.addAll(NASMRegisterSet.generalRegs);
//			System.err.println(irFunction.getName());
		}

		getBuiltInFunction("__string_class__length").setTrivial(true);
		getBuiltInFunction("__array_class__size").setTrivial(true);
	}


	public IRFunction getBuiltInFunction(String name) {
		return builtInFunctionMap.get(name);
	}

	public List<IRFunction> getBuiltInFunctionList() {
		return builtInFunctionList;
	}

	public void removeFunc(String string) {
		functionList.remove(functionMap.get(string));
		functionMap.remove(string);
	}

	public void addFunction(IRFunction function) {
		functionMap.put(function.getName(), function);
		functionList.add(function);
	}

	public IRFunction getFunction(String name) {
		return functionMap.get(name);
	}

	public List<StaticData> getStaticDataList() {
		return staticDataList;
	}

	public List<IRFunction> getFunctionList() {
		return functionList;
	}

	public void accept(IRVisitor visitor) {
		visitor.visit(this);
	}

	public void updateMaxArgsNum(int x) {
		if(x > maxArgsNum) maxArgsNum = x;
	}
	public int getMaxArgsNum() {
		return maxArgsNum;
	}

	public void setReversedRegister(PhysicalReg reversedRegister) {
		this.reversedRegister = reversedRegister;
	}

	public PhysicalReg getReversedRegister() {
		return reversedRegister;
	}

	public void updateCalleeSet() {
		Set<IRFunction> recursiveCalleeSet = new HashSet<>();
		for (IRFunction irFunction : getFunctionList()) {
			irFunction.recursiveCalleeSet.clear();
		}
		boolean changed = true;
		while (changed) {
			changed = false;
			for (IRFunction irFunction : getFunctionList()) {
				recursiveCalleeSet.clear();
				recursiveCalleeSet.addAll(irFunction.calleeSet);
				for (IRFunction calleeFunction : irFunction.calleeSet) {
					recursiveCalleeSet.addAll(calleeFunction.recursiveCalleeSet);
				}
				if (!recursiveCalleeSet.equals(irFunction.recursiveCalleeSet)) {
					irFunction.recursiveCalleeSet.clear();
					irFunction.recursiveCalleeSet.addAll(recursiveCalleeSet);
					changed = true;
				}
			}
		}
	}


}
