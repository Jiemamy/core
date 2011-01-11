/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/11
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
package org.jiemamy.model.parameter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import org.apache.commons.lang.Validate;

import org.jiemamy.JiemamyError;

/**
 * TODO for daisuke
 * 
 * @version $Id$
 * @author daisuke
 */
public final class ParameterMap implements Iterable<Map.Entry<String, String>>, Cloneable {
	
	/* final */HashMap<String, String> map = Maps.newHashMap();
	

	@Override
	@SuppressWarnings("unchecked")
	public ParameterMap clone() {
		try {
			ParameterMap clone = (ParameterMap) super.clone();
			clone.map = (HashMap<String, String>) map.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new JiemamyError("clone must to be supported.", e);
		}
	}
	
	public <T>T get(ParameterKey<T> key) {
		Validate.notNull(key);
		if (map.containsKey(key.getKeyString()) == false) {
			return null;
		}
		Converter<T> converter = key.getConverter();
		String string = map.get(key.getKeyString());
		return converter.valueOf(string);
	}
	
	public Iterator<Entry<String, String>> iterator() {
		return map.entrySet().iterator();
	}
	
	public <T>T put(ParameterKey<T> key, T value) {
		Validate.notNull(key);
		Validate.notNull(value);
		Converter<T> converter = key.getConverter();
		if (map.containsKey(key.getKeyString()) == false) {
			map.put(key.getKeyString(), converter.toString(value));
			return null;
		} else {
			String old = map.put(key.getKeyString(), converter.toString(value));
			return converter.valueOf(old);
		}
	}
	
	public String put(String key, String value) {
		return map.put(key, value);
	}
	
	public <T>T remove(ParameterKey<T> key) {
		Validate.notNull(key);
		Converter<T> converter = key.getConverter();
		String string = map.remove(key.getKeyString());
		return converter.valueOf(string);
	}
	
	public int size() {
		return map.size();
	}
	
	@Override
	public String toString() {
		return map.toString();
	}
}
