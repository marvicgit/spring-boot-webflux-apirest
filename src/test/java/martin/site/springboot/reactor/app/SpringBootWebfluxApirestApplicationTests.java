package martin.site.springboot.reactor.app;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import martin.site.springboot.reactor.app.models.documents.Categoria;
import martin.site.springboot.reactor.app.models.documents.Producto;
import martin.site.springboot.reactor.app.models.service.CategoriaService;
import martin.site.springboot.reactor.app.models.service.ProductoService;
import reactor.core.publisher.Mono;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class SpringBootWebfluxApirestApplicationTests {

	@Autowired
	private ProductoService service;
	
	@Autowired
	private CategoriaService serviceCategoria;
	
	@Value("${config.base.endpoint}")
	private String url;
	
	@Autowired
	private WebTestClient client;
	
	@Test
	public void listarTest() {
		client.get()
		.uri(url)
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBodyList(Producto.class)
		.consumeWith(response -> {
			List<Producto> productos = response.getResponseBody();
			productos.forEach(p -> {
				System.out.println(p.getNombre());
			});
			assertThat(productos.size() > 0).isTrue();
		});
		//.hasSize(9);
	}
	
	@Test
	public void verTest() {
		Producto producto =  service.findByNombre("TV Panasonic Pantalla LCD").block();
		client.get()
		.uri(url + "/{id}", Collections.singletonMap("id", producto.getId()))
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody(Producto.class)
		.consumeWith(response -> {
			Producto productos = response.getResponseBody();
			assertThat(productos.getId()).isNotEmpty();
			assertThat(productos.getId().length() > 0).isTrue();
			assertThat(productos.getNombre()).isEqualTo("TV Panasonic Pantalla LCD");
		});
		/*.expectBody()
		.jsonPath("$.id").isNotEmpty()
		.jsonPath("$.nombre").isEqualTo("TV Panasonic Pantalla LCD");*/
		
		
	}
	
	@Test
	public void crearTest() {
		Categoria categoria = serviceCategoria.findByNombre("Muebles").block();
		Producto producto =  new Producto("Mesita de madera", 100.00, categoria);
		
		client.post()
		.uri(url)
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.body(Mono.just(producto), Producto.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody(new ParameterizedTypeReference<LinkedHashMap<String, Object>>(){})
		.consumeWith(response -> {
			Object o = response.getResponseBody().get("producto");
			Producto p = new ObjectMapper().convertValue(o, Producto.class);
			assertThat(p.getId()).isNotEmpty();
			assertThat(p.getId().length() > 0).isTrue();
			assertThat(p.getNombre()).isEqualTo("Mesita de madera");
			assertThat(p.getCategoria().getNombre()).isEqualTo("Muebles");
		});
		/*.expectBody()
		.jsonPath("$.id").isNotEmpty()
		.jsonPath("$.nombre").isEqualTo("Mesita de madera")
		.jsonPath("$.categoria.nombre").isEqualTo("Muebles");*/
			
	}
	
	@Test
	public void editarTest() {
		Producto producto =  service.findByNombre("Hewlett Packard Multifuncional").block();
		Categoria categoria = serviceCategoria.findByNombre("electronico").block();
		Producto productoEditado =  new Producto("TV Samsung Led 55", 1000.00, categoria);
		client.put()
		.uri(url + "/{id}",  Collections.singletonMap("id", producto.getId()))
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.body(Mono.just(productoEditado), Producto.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		/*.expectBody(Producto.class)
		.consumeWith(response -> {
			Producto productos = response.getResponseBody();
			assertThat(productos.getId()).isNotEmpty();
			assertThat(productos.getId().length() > 0).isTrue();
			assertThat(productos.getNombre()).isEqualTo("Mesita de madera");
			assertThat(productos.getCategoria().getNombre()).isEqualTo("Muebles");
		});*/
		.expectBody()
		.jsonPath("$.id").isNotEmpty()
		.jsonPath("$.nombre").isEqualTo("TV Samsung Led 55")
		.jsonPath("$.categoria.nombre").isEqualTo("electronico");
			
	}
	
	@Test
	public void eliminarTest() {
		Producto producto =  service.findByNombre("Apple iPod").block();
		client.delete()
		.uri(url + "/{id}",  Collections.singletonMap("id", producto.getId()))
		.exchange()
		.expectStatus().isNoContent()
		.expectBody().isEmpty();
		
		client.get()
		.uri(url + "/{id}",  Collections.singletonMap("id", producto.getId()))
		.exchange()
		.expectStatus().isNotFound()
		.expectBody().isEmpty();

			
	}

}
