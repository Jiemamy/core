/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
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

import org.jiemamy.ServiceLocator;
import org.jiemamy.model.dbo.DatabaseObjectModel;

/**
 * 開始/終了スクリプトモデル。
 * 
 * @since 0.3
 * @author daisuke
 */
public interface AroundScript {
	
	/**
	 * スクリプトを取得する。
	 * 
	 * @param position スクリプト挿入位置
	 * @return スクリプト
	 * @since 0.3
	 */
	String getScript(Position position);
	
	/**
	 * スクリプトエンジンのクラス名を取得する。
	 * 
	 * @param position スクリプト挿入位置
	 * @return スクリプトエンジンのクラス名
	 * @since 0.3
	 */
	String getScriptEngineClassName(Position position);
	
	/**
	 * スクリプトを実行し、結果を得る。
	 * 
	 * @param serviceLocator サービスロケータ
	 * @param position スクリプト挿入位置
	 * @param target 周辺スクリプト挿入対象モデル
	 * @return スクリプト実行結果
	 * @throws ClassNotFoundException スクリプトエンジンのクラスが解決できない場合
	 * @since 0.3
	 */
	String process(ServiceLocator serviceLocator, Position position, DatabaseObjectModel target)
			throws ClassNotFoundException;
	
	/**
	 * スクリプトを設定する。
	 * 
	 * @param position スクリプト挿入位置
	 * @param script スクリプト
	 * @since 0.3
	 */
	void setScript(Position position, String script);
	
	/**
	 * スクリプトエンジンのクラス名を設定する。
	 * 
	 * @param position スクリプト挿入位置
	 * @param scriptEngineClassName スクリプトエンジンのクラス名
	 * @since 0.3
	 */
	void setScriptEngineClassName(Position position, String scriptEngineClassName);
	
}
