/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2011/01/22
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
package org.jiemamy.composer;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.Validate;

import org.jiemamy.JiemamyContext;
import org.jiemamy.JiemamyError;

/**
 * {@link Importer}の骨格実装クラス。
 * 
 * @param <T> 設定オブジェクトの型
 * @version $Id$
 * @author daisuke
 */
public abstract class AbstractImporter<T extends ImportConfig> implements Importer<T> {
	
	public boolean importModel(JiemamyContext context, Map<String, Object> configMap) throws ImportException {
		Validate.notNull(context);
		Validate.notNull(configMap);
		
		T config = newSimpleConfigInstance();
		try {
			PropertyUtils.copyProperties(configMap, config);
		} catch (IllegalAccessException e) {
			throw new JiemamyError("SimpleDbImportConfig must have public setter.", e);
		} catch (InvocationTargetException e) {
			throw new JiemamyError("SimpleDbImportConfig's setter must not to thwow exceptions.", e);
		} catch (NoSuchMethodException e) {
			// ignore
		}
		return importModel(context, config);
	}
	
	/**
	 * この{@link Importer}用の{@link ImportConfig}のデフォルト実装インスタンスを生成し、返す。
	 * 
	 * @return この{@link Importer}用の{@link ImportConfig}のデフォルト実装インスタンス
	 */
	protected abstract T newSimpleConfigInstance();
	
}
