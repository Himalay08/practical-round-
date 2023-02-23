package com.inventory.controller;

import java.io.IOException;
import java.io.InputStream;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.inventory.dto.PostDto;
import com.inventory.service.FileService;
import com.inventory.service.PostService;

import jakarta.servlet.http.HttpServletResponse;



@RestController
@RequestMapping("/api/")
public class PostController {

	@Value("${project.image}")
	private String path;
	
	@Autowired
	FileService fileService;
	
	@Autowired
	PostService postService;
	
	@PostMapping("/post")
	public ResponseEntity<PostDto> uploadPostImage(
			@RequestParam("image") MultipartFile image
			) throws IOException{

	
		String fileName=null;
	
		fileName = this.fileService.uploadImage(path, image);
		PostDto postdto = new PostDto();
		postdto.setImageName(fileName);
		PostDto savePost=this.postService.savePost(postdto);
		return new ResponseEntity<PostDto>(savePost,HttpStatus.OK);
	}
	
	@GetMapping(value = "/post/image/{imageName}",produces = org.springframework.http.MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage
	      (
			@PathVariable("imageName") String imageName,
			HttpServletResponse response
			) throws IOException {
		InputStream resource=this.fileService.getResource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
	     StreamUtils.copy(resource, response.getOutputStream());
	}
}
