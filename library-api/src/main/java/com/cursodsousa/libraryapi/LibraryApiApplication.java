package com.cursodsousa.libraryapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LibraryApiApplication {

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();  //serve para injetar a dependencia do modelmapper para usar onde querer
	}

	public static void main(String[] args) {
		SpringApplication.run(LibraryApiApplication.class, args);
	}
}
//ModelMapper: tem um site, e serve para transformar uma entidade em dto
//Ex: ModelMapper modelMapper = new ModelMapper();
//OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
