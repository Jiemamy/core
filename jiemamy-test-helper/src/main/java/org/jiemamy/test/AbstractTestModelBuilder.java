/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2009/09/02
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
package org.jiemamy.test;

import org.jiemamy.JiemamyContext;
import org.jiemamy.dialect.GenericDialect;

/**
 * {@link TestModelBuilder}の骨格実装。
 * 
 * @author daisuke
 */
public abstract class AbstractTestModelBuilder implements TestModelBuilder {
	
	private static final String DIALECT_CLASS_NAME = GenericDialect.class.getName();
	
	
	public JiemamyContext build() {
		return build(DIALECT_CLASS_NAME);
	}
	
}
