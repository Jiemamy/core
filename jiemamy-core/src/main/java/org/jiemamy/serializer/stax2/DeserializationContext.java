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

import org.jiemamy.JiemamyContext;
import org.jiemamy.utils.collection.EssentialStack;
import org.jiemamy.utils.collection.EssentialStacks;

/**
 * デシリアライズ処理のコンテキストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class DeserializationContext {
	
	private final EssentialStack<JiemamyCursor> stack = EssentialStacks.newArrayEssentialStack();
	
	private final JiemamyContext context;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param context コンテキスト
	 * @param cursor カーソル
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	DeserializationContext(JiemamyContext context, JiemamyCursor cursor) {
		Validate.notNull(context);
		Validate.notNull(cursor);
		this.context = context;
		stack.push(cursor);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param context コンテキスト
	 * @param cursor カーソル
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	DeserializationContext(JiemamyContext context, SMHierarchicCursor cursor) {
		this(context, new JiemamyCursor(cursor));
	}
	
	/**
	 * {@link JiemamyContext}を取得する。
	 * 
	 * @return {@link JiemamyContext}
	 */
	public JiemamyContext getContext() {
		assert context != null;
		return context;
	}
	
	/**
	 * カーソルを取得する。
	 * 
	 * @return {@link JiemamyCursor}
	 */
	public JiemamyCursor peek() {
		return stack.peek();
	}
	
	public JiemamyCursor pop() {
		return stack.pop();
	}
	
	public void push(JiemamyCursor cursor) {
		Validate.notNull(cursor);
		stack.push(cursor);
	}
	
	public void push(SMHierarchicCursor cursor) {
		Validate.notNull(cursor);
		push(new JiemamyCursor(cursor));
	}
}
