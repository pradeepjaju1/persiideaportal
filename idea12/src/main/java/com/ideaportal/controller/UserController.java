package com.ideaportal.controller;


import java.util.List;





import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.ideaportal.datainteraction.DataInteract;
import com.ideaportal.datainteraction.UserInteract;
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
import com.ideaportal.models.ThemesCategory;
import com.ideaportal.models.User;
import com.ideaportal.services.UserService;

@RestController
@RequestMapping(value = "/api")
public class UserController {

	
	 
	final ObjectMapper objectMapper = new ObjectMapper();
	
	private static final Logger LOG = LoggerFactory.getLogger(ClientPartnerController.class);
	
	@Autowired
	  ModelMapper modelMapper;
	@Autowired
	    UserInteract userinteract;
	 @Autowired
	  UserService us;
	    @Autowired
	    DataInteract datainteract;
	
	@PostMapping(value = "/signup/register")
	public ResponseEntity<Rp<User>> Usercreated(@RequestBody UserData userdata)
	{
		LOG.info("Request URL: POST Signup");
         User userDetails = modelMapper.map(userdata, User.class); 
         LOG.info("Control to us.registerUser");
        Rp<User> rpm = us.registerUser(userDetails);
        LOG.info("Control back to User Controller");
        LOG.info("User Signup successfully");
        return new ResponseEntity<>(rpm, HttpStatus.valueOf(rpm.getStatus()));
    }
	
	@PostMapping(value = "signup/login")
	public ResponseEntity<Rp<User>> loginUser(@RequestBody Login userDetails) throws UserAuthException 
	{
		LOG.info("Request URL: POST Login");
		LOG.info("Control to us.check");
        Rp<User> rpm = us.check(userDetails);
        LOG.info("Control back to User Controller");
        LOG.info("User Logged in successfully");
       return	 new 	ResponseEntity<>(rpm, HttpStatus.valueOf(rpm.getStatus()));

    }

	  @GetMapping(value="idea/{ideaID}")
	    public ResponseEntity<Rp<Ideas>> getIdeaByID(@PathVariable ("ideaID") long ideaID) throws Exception {
		  LOG.info("Request URL: GET particular Idea");
		  LOG.info("Control to us.getIdeaByIDResponseMessage");
		  Rp<Ideas> rpm=us.getIdeaByIDResponseMessage(ideaID);
		  LOG.info("Control back to User Controller");
	        LOG.info("Particular Idea returned successfully");
	        
		  return 	new		 ResponseEntity<>(rpm, HttpStatus.valueOf(rpm.getStatus()));
	        
	        
	  }
	  @GetMapping(value = "signup/login/themes/{themeID}/ideas/")
	    public ResponseEntity<Rp<List<Ideas>>> getIdeasByTheme(@PathVariable("themeID") long themeID) 
	    {
		  LOG.info("Request URL: GET all Idea for a Theme");
		  LOG.info("Control to us.getIdeasBythemeid");
	        Rp<List<Ideas>> responseMessage = us.getIdeasBythemeid(themeID);
	        LOG.info("Control back to User Controller");
	        LOG.info("All Idea for a theme returned successfully");
	        return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(responseMessage.getStatus()));
	    }
	
    
    @GetMapping(value="/themes")
    public ResponseEntity<Rp<List<Themes>>> getAllThemes()
    {
    	 LOG.info("Request URL: GET all Themes");
    	 LOG.info("Control to us.getAllThemesResponseMessage");
        Rp<List<Themes>> rpm = us.getAllThemesResponseMessage();
        LOG.info("Control back to User Controller");
        LOG.info("All themes returned successfully");
        return new ResponseEntity<>(rpm, HttpStatus.valueOf(rpm.getStatus()));
    }
    
    //Function to get a theme by id
    @GetMapping(value = "/themes/{id}")
    public ResponseEntity<Rp<Themes>> getThemeByID(@PathVariable("id")String themeID) throws Exception {

	    Themes themes;
	    LOG.info("Request URL: GET particular Themes");
   	 LOG.info("Control to datainteract.findThemeByID");
	    themes = datainteract.findThemeByID(Long.parseLong(themeID));
	    LOG.info("Control back to User Controller");
        
        Rp<Themes> rpm = new Rp<>();

        if(themes==null) {
        	LOG.error("No themes present Exception");
            throw new Exception("No themes present");
        }
	    else 
	    {
	    	LOG.info("All themes returned successfully");
            rpm.setResult(themes);
            rpm.setStatus(HttpStatus.OK.value());
           rpm.setStatusText("Theme sent successfully");
            rpm.setTotalElements(1);
        }

        return new ResponseEntity<>(rpm, HttpStatus.valueOf(rpm.getStatus()));
    }
    @PutMapping(value="/user/idea/like")
    public ResponseEntity<Rp<Likes>> likeIdea(@RequestBody LikesData likesdata)throws Exception {
            
    	LOG.info("Request URL: PUT Like on a Idea");
        Likes likes= modelMapper.map(likesdata, Likes.class);
    	
        LOG.info("Control to datainteract.IdeaValid");
    	Ideas idea=datainteract.IdeaValid(likes.getIdea().getIdeaId());
    	LOG.info("Control back to User Controller");
    	LOG.info("Control to datainteract.findUser");
    	User user=datainteract.findUser(likes.getUser().getUserId());
    	LOG.info("Control back to User Controller");
      	if(user==null) {
      		LOG.error("User not found Exception");
            throw new Exception("User Not Found, Please try again!");
        }
    	if(idea==null) {
    		LOG.error("Idea not found Exception");
            throw new Exception("Invalid Idea Id");
    	}
    	LOG.info("Control to us.likeAnIdeaResponseMessage");
        Rp<Likes> rpm = us.likeAnIdeaResponseMessage(likes);
        LOG.info("Control back to User Controller");
      
        LOG.info("Particular Idea Liked ");
    	return new ResponseEntity<>(rpm, HttpStatus.valueOf(rpm.getStatus()));
    }
    
    //Function to support that user can comment an idea
    @PostMapping(value="/user/idea/comment")
    public ResponseEntity<Rp<Comments>> commentidea(@RequestBody CommentsData commentsData) throws
            Exception
    {
    	LOG.info("Request URL: Post Comment on a Idea");
        Comments comment = modelMapper.map(commentsData, Comments.class);
        LOG.info("Control to datainteract.findUser");
    	User user=datainteract.findUser(comment.getUser().getUserId());
    	LOG.info("Control back to User Controller");
    	LOG.info("Control to datainteract.IdeaValid");
    	Ideas idea=datainteract.IdeaValid(comment.getIdea().getIdeaId());
    	LOG.info("Control back to User Controller");
    	if(user==null) {
    		LOG.error("User Not found Exception");
            throw new UserNotFoundException("User Not Found, Please try again!");
        }
    	if(idea==null) {
    		LOG.error("Idea Not found Exception");
            throw new Exception("Invalid Idea Id");
        }
    	LOG.info("Control to us.commentAnIdeaResponseMessage");
        Rp<Comments> responseMessage = us.commentAnIdeaResponseMessage(comment);
        LOG.info("Control back to User Controller");
        LOG.info("Comment on Idea done successfully");
    	return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(responseMessage.getStatus()));
    	
    }
    //Function to get a  list of comments for an idea
    @GetMapping(value="idea/{ideaID}/comments")
    public ResponseEntity<Rp<List<Comments>>> getComments(@PathVariable ("ideaID") long ideaID) throws Exception
    {
    	LOG.info("Request URL: GET all Comment for an Idea");
    	LOG.info("Control to datainteract.IdeaValid");
    	Ideas idea=datainteract.IdeaValid(ideaID);
    	 LOG.info("Control back to User Controller");
    	if(idea==null) {
    		LOG.error("Idea is invalid exception");
            throw new Exception("Invalid Idea ID");
        }
    	LOG.info("Control to us.getCommentForIdeaResponseMessage");
        Rp<List<Comments>> rpm = us.getCommentForIdeaResponseMessage(ideaID);
        LOG.info("Control back to User Controller");
        LOG.info("All comment for an idea returned");
    	return new ResponseEntity<>(rpm, HttpStatus.valueOf(rpm.getStatus()));
    }
    @GetMapping(value = "idea/{ideaID}/likes")
    public ResponseEntity<Rp<List<User>>> getLikes(@PathVariable ("ideaID") long ideaID) throws Exception
    {
    	LOG.info("Request URL: GET all Likes for an Idea");
    	LOG.info("Control to datainteract.IdeaValid");
    	Ideas idea=datainteract.IdeaValid(ideaID);
     	 LOG.info("Control back to User Controller");
    	if(idea==null) {
    	    LOG.error("Idea not found Exception");
            throw new Exception("IDEA_NOT_FOUND");
        }
        
    	LOG.info("Control to us.getLikesForIdeaResponseMessage");
        Rp<List<User>> rpm = us.getLikesForIdeaResponseMessage(ideaID);
        LOG.info("Control back to User Controller");
        LOG.info("All likes for an idea returned");
    	return new ResponseEntity<>(rpm, HttpStatus.valueOf(rpm.getStatus()));
    }
    @GetMapping(value = "idea/{ideaID}/dislikes")
    public ResponseEntity<Rp<List<User>>> getDislikesForIdea(@PathVariable ("ideaID") long ideaID) throws Exception
    {
    	LOG.info("Request URL: GET all dislikes for an Idea");
    	LOG.info("Control to datainteract.IdeaValid");
    	Ideas idea=datainteract.IdeaValid(ideaID);
    	if(idea==null) {
    		LOG.error("Idea not found Exception");
            throw new Exception("Idea not found");
        }
       
    	LOG.info("Control to us.getDisLikesForIdeaResponseMessage");
        Rp<List<User>> responseMessage = us.getDislikesForIdeaResponseMessage(ideaID);
        LOG.info("Control back to User Controller");
        LOG.info("All dislikes for an idea returned");
    	return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(responseMessage.getStatus()));
    }
    @PostMapping(path="user/theme/themecategory")
    public ResponseEntity<Rp<ThemesCategory>> createThemeCategory(@RequestBody ThemesCategory category) throws Exception
    {
    	LOG.info("Request URL: POST create theme category");
    	LOG.info("Control to datainteract.isCategoryPresent");
 	   int id=datainteract.isCategoryPresent(category.getThemeCategoryName());
 	   
 	   if(id==1) {
 		  LOG.error("Category Already Present");
 		   throw new Exception("Category Already Present");
 	   }
 	   LOG.info("Control to userinteract.addCategory");
 	   Rp<ThemesCategory> responseMessage=userinteract.addCategory(category);
 	  LOG.info("Control back to User Controller");
 	  LOG.info("New Theme Category Created");
 	   return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(responseMessage.getStatus()));
    }
    
    @GetMapping(path="theme/all/themecategory")
    public ResponseEntity<Rp<List<ThemesCategory>>> showThemeCategory()
    {
    	LOG.info("Request URL: GET all theme category");
    	LOG.info("Control to us.getCategories");
 	   Rp<List<ThemesCategory>> responseMessage = us.getCategories();
 	  LOG.info("Control back to User Controller");
 	  LOG.info("All  Theme Category returned");
    	return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(responseMessage.getStatus()));
    }
    
    @GetMapping(value="/themes/{themeID}/ideas/mostlikes")
    public ResponseEntity<Rp<List<Ideas>>> getIdeasByMostLikesForTheme(@PathVariable("themeID") long themeID) throws Exception
    {
    	LOG.info("Request URL: GET sort all ideas according to likes");
    	LOG.info("Control to datainteract.findThemeByID");
    	Themes theme=datainteract.findThemeByID(themeID);
    	LOG.info("Control back to User Controller");
    	if(theme==null) {
    		LOG.error("Theme not found");
            throw new Exception("THEME_NOT_FOUND");
        }
    	LOG.info("Control to us.getIdeasByMostLikesResponseMessage");
    	Rp<List<Ideas>> responseMessage = us.getIdeasByMostLikesResponseMessage(themeID);
    	LOG.info("All ideas returned sorted by most likes");
    	return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(responseMessage.getStatus()));
    }
    
    @GetMapping(value="/themes/{themeID}/ideas/mostcomments")
    public ResponseEntity<Rp<List<Ideas>>> getIdeasByMostCommentsForTheme(@PathVariable ("themeID") long themeID) throws Exception
    {
    	LOG.info("Request URL: GET sort all ideas according to most commented");
    	LOG.info("Control to datainteract.findThemeByID");
        Themes theme=datainteract.findThemeByID(themeID);
        LOG.info("Control back to User Controller");
        if(theme==null) {
        	LOG.error("Theme not found");
            throw new Exception("THEME_NOT_FOUND");
        }
        LOG.info("Control to us.getIdeasByMostCommentsResponseMessage");
        Rp<List<Ideas>> responseMessage = us.getIdeasByMostCommentsResponseMessage(themeID);
        LOG.info("All ideas returned sorted by most comment");
        return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(responseMessage.getStatus()));
    }
    
  //Function to update user password
    @PutMapping(value = {"/user/profile/update/password", "/login/savePassword"})
    public ResponseEntity<Rp<User>> updateUserPassword(@RequestBody UserData userData)
    {
    	LOG.info("Request URL: PUT update User Password");
        User userDetail = modelMapper.map(userData, User.class);
        LOG.info("Control to us.saveUserPasswordResponseMessage");
    	Rp<User> responseMessage=us.saveUserPasswordResponseMessage(userDetail);
    	LOG.info("Control back to User Controller");
    	LOG.info("User Password updated successfully");
    	return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(responseMessage.getStatus()));
    }

}
