package com.example.Controller;

import com.example.Entity.Fighter;
import com.example.Services.FighterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Oisín on 10/22/2016.
 */
@RestController
@RequestMapping("/fighters")
public class FighterController {

    @Autowired
    FighterService fighterService;

    public FighterController(FighterService fighterService)
    {
        this.fighterService=fighterService;
    }

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public List<Object[]> getAll(){
        return fighterService.findAllToDisplay();
    }

    @RequestMapping(value = "/view/{id}",method = RequestMethod.GET)
    public Fighter view(@PathVariable int id) {
        return fighterService.get(id);
    }

}

