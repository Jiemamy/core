/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/05
 *
 * This file is part of Jiemamy.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.jiemamy.serializer.stax2;

import org.codehaus.staxmate.out.SMOutputDocument;

import org.jiemamy.utils.collection.ArrayEssentialStack;
import org.jiemamy.utils.collection.EssentialStack;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class SerializationContext {
	
	private final EssentialStack<JiemamyOutputContainer> containerStack =
			new ArrayEssentialStack<JiemamyOutputContainer>();
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param document
	 */
	SerializationContext(SMOutputDocument document) {
		push(new JiemamyDocument(document));
	}
	
	public JiemamyOutputContainer peek() {
		return containerStack.peek();
	}
	
	public JiemamyOutputContainer pop() {
		return containerStack.pop();
	}
	
	public void push(JiemamyOutputContainer container) {
		containerStack.push(container);
	}
}
