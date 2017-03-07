package com.example.Controller;

import com.example.Entity.Fighter;
import com.example.Services.FighterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Oisín on 10/22/2016.
 */
@RestController
@RequestMapping("/fighters")
public class FighterController {

    @Autowired
    FighterService fighterService;

   // @Autowired

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



    /*
    @RequestMapping(value = "/IDS/{id}",method = RequestMethod.GET)
    public List<Fighter> tester(@PathVariable int id){
    return fighterService.findByIdLessThan(id);
    }


    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public List<Fighter> create(@RequestBody Fighter fighter) {
    fighterService.save(fighter);
        return fighterService.findAll();
    }

    @RequestMapping(value = "/delete/{id}",method = RequestMethod.POST)
    public List<Fighter> delete(@PathVariable int id) {
        fighterService.delete(id);
        return fighterService.findAll();
    }


*/
}

