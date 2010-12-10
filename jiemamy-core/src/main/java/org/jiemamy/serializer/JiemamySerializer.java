/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2008/06/09
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
package org.jiemamy.serializer;

import java.io.InputStream;
import java.io.OutputStream;

import org.jiemamy.JiemamyContext;

/**
 * シリアライザインターフェイス。
 * 
 * <p>シリアライザの実装クラスはステートレスでなければならない。</p>
 * 
 * @author daisuke
 */
public interface JiemamySerializer {
	
	/**
	 * {@link InputStream} から {@link JiemamyContext} にデシリアライズする。
	 * 
	 * @param in デシリアライズするInputStream
	 * @return デシリアライズしたRootModel
	 * @throws SerializationException デシリアライズに失敗した時
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.2
	 */
	JiemamyContext deserialize(InputStream in) throws SerializationException;
	
	/**
	 * {@link JiemamyContext} を {@link InputStream} にシリアライズする。
	 * 
	 * @param context シリアライズする {@link JiemamyContext}
	 * @param out シリアライズ結果を出力するstream
	 * @throws SerializationException シリアライズに失敗した時
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 * @since 0.2
	 */
	void serialize(JiemamyContext context, OutputStream out) throws SerializationException;
	
}