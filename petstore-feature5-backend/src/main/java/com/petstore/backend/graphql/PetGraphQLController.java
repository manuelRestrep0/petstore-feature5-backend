package com.petstore.backend.graphql;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class PetGraphQLController {

    // Simulamos una base de datos en memoria para testing
    private final Map<Long, Pet> pets = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public PetGraphQLController() {
        // Datos de prueba
        Pet pet1 = new Pet();
        pet1.setId(1L);
        pet1.setName("Buddy");
        pet1.setSpecies("Dog");
        pet1.setBreed("Golden Retriever");
        pet1.setAge(3);
        pet1.setPrice(500.0);
        pet1.setDescription("Friendly and energetic dog");
        pet1.setAvailable(true);
        pet1.setCreatedAt(LocalDateTime.now().toString());
        pet1.setUpdatedAt(LocalDateTime.now().toString());
        pets.put(1L, pet1);

        Pet pet2 = new Pet();
        pet2.setId(2L);
        pet2.setName("Whiskers");
        pet2.setSpecies("Cat");
        pet2.setBreed("Persian");
        pet2.setAge(2);
        pet2.setPrice(300.0);
        pet2.setDescription("Calm and affectionate cat");
        pet2.setAvailable(true);
        pet2.setCreatedAt(LocalDateTime.now().toString());
        pet2.setUpdatedAt(LocalDateTime.now().toString());
        pets.put(2L, pet2);
    }

    @QueryMapping
    public String hello() {
        return "¡Hola desde GraphQL! Pet Store API funcionando correctamente.";
    }

    @QueryMapping
    public String health() {
        return "GraphQL API is running! " + LocalDateTime.now();
    }

    @QueryMapping
    public List<Pet> pets() {
        return new ArrayList<>(pets.values());
    }

    @QueryMapping
    public Pet pet(@Argument String id) {
        return pets.get(Long.parseLong(id));
    }

    @MutationMapping
    public Pet createPet(@Argument Map<String, Object> input) {
        Pet pet = new Pet();
        long id = idGenerator.getAndIncrement();
        pet.setId(id);
        pet.setName((String) input.get("name"));
        pet.setSpecies((String) input.get("species"));
        pet.setBreed((String) input.get("breed"));
        pet.setAge(input.get("age") != null ? (Integer) input.get("age") : null);
        pet.setPrice(((Number) input.get("price")).doubleValue());
        pet.setDescription((String) input.get("description"));
        pet.setImageUrl((String) input.get("imageUrl"));
        pet.setAvailable(input.get("available") != null ? (Boolean) input.get("available") : true);
        pet.setCreatedAt(LocalDateTime.now().toString());
        pet.setUpdatedAt(LocalDateTime.now().toString());
        
        pets.put(id, pet);
        return pet;
    }

    @MutationMapping
    public Boolean deletePet(@Argument String id) {
        return pets.remove(Long.parseLong(id)) != null;
    }

    // Clase Pet interna para simplificar
    public static class Pet {
        private Long id;
        private String name;
        private String species;
        private String breed;
        private Integer age;
        private Double price;
        private String description;
        private String imageUrl;
        private Boolean available;
        private String createdAt;
        private String updatedAt;

        // Getters y setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getSpecies() { return species; }
        public void setSpecies(String species) { this.species = species; }
        
        public String getBreed() { return breed; }
        public void setBreed(String breed) { this.breed = breed; }
        
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
        
        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        
        public Boolean getAvailable() { return available; }
        public void setAvailable(Boolean available) { this.available = available; }
        
        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
        
        public String getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    }
}
