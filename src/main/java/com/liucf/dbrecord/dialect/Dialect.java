/**
 * Copyright (c) 2011-2015, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liucf.dbrecord.dialect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.liucf.dbrecord.Page;
import com.liucf.dbrecord.Record;

/**
 * Dialect.
 */
public abstract class Dialect {
	public abstract String forTableBuilderDoBuild(String tableName);
	public abstract String forDbFindById(String tableName, String primaryKey, String columns);
	public abstract String forDbDeleteById(String tableName, String primaryKey);
	public abstract void forDbSave(StringBuilder sql, List<Object> paras, String tableName, Record record);
	public abstract void forDbUpdate(String tableName, String primaryKey, Object id, Record record, StringBuilder sql, List<Object> paras);
	public abstract void forPaginate(StringBuilder sql, int pageNumber, int pageSize, String select);
	
	public boolean isOracle() {
		return false;
	}
	
	public boolean isTakeOverDbPaginate() {
		return false;
	}
	
	public Page<Record> takeOverDbPaginate(Connection conn, int pageNumber, int pageSize, String select, boolean count, Object... paras) throws SQLException {
		throw new RuntimeException("You should implements this method in " + getClass().getName());
	}
	
	public boolean isTakeOverModelPaginate() {
		return false;
	}
	
	public void fillStatement(PreparedStatement pst, List<Object> paras) throws SQLException {
		for (int i=0, size=paras.size(); i<size; i++) {
			pst.setObject(i + 1, paras.get(i));
		}
	}
	
	public void fillStatement(PreparedStatement pst, Object... paras) throws SQLException {
		for (int i=0; i<paras.length; i++) {
			pst.setObject(i + 1, paras[i]);
		}
	}
	
	public String getDefaultPrimaryKey() {
		return "id";
	}
}






