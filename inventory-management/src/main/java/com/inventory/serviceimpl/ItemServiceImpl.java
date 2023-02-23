package com.inventory.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventory.dto.ItemDto;
import com.inventory.entity.Item;
import com.inventory.exception.ResourceNotFoundException;
import com.inventory.repository.ItemRepository;
import com.inventory.service.ItemService;
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Override
	public ItemDto createItem(ItemDto itemDto) {
		Item item = this.dtoToItem(itemDto);
		Item savedItem = this.itemRepository.save(item);
		return this.ItemToDto(savedItem);
	}
	

	@Override
	public ItemDto updateItem(ItemDto itemDto, Integer itemId) {
        Item item = this.itemRepository.findById(itemId).orElseThrow(()->new ResourceNotFoundException("Item"," Id ",itemId));
		
        item.setName(itemDto.getName());
        item.setPrice(itemDto.getPrice());
        item.setQuantity(itemDto.getQuantity());

		
		Item updatedItem=this.itemRepository.save(item);
		return this.ItemToDto(updatedItem);
	}

	@Override
	public ItemDto getItemById(Integer itemId) {
		Item item=this.itemRepository.findById(itemId).orElseThrow(()->new ResourceNotFoundException("Item"," Id ",itemId));;
		return this.ItemToDto(item);
	}

	@Override
	public List<ItemDto> getAllItems() {
		List<Item> items = this.itemRepository.findAll();
		List<ItemDto> itemDtos=items.stream().map(item->this.ItemToDto(item)).collect(Collectors.toList());
		return itemDtos;
	}

	@Override
	public void deleteItem(Integer itemId) {
		Item item = this.itemRepository.findById(itemId).orElseThrow(()->new ResourceNotFoundException("item"," Id ", itemId));
		this.itemRepository.delete(item);	
	}
	
	private Item dtoToItem(ItemDto itemDto) {		
		Item item =this.modelMapper.map(itemDto,Item.class);
		return item;
	}
	
	private ItemDto ItemToDto(Item item) {
		ItemDto itemDto=this.modelMapper.map(item,ItemDto.class);
		return itemDto;
	}

}
