package com.collabera.mapper;

import com.collabera.dto.BookDTO;
import com.collabera.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    Book save(BookDTO dto);

    void update(BookDTO dto, @MappingTarget Book book);

    BookDTO from(Book book);
}
