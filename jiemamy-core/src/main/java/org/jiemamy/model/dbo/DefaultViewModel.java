/*
 * Copyright 2007-2009 Jiemamy Project and the Others.
 * Created on 2008/06/09
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
package org.jiemamy.model.dbo;

import java.util.UUID;

import org.jiemamy.model.DefaultEntityRef;
import org.jiemamy.model.EntityRef;

/**
 * ビューモデル
 * 
 * @author daisuke
 */
public final class DefaultViewModel extends AbstractDatabaseObjectModel implements ViewModel {
	
	/** VIEW定義SELECT文 */
	private String definition;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * <p>ENTITY IDは自動生成される。</p>
	 */
	public DefaultViewModel() {
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 */
	public DefaultViewModel(UUID id) {
		super(id);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof DefaultViewModel)) {
			return false;
		}
		DefaultViewModel other = (DefaultViewModel) obj;
		if (definition == null) {
			if (other.definition != null) {
				return false;
			}
		} else if (!definition.equals(other.definition)) {
			return false;
		}
		return true;
	}
	
	public String getDefinition() {
		return definition;
	}
	
	public EntityRef<?> getReference() {
		return new DefaultEntityRef<ViewModel>(this);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((definition == null) ? 0 : definition.hashCode());
		return result;
	}
	
	/**
	 * somethingを設定する。 TODO for daisuke
	 * @param definition the definition to set
	 */
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	
}
