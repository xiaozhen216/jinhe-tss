package com.jinhe.tss.um.action;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jinhe.tss.framework.web.dispaly.grid.GridDataEncoder;
import com.jinhe.tss.framework.web.dispaly.xform.XFormEncoder;
import com.jinhe.tss.framework.web.mvc.BaseActionSupport;
import com.jinhe.tss.um.UMConstants;
import com.jinhe.tss.um.entity.Message;
import com.jinhe.tss.um.service.IMessageService;
 
@Controller
@RequestMapping("/auth/message")
public class MessageAction extends BaseActionSupport {

	private static final String XFORM_URI = "template/xform/message.xml";
	private static final String GRID_URI  = "template/grid/messageGrid.xml";
	
	@Autowired private IMessageService service;
	
	@RequestMapping(method = RequestMethod.PUT)
	public void sendMessage(HttpServletResponse response, Message message) {
		service.sendMessage(message);
		printSuccessMessage("发送成功!");
	}
	
	@RequestMapping("/{id}")
	public void viewMessage(HttpServletResponse response, @PathVariable("id") Long id) {
		if(UMConstants.DEFAULT_NEW_ID.equals(id)) {
			print("MessageInfo", new XFormEncoder(XFORM_URI));
		} else {
			print("MessageInfo", new XFormEncoder(XFORM_URI, service.viewMessage(id)));
		}
		
	}
	
    @RequestMapping("/reply/{id}")
    public void getMessage4Reply(HttpServletResponse response, @PathVariable("id") Long id) {
        Message message = service.viewMessage(id);
        Message newMessage = new Message();
        newMessage.setReceiverId(message.getSenderId());
        newMessage.setReceiver(message.getSender());
        newMessage.setTitle("Re: " + message.getTitle());
        message = newMessage;

        XFormEncoder messagerEncoder = new XFormEncoder(XFORM_URI, message);
        print("ReplyInfo", messagerEncoder);
    }
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteMessage(HttpServletResponse response, @PathVariable("id") Long id) {
		service.deleteMessage(id);
		printSuccessMessage("删除成功!");
	}
 
	@RequestMapping("/list/{boxType}")
	public void getMessageList(HttpServletResponse response, @PathVariable("boxType") int boxType) {
		List<?> messages = null;
		switch (boxType) { // 信箱類型，分收件箱、發件箱
    		case 1:	break;
    		case 2:	messages = service.getInboxList(); break;
    		case 4: messages = service.getOutboxList(); break;
    		default: break;
		}
		
		print("MessageList", new GridDataEncoder(messages, GRID_URI));
	}
}
