/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
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
package org.jiemamy.model.constraint;

import java.util.UUID;

import org.jiemamy.dddbase.AbstractEntity;
import org.jiemamy.dddbase.DefaultEntityRef;
import org.jiemamy.dddbase.EntityRef;

/**
 * 抽象制約モデル。
 * 
 * @author daisuke
 */
public abstract class AbstractConstraintModel extends AbstractEntity implements ConstraintModel {
	
	/** 物理名 */
	private String name;
	
	/** 論理名 */
	private String logicalName;
	
	/** 説明 */
	private String description;
	
	/** 遅延評価可能性モデル */
	private DeferrabilityModel deferrability;
	

	/**
	 * インスタンスを生成する。
	 * 
	 * @param id ENTITY ID
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public AbstractConstraintModel(UUID id) {
		super(id);
	}
	
	@Override
	public AbstractConstraintModel clone() {
		AbstractConstraintModel clone = (AbstractConstraintModel) super.clone();
		return clone;
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
	
	public void setDeferrability(DeferrabilityModel deferrability) {
		this.deferrability = deferrability;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setLogicalName(String logicalName) {
		this.logicalName = logicalName;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public EntityRef<? extends AbstractConstraintModel> toReference() {
		return new DefaultEntityRef<AbstractConstraintModel>(this);
	}
	
	@Override
	public String toString() {
		return super.toString() + "{name=" + name + "}";
	}
}
