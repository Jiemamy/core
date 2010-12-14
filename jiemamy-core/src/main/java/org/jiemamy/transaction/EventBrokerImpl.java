/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/02/08
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

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.utils.LogMarker;

/**
 * EDITコマンドの実行を監視し、登録されている{@link CommandListener}にイベントを通知する。
 * 
 * <p>{@link Command}が実行されたタイミングで、{@link #listeners}として保持している{@link CommandListener}の中で,
 * {@link Command#getTarget()}を監視する必要があるものにイベントを通知する。</p>
 * 
 * @author shin1ogawa
 */
public class EventBrokerImpl implements EventBroker {
	
	private static Logger logger = LoggerFactory.getLogger(EventBrokerImpl.class);
	
	private List<CommandListener> listeners = Lists.newArrayList();
	
	private DispatchStrategy strategy = new DefaultDispatchStrategy();
	
	private Map<CommandListener, DispatchStrategy> strategies = Maps.newHashMap();
	

	public void addListener(CommandListener listener) {
		assert listeners != null;
		Validate.notNull(listener);
		listeners.add(listener);
		logger.info(LogMarker.LIFECYCLE, "CommandListener is registered: " + listener.toString());
	}
	
	public void addListener(CommandListener listener, DispatchStrategy strategy) {
		assert listeners != null;
		Validate.notNull(listener);
		Validate.notNull(strategy);
		listeners.add(listener);
		strategies.put(listener, strategy);
		logger.info(LogMarker.LIFECYCLE, "CommandListener and DispatchStrategy is registered: " + listener.toString()
				+ "," + strategy.toString());
	}
	
	public void fireCommandProcessed(Command command) {
		Validate.notNull(command);
		assert listeners != null;
		logger.info(LogMarker.LIFECYCLE, "EventBroker is kicked enter: " + command.toString());
		logger.debug(LogMarker.LIFECYCLE, "target : " + command.getTarget());
		// java.util.ConcurrentModificationExceptionへの対策。
		List<CommandListener> listenersSnapthot = Lists.newArrayList(listeners);
		for (CommandListener listener : listenersSnapthot) {
			boolean needToDispatch = false;
			if (strategies.containsKey(listener)) {
				needToDispatch = strategies.get(listener).needToDispatch(listener, command);
			} else {
				needToDispatch = strategy.needToDispatch(listener, command);
			}
			if (needToDispatch) {
				listener.commandExecuted(command);
				logger.debug(LogMarker.LIFECYCLE, "Listener is kicked: " + listener.toString());
			}
		}
		logger.info(LogMarker.LIFECYCLE, "EventBroker is kicked exit: " + command.toString());
	}
	
	/**
	 * (for debug) 登録されたリスナのリストを取得する。
	 * 
	 * <p>このメソッドは、インスタンスの持つ{@link List}の防御コピーを返す。返される{@link List}を直接操作しても、
	 * このオブジェクトの属性を操作することはできない。ただし、コレクションの要素はコピーされない、シャローコピーである。</p>
	 * 
	 * @return the listeners
	 */
	public List<CommandListener> getListeners() {
		assert listeners != null;
		return Lists.newArrayList(listeners);
	}
	
	public void removeListener(CommandListener listener) {
		assert listeners != null;
		Validate.notNull(listener);
		listeners.remove(listener);
		if (strategies.containsKey(listener)) {
			strategies.remove(listener);
		}
		logger.info(LogMarker.LIFECYCLE, "CommandListener is unregistered: " + listener.toString());
	}
	
	public void setStrategy(DispatchStrategy strategy) {
		Validate.notNull(strategy);
		this.strategy = strategy;
	}
	

	static class DefaultDispatchStrategy implements DispatchStrategy {
		
		public boolean needToDispatch(CommandListener listener, Command command) {
			return true;
		}
	}
}
