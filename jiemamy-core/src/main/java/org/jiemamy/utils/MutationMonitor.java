/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/12/14
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
package org.jiemamy.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.JiemamyContext;

/**
 * コレクションに対するミューテーションを監視するデバッグ用クラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class MutationMonitor {
	
	private static Logger logger = LoggerFactory.getLogger(MutationMonitor.class);
	

	@SuppressWarnings("unchecked")
	public static <E>List<E> monitor(List<E> core) {
		if (JiemamyContext.isDebug()) {
			return wrap(core, List.class);
		}
		return core;
	}
	
	public static <K, V>Map<K, V> monitor(Map<K, V> core) {
		if (JiemamyContext.isDebug()) {
			return wrap(core);
		}
		return core;
	}
	
	@SuppressWarnings("unchecked")
	public static <E>Set<E> monitor(Set<E> core) {
		if (JiemamyContext.isDebug()) {
			return wrap(core, Set.class);
		}
		return core;
	}
	
	@SuppressWarnings("unchecked")
	public static <E>SortedSet<E> monitor(SortedSet<E> core) {
		if (JiemamyContext.isDebug()) {
			return wrap(core, SortedSet.class);
		}
		return core;
	}
	
	static <C extends Collection<E>, E>C wrap(final C core, Class<C> clazz) {
		return clazz.cast(Proxy.newProxyInstance(null, new Class<?>[] {
			clazz
		}, new InvocationHandler() {
			
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				if (Arrays.asList("add", "addAll", "remove", "removeAll", "retainAll", "clear").contains(
						method.getName())) {
					logger.warn("{} on {}", method.toString(), core.toString());
					logger.trace("stack trace: ", new Exception());
				}
				return method.invoke(core, args);
			}
		}));
		
	}
	
	@SuppressWarnings("unchecked")
	static <K, V>Map<K, V> wrap(final Map<K, V> core) {
		return (Map<K, V>) Proxy.newProxyInstance(null, new Class<?>[] {
			Map.class
		}, new InvocationHandler() {
			
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				if (Arrays.asList("put", "putAll", "remove", "clear").contains(method.getName())) {
					logger.warn("{} on {}", method.toString(), core.toString());
					logger.trace("stack trace: ", new Exception());
				}
				return method.invoke(core, args);
			}
		});
		
	}
	
	private MutationMonitor() {
	}
	
}
