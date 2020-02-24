package martin.site.springboot.reactor.app.models.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import martin.site.springboot.reactor.app.models.dao.CategoriaDao;
import martin.site.springboot.reactor.app.models.documents.Categoria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CategoriaServiceImpl implements CategoriaService {

	@Autowired
	private CategoriaDao dao;

	@Override
	public Flux<Categoria> findAll() {
		return dao.findAll();
	}

	@Override
	public Mono<Categoria> findById(String id) {
		return dao.findById(id);
	}

	@Override
	public Mono<Categoria> save(Categoria categoria) {
		return dao.save(categoria);
	}

	@Override
	public Mono<Categoria> findByNombre(String nombre) {
		return dao.findByNombre(nombre);
	}


}
