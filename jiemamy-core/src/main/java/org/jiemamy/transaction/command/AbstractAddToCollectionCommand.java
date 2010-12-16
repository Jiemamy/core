/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/02/21
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
package org.jiemamy.transaction.command;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.Validate;

import org.jiemamy.dddbase.Entity;
import org.jiemamy.transaction.EventBroker;

/**
 * 親モデルが持つコレクションに要素を追加するEDITコマンドの抽象クラス。
 * 
 * @param <P> 追加されるコレクションを持つ型（親モデル）
 * @param <C> 追加対象の要素の型（子モデル）
 * @see AbstractRemoveFromCollectionCommand
 * @author shin1ogawa
 */
public abstract class AbstractAddToCollectionCommand<P extends Entity, C> extends AbstractCommand {
	
	/** 追加対象の子モデル要素 */
	private final C element;
	
	/**
	 * 対象親モデル
	 * 
	 * イベントのバブリングのために{@link #getTarget()}で返す対象として必要。
	 */
	private final P target;
	
	/** 追加される位置インデックス */
	private final int index;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param eventBroker イベント通知用{@link EventBroker}
	 * @param target 対象親モデル
	 * @param element 追加対象の子モデル要素
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public AbstractAddToCollectionCommand(EventBroker eventBroker, P target, C element) {
		super(eventBroker);
		Validate.notNull(target);
		Validate.notNull(element);
		
		this.target = target;
		this.element = element;
		index = -1;
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param eventBroker イベント通知用{@link EventBroker}
	 * @param target 対象親モデル
	 * @param element 追加対象の子モデル要素
	 * @param index 追加される位置インデックス
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public AbstractAddToCollectionCommand(EventBroker eventBroker, P target, C element, int index) {
		super(eventBroker);
		Validate.notNull(target);
		Validate.notNull(element);
		
		this.target = target;
		this.element = element;
		this.index = index;
	}
	
	/**
	 * 追加対象の子モデル要素を取得する。
	 * 
	 * @return 追加対象の子モデル要素
	 */
	public C getElement() {
		return element;
	}
	
	/**
	 * 追加される位置インデックスを取得する。
	 * 
	 * @return 追加される位置インデックス
	 */
	public int getIndex() {
		return index;
	}
	
	public P getTarget() {
		return target;
	}
	
	/**
	 * 親モデルが保持する、子モデルの集合を取得する。
	 * 
	 * <p>このメソッドは、インスタンスの持つフィールドをそのまま返す。返される{@link Collection}を直接操作することで、
	* このオブジェクトのフィールドとして保持される{@link Collection}を変更することができる。</p>
	 * 
	 * @return 親モデルが保持する、子モデルの集合
	 */
	public abstract Collection<? super C> getTargetCollection();
	
	@Override
	protected void execute0() {
		Collection<? super C> targetList = getTargetCollection();
		if (index != -1 && targetList instanceof List<?>) {
			List<? super C> list = (List<? super C>) targetList;
			list.add(index, element);
		} else {
			targetList.add(element);
		}
	}
}
