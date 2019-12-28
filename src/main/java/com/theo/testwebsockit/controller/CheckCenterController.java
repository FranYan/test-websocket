package com.theo.testwebsockit.controller;

import com.theo.testwebsockit.server.WebSocketServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/checkcenter")
public class CheckCenterController
{

        @GetMapping("/socket/{cid}")
        public String socket(@PathVariable String cid)
        {
                return cid;
        }
        @RequestMapping("/socket/push/{cid}")
        public Map pushToWeb(@PathVariable String cid,String message)
        {

                Map<String,Object> param = new HashMap<>();

                try
                {
                        WebSocketServer.sendInfo(message,cid);
                }catch (IOException e){
                        e.printStackTrace();
                        return (Map) param.put("cid",e.getMessage());
                }
                return (Map) param.put("success",cid);

        }

}
