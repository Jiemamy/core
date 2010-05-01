/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/01/20
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

import java.util.UUID;


/**
 * NOT NULL制約モデル。
 * 
 * @author daisuke
 */
public class DefaultNotNullConstraint extends AbstractConstraintModel implements NotNullConstraint {
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id モデルID
	 */
	public DefaultNotNullConstraint(UUID id) {
		super(id);
	}
}
