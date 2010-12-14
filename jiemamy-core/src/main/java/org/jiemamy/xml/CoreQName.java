/*
 * Copyright 2007-2010 Jiemamy Project and the Others.
 * Created on 2008/12/15
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
package org.jiemamy.xml;

import javax.xml.namespace.QName;

/**
 * Jiemamy XML Modelにおける、XMLのノード（要素や属性）の完全修飾名を保持する列挙型。
 * 
 * @since 0.2
 * @author daisuke
 */
public enum CoreQName implements JiemamyQName {
	
	/***/
	JIEMAMY(CoreNamespace.NS_CORE, "jiemamy"),

	// --------
	
	/***/
	DIALECT(CoreNamespace.NS_CORE, "dialect"),

	/***/
	SCHEMA_NAME(CoreNamespace.NS_CORE, "schemaName"),

	/***/
	BEGIN_SCRIPT(CoreNamespace.NS_CORE, "beginScript"),

	/***/
	END_SCRIPT(CoreNamespace.NS_CORE, "endScript"),

	/***/
	DESCRIPTION(CoreNamespace.NS_CORE, "description"),

	/***/
	NAME(CoreNamespace.NS_CORE, "name"),

	/***/
	LOGICAL_NAME(CoreNamespace.NS_CORE, "logicalName"),

	// --------
	
	/***/
	DOMAINS(CoreNamespace.NS_CORE, "domains"),

	/***/
	DOMAIN(CoreNamespace.NS_CORE, "domain"),

	// --------
	
	/***/
	ENTITIES(CoreNamespace.NS_CORE, "entities"),

	/***/
	TABLE(CoreNamespace.NS_CORE, "table"),

	/***/
	VIEW(CoreNamespace.NS_CORE, "view"),

	// --------
	
	/***/
	ATTRIBUTES(CoreNamespace.NS_CORE, "attributes"),

	/***/
	PRIMARY_KEY(CoreNamespace.NS_CORE, "primaryKey"),

	/***/
	UNIQUE_KEY(CoreNamespace.NS_CORE, "uniqueKey"),

	/***/
	FOREIGN_KEY(CoreNamespace.NS_CORE, "foreignKey"),

	/***/
	TABLE_CHECK_CONSTRAINT(CoreNamespace.NS_CORE, "tableCheck"),

	/***/
	COLUMN(CoreNamespace.NS_CORE, "column"),

	/***/
	REFERENCE_COLUMNS(CoreNamespace.NS_CORE, "referenceColumns"),

	// --------
	
	/***/
	INDEXES(CoreNamespace.NS_CORE, "indexes"),

	/***/
	INDEX(CoreNamespace.NS_CORE, "index"),

	/***/
	UNIQUE(CoreNamespace.NS_CORE, "unique"),

	/***/
	INDEX_COLUMNNS(CoreNamespace.NS_CORE, "indexColumns"),

	/***/
	INDEX_COLUMNN(CoreNamespace.NS_CORE, "indexColumn"),

	/***/
	SORT_ORDER(CoreNamespace.NS_CORE, "sortOrder"),

	// --------
	
	/***/
	DATA_TYPE(CoreNamespace.NS_CORE, "dataType"),

	/***/
	TYPE_CATEGORY(CoreNamespace.NS_CORE, "typeCategory"),

	/***/
	TYPE_NAME(CoreNamespace.NS_CORE, "typeName"),

	/***/
	ADAPTER(CoreNamespace.NS_CORE, "adapter"),

	/***/
	DEFAULT_VALUE(CoreNamespace.NS_CORE, "defaultValue"),

	/***/
	NOT_NULL(CoreNamespace.NS_CORE, "notNull"),

	/***/
	COLUMN_UNIQUE_KEY(CoreNamespace.NS_CORE, "columnUniqueKey"),

	/***/
	COLUMN_PRIMARY_KEY(CoreNamespace.NS_CORE, "columnPrimaryKey"),

	/***/
	COLUMN_CHECK_CONSTRAINT(CoreNamespace.NS_CORE, "columnCheck"),

	/***/
	EXPRESSION(CoreNamespace.NS_CORE, "expression"),

	/***/
	DEFINITION(CoreNamespace.NS_CORE, "definition"),

	/***/
	MATCH_TYPE(CoreNamespace.NS_CORE, "matchType"),

	/***/
	ON_DELETE(CoreNamespace.NS_CORE, "onDelete"),

	/***/
	ON_UPDATE(CoreNamespace.NS_CORE, "onUpdate"),

	/***/
	DEFERRABILITY(CoreNamespace.NS_CORE, "deferrability"),

	/***/
	DEFERRABLE(CoreNamespace.NS_CORE, "deferrable"),

	/***/
	INITIALLY_CHECK_TIME(CoreNamespace.NS_CORE, "initiallyCheckTime"),

	// --------
	
	/***/
	DATASETS(CoreNamespace.NS_CORE, "dataSets"),

	/***/
	DATASET(CoreNamespace.NS_CORE, "dataSet"),

	/***/
	RECORD(CoreNamespace.NS_CORE, "record"),

	// --------
	
	/***/
	COLUMN_REFS(CoreNamespace.NS_CORE, "columnRefs"),

	/***/
	COLUMN_REF(CoreNamespace.NS_CORE, "columnRef"),

	/***/
	TABLE_REF(CoreNamespace.NS_CORE, "tableRef"),

	/***/
	ENTITY_REF(CoreNamespace.NS_CORE, "entityRef"),

	// -------- Attributes
	
	/***/
	VERSION(CoreNamespace.NONE, "version"),

	/***/
	ID(CoreNamespace.NONE, "id"),

	/***/
	REF(CoreNamespace.NONE, "ref"),

	/***/
	CLASS(CoreNamespace.NONE, "class"),

	/***/
	ENGINE(CoreNamespace.NONE, "engine");
	
	/** XML仕様における完全修飾名 */
	private final QName qName;
	

	CoreQName(JiemamyNamespace namespace, String localPart) {
		qName = new QName(namespace.getNamespaceURI().toString(), localPart, namespace.getPrefix());
	}
	
	public QName getQName() {
		return qName;
	}
	
	public String getQNameString() {
		String prefix = isEmpty(qName.getPrefix()) ? "" : qName.getPrefix() + ":";
		return prefix + qName.getLocalPart();
	}
	
	private boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}
}
