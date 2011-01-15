/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/02/02
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
package org.jiemamy.validator;

import java.util.List;

import org.jiemamy.validator.impl.CheckConstraintValidator;
import org.jiemamy.validator.impl.ColumnValidator;
import org.jiemamy.validator.impl.EntityNameCollisionValidator;
import org.jiemamy.validator.impl.ForeignKeyValidator;
import org.jiemamy.validator.impl.IdCollisionValidator;
import org.jiemamy.validator.impl.IndexValidator;
import org.jiemamy.validator.impl.KeyConstraintValidator;
import org.jiemamy.validator.impl.PrimaryKeyValidator;
import org.jiemamy.validator.impl.ReferenceValidator;
import org.jiemamy.validator.impl.TableValidator;

/**
 * 標準バリデータを全て動かすバリデータ。
 * 
 * @author daisuke
 */
public class AllValidator extends CompositeValidator {
	
	/**
	 * インスタンスを生成する。
	 */
	public AllValidator() {
		List<Validator> validators = getValidators();
		validators.add(new ColumnValidator());
		validators.add(new EntityNameCollisionValidator());
		validators.add(new ForeignKeyValidator());
		validators.add(new IdCollisionValidator());
		validators.add(new IndexValidator());
		validators.add(new KeyConstraintValidator());
		validators.add(new PrimaryKeyValidator());
		validators.add(new ReferenceValidator());
		validators.add(new TableValidator());
		validators.add(new CheckConstraintValidator());
	}
	
}
