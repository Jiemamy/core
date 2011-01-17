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
import org.jiemamy.dddbase.utils.CloneUtil;

/**
 * 不確定（存在するかどうか分からない・項目をcoreは知らず、dialectが知っているもの）なモデル情報
 * （パラメータ）を保持するクラス。
 * 
 * <p>{@link ParameterKey}が持つ {@link Converter} を利用して、タイプセーフに値を取り扱いつつ、
 * 容易に{@link String}へのシリアライズ・デシリアライズができる {@link Map} ライクなクラス。</p>
 * 
 * @version $Id$
 * @author daisuke
 */
public final class ParameterMap implements Iterable<Map.Entry<String, String>>, Cloneable {
	
	/* final */HashMap<String, String> map = Maps.newHashMap();
	

	@Override
	public ParameterMap clone() {
		try {
			ParameterMap clone = (ParameterMap) super.clone();
			clone.map = CloneUtil.cloneValueHashMap(map);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new JiemamyError("clone must to be supported.", e);
		}
	}
	
	/**
	 * キーに対応する値を取得する。
	 * 
	 * @param <T> 値の型
	 * @param key キー
	 * @return 値
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
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
	
	/**
	 * キーに対応する値を設定する。
	 * 
	 * @param <T> 値の型
	 * @param key キー
	 * @param value 値
	 * @return 古い値。無かった場合は{@code null}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
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
	
	/**
	 * キーに文字列に対応する値文字列を設定する。
	 * 
	 * @param key キー文字列
	 * @param value 値文字列
	 * @return 古い値文字列。無かった場合は{@code null}
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public String put(String key, String value) {
		return map.put(key, value);
	}
	
	/**
	 * キーに対応する値を削除する。
	 * 
	 * @param <T> 値の型
	 * @param key キー
	 * @return 削除された値
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public <T>T remove(ParameterKey<T> key) {
		Validate.notNull(key);
		Converter<T> converter = key.getConverter();
		String string = map.remove(key.getKeyString());
		return converter.valueOf(string);
	}
	
	/**
	 * パラメータ数を返す。
	 * 
	 * @return パラメータ数
	 */
	public int size() {
		return map.size();
	}
	
	@Override
	public String toString() {
		return map.toString();
	}
}
