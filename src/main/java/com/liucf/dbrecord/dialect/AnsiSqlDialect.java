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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.liucf.dbrecord.Db;
import com.liucf.dbrecord.DbKit;
import com.liucf.dbrecord.DbRecordException;
import com.liucf.dbrecord.Page;
import com.liucf.dbrecord.Record;
import com.liucf.dbrecord.RecordBuilder;

/**
 * AnsiSqlDialect. Try to use ANSI SQL dialect with ActiveRecordPlugin.
 * <p>
 * A clever person solves a problem. A wise person avoids it.
 */
public class AnsiSqlDialect extends Dialect {
	
	public String forTableBuilderDoBuild(String tableName) {
		return "select * from " + tableName + " where 1 = 2";
	}
	
	public String forDbFindById(String tableName, String primaryKey, String columns) {
		StringBuilder sql = new StringBuilder("select ");
		if (columns.trim().equals("*")) {
			sql.append(columns);
		}
		else {
			String[] columnsArray = columns.split(",");
			for (int i=0; i<columnsArray.length; i++) {
				if (i > 0)
					sql.append(", ");
				sql.append(columnsArray[i].trim());
			}
		}
		sql.append(" from ");
		sql.append(tableName.trim());
		sql.append(" where ").append(primaryKey).append(" = ?");
		return sql.toString();
	}
	
	public String forDbDeleteById(String tableName, String primaryKey) {
		StringBuilder sql = new StringBuilder("delete from ");
		sql.append(tableName.trim());
		sql.append(" where ").append(primaryKey).append(" = ?");
		return sql.toString();
	}
	
	public void forDbSave(StringBuilder sql, List<Object> paras, String tableName, Record record) {
		sql.append("insert into ");
		sql.append(tableName.trim()).append("(");
		StringBuilder temp = new StringBuilder();
		temp.append(") values(");
		
		for (Entry<String, Object> e: record.getColumns().entrySet()) {
			if (paras.size() > 0) {
				sql.append(", ");
				temp.append(", ");
			}
			sql.append(e.getKey());
			temp.append("?");
			paras.add(e.getValue());
		}
		sql.append(temp.toString()).append(")");
	}
	
	public void forDbUpdate(String tableName, String primaryKey, Object id, Record record, StringBuilder sql, List<Object> paras) {
		sql.append("update ").append(tableName.trim()).append(" set ");
		for (Entry<String, Object> e: record.getColumns().entrySet()) {
			String colName = e.getKey();
			if (!primaryKey.equalsIgnoreCase(colName)) {
				if (paras.size() > 0) {
					sql.append(", ");
				}
				sql.append(colName).append(" = ? ");
				paras.add(e.getValue());
			}
		}
		sql.append(" where ").append(primaryKey).append(" = ?");
		paras.add(id);
	}
	
	/**
	 * SELECT * FROM subject t1 WHERE (SELECT count(*) FROM subject t2 WHERE t2.id < t1.id AND t2.key = '123') > = 10 AND (SELECT count(*) FROM subject t2 WHERE t2.id < t1.id AND t2.key = '123') < 20 AND t1.key = '123'
	 */
	public void forPaginate(StringBuilder sql, int pageNumber, int pageSize, String select) {
		throw new DbRecordException("Your should not invoke this method because takeOverDbPaginate(...) will take over it.");
	}
	
	public boolean isTakeOverDbPaginate() {
		return true;
	}
	
	@SuppressWarnings("rawtypes")
	public Page<Record> takeOverDbPaginate(Connection conn, int pageNumber, int pageSize, String select, boolean count, Object... paras) throws SQLException {
		long totalRow = 0;
		int totalPage = 0;
		if(count) {
			//TODO 此处该有bug  应该没有指定数据源
			List result = Db.query(DbKit.getConfig(),conn, "select count(*) from (" +select+") as _tmp", paras);
			int size = result.size();
			if (size == 1)
				totalRow = ((Number)result.get(0)).longValue();
			else if (size > 1)
				totalRow = result.size();
			else
				return new Page<Record>(new ArrayList<Record>(0), pageNumber, pageSize, 0, 0);
		}
		
		totalPage = (int) (totalRow / pageSize);
		if (totalRow % pageSize != 0) {
			totalPage++;
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append(select).append(" ");
		PreparedStatement pst = conn.prepareStatement(sql.toString(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		for (int i=0; i<paras.length; i++) {
			pst.setObject(i + 1, paras[i]);
		}
		ResultSet rs = pst.executeQuery();
		
		// move the cursor to the start
		int offset = pageSize * (pageNumber - 1);
		for (int i=0; i<offset; i++)
			if (!rs.next())
				break;
		
		List<Record> list = buildRecord(rs, pageSize);
		if (rs != null) rs.close();
		if (pst != null) pst.close();
		return new Page<Record>(list, pageNumber, pageSize, totalPage, (int) totalRow);
	}
	
	private List<Record> buildRecord(ResultSet rs, int pageSize) throws SQLException {
		List<Record> result = new ArrayList<Record>();
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		String[] labelNames = new String[columnCount + 1];
		int[] types = new int[columnCount + 1];
		buildLabelNamesAndTypes(rsmd, labelNames, types);
		for (int k=0; k<pageSize && rs.next(); k++) {
			Record record = new Record();
			Map<String, Object> columns = record.getColumns();
			for (int i=1; i<=columnCount; i++) {
				Object value;
				if (types[i] < Types.BLOB)
					value = rs.getObject(i);
				else if (types[i] == Types.CLOB)
					value = RecordBuilder.handleClob(rs.getClob(i));
				else if (types[i] == Types.NCLOB)
					value = RecordBuilder.handleClob(rs.getNClob(i));
				else if (types[i] == Types.BLOB)
					value = RecordBuilder.handleBlob(rs.getBlob(i));
				else
					value = rs.getObject(i);
				
				columns.put(labelNames[i], value);
			}
			result.add(record);
		}
		return result;
	}
	
	private void buildLabelNamesAndTypes(ResultSetMetaData rsmd, String[] labelNames, int[] types) throws SQLException {
		for (int i=1; i<labelNames.length; i++) {
			labelNames[i] = rsmd.getColumnLabel(i);
			types[i] = rsmd.getColumnType(i);
		}
	}
	
	public boolean isTakeOverModelPaginate() {
		return true;
	}
	
}
