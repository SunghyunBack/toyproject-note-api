package com.sunghyun.note.domain;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Entity 
public class Note {
    
    public static final int TITLE_MAX_LENGTH=100;
    public static final int TAG_MAX_LENGTH=20;

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length =TITLE_MAX_LENGTH)
    private String title;

    @Column(nullable = false, length = 10_000)
    private String content;

    @Column(nullable =false, updatable =false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ElementCollection 
    @CollectionTable(name = "note_tag", joinColumns=@JoinColumn(name="note_id"))
@Column(name="tag", nullable=false, length =TAG_MAX_LENGTH)
private Set<String> tags = new HashSet<>();


    protected Note(){

    }
    public Note(String title, String content){
            this(title, content, Set.of());

    }  

    public Note(String title, String content, Set<String> tags){
validateTitle(title);
this.title=title;
this.content = (content ==null)?"":content;
this.tags = normalizeTags(tags);
    }

private static Set<String> normalizeTags(Set<String> tags){
    Set<String> result = new HashSet<>();
    if(tags ==null) {
        return result;
    }
    for(String tag : tags){
        validateTag(tag);
        result.add(tag.trim());
    }
    return result;
}
private static void validateTag(String tag){
    if(tag==null ||tag.isBlank()){
        throw new IllegalArgumentException("태그는 비어있을 수 없습니다");

    }
    if(tag.trim().length() > TAG_MAX_LENGTH){
        throw new IllegalArgumentException("태그는" +TAG_MAX_LENGTH + "자 이하여야 합니다.");
    }
}

    private static void validateTitle(String title){
        if(title ==null || title.isBlank()){
            throw new IllegalArgumentException("제목은 비어 있을 수 없습니다.");
        }
        if(title.length()>TITLE_MAX_LENGTH){
            throw new IllegalArgumentException("제목은" +TITLE_MAX_LENGTH + "자 이하여야 합니다.");
        
        }
    }
    @PrePersist
    void onCreate(){
        LocalDateTime now = LocalDateTime.now();
        this.createdAt =now;
        this.updatedAt =now;
    }
@PreUpdate 
void onUpdate(){
    this.updatedAt = LocalDateTime.now();
}

public Set<String> getTags(){
    return Collections.unmodifiableSet(tags);
}
public Long getId(){return id;}
public String getTitle(){return title;}
public String getContent(){return content;}
public LocalDateTime getCreatedAt(){return createdAt;}
public LocalDateTime getUpdatedAt(){return updatedAt;}



}
