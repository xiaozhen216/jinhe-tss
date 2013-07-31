package com.jinhe.tss.portal.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jinhe.tss.framework.persistence.TreeSupportDao;
import com.jinhe.tss.framework.sso.Environment;
import com.jinhe.tss.portal.dao.IPortalDao;
import com.jinhe.tss.portal.entity.Structure;
import com.jinhe.tss.portal.entity.ThemePersonal;
 
@Repository("PortalDao")
public class PortalDao extends TreeSupportDao<Structure> implements IPortalDao {

    public PortalDao() {
		super(Structure.class);
	}
    
    public Structure saveStructure(Structure ps) {
    	if(ps.getId() == null) {
            return create(ps);
        } 
    	else {
            update(ps);
            return ps; 
        }
    }
    
    public void deleteStructure(Structure ps) {
        super.delete(em.merge(ps));
    }
    
    public Structure moveStructure(Structure ps){
        return saveStructure(ps);
    }
 
    public List<Structure> getParentsById(Long id, String operationId) {
        return getParentsById(id);
    }

    public List<Structure> getChildrenById(Long id, String operationId) {
        return getChildrenById(id);
    }

    public Object[] getPortalComponents(Long portalId, Long currentThemeId){
        String[] hqls = new String[3];        
        hqls[0] = "from Component o where o.id in (select distinct ti.decorator.id from Structure p, ThemeInfo ti where p.portalId=? and p.id=ti.id.structureId and ti.id.themeId = ? and p.type<>0 and p.disabled<>1) or o.isDefault = 1 ";
        hqls[1] = "from Component o where o.id in (select distinct ti.layout.id    from Structure p, ThemeInfo ti where p.portalId=? and p.id=ti.id.structureId and ti.id.themeId = ? and p.type<>3 and p.disabled<>1) or o.isDefault = 1 ";
        hqls[2] = "from Component o where o.id in (select distinct t.id from Structure p, Portlet t where p.definerId=t.id and p.type=3 and p.portalId=? and p.disabled<>1)";
        
        Object[] returnVal = new Object[3];   
        returnVal[0] = getEntities(hqls[0], portalId, currentThemeId); 
        returnVal[1] = getEntities(hqls[1], portalId, currentThemeId); 
        returnVal[2] = getEntities(hqls[2], currentThemeId); 
        return returnVal;
    }
    
    public List<?> getThemesByPortal(Long portalId) {
        return getEntities("from Theme o where o.portalId = ? and o.disabled <> 1 order by o.id", portalId);
    }
    
    public ThemePersonal getPersonalTheme(Long portalId) {
        String hql = "from ThemePersonal o where o.userId = ? and o.portalId = ?";
        List<?> list = getEntities(hql, Environment.getOperatorId(), portalId);
        if( !list.isEmpty() ) {
            return (ThemePersonal) list.get(0);
        }
        return null;
    }
}