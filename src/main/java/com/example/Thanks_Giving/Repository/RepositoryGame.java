package com.example.Thanks_Giving.Repository;

import com.example.Thanks_Giving.Entity.Item;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RepositoryGame extends CrudRepository <Item,Long> {

    List<Item> findAllByName(String name);
}
