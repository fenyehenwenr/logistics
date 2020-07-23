package swd.logistics.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@Controller
@RequestMapping("/")
public class index {

    @RequestMapping("/")
    public ModelAndView index(){
        ModelAndView view = new ModelAndView("index");

        return view;
    }
}
