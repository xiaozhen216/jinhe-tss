package com.jinhe.tss.framework.mock;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.TxTestSupport;
import com.jinhe.tss.framework.mock.dao._IUserDAO;
import com.jinhe.tss.framework.mock.model._Group;
import com.jinhe.tss.framework.mock.model._GroupRole;
import com.jinhe.tss.framework.mock.model._GroupRoleId;
import com.jinhe.tss.framework.mock.model._Role;
import com.jinhe.tss.framework.mock.model._User;
import com.jinhe.tss.framework.mock.service._IUMSerivce;

public class UMServiceTest extends TxTestSupport { 
    
    @Autowired _IUMSerivce umSerivce;
    
    @Autowired _IUserDAO userDao;
    
    @Test
    public void testGetEntitiesByHQL() {
        _Group group = new _Group();
        group.setCode("RD");
        group.setName("研发");
        umSerivce.createGroup(group);
        
        _User user = new _User();
        user.setGroup(group);
        user.setUserName("JohnXa");
        user.setPassword("123456");
        user.setAge(new Integer(25));
        user.setAddr("New York");
        user.setEmail("john@hotmail.com");
        umSerivce.createUser(user);
        
        String hql = "from _User t where t.userName in ( :names ) ";
        List<String> nameList = new ArrayList<String>();
        nameList.add("JohnXa");
        List<?> result = userDao.getEntities(hql, new Object[]{"names"}, new Object[]{nameList});
        assertEquals(1, result.size());
        log.debug(result.get(0));
    }
    
    @Test
    public void testGetEntitiesByNativeSql() {
        _Group group = new _Group();
        group.setCode("RD");
        group.setName("研发");
        umSerivce.createGroup(group);
        
        _User user = new _User();
        user.setGroup(group);
        user.setUserName("JohnXa");
        user.setPassword("123456");
        user.setAge(new Integer(25));
        user.setAddr("New York");
        user.setEmail("john@hotmail.com");
        umSerivce.createUser(user);
        
    	String nativeSql = "select t.* from test_user t where t.userName = ? ";
		List<?> result = userDao.getEntitiesByNativeSql(nativeSql, _User.class, "JohnXa");
		assertEquals(1, result.size());
		log.debug(result.get(0));
    }
 
    @Test
    public void testUMCRUD() {
        log.info("test start......");
        
        _Group group = new _Group();
        group.setCode("RD");
        group.setName("研发");
        umSerivce.createGroup(group);
        
        _User user = new _User();
        user.setGroup(group);
        user.setUserName("JohnXa");
        user.setPassword("123456");
        user.setAge(new Integer(25));
        user.setAddr("New York");
        user.setEmail("john@hotmail.com");
        umSerivce.createUser(user);
        
        List<_User> result = umSerivce.queryAllUsers();
        assertEquals(1, result.size());
        
        _User userPo = result.get(0);
        log.info(userPo + userPo.getCreateTime().toString());
        
        userPo.setUserName("Jon.King");
        umSerivce.updateUser(userPo);
        
        log.info(userPo + userPo.getUpdateTime().toString());
        
        result = umSerivce.queryAllUsers();
        userPo = result.get(0);
        log.info(userPo);
        
        umSerivce.deleteUser(userPo);
        
        result = umSerivce.queryAllUsers();
        assertEquals(0, result.size());
        
        _Role role = new _Role();
        role.setCode("Engineer");
        role.setName("工程师");
        umSerivce.createRole(role);
        
        _GroupRole gr = new _GroupRole();
        gr.setId(new _GroupRoleId(group.getId(), role.getId()));
        umSerivce.createGroupRole(gr);
        
        _UMCondition condition = new _UMCondition();
        assertEquals(1, umSerivce.queryGroupRole(condition).size());
    }
}
