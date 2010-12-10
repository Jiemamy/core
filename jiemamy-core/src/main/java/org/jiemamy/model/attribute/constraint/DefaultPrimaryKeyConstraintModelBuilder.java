/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/05/07
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
package org.jiemamy.model.attribute.constraint;

/**
 * {@link DefaultPrimaryKeyConstraintModel}のビルダークラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class DefaultPrimaryKeyConstraintModelBuilder extends
		KeyConstraintModelBuilder<DefaultPrimaryKeyConstraintModel, DefaultPrimaryKeyConstraintModelBuilder> {
	
	@Override
	protected DefaultPrimaryKeyConstraintModel createValueObject() {
		return new DefaultPrimaryKeyConstraintModel(name, logicalName, description, keyColumns, deferrability);
	}
	
	@Override
	protected DefaultPrimaryKeyConstraintModelBuilder getThis() {
		return this;
	}
	
	@Override
	protected DefaultPrimaryKeyConstraintModelBuilder newInstance() {
		return new DefaultPrimaryKeyConstraintModelBuilder();
	}
	
}
