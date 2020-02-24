package martin.site.springboot.reactor.app.models.service;

import martin.site.springboot.reactor.app.models.documents.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {
	public Flux<Producto> findAll();
	public Flux<Producto> findAllconNombreUpperCase();
	public Flux<Producto> findAllconNombreUpperCaseRepeat();
	public Mono<Producto> findById(String id);
	public Mono<Producto> save(Producto producto);
	public Mono<Void> delete(Producto producto);
	public Mono<Producto> findByNombre(String nombre);
}
