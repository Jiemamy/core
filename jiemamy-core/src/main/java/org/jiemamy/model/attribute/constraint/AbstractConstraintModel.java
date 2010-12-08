/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
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
package org.jiemamy.model.attribute.constraint;

/**
 * 抽象制約モデル。
 * 
 * @author daisuke
 */
public abstract class AbstractConstraintModel implements ConstraintModel {
	
	/** 物理名 */
	private String name;
	
	/** 論理名 */
	private String logicalName;
	
	/** 説明 */
	private String description;
	
	/** 遅延評価可能性モデル */
	private final DeferrabilityModel deferrability;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param name 物理名
	 * @param logicalName 論理名
	 * @param description 説明
	 * @param deferrability 遅延評価可能性
	 */
	public AbstractConstraintModel(String name, String logicalName, String description, DeferrabilityModel deferrability) {
		this.name = name;
		this.logicalName = logicalName;
		this.description = description;
		this.deferrability = deferrability;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AbstractConstraintModel)) {
			return false;
		}
		AbstractConstraintModel other = (AbstractConstraintModel) obj;
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (logicalName == null) {
			if (other.logicalName != null) {
				return false;
			}
		} else if (!logicalName.equals(other.logicalName)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (deferrability == null) {
			if (other.deferrability != null) {
				return false;
			}
		} else if (!deferrability.equals(other.deferrability)) {
			return false;
		}
		return true;
	}
	
	public DeferrabilityModel getDeferrability() {
		return deferrability;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getLogicalName() {
		return logicalName;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((logicalName == null) ? 0 : logicalName.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((deferrability == null) ? 0 : deferrability.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return "Constraint " + getName();
	}
	
	void setDescription(String description) {
		this.description = description;
	}
	
	void setLogicalName(String logicalName) {
		this.logicalName = logicalName;
	}
	
	void setName(String name) {
		this.name = name;
	}
}
