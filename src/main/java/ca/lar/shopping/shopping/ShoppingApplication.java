package ca.lar.shopping.shopping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.lar.shopping.shopping.model.MerchantShopping;
import ca.lar.shopping.shopping.model.ShoppingOption;


@SpringBootApplication
public class ShoppingApplication extends SpringBootServletInitializer {

	public static long sleep = 2;
	private static ConfigurableApplicationContext context;
	
	public static void main(String[] args) {
		SpringApplication.run(ShoppingApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return  builder.sources(ShoppingApplication.class);
	}
	
    public static void restart() {
        

        Thread thread = new Thread(() -> {
            context.close();
            context = SpringApplication.run(ShoppingApplication.class, null);
        });

        thread.setDaemon(false);
        thread.start();
    }
	
	@RestController
	@RequestMapping("/v1/shopping")
	public class MerchantController {
		
		@Autowired
		MerchantService merchantService;
		
		
		// http://localhost:8082/v1/shopping/1
		@GetMapping("/{id}")
		public ResponseEntity<MerchantShopping> findById(@PathVariable("id") Long id){
			
			Optional<MerchantShopping> model = merchantService.findAll()
					.stream()
					.filter(f -> f.getId().equals(id))
					.findFirst();
			
			if(model.isPresent()) {
				return new ResponseEntity<>(model.get(), HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}	
	}	

	@Service
	public class MerchantService {
		
		public List<MerchantShopping> findAll() {
			sleep= sleep +10; 
			
			if(sleep > 500 && sleep > 510) {
				ShoppingApplication.restart();
			}
			
			List<MerchantShopping> list = new ArrayList<>();
			Boolean isCad = Boolean.TRUE;
			try {
				Thread.sleep(sleep);
				System.out.print("");
			} catch (InterruptedException e) {
				System.out.print("");
			}
			for (int i = 0; i < sleep; i++) {
				if(Boolean.TRUE.equals(isCad)) {
					list.add(new MerchantShopping(Long.valueOf(i),ShoppingOption.ONLINE));
					isCad = null;
				} else if(Boolean.FALSE.equals(isCad)) {
					list.add(new MerchantShopping(Long.valueOf(i),ShoppingOption.STORE_AND_ONLINE));
					isCad = Boolean.TRUE;
				} else {
					list.add(new MerchantShopping(Long.valueOf(i),ShoppingOption.STORE));
					isCad = Boolean.FALSE;
				}
			}
			
			return list;
			
			/*return List.of(
					new MerchantShopping(1L, ShoppingOption.ONLINE),
					new MerchantShopping(2L,ShoppingOption.STORE_AND_ONLINE),
					new MerchantShopping(3L,ShoppingOption.STORE_AND_ONLINE),
					new MerchantShopping(4L, ShoppingOption.ONLINE),
					new MerchantShopping(5L,ShoppingOption.STORE_AND_ONLINE),
					new MerchantShopping(6L, ShoppingOption.STORE)
					);*/
		}
	}	
	
	


	

}
