/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/01/26
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

import org.jiemamy.validator.impl.CheckConstraintValidator;
import org.jiemamy.validator.impl.ColumnValidator;
import org.jiemamy.validator.impl.DataTypeValidator;
import org.jiemamy.validator.impl.DatabaseObjectNameCollisionValidator;
import org.jiemamy.validator.impl.ForeignKeyValidator;
import org.jiemamy.validator.impl.IdCollisionValidator;
import org.jiemamy.validator.impl.IndexValidator;
import org.jiemamy.validator.impl.KeyConstraintValidator;
import org.jiemamy.validator.impl.NotNullConstraintValidator;
import org.jiemamy.validator.impl.PrimaryKeyValidator;
import org.jiemamy.validator.impl.ReferenceValidator;
import org.jiemamy.validator.impl.TableValidator;

/**
 * 標準バリデータ。
 * 
 * @version $Id$
 * @author daisuke
 */
public final class Validators {
	
	// CHECKSTYLE:OFF
	
	public static final Validator CheckConstraintValidator = new CheckConstraintValidator();
	
	public static final Validator ColumnValidator = new ColumnValidator();
	
	public static final Validator DatabaseObjectNameCollisionValidator = new DatabaseObjectNameCollisionValidator();
	
	public static final Validator DataTypeValidator = new DataTypeValidator();
	
	public static final Validator ForeignKeyValidator = new ForeignKeyValidator();
	
	public static final Validator IdCollisionValidator = new IdCollisionValidator();
	
	public static final Validator IndexValidator = new IndexValidator();
	
	public static final Validator KeyConstraintValidator = new KeyConstraintValidator();
	
	public static final Validator NotNullConstraintValidator = new NotNullConstraintValidator();
	
	public static final Validator PrimaryKeyValidator = new PrimaryKeyValidator();
	
	public static final Validator ReferenceValidator = new ReferenceValidator();
	
	public static final Validator TableValidator = new TableValidator();
	
	private static Validator[] values = new Validator[] {
		CheckConstraintValidator,
		ColumnValidator,
		DatabaseObjectNameCollisionValidator,
		DataTypeValidator,
		ForeignKeyValidator,
		IdCollisionValidator,
		IndexValidator,
		KeyConstraintValidator,
		NotNullConstraintValidator,
		PrimaryKeyValidator,
		ReferenceValidator,
		TableValidator
	};
	

	// CHECKSTYLE:ON
	
	public static Validator[] values() {
		return values.clone();
	}
	
	private Validators() {
	}
}
