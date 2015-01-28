package ke.co.chaiportal.controller;

import java.util.List;
import java.util.Map;

import ke.co.chaiportal.model.Transaction;
import ke.co.chaiportal.model.UserLogin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

@Controller
@RequestMapping("/")
public class BaseController {

	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public String welcome(ModelMap model) {
		
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(clientConfig);
		
		WebResource webResource = client.resource("http://localhost:8080/restcomponent/statement/bosire");
		
		//ClientResponse response = webResource.accept("application/json").type("application/json");

		List<Transaction> listTransaction = webResource.get(new GenericType<List<Transaction>>(){});
		
		// for (Transaction transaction : listTransaction) {
		// System.out.println(transaction);
		// }
		//System.out.println();
		model.addAttribute("message",
				"Maven Web Project + Spring 3 MVC - welcome()");
		model.addAttribute("listTransaction", listTransaction);

		// Spring uses InternalResourceViewResolver and return back index.jsp
		return "index";

	}

	@RequestMapping(value = "/welcome/{name}", method = RequestMethod.GET)
	public String welcomeName(@PathVariable String name, ModelMap model) {

		model.addAttribute("message", "Maven Web Project + Spring 3 MVC - "
				+ name);
		return "index";

	}
	
	@RequestMapping(value = "/memberstatement", method = RequestMethod.GET)
	public String memberstatement(ModelMap model) {
		
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(clientConfig);
		
		//http://localhost:8080/vergesaccowebservices
		//WebResource webResource = client.resource("http://localhost:8080/restcomponent/statement/bosire");
		WebResource webResource = client.resource("http://localhost:8080/vergesaccowebservices/statement/samuel.githaiga");
		
		//ClientResponse response = webResource.accept("application/json").type("application/json");

		List<Transaction> listTransaction = webResource.get(new GenericType<List<Transaction>>(){});
		
		// for (Transaction transaction : listTransaction) {
		// System.out.println(transaction);
		// }
		//System.out.println();
		model.addAttribute("message",
				"Maven Web Project + Spring 3 MVC - welcome()");
		model.addAttribute("listTransaction", listTransaction);

		// Spring uses InternalResourceViewResolver and return back index.jsp
		return "memberstatement";

	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String main(ModelMap model) {
		model.addAttribute(new UserLogin());
		return "main";

	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@ModelAttribute("userLogin") UserLogin userLogin, ModelMap model, Map<String, Object> map) {
		
		System.out.println("User Name : "+userLogin.getUserName());
		System.out.println("Pass : "+userLogin.getPassword());
		
		map.put("userLogin", userLogin);
		
		if ((userLogin.getUserName() == null) || (userLogin.getUserName().equals("")) || (userLogin.getPassword().equals("")) || userLogin.getPassword() == null){
			return "main";
		}
		
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(clientConfig);
		
		WebResource webResource = null;
		webResource = client.resource("http://localhost:8080/vergesaccowebservices/statement/"+userLogin.getUserName());
		
		//ClientResponse response = webResource.accept("application/json").type("application/json");

		List<Transaction> listTransaction = null;
		if ((webResource != null)){
			try{
			listTransaction =		webResource.get(new GenericType<List<Transaction>>(){});
			} catch(UniformInterfaceException e){
				return "main";
			}
		} 
		
		model.addAttribute("listTransaction", listTransaction);
		model.addAttribute("userName", userLogin.getUserName());
		
		return "memberstatement";

	}


}