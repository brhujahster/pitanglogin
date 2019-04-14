package com.pitang.pitanglogin;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.pitang.pitanglogin.model.Phone;
import com.pitang.pitanglogin.model.User;
import com.pitang.pitanglogin.repository.Phones;
import com.pitang.pitanglogin.repository.Users;
import com.pitang.pitanglogin.service.UsersService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PitangloginApplicationTests {

	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private Phones phones;
	
	@Autowired
	private Users users;
	
	@Autowired
	private UsersService usersService;
	
	@Before
	public void setup() {
		
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(webApplicationContext);
		this.mockMvc = builder.build();
		createUserForTest();
	}
	
	@After
	public void finish() {
		phones.deleteAll();
		users.deleteAll();;
	}
	
	private void createUserForTest() {
		List<Phone> phones = new ArrayList<>();
		Phone phone = new Phone();
		phone.setArea_code(81);
		phone.setCountry_code("+ 55");
		phone.setNumber(123456789);

		User user = new User();
		user.setCreatedAt(LocalDate.now());
		user.setEmail("c.diego.f.costa@gmail.com");
		user.setFirstName("Diego");
		user.setLastName("Costa");
		user.setPassword("admin");
		user.setPhones(phones);
		
		usersService.save(user);
	}
	
	@Test
	public void deveCriarNovoUsuario() throws Exception {
		ResultMatcher ok = MockMvcResultMatchers.status()
                .isOk();
		
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{" + 
						"	\"firstName\": \"Diego\"," + 
						"	\"lastName\": \"Costa\"," + 
						"	\"email\": \"diego10@gmail.com\"," + 
						"	\"password\": \"admin\",\n" + 
						"	\"phones\": [\n" + 
						"		{\n" + 
						"			\"number\": 999999999," + 
						"			\"area_code\": 81," + 
						"			\"country_code\": \"+ 55\"" + 
						"		}" + 
						"	]" + 
						"	" + 
						"}");

		
		this.mockMvc.perform(builder).andExpect(ok);
	}
	
	@Test
	public void deveRetornarDadosDoUsuarioLogado() throws Exception {
		ResultMatcher ok = MockMvcResultMatchers.status()
                .isOk();
		
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/signin")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{" + 
						"	\"email\": \"c.diego.f.costa@gmail.com\"," + 
						"	\"password\": \"admin\"\n" + 
						"}");

		
		this.mockMvc.perform(builder).andExpect(ok);
		
	}
	
	@Test
	public void deveRetornarMensagemDeErroEmailJaExistente() throws Exception {
		ResultMatcher badRequest = MockMvcResultMatchers.status().is4xxClientError();
		
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{" + 
						"	\"firstName\": \"Diego\"," + 
						"	\"lastName\": \"Costa\"," + 
						"	\"email\": \"c.diego.f.costa@gmail.com\"," + 
						"	\"password\": \"admin\",\n" + 
						"	\"phones\": [\n" + 
						"		{\n" + 
						"			\"number\": 999999999," + 
						"			\"area_code\": 81," + 
						"			\"country_code\": \"+ 55\"" + 
						"		}" + 
						"	]" + 
						"	" + 
						"}");

		
		this.mockMvc.perform(builder).andExpect(badRequest);
	}
	
	@Test()
	public void deveRetornarMensagemDeErroCamposInvalidosOuNulls() throws Exception {
		ResultMatcher badRequest = MockMvcResultMatchers.status().is4xxClientError();
		
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{" +  
						"	\"lastName\": \"Costa\"," + 
						"	\"email\": \"c.diego.f.costa@gmail.com\"," + 
						"	\"password\": \"admin\",\n" + 
						"	\"phones\": [\n" + 
						"		{\n" + 
						"			\"number\": 999999999," + 
						"			\"area_code\": 81," + 
						"			\"country_code\": \"+ 55\"" + 
						"		}" + 
						"	]" + 
						"	" + 
						"}");

		
		this.mockMvc.perform(builder).andExpect(badRequest);
	}

}
