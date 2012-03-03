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
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.jiemamy.JiemamyContext;
import org.jiemamy.SimpleJmMetadata;
import org.jiemamy.model.DbObject;
import org.jiemamy.model.column.JmColumn;
import org.jiemamy.model.constraint.JmCheckConstraint;
import org.jiemamy.model.constraint.JmDeferrability;
import org.jiemamy.model.constraint.JmForeignKeyConstraint;
import org.jiemamy.model.constraint.JmPrimaryKeyConstraint;
import org.jiemamy.model.constraint.ReferentialAction;
import org.jiemamy.model.dataset.JmRecord;
import org.jiemamy.model.dataset.SimpleJmDataSet;
import org.jiemamy.model.dataset.SimpleJmRecord;
import org.jiemamy.model.datatype.RawTypeCategory;
import org.jiemamy.model.datatype.SimpleDataType;
import org.jiemamy.model.datatype.TypeParameterKey;
import org.jiemamy.model.domain.JmDomain;
import org.jiemamy.model.index.JmIndex;
import org.jiemamy.model.index.JmIndexColumn;
import org.jiemamy.model.index.SortOrder;
import org.jiemamy.model.table.JmTable;
import org.jiemamy.model.view.JmView;

/**
 * Jiemamyテストモデル1（EMP-DEPTテーブル）を組み立てるビルダ。
 * 
 * @author daisuke
 */
public class CoreTestModelBuilder extends AbstractTestModelBuilder {
	
	// ---- basics
	
	/** UUID生成戦略 */
	public final UuidStrategy uuid;
	
	/** 生成方針 */
	public final Instruction instruction;
	
	/** 生成するモデルのインスタンス空間 */
	public final JiemamyContext context;
	
	// ---- models
	
	/** 生成したモデル */
	public JmDomain domainId;
	
	// dept
	
	/** 生成したモデル */
	public JmDomain domainName;
	
	/** 生成したモデル */
	public JmTable tableDept;
	
	/** 生成したモデル */
	public JmColumn deptId;
	
	/** 生成したモデル */
	public JmColumn deptDeptNo;
	
	/** 生成したモデル */
	public JmColumn deptDeptName;
	
	/** 生成したモデル */
	public JmColumn deptLoc;
	
	// emp
	
	/** 生成したモデル */
	public JmPrimaryKeyConstraint deptPk;
	
	/** 生成したモデル */
	public JmTable tableEmp;
	
	/** 生成したモデル */
	public JmColumn empId;
	
	/** 生成したモデル */
	public JmColumn empEmpNo;
	
	/** 生成したモデル */
	
	public JmColumn empEmpName;
	
	/** 生成したモデル */
	public JmColumn empMgrId;
	
	/** 生成したモデル */
	public JmColumn empHiredate;
	
	/** 生成したモデル */
	public JmColumn empSal;
	
	/** 生成したモデル */
	public JmColumn empDeptId;
	
	// highSal
	
	/** 生成したモデル */
	public JmPrimaryKeyConstraint empPk;
	
	// fk
	
	/** 生成したモデル */
	public JmView viewHighSal;
	
	/** 生成したモデル */
	public JmForeignKeyConstraint fkEmpEmp;
	
	/** 生成したモデル */
	public JmForeignKeyConstraint fkEmpDept;
	
	private JmIndex empNameIndex;
	
	
	/**
	 * インスタンスを生成する。
	 */
	public CoreTestModelBuilder() {
		this(UuidStrategy.FIXED, new Instruction(), new JiemamyContext());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param instruction 設定オブジェクト
	 */
	public CoreTestModelBuilder(Instruction instruction) {
		this(UuidStrategy.FIXED, instruction, new JiemamyContext());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param instruction 設定オブジェクト
	 * @param jiemamy コンテキスト
	 */
	public CoreTestModelBuilder(Instruction instruction, JiemamyContext jiemamy) {
		this(UuidStrategy.FIXED, instruction, jiemamy);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param jiemamy コンテキスト
	 */
	public CoreTestModelBuilder(JiemamyContext jiemamy) {
		this(UuidStrategy.FIXED, new Instruction(), jiemamy);
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param uuid UUID生成ストラテジ
	 */
	public CoreTestModelBuilder(UuidStrategy uuid) {
		this(uuid, new Instruction(), new JiemamyContext());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param uuid UUID生成ストラテジ
	 * @param instruction 設定オブジェクト
	 */
	public CoreTestModelBuilder(UuidStrategy uuid, Instruction instruction) {
		this(uuid, instruction, new JiemamyContext());
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param uuid UUID生成ストラテジ
	 * @param instruction 設定オブジェクト
	 * @param context コンテキスト
	 */
	public CoreTestModelBuilder(UuidStrategy uuid, Instruction instruction, JiemamyContext context) {
		this.uuid = uuid;
		this.instruction = instruction;
		this.context = context;
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param uuid UUID生成ストラテジ
	 * @param jiemamy コンテキスト
	 */
	public CoreTestModelBuilder(UuidStrategy uuid, JiemamyContext jiemamy) {
		this(UuidStrategy.FIXED, new Instruction(), new JiemamyContext());
	}
	
	public JiemamyContext build(String dialectClassName) {
		SimpleJmMetadata meta = new SimpleJmMetadata();
		meta.setDialectClassName(dialectClassName);
		meta.setDescription("Jiemamyテストモデル1");
		meta.setSchemaName("FOO");
		context.setMetadata(meta);
		
		createDomains();
		createDbObjects();
		createForeignKeys();
		createDataSets();
		
		return context;
	}
	
	/**
	 * ビルドしたモデルを持つJiemamyオブジェクトを取得する。
	 * 
	 * @return Jiemamyオブジェクト
	 */
	public JiemamyContext getJiemamyContext() {
		return context;
	}
	
	/**
	 * データセットを生成する。
	 */
	protected void createDataSets() {
		if (instruction.supressUseDataSet) {
			return;
		}
		// データセットの生成・追加(1)
		
		SimpleJmDataSet dataSetEn = new SimpleJmDataSet(uuid.get("b73100b5-2d70-4b48-a825-311eacb63b2f"));
		dataSetEn.setName("データ群en");
		dataSetEn.putRecord(tableDept.toReference(), createDataSetEnDept());
		dataSetEn.putRecord(tableEmp.toReference(), createDataSetEnEmp());
		context.store(dataSetEn);
		
		// データセットの生成・追加(2)
		SimpleJmDataSet dataSetJa = new SimpleJmDataSet(uuid.get("91246ed4-1ef3-440e-bf12-40fa4439a71b"));
		dataSetJa.setName("データ群ja");
		dataSetJa.putRecord(tableDept.toReference(), createDataSetJaDept());
		dataSetJa.putRecord(tableEmp.toReference(), createDataSetJaEmp());
		context.store(dataSetJa);
	}
	
	/**
	 * {@link DbObject}を生成する。
	 */
	protected void createDbObjects() {
		createTableDept();
		context.store(tableDept);
		createTableEmp();
		context.store(tableEmp);
		context.store(empNameIndex);
		createViewHighSal();
		context.store(viewHighSal);
	}
	
	/**
	 * ドメインを生成する。
	 */
	protected void createDomains() {
		if (instruction.supressUseDomain) {
			return;
		}
		
		domainId = new JmDomain(uuid.get("2eec0aa0-5122-4eb7-833d-9f5a43e7abe9"));
		domainId.setName("ID");
		domainId.setDataType(ModelUtil.createDataType(context, RawTypeCategory.INTEGER));
		domainId.setNotNull(true);
		JmCheckConstraint check = new JmCheckConstraint(uuid.get("48b76d76-b288-480a-afa4-111247379f8d"));
		check.setName("hoge");
		check.setExpression("VALUE > 0");
		domainId.addCheckConstraint(check);
		domainId.putParam(TypeParameterKey.SERIAL, true);
		context.store(domainId);
		
		domainName = new JmDomain(uuid.get("62f1e6ec-e6aa-4d52-a6c3-27dac086f2d7"));
		domainName.setName("NAME");
		SimpleDataType dataType = ModelUtil.createDataType(context, RawTypeCategory.VARCHAR);
		dataType.putParam(TypeParameterKey.SIZE, 32); // CHECKSTYLE IGNORE THIS LINE
		domainName.setDataType(dataType);
		domainName.setDescription("人名用の型です。");
		context.store(domainName);
	}
	
	/**
	 * 外部キーを生成する。
	 */
	protected void createForeignKeys() {
		if (instruction.supressUseForeignKey) {
			return;
		}
		
		fkEmpEmp = new JmForeignKeyConstraint(uuid.get("e43d3c43-33c8-4b02-aa42-83f2d868cfe6"));
		fkEmpEmp.setName("emp_mgr_id_fkey");
		fkEmpEmp.addReferencing(empMgrId.toReference(), empId.toReference());
		fkEmpEmp.setOnDelete(ReferentialAction.SET_NULL);
		JmDeferrability deferrability = JmDeferrability.DEFERRABLE_DEFERRED;
		fkEmpEmp.setDeferrability(deferrability);
		tableEmp.store(fkEmpEmp);
		
		fkEmpDept = new JmForeignKeyConstraint(uuid.get("e7dd92b4-1d97-4be6-bab6-fa9fe26eb6ed"));
		fkEmpDept.setName("emp_dept_id_fkey");
		fkEmpDept.addReferencing(empDeptId.toReference(), deptId.toReference());
		tableEmp.store(fkEmpDept);
	}
	
	private List<JmRecord> createDataSetEnDept() {
		List<JmRecord> result = Lists.newArrayList();
		
		// CHECKSTYLE:OFF
		// FORMAT-OFF
		SimpleJmRecord record = ModelUtil.newRecord()
				.addValue(deptId, "1")
				.addValue(deptDeptNo, "10")
				.addValue(deptDeptName, "ACCOUNTING")
				.addValue(deptLoc, "NEW YORK").build();
		result.add(record);
		
		record = ModelUtil.newRecord()
				.addValue(deptId, "2")
				.addValue(deptDeptNo, "20")
				.addValue(deptDeptName, "RESEARCH")
				.addValue(deptLoc, "DALLAS").build();
		result.add(record);
		
		record = ModelUtil.newRecord()
				.addValue(deptId, "3")
				.addValue(deptDeptNo, "30")
				.addValue(deptDeptName, "SALES")
				.addValue(deptLoc, "CHICAGO").build();
		result.add(record);
		
		record = ModelUtil.newRecord()
				.addValue(deptId, "4")
				.addValue(deptDeptNo, "40")
				.addValue(deptDeptName, "OPERATIONS")
				.addValue(deptLoc, "BOSTON").build();
		result.add(record);
		// CHECKSTYLE:ON
		
		return result;
	}
	
	private List<JmRecord> createDataSetEnEmp() {
		List<JmRecord> result = Lists.newArrayList();
		
		// CHECKSTYLE:OFF
		JmRecord record = ModelUtil.newRecord()
				.addValue(empId, "1")
				.addValue(empEmpNo, "10")
				.addValue(empEmpName, "SMITH")
				.addValue(empMgrId, "3")
				.addValue(empHiredate, "2003-02-01")
				.addValue(empSal, "40")
				.addValue(empDeptId, "3").build();
		result.add(record);
		
		record = ModelUtil.newRecord()
				.addValue(empId, "2")
				.addValue(empEmpNo, "20")
				.addValue(empEmpName, "ALLEN")
				.addValue(empMgrId, "3")
				.addValue(empHiredate, "2000-03-04")
				.addValue(empSal, "50")
				.addValue(empDeptId, "4").build();
		result.add(record);
		
		record = ModelUtil.newRecord()
				.addValue(empId, "3")
				.addValue(empEmpNo, "30")
				.addValue(empEmpName, "WARD")
//				.addValue(empMgrId, null)
				.addValue(empHiredate, "1993-12-05")
				.addValue(empSal, "60")
				.addValue(empDeptId, "4").build();
		result.add(record);
		
		record = ModelUtil.newRecord()
				.addValue(empId, "4")
				.addValue(empEmpNo, "40")
				.addValue(empEmpName, "JONES")
				.addValue(empMgrId, "2")
				.addValue(empHiredate, "2007-04-01")
				.addValue(empSal, "36")
				.addValue(empDeptId, "2").build();
		result.add(record);
		
		record = ModelUtil.newRecord()
				.addValue(empId, "5")
				.addValue(empEmpNo, "50")
				.addValue(empEmpName, "MARTIN")
				.addValue(empMgrId, "1")
				.addValue(empHiredate, "2002-05-30")
				.addValue(empSal, "30")
				.addValue(empDeptId, "3").build();
		result.add(record);
		
		record = ModelUtil.newRecord()
				.addValue(empId, "6")
				.addValue(empEmpNo, "60")
				.addValue(empEmpName, "BLAKE")
				.addValue(empMgrId, "3")
				.addValue(empHiredate, "2007-04-01")
				.addValue(empSal, "25")
				.addValue(empDeptId, "2").build();
		result.add(record);
		
		record = ModelUtil.newRecord()
				.addValue(empId, "7")
				.addValue(empEmpNo, "70")
				.addValue(empEmpName, "CLARK")
				.addValue(empMgrId, "1")
				.addValue(empHiredate, "2004-09-01")
				.addValue(empSal, "30")
				.addValue(empDeptId, "1").build();
		result.add(record);
		
		record = ModelUtil.newRecord()
				.addValue(empId, "8")
				.addValue(empEmpNo, "80")
				.addValue(empEmpName, "SCOTT")
				.addValue(empMgrId, "4")
				.addValue(empHiredate, "2008-03-01")
				.addValue(empSal, "25")
				.addValue(empDeptId, "2").build();
		result.add(record);
		// CHECKSTYLE:ON
		
		return result;
	}
	
	private List<JmRecord> createDataSetJaDept() {
		List<JmRecord> result = new ArrayList<JmRecord>();
		
		// CHECKSTYLE:OFF
		JmRecord record = ModelUtil.newRecord()
				.addValue(deptId, "1")
				.addValue(deptDeptNo, "10")
				.addValue(deptDeptName, "経理部")
				.addValue(deptLoc, "広島").build();
		result.add(record);
		
		record = ModelUtil.newRecord()
				.addValue(deptId, "2")
				.addValue(deptDeptNo, "20")
				.addValue(deptDeptName, "研究開発部")
				.addValue(deptLoc, "京都").build();
		result.add(record);
		
		record = ModelUtil.newRecord()
				.addValue(deptId, "3")
				.addValue(deptDeptNo, "30")
				.addValue(deptDeptName, "営業部")
				.addValue(deptLoc, "東京").build();
		result.add(record);
		
		record = ModelUtil.newRecord()
				.addValue(deptId, "4")
				.addValue(deptDeptNo, "40")
				.addValue(deptDeptName, "経営本部").build();
				// locは指定せず、特定しない状態
		result.add(record);
		// CHECKSTYLE:ON
		
		return result;
	}
	
	private List<JmRecord> createDataSetJaEmp() {
		List<JmRecord> result = Lists.newArrayList();
		
		// CHECKSTYLE:OFF
		JmRecord record = ModelUtil.newRecord()
				.addValue(empId, "1")
				.addValue(empEmpNo, "10")
				.addValue(empEmpName, "鈴木 茂")
				.addValue(empMgrId, "3")
				.addValue(empHiredate, "2003-02-01")
				.addValue(empSal, "40")
				.addValue(empDeptId, "3").build();
		result.add(record);
		
		record = ModelUtil.newRecord()
				.addValue(empId, "2")
				.addValue(empEmpNo, "20")
				.addValue(empEmpName, "内海 透")
				.addValue(empMgrId, "3")
				.addValue(empHiredate, "2000-03-04")
				.addValue(empSal, "50")
				.addValue(empDeptId, "4").build();
		result.add(record);
		
		record = ModelUtil.newRecord()
				.addValue(empId, "3")
				.addValue(empEmpNo, "30")
				.addValue(empEmpName, "村瀬 武彦")
//				.addValue(empMgrId, null)
				.addValue(empHiredate, "1993-12-05")
				.addValue(empSal, "60")
				.addValue(empDeptId, "4").build();
		result.add(record);
		
		record = ModelUtil.newRecord()
				.addValue(empId, "4")
				.addValue(empEmpNo, "40")
				.addValue(empEmpName, "近藤 美樹")
				.addValue(empMgrId, "2")
				.addValue(empHiredate, "2007-04-01")
				.addValue(empSal, "36")
				.addValue(empDeptId, "2").build();
		result.add(record);
		
		record = ModelUtil.newRecord()
				.addValue(empId, "5")
				.addValue(empEmpNo, "50")
				.addValue(empEmpName, "榊 美子")
				.addValue(empMgrId, "1")
				.addValue(empHiredate, "2002-05-30")
				.addValue(empSal, "30")
				.addValue(empDeptId, "3").build();
		result.add(record);
		
		record = ModelUtil.newRecord()
				.addValue(empId, "6")
				.addValue(empEmpNo, "60")
				.addValue(empEmpName, "三浦 佑")
				.addValue(empMgrId, "3")
				.addValue(empHiredate, "2007-04-01")
				.addValue(empSal, "25")
				.addValue(empDeptId, "2").build();
		result.add(record);
		
		record = ModelUtil.newRecord()
				.addValue(empId, "7")
				.addValue(empEmpNo, "70")
				.addValue(empEmpName, "前島 孝幸")
				.addValue(empMgrId, "1")
				.addValue(empHiredate, "2004-09-01")
				.addValue(empSal, "30")
				.addValue(empDeptId, "1").build();
		result.add(record);
		
		record = ModelUtil.newRecord()
				.addValue(empId, "8")
				.addValue(empEmpNo, "80")
				.addValue(empEmpName, "島崎 由比")
				.addValue(empMgrId, "4")
				.addValue(empHiredate, "2008-03-01")
				.addValue(empSal, "25")
				.addValue(empDeptId, "2").build();
		result.add(record);
		// CHECKSTYLE:ON
		// FORMAT-ON
		
		return result;
	}
	
	private void createTableDept() {
		tableDept = new JmTable(uuid.get("d7489ed6-0add-443d-95cf-234376eb0455"));
		tableDept.setName("T_DEPT");
		
//		tableDept.getAroundScript().setScript(Position.BEGIN, "/* test begin script */");
		
		tableDept.setDescription("部署マスタです。");
		
		deptId = new JmColumn(uuid.get("c7ed225d-92a6-4cc2-90de-60531804464e"));
		deptId.setName("ID");
		if (instruction.supressUseDomain) {
			deptId.setDataType(ModelUtil.createDataType(context, RawTypeCategory.INTEGER));
		} else {
			deptId.setDataType(new SimpleDataType(domainId.asType(context)));
		}
		deptId.setLogicalName("部署ID");
		tableDept.store(deptId);
		
		deptPk = new JmPrimaryKeyConstraint(uuid.get("8de55e65-ec48-467a-bac5-8eee2d71d41c"));
		deptPk.setName("dept_pkey");
		deptPk.addKeyColumn(deptId.toReference());
		tableDept.store(deptPk);
		
		deptDeptNo = new JmColumn(uuid.get("2d951389-6bc7-49d7-8631-1d26fe17047e"));
		deptDeptNo.setName("DEPT_NO");
		deptDeptNo.setDataType(ModelUtil.createDataType(context, RawTypeCategory.INTEGER));
		deptDeptNo.setLogicalName("部署番号");
		tableDept.store(deptDeptNo);
		
		deptDeptName = new JmColumn(uuid.get("1fcd63d3-974e-4d2e-a0d8-3b9c233104d9"));
		deptDeptName.setName("DEPT_NAME");
		SimpleDataType deptDeptNameDataType = ModelUtil.createDataType(context, RawTypeCategory.VARCHAR);
		deptDeptNameDataType.putParam(TypeParameterKey.SIZE, 20);
		deptDeptName.setDataType(deptDeptNameDataType);
		deptDeptName.setLogicalName("部署名");
		tableDept.store(deptDeptName);
		
		deptLoc = new JmColumn(uuid.get("7bf79e76-07b8-43b6-a993-b8ef374a31f5"));
		deptLoc.setName("LOC");
		SimpleDataType deptLocDataType = ModelUtil.createDataType(context, RawTypeCategory.VARCHAR);
		deptLocDataType.putParam(TypeParameterKey.SIZE, 20);
		deptLoc.setDataType(deptLocDataType);
		deptLoc.setLogicalName("ロケーション");
		deptLoc.setDefaultValue("'secret'");
		tableDept.store(deptLoc);
		
		Map<UUID, UUID> columnNotNullMap = Maps.newHashMap();
		columnNotNullMap.put(deptDeptNo.getId(), uuid.get("cc709f63-a886-4207-a316-58ad7f279e10"));
		columnNotNullMap.put(deptDeptName.getId(), uuid.get("fab2f883-0489-4661-bd57-f04286188eef"));
		
		for (JmColumn column : Arrays.asList(deptDeptNo, deptDeptName)) {
			column.setNotNull(true);
			tableDept.store(column);
		}
	}
	
	private void createTableEmp() { // CHECKSTYLE IGNORE THIS LINE
		tableEmp = new JmTable(uuid.get("9f522e56-809c-45fd-8416-39201014218b"));
		tableEmp.setName("T_EMP");
		tableEmp.setLogicalName("従業員");
		
//		tableEmp.getAroundScript().setScript(Position.BEGIN, "/* test end script */");
		
		tableEmp.setDescription("従業員マスタです。");
		
		empId = new JmColumn(uuid.get("44c8e93d-b7ad-46cc-9b29-88c3a7d6c33e"));
		empId.setName("ID");
		if (instruction.supressUseDomain) {
			empId.setDataType(ModelUtil.createDataType(context, RawTypeCategory.INTEGER));
		} else {
			empId.setDataType(new SimpleDataType(domainId.asType(context)));
		}
		empId.setLogicalName("従業員ID");
		tableEmp.store(empId);
		
		empEmpNo = new JmColumn(uuid.get("248a429b-2159-4ebd-a791-eee42a059374"));
		empEmpNo.setName("EMP_NO");
		empEmpNo.setDataType(ModelUtil.createDataType(context, RawTypeCategory.INTEGER));
		empEmpNo.setLogicalName("従業員番号");
		empEmpNo.setNotNull(true);
		tableEmp.store(empEmpNo);
		
		empEmpName = new JmColumn(uuid.get("0e51b6df-43ab-408c-90ef-de13c6aab881"));
		empEmpName.setName("EMP_NAME");
		if (instruction.supressUseDomain) {
			SimpleDataType dataType = ModelUtil.createDataType(context, RawTypeCategory.VARCHAR);
			dataType.putParam(TypeParameterKey.SIZE, 32); // CHECKSTYLE IGNORE THIS LINE
			empEmpName.setDataType(dataType);
		} else {
			empEmpName.setDataType(new SimpleDataType(domainName.asType(context)));
		}
		empEmpName.setLogicalName("従業員名");
		empEmpName.setDefaultValue("'no name'");
		tableEmp.store(empEmpName);
		
		empMgrId = new JmColumn(uuid.get("3d21a85a-72de-41b3-99dd-f4cb94e58d84"));
		empMgrId.setName("MGR_ID");
		empMgrId.setDataType(ModelUtil.createDataType(context, RawTypeCategory.INTEGER));
		empMgrId.setLogicalName("上司ID");
		tableEmp.store(empMgrId);
		
		empHiredate = new JmColumn(uuid.get("f0b57eed-98ab-4c21-9855-218c592814dc"));
		empHiredate.setName("HIREDATE");
		empHiredate.setDataType(ModelUtil.createDataType(context, RawTypeCategory.DATE));
		tableEmp.store(empHiredate);
		
		empSal = new JmColumn(uuid.get("80786549-dc2c-4c1c-bcbd-9f6fdec911d2"));
		empSal.setName("SAL");
		SimpleDataType dataType = ModelUtil.createDataType(context, RawTypeCategory.NUMERIC);
		dataType.putParam(TypeParameterKey.PRECISION, 7);
		dataType.putParam(TypeParameterKey.SCALE, 2);
		empSal.setDataType(dataType);
		tableEmp.store(empSal);
		
		JmCheckConstraint checkConstraint = new JmCheckConstraint(uuid.get("873f6660-7a61-4c2c-87a0-e922fa03b88c"));
		checkConstraint.setName("positive_sal");
		checkConstraint.setExpression("SAL >= 0");
		tableEmp.store(checkConstraint);
		
		empDeptId = new JmColumn(uuid.get("4ae69b7a-7a0e-422a-89dc-0f0cff77565b"));
		empDeptId.setName("DEPT_ID");
		empDeptId.setDataType(ModelUtil.createDataType(context, RawTypeCategory.INTEGER));
		tableEmp.store(empDeptId);
		
		empPk = new JmPrimaryKeyConstraint(uuid.get("6145e6a0-9ff7-4033-999d-99d80392a48f"));
		empPk.setName("emp_pkey");
		empPk.addKeyColumn(empId.toReference());
		tableEmp.store(empPk);
		
		Map<UUID, UUID> columnNotNullMap = Maps.newHashMap();
		columnNotNullMap.put(empEmpName.getId(), uuid.get("41f178b9-2cb5-4dad-a6c0-48df2d5b1300"));
		columnNotNullMap.put(empHiredate.getId(), uuid.get("2d66fd73-8d6b-41d8-b6b8-daec7d6c0c53"));
		columnNotNullMap.put(empSal.getId(), uuid.get("a446779a-4fb6-4a0f-8262-22daae856e85"));
		columnNotNullMap.put(empDeptId.getId(), uuid.get("b9a0fdce-a965-4118-ae71-5dc7150f6d4e"));
		
		for (JmColumn column : Arrays.asList(empEmpName, empHiredate, empSal, empDeptId)) {
			column.setNotNull(true);
		}
		
		empNameIndex = new JmIndex(uuid.get("9abc9e01-4cdb-42fe-a495-93b56af35a1d"));
		empNameIndex.setName("IDX_EMP_NAME");
		JmIndexColumn indexColumn = JmIndexColumn.of(empEmpName, SortOrder.DESC);
		empNameIndex.addIndexColumn(indexColumn);
	}
	
	private void createViewHighSal() {
		viewHighSal = new JmView(uuid.get("516f7961-cb7b-48e2-990b-7fb0c750c3a4"));
		viewHighSal.setName("V_HIGH_SAL_EMP");
		viewHighSal.setDefinition("SELECT * FROM T_EMP WHERE SAL > 2000");
		viewHighSal.setLogicalName("高給取り");
	}
}
