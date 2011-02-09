/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2008/07/12
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
package org.jiemamy.composer.exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import org.jiemamy.JiemamyContext;
import org.jiemamy.composer.AbstractExporter;
import org.jiemamy.composer.ExportException;
import org.jiemamy.dialect.Dialect;
import org.jiemamy.dialect.SqlEmitter;
import org.jiemamy.model.ModelConsistencyException;
import org.jiemamy.model.sql.SqlStatement;

/**
 * モデルからSQLを構築し、ファイルに書き出すエクスポータ。
 * 
 * <p>モデルをSQL化するだけではなく、その結果をファイルに書き出すところまでの責務を負う。</p>
 * 
 * @author daisuke
 */
public class SqlExporter extends AbstractExporter<SqlExportConfig> {
	
	/** ConfigKey: オーバーライトするかどうか (Boolean) */
	public static final String OVERWRITE = "overwrite";
	
	/** ConfigKey: 出力ファイル名 (String) */
	public static final String OUTPUT_FILE = "outputFile";
	
	/** ConfigKey: 出力データセット番号 (Integer) */
	public static final String DATA_SET_INDEX = "dataSetIndex";
	
	/** COnfigKey: DROP文を出力するかどうか (Boolean) */
	public static final String DROP = "emitDropStatements";
	
	/** COnfigKey: DROP文を出力するかどうか (Boolean) */
	public static final String SCHEMA = "emitCreateSchemaStatement";
	

	/**
	 * {@inheritDoc}
	 * 
	 * <p>この実装では、{@link SqlExportConfig#getOutputFile()}で示されるファイルの親ディレクトリが存在しなかった場合、
	 * そのディレクトリを作成しようと試みる。ディレクトリの作成に失敗した場合は、 {@link ExportException}をスローする。</p>
	 */
	public boolean exportModel(JiemamyContext context, SqlExportConfig config) throws ExportException {
		Validate.notNull(context);
		Validate.notNull(config);
		Validate.notNull(config.getOutputFile());
		
		Writer writer = null;
		try {
			Dialect dialect = context.findDialect();
			SqlEmitter emitter = dialect.getSqlEmitter();
			List<SqlStatement> statements = emitter.emit(context, config);
			
			File outputFile = config.getOutputFile();
			if (outputFile.exists()) {
				if (config.isOverwrite() == false) {
					return false;
				}
				if (outputFile.delete() == false) {
					throw new ExportException("Cannot delete file: " + outputFile.getAbsolutePath());
				}
			}
			
			File parentDir = outputFile.getParentFile();
			if (parentDir != null && parentDir.exists() == false) {
				boolean mkdirResult = parentDir.mkdir();
				if (mkdirResult == false) {
					throw new ExportException("Cannot create directory: " + parentDir.getAbsolutePath());
				}
			}
			
			writer = new OutputStreamWriter(new FileOutputStream(outputFile), CharEncoding.UTF_8);
			for (SqlStatement stmt : statements) {
				writer.write(stmt.toString());
				writer.write(SystemUtils.LINE_SEPARATOR);
			}
		} catch (IOException e) {
			throw new ExportException(e);
		} catch (ClassNotFoundException e) {
			throw new ExportException("Dialect not found.", e);
		} catch (ModelConsistencyException e) {
			throw new ExportException("This model is inconsistent.", e);
		} catch (UnsupportedOperationException e) {
			throw new ExportException("This dialect does not support export SQL.", e);
		} finally {
			IOUtils.closeQuietly(writer);
		}
		return true;
	}
	
	public String getName() {
		return "SQL Exporter";
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	@Override
	protected SqlExportConfig newSimpleConfigInstance() {
		return new SimpleSqlExportConfig();
	}
}
