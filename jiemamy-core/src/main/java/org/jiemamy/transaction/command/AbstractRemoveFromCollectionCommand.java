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

import org.apache.commons.lang.Validate;

import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.transaction.EventBroker;

/**
 * 親モデルが持つコレクションから要素を削除するEDITコマンドの抽象クラス。
 * 
 * @param <P> 追加されるコレクションを持つ型（親モデル）
 * @param <C> 削除対象の要素（子モデル）
 * @see AbstractAddToCollectionCommand
 * @author shin1ogawa
 */
public abstract class AbstractRemoveFromCollectionCommand<P extends Entity, C> extends AbstractCommand {
	
	/**
	 * 削除対象の子モデル要素
	 * 
	 * HACK 本来finalであるべきだが、subclassのコンストラクタから初期化する必要があったため、finalを外し protectedとなっている。
	 * 従って、コンストラクタ以外から代入を行ってはならない。
	 */
	protected C element;
	
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
	 * @param element 削除対象の子モデル要素
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public AbstractRemoveFromCollectionCommand(EventBroker eventBroker, P target, C element) {
		super(eventBroker);
		Validate.notNull(target);
		Validate.notNull(element);
		
		this.target = target;
		this.element = element;
		this.index = -1;
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param eventBroker イベント通知用{@link EventBroker}
	 * @param target 対象親モデル
	 * @param index 追加される位置インデックス
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public AbstractRemoveFromCollectionCommand(EventBroker eventBroker, P target, int index) {
		super(eventBroker);
		Validate.notNull(target);
		
		this.target = target;
		this.index = index;
	}
	
	/**
	 * 削除対象の子モデル要素を取得する。
	 * 
	 * @return 削除対象の子モデル要素
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
	
	public EntityRef<?> getTarget() {
		return target.toReference();
	}
	
	/**
	 * 親モデルが保持する、子モデルの集合を取得する。
	 * 
	 * @return 親モデルが保持する、子モデルの集合
	 */
	public abstract Collection<? super C> getTargetCollection();
	
	@Override
	public String toString() {
		return "RemoveCommand(" + element + " from " + target + ")";
	}
	
	@Override
	protected void execute0() {
		getTargetCollection().remove(element);
	}
	
}
