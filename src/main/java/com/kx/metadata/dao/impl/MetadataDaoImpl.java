package com.kx.metadata.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.kx.metadata.dao.IMetadataDao;
@Repository("metadataDao")
public class MetadataDaoImpl implements IMetadataDao {
	private DataSourceTransactionManager transactionManager; 
	private DefaultTransactionDefinition def;

	private JdbcTemplate jdbcTemplate;
    
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.transactionManager =  new DataSourceTransactionManager(dataSource);
		this.def = new DefaultTransactionDefinition();  
		this.def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	@Override
	public boolean execute(String sql) {
		try {
			this.jdbcTemplate.execute(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Map<String, Object>> queryForList(String sql) {
		return  this.jdbcTemplate.queryForList(sql);
	}

	@Override
	public Map<String, Object> queryForMap(String sql) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map= this.jdbcTemplate.queryForMap(sql);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return map;
		
	}

	@Override
	public int queryForInt(String sql) {
		return  this.jdbcTemplate.queryForInt(sql);
	}
	@Transactional("transactionManager")
	public boolean batch(String [] sqls){
		this.jdbcTemplate.batchUpdate(sqls);
		return true;
	}

	@Override
	public String getPrimaryValue(String tableName) {
		try {
			return this.jdbcTemplate.queryForMap("SELECT _NEXTVAL('"+tableName+"') sequence").get("sequence").toString();
			//SqlRowSet sqlRowSet = this.jdbcTemplate.queryForRowSet("SELECT _NEXTVAL('"+tableName+"') sequence");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
