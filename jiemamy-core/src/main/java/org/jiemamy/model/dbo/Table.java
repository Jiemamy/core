package org.jiemamy.model.dbo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jiemamy.model.AbstractEntityFactory;
import org.jiemamy.model.attribute.AttributeModel;
import org.jiemamy.model.attribute.ColumnModel;

/**
 * {@link DefaultTableModel}のファクトリクラス。
 * 
 * @version $Id$
 * @author daisuke
 */
public class Table extends AbstractEntityFactory<DefaultTableModel> {
	
	List<ColumnModel> columns = new ArrayList<ColumnModel>();
	
	List<AttributeModel> attributes = new ArrayList<AttributeModel>();
	
	String name;
	

	/**
	 * インスタンスを生成する。
	 */
	public Table() {
		// noop
	}
	
	/**
	 * インスタンスを生成する。
	 * 
	 * @param name テーブル名
	 */
	public Table(String name) {
		this.name = name;
	}
	
	/**
	 * ファクトリの状態に基づいて {@link DefaultTableModel}のインスタンスを生成する。
	 * 
	 * @param id  ENTITY ID
	 * @return 新しい {@link DefaultTableModel}のインスタンス
	 * @throws IllegalArgumentException 引数{@code id}に{@code null}を与えた場合
	 */
	public DefaultTableModel build(UUID id) {
		DefaultTableModel tableModel = new DefaultTableModel(id);
		tableModel.setName(name);
		for (ColumnModel column : columns) {
			tableModel.addColumn(column);
		}
		for (AttributeModel attribute : attributes) {
			tableModel.addAttribute(attribute);
		}
		return tableModel;
	}
	
	/**
	 * テーブル名を設定する。
	 * 
	 * @param name テーブル名
	 * @return this
	 */
	public Table whoseNameIs(String name) {
		this.name = name;
		return this;
	}
	
	/**
	 * テーブルに作成する属性を追加する。
	 * 
	 * @param attribute 属性
	 * @return this
	 */
	public Table with(AttributeModel attribute) {
		attributes.add(attribute);
		return this;
	}
	
	/**
	 * テーブルに作成するカラムを追加する。
	 * 
	 * @param column カラム
	 * @return this
	 */
	public Table with(ColumnModel column) {
		columns.add(column);
		return this;
	}
}
