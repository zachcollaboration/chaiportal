package ke.co.chaiportal.controller;

import java.util.List;
import java.util.Map;

import javax.ws.rs.PathParam;

import ke.co.chaiportal.model.LoanAccount;
import ke.co.chaiportal.model.LoanAccountTransaction;
import ke.co.chaiportal.model.StatementAccount;
import ke.co.chaiportal.model.StatementAccountTransaction;
import ke.co.chaiportal.model.Transaction;
import ke.co.chaiportal.model.UserLogin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

@Controller
@SessionAttributes("userLogin")
@RequestMapping("/")
public class BaseController {

	@Value("${host.url}")
	private String hostUrl;

	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public String welcome(ModelMap model) {

		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
				Boolean.TRUE);
		Client client = Client.create(clientConfig);

		// WebResource webResource =
		// client.resource(hostUrl+"/restcomponent/statement/bosire");
		WebResource webResource = client.resource(hostUrl
				+ "/vergesaccowebservices/statement/"
				+ ((UserLogin) model.get("userLogin")).getUserName());
		// ClientResponse response =
		// webResource.accept("application/json").type("application/json");

		List<Transaction> listTransaction = webResource
				.get(new GenericType<List<Transaction>>() {
				});

		// for (Transaction transaction : listTransaction) {
		// System.out.println(transaction);
		// }
		// System.out.println();
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

	@RequestMapping(value = { "/accountsSummary", "/login" }, method = RequestMethod.GET)
	public String memberstatement(ModelMap model) {

		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
				Boolean.TRUE);
		Client client = Client.create(clientConfig);

		// http://192.168.1.207:8080/vergesaccowebservices
		// WebResource webResource =
		// client.resource(hostUrl+"/restcomponent/statement/bosire");
		// WebResource webResource =
		// client.resource(hostUrl+"/vergesaccowebservices/statement/"+((UserLogin)model.get("userLogin")).getUserName());
		WebResource webResource = client.resource(hostUrl
				+ "/vergesaccowebservices/statement/accounts/"
				+ ((UserLogin) model.get("userLogin")).getUserName());
		// ClientResponse response =
		// webResource.accept("application/json").type("application/json");

		List<StatementAccount> listStatementAccount = webResource
				.get(new GenericType<List<StatementAccount>>() {
				});

		// for (Transaction transaction : listTransaction) {
		// System.out.println(transaction);
		// }
		// System.out.println();
		model.addAttribute("message",
				"Maven Web Project + Spring 3 MVC - welcome()");
		model.addAttribute("listStatementAccount", listStatementAccount);
		model.addAttribute("userName",
				((UserLogin) model.get("userLogin")).getUserName());

		// Spring uses InternalResourceViewResolver and return back index.jsp
		// return "memberstatement";
		
		WebResource webResourceLoans = null;
		webResourceLoans = client.resource(hostUrl
				+ "/vergesaccowebservices/statement/loansbalances/"
				+ ((UserLogin) model.get("userLogin")).getUserName());

		List<LoanAccount> listLoanAccount = null;
		if ((webResourceLoans != null)) {
			try {
				listLoanAccount = webResourceLoans.get(new GenericType<List<LoanAccount>>() {
						});
			} catch (UniformInterfaceException e) {
				return "main";
			}
		}
		model.addAttribute("listLoanAccount", listLoanAccount);
		
		return "accountsSummary";

	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String main(ModelMap model) {
		model.addAttribute(new UserLogin());
		return "main";

	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@ModelAttribute("userLogin") UserLogin userLogin,
			ModelMap model, Map<String, Object> map) {

		System.out.println("User Name : " + userLogin.getUserName());
		// System.out.println("Pass : "+userLogin.getPassword());

		map.put("userLogin", userLogin);

		if ((userLogin.getUserName() == null)
				|| (userLogin.getUserName().equals(""))
				|| (userLogin.getPassword().equals(""))
				|| userLogin.getPassword() == null) {

			return "main";
		}

		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
				Boolean.TRUE);
		Client client = Client.create(clientConfig);

		WebResource webResource = null;
		// webResource =
		// client.resource(hostUrl+"/vergesaccowebservices/statement/"+userLogin.getUserName());

		webResource = client.resource(hostUrl
				+ "/vergesaccowebservices/statement/accounts/"
				+ userLogin.getUserName());

		// ClientResponse response =
		// webResource.accept("application/json").type("application/json");

		List<StatementAccount> listStatementAccount = null;
		if ((webResource != null)) {
			try {
				listStatementAccount = webResource
						.get(new GenericType<List<StatementAccount>>() {
						});
			} catch (UniformInterfaceException e) {
				return "main";
			}
		}

		model.addAttribute("listStatementAccount", listStatementAccount);
		model.addAttribute("userName", userLogin.getUserName());

		model.addAttribute(userLogin);

		// Add Loans

		WebResource webResourceLoans = null;
		webResourceLoans = client.resource(hostUrl
				+ "/vergesaccowebservices/statement/loansbalances/"
				+ userLogin.getUserName());

		List<LoanAccount> listLoanAccount = null;
		if ((webResourceLoans != null)) {
			try {
				listLoanAccount = webResourceLoans.get(new GenericType<List<LoanAccount>>() {
						});
			} catch (UniformInterfaceException e) {
				return "main";
			}
		}
		model.addAttribute("listLoanAccount", listLoanAccount);
		
		
		return "accountsSummary";

	}

	@RequestMapping(value = "/memberstatement/{memberAccountId}", method = RequestMethod.GET)
	public String getMemberTransactions(
			@PathVariable("memberAccountId") Long memberAccountId,
			ModelMap model) {

		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
				Boolean.TRUE);
		Client client = Client.create(clientConfig);

		// http://192.168.1.207:8080/vergesaccowebservices
		// WebResource webResource =
		// client.resource(hostUrl+"/restcomponent/statement/bosire");
		// WebResource webResource =
		// client.resource(hostUrl+"/vergesaccowebservices/statement/"+((UserLogin)model.get("userLogin")).getUserName());

		// /accounts/{memberAccountId}/{isLoan}
		System.out.println("PPPPPPPPPPP " + model.get("memberAccountId"));
		System.out.println("PPPPPPPPPPP " + memberAccountId);

		WebResource webResource = client.resource(hostUrl
				+ "/vergesaccowebservices/statement/accounts/"
				+ memberAccountId + "/N");
		// ClientResponse response =
		// webResource.accept("application/json").type("application/json");

		List<StatementAccountTransaction> listStatementAccountTransaction = webResource
				.get(new GenericType<List<StatementAccountTransaction>>() {
				});

		// for (Transaction transaction : listTransaction) {
		// System.out.println(transaction);
		// }
		// System.out.println();
		model.addAttribute("message",
				"Maven Web Project + Spring 3 MVC - welcome()");
		model.addAttribute("listStatementAccountTransaction",
				listStatementAccountTransaction);
		model.addAttribute("userName",
				((UserLogin) model.get("userLogin")).getUserName());

		// Spring uses InternalResourceViewResolver and return back index.jsp
		return "memberstatement";

	}
	
	//loanstatement
	@RequestMapping(value = "/loanstatement/{loanApplicationId}", method = RequestMethod.GET)
	public String getMemberLoanTransactions(
			@PathVariable("loanApplicationId") Long loanApplicationId,
			ModelMap model) {

		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
				Boolean.TRUE);
		Client client = Client.create(clientConfig);

		// http://192.168.1.207:8080/vergesaccowebservices
		// WebResource webResource =
		// client.resource(hostUrl+"/restcomponent/statement/bosire");
		// WebResource webResource =
		// client.resource(hostUrl+"/vergesaccowebservices/statement/"+((UserLogin)model.get("userLogin")).getUserName());

		// /accounts/{memberAccountId}/{isLoan}
		System.out.println("PPPPPPPPPPP " + loanApplicationId);

		WebResource webResource = client.resource(hostUrl
				+ "/vergesaccowebservices/statement/loanstransactions/"
				+ loanApplicationId);
		// ClientResponse response =
		// webResource.accept("application/json").type("application/json");

		List<LoanAccountTransaction> listLoanAccountTransaction = webResource
				.get(new GenericType<List<LoanAccountTransaction>>() {
				});

		// for (Transaction transaction : listTransaction) {
		// System.out.println(transaction);
		// }
		// System.out.println();
		model.addAttribute("message",
				"Maven Web Project + Spring 3 MVC - welcome()");
		model.addAttribute("listLoanAccountTransaction",
				listLoanAccountTransaction);
		model.addAttribute("userName",
				((UserLogin) model.get("userLogin")).getUserName());

		// Spring uses InternalResourceViewResolver and return back index.jsp
		return "loanstatement";

	}

}