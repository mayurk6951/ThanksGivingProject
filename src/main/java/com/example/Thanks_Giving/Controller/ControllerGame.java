package com.example.Thanks_Giving.Controller;

import com.example.Thanks_Giving.Entity.Item;
import com.example.Thanks_Giving.Repository.RepositoryGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

public class ControllerGame {

    @Autowired
    RepositoryGame gamerepo;

    @PostMapping("/object/create/item")
    public Item addItem(@RequestBody Item item) { return this.gamerepo.save(item);
    }


    @DeleteMapping("/object/remove/item/{id}")
    public ResponseEntity<Item> deleteItem(@PathVariable Long id){
        if(!gamerepo.existsById(id))
        {
            return new ResponseEntity<Item>(HttpStatus.NOT_FOUND);
        }
        Item deleteitem = new Item();
        this.gamerepo.deleteById(id);
        return new ResponseEntity<Item>(deleteitem,HttpStatus.OK);
    }

    @GetMapping("object/get/itembyId/{id}")
    public ResponseEntity<Item> getItem(@PathVariable Long id){
        if(!gamerepo.existsById(id))
        {
            return new ResponseEntity<Item>(HttpStatus.NOT_FOUND);
        }
        Iterable<Item> returnitem = this.gamerepo.findAllById(Arrays.asList(id));
        return new ResponseEntity<Item>(returnitem.iterator().next(),HttpStatus.OK);

    }

    @GetMapping("/object/get/allitem")
    public Iterable<Item> getAllItems(){return this.gamerepo.findAll();}

    @GetMapping("/object/get/itembyName/{name}")
    public Iterable<Item> getAllByType(@PathVariable String name){
        return  this.gamerepo.findAllByName(name);
    }



}
