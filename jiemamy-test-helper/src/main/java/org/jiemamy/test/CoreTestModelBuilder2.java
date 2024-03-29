/*
 * Copyright 2007-2012 Jiemamy Project and the Others.
 * Created on 2009/01/22
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
package org.jiemamy.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

import org.jiemamy.JiemamyContext;
import org.jiemamy.SimpleJmMetadata;
import org.jiemamy.model.DbObject;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.constraint.JmDeferrability;
import org.jiemamy.model.constraint.JmForeignKeyConstraint;
import org.jiemamy.model.constraint.JmPrimaryKeyConstraint;
import org.jiemamy.model.constraint.ReferentialAction;
import org.jiemamy.model.dataset.JmRecord;
import org.jiemamy.model.dataset.SimpleJmDataSet;
import org.jiemamy.model.datatype.RawTypeCategory;
import org.jiemamy.model.datatype.SimpleDataType;
import org.jiemamy.model.datatype.TypeParameterKey;
import org.jiemamy.model.domain.JmDomain;
import org.jiemamy.model.table.JmTable;

/**
 * Jiemamyテストモデル1（ORDERテーブル）を組み立てるビルダ。
 * 
 * @author daisuke
 */
public class CoreTestModelBuilder2 extends AbstractTestModelBuilder {
	
	private static final String ID_1 = "1";
	
	private static final String ID_2 = "2";
	
	private static final String ID_3 = "3";
	
	private static final String ID_4 = "4";
	
	// ---- basics
	
	/** UUID生成戦略 */
	public final UuidStrategy uuid;
	
	/** 生成方針 */
	public final Instruction instruction;
	
	/** 生成するモデルのインスタンス空間 */
	public final JiemamyContext jiemamy;
	
	// ---- models
	
	/** 生成したモデル */
	public JmDomain idDomain;
	
	/** 生成したモデル */
	public JmDomain nameDomain;
	
	// item
	
	/** 生成したモデル */
	public JmTable tableItem;
	
	/** 生成したモデル */
	public JmColumn itemId;
	
	/** 生成したモデル */
	public JmColumn itemName;
	
	/** 生成したモデル */
	public JmColumn itemPrice;
	
	/** 生成したモデル */
	public JmPrimaryKeyConstraint itemPk;
	
	// user
	
	/** 生成したモデル */
	public JmTable tableUser;
	
	/** 生成したモデル */
	public JmColumn userId;
	
	/** 生成したモデル */
	public JmColumn userName;
	
	/** 生成したモデル */
	public JmPrimaryKeyConstraint userPk;
	
	// order
	
	/** 生成したモデル */
	public JmTable tableOrder;
	
	/** 生成したモデル */
	public JmColumn orderId;
	
	/** 生成したモデル */
	public JmColumn orderUserId;
	
	/** 生成したモデル */
	public JmColumn orderDate;
	
	/** 生成したモデル */
	public JmPrimaryKeyConstraint orderPk;
	
	// detail
	
	/** 生成したモデル */
	public JmTable tableDetail;
	
	/** 生成したモデル */
	public JmColumn detailId;
	
	/** 生成したモデル */
	public JmColumn detailOrderId;
	
	/** 生成したモデル */
	public JmColumn detailItemId;
	
	/** 生成したモデル */
	public JmColumn detailQuantity;
	
	/** 生成したモデル */
	public JmPrimaryKeyConstraint detailPk;
	
	// fk
	
	/** 生成したモデル */
	public JmForeignKeyConstraint fkDetailItem;
	
	/** 生成したモデル */
	public JmForeignKeyConstraint fkDetailOrder;
	
	/** 生成したモデル */
	public JmForeignKeyConstraint fkOrderUser;
	
	
	/**
	 * インスタンスを生成する。
	 */
	public CoreTestModelBuilder2() {
		this(UuidStrategy.FIXED, new Instruction(), new JiemamyContext());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param instruction 設定オブジェクト
	 */
	public CoreTestModelBuilder2(Instruction instruction) {
		this(UuidStrategy.FIXED, instruction, new JiemamyContext());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param instruction 設定オブジェクト
	 * @param jiemamy コンテキスト
	 */
	public CoreTestModelBuilder2(Instruction instruction, JiemamyContext jiemamy) {
		this(UuidStrategy.FIXED, instruction, jiemamy);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param jiemamy コンテキスト
	 */
	public CoreTestModelBuilder2(JiemamyContext jiemamy) {
		this(UuidStrategy.FIXED, new Instruction(), jiemamy);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param uuid UUID生成ストラテジ
	 */
	public CoreTestModelBuilder2(UuidStrategy uuid) {
		this(uuid, new Instruction(), new JiemamyContext());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param uuid UUID生成ストラテジ
	 * @param instruction 設定オブジェクト
	 */
	public CoreTestModelBuilder2(UuidStrategy uuid, Instruction instruction) {
		this(uuid, instruction, new JiemamyContext());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param uuid UUID生成ストラテジ
	 * @param instruction 設定オブジェクト
	 * @param jiemamy コンテキスト
	 */
	public CoreTestModelBuilder2(UuidStrategy uuid, Instruction instruction, JiemamyContext jiemamy) {
		this.uuid = uuid;
		this.instruction = instruction;
		this.jiemamy = jiemamy;
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param uuid UUID生成ストラテジ
	 * @param jiemamy コンテキスト
	 */
	public CoreTestModelBuilder2(UuidStrategy uuid, JiemamyContext jiemamy) {
		this(UuidStrategy.FIXED, new Instruction(), new JiemamyContext());
	}
	
	public JiemamyContext build(String dialectClassName) {
		SimpleJmMetadata meta = new SimpleJmMetadata();
		meta.setDialectClassName(dialectClassName);
		meta.setDescription("Jiemamyテストモデル2");
		meta.setSchemaName("BAR");
		jiemamy.setMetadata(meta);
		
//		context.getAroundScript().setScript(Position.BEGIN, "BEGIN;");
//		context.getAroundScript().setScript(Position.END, "COMMIT;");
		
		createDomains();
		createDbObjects();
		createDefaultForeignKeyConstraints();
		createDataSets();
		
		return jiemamy;
	}
	
	/**
	 * ビルドしたモデルを持つJiemamyオブジェクトを取得する。
	 * 
	 * @return Jiemamyオブジェクト
	 */
	public JiemamyContext getJiemamy() {
		return jiemamy;
	}
	
	/**
	 * データセットを生成する。
	 */
	protected void createDataSets() {
		if (instruction.supressUseDataSet) {
			return;
		}
		// データセットの生成・追加(1)
		SimpleJmDataSet dataSetEn = new SimpleJmDataSet();
		dataSetEn.setName("データ群en");
		dataSetEn.getRecords().put(tableItem.toReference(), createDataSetEnItem());
		dataSetEn.getRecords().put(tableUser.toReference(), createDataSetEnUser());
		dataSetEn.getRecords().put(tableOrder.toReference(), createDataSetEnOrder());
		dataSetEn.getRecords().put(tableDetail.toReference(), createDataSetEnDetail());
		jiemamy.add(dataSetEn);
		
		// データセットの生成・追加(2)
		SimpleJmDataSet dataSetJa = new SimpleJmDataSet();
		dataSetJa.setName("データ群ja");
		dataSetJa.getRecords().put(tableItem.toReference(), createDataSetJaItem());
		dataSetJa.getRecords().put(tableUser.toReference(), createDataSetJaUser());
		dataSetJa.getRecords().put(tableOrder.toReference(), createDataSetJaOrder());
		dataSetJa.getRecords().put(tableDetail.toReference(), createDataSetJaDetail());
		jiemamy.add(dataSetJa);
	}
	
	/**
	 * {@link DbObject}を生成する。
	 */
	protected void createDbObjects() {
		createTableItem();
		jiemamy.add(tableItem);
		createTableUser();
		jiemamy.add(tableUser);
		createTableOrder();
		jiemamy.add(tableOrder);
		createTableDetail();
		jiemamy.add(tableDetail);
	}
	
	/**
	 * 外部キーを生成する。
	 */
	protected void createDefaultForeignKeyConstraints() {
		if (instruction.supressUseForeignKey) {
			return;
		}
		
		fkDetailItem = new JmForeignKeyConstraint(uuid.get("df781ad0-112a-4db7-a76c-4395b15600b2"));
		fkDetailItem.addReferencing(detailItemId.toReference(), itemId.toReference());
		fkDetailItem.setOnDelete(ReferentialAction.RESTRICT);
		tableDetail.add(fkDetailItem);
		
		fkDetailOrder = new JmForeignKeyConstraint(uuid.get("fca97c96-db8c-44b4-8427-6207601eaa94"));
		fkDetailOrder.addReferencing(detailOrderId.toReference(), orderId.toReference());
		fkDetailOrder.setOnDelete(ReferentialAction.CASCADE);
		fkDetailOrder.setDeferrability(JmDeferrability.DEFERRABLE_DEFERRED);
		tableDetail.add(fkDetailOrder);
		
		fkOrderUser = new JmForeignKeyConstraint(uuid.get("325b5aa9-821e-4791-aac5-2d3eb64f9392"));
		fkOrderUser.addReferencing(orderUserId.toReference(), userId.toReference());
		fkOrderUser.setOnDelete(ReferentialAction.RESTRICT);
		fkOrderUser.setDeferrability(JmDeferrability.DEFERRABLE_IMMEDIATE);
		tableOrder.add(fkOrderUser);
	}
	
	/**
	 * ドメインを生成する。
	 */
	protected void createDomains() {
		if (instruction.supressUseDomain) {
			return;
		}
		
		idDomain = new JmDomain(uuid.get("9a3ba23c-b328-4c70-a32d-3e4be3ee3f08"));
		idDomain.setName("ID");
		idDomain.setDataType(ModelUtil.createDataType(jiemamy, RawTypeCategory.INTEGER));
		idDomain.setNotNull(true);
		idDomain.putParam(TypeParameterKey.SERIAL, true);
		jiemamy.add(idDomain);
		
		nameDomain = new JmDomain(uuid.get("e2ebf8a7-90d8-48d4-9b5d-8d2e2601b193"));
		nameDomain.setName("NAME");
		SimpleDataType dataType = ModelUtil.createDataType(jiemamy, RawTypeCategory.VARCHAR);
		dataType.putParam(TypeParameterKey.SIZE, 32); // CHECKSTYLE IGNORE THIS LINE
		nameDomain.setDataType(dataType);
		nameDomain.setDescription("人名用の型です。");
		jiemamy.add(nameDomain);
	}
	
	private List<JmRecord> createDataSetEnDetail() {
		List<JmRecord> result = Lists.newArrayList();
		
		// FORMAT-OFF
		JmRecord record = ModelUtil.newRecord() // uuid.get("546244f5-afa2-4e7e-bde0-5a811ced77af"));
		.addValue(detailId, ID_1)
		.addValue(detailOrderId, ID_1)
		.addValue(detailItemId, ID_1)
		.addValue(detailQuantity, "1").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("943fda94-a3f2-4c47-b19f-9e8655b47362"));
		.addValue(detailId, ID_2)
		.addValue(detailOrderId, ID_1)
		.addValue(detailItemId, ID_4)
		.addValue(detailQuantity, "3").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("d11db81a-2d53-42b4-9f10-17f98f1f7081"));
		.addValue(detailId, ID_3)
		.addValue(detailOrderId, ID_2)
		.addValue(detailItemId, ID_2)
		.addValue(detailQuantity, "2").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("0aa0dd6f-5656-4994-abfe-05f05aec7275"));
		.addValue(detailId, ID_4)
		.addValue(detailOrderId, ID_2)
		.addValue(detailItemId, ID_3)
		.addValue(detailQuantity, "6").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("4b9be90a-886f-47d8-89f0-da03d7ad419e"));
		.addValue(detailId, "5")
		.addValue(detailOrderId, ID_2)
		.addValue(detailItemId, ID_4)
		.addValue(detailQuantity, "12").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("23665133-7f4e-4f65-af7c-0de1b655f7c9"));
		.addValue(detailId, "6")
		.addValue(detailOrderId, ID_3)
		.addValue(detailItemId, ID_2)
		.addValue(detailQuantity, "1").build();
		result.add(record);
		
		return result;
	}
	
	private List<JmRecord> createDataSetEnItem() {
		List<JmRecord> result = new ArrayList<JmRecord>();
		
		JmRecord record = ModelUtil.newRecord() // uuid.get("7162b83a-1815-4b08-8c11-8e6400c05a9e"));
		.addValue(itemId, ID_1)
		.addValue(itemName, "DIAMOND")
		.addValue(itemPrice, "100000").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("a3d6480d-9088-49d8-a500-872262a635d5"));
		.addValue(itemId, ID_2)
		.addValue(itemName, "EMERALD")
		.addValue(itemPrice, "75000").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("bf4556ff-fb81-472c-945d-14067945e7c2"));
		.addValue(itemId, ID_3)
		.addValue(itemName, "RUBY")
		.addValue(itemPrice, "30000").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("fc30dd3e-495d-4f7c-9237-dd9f84c14eb5"));
		.addValue(itemId, ID_4)
		.addValue(itemName, "SAPPHIRE")
		.addValue(itemPrice, "10000").build();
		result.add(record);
		
		return result;
	}
	
	private List<JmRecord> createDataSetEnOrder() {
		List<JmRecord> result = new ArrayList<JmRecord>();
		
		JmRecord record = ModelUtil.newRecord() // uuid.get("bf813157-8690-4eb3-9168-cd4906f13f27"));
		.addValue(orderId, ID_1)
		.addValue(orderUserId, ID_2)
		.addValue(orderDate, "2009-01-23 00:11:22").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("645b241d-a6ba-4821-8682-448f59949b3e"));
		.addValue(orderId, ID_2)
		.addValue(orderUserId, ID_3)
		.addValue(orderDate, "2009-01-23 00:22:33").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("654ceb1b-b6eb-406d-8eac-aa07c7c41e62"));
		.addValue(orderId, ID_3)
		.addValue(orderUserId, ID_2)
		.addValue(orderDate, "2009-01-24 00:33:44").build();
		result.add(record);
		
		return result;
	}
	
	private List<JmRecord> createDataSetEnUser() {
		List<JmRecord> result = new ArrayList<JmRecord>();
		
		JmRecord record = ModelUtil.newRecord() // uuid.get("c60fb4ba-e3d6-415e-a22c-20fbaf4e04e6"));
		.addValue(userId, ID_1)
		.addValue(userName, "SMITH").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("83d5c923-7c61-4393-8185-ac95042f6476"));
		.addValue(userId, ID_2)
		.addValue(userName, "ALLEN").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("2ddd8857-3fca-4020-bb23-49d0126af6fc"));
		.addValue(userId, ID_3)
		.addValue(userName, "WARD").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("2b9aeb38-97de-439e-b96b-40a1c82d249e"));
		.addValue(userId, ID_4)
		.addValue(userName, "JONES").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("a6242c24a-b415-4a93-867b-16a68c2fe884"));
		.addValue(userId, "5")
		.addValue(userName, "MARTIN").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("3b9d3161-a613-4090-a095-708433b23491"));
		.addValue(userId, "6")
		.addValue(userName, "BLAKE").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("aeaad211-b62e-4a35-b410-d107753469e9"));
		.addValue(userId, "7")
		.addValue(userName, "CLARK").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("8c7c220d-ff7f-48c4-9ce7-1395ed1816fa"));
		.addValue(userId, "8")
		.addValue(userName, "SCOTT").build();
		result.add(record);
		
		return result;
	}
	
	private List<JmRecord> createDataSetJaDetail() {
		List<JmRecord> result = new ArrayList<JmRecord>();
		
		JmRecord record = ModelUtil.newRecord() // uuid.get("5a6604ce-6ed2-4542-b009-2a3264b0946a"));
		.addValue(detailId, ID_1)
		.addValue(detailOrderId, ID_1)
		.addValue(detailItemId, ID_1)
		.addValue(detailQuantity, "1").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("db883c9c-b7d3-44f4-88ba-d9753eeae58a"));
		.addValue(detailId, ID_2)
		.addValue(detailOrderId, ID_1)
		.addValue(detailItemId, ID_4)
		.addValue(detailQuantity, "3").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("04e92727-483c-42ad-b671-c1e4661c1844"));
		.addValue(detailId, ID_3)
		.addValue(detailOrderId, ID_2)
		.addValue(detailItemId, ID_2)
		.addValue(detailQuantity, "2").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("f5147476-8bc6-4656-9d0c-fd883a24a18c"));
		.addValue(detailId, ID_4)
		.addValue(detailOrderId, ID_2)
		.addValue(detailItemId, ID_3)
		.addValue(detailQuantity, "6").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("d849f188-9940-4ecc-98a8-868dac5b3b4e"));
		.addValue(detailId, "5")
		.addValue(detailOrderId, ID_2)
		.addValue(detailItemId, ID_4)
		.addValue(detailQuantity, "12").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("2abef8cc-bfb7-4565-ba4e-afca3bf98811"));
		.addValue(detailId, "6")
		.addValue(detailOrderId, ID_3)
		.addValue(detailItemId, ID_2)
		.addValue(detailQuantity, "1").build();
		result.add(record);
		
		return result;
	}
	
	private List<JmRecord> createDataSetJaItem() {
		List<JmRecord> result = new ArrayList<JmRecord>();
		
		JmRecord record = ModelUtil.newRecord() // uuid.get("5fd803d4-5a05-496b-813e-6399955a79bb"));
		.addValue(itemId, ID_1)
		.addValue(itemName, "ダイヤモンド")
		.addValue(itemPrice, "100000").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("d59e426b-e5ed-433e-8d5a-e748dc75af60"));
		.addValue(itemId, ID_2)
		.addValue(itemName, "エメラルド")
		.addValue(itemPrice, "75000").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("59e0a554-162a-4c48-b13d-1dda7fc936e0"));
		.addValue(itemId, ID_3)
		.addValue(itemName, "ルビー")
		.addValue(itemPrice, "30000").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("1c86858f-1e8b-4fc5-807f-f0548e1811e4"));
		.addValue(itemId, ID_4)
		.addValue(itemName, "サファイヤ")
		.addValue(itemPrice, "10000").build();
		result.add(record);
		
		return result;
	}
	
	private List<JmRecord> createDataSetJaOrder() {
		List<JmRecord> result = new ArrayList<JmRecord>();
		
		JmRecord record = ModelUtil.newRecord() // uuid.get("2bdf3969-c2e5-4e3e-a939-2907ca3a5b6a"));
		.addValue(orderId, ID_1)
		.addValue(orderUserId, ID_2)
		.addValue(orderDate, "2009-01-23 00:11:22").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("cca67c6f-a3b2-4b43-a466-be72ccd66e8f"));
		.addValue(orderId, ID_2)
		.addValue(orderUserId, ID_3)
		.addValue(orderDate, "2009-01-23 00:22:33").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("1f74ab8a-3191-464b-89c3-f4f3dddb8ce8"));
		.addValue(orderId, ID_2)
		.addValue(orderUserId, ID_2)
		.addValue(orderDate, "2009-01-24 00:33:44").build();
		result.add(record);
		
		return result;
	}
	
	private List<JmRecord> createDataSetJaUser() {
		List<JmRecord> result = Lists.newArrayList();
		
		JmRecord record = ModelUtil.newRecord() // uuid.get("25288a67-b4c7-4296-8113-835afeb15c9d"));
		.addValue(userId, ID_1)
		.addValue(userName, "鈴木 茂").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("20a62610-ad04-479f-85c6-a1340e26adb5"));
		.addValue(userId, ID_2)
		.addValue(userName, "内海 透").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("d8fa5455-9791-46fc-9695-da7a5167f7ea"));
		.addValue(userId, ID_3)
		.addValue(userName, "村瀬 武彦").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("3cc66837-d604-4af8-af01-343924e9f058"));
		.addValue(userId, ID_4)
		.addValue(userName, "近藤 美樹").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("2f09b9fe-78fd-4f8a-abf2-f077040f45f2"));
		.addValue(userId, "5")
		.addValue(userName, "榊 美子").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("e3431be3-462c-4419-a2a0-e821597490cc"));
		.addValue(userId, "6")
		.addValue(userName, "三浦 佑").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("c50a3e24-dd00-4969-aae9-69cf9be7f035"));
		.addValue(userId, "7")
		.addValue(userName, "前島 孝幸").build();
		result.add(record);
		
		record = ModelUtil.newRecord() // uuid.get("890bf55f-8bfd-420c-a9df-801da73bc46d"));
		.addValue(userId, "8")
		.addValue(userName, "島崎 由比").build();
		result.add(record);
		// FORMAT-ON
		
		return result;
	}
	
	private void createTableDetail() {
		tableDetail = new JmTable(uuid.get("5705ed1a-f329-4f21-9956-94caf4863fba"));
		tableDetail.setName("T_DETAIL");
		tableDetail.setLogicalName("明細");
		tableDetail.setDescription("明細テーブルです。");
		
		detailId = new JmColumn(uuid.get("d3571020-4e1b-4158-958d-b5460fa6c32c"));
		detailId.setName("ID");
		if (instruction.supressUseDomain) {
			detailId.setDataType(ModelUtil.createDataType(jiemamy, RawTypeCategory.INTEGER));
		} else {
			detailId.setDataType(new SimpleDataType(idDomain.asType(jiemamy)));
		}
		detailId.setLogicalName("ユーザID");
		tableDetail.add(detailId);
		
		detailOrderId = new JmColumn(uuid.get("a28c64c6-b379-41a4-9563-b774f5bce165"));
		detailOrderId.setName("ORDER_ID");
		detailOrderId.setDataType(ModelUtil.createDataType(jiemamy, RawTypeCategory.INTEGER));
		tableDetail.add(detailOrderId);
		
		detailItemId = new JmColumn(uuid.get("b4d50786-3b3e-4557-baa3-b739159f0530"));
		detailItemId.setName("ITEM_ID");
		detailItemId.setDataType(ModelUtil.createDataType(jiemamy, RawTypeCategory.INTEGER));
		tableDetail.add(detailItemId);
		
		detailQuantity = new JmColumn(uuid.get("77bb21f4-e793-4198-a695-42363dac2216"));
		detailQuantity.setName("QUANTITY");
		detailQuantity.setDataType(ModelUtil.createDataType(jiemamy, RawTypeCategory.INTEGER));
		tableDetail.add(detailQuantity);
		
		detailPk = new JmPrimaryKeyConstraint(uuid.get("90243681-19af-4bc0-9e6f-f814fbc58f85"));
		detailPk.addKeyColumn(detailId.toReference());
		tableDetail.add(detailPk);
		
		for (JmColumn column : Arrays.asList(detailOrderId, detailItemId, detailQuantity)) {
			column.setNotNull(true);
		}
	}
	
	private void createTableItem() {
		tableItem = new JmTable(uuid.get("7ecf5ba1-e80c-472c-b19b-bef2649d7974"));
		tableItem.setName("T_ITEM");
		
//		tableItem.getAroundScript().setScript(Position.BEGIN, "/* test begin script of T_ITEM */");
//		tableItem.getAroundScript().setScript(Position.END, "/* test end script of T_ITEM */");
		
		tableItem.setDescription("商品マスタです。");
		
		itemId = new JmColumn(uuid.get("5a9585be-4b0d-4675-99aa-97b0417c816c"));
		itemId.setName("ID");
		if (instruction.supressUseDomain) {
			itemId.setDataType(ModelUtil.createDataType(jiemamy, RawTypeCategory.INTEGER));
		} else {
			itemId.setDataType(new SimpleDataType(idDomain.asType(jiemamy)));
		}
		itemId.setLogicalName("商品ID");
		tableItem.add(itemId);
		
		itemName = new JmColumn(uuid.get("5c9b38e1-2cc9-45f9-ad3f-20b02471cc40"));
		itemName.setName("NAME");
		SimpleDataType itemNameDataType = ModelUtil.createDataType(jiemamy, RawTypeCategory.VARCHAR);
		itemNameDataType.putParam(TypeParameterKey.SIZE, 20);
		itemName.setDataType(itemNameDataType);
		itemName.setLogicalName("商品名");
		tableItem.add(itemName);
		
		itemPrice = new JmColumn(uuid.get("7a0cabe3-d382-4e5d-845b-dadd1b637a5f"));
		itemPrice.setName("PRICE");
		SimpleDataType itemPriceDataType = ModelUtil.createDataType(jiemamy, RawTypeCategory.VARCHAR);
		itemPriceDataType.putParam(TypeParameterKey.SIZE, 20);
		itemPrice.setDataType(itemPriceDataType);
		itemPrice.setLogicalName("価格");
		tableItem.add(itemPrice);
		
		itemPk = new JmPrimaryKeyConstraint(uuid.get("32b2b2f3-e668-404b-9478-56f1e680f915"));
		itemPk.addKeyColumn(itemId.toReference());
		tableItem.add(itemPk);
		
		for (JmColumn column : Arrays.asList(itemName, itemPrice)) {
			column.setNotNull(true);
		}
	}
	
	private void createTableOrder() {
		tableOrder = new JmTable(uuid.get("cefee0d9-d23f-441b-986a-66660354ec74"));
		tableOrder.setName("T_ORDER");
		tableOrder.setLogicalName("注文");
		tableOrder.setDescription("注文テーブルです。");
		
		orderId = new JmColumn(uuid.get("b647212a-4d8f-4d18-88e4-5397e54ebe67"));
		orderId.setName("ID");
		if (instruction.supressUseDomain) {
			orderId.setDataType(ModelUtil.createDataType(jiemamy, RawTypeCategory.INTEGER));
		} else {
			orderId.setDataType(new SimpleDataType(idDomain.asType(jiemamy)));
		}
		orderId.setLogicalName("注文ID");
		tableOrder.add(orderId);
		
		orderUserId = new JmColumn(uuid.get("db7a2f62-9658-406a-9dc1-8b90ae2da47c"));
		orderUserId.setName("USER_ID");
		orderUserId.setDataType(ModelUtil.createDataType(jiemamy, RawTypeCategory.INTEGER));
		orderUserId.setLogicalName("オーダーユーザID");
		tableOrder.add(orderUserId);
		
		orderDate = new JmColumn(uuid.get("4ce761b0-137b-4105-ad2a-2efcba5e6bc4"));
		orderDate.setName("ORDER_DATE");
		orderDate.setDataType(ModelUtil.createDataType(jiemamy, RawTypeCategory.TIMESTAMP));
		tableOrder.add(orderDate);
		
		orderPk = new JmPrimaryKeyConstraint(uuid.get("b204ff42-537b-4e14-bf61-e9baf1b119dc"));
		orderPk.addKeyColumn(orderId.toReference());
		tableOrder.add(orderPk);
		
		for (JmColumn column : Arrays.asList(orderUserId, orderDate)) {
			column.setNotNull(true);
		}
	}
	
	private void createTableUser() {
		tableUser = new JmTable(uuid.get("0802ef7b-b2da-4fc4-8104-0c99a1e472d5"));
		tableUser.setName("T_USER");
		tableUser.setLogicalName("ユーザ");
		tableUser.setDescription("ユーザマスタです。");
		
		userId = new JmColumn(uuid.get("6b022a79-45a6-4be4-9d3d-cfb27042a08e"));
		userId.setName("ID");
		if (instruction.supressUseDomain) {
			userId.setDataType(ModelUtil.createDataType(jiemamy, RawTypeCategory.INTEGER));
		} else {
			userId.setDataType(new SimpleDataType(idDomain.asType(jiemamy)));
		}
		userId.setLogicalName("ユーザID");
		tableUser.add(userId);
		
		userName = new JmColumn(uuid.get("dacc68d2-fe32-4f4b-8082-9d55232ba7da"));
		userName.setName("NAME");
		if (instruction.supressUseDomain) {
			SimpleDataType dataType = ModelUtil.createDataType(jiemamy, RawTypeCategory.VARCHAR);
			dataType.putParam(TypeParameterKey.SIZE, 32); // CHECKSTYLE IGNORE THIS LINE
			userName.setDataType(dataType);
		} else {
			userName.setDataType(new SimpleDataType(nameDomain.asType(jiemamy)));
		}
		userName.setLogicalName("ユーザ名");
		userName.setDefaultValue("'no name'");
		tableUser.add(userName);
		
		userPk = new JmPrimaryKeyConstraint(uuid.get("dac295e3-c390-46ee-9a5b-e89636ab9d7e"));
		userPk.addKeyColumn(userId.toReference());
		tableUser.add(userPk);
		
		userName.setNotNull(true);
	}
}
