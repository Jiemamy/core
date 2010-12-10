/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/01/19
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

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.transaction.Command;
import org.jiemamy.transaction.EventBroker;

/**
 * {@link Command}を実装するための抽象クラス。
 * 
 * @author daisuke
 */
public abstract class AbstractCommand implements Command {
	
	private static Logger logger = LoggerFactory.getLogger(AbstractCommand.class);
	
	/** イベント通知のためのブローカ */
	private final EventBroker eventBroker;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param eventBroker イベント通知用{@link EventBroker}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public AbstractCommand(EventBroker eventBroker) {
		Validate.notNull(eventBroker);
		this.eventBroker = eventBroker;
		logger.debug("construct: " + this);
	}
	
	public final void execute() {
		execute0();
		eventBroker.fireCommandProcessed(this);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	/**
	 * コマンドの処理内容を実装する内部メソッド。
	 */
	protected abstract void execute0();
	
	/**
	 * イベント通知のためのブローカを取得する。
	 * 
	 * @return イベント通知のためのブローカ
	 */
	protected EventBroker getEventBroker() {
		return eventBroker;
	}
}
