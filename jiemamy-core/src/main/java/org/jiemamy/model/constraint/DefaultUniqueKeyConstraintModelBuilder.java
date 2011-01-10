/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/05/14
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
package org.jiemamy.model.constraint;

/**
 * {@link DefaultUniqueKeyConstraintModel}のビルダークラス。
 * 
 * @version $Id$
 * @author Keisuke.K
 */
public final class DefaultUniqueKeyConstraintModelBuilder extends
		KeyConstraintModelBuilder<DefaultUniqueKeyConstraintModel, DefaultUniqueKeyConstraintModelBuilder> {
	
	@Override
	protected DefaultUniqueKeyConstraintModel createValueObject() {
		return new DefaultUniqueKeyConstraintModel(name, logicalName, description, keyColumns, deferrability);
	}
	
	@Override
	protected DefaultUniqueKeyConstraintModelBuilder getThis() {
		return this;
	}
	
	@Override
	protected DefaultUniqueKeyConstraintModelBuilder newInstance() {
		return new DefaultUniqueKeyConstraintModelBuilder();
	}
	
}