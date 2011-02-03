/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2009/01/21
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
package org.jiemamy.validator.impl;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.JiemamyContext;
import org.jiemamy.JiemamyFacet;
import org.jiemamy.dddbase.Entity;
import org.jiemamy.validator.AbstractProblem;
import org.jiemamy.validator.AbstractValidator;
import org.jiemamy.validator.Problem;

/**
 * モデルIDが衝突しているかどうかを調べるバリデータ。
 * 
 * <ul>
 *   <li>全てのモデルは、IDが重複していてはならない。</li>
 * </uL>
 * 
 * @author daisuke
 */
public class IdCollisionValidator extends AbstractValidator {
	
	private static Logger logger = LoggerFactory.getLogger(IdCollisionValidator.class);
	

	public Collection<? extends Problem> validate(JiemamyContext context) {
		Set<UUID> uuid = Sets.newHashSet();
		Validate.notNull(context);
		Collection<Problem> problems = Lists.newArrayList();
		Collection<Entity> entities = Lists.newArrayList();
		
		entities.addAll(context.getDatabaseObjects());
		entities.addAll(context.getDataSets());
		for (JiemamyFacet facet : context.getFacets()) {
			entities.addAll(facet.getEntities());
		}
		for (Entity entity : entities) {
			check(entity, uuid, problems);
		}
		return problems;
	}
	
	void check(Entity entity, Set<UUID> uuid, Collection<Problem> problems) {
		check0(entity, uuid, problems);
		Collection<? extends Entity> subEntities = entity.getSubEntities();
		for (Entity subEntity : subEntities) {
			check(subEntity, uuid, problems);
		}
	}
	
	void check0(Entity entity, Set<UUID> uuid, Collection<Problem> problems) {
		if (uuid.contains(entity.getId())) {
			problems.add(new IdCollisionProblem(entity));
			logger.warn("collision : " + entity.getId());
		}
		uuid.add(entity.getId());
	}
	

	/**
	 * ID名が衝突していることを示す{@link Problem}オブジェクト。
	 * 
	 * @author daisuke
	 */
	static class IdCollisionProblem extends AbstractProblem {
		
		/**
		 * インスタンスを生成する。
		 * 
		 * @param entity 衝突したentity
		 */
		public IdCollisionProblem(Entity entity) {
			super(null, "F0110");
			setArguments(new Object[] {
				entity.getId(),
				entity.toString(),
				null
			});
		}
	}
	
}
