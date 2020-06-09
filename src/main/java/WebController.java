import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    @RequestMapping({ "/", "index"})
    public String start () {
        return "index";
    }
    @RequestMapping ("/private")
    public String priVate () {
        return "priVate";
    }
    @RequestMapping ("/public")
    public String loginpub () {
        return "public" ;
    }
    @RequestMapping ("/admin")
    public String admin () {
        return "admin";
    }
    @RequestMapping ("/login")
    public String login () {
        return "login";
    }
}
