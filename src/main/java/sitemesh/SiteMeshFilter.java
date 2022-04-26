package sitemesh;

import javax.servlet.annotation.WebFilter;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

@WebFilter("/*")
public class SiteMeshFilter extends ConfigurableSiteMeshFilter {
	
	@Override
	//path는 url임
	protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
		builder
		.addDecoratorPath("/*","/layout/head.jsp")
		.addExcludedPath("/member/readId")
		.addExcludedPath("/member/pictureForm")
		.addExcludedPath("/member/picturePro");
		
	}

}
   

