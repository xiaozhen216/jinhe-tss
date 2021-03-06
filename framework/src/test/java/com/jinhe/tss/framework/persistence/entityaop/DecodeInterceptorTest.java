package com.jinhe.tss.framework.persistence.entityaop;

import static org.junit.Assert.*;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jinhe.tss.framework.TxTestSupport;
import com.jinhe.tss.framework.mock.model._Group;
import com.jinhe.tss.framework.mock.service._IUMSerivce;

public class DecodeInterceptorTest extends TxTestSupport { 
    
    @Autowired _IUMSerivce umSerivce;
    
    @Test
    public void testDecodeUtil() {
        String decode = DecodeUtil.getDecode(null, null, -1);
        assertEquals("0", decode);
    }
 
    @Test
    public void testUMCRUD() {
        log.info("test start......");
        
        _Group group = new _Group();
        group.setCode("RD");
        group.setName("研发");
        group.setParentId(0L);
        group = umSerivce.createGroup(group);
        log.info(group);
        
        for(int i = 1; i < 11; i++) {
            _Group group1 = new _Group();
            group1.setCode("RD" + i);
            group1.setName("研发");
            group1.setParentId(group.getId());
            group1 = umSerivce.createGroup(group1);
            log.info(group1);
            
            for(int j = 1; j < 11; j++) {
                _Group group2 = new _Group();
                group2.setCode("RD" + i + "_" + j);
                group2.setName("研发");
                group2.setParentId(group1.getId());
                group2 = umSerivce.createGroup(group2);
                log.info(group2);
            }
        }
        
        List<_Group> list = umSerivce.queryGroups("from _Group o where o.decode <> ? and o.decode like ? order by o.decode", 
                "0000100002", "0000100002%");
        log.info(list.size());
        for(_Group temp : list) {
            log.info(temp);
        }
        
        List<?> data = umSerivce.getChildrenByDecode("0000100002");
        log.info(data.size());
        for(Object temp : data) {
            log.info(temp);
        }
        
        data = umSerivce.getChildrenById(46L);
        log.info(data.size());
        for(Object temp : data) {
            log.info(temp);
        }
        
        data = umSerivce.getParentsById(46L);
        log.info(data.size());
        for(Object temp : data) {
            log.info(temp);
        }
        
        data = umSerivce.getRelationsNodeWhenSort(1L, 3, 7);
        log.info(data.size());
        for(Object temp : data) {
            log.info(temp);
        }
    }
}
