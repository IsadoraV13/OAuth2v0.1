package com.OAuth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    @RequestMapping({ "/", "index"})
    public String start () {
        return "index";
    }
    @RequestMapping ("/webprivate")
    public String priVate () {
        return "priVate";
    }
    @RequestMapping ("/webpublic")
    public String loginpub () {
        return "public" ;
    }
    @RequestMapping ("/webadmin")
    public String admin () {
        return "admin";
    }
    @RequestMapping ("/login")
    public String login () {
        return "login";
    }
}
