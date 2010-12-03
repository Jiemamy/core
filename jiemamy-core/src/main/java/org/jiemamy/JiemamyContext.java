/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/08/29
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
package org.jiemamy;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;

import org.jiemamy.model.EntityLifecycleException;
import org.jiemamy.utils.Disposable;
import org.jiemamy.utils.collection.CollectionsUtil;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public class JiemamyContext implements Disposable {
	
	private static IdentityHashMap<Entity, JiemamyContext> map = CollectionsUtil.newIdentityHashMap();
	
	private JiemamyCore core;
	

	public JiemamyContext() {
		core = new JiemamyCore(this);
	}
	
	public void dispose() {
		synchronized (map) {
			Iterator<Map.Entry<Entity, JiemamyContext>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<Entity, JiemamyContext> entry = iterator.next();
				if (entry.getValue() == this) {
					iterator.remove();
				}
			}
		}
	}
	
	public void finishManage(Entity entity) {
		synchronized (map) {
			map.remove(entity);
		}
	}
	
	public JiemamyCore getCore() {
		return core;
	}
	
	public <T extends JiemamyFacet>T getFacet(Class<T> clazz) {
		return null;
	}
	
	public boolean isManaging(Entity entity) {
		synchronized (map) {
			return map.get(entity) == this;
		}
	}
	
	public void startManage(Entity entity) {
		synchronized (map) {
			if (isManaged(entity)) {
				throw new EntityLifecycleException();
			}
			map.put(entity, this);
		}
	}
	
	boolean isManaged(Entity entity) {
		synchronized (map) {
			if (map.containsKey(entity)) {
				return true;
			}
			return false;
		}
	}
}
