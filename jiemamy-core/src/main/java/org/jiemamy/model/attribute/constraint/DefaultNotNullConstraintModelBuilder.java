/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2010/07/13
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
 * {@link DefaultNotNullConstraintModel}のビルダークラス。
 * 
 * @version $Id$
 * @author Keisuke.K
 */
public class DefaultNotNullConstraintModelBuilder extends
		NotNullConstraintModelBuilder<DefaultNotNullConstraintModel, DefaultNotNullConstraintModelBuilder> {
	
	@Override
	protected DefaultNotNullConstraintModel createValueObject() {
		return new DefaultNotNullConstraintModel(name, logicalName, description, deferrability, column);
	}
	
	@Override
	protected DefaultNotNullConstraintModelBuilder getThis() {
		return this;
	}
	
	@Override
	protected DefaultNotNullConstraintModelBuilder newInstance() {
		return new DefaultNotNullConstraintModelBuilder();
	}
	
}
