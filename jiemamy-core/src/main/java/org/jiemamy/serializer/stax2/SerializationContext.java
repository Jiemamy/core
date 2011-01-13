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

import org.jiemamy.JiemamyContext;
import org.jiemamy.utils.collection.ArrayEssentialStack;
import org.jiemamy.utils.collection.EssentialStack;

/**
 * シリアライズ処理のコンテキストクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class SerializationContext {
	
	/** コンテナスタック */
	private final EssentialStack<JiemamyOutputContainer> containerStack =
			new ArrayEssentialStack<JiemamyOutputContainer>();
	
	private final JiemamyContext context;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param document XMLドキュメント
	 */
	SerializationContext(JiemamyContext context, SMOutputDocument document) {
		this.context = context;
		push(new JiemamyDocument(document));
	}
	
	/**
	 * somethingを取得する。 TODO for daisuke
	 * @return the context
	 */
	public JiemamyContext getContext() {
		return context;
	}
	
	/**
	 * コンテナスタックの一番上の要素を参照する。（スタックから削除はしない）
	 * 
	 * @return コンテナ
	 */
	public JiemamyOutputContainer peek() {
		return containerStack.peek();
	}
	
	/**
	 * コンテナスタックの一番上の要素を取得する。（スタックから削除する）
	 * 
	 * @return コンテナ
	 */
	public JiemamyOutputContainer pop() {
		return containerStack.pop();
	}
	
	/**
	 * コンテナスタックに要素を追加する。
	 * 
	 * @param container コンテナ
	 */
	public void push(JiemamyOutputContainer container) {
		containerStack.push(container);
	}
}
