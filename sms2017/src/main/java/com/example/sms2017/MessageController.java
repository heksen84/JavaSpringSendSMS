package com.example.sms2017;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

	/* --- отправить сообщение --- */
	@RequestMapping(value = "/message/post", method = RequestMethod.POST )
	public String post(HttpServletRequest request){											
		return SMS.SendMessage(request.getParameter("number"), request.getParameter("text"), "");		
	}
	
	/* --- получить информацию о сообщении --- */
	@RequestMapping(value = "/message/get", method = { RequestMethod.GET } )	
	public String getMessageInfo(HttpServletRequest request){					
		return SMS.GetMessageInfo(request.getParameter("message_id"));		
	}
	
	/* --- удалить сообщение  --- */
	@RequestMapping(value = "/message/delete", method = { RequestMethod.DELETE })	
	public Map<String, Object> delete(){		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("answer", "success");
		result.put("text", "Всё в порядке!");		
		return result;		
	}
	
	/* --- отправить пачку сообщений --- */
	@RequestMapping(value = "/batches/post", method = { RequestMethod.POST } )
	public String batchPost(HttpServletRequest request){							
		return SMS.SendButchMessages(request.getParameter("data"));
	}
	
	/* --- получить информацию о пачке --- */
	@RequestMapping(value = "/batches/get", method = { RequestMethod.GET } )	
	public String getBatchInfo(HttpServletRequest request){					
		return SMS.GetBatchInfo(request.getParameter("batch_id"));		
	}
	
}