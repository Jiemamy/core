/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2009/09/14
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
package org.jiemamy.model.script;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dddbase.UUIDEntity;
import org.jiemamy.dddbase.UUIDEntityRef;
import org.jiemamy.model.DbObject;
import org.jiemamy.script.ScriptEngine;
import org.jiemamy.script.ScriptException;

/**
 * 開始/終了スクリプトモデル。
 * 
 * <p>このインターフェイスで定義する全てのメソッドは冪等でなければならない(must)。</p>
 * 
 * @since 0.3
 * @author daisuke
 */
public interface JmAroundScript extends UUIDEntity {
	
	JmAroundScript clone();
	
	/**
	 * コアモデルへの参照を取得する。
	 * 
	 * @return コアモデルへの参照。コアが無い場合は{@code null}
	 * @since 0.3
	 */
	UUIDEntityRef<? extends DbObject> getCoreModelRef();
	
	/**
	 * スクリプトを取得する。
	 * 
	 * @param position スクリプト挿入位置
	 * @return スクリプト
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.3
	 */
	String getScript(Position position);
	
	/**
	 * スクリプトエンジンを取得する。
	 * 
	 * @param position スクリプト挿入位置
	 * @return {@link ScriptEngine}
	 * @throws ClassNotFoundException エンジンが見つからなかった場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	ScriptEngine getScriptEngine(Position position) throws ClassNotFoundException;
	
	/**
	 * スクリプトエンジンのクラス名を取得する。
	 * 
	 * @param position スクリプト挿入位置
	 * @return スクリプトエンジンのクラス名
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.3
	 */
	String getScriptEngineClassName(Position position);
	
	/**
	 * スクリプトを実行し、結果を得る。
	 * 
	 * @param context コンテキスト
	 * @param position スクリプト挿入位置
	 * @param target 周辺スクリプト挿入対象モデル
	 * @return スクリプト実行結果
	 * @throws ClassNotFoundException スクリプトエンジンのクラスが解決できない場合
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @throws ScriptException スクリプトの実行に失敗した場合
	 * @since 0.3
	 */
	String process(JiemamyContext context, Position position, Object target) throws ClassNotFoundException,
			ScriptException;
	
	UUIDEntityRef<? extends JmAroundScript> toReference();
}
