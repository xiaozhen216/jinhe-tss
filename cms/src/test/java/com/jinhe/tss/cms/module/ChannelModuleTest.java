package com.jinhe.tss.cms.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.jinhe.tss.cms.AbstractTestSupport;
import com.jinhe.tss.cms.CMSConstants;
import com.jinhe.tss.cms.entity.Channel;
import com.jinhe.tss.framework.test.TestUtil;
import com.jinhe.tss.um.UMConstants;

/**
 * 文章站点栏目相关模块的单元测试。
 */
public class ChannelModuleTest extends AbstractTestSupport {
 
	@Test
    public void testChannelModule() {
        // 新建站点
        Channel site = new Channel();
        site.setName("我的门户" + System.currentTimeMillis());
        site.setPath(super.tempDir1.getPath());
        site.setDocPath("doc");
        site.setImagePath("img");
        
        channelAction.saveSite(response, site);
        Long siteId = site.getId();
        assertNotNull(siteId);
        
        channelAction.saveSite(response, site);
        
        channelAction.getSiteDetail(response, CMSConstants.DEFAULT_NEW_ID);
        channelAction.getSiteDetail(response, siteId);
        
        // 新建栏目
        channelAction.getChannelDetail(response, CMSConstants.DEFAULT_NEW_ID, siteId);
        
        Channel channel1 = new Channel();
        channel1.setName("时事评论");
        channel1.setParentId(siteId);
        channelAction.saveChannel(response, channel1);
        Long channelId = channel1.getId();
        assertNotNull(channelId);
        
        channelAction.getChannelDetail(response, channelId, channel1.getParentId());
        
        channelAction.saveChannel(response, channel1); // update
        
        Channel channel2 = super.createChannel("体育新闻", channel1, siteId);
        Channel channel3 = super.createChannel("NBA战况", channel2, channel2.getId());
        
        List<?> list = channelService.getAllSiteChannelList();
        assertTrue(list.size() >= 3);
        for(Object temp : list) {
            log.debug(temp);
        }
        
        // 栏目排序
        channelAction.sortChannel(response, channelId, channel2.getId(), 1);
        log.debug(channel1);
        log.debug(channel2);
        assertTrue(channel1.getSeqNo() > channel2.getSeqNo());
        
        // 栏目移动
        channelAction.moveChannel(response, channel3.getId(), channelId);
        assertTrue(channel3.getDecode().startsWith(channel1.getDecode()));
        assertEquals(channel3.getParentId(), channelId);
        
        // 停用启用
        channelAction.disable(response, siteId);
        assertEquals(site.getDisabled(), UMConstants.TRUE);
        assertEquals(channel1.getDisabled(), UMConstants.TRUE);
        
        channelAction.enable(response, siteId);
        assertEquals(site.getDisabled(), UMConstants.FALSE);
        assertEquals(channel1.getDisabled(), UMConstants.FALSE);
        
        channelAction.disable(response, channelId);
        assertEquals(channel1.getDisabled(), UMConstants.TRUE);
        
        channelAction.enable(response, channel3.getId());
        assertEquals(channel1.getDisabled(), UMConstants.FALSE);
        assertEquals(channel3.getDisabled(), UMConstants.FALSE);
        
        channelAction.getOperations(response, channelId);
        
        channelAction.getChannelAll(response);
        
        // 栏目站点删除
        channelAction.delete(response, channel2.getId());
        channelAction.delete(response, siteId);
        
        list = channelDao.getEntities(" from Channel where site.id = ?", siteId);
        assertTrue(list.size() == 0);
        
        assertTrue(TestUtil.printLogs(logService) > 0);
    }
    
}
