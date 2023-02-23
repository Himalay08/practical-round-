package com.inventory.controller;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.dto.ItemDto;
import com.inventory.service.ItemService;
import com.inventory.utils.ApiResponse;


@RestController
@RequestMapping("/api/items")
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	
	@PostMapping("/")
	public ResponseEntity<ItemDto> createUser(@RequestBody ItemDto itemDto){
		ItemDto createItemDto=this.itemService.createItem(itemDto);
		return new ResponseEntity<ItemDto>(createItemDto,HttpStatus.CREATED);
	}
	
	@PutMapping("/{itemId}")
	public ResponseEntity<ItemDto> updateUser(@RequestBody ItemDto itemDto,@PathVariable("itemId") Integer itemId){
		ItemDto updatedItem=this.itemService.updateItem(itemDto, itemId);
		return ResponseEntity.ok(updatedItem);
	}
	
	@DeleteMapping("/{itemId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable("itemId") Integer itemId){
		this.itemService.deleteItem(itemId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("item Deleted Successfully",true),HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<ItemDto>> getAllUsers(){
		return ResponseEntity.ok(this.itemService.getAllItems());
	}
	
	@GetMapping("/{itemId}")
	public ResponseEntity<ItemDto> getSingleUser(@PathVariable("itemId") Integer itemId){
		return ResponseEntity.ok(this.itemService.getItemById(itemId));
	}
}
