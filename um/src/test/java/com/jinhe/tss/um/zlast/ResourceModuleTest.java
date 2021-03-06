package com.jinhe.tss.um.zlast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;

import com.jinhe.tss.framework.Global;
import com.jinhe.tss.framework.sso.context.Context;
import com.jinhe.tss.um.TxSupportTest4UM;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.action.ResourceAction;
import com.jinhe.tss.um.dao.IResourceTypeDao;
import com.jinhe.tss.um.entity.Application;
import com.jinhe.tss.um.entity.Operation;
import com.jinhe.tss.um.entity.ResourceType;
import com.jinhe.tss.um.service.IResourceService;
import com.jinhe.tss.util.BeanUtil;

/**
 * 系统、资源、权限项相关模块的单元测试
 */
public class ResourceModuleTest extends TxSupportTest4UM {
    
    @Autowired ResourceAction action;
    @Autowired IResourceService service;
    @Autowired IResourceTypeDao   resourceTypeDao;
    
    @Before
    public void setUp() {
    	Global.setContext(super.applicationContext);
		Context.setResponse(response = new MockHttpServletResponse());
        
        // 初始化虚拟登录用户信息
        login(UMConstants.ADMIN_USER_ID, UMConstants.ADMIN_USER_NAME);
    
        init();
    }
  
    @Test
    public void testApplication() {
        Application application = service.getApplication(UMConstants.TSS_APPLICATION_ID);
        assertNotNull(application);
        
        action.getApplicationInfo(response, application.getId());
        
        Application application2 = new Application();
        BeanUtil.copy(application2, application);
        application2.setId(null);
        application2.setApplicationId("tss2");
        application2.setName("TSS2");
        service.saveApplication(application2);
        
        action.getAllApplication2Tree(response);
        
        List<?> apps = service.getApplications();
        assertTrue(apps.size() >= 2);
        
        action.editApplication(response, application);
        action.deleteApplication(response, application.getId());
    }

    @Test
    public void testResourceType() {
        String applicationId = UMConstants.TSS_APPLICATION_ID;
        String resourceTypeId = UMConstants.ROLE_RESOURCE_TYPE_ID;

        String hql = " from ResourceType o where upper(o.applicationId) = ? and o.resourceTypeId = ?";
		List<?> list = resourceTypeDao.getEntities(hql, applicationId.toUpperCase(), resourceTypeId);
		assertTrue(list.size() > 0);
 
		ResourceType rt = (ResourceType) list.get(0);
		assertNotNull(rt);
        
        action.getResourceTypeInfo(response, rt.getId());

        ResourceType resourceType = service.getResourceTypeById(rt.getId());
        action.editResourceType(response, resourceType);
        
        action.deleteResourceType(response, rt.getId());
    }
    
    @Test
    public void testOperation() {
        String applicationId = UMConstants.TSS_APPLICATION_ID;
        String roleResourceTypeId = UMConstants.ROLE_RESOURCE_TYPE_ID;
 
        List<?> operations = resourceTypeDao.getOperations(applicationId, roleResourceTypeId);
        assertTrue(operations.size() > 0);
        
        Operation operation = (Operation) operations.get(0);
        Long operationId = operation.getId();
        
		Operation operationPO = service.getOperationById(operationId);
        assertEquals(operation, operationPO);
        
		action.editOperation(response, operationPO);
        
        action.getOperationInfo(response, operationId);
        action.deleteOperation(response, operationId);
    }
}
