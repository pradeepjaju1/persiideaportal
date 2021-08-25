package com.ideaportal.services;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ideaportal.dao.ProductManagerdao;
import com.ideaportal.models.Ideas;
import com.ideaportal.models.Rp;
import com.ideaportal.models.ThemeIdeaFiles;

@Service
public class ProductManagerService {

	@Autowired
	ProductManagerdao pmDAO;
	

	
	public Rp<Ideas> createNewIdeaResponseMessage(final Ideas ideas) throws IOException, URISyntaxException {
         Rp<Ideas> responseMessage=new Rp<>();
		responseMessage.setResult(pmDAO.createNewIdeaObject(ideas));
		responseMessage.setStatus(HttpStatus.CREATED.value());
		
		

		return responseMessage;
	}
	 public void saveArtifacts(final List<ThemeIdeaFiles> artifactList, final long ideaID) {
	        this.pmDAO.saveArtifacts(artifactList, ideaID);
	    }
	 

		public Rp<List<Ideas>> getMySubmittedIdeasResponseMessage(long userID) 
		{
			Rp<List<Ideas>> responseMessage=new Rp<>();
			
			List<Ideas> list=pmDAO.getMyIdeasList(userID);
			
			int size=list.size();
			
			if(size==0)
			{
				responseMessage.setResult(null);
				responseMessage.setStatusText("No themes were found");
			}
			else
			{
				responseMessage.setResult(list);
				responseMessage.setStatusText("List all ideas");
			}
			responseMessage.setTotalElements(list.size());
			responseMessage.setStatus(HttpStatus.OK.value());
			return responseMessage;
		}
		
}
