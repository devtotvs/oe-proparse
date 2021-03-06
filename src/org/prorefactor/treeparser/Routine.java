/**
 * Routine.java - 
 * @author Peter Dalbadie
 * 21-Sep-2004
 * 
 * Copyright (c) 2004-2007 ProRefactor.org.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.prorefactor.treeparser;

import java.util.ArrayList;
import java.io.IOException;

import org.prorefactor.core.JPNode;
import com.joanju.DataXferStream;

/** Represents the definition of a Routine.
 * Is a Symbol - used as an entry in the symbol table.
 * A Routine is a Program_root, PROCEDURE, FUNCTION, or METHOD.
 */
public class Routine extends Symbol {

	/** Only to be used for persistence/serialization. */
	public Routine() {}
		
	public Routine (String name, SymbolScope definingScope, SymbolScope routineScope){
		super(definingScope);
		setName(name);
		this.routineScope = routineScope;
	}

	private ArrayList<Parameter> parameters = new ArrayList<Parameter>();
	private JPNode returnDatatypeNode = null;
	private SymbolScope routineScope;
	private int progressType;
	

	
	
	/** Called by the tree parser. */
	public void addParameter(Parameter p) { parameters.add(p); }

	
	@Override
	public Symbol copyBare(SymbolScope scope) {
		Routine ret = new Routine(getName(), scope, scope);
		ret.progressType = this.progressType;
		return ret;
	}

	
	/** @see org.prorefactor.treeparser.Symbol#fullName() */
	@Override
	public String fullName() { return getName(); }
	
	
	public ArrayList<Parameter> getParameters() { return parameters; }

	
	/** Return TokenTypes: Program_root, PROCEDURE, FUNCTION, or METHOD. */
	@Override
	public int getProgressType() { return progressType; }
	

	/** Null for PROCEDURE, node of the datatype for FUNCTION or METHOD.
	 * For a Class return value, won't be the CLASS node, but the TYPE_NAME node.
	 * TODO For 10.1B, anything that uses this might want to look up the fully qualified class name.
	 */
	public JPNode getReturnDatatypeNode() {return returnDatatypeNode;}


	public SymbolScope getRoutineScope(){ return routineScope; }
	
	
	public Routine setProgressType(int t) { progressType=t; return this; }

	
	/** Set by TreeParser01 for functions and methods. */
	public void setReturnDatatypeNode(JPNode n) {this.returnDatatypeNode = n;}


	/** Implement Xferable. */
	@Override
	public void writeXferBytes(DataXferStream out) throws IOException {
		super.writeXferBytes(out);	//To change body of overridden methods use File | Settings | File Templates.
		out.writeRef(parameters);
		out.writeInt(progressType);
		out.writeRef(returnDatatypeNode);
		out.writeRef(routineScope);
	}
	/** Implement Xferable. */
	@Override
	public void writeXferSchema(DataXferStream out) throws IOException {
		super.writeXferSchema(out);	//To change body of overridden methods use File | Settings | File Templates.
		out.schemaRef("parameters");
		out.schemaInt("progressType");
		out.schemaRef("returnDatatypeNode");
		out.schemaRef("routineScope");
	}


}
