package com.ideaportal.controller;


import java.util.List;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ideaportal.dao.DaoUtils;
import com.ideaportal.dao.UserDAO;

import com.ideaportal.dto.CommentsData;
import com.ideaportal.dto.LikesData;
import com.ideaportal.dto.UserData;
import com.ideaportal.exception.UserAuthException;
import com.ideaportal.exception.UserNotFoundException;
import com.ideaportal.models.Comments;
import com.ideaportal.models.Ideas;
import com.ideaportal.models.Likes;
import com.ideaportal.models.Login;

import com.ideaportal.models.Rp;
import com.ideaportal.models.Themes;
import com.ideaportal.models.User;
import com.ideaportal.services.UserService;

@RestController
@RequestMapping(value = "/api")
public class UserController {

	
	 
	final ObjectMapper objectMapper = new ObjectMapper();
	
	
	
	@Autowired
	  ModelMapper modelMapper;
	@Autowired
	    UserDAO userDAO;
	 @Autowired
	  UserService us;
	    @Autowired
	      DaoUtils utils;
	
	@PostMapping(value = "/signup/register")
	public ResponseEntity<Rp<User>> Usercreated(@RequestBody UserData userdata)
	{
        
         User userDetails = modelMapper.map(userdata, User.class);
         
         
         
        Rp<User> rpm = us.registerUser(userDetails);
        
        
        
        
        return new ResponseEntity<>(rpm, HttpStatus.valueOf(rpm.getStatus()));
    }
	
	@PostMapping(value = "signup/login")
	public ResponseEntity<Rp<User>> loginUser(@RequestBody Login userDetails) throws UserAuthException 
	{
	   
        Rp<User> rpm = us.check(userDetails);
       return	 new 	ResponseEntity<>(rpm, HttpStatus.valueOf(rpm.getStatus()));

    }

	  @GetMapping(value="idea/{ideaID}")
	    public ResponseEntity<Rp<Ideas>> getIdeaByID(@PathVariable ("ideaID") long ideaID) throws Exception {
		  
		  Rp<Ideas> rpm=us.getIdeaByIDResponseMessage(ideaID);

	        
		  return 	new		 ResponseEntity<>(rpm, HttpStatus.valueOf(rpm.getStatus()));
	        
	        
	  }
	  @GetMapping(value = "signup/login/themes/{themeID}/ideas/")
	    public ResponseEntity<Rp<List<Ideas>>> getIdeasByTheme(@PathVariable("themeID") long themeID) 
	    {

	        Rp<List<Ideas>> responseMessage = us.getIdeasBythemeid(themeID);
	        return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(responseMessage.getStatus()));
	    }
	
    
    @GetMapping(value="/themes")
    public ResponseEntity<Rp<List<Themes>>> getAllThemes()
    {

        Rp<List<Themes>> rpm = us.getAllThemesResponseMessage();
        
        return new ResponseEntity<>(rpm, HttpStatus.valueOf(rpm.getStatus()));
    }
    
    //Function to get a theme by id
    @GetMapping(value = "/themes/{id}")
    public ResponseEntity<Rp<Themes>> getThemeByID(@PathVariable("id")String themeID) throws Exception {

	    Themes themes;
	    
	    themes = utils.findThemeByID(Long.parseLong(themeID));

        Rp<Themes> rpm = new Rp<>();

        if(themes==null) {
            throw new Exception("No themes present");
        }
	    else 
	    {
            rpm.setResult(themes);
            rpm.setStatus(HttpStatus.OK.value());
           rpm.setStatusText("Theme sent successfully");
            rpm.setTotalElements(1);
        }

        return new ResponseEntity<>(rpm, HttpStatus.valueOf(rpm.getStatus()));
    }
    @PutMapping(value="/user/idea/like")
    public ResponseEntity<Rp<Likes>> likeIdea(@RequestBody LikesData likesdata)throws Exception {
            

        Likes likes= modelMapper.map(likesdata, Likes.class);
    	
    	
    	Ideas idea=utils.IdeaValid(likes.getIdea().getIdeaId());
    	
    	User user=utils.findUser(likes.getUser().getUserId());
      	if(user==null) {
            throw new Exception("User Not Found, Please try again!");
        }
    	if(idea==null) {
            throw new Exception("Invalid Idea Id");
    	}
    	
        Rp<Likes> rpm = us.likeAnIdeaResponseMessage(likes);

      

    	return new ResponseEntity<>(rpm, HttpStatus.valueOf(rpm.getStatus()));
    }
    
    //Function to support that user can comment an idea
    @PostMapping(value="/user/idea/comment")
    public ResponseEntity<Rp<Comments>> commentidea(@RequestBody CommentsData commentsData) throws
            Exception
    {
        Comments comment = modelMapper.map(commentsData, Comments.class);
    	User user=utils.findUser(comment.getUser().getUserId());
    	Ideas idea=utils.IdeaValid(comment.getIdea().getIdeaId());
    	if(user==null) {
            throw new UserNotFoundException("User Not Found, Please try again!");
        }
    	if(idea==null) {
            throw new Exception("Invalid Idea Id");
        }
        Rp<Comments> responseMessage = us.commentAnIdeaResponseMessage(comment);

    	return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(responseMessage.getStatus()));
    	
    }
    //Function to get a  list of comments for an idea
    @GetMapping(value="idea/{ideaID}/comments")
    public ResponseEntity<Rp<List<Comments>>> getComments(@PathVariable ("ideaID") long ideaID) throws Exception
    {
        
    	Ideas idea=utils.IdeaValid(ideaID);
    	if(idea==null) {
            throw new Exception("Invalid Idea ID");
        }

        Rp<List<Comments>> rpm = us.getCommentForIdeaResponseMessage(ideaID);


    	return new ResponseEntity<>(rpm, HttpStatus.valueOf(rpm.getStatus()));
    }
    @GetMapping(value = "idea/{ideaID}/likes")
    public ResponseEntity<Rp<List<User>>> getLikes(@PathVariable ("ideaID") long ideaID) throws Exception
    {
        
    	Ideas idea=utils.IdeaValid(ideaID);
    	if(idea==null) {
    	    
            throw new Exception("IDEA_NOT_FOUND");
        }
        

        Rp<List<User>> rpm = us.getLikesForIdeaResponseMessage(ideaID);

       

    	return new ResponseEntity<>(rpm, HttpStatus.valueOf(rpm.getStatus()));
    }
    

}
