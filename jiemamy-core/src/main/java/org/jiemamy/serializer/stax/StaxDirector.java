/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2011/01/05
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
package org.jiemamy.serializer.stax;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import com.google.common.collect.Maps;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jiemamy.JiemamyContext;
import org.jiemamy.JiemamyContextStaxHandler;
import org.jiemamy.JiemamyFacet;
import org.jiemamy.SimpleJmMetadata;
import org.jiemamy.SimpleJmMetadataStaxHandler;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.column.JmColumnStaxHandler;
import org.jiemamy.model.constraint.JmCheckConstraint;
import org.jiemamy.model.constraint.JmCheckConstraintStaxHandler;
import org.jiemamy.model.constraint.JmDeferrability;
import org.jiemamy.model.constraint.JmDeferrabilityStaxHandler;
import org.jiemamy.model.constraint.JmForeignKeyConstraint;
import org.jiemamy.model.constraint.JmForeignKeyConstraintStaxHandler;
import org.jiemamy.model.constraint.JmPrimaryKeyConstraint;
import org.jiemamy.model.constraint.JmPrimaryKeyConstraintStaxHandler;
import org.jiemamy.model.constraint.JmUniqueKeyConstraint;
import org.jiemamy.model.constraint.JmUniqueKeyConstraintStaxHandler;
import org.jiemamy.model.dataset.SimpleJmDataSet;
import org.jiemamy.model.dataset.SimpleJmDataSetStaxHandler;
import org.jiemamy.model.dataset.SimpleJmRecord;
import org.jiemamy.model.dataset.SimpleJmRecordStaxHandler;
import org.jiemamy.model.datatype.SimpleDataType;
import org.jiemamy.model.datatype.SimpleDataTypeStaxHandler;
import org.jiemamy.model.datatype.SimpleRawTypeDescriptor;
import org.jiemamy.model.datatype.SimpleRawTypeDescriptorStaxHandler;
import org.jiemamy.model.domain.JmDomain;
import org.jiemamy.model.domain.JmDomainStaxHandler;
import org.jiemamy.model.domain.JmDomainTypeStaxHandler;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.model.table.JmTableStaxHandler;
import org.jiemamy.model.view.JmView;
import org.jiemamy.model.view.JmViewStaxHandler;
import org.jiemamy.serializer.SerializationException;
import org.jiemamy.xml.CoreQName;
import org.jiemamy.xml.JiemamyQName;

/**
 * シリアライズ・デシリアライズ処理の指揮をとり、各 {@link StaxHandler} に処理を委譲するクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class StaxDirector {
	
	private static Logger logger = LoggerFactory.getLogger(StaxDirector.class);
	
	final Map<String, StaxHandler<?>> handlersWithFqcnKey = Maps.newHashMap();
	
	final Map<QName, StaxHandler<?>> handlersWithQNameKey = Maps.newHashMap();
	
	final DummyHandler dummy;
	
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param context コンテキスト
	 * @throws IllegalArgumentException 引数に{@code null}を与えた場合
	 */
	public StaxDirector(JiemamyContext context) {
		Validate.notNull(context);
		
		dummy = new DummyHandler(this); // これがケツ持ちをしてる
		
		addHandler(JiemamyContext.class, CoreQName.JIEMAMY, new JiemamyContextStaxHandler(this));
		addHandler(SimpleJmMetadata.class, CoreQName.META, new SimpleJmMetadataStaxHandler(this));
		
		addHandler(JmView.class, CoreQName.VIEW, new JmViewStaxHandler(this));
		addHandler(JmTable.class, CoreQName.TABLE, new JmTableStaxHandler(this));
		addHandler(JmDomain.class, CoreQName.DOMAIN, new JmDomainStaxHandler(this));
		
		addHandler(JmColumn.class, CoreQName.COLUMN, new JmColumnStaxHandler(this));
		
		addHandler(SimpleDataType.class, CoreQName.DATA_TYPE, new SimpleDataTypeStaxHandler(this));
		
		// CoreQName.TYPE_DESCのデフォルトハンドラはSimpleRawTypeDescriptorの為、それを最後に登録すること
		addHandler(JmDomain.DomainType.class, CoreQName.TYPE_DESC, new JmDomainTypeStaxHandler(this));
		addHandler(SimpleRawTypeDescriptor.class, CoreQName.TYPE_DESC, new SimpleRawTypeDescriptorStaxHandler(this));
		
		addHandler(JmPrimaryKeyConstraint.class, CoreQName.PRIMARY_KEY, new JmPrimaryKeyConstraintStaxHandler(this));
		addHandler(JmForeignKeyConstraint.class, CoreQName.FOREIGN_KEY, new JmForeignKeyConstraintStaxHandler(this));
		addHandler(JmUniqueKeyConstraint.class, CoreQName.UNIQUE_KEY, new JmUniqueKeyConstraintStaxHandler(this));
		addHandler(JmCheckConstraint.class, CoreQName.CHECK, new JmCheckConstraintStaxHandler(this));
		addHandler(JmDeferrability.class, CoreQName.DEFERRABILITY, new JmDeferrabilityStaxHandler(this));
		
		addHandler(SimpleJmDataSet.class, CoreQName.DATASET, new SimpleJmDataSetStaxHandler(this));
		addHandler(SimpleJmRecord.class, CoreQName.RECORDS, new SimpleJmRecordStaxHandler(this));
		
		for (JiemamyFacet jiemamyFacet : context.getFacets()) {
			jiemamyFacet.prepareStaxHandlers(this);
		}
	}
	
	/**
	 * ハンドラを登録する。
	 * 
	 * @param <T> ハンドラが対応するモデルの型
	 * @param clazz ハンドラの対応するモデルの型
	 * @param jQName ハンドラの対応するXML装飾名
	 * @param handler ハンドラ
	 */
	public <T>void addHandler(Class<T> clazz, JiemamyQName jQName, StaxHandler<T> handler) {
		Validate.notNull(clazz);
		Validate.notNull(jQName);
		Validate.notNull(handler);
		handlersWithFqcnKey.put(clazz.getName(), handler);
		handlersWithQNameKey.put(jQName.getQName(), handler);
	}
	
	/**
	 * デシリアライズする。
	 * 
	 * @param <T> デシリアライズ結果の型
	 * @param dctx デシリアライズコンテキスト
	 * @return デシリアライズ結果
	 * @throws SerializationException デシリアライズに失敗した場合
	 */
	public <T>T direct(DeserializationContext dctx) throws SerializationException {
		Validate.notNull(dctx);
		StaxHandler<T> handler = findHandler(dctx.peek());
		return handler.handleDeserialization(dctx);
	}
	
	/**
	 * シリアライズする。
	 * 
	 * @param <T> シリアライズ対象の型
	 * @param target シリアライズ対象
	 * @param sctx シリアライズコンテキスト
	 * @throws SerializationException シリアライズに失敗した場合
	 */
	public <T>void direct(T target, SerializationContext sctx) throws SerializationException {
		Validate.notNull(target);
		Validate.notNull(sctx);
		StaxHandler<T> handler = findHandler(target);
		handler.handleSerialization(target, sctx);
	}
	
	@SuppressWarnings("unchecked")
	private <T>StaxHandler<T> findHandler(JiemamyCursor cursor) throws SerializationException {
		Validate.notNull(cursor);
		
		try {
			QName qName = cursor.getQName();
			
			if (cursor.hasAttr(CoreQName.CLASS)) {
				String className = cursor.getAttrValue(CoreQName.CLASS);
				return findHandler(className);
			}
			
			StaxHandler<T> handler = (StaxHandler<T>) handlersWithQNameKey.get(qName);
			
			if (handler == null) {
				// 例えば JiemamyContext が DiagramFacet を持っていないのに<diagram:*>要素が来た時など、このハンドラが相手をする。
				logger.warn("can not found deserialization handler : " + qName);
				handler = (StaxHandler<T>) dummy;
			}
			
			return handler;
		} catch (XMLStreamException e) {
			throw new SerializationException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T>StaxHandler<T> findHandler(String targetName) {
		Validate.notNull(targetName);
		StaxHandler<T> handler = (StaxHandler<T>) handlersWithFqcnKey.get(targetName);
		
		if (handler == null) {
			logger.warn("can not found serialization handler : " + targetName);
			handler = (StaxHandler<T>) dummy;
		}
		
		return handler;
	}
	
	private <T>StaxHandler<T> findHandler(T target) {
		Validate.notNull(target);
		return findHandler(target.getClass().getName());
	}
}
