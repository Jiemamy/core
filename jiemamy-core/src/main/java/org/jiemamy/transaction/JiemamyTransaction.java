/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/03/02
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
package org.jiemamy.transaction;

import java.util.Collection;

import com.google.common.collect.Lists;

import org.apache.commons.lang.Validate;

import org.jiemamy.Experimental;
import org.jiemamy.JiemamyContext;
import org.jiemamy.JiemamyError;
import org.jiemamy.dddbase.Entity;
import org.jiemamy.dddbase.EntityRef;
import org.jiemamy.dddbase.Repository;
import org.jiemamy.dddbase.RepositoryException;
import org.jiemamy.utils.collection.ArrayEssentialStack;
import org.jiemamy.utils.collection.EssentialStack;
import org.jiemamy.utils.collection.EssentialStacks;

/**
 * Jiemamyのモデル操作を単純化するためのファサードインターフェイス。
 * 
 * <p>ファサードの具体的な目的としては、以下の通り。
 * <ul>
 *   <li>不整合モデルが出来ないよう、適切な操作インターフェイスを提供する。</li>
 *   <li>モデルの編集に対するイベント発生をサポートする。</li>
 *   <li>{@link SavePoint}によるロールバックを提供する。</li>
 * </ul></p>
 * 
 * @since 0.3
 * @author daisuke
 */
@Experimental
public class JiemamyTransaction implements StoredEventListener {
	
	/** このファサードが発行したセーブポイントの集合 */
	private Collection<SavePoint> publishedSavePoints = Lists.newArrayList();
	
	/** UNDOスタック */
	protected EssentialStack<StoredEvent<?>> undoStack = new ArrayEssentialStack<StoredEvent<?>>();
	
	/** イベントブローカ */
	protected EventBroker eventBroker;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param context {@link JiemamyContext}
	 */
	public JiemamyTransaction(JiemamyContext context) {
		context.getEventBroker().addListener(this);
	}
	
	public void commandExecuted(StoredEvent<?> event) {
		Validate.notNull(event);
		undoStack.push(event);
	}
	
	/**
	 * モデルの状態をセーブポイントまでロールバックする。
	 * 
	 * @param <T> エンティティの型
	 * @param savePoint セーブポイント
	 * @throws IllegalArgumentException このファサードが発行したｓ{@link SavePoint}でない場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.2
	 */
	@SuppressWarnings("unchecked")
	public <T extends Entity>void rollback(SavePoint savePoint) {
		Validate.notNull(savePoint);
		if (publishedSavePoints.contains(savePoint) == false) {
			throw new IllegalArgumentException();
		}
		
		// this.undoStack(現状スタック)が ABCPQR, savePointStack(セーブ時スタック)が ABCXYZ だったとすると…
		EssentialStack<StoredEvent<?>> savePointStack = savePoint.getUndoStackSnapshot();
		
		// intersect = ABC (先頭共通部分の抽出)
		EssentialStack<StoredEvent<?>> intersect = EssentialStacks.intersection(undoStack, savePointStack);
		
		// undoStackMinus = PQR (引き算)
		EssentialStack<StoredEvent<?>> undoStackMinus = EssentialStacks.minus(undoStack, intersect);
		
		// RQPの順番（pop順）でundoコマンドを実行し、現状を ABC 状態まで退行させる。
		while (undoStackMinus.isEmpty() == false) {
			StoredEvent<T> command = (StoredEvent<T>) undoStackMinus.pop();
			Repository<T> source = command.getSource();
			T before = command.getBefore();
			T after = command.getAfter();
			try {
				if (before == null) {
					source.delete((EntityRef<? extends T>) after.toReference());
				} else {
					source.store(before);
				}
			} catch (RepositoryException e) {
				throw new JiemamyError("OnMemory実装なので発生しないはず", e);
			}
		}
		
		// savePointStackMinus = XYZ (引き算)
		EssentialStack<StoredEvent<?>> savePointStackMinus = EssentialStacks.minus(savePointStack, intersect);
		
		// XYZの順番（foreach順）でredoコマンド（スタックから得られたコマンドの逆コマンド）を実行し、savePoint状態を再現する。
		for (StoredEvent<?> command : savePointStackMinus) {
			StoredEvent<T> negateCommand = (StoredEvent<T>) command;
			Repository<T> source = negateCommand.getSource();
			T after = negateCommand.getAfter();
			T before = negateCommand.getBefore();
			try {
				if (after == null) {
					source.delete((EntityRef<? extends T>) before.toReference());
				} else {
					source.store(after);
				}
			} catch (RepositoryException e) {
				throw new JiemamyError("OnMemory実装なので発生しないはず", e);
			}
		}
		
		// 現状スタックをセーブ時の状況に直しておく。
		undoStack = savePointStack;
	}
	
	/**
	 * 現在のモデルの状態にロールバックするためのセーブポイントを取得する。
	 * 
	 * @return セーブポイント
	 * @since 0.2
	 */
	public SavePoint save() {
		EssentialStack<StoredEvent<?>> undoCopy = new ArrayEssentialStack<StoredEvent<?>>(undoStack);
		SavePoint sp = new SavePoint(undoCopy);
		publishedSavePoints.add(sp);
		return sp;
	}
	
}
