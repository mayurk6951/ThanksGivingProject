package com.example.Thanks_Giving;

import com.example.Thanks_Giving.Entity.Item;
import com.example.Thanks_Giving.Repository.RepositoryGame;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ThanksGivingApplicationTests {

	@Autowired private MockMvc	mockmvc;

	@Autowired private ObjectMapper mapper;

	@MockBean RepositoryGame gamerepo;

	List<Item> items;
	List<Item> many_items;
	@Before
	public void initiation() {
		items = Arrays.asList(new Item(1,"Knife"),
				new Item(2,"Sword"),
				new Item(3,"Gun"),
				new Item(4,"AK47"),
				new Item(5,"Sword"));

		many_items =Arrays.asList(new Item(1,"Sword"),
				new Item(2,"Sword"));
	}



	@Test

	public void addItem_test() throws Exception {
		Item item = new Item(1,"Knife");
		when(gamerepo.save(item)).thenReturn(item);


		String json = mapper.writeValueAsString(item);
		System.out.println(json);
		mockmvc.perform(MockMvcRequestBuilders.post("/object/create/item")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isOk());

		Mockito.verify(gamerepo, times(1)).save(isA(Item.class));
		verifyNoMoreInteractions(gamerepo);
	}

	@Test
	public void removeItem_test() throws Exception {
		if(gamerepo.existsById(2L)) {
			when(gamerepo.existsById(2L)).thenReturn(true);

			mockmvc.perform(MockMvcRequestBuilders
					.delete("/object/remove/item/2"))
					.andExpect(status().isOk())
					.andDo(print());

			Mockito.verify(gamerepo, times(1)).existsById(2L);
			Mockito.verify(gamerepo, times(1)).deleteById(2L);
			verifyNoMoreInteractions(gamerepo);
		}
		else
		{
			when(gamerepo.existsById(7L)).thenReturn(false);


			mockmvc.perform(MockMvcRequestBuilders
					.delete("/object/remove/item/7"))
					.andExpect(status().isNotFound())
					.andDo(print());

			Mockito.verify(gamerepo, times(1)).existsById(7L);
			Mockito.verify(gamerepo, times(0)).deleteById(7L);
			verifyNoMoreInteractions(gamerepo);
		}
	}

	@Test
	public void getItembyId_test() throws Exception {
		if(gamerepo.existsById(1L)) {
			when(gamerepo.findById(1L))
					.thenReturn(Optional.ofNullable(items.get(1)));
			mockmvc.perform(MockMvcRequestBuilders.get("/object/get/itembyId/1"))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(jsonPath("$.name", instanceOf(String.class)))
					.andExpect(jsonPath("$.*", hasSize(2)));

			Mockito.verify(gamerepo, times(1)).findById(1L);
			verifyNoMoreInteractions(gamerepo);
		}
		else
		{
			when(gamerepo.findById(7L))
					.thenReturn(null);
			mockmvc.perform(MockMvcRequestBuilders.get("/object/get/itembyId/7"))
					.andExpect(status().isNotFound());
			Mockito.verify(gamerepo, times(0)).findById(7L);

		}

	}

	@Test
	public void getAllItems_test() throws Exception{
		when(gamerepo.findAll()).thenReturn(items);
		mockmvc.perform(MockMvcRequestBuilders.get("/object/get/allitem"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(5)));

		Mockito.verify(gamerepo, times(1)).findAll();
		verifyNoMoreInteractions(gamerepo);

	}


	@Test
	public void getAllItems_Byname() throws Exception {
		when(gamerepo.findAllByName("Sword")).thenReturn((List<Item>) many_items.iterator());

		mockmvc.perform(MockMvcRequestBuilders.get("\"/object/get/itembyName/Sword"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$",hasSize(2)));

		Mockito.verify(gamerepo,times(1)).findAllByName("Sword");
		verifyNoMoreInteractions(gamerepo);
	}


	public void contextLoads() {
	}

}
