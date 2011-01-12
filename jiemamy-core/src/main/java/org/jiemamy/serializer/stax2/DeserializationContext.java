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

import org.apache.commons.lang.Validate;
import org.codehaus.staxmate.in.SMHierarchicCursor;

import org.jiemamy.utils.collection.ArrayEssentialStack;
import org.jiemamy.utils.collection.EssentialStack;

/**
 * デシリアライズ処理のコンテキストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class DeserializationContext {
	
	private final EssentialStack<JiemamyCursor> stack = new ArrayEssentialStack<JiemamyCursor>();
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param cursor カーソル
	 */
	DeserializationContext(JiemamyCursor cursor) {
		Validate.notNull(cursor);
		stack.push(cursor);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param cursor カーソル
	 */
	DeserializationContext(SMHierarchicCursor cursor) {
		this(new JiemamyCursor(cursor));
	}
	
	/**
	 * カーソルを取得する。
	 * 
	 * @return カーソル
	 */
	public JiemamyCursor peek() {
		return stack.peek();
	}
	
	public JiemamyCursor pop() {
		return stack.pop();
	}
	
	public void push(JiemamyCursor cursor) {
		stack.push(cursor);
	}
	
	public void push(SMHierarchicCursor cursor) {
		push(new JiemamyCursor(cursor));
	}
}
