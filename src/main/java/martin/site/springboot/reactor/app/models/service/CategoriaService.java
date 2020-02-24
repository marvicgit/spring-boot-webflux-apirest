package martin.site.springboot.reactor.app.models.service;

import martin.site.springboot.reactor.app.models.documents.Categoria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoriaService {
	public Flux<Categoria> findAll();
	public Mono<Categoria> findById(String id);
	public Mono<Categoria> save(Categoria categoria);
	public Mono<Categoria> findByNombre(String nombre);
}
