/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
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

import org.apache.commons.lang.Validate;

/**
 * {@link Boolean}と{@link String}を相互変換するコンバータ。
 * 
 * @version $Id$
 * @author daisuke
 */
public class BooleanConverter implements Converter<Boolean> {
	
	BooleanConverter() {
	}
	
	public String toString(Boolean obj) {
		Validate.notNull(obj);
		return obj.toString();
	}
	
	public Boolean valueOf(String str) {
		Validate.notNull(str);
		return Boolean.valueOf(str);
	}
	
}
