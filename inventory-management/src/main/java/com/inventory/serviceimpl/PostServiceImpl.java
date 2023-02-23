package com.inventory.serviceimpl;

import java.util.Date; 
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.inventory.dto.PostDto;
import com.inventory.entity.Post;
import com.inventory.repository.PostRepository;
import com.inventory.service.PostService;



@Service
public class PostServiceImpl implements PostService{
	
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private ModelMapper modelMapper;


	@Override
	public PostDto savePost(PostDto postDto) {
		Post post=new Post();
		post.setImageName(postDto.getImageName());
		Post updatedPost=this.postRepository.save(post);
		return this.modelMapper.map(updatedPost,PostDto.class);
	}

	

}
