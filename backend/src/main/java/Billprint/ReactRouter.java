package Billprint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class ReactRouter {
  @RequestMapping(value = "/**/{[path:[^\\.]*}")
        public String forwardToRouteUrl() {
            return "forward:/";
        }

}
