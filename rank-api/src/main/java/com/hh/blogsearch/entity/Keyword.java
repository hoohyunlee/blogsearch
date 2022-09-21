package com.hh.blogsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
/**
 * Keyword 검색 수 저장을 위한 Entity
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String query;

    @Column
    private long count;

    public void setKeyword(String query, long count) {
        this.query = query;
        this.count = count;
    }
}
