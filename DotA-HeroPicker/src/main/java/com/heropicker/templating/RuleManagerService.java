package com.heropicker.templating;

import java.io.File;
import java.io.OutputStream;
import java.util.Arrays;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.heropicker.io.RuleFileManager;


@Service
public class RuleManagerService {
	
	@Autowired
	private Environment env;
	
	public void triggerMvnInstall() {
		
		
		boolean autoTrigger = env.getProperty("config.mvn.autoTrigger").equals("true");
		
		if (autoTrigger) {
			String projectPath = System.getProperty("user.dir") + "/../integration-kjar/pom.xml";
			
			InvocationRequest request = new DefaultInvocationRequest();
			request.setPomFile( new File( projectPath ) );
			request.setGoals( Arrays.asList( "clean", "install" ) );

			Invoker invoker = new DefaultInvoker();
			String var = env.getProperty("config.mvn.path");
			invoker.setMavenHome(new File(var));
			try {
				invoker.execute( request );
			} catch (MavenInvocationException e) {
				e.printStackTrace();
			}
		}
	}

}
