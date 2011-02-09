/*
 * Copyright 2007-2011 Jiemamy Project and the Others.
 * Created on 2011/02/03
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
package org.jiemamy;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.google.common.collect.Lists;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

/**
 * jiemamy-coreのリリーステスト。
 * 
 * @version $Id$
 * @author daisuke
 */
@Ignore
public class JiemamyCoreReleaseTest {
	
	private static Logger logger = LoggerFactory.getLogger(JiemamyCoreReleaseTest.class);
	
	static final String HEADER = "/* * Copyright 2007-" + Calendar.getInstance().get(Calendar.YEAR)
			+ " Jiemamy Project and the Others.";
	
	static final XPathExpression VERSION_IN_POM;
	static {
		try {
			VERSION_IN_POM = XPathFactory.newInstance().newXPath().compile("/project/version");
		} catch (XPathExpressionException e) {
			throw new Error(e);
		}
	}
	

	/**
	 * fixmeレベルのタスクタグが残っていないこと。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testFixMe() throws Exception {
		File srcDir = new File("./src");
		Collection<String> paths = Lists.newArrayList();
		checkFixMe(srcDir, paths);
		for (String path : paths) {
			logger.error("FIX" + "ME detected: " + path);
		}
		assertThat(paths.isEmpty(), is(true));
	}
	
	/**
	 * 各ソースのヘッダがあること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testSourceHeader() throws Exception {
		File srcDir = new File("./src");
		Collection<String> paths = Lists.newArrayList();
		checkHeader(srcDir, paths);
		for (String path : paths) {
			logger.error("Illegal header detected: " + path);
		}
		assertThat(paths.isEmpty(), is(true));
	}
	
	/**
	 * sysoutを使っていないこと。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testSystemOut() throws Exception {
		File srcDir = new File("./src");
		Collection<String> paths = Lists.newArrayList();
		checkSystemOut(srcDir, paths);
		for (String path : paths) {
			logger.error("sysout detected: " + path);
		}
		assertThat(paths.isEmpty(), is(true));
	}
	
	/**
	 * {@link Version}がpomと一致していること。
	 * 
	 * @throws Exception 例外が発生した場合
	 */
	@Test
	public void testVersion() throws Exception {
		File pom = new File("./pom.xml");
		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document dom = documentBuilder.parse(pom);
		String pomVersion = VERSION_IN_POM.evaluate(dom);
		logger.info("version = " + pomVersion);
		Version version = JiemamyContext.getVersion();
		assertThat(pomVersion, is(version.toString()));
		assertThat(version.isSnapshot(), is(false));
	}
	
	private void checkFixMe(File target, Collection<String> paths) throws IOException {
		if (target.isDirectory()) {
			for (File child : target.listFiles()) {
				if (child.getName().equals(".svn") == false) {
					checkFixMe(child, paths);
				}
			}
		} else if (target.isFile()) {
			String content = FileUtils.readFileToString(target).replaceAll("[\\r\\n]", "");
			if (content.contains("FIX" + "ME")) {
				paths.add(target.getPath());
			}
		}
	}
	
	private void checkHeader(File target, Collection<String> paths) throws IOException {
		if (target.isDirectory()) {
			for (File child : target.listFiles()) {
				if (child.getName().equals(".svn") == false) {
					checkHeader(child, paths);
				}
			}
		} else if (target.isFile() && target.getName().endsWith(".java")
				&& target.getName().equals("package-info.java") == false) {
			String content =
					FileUtils.readFileToString(target).replaceAll("[\\r\\n]", "").substring(0, HEADER.length());
			if (content.startsWith(HEADER) == false) {
				paths.add(target.getPath());
			}
		}
	}
	
	private void checkSystemOut(File target, Collection<String> paths) throws IOException {
		if (target.isDirectory()) {
			for (File child : target.listFiles()) {
				if (child.getName().equals(".svn") == false) {
					checkSystemOut(child, paths);
				}
			}
		} else if (target.isFile() && target.getName().endsWith(".java")
				&& target.getName().equals("package-info.java") == false
				&& target.getName().equals("JiemamyCoreReleaseTest.java") == false) {
			String content = FileUtils.readFileToString(target).replaceAll("[\\r\\n]", "");
			if (content.contains("System" + ".out") || content.contains("print" + "StackTrace()")) {
				paths.add(target.getPath());
			}
		}
	}
}
