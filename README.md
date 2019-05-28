# db.record
通过 record(map)的方式操作表数据,似orm非orm,半对像操作
本类库基于jfinal V1.9 改造

### maven 使用
```
<dependency>
	<groupId>com.liucf</groupId>
	<artifactId>db.record</artifactId>
	<version>0.0.4</version>
</dependency>
```

### 示例

##### 1.初始化Db
```
//初始化数据连接
Db.init("jdbc:mysql://host:port/test?characterEncoding=utf-8&autoReconnect=true&autoReconnectForPools=true&serverTimezone=GMT%2B8","root", "xxx");
//打印sql日志
Db.use().setShowSql(true);
```

##### 2.查询数据
```
//简单查询
List<Record> baskets = Db.find("select * from base_basket");
for (Record record : baskets) {
	System.out.println(record.getStr("id"));
	System.out.println(record.toJson());
}

//根据id查询
Record record = Db.findById("base_basket", "001")

//查询首条数据
Db.findFirst("select * from base_basket where id = ?", "001")

//分页查询 count参数决定是否统计总行数
Page<Record> p = Db.paginate(1, 2, "select * from base_basket where id>?", false, "1000");
p.getList();
p.getPageNumber();
p.getPageSize();
p.getTotalPage();
p.getTotalRow();
```

##### 3.新增
```
Record r = new Record();
r.set("id", "ddddd");
Db.save("base_basket", r);
```

##### 4.更新
```
Record r = new Record();
r.set("id", "ddddd");
Db.update("base_basket", r);
//主键名称非id
//Db.update("base_basket", "id", r);
```

##### 4.删除
```
Db.deleteById("base_basket", "001");
//Db.update("delete from base_basket");
```

##### 5.事务
```
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
```
