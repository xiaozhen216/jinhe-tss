/* ==================================================================   
 * Created [2007-2-15] by Jon.King 
 * ==================================================================  
 * TSS 
 * ================================================================== 
 * mailTo:jinpujun@hotmail.com
 * Copyright (c) Jon.King, 2012-2015 
 * ================================================================== 
 */
package com.jinhe.tss.framework.persistence.connpool;

import java.sql.Connection;
import java.sql.SQLException;

import com.jinhe.tss.cache.Cacheable;
import com.jinhe.tss.cache.JCache;
import com.jinhe.tss.cache.Pool;
import com.jinhe.tss.cache.extension.workqueue.AbstractTask;

/**
 * 输出记录到数据库的任务抽象超类。<br/>
 * 类似日志输出、访问量输出可以通过继承该超类实现。
 * 
 */
public abstract class Output2DBTask extends AbstractTask {

	public void excute() {
		if (records == null)
			return;

		Pool connectionPool = JCache.getInstance().getConnectionPool();
		Cacheable connItem = connectionPool.checkOut(0);
		Connection conn = (Connection) connItem.getValue();
		try {
			createRecords(conn);
		} catch (SQLException e) {
			log.error("写入记录到数据库时候出错", e);
		} finally {
			connectionPool.checkIn(connItem);
		}
	}
	
	protected abstract void createRecords(Connection conn) throws SQLException;

}
