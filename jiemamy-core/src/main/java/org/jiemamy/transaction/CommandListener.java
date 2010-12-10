/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/02/09
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

import org.jiemamy.IdentifiableEntity;

/**
 * EDITコマンドが実行されたイベントの通知を受け取るリスナ。
 * 
 * @since 0.2
 * @author shin1ogawa
 */
public interface CommandListener {
	
	/**
	 * コマンドが実行されたことを通知するcallbackメソッド。
	 * 
	 * <p>監視対象に変更があった場合に{@link EventBroker}によってcallbackされる。</p>
	 * 
	 * @param command 実行されたコマンド
	 * @since 0.2
	 */
	void commandExecuted(Command command);
	
	/**
	 * 監視対象を返す。
	 * 
	 * @return 監視対象の{@link IdentifiableEntity}
	 * @since 0.2
	 */
	IdentifiableEntity getTargetModel();
	
}
