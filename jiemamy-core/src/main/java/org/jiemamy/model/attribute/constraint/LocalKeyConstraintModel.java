/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2009/01/26
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
 * 内部キー制約を表すインターフェイス。
 * 
 * <p>内部キーとは、外部キーではないキー制約、すなわち {@link UniqueKeyConstraintModel} や {@link PrimaryKeyConstraintModel} の事である。</p>
 * 
 * @since 0.2
 * @author daisuke
 */
public interface LocalKeyConstraintModel extends KeyConstraintModel {
	
}
