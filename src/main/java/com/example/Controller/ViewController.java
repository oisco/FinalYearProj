package com.example.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * Created by Oisín on 10/23/2016.
 */
@Controller
public class ViewController {

    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("username","oisin");
        model.addAttribute("datetime",new Date());
        return "index";
    }
}

