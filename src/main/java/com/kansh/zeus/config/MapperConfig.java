package com.kansh.zeus.config;

import com.kansh.zeus.domain.dto.exercises.ExercisesDto;
import com.kansh.zeus.domain.dto.exercises.SupersetOutputDto;
import com.kansh.zeus.domain.dto.exercises.SupersetsDto;
import com.kansh.zeus.domain.dto.friends.PostDto;
import com.kansh.zeus.domain.entities.exercises.ExercisesEntity;
import com.kansh.zeus.domain.entities.exercises.SupersetsEntity;
import com.kansh.zeus.domain.entities.friends.PostEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        modelMapper.addMappings(new PropertyMap<PostEntity, PostDto>() {
            @Override
            protected void configure() {
                map().setCreatedByFirstName(source.getCreatedBy().getFirstName());
                map().setCreatedByLastName(source.getCreatedBy().getLastName());
                map().setCreatedByPhoto(source.getCreatedBy().getPhoto());
                map().setCreatedById(source.getCreatedBy().getId());
            }
        });

        modelMapper.addMappings(new PropertyMap<PostDto, PostEntity>() {
            @Override
            protected void configure() {
            }
        });

        modelMapper.addMappings(new PropertyMap<ExercisesEntity, ExercisesDto>() {
            @Override
            protected void configure() {
                map().setCreatedBy(source.getCreatedBy().getId());
            }
        });

        modelMapper.addMappings(new PropertyMap<SupersetsEntity, SupersetsDto>() {
            @Override
            protected void configure() {
                map().setCreatedBy(source.getCreatedBy().getId());
            }
        });

        modelMapper.addMappings(new PropertyMap<SupersetsEntity, SupersetOutputDto>() {
            @Override
            protected void configure() {
                map().setCreatedBy(source.getCreatedBy().getId());
            }
        });

        return modelMapper;
    }
}
