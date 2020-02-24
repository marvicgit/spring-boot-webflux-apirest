package martin.site.springboot.reactor.app.models.dao;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import martin.site.springboot.reactor.app.models.documents.Producto;
import reactor.core.publisher.Mono;

public interface ProductoDao extends ReactiveMongoRepository<Producto, String> {

	public Mono<Producto> findByNombre(String nombre);
	
	@Query("{'nombre':?0}")
	public Mono<Producto> obtenerPorNombre(String nombre);
}
