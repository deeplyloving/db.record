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

package com.liucf.dbrecord;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.liucf.dbrecord.dialect.Dialect;

/**
 * Db. Powerful database query and update tool box.
 */
public class Db {
	
	private static DbPro dbPro = null;
	
	public static void init(String jdbcURL, String jdbcUser, String jdbcPassword) {
		init(DbKit.MAIN_CONFIG_NAME,"com.mysql.cj.jdbc.Driver",jdbcURL,jdbcUser,jdbcPassword,null,null,false);
	}
	
	public static void init(String jdbcDriver, String jdbcURL, String jdbcUser, String jdbcPassword) {
		init(DbKit.MAIN_CONFIG_NAME,jdbcDriver,jdbcURL,jdbcUser,jdbcPassword,null,null,false);
	}
	
	public static void init(String jdbcDriver, String jdbcURL, String jdbcUser, String jdbcPassword,Integer transactionLevel) {
		init(DbKit.MAIN_CONFIG_NAME,jdbcDriver,jdbcURL,jdbcUser,jdbcPassword,transactionLevel,null,false);
	}
	
	public static void init(String jdbcDriver, String jdbcURL, String jdbcUser, String jdbcPassword,Integer transactionLevel,Dialect dialect) {
		init(DbKit.MAIN_CONFIG_NAME,jdbcDriver,jdbcURL,jdbcUser,jdbcPassword,transactionLevel,dialect,false);
	}
	
	public static void init(String jdbcDriver, String jdbcURL, String jdbcUser, String jdbcPassword,Integer transactionLevel,Dialect dialect,Boolean showSql) {
		init(DbKit.MAIN_CONFIG_NAME,jdbcDriver,jdbcURL,jdbcUser,jdbcPassword,transactionLevel,dialect,showSql);
	}
	
	static void init(String configName,String jdbcDriver, String jdbcURL, String jdbcUser, String jdbcPassword,Integer transactionLevel,Dialect dialect,Boolean showSql) {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(jdbcURL);
		dataSource.setUsername(jdbcUser);
		dataSource.setPassword(jdbcPassword);
		dataSource.setDriverClassName(jdbcDriver);
		dataSource.setInitialSize(5);
		dataSource.setMinIdle(5);
		dataSource.setMaxActive(20);
		init(DbKit.MAIN_CONFIG_NAME,dataSource,transactionLevel,dialect,showSql);
	}
	
	public static void init(String configName,DataSource dataSource,Integer transactionLevel,Dialect dialect,Boolean showSql) {
		Config	config = new Config(configName, dataSource, dialect, showSql, transactionLevel);
		DbKit.addConfig(config);
		dbPro = DbPro.use(configName);
	}
	
	public static DbPro use() {
		return DbPro.use();
	}
	
	public static DbPro use(String configName) {
		return DbPro.use(configName);
	}
	
	public static <T> List<T> query(Config config, Connection conn, String sql, Object... paras) throws SQLException {
		return dbPro.query(config, conn, sql, paras);
	}
	
	/**
	 * @see #query(String, String, Object...)
	 */
	public static <T> List<T> query(String sql, Object... paras) {
		return dbPro.query(sql, paras);
	}
	
	/**
	 * @see #query(String, Object...)
	 * @param sql an SQL statement
	 */
	public static <T> List<T> query(String sql) {
		return dbPro.query(sql);
	}
	
	/**
	 * Execute sql query and return the first result. I recommend add "limit 1" in your sql.
	 * @param sql an SQL statement that may contain one or more '?' IN parameter placeholders
	 * @param paras the parameters of sql
	 * @return Object[] if your sql has select more than one column,
	 * 			and it return Object if your sql has select only one column.
	 */
	public static <T> T queryFirst(String sql, Object... paras) {
		return dbPro.queryFirst(sql, paras);
	}
	
	/**
	 * @see #queryFirst(String, Object...)
	 * @param sql an SQL statement
	 */
	public static <T> T queryFirst(String sql) {
		return dbPro.queryFirst(sql);
	}
	
	// 26 queryXxx method below -----------------------------------------------
	/**
	 * Execute sql query just return one column.
	 * @param <T> the type of the column that in your sql's select statement
	 * @param sql an SQL statement that may contain one or more '?' IN parameter placeholders
	 * @param paras the parameters of sql
	 * @return List<T>
	 */
	public static <T> T queryColumn(String sql, Object... paras) {
		return dbPro.queryColumn(sql, paras);
	}
	
	public static <T> T queryColumn(String sql) {
		return dbPro.queryColumn(sql);
	}
	
	public static String queryStr(String sql, Object... paras) {
		return dbPro.queryStr(sql, paras);
	}
	
	public static String queryStr(String sql) {
		return dbPro.queryStr(sql);
	}
	
	public static Integer queryInt(String sql, Object... paras) {
		return dbPro.queryInt(sql, paras);
	}
	
	public static Integer queryInt(String sql) {
		return dbPro.queryInt(sql);
	}
	
	public static Long queryLong(String sql, Object... paras) {
		return dbPro.queryLong(sql, paras);
	}
	
	public static Long queryLong(String sql) {
		return dbPro.queryLong(sql);
	}
	
	public static Double queryDouble(String sql, Object... paras) {
		return dbPro.queryDouble(sql, paras);
	}
	
	public static Double queryDouble(String sql) {
		return dbPro.queryDouble(sql);
	}
	
	public static Float queryFloat(String sql, Object... paras) {
		return dbPro.queryFloat(sql, paras);
	}
	
	public static Float queryFloat(String sql) {
		return dbPro.queryFloat(sql);
	}
	
	public static java.math.BigDecimal queryBigDecimal(String sql, Object... paras) {
		return dbPro.queryBigDecimal(sql, paras);
	}
	
	public static java.math.BigDecimal queryBigDecimal(String sql) {
		return dbPro.queryBigDecimal(sql);
	}
	
	public static byte[] queryBytes(String sql, Object... paras) {
		return dbPro.queryBytes(sql, paras);
	}
	
	public static byte[] queryBytes(String sql) {
		return dbPro.queryBytes(sql);
	}
	
	public static java.util.Date queryDate(String sql, Object... paras) {
		return dbPro.queryDate(sql, paras);
	}
	
	public static java.util.Date queryDate(String sql) {
		return dbPro.queryDate(sql);
	}
	
	public static java.sql.Time queryTime(String sql, Object... paras) {
		return dbPro.queryTime(sql, paras);
	}
	
	public static java.sql.Time queryTime(String sql) {
		return dbPro.queryTime(sql);
	}
	
	public static java.sql.Timestamp queryTimestamp(String sql, Object... paras) {
		return dbPro.queryTimestamp(sql, paras);
	}
	
	public static java.sql.Timestamp queryTimestamp(String sql) {
		return dbPro.queryTimestamp(sql);
	}
	
	public static Boolean queryBoolean(String sql, Object... paras) {
		return dbPro.queryBoolean(sql, paras);
	}
	
	public static Boolean queryBoolean(String sql) {
		return dbPro.queryBoolean(sql);
	}
	
	public static Number queryNumber(String sql, Object... paras) {
		return dbPro.queryNumber(sql, paras);
	}
	
	public static Number queryNumber(String sql) {
		return dbPro.queryNumber(sql);
	}
	// 26 queryXxx method under -----------------------------------------------
	
	/**
	 * Execute sql update
	 */
	static int update(Config config, Connection conn, String sql, Object... paras) throws SQLException {
		return dbPro.update(config, conn, sql, paras);
	}
	
	/**
	 * Execute update, insert or delete sql statement.
	 * @param sql an SQL statement that may contain one or more '?' IN parameter placeholders
	 * @param paras the parameters of sql
	 * @return either the row count for <code>INSERT</code>, <code>UPDATE</code>,
     *         or <code>DELETE</code> statements, or 0 for SQL statements 
     *         that return nothing
	 */
	public static int update(String sql, Object... paras) {
		return dbPro.update(sql, paras);
	}
	
	/**
	 * @see #update(String, Object...)
	 * @param sql an SQL statement
	 */
	public static int update(String sql) {
		return dbPro.update(sql);
	}
	
	static List<Record> find(Config config, Connection conn, String sql, Object... paras) throws SQLException {
		return dbPro.find(config, conn, sql, paras);
	}
	
	/**
	 * @see #find(String, String, Object...)
	 */
	public static List<Record> find(String sql, Object... paras) {
		return dbPro.find(sql, paras);
	}
	
	/**
	 * @see #find(String, String, Object...)
	 * @param sql the sql statement
	 */
	public static List<Record> find(String sql) {
		return dbPro.find(sql);
	}
	
	/**
	 * Find first record. I recommend add "limit 1" in your sql.
	 * @param sql an SQL statement that may contain one or more '?' IN parameter placeholders
	 * @param paras the parameters of sql
	 * @return the Record object
	 */
	public static Record findFirst(String sql, Object... paras) {
		return dbPro.findFirst(sql, paras);
	}
	
	/**
	 * @see #findFirst(String, Object...)
	 * @param sql an SQL statement
	 */
	public static Record findFirst(String sql) {
		return dbPro.findFirst(sql);
	}
	
	/**
	 * Find record by id.
	 * Example: Record user = Db.findById("user", 15);
	 * @param tableName the table name of the table
	 * @param idValue the id value of the record
	 */
	public static Record findById(String tableName, Object idValue) {
		return dbPro.findById(tableName, idValue);
	}
	
	/**
	 * Find record by id. Fetch the specific columns only.
	 * Example: Record user = Db.findById("user", 15, "name, age");
	 * @param tableName the table name of the table
	 * @param idValue the id value of the record
	 * @param columns the specific columns separate with comma character ==> ","
	 */
	public static Record findById(String tableName, Number idValue, String columns) {
		return dbPro.findById(tableName, idValue, columns);
	}
	
	/**
	 * Find record by id.
	 * Example: Record user = Db.findById("user", "user_id", 15);
	 * @param tableName the table name of the table
	 * @param primaryKey the primary key of the table
	 * @param idValue the id value of the record
	 */
	public static Record findById(String tableName, String primaryKey, Number idValue) {
		return dbPro.findById(tableName, primaryKey, idValue);
	}
	
	/**
	 * Find record by id. Fetch the specific columns only.
	 * Example: Record user = Db.findById("user", "user_id", 15, "name, age");
	 * @param tableName the table name of the table
	 * @param primaryKey the primary key of the table
	 * @param idValue the id value of the record
	 * @param columns the specific columns separate with comma character ==> ","
	 */
	public static Record findById(String tableName, String primaryKey, Object idValue, String columns) {
		return dbPro.findById(tableName, primaryKey, idValue, columns);
	}
	
	/**
	 * Delete record by id.
	 * Example: boolean succeed = Db.deleteById("user", 15);
	 * @param tableName the table name of the table
	 * @param id the id value of the record
	 * @return true if delete succeed otherwise false
	 */
	public static boolean deleteById(String tableName, Object id) {
		return dbPro.deleteById(tableName, id);
	}
	
	/**
	 * Delete record by id.
	 * Example: boolean succeed = Db.deleteById("user", "user_id", 15);
	 * @param tableName the table name of the table
	 * @param primaryKey the primary key of the table
	 * @param id the id value of the record
	 * @return true if delete succeed otherwise false
	 */
	public static boolean deleteById(String tableName, String primaryKey, Object id) {
		return dbPro.deleteById(tableName, primaryKey, id);
	}
	
	/**
	 * Delete record.
	 * Example: boolean succeed = Db.delete("user", "id", user);
	 * @param tableName the table name of the table
	 * @param primaryKey the primary key of the table
	 * @param record the record
	 * @return true if delete succeed otherwise false
	 */
	public static boolean delete(String tableName, String primaryKey, Record record) {
		return dbPro.delete(tableName, primaryKey, record);
	}
	
	/**
	 * Example: boolean succeed = Db.delete("user", user);
	 * @see #delete(String, String, Record)
	 */
	public static boolean delete(String tableName, Record record) {
		return dbPro.delete(tableName, record);
	}
	
	static Page<Record> paginate(Config config, Connection conn, int pageNumber, int pageSize, String select, boolean count, Object... paras) throws SQLException {
		return dbPro.paginate(config, conn, pageNumber, pageSize, select, count, paras);
	}
	
	/**
	 * @see #paginate(String, int, int, String, String, Object...)
	 */
	public static Page<Record> paginate(int pageNumber, int pageSize, String select, boolean count, Object... paras) {
		return dbPro.paginate(pageNumber, pageSize, select, count, paras);
	}
	
	/**
	 * @see #paginate(String, int, int, String, String, Object...)
	 */
	public static Page<Record> paginate(int pageNumber, int pageSize, String select) {
		return dbPro.paginate(pageNumber, pageSize, select);
	}
	
	static boolean save(Config config, Connection conn, String tableName, String primaryKey, Record record) throws SQLException {
		return dbPro.save(config, conn, tableName, primaryKey, record);
	}
	
	/**
	 * Save record.
	 * @param tableName the table name of the table
	 * @param primaryKey the primary key of the table
	 * @param record the record will be saved
	 * @param true if save succeed otherwise false
	 */
	public static boolean save(String tableName, String primaryKey, Record record) {
		return dbPro.save(tableName, primaryKey, record);
	}
	
	/**
	 * @see #save(String, String, Record)
	 */
	public static boolean save(String tableName, Record record) {
		return dbPro.save(tableName, record);
	}
	
	static boolean update(Config config, Connection conn, String tableName, String primaryKey, Record record) throws SQLException {
		return dbPro.update(config, conn, tableName, primaryKey, record);
	}
	
	/**
	 * Update Record.
	 * @param tableName the table name of the Record save to
	 * @param primaryKey the primary key of the table
	 * @param record the Record object
	 * @param true if update succeed otherwise false
	 */
	public static boolean update(String tableName, String primaryKey, Record record) {
		return dbPro.update(tableName, primaryKey, record);
	}
	
	/**
	 * Update Record. The primary key of the table is: "id".
	 * @see #update(String, String, Record)
	 */
	public static boolean update(String tableName, Record record) {
		return dbPro.update(tableName, record);
	}
	
	/**
	 * Execute transaction.
	 * @param config the Config object
	 * @param transactionLevel the transaction level
	 * @param atom the atom operation
	 * @return true if transaction executing succeed otherwise false
	 */
	static boolean tx(Config config, int transactionLevel, TransactionWrap atom) {
		return dbPro.tx(config, transactionLevel, atom);
	}
	
	public static boolean tx(int transactionLevel, TransactionWrap atom) {
		return dbPro.tx(transactionLevel, atom);
	}
	
	/**
	 * Execute transaction with default transaction level.
	 * @see #tx(int, TransactionWrap)
	 */
	public static boolean tx(TransactionWrap atom) {
		return dbPro.tx(atom);
	}
	
	/**
	 * @see #batch(String, String, Object[][], int)
     */
    public static int[] batch(String sql, Object[][] paras, int batchSize) {
    	return dbPro.batch(sql, paras, batchSize);
    }
	
	/**
	 * @see #batch(String, String, String, List, int)
     */
	public static int[] batch(String sql, String columns, List<?> modelOrRecordList, int batchSize) {
		return dbPro.batch(sql, columns, modelOrRecordList, batchSize);
	}
	
	/**
	 * @see #batch(String, List, int)
     */
    public static int[] batch(List<String> sqlList, int batchSize) {
    	return dbPro.batch(sqlList, batchSize);
    }
}



