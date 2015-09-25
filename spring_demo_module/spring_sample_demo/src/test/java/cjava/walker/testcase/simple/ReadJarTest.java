package cjava.walker.testcase.simple;


import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.junit.Test;
import org.springframework.util.ResourceUtils;

public class ReadJarTest {

	@Test
	public void testReadJar() throws Exception{
	    /* Enumeration<URL> resources = getClass().getClassLoader().getResources("com");
		 while(resources.hasMoreElements()){
			 URL url = resources.nextElement();
			 System.out.println(url);
		 }*/
		
		String path;
//		path = "org/springframework/web/servlet/DispatcherServlet.properties";
//		path = "org/springframework/web/servlet/DispatcherServlet.class";
		path = "org/springframework/web/servlet/";
		
		URL resource = getClass().getClassLoader().getResource(path);
		System.out.println(resource); //resource.toExternalForm()
//		System.out.println(resource.toURI().getScheme());//jar
//		System.out.println(resource.toURI().getSchemeSpecificPart());
		
		/*String jarf = resource.toURI().getSchemeSpecificPart().replaceAll(" ", "%20");
		URI uri = new URI(jarf);
		System.out.println(uri.getScheme());*/
		
		URLConnection con = resource.openConnection();
		
		JarFile jarFile;
		String jarFileUrl;
		String rootEntryPath;
		
		if(con instanceof JarURLConnection){
			JarURLConnection jarCon = (JarURLConnection) con;
			
			jarFile = jarCon.getJarFile();
			jarFileUrl = jarCon.getJarFileURL().toExternalForm();
			JarEntry jarEntry = jarCon.getJarEntry();
			rootEntryPath = (jarEntry != null ? jarEntry.getName() : "");
			
			System.out.println("JarEntry :" + jarEntry);
			System.out.println("JarFileURL :" + jarFileUrl);//Jar File url
			System.out.println("Root Entry path :" + rootEntryPath);
			
			/*Enumeration<JarEntry> entries = jarFile.entries();//列出jar中的所有entry
			System.out.println("===================");
			while(entries.hasMoreElements()){
				JarEntry entry = entries.nextElement();
				System.out.println(entry.getName());//
			}
			System.out.println("===================");*/
		}else{//???
			String urlFile = resource.getFile();
			int separatorIndex = urlFile.indexOf(ResourceUtils.JAR_URL_SEPARATOR);
			if (separatorIndex != -1) {
				jarFileUrl = urlFile.substring(0, separatorIndex);
				rootEntryPath = urlFile.substring(separatorIndex + ResourceUtils.JAR_URL_SEPARATOR.length());
				jarFile = getJarFile(jarFileUrl);
			}
			else {
				jarFile = new JarFile(urlFile);
				jarFileUrl = urlFile;
				rootEntryPath = "";
			}
		}
	}
	
	protected JarFile getJarFile(String jarFileUrl) throws IOException {
		if (jarFileUrl.startsWith(ResourceUtils.FILE_URL_PREFIX)) {
			try {
				return new JarFile(ResourceUtils.toURI(jarFileUrl).getSchemeSpecificPart());
			}
			catch (URISyntaxException ex) {
				// Fallback for URLs that are not valid URIs (should hardly ever happen).
				return new JarFile(jarFileUrl.substring(ResourceUtils.FILE_URL_PREFIX.length()));
			}
		}
		else {
			return new JarFile(jarFileUrl);
		}
	}
	
}
