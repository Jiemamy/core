/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2010/12/14
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
package org.jiemamy.dialect;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.commons.lang.Validate;

import org.jiemamy.dialect.TypeParameterSpec.Necessity;
import org.jiemamy.model.datatype.DataTypeCategory;
import org.jiemamy.model.datatype.DefaultTypeReference;
import org.jiemamy.model.datatype.TypeParameterKey;
import org.jiemamy.model.datatype.TypeReference;
import org.jiemamy.validator.CompositeValidator;
import org.jiemamy.validator.Validator;

/**
 * SQL方言の抽象実装クラス。
 * 
 * @version $Id$
 * @author daisuke
 * @deprecated テスト用のサンプルなので使わないこと
 */
@Deprecated
public class SampleDialect extends AbstractDialect {
	
	private Map<TypeReference, TypeParameterSpec[]> allDataTypes = Maps.newLinkedHashMap();
	
	private Validator validator = new CompositeValidator();
	

	/**
	 * インスタンスを生成する。
	 */
	public SampleDialect() {
		super("jdbc:dummy:foobar");
		allDataTypes.put(new DefaultTypeReference(DataTypeCategory.INTEGER), new TypeParameterSpec[] {
			TypeParameterSpec.of(TypeParameterKey.SERIAL, Necessity.OPTIONAL),
		});
		allDataTypes.put(new DefaultTypeReference(DataTypeCategory.DECIMAL), new TypeParameterSpec[] {
			TypeParameterSpec.of(TypeParameterKey.PRECISION, Necessity.REQUIRED),
			TypeParameterSpec.of(TypeParameterKey.SCALE, Necessity.REQUIRED)
		});
		allDataTypes.put(new DefaultTypeReference(DataTypeCategory.BOOLEAN), new TypeParameterSpec[0]);
		allDataTypes.put(new DefaultTypeReference(DataTypeCategory.VARCHAR), new TypeParameterSpec[] {
			TypeParameterSpec.of(TypeParameterKey.SIZE, Necessity.REQUIRED)
		});
		allDataTypes.put(new DefaultTypeReference(DataTypeCategory.TIMESTAMP), new TypeParameterSpec[] {
			TypeParameterSpec.of(TypeParameterKey.WITH_TIMEZONE, Necessity.OPTIONAL)
		});
	}
	
	public List<TypeReference> getAllTypeReferences() {
		return Lists.newArrayList(allDataTypes.keySet());
	}
	
	public Collection<TypeParameterSpec> getTypeParameterSpecs(TypeReference reference) {
		Validate.notNull(reference);
		return Lists.newArrayList(allDataTypes.get(reference));
	}
	
	public Validator getValidator() {
		return validator;
	}
}
