package com.ideaportal.services;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ideaportal.dao.DaoUtils;
import com.ideaportal.dao.UserDAO;
import com.ideaportal.exception.InvalidRoleException;
import com.ideaportal.exception.UserAuthException;
import com.ideaportal.models.Comments;
import com.ideaportal.models.Ideas;
import com.ideaportal.models.Likes;
import com.ideaportal.models.Login;
import com.ideaportal.models.Rp;
import com.ideaportal.models.Themes;
import com.ideaportal.models.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
@Service
public class UserService {
	
	@Autowired
	DaoUtils utils;
	@Autowired
	UserDAO userDAO;
	
	@Value("${ideaportal.jwt.secret-key}")
	public String jwtSecretKey;

	@Value("${ideaportal.jwt.expiration-time}")
	public long jwtExpirationTime;
	
	
	  public String generateJWT(User user) 
	    {
	        long timestamp = System.currentTimeMillis(); //current time in milliseconds
	        
	        return       Jwts.builder().signWith(SignatureAlgorithm.HS256, jwtSecretKey)
	                   .setIssuedAt(new Date(timestamp))
	                 .setExpiration(new Date(timestamp + jwtExpirationTime))
					.claim("user", user)
	                .compact(); //builds the token
	    }
	public boolean saveFile(final MultipartFile file, final File dir) {
        final String filename = file.getOriginalFilename();
        final String path = dir + File.separator + filename;
        final Path filePath = Paths.get(path, new String[0]);
        try {
            final InputStream fileInputStream = file.getInputStream();
            Files.copy(fileInputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        }
        catch (IOException e) {
           
            return false;
        }
    }
	 public Rp<User> registerUser(User userDetails)throws InvalidRoleException, IllegalArgumentException
	    {
	    

	        int emailCount = utils.getCountByEmail(userDetails.getUserEmailId());	//checks whether email is already registered or not
	        int userNameCount=utils.getCountByUserName(userDetails.getUserName()); //	checks whether user name is already registered or not
	        
	        Rp<User> rpm=new Rp<>();
	        
	        if(userNameCount>0) {
	        	
	        	throw new UserAuthException("User Name already is in use");
			}

	        if (emailCount > 0) {
				
	        	throw new UserAuthException("User Email already in use");
			}
	        
	      
	        User user = userDAO.saveUser(userDetails);

	        rpm.setResult(user);			//Returns the user object that is saved in the database
	        rpm.setStatus(HttpStatus.CREATED.value());
	        rpm.setStatusText("signup Sucessfully");
	        rpm.setToken(generateJWT(user));			//Passes the generated JWT
	        rpm.setTotalElements(1);
	        return rpm;
	        		
	    }
	 public Rp<User> check(Login userDetails) 
	    {
	    	User user=userDAO.isLoginCredentialsValid(userDetails);		//Checks whether valid credentials are passes or not
	    	
	    	if(user==null) {
	    		
				throw new UserAuthException("Invalid credentials");
			}
	        Rp<User> responseMessage =new Rp<>();
	        
	        responseMessage.setResult(user);		//Returns the object retrieved from the database
	        responseMessage.setStatus(HttpStatus.OK.value());
	        responseMessage.setStatusText("login Sucessfully");
	        responseMessage.setToken(generateJWT(user));				//Generates JWT
	        
	        return responseMessage;
	    }
	 public Rp<Ideas> getIdeaByIDResponseMessage(long ideaID) 
		{
			Rp<Ideas> rpm=new Rp<>();
			
			Ideas idea=userDAO.getIdea(ideaID);
			if(idea==null)
			{
				rpm.setResult(null);
				rpm.setStatus(HttpStatus.NOT_FOUND.value());
				rpm.setStatusText("Idea not found");

			}
			else
			{
				rpm.setResult(idea);
				rpm.setStatus(HttpStatus.OK.value());
				rpm.setStatusText("founded");
				rpm.setTotalElements(1);

			}
			return rpm;

		}

	 //Service to get all the themes submitted by client partners
		public Rp<List<Themes>> getAllThemesResponseMessage() 
		{
			List<Themes> list=userDAO.getAllThemesList();
			
			Rp<List<Themes>> rpm=new Rp<>();
			
			int size=list.size();
			
			if(size==0)
			{
				rpm.setResult(null);
				rpm.setStatus(HttpStatus.OK.value());
				rpm.setStatusText("OOPS!! No Themes been uploaded by Client Partner.Please try again later!!");

			}
			else
			{
				rpm.setResult(list);
				rpm.setStatus(HttpStatus.OK.value());
				rpm.setStatusText("List of all themes");
				rpm.setTotalElements(size);
			}
			return rpm;
		}
		public Rp<List<Ideas>> getIdeasBythemeid(long themeID) 
		{
			Rp<List<Ideas>> rpm=new Rp<>();

			List<Ideas> list=userDAO.getAllIdeas(themeID);

			if(list.isEmpty())
			{
				
				rpm.setResult(null);
				rpm.setStatus(HttpStatus.OK.value());
				rpm.setStatusText("no idea submitted");
			}
			else
			{
				rpm.setResult(list);
				rpm.setStatus(HttpStatus.OK.value());
				rpm.setStatusText("List all ideas");
				rpm.setTotalElements(list.size());
			}
			return rpm;
		}
		
//		Service to comment on an idea
		public Rp<Comments> commentAnIdeaResponseMessage(Comments comment) throws Exception
		{
			Comments dbComment=userDAO.saveComment(comment);
			
			if(dbComment==null) {
				throw new Exception("Some error occurred, Please try again");
			}
			Rp<Comments> rpm=new Rp<>();
			rpm.setResult(dbComment);
			rpm.setStatus(HttpStatus.CREATED.value());
			rpm.setStatusText("Your comment was added");
			rpm.setTotalElements(1);
			return rpm;
		}
	
		public Rp<List<Comments>> getCommentForIdeaResponseMessage(long ideaID) 
		{
			List<Comments> list=userDAO.getCommentsList(ideaID);
			
			Rp<List<Comments>> rpm=new Rp<>();
			
			int size=list.size();
			if(size==0)
			{
				rpm.setResult(null);
				rpm.setStatus(HttpStatus.OK.value());
				rpm.setStatusText("No Comments to the idea yet");

			}
			
			else
			{
				rpm.setResult(list);
				rpm.setStatus(HttpStatus.OK.value());
				rpm.setStatusText("List of all Comments");
				rpm.setTotalElements(size);

			}
			return rpm;

		}
		public Rp<Likes> likeAnIdeaResponseMessage(Likes likes) throws Exception
		{
			Likes like =userDAO.saveLikes(likes);
			
		Rp<Likes> rpm=new Rp<>();
			
			if(like==null) {
				
				throw new Exception("idea was not liked");
			}
			else
			{
				rpm.setResult(like);
				rpm.setStatus(HttpStatus.CREATED.value());
			
				if(!(like.getLikeValue()))
					rpm.setStatusText("dislike idea success");
				else
					rpm.setStatusText("like idea success");
				rpm.setTotalElements(1);
			}
			
			return rpm;
		}
		public Rp<List<User>> getLikesForIdeaResponseMessage(long ideaID) 
		{
			List<User> list=userDAO.getLikesForIdeaList(ideaID);
			
			Rp<List<User>> rpm=new Rp<>();
			
			int size=list.size();
			
			if(size==0)
			{
				
				rpm.setResult(null);
				rpm.setStatus(HttpStatus.OK.value());
				rpm.setStatusText("NO LIKES");
			}
			else
			{
				rpm.setResult(list);
				rpm.setStatus(HttpStatus.OK.value());
				rpm.setStatusText("LIKES_LIST");
				rpm.setTotalElements(size);

			}
			return rpm;

		}

}
