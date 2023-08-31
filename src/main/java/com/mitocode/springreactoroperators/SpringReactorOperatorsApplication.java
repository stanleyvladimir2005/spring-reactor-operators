package com.mitocode.springreactoroperators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringReactorOperatorsApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SpringReactorOperatorsApplication.class);
    private static List<String> platos = new ArrayList<>();

    private static List<String> clientes = new ArrayList<>();

    public static void main(String[] args) {
        platos.add("Hamburguesa");
        platos.add("Pizza");
        platos.add("Soda");
        clientes.add("Jaime");
        clientes.add("Code");
        SpringApplication.run(SpringReactorOperatorsApplication.class, args);
    }

    public void crearMono() {
        Mono<Integer> monoNumero = Mono.just(7); //Creamos un flujo de un elemento
        monoNumero.subscribe(x -> log.info("Numero: " + x));
    }

    public void crearFlux() {
        Flux<String> fxPlatos = Flux.fromIterable(platos);
        fxPlatos.subscribe(x -> log.info(x)); //Devolvemos el cada elemento individual de la lista
        fxPlatos
                .collectList() //Mono<List<String>>
                .subscribe(lista -> log.info(lista.toString()));
    }

    public void m1doOnNext() {
        Flux<String> fxPlatos = Flux.fromIterable(platos);
        fxPlatos
                .doOnNext(p -> log.info(p))  //este metodo se usa para evaluar el siguiente contenido del flujo
                .subscribe();
    }

    public void m2map() { //map permite transformar directamente
        Flux<String> fxPlatos = Flux.fromIterable(platos);
        fxPlatos
                .map(p -> "Plato: " + p)
                .subscribe(p -> log.info(p));
    }

    public void m3flatMap() { // // permite transformar pero te obliga a retornar un flux o un mono
        Mono.just("Jaime")
                .flatMap(x -> Mono.just(30))
                .subscribe(p -> log.info(p.toString()));
    }

    public void m4range() {
        Flux<Integer> fx1 = Flux.range(0, 10);
        fx1.map(x -> {//más lineas
                    return x + 1;
                })
                .subscribe(x -> log.info("N: " + x));
    }

    public void m5delayElements() throws InterruptedException {
        Flux.range(0, 10)
                .delayElements(Duration.ofSeconds(2))
                .doOnNext(i -> log.info(i.toString()))
                .subscribe();
        Thread.sleep(20000);
    }

    public void m6zipWith() {
        Flux<String> fxPlatos = Flux.fromIterable(platos);
        Flux<String> fxClientes = Flux.fromIterable(clientes);
        fxPlatos
                .zipWith(fxClientes, (p, c) -> String.format("Flux1: %s, Flux2: %s", p, c)) //crea  flujos uniendo 1 elemento de platos y cliente (Flux1: Hamburguesa, Flux2: Jaime)
                .subscribe(x -> log.info(x));
    }

    public void m7merge() {
        Flux<String> fxPlatos = Flux.fromIterable(platos);
        Flux<String> fxClientes = Flux.fromIterable(clientes);
        Flux.merge(fxPlatos, fxClientes) //Une los dos flux
                .subscribe(x -> log.info(x));
    }

    public void m8filter() {
        Flux<String> fxPlatos = Flux.fromIterable(platos);
        fxPlatos.filter(p -> {
                    return p.startsWith("H");
                })
                .subscribe(x -> log.info(x));
    }

    public void m9takeLast() {
        Flux<String> fxPlatos = Flux.fromIterable(platos);
        fxPlatos
                .takeLast(2)
                .subscribe(x -> log.info(x));
    }

    public void m10take() {
        Flux<String> fxPlatos = Flux.fromIterable(platos);
        fxPlatos
                .take(2)
                .subscribe(x -> log.info(x));
    }

    public void m11DefaultIfEmpty() {
        platos = new ArrayList<>();
        Flux<String> fxPlatos = Flux.fromIterable(platos);
        fxPlatos
                .defaultIfEmpty("LISTA VACIA")
                .subscribe(x -> log.info(x));

        //Mono.empty()
        //Flux.empty()
    }

    public void m12onErrorReturn() {
        Flux<String> fxPlatos = Flux.fromIterable(platos);
        fxPlatos
                .doOnNext(p -> {
                    //calculo aritmetico
                    throw new ArithmeticException("MAL CALCULO");
                })
                .onErrorMap(ex -> new Exception(ex.getMessage()))
                .subscribe(x -> log.info(x));
    }

    public void m13retry() {
        Flux<String> fxPlatos = Flux.fromIterable(platos);
        fxPlatos
                .doOnNext(p -> {
                    log.info("intentando....");
                    throw new ArithmeticException("MAL CALCULO");
                })
                .retry(3)
                .onErrorReturn("ERROR!")
                .subscribe(x -> log.info(x));
    }



    @Override
    public void run(String... args) throws Exception {
        //crearMono();
        //crearFlux();
        //m1doOnNext();
        //m2map();
        m3flatMap();
        //m4range();
        //m5delayElements();
        //m6zipWith();
        //m7merge();
        //m8filter();
        //m9takeLast();
        //m10take();
        //m11DefaultIfEmpty();
        //m12onErrorReturn();
        //m13retry();
    }
}