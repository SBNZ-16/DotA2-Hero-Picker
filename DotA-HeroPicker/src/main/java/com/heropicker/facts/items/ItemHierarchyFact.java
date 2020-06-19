package com.heropicker.facts.items;

import org.kie.api.definition.type.Position;

import com.heropicker.facts.Fact;

public class ItemHierarchyFact extends Fact {
	
	@Position(0)
	private String child;
	@Position(1)
	private String parent;
	
	
	public ItemHierarchyFact() {
		
	}

	public ItemHierarchyFact(String child, String parent) {
		super();
		this.child = child;
		this.parent = parent;
	}

	public String getChild() {
		return child;
	}

	public void setChild(String child) {
		this.child = child;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}
}
