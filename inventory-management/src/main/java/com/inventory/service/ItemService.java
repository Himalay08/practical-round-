package com.inventory.service;

import java.util.List;


import com.inventory.dto.ItemDto;

public interface ItemService {

	
	ItemDto createItem(ItemDto itemDto);
	
	ItemDto updateItem(ItemDto itemDto,Integer itemId);
	
	ItemDto getItemById(Integer itemId);
	
	List<ItemDto> getAllItems();
	
	void deleteItem(Integer itemId);
}
