package com.liucf.db.record;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import com.liucf.dbrecord.Db;
import com.liucf.dbrecord.TransactionWrap;
import com.liucf.dbrecord.Page;
import com.liucf.dbrecord.Record;

/**
 * Unit test for simple App.
 */
public class AppTest {
	
	@Test
	public void test() {
		//初始化数据连接
		Db.init("jdbc:mysql://192.168.4.212:3306/test?characterEncoding=utf-8&autoReconnect=true&autoReconnectForPools=true&serverTimezone=GMT%2B8","root", "xxx");
		//打印sql日志
		Db.use().setShowSql(true);
		
		List<Record> baskets = Db.find("select * from base_basket");
		for (Record record : baskets) {
			System.out.println(record.getStr("id"));
			System.out.println(record.toJson());
		}
		Db.deleteById("base_basket", "ddddd");
		Db.
		Record r = new Record();
		r.set("id", "ddddd");
		Db.save("base_basket", r);
		
		r.set("id", "ddddd");
		r.set("remarks", "remarks");
		Db.update("base_basket", "id", r);
		
		Db.tx(new TransactionWrap() {
			@Override
			public boolean run() throws SQLException {
				try {
					Record r = new Record();
					r.set("id", "ddddd");
					Db.save("base_basket", r);
					
					r.set("id", "ddddd");
					r.set("remarks", "remarks");
					Db.update("base_basket", "id", r);
					
				} catch (Exception e) {
					return false;
				}
				return true;
			}
		});
		//count参数决定是否统计总行数
		Page<Record> p = Db.paginate(1, 2, "select * from base_basket where id>?", false, "1000");
		p.getList();
		p.getPageNumber();
		p.getPageSize();
		p.getTotalPage();
		p.getTotalRow();
		
		System.out.println(p.getTotalRow());
	}
}
